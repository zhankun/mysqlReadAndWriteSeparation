package com.example.separation.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


/**
 * Created by zhankun on 2018/11/15.
 */
public class ZKExists {

    static ZooKeeper zk;

    static ZookeeperConnection connection;

    public static Stat znodeExists(String path) throws KeeperException, InterruptedException {
        return zk.exists(path,true);
    }

    public static void main(String[] args){
        String path = "/MyFirstZnode";
        connection = new ZookeeperConnection();
        try {
            zk = connection.connect();
            Stat stat = znodeExists(path);
            if (stat != null){
                System.out.println("Node exists and the node version is " +
                        stat.getVersion()+"createtime is :"+stat.getCtime());
            } else {
                System.out.println("Node does not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
