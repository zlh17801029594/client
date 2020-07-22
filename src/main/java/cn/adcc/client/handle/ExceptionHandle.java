package cn.adcc.client.handle;

import cn.adcc.client.VO.Result;
import cn.adcc.client.exception.*;
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
//         e.printStackTrace();
        if (e instanceof MSAPiException) {
            return ResultUtil.error(((MSAPiException) e).getCode(), e.getMessage());
        } else if (e instanceof MSUserApiException) {
            return ResultUtil.error(((MSUserApiException) e).getCode(), e.getMessage());
        } else if (e instanceof UserException) {
            return ResultUtil.error(((UserException) e).getCode(), e.getMessage());
        } else if (e instanceof SwaggerException) {
            return ResultUtil.error(((SwaggerException) e).getCode(), e.getMessage());
        } else if (e instanceof BusinessException) {
            return ResultUtil.error(((BusinessException) e).getCode(), e.getMessage());
        } else if (e instanceof MSApplyException) {
            return ResultUtil.error(((MSApplyException) e).getCode(), e.getMessage());
        } else if (e instanceof MissingServletRequestParameterException) {
            return ResultUtil.error(0, e.getMessage());
        } else if (e instanceof ValidatorFixmException) {
            return ResultUtil.error(((ValidatorFixmException) e).getCode(), e.getMessage());
        } else {
            log.error("其他异常", e);
            return ResultUtil.error(-1, "未知错误");
        }
    }
}
