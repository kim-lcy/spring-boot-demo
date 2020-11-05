package com.kim.idempotent.aspect;


import com.kim.idempotent.Idempotent;
import com.kim.idempotent.IdempotentMode;
import com.kim.idempotent.cache.IdempotentCache;
import com.kim.idempotent.constants.IdempotentConstants;
import com.kim.idempotent.exception.IdempotentException;
import com.kim.idempotent.lock.IdempotentLock;
import com.kim.idempotent.policy.AbstractIdempotentCacheKeyPolicy;
import com.kim.idempotent.policy.DefaultIdempotentCacheKeyPolicy;
import com.kim.idempotent.policy.IdempotentCacheKeyPolicy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;

/**
 * Idempotent 注解切面
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-13 16:11
 */
@Aspect
public class IdempotentAspect implements ApplicationContextAware {

    private static  ApplicationContext applicationContext ;

    @Autowired
    @Qualifier(value = "idempotentLock")
    private IdempotentLock idempotentLock ;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext =  ac;
    }

    @Autowired
    @Qualifier(value = "idempotentCache")
    private IdempotentCache idempotentCache ;


    @Autowired
    @Qualifier(value = "idempotentCacheKeyPolicy")
    private IdempotentCacheKeyPolicy idempotentCacheKeyPolicy ;




    /**
     * 定义切点，以@Idempotent注解的配置的方法
     */
    @Pointcut("@annotation(com.kim.idempotent.Idempotent)")
    public void idempotent() {

    }

    /**
     * 环绕通知
     * @param pjp
     */
    @Around("idempotent()")
    public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

        Method method = methodSignature.getMethod();

        Idempotent idempotent = method.getAnnotation(Idempotent.class);

        String key = generateKey(idempotent.keyPolicy(),method,pjp.getArgs());

        if(null == key){
            throw  new IdempotentException(" {"+idempotent.keyPolicy().getCanonicalName()+"}  generate key is null " );
        }
        IdempotentMode type =  idempotent.mode();
        if(IdempotentMode.DUPLICATE.equals(type)){
             //防重复
            return handleDuplicate(key,idempotent,pjp);
        }else if(IdempotentMode.IDEMPOTENT.equals(type)){
            // 幂等
            return handleIdempotent(key,idempotent,pjp);
        }
        return pjp.proceed(pjp.getArgs());
    }

    private Object handleIdempotent(String key ,Idempotent idempotent , ProceedingJoinPoint pjp) throws Throwable{
        try{
            idempotentLock.lock(key);
            Object object = idempotentCache.get(key);
            if(null == object){
                // key不存在, 直接执行
                Object result = pjp.proceed(pjp.getArgs()); // 若没有返回值
                if(null == result){ // 无返回值
                    result = IdempotentConstants.MethodResult.VOID;
                }
                idempotentCache.set(key,result,idempotent.expire());
                return result;
            }else{
                return IdempotentConstants.MethodResult.VOID.equals(object) ? null : object;
            }
        }
        finally {
            idempotentLock.unLock(key);
        }


    }


    private Object handleDuplicate(String key ,Idempotent idempotent , ProceedingJoinPoint pjp) throws Throwable {
        //1.判断key是否存在，不存在说明第一次提交 或者 过了有效期,直接执行，将key，和结果缓存，设置key的有效时间
        try{
            idempotentLock.lock(IdempotentConstants.LockPrefix.Lock_Prefix + key);
            Object object = idempotentCache.get(key);
            if(null == object){
                // key不存在, 直接执行
                Object result = pjp.proceed(pjp.getArgs()); // 若没有返回值
                if(null == result){ // 无返回值
                    result = IdempotentConstants.MethodResult.VOID;
                }
                idempotentCache.set(key,result,idempotent.expire());
                idempotentCache.set(IdempotentConstants.CacheKeyPrefix.CACHE_KEY_TIMES + key,1,idempotent.expire());
                return result;
            }else {
                // key 存在,判断调用次数
                Integer times = idempotent.times() ;
                if(0 >= times ){ // 配置0次，不限制，直接调用
                    return pjp.proceed(pjp.getArgs());
                }
                Object timesObj =   idempotentCache.get(IdempotentConstants.CacheKeyPrefix.CACHE_KEY_TIMES + key);
                Integer usedTimes =  null == timesObj ? 0 : (Integer) timesObj ;
                if(usedTimes < times){ // 调用次数已经失效 或者 未达到次数 , 直接调用
                    idempotentCache.set(IdempotentConstants.CacheKeyPrefix.CACHE_KEY_TIMES + key , ++usedTimes , idempotent.expire());
                    return pjp.proceed(pjp.getArgs());
                }
                // 以达到调用次数，防止重复调用，直接返回缓存中的值。
                return IdempotentConstants.MethodResult.VOID.equals(object) ? null : object;
            }
        }finally {
            idempotentLock.unLock(IdempotentConstants.LockPrefix.Lock_Prefix + key);
        }
    }



    private String generateKey(Class<? extends AbstractIdempotentCacheKeyPolicy> keyPolicyClass, Method method, Object [] args ){

        IdempotentCacheKeyPolicy policy ;

        if(!DefaultIdempotentCacheKeyPolicy.class.getCanonicalName().equals(keyPolicyClass.getCanonicalName())){
           policy =  applicationContext.getBean(keyPolicyClass);
        }else{
            policy = idempotentCacheKeyPolicy ;
        }
        return policy.generate(method, args);
    }

}
