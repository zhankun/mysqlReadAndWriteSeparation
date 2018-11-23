package com.example.separation.zookeeper;

import com.example.separation.entity.User;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by zhankun on 2018/11/20.
 * 使用Curator来进行操作
 */
public class MyZookeeperTest {

    public void myTest() throws Exception {
        //重试策略
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181,localhost:2182,localhost:2183")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retry)
                .namespace("myLock") //每个业务对应的独立命名空间
                .build();
        //启动客户端
        client.start();
        /*
        EPHEMERAL 临时节点
        EPHEMERAL_SEQUENTIAL 临时并且带序列号
        PERSISTENT 持久化节点
        PERSISTENT_SEQUENTIAL 持久化并且带序列号
        */
        //创建数据节点,指定创建模式,附带初始化数据
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/myFirstPath/first","init".getBytes());
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/myFirstPath/second","init".getBytes());
        //读取节点数据
        String data = client.getData().forPath("/myFirstPath").toString();
        System.out.println(String.format("myFirstPath这个节点里面的数据是:%s",data));
        //更新一个节点的数据内容
        client.setData().forPath("/myFirstPath","update".getBytes());
        System.out.println(String.format("myFirstPath这个节点更新后的数据是:%s",data));
    }

    public static void main(String[] args) throws Exception {
        /*MyZookeeperTest myZookeeperTest = new MyZookeeperTest();
        myZookeeperTest.myTest();*/
        ArrayList<User> list = new ArrayList<>();
        list.sort(Comparator.comparing(User::getId));
    }
}
