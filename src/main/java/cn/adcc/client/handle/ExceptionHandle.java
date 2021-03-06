package cn.adcc.client.handle;

import cn.adcc.client.VO.Result;
import cn.adcc.client.exception.MSAPiException;
import cn.adcc.client.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception e) throws Exception {
        if (e instanceof MSAPiException) {
            return ResultUtil.error(((MSAPiException) e).getCode(), e.getMessage());
        } else if (e instanceof MissingServletRequestParameterException) {
            return ResultUtil.error(0, e.getMessage());
        } else {
            e.printStackTrace();
            log.info(e.getMessage());
            return ResultUtil.error(-1, "未知错误");
        }
    }
}
