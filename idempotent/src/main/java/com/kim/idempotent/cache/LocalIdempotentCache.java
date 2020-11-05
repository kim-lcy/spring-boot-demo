package com.kim.idempotent.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.kim.idempotent.constants.IdempotentConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-13 16:25
 */
public class LocalIdempotentCache implements  IdempotentCache , InitializingBean , DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(LocalIdempotentCache.class);

    public static final Integer INITIAL_CAPACITY = 10000;

    public  static final Long MAX_EXPIRE = 7200L ;

    private static Cache<String,Object> valueCache;

    private static Cache<String,Long> expireCache ;

    private ScheduledExecutorService executor ;


    @Override
    public void set(String key, Object value, Long time) {
        if(MAX_EXPIRE < time && time <= 0L){
            time = MAX_EXPIRE ;
        }
        expireCache.put(IdempotentConstants.CacheKeyPrefix.CACHE_KEY_EXPIRE + key , System.currentTimeMillis() + time * 1000);
        valueCache.put(key , value);
    }

    @Override
    public Object get(String key) {
        return valueCache.getIfPresent(key) ;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        valueCache = CacheBuilder.newBuilder()
                //设置本地缓存容器的初始容量
                .initialCapacity(INITIAL_CAPACITY)
                //设置本地缓存的最大容量
//                .maximumSize(5000)
                //设置写缓存后多少秒过期
                .expireAfterWrite(MAX_EXPIRE, TimeUnit.SECONDS).build();


        expireCache = CacheBuilder.newBuilder()
                .initialCapacity(INITIAL_CAPACITY)
                .expireAfterWrite(MAX_EXPIRE,TimeUnit.SECONDS).build();
        executor = new ScheduledThreadPoolExecutor(1, (Runnable r)-> new Thread(r,"thread-cache-key-checker"));

        executor.scheduleAtFixedRate(()->{
            try{
                Set<String>  keys = new HashSet<>(expireCache.asMap().keySet());
                for(String key : keys){
                    Long  expire = expireCache.getIfPresent(key);
                    if(null != expire) {
                        if(System.currentTimeMillis() >= expire){
                            expireCache.invalidate(key);
                            if(key.startsWith(IdempotentConstants.CacheKeyPrefix.CACHE_KEY_EXPIRE)){
                                String k = key.substring(IdempotentConstants.CacheKeyPrefix.CACHE_KEY_EXPIRE.length());
                                valueCache.invalidate(k);
                            }
                        }
                    }
                }
            }catch (Exception e){
                log.error("{}" , e);
            }
        },0,200,TimeUnit.MILLISECONDS);
    }

    @Override
    public void destroy() throws Exception {
        valueCache.cleanUp();
        expireCache.cleanUp();
        if(!executor.isShutdown()){
            executor.shutdown();
        }
    }
}
