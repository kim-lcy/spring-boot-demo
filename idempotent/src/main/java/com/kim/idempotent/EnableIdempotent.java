package com.kim.idempotent;



import com.kim.idempotent.config.IdempotentAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-14 10:46
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(IdempotentAutoConfiguration.class)
public @interface EnableIdempotent {

}
