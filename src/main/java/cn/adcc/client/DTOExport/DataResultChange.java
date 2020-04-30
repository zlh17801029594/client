package cn.adcc.client.DTOExport;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/*@Aspect
@Component*/
/*aop统一处理 值得探索*/
public class DataResultChange {

    @Around("execution(* cn.adcc.client.controller.UserController.*(..))")
    //@Around("execution(* cn.adcc.datacenter.adminbackend.controller..*.*(..))")
    public Object logServiceAccess(ProceedingJoinPoint pjp){
        Object result = null;
        try {
            result = pjp.proceed();
            if (!(result instanceof DataResult)) {
                result = new DataResult<>(200, "success", result);
            }
        } catch (Throwable throwable) {
            /*这里捕获了异常，导致ExceptionHandle 处理失败*/
            throwable.printStackTrace();
        }
        return result;
    }
}
