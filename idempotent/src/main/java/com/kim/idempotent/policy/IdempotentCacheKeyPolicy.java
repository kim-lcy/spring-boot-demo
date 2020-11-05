package com.kim.idempotent.policy;

import java.lang.reflect.Method;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-14 09:23
 */
public interface IdempotentCacheKeyPolicy {

    String  generate(Method method, Object[] args);
}
