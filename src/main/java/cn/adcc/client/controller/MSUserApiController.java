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
@RequestMapping("/permission")
public class MSUserApiController {
    @Autowired
    private MSUserApiService msUserApiService;
    @Autowired
    private UserService userService;

    @GetMapping
    public Result permissions() {
        List<MSUserApiDto> msUserApiDtos = msUserApiService.findAllMSUserAPi();
        return ResultUtil.success(msUserApiDtos);
    }

    @GetMapping("/user")
    public Result permissionsByUser(){
        /*获取用户名查询用户接口信息*/
        /*用户申请处标识： 1.已启用、2.被停用、3.待审批*/
        String username = userService.getUser().getUsername();
        MSUser msUser = msUserApiService.findMSUserByUsername(username);
        return ResultUtil.success(msUser);
    }

    @PostMapping("/apply")
    public Result setApplyPermissions(@RequestBody List<MSUserApiDto> msUserApiDtos) {
        msUserApiDtos = msUserApiService.apply(msUserApiDtos);
        return ResultUtil.success(msUserApiDtos);
    }

    @PostMapping("/{userId}/on")
    public void turnOnPermissions(@PathVariable("userId") Long userId, @RequestBody List<Long> ids) {
        System.out.println(userId);
        System.out.println(ids);
    }

    @PostMapping("/{userId}/off")
    public void turnOffPermissions(@PathVariable("userId") Long userId, @RequestBody List<Long> ids) {
        System.out.println(userId);
        System.out.println(ids);
    }
}
