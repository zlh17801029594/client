package cn.adcc.client.controller;

import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DTO.MSUserApiDto;
import cn.adcc.client.VO.Result;
import cn.adcc.client.service.MSUserApiService;
import cn.adcc.client.service.UserService;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户接口关系维护
 */
@CrossOrigin
@RestController
@RequestMapping("/user_api")
public class MSUserApiController {
    @Autowired
    private MSUserApiService msUserApiService;

    @PostMapping("/on")
    public Result turnOnUserApi(@RequestBody List<Long> ids) {
        msUserApiService.onMSUserApi(ids);
        return ResultUtil.success();
    }

    @PostMapping("/off")
    public Result turnOffUserApi(@RequestBody List<Long> ids) {
        msUserApiService.offMSUserApi(ids);
        return ResultUtil.success();
    }

    @PostMapping("/del")
    public Result delUserApi(@RequestBody List<Long> ids) {
        msUserApiService.delMSUserApi(ids);
        return ResultUtil.success();
    }
}
