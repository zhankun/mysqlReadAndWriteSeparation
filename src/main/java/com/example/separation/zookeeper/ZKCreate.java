package com.example.separation.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;


/**
 * Created by zhankun on 2018/11/15.
 */
public class ZKCreate {

    // create static instance for zookeeper class.
    static ZooKeeper zk;

    // create static instance for ZooKeeperConnection class.
    static ZookeeperConnection conn;

    public static void create(String path,byte[] data) throws KeeperException, InterruptedException {
        zk.create(path,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public static void main(String[] args){
        String path = "/MyFirstZnode";
        byte[] data = "My first zookeeper app".getBytes();
        conn = new ZookeeperConnection();
        try {
            zk = conn.connect();
            create(path,data);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
