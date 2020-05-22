package cn.adcc.client.controller;

import cn.adcc.client.DTO.UserDto;
import cn.adcc.client.VO.PageRequestDto;
import cn.adcc.client.VO.Result;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.UserException;
import cn.adcc.client.service.UserService;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/list")
    public Result getUserList(PageRequestDto<UserDto> pageRequestDto) {
        if (pageRequestDto.getData() == null) {
            pageRequestDto.setData(new UserDto());
        }
        userService.list(pageRequestDto);
        return ResultUtil.success(pageRequestDto);
    }

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable("id") Long id) {
        return ResultUtil.success(userService.findById(id));
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody SsoUser ssoUser) {
        if (ssoUser == null || !StringUtils.isEmpty(ssoUser.getUsername())) {
            throw new UserException(ResultEnum.COMMON_ERROR.getCode(), "用户信息异常");
        }
        userService.updateUser(ssoUser);
        return ResultUtil.success();
    }
}
