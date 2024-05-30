/**
 * Copyright (c) 2016-2019 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson.example.locks;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class LockExamples {

    public static void main(String[] args) throws InterruptedException {
        // connects to 127.0.0.1:6379 by default
        RedissonClient redisson = Redisson.create();
        String lockName = "key_test_pub3";

        RLock lock = redisson.getLock(lockName);
        lock.tryLock(30000, -1, TimeUnit.SECONDS);
        lock.tryLock(30000, -1, TimeUnit.SECONDS);
        lock.unlock();
        boolean isLock = lock.isLocked();
        System.out.println("isLock->"+isLock);
        lock.unlock();
        isLock = lock.isLocked();
        System.out.println("isLock->"+isLock);

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
