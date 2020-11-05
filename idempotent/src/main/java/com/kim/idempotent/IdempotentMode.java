package com.kim.idempotent;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-13 16:03
 */
public enum IdempotentMode {
    /**
     * 幂等
     */
    IDEMPOTENT ,

    /**
     * 重复提交
     */
    DUPLICATE
}
