package com.kim.idempotent;



import com.kim.idempotent.policy.AbstractIdempotentCacheKeyPolicy;
import com.kim.idempotent.policy.DefaultIdempotentCacheKeyPolicy;

import java.lang.annotation.*;

/**
 * 重复提交,幂等注解
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Idempotent {

    /**
     * 功能类型
     * @return 功能类型  IdempotentType.IDEMPOTENT(调用幂等)  IdempotentType.DUPLICATE(防止重复提交)
     */
    IdempotentMode mode() default  IdempotentMode.DUPLICATE ;

    /**
     * 有效时间 单位ms
     * @return  有效时间 默认2小时
     */
    long expire() default 10L;

    /**
     * 有效时间内的访问次数
     * @return 在有效时间内的访问次数 默认为0 不限次数
     */
    int times()  default  1 ;

    /**
     * 生成key的策略
     * @return 策略类
     */
    Class<? extends AbstractIdempotentCacheKeyPolicy> keyPolicy() default DefaultIdempotentCacheKeyPolicy.class;


}
