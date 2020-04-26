package cn.adcc.client.controller;


import cn.adcc.client.VO.Result;
import cn.adcc.client.service.MSUserService;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理(后期可能加入用户管理方面业务 eg：用户增加启停功能-用户层限制是否可使用微服务)
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class MSUserController {

    @Autowired
    private MSUserService msUserService;

    /**
     * 获取全部用户(用于用户管理->用户接口管理)
     *
     * @return
     */
    @GetMapping("/all")
    public Result findUsers() {
        /**
         * 1.查询全部用户，级联查询用户接口
         *  考虑对用户信息进行分页查询
         */
        return null;
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody SsoUser ssoUser) {
        msUserService.updateMSUser(ssoUser);
        return ResultUtil.success();
    }

}
