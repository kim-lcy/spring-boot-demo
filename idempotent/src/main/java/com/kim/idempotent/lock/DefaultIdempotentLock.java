package com.kim.idempotent.lock;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-16 11:39
 */

public class DefaultIdempotentLock implements  IdempotentLock {

    private static final Logger logger = LoggerFactory.getLogger(DefaultIdempotentLock.class);

    private ConcurrentHashMap<String,CountDownLatch> lockMap = new ConcurrentHashMap<>();

    private CopyOnWriteArraySet<String> keySet = new CopyOnWriteArraySet<>();

    private  ReentrantLock reentrantLock  = new ReentrantLock();


    @Override
    public void lock(String key) {
        if(keySet.contains(key)){
            if(!lockMap.containsKey(key)){
                try{
                    reentrantLock.lock();
                    // 双重校验,考虑高并发问题.必须保证对同一个key加锁的线程失败后是落在同一个countdown对象上
                    if(!lockMap.containsKey(key)){
                        lockMap.put(key,new CountDownLatch(1));
                    }
                }finally {
                    reentrantLock.unlock();
                }
            }
            try {
                CountDownLatch cdl = lockMap.get(key);
                if(null != cdl){
                    cdl.await();
                }
            } catch (InterruptedException e) {
                logger.error("InterruptedException message {}" , e.getMessage());
            }
        }else{
            keySet.add(key);
        }
    }

    @Override
    public void unLock(String key) {
        if(keySet.remove(key)){
            CountDownLatch countDownLatch = lockMap.remove(key);
            if(countDownLatch != null){ //有可能没有竞争,countdown不存在
                countDownLatch.countDown();
            }
        }
    }

}
