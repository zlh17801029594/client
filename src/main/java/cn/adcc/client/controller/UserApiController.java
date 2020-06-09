package cn.adcc.client.controller;

import cn.adcc.client.DO.User;
import cn.adcc.client.DO.UserApiKey;
import cn.adcc.client.DTO.UserApiDto;
import cn.adcc.client.DTO.UserDto;
import cn.adcc.client.VO.Result;
import cn.adcc.client.service.SsoUserService;
import cn.adcc.client.service.UserApiService;
import cn.adcc.client.service.UserService;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户接口关系维护
 */
@RestController
@RequestMapping("/user-api")
public class UserApiController {
    @Autowired
    private UserApiService userApiService;
    @Autowired
    private UserService userService;
    @Autowired
    private SsoUserService ssoUserService;

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

    @GetMapping("/user")
    public Result findByUser() {
        SsoUser ssoUser = ssoUserService.getSsoUser();
        User user = userService.findByUsername(ssoUser.getUsername());
        List<UserApiDto> userApiDtos = new ArrayList<>();
        if (user != null) {
            userApiDtos = userApiService.findByUserId(user.getId());
        }
        return ResultUtil.success(userApiDtos);
    }

    @GetMapping("/api/{apiId}")
    public Result findByApiId(@PathVariable("apiId") Long apiId) {
        return ResultUtil.success(userApiService.findByApiId(apiId));
    }
}
