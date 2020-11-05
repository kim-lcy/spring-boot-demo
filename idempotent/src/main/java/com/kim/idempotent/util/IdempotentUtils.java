package com.kim.idempotent.util;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-13 18:03
 */
public class IdempotentUtils {

    private static Gson gson = new Gson() ;

    private static HashFunction hashFunction = Hashing.murmur3_32(16);

    public static String  hash(String key ){
        return  hashFunction.newHasher().putString(key, Charsets.UTF_8).hash().toString();
    }

    public static  String toJson(Object object){
        return gson.toJson(object);
    }
}
