package com.kim.idempotent.config;

import com.kim.idempotent.aspect.IdempotentAspect;
import com.kim.idempotent.cache.IdempotentCache;
import com.kim.idempotent.cache.LocalIdempotentCache;
import com.kim.idempotent.lock.DefaultIdempotentLock;
import com.kim.idempotent.lock.IdempotentLock;
import com.kim.idempotent.policy.DefaultIdempotentCacheKeyPolicy;
import com.kim.idempotent.policy.IdempotentCacheKeyPolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-13 16:10
 */
@Configuration
public class IdempotentAutoConfiguration {


    @Bean
    public IdempotentAspect idempotentAspect(){

        return new IdempotentAspect();
    }


    @Bean
    @ConditionalOnMissingBean(
            name = {"idempotentCache"}
    )
    public IdempotentCache idempotentCache(){
        return new LocalIdempotentCache();
    }



    @Bean
    public IdempotentCacheKeyPolicy cacheKeyPolicy(){
        return new DefaultIdempotentCacheKeyPolicy();
    }


    @Bean
    @ConditionalOnMissingBean(
            name = {"idempotentLock"}
    )
    public IdempotentLock idempotentLock(){
        return  new DefaultIdempotentLock();
    }


    @Bean
    @ConditionalOnMissingBean(
            name = {"idempotentCacheKeyPolicy"}
    )
    public IdempotentCacheKeyPolicy idempotentCacheKeyPolicy(){
        return new DefaultIdempotentCacheKeyPolicy();
    }
}
