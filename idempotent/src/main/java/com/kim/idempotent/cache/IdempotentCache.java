package com.kim.idempotent.cache;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-13 16:25
 */
public interface IdempotentCache{

    void set(String key, Object value, Long time);


    Object  get(String key);

}
