package cn.adcc.client.utils;

import cn.adcc.client.VO.Result;

public class ResultUtil {

    public static Result success(Object object) {
        Result result = success();
        result.setData(object);
        return result;
    }

    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("成功");
        return result;
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
