package cn.adcc.client.controller;

import cn.adcc.client.DO.UserApiKey;
import cn.adcc.client.VO.Result;
import cn.adcc.client.service.UserApiService;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户接口关系维护
 */
@RestController
@RequestMapping("/user-api")
public class UserApiController {
    @Autowired
    private UserApiService userApiService;

    @PostMapping("/on")
    public Result turnOnUserApi(@RequestBody List<UserApiKey> ids) {
        userApiService.updateStatusOnBatch(ids);
        return ResultUtil.success();
    }

    @PostMapping("/off")
    public Result turnOffUserApi(@RequestBody List<UserApiKey> ids) {
        userApiService.updateStatusOffBatch(ids);
        return ResultUtil.success();
    }

    @PostMapping("/del")
    public Result delUserApi(@RequestBody List<UserApiKey> ids) {
        userApiService.deleteBatch(ids);
        return ResultUtil.success();
    }

    @GetMapping("/user/{userId}")
    public Result findByUserId(@PathVariable("userId") Long userId) {
        return ResultUtil.success(userApiService.findByUserId(userId));
    }

    @GetMapping("/api/{apiId}")
    public Result findByApiId(@PathVariable("apiId") Long apiId) {
        return ResultUtil.success(userApiService.findByApiId(apiId));
    }
}
