package cn.adcc.client.controller;

import cn.adcc.client.DTO.MSUserApiDto;
import cn.adcc.client.VO.Result;
import cn.adcc.client.service.MSUserApiService;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permissions")
public class UserApiController {
    @Autowired
    private MSUserApiService msUserApiService;

    @GetMapping
    public Result permissions() {
        List<MSUserApiDto> msUserApiDtos = msUserApiService.findAllMSUserAPi();
        return ResultUtil.success(msUserApiDtos);
    }

    @GetMapping("/user")
    public List<MSUserApiDto> permissionsByUser(){
        /*获取用户名查询用户接口信息*/
        /*用户申请处标识： 1.已启用、2.被停用、3.待审批*/
        return null;
    }

    @PostMapping("/apply")
    public Result setApplyPermissions(@RequestBody List<MSUserApiDto> msUserApiDtos) {
        msUserApiDtos = msUserApiService.apply(msUserApiDtos);
        return ResultUtil.success(msUserApiDtos);
    }

    @PostMapping("/on")
    public void setOnPermissions(@RequestParam("ids") List<Integer> ids) {

    }

    @PostMapping("/off")
    public void setOffPermissions(@RequestParam("ids") List<Integer> ids) {

    }

    @PostMapping("/deny")
    public void setDenyPermissions(@RequestParam("ids") List<Integer> ids) {

    }
}
