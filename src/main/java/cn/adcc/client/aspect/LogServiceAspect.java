package cn.adcc.client.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogServiceAspect {

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(* cn.adcc.client.service.*.*(..))")
    public void logPointcut() {

    }

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        long time = System.currentTimeMillis() - beginTime;
        saveRepoLog(pjp, time);
        return result;
    }

    private void saveRepoLog(ProceedingJoinPoint pjp, long time) {
        /*if (pjp.getTarget() instanceof SysLogService) {
            return;
        }*/
        StringBuilder logBuilder = new StringBuilder();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String className = pjp.getTarget().getClass().getName();
        String methodName = signature.getName();
        logBuilder.append(className + "." + methodName + "()").append("<=");
        Object[] args = pjp.getArgs();
        try {
            String params = objectMapper.writeValueAsString(args);
            if (params.length() > 200) {
                params = params.substring(0, 200) + "...";
            }
            logBuilder.append(params);
        } catch (JsonProcessingException e) {
            logBuilder.append("参数序列化异常");
        }
        logBuilder.append(String.format("[执行时间：%s]", time));
        log.info(logBuilder.toString());
    }
}
