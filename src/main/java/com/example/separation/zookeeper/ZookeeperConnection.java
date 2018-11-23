package com.example.separation.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zhankun on 2018/11/22.
 */
public class ZookeeperConnection {

    private static String host = "localhost:2181,localhost:2182,localhost:2183";

    private ZooKeeper zoo;

    final CountDownLatch connectedSignal = new CountDownLatch(1);

    public ZooKeeper connect() throws IOException, InterruptedException {
        zoo = new ZooKeeper(host, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent we) {
                if (we.getState() == Event.KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }
        });
        connectedSignal.await();
        return this.zoo;
    }

    public void close() throws InterruptedException {
        zoo.close();
    }
}
