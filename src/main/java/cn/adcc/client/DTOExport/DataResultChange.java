package cn.adcc.client.DTOExport;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataResultChange {

    @Around("execution(* cn.adcc.client.controller.UserController.*(..))")
    //@Around("execution(* cn.adcc.datacenter.adminbackend.controller..*.*(..))")
    public Object logServiceAccess(ProceedingJoinPoint pjp){
        Object result = null;
        try {
            result = pjp.proceed();
            if (!(result instanceof DataResult)) {
                result = new DataResult<>(20000, "success", result);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}
