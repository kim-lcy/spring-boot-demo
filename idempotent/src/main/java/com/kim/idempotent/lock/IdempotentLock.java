package com.kim.idempotent.lock;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-16 11:37
 */
public interface IdempotentLock {

    void lock(String key);

    void unLock(String key);
}
