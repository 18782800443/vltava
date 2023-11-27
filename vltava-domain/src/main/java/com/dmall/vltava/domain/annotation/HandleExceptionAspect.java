package com.testhuamou.vltava.domain.annotation;

import com.testhuamou.vltava.domain.base.CommonException;
import com.testhuamou.vltava.domain.base.CommonResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Rob
 * @date Create in 10:15 AM 2019/11/21
 */
@Aspect
@Component
public class HandleExceptionAspect {
    public static final Logger logger = LoggerFactory.getLogger(HandleExceptionAspect.class);


    @Around("@annotation(com.testhuamou.vltava.domain.annotation.HandleException)")
    public Object around(ProceedingJoinPoint point){
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        if (method.getGenericReturnType().equals(CommonResult.class)){
            try {
                return point.proceed();
            } catch (CommonException e){
                logger.error(e.getMessage(), e);
                return CommonResult.fail(e.getMessage(), e.getData());
            } catch (Throwable e){
                logger.error(e.getMessage(), e);
                return CommonResult.fail("Error", e.getMessage());
            }
        } else{
            return null;
        }
    }
}
