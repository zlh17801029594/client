package cn.adcc.client.controller;


import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DTO.MSUserApiDto;
import cn.adcc.client.DTO.MSUserDto;
import cn.adcc.client.VO.Result;
import cn.adcc.client.enums.MSUserSensEnum;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.UserException;
import cn.adcc.client.service.MSUserService;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        List<MSUser> msUsers = msUserService.findAll();
        List<MSUserDto> msUserDtos = new ArrayList<>();
        msUsers.forEach(msUser -> {
            MSUserDto msUserDto = new MSUserDto();
            BeanUtils.copyProperties(msUser, msUserDto);
            List<MSUserApiDto> msUserApiDtos = new ArrayList<>();
            msUserDto.setMsUserApiDtos(msUserApiDtos);
            msUser.getMsUserApis()
                    .forEach(msUserApi -> {
                        MSUserApiDto msUserApiDto = new MSUserApiDto();
                        BeanUtils.copyProperties(msUserApi, msUserApiDto);
                        msUserApiDto.setApiId(msUserApi.getMsApi().getId());
                        msUserApiDto.setApiName(msUserApi.getMsApi().getName());
                        msUserApiDto.setApiUrl(msUserApi.getMsApi().getUrl());
                        msUserApiDto.setApiStatus(msUserApi.getMsApi().getStatus());
                        if (msUserApi.getMsApi().getStatus().equals(2)) {
                            if (msUser.getSensitiveNum() < msUserApi.getMsApi().getSensitiveNum()) {
                                msUserApiDto.setApiStatus(3);
                            }
                        }
                        msUserApiDto.setApiSensitiveNum(msUserApi.getMsApi().getSensitiveNum());
                        msUserApiDtos.add(msUserApiDto);
                    });
            msUserDtos.add(msUserDto);
        });
        return ResultUtil.success(msUserDtos);
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody SsoUser ssoUser) {
        if (ssoUser == null || !StringUtils.isEmpty(ssoUser.getUsername())) {
            throw new UserException(ResultEnum.COMMON_ERROR.getCode(), "用户信息异常");
        }
        MSUser msUser = msUserService.findMSUserByUsername(ssoUser.getUsername());
        if (msUser != null && !msUser.getSensitiveNum().equals(ssoUser.getSensitiveLevel())) {
            Map<Integer, List<MSUser>> updateMsUsers = new HashMap<>();
            if (ssoUser.getSensitiveLevel() > msUser.getSensitiveNum()) {
                updateMsUsers.put(MSUserSensEnum.UP.getCode(), Arrays.asList(msUser));
            } else {
                updateMsUsers.put(MSUserSensEnum.DOWN.getCode(), Arrays.asList(msUser));
            }
            msUserService.updateMSUser(updateMsUsers);
        }
        return ResultUtil.success();
    }

}
