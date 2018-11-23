package com.example.separation.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zhankun on 2018/11/15.
 */
public class ZKGetData {

    static ZooKeeper zk;

    static ZookeeperConnection connection;


    public static Stat znode_exists(String path) throws
            KeeperException,InterruptedException {
        return zk.exists(path,true);
    }

    public static void main(String[] args){
        final CountDownLatch connectedSignal = new CountDownLatch(1);
        String path = "/MyFirstZnode";
        connection = new ZookeeperConnection();
        try {
            zk = connection.connect();
            Stat stat = znode_exists(path);
            if (stat != null){
                byte[] b = zk.getData(path, new Watcher() {
                    @Override
                    public void process(WatchedEvent we) {
                        if (we.getType() == Event.EventType.None) {
                            switch (we.getState()) {
                                case Expired:
                                    connectedSignal.countDown();
                                    break;
                            }
                        } else {
                            String path = "/MyFirstZnode";
                            try {
                                byte[] bn = zk.getData(path,
                                        false, null);
                                String data = new String(bn,
                                        "UTF-8");
                                System.out.println(data);
                                connectedSignal.countDown();

                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                }, stat);
                String data = new String(b, "UTF-8");
                System.out.println("data:------:"+data);
                connectedSignal.await();
            } else {
                System.out.println("Node does not exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
