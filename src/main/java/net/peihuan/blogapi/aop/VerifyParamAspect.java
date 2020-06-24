package net.peihuan.blogapi.aop;

import lombok.extern.slf4j.Slf4j;
import net.peihuan.blogapi.exception.RequestException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;


@Aspect
@Component
@Slf4j
@Order(value = HIGHEST_PRECEDENCE)
public class VerifyParamAspect {


    @Pointcut("execution(public * net.peihuan.blogapi.web.controller.*.*(..)) "
            + "&& !execution(public * net.peihuan.blogapi.web.controller.GlobalExceptionHandler.*(..))")
    public void verify() {}

    @Around("verify()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] objects = joinPoint.getArgs();
        for (Object object:objects) {
            if (object instanceof BindingResult) {
                if (((BindingResult) object).hasErrors()) {
                    throw new RequestException(((BindingResult) object).getAllErrors().toString());
                }
            }
        }
        return joinPoint.proceed();
    }
}
