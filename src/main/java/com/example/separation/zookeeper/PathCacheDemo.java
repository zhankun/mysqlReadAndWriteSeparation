package com.example.separation.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * Created by zhankun on 2018/11/21.
 *
 * Path Cache用来监控一个ZNode的子节点. 当一个子节点增加， 更新，删除时，
 * Path Cache会改变它的状态， 会包含最新的子节点， 子节点的数据和状态，而状态的更变将通过PathChildrenCacheListener通知
 */
public class PathCacheDemo {

    private static final String PATH = "/example/pathCache";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181,localhost:2182,localhost:2183", new ExponentialBackoffRetry(1000, 3));
        client.start();
        PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start();
        PathChildrenCacheListener cacheListener = (client1, event) -> {
            System.out.println("事件类型：" + event.getType());
            if (null != event.getData()) {
                System.out.println("节点数据：" + event.getData().getPath() + " = " + new String(event.getData().getData()));
            }
        };
        cache.getListenable().addListener(cacheListener);
        client.create().creatingParentsIfNeeded().forPath("/example/pathCache/test01", "01".getBytes());
        Thread.sleep(10);
        client.create().creatingParentsIfNeeded().forPath("/example/pathCache/test02", "02".getBytes());
        Stat stat = client.checkExists().forPath("/example/pathCache/test02");
        Thread.sleep(10);
        cache.getCurrentData().forEach(data -> System.out.println("getCurrentData:" + data.getPath() + " = " + new String(data.getData())));
        client.setData().forPath("/example/pathCache/test01", "01_V2".getBytes());
        Thread.sleep(10);
        cache.getCurrentData().forEach(data -> System.out.println("getCurrentData:" + data.getPath() + " = " + new String(data.getData())));
        client.delete().forPath("/example/pathCache/test01");
        Thread.sleep(10);
        client.delete().forPath("/example/pathCache/test02");
        Thread.sleep(1000 * 5);
        cache.close();
        client.close();
        System.out.println("OK!");
    }
}
