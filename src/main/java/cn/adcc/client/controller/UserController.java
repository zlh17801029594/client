package cn.adcc.client.controller;

import cn.adcc.client.DTO.UserDto;
import cn.adcc.client.VO.PageRequestDto;
import cn.adcc.client.VO.Result;
import cn.adcc.client.service.UserService;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
}
