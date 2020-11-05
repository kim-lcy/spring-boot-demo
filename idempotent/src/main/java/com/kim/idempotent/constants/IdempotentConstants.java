package com.kim.idempotent.constants;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-13 18:25
 */
public class IdempotentConstants {

    public static class Variable {

        public static final String METHOD_NAME = "kim_method_name" ;

        public  static  final String METHOD_ARGS = "kim_method_args";
    }

    public static class MethodResult{
        public static final String VOID = "kim_void_";
    }

    public static class LockPrefix{
        public static final String Lock_Prefix = "kim_lock_";
    }

    public static class CacheKeyPrefix{

        public static final String CACHE_KEY_EXPIRE = "kim_it_expire_";

        public static final String CACHE_KEY_TIMES = "kim_it_times_" ;
    }

}
