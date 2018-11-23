package com.example.separation.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by zhankun on 2018/11/21.
 * Node Cache与Path Cache类似，Node Cache只是监听某一个特定的节点。它涉及到下面的三个类：
 */
public class NodeCacheDemo {

    private static final String PATH = "/example/cache";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181,localhost:2182,localhost:2183", new ExponentialBackoffRetry(1000, 3));
        client.start();
        client.create().creatingParentsIfNeeded().forPath(PATH);
        NodeCache cache = new NodeCache(client, PATH);
        NodeCacheListener listener = () -> {
            ChildData data = cache.getCurrentData();
            if (null != data) {
                System.out.println("节点数据：" + new String(cache.getCurrentData().getData()));
            } else {
                System.out.println("节点被删除!");
            }
        };
        cache.getListenable().addListener(listener);
        cache.start();
        client.setData().forPath(PATH, "01".getBytes());
        Thread.sleep(100);
        System.out.println("当前节点数据是:"+new String(cache.getCurrentData().getData()));
        client.setData().forPath(PATH, "02".getBytes());
        Thread.sleep(100);
        System.out.println("当前节点数据是:"+new String(cache.getCurrentData().getData()));
        client.delete().deletingChildrenIfNeeded().forPath(PATH);
        Thread.sleep(1000 * 2);
        cache.close();
        client.close();
        System.out.println("OK!");
    }
}
