package com.kim.idempotent.policy;

import java.lang.reflect.Method;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-14 09:52
 */
public abstract class AbstractIdempotentCacheKeyPolicy implements IdempotentCacheKeyPolicy {

    public abstract String generate(Method method, Object[] args);
}
