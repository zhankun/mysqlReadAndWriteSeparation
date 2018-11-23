package com.example.separation.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zhankun on 2018/11/22.
 */
public class LockTest {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181,localhost:2182,localhost:2183")
                .sessionTimeoutMs(30000)
                .connectionTimeoutMs(30000)
                .retryPolicy(retry)
                //.namespace("esdDistributedLock") //每个业务对应的独立命名空间
                .build();
        client.start();
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath("/esdDistributedLock");
        String myZnode = client.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/esdDistributedLock/locktest&subject1_lock_", new byte[0]);
        client.getChildren().forPath("/esdDistributedLock").forEach(e -> System.out.println("------------------\r\n"+e));
        String mySecondZnode = client.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/esdDistributedLock/locktest&subject1_lock_", new byte[1]);
        ArrayList<String> list = new ArrayList<>();
        client.getChildren().forPath("/esdDistributedLock").forEach(e -> {
            list.add(e);
        });
        Collections.sort(list);
        //获取比自己小一个的节点，并监听这个节点，当这个节点被删除池，则当前节点可以获得锁
        String substring = mySecondZnode.substring(mySecondZnode.lastIndexOf("/") + 1);
        String waitNode = list.get(Collections.binarySearch(list, substring) - 1);
        Stat stat = client.checkExists().forPath("/esdDistributedLock/"+waitNode);
        if (stat != null){
            NodeCache cache = new NodeCache(client, "/esdDistributedLock/"+waitNode);
            NodeCacheListener listener = () -> {
                ChildData data = cache.getCurrentData();
                if (null != data) {
                    System.out.println("节点数据：" + new String(cache.getCurrentData().getData()));
                } else {
                    System.out.println("节点被删除!");
                    countDownLatch.countDown();
                }
            };
            cache.getListenable().addListener(listener);
            cache.start();
            Runnable runnable = () -> {
                try {
                    Thread.sleep(60000);
                    client.delete().withVersion(-1).forPath("/esdDistributedLock/"+waitNode);
                    System.out.println("=============节点删除成功============");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            new Thread(runnable).start();
            countDownLatch.await();
            cache.close();
            client.close();
            System.out.println("---------------哈哈哈哈，我是第一，我获得锁了-------------------");
        }


        /***
         * 枷锁较简单实现
         */
       /* InterProcessMutex lock = new InterProcessMutex(client, "");
        if ( lock.acquire(10, TimeUnit.SECONDS) )
        {
            try
            {
                // do some work inside of the critical section here
            }
            finally
            {
                lock.release();
            }
        }*/
    }
}
