package com.kim.idempotent.policy;


import com.kim.idempotent.constants.IdempotentConstants;
import com.kim.idempotent.util.IdempotentUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-14 09:26
 */
public class DefaultIdempotentCacheKeyPolicy extends AbstractIdempotentCacheKeyPolicy {

    @Override
    public String generate(Method method, Object[] args) {
        Map<String,Object> keyMap = new HashMap<>(2);
        keyMap.put(IdempotentConstants.Variable.METHOD_NAME,method.getDeclaringClass().getCanonicalName()+"#"+method.getName());
        if(null != args && args.length > 0){
            keyMap.put(IdempotentConstants.Variable.METHOD_ARGS,args);
        }
        return IdempotentUtils.hash(IdempotentUtils.toJson(keyMap));
    }

}
