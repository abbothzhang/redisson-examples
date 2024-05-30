package org.redisson.example.locks;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

class LockExamplesTest {

    @Test
    void simple() throws InterruptedException {
        // connects to 127.0.0.1:6379 by default
        RedissonClient redisson = Redisson.create();
        String lockName = "key_test_pub3";

        RLock lock = redisson.getLock(lockName);
        lock.tryLock(30000, -1, TimeUnit.SECONDS);
        lock.tryLock(30000, -1, TimeUnit.SECONDS);

        Thread t = new Thread() {
            public void run() {
                RLock lock1 = redisson.getLock(lockName);
                boolean lockSuccess = lock1.tryLock();
                boolean islock = lock1.isLocked();
                System.out.println("islock->" + islock);
                // 如果锁还没释放，那么等待
                lock1.lock();
                lock1.unlock();
            };
        };

        t.start();
        t.join();

        redisson.shutdown();
    }

    @Test
    void testReentrant() throws InterruptedException {
        // connects to 127.0.0.1:6379 by default
        RedissonClient redisson = Redisson.create();
        String lockName = "key_test_pub3";

        RLock lock = redisson.getLock(lockName);
        lock.tryLock(30000, -1, TimeUnit.SECONDS);
        lock.tryLock(30000, -1, TimeUnit.SECONDS);
        lock.unlock();
        boolean isLock = lock.isLocked();
        if (!isLock) {
            Assert.fail("");
        }

        Thread t = new Thread() {
            public void run() {
                RLock lock1 = redisson.getLock(lockName);
                boolean lockSuccess = lock1.tryLock();
                boolean islock = lock1.isLocked();
                System.out.println("islock->" + islock);
                // 如果锁还没释放，那么等待
                lock1.lock();
                lock1.unlock();
            };
        };

        t.start();
        t.join();

        redisson.shutdown();
    }
}