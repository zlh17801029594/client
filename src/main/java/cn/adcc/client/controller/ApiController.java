package cn.adcc.client.controller;

import cn.adcc.client.DTO.ApiDto;
import cn.adcc.client.DTO.UserDto;
import cn.adcc.client.DTOSwagger.SwaggerApiDoc;
import cn.adcc.client.VO.Result;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.MSAPiException;
import cn.adcc.client.service.*;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {
    @Autowired
    private SwaggerApiDocService swaggerApiDocService;
    @Autowired
    private ApiService apiService;
    @Autowired
    private ApiDetailsService apiDetailsService;
    @Autowired
    private SsoUserService ssoUserService;

    @PostMapping("/updateAll")
    public Result updateAll(@RequestParam("url") String url) {
        long beginTime = System.currentTimeMillis();
        List<SwaggerApiDoc> swaggerApiDocs = swaggerApiDocService.getSwaggerApiDoc(url);
        long time = System.currentTimeMillis() - beginTime;
        if (time > 4500) {
            log.error("[更新微服务信息] 时间超时: {}", time);
            return null;
        }
        List<ApiDto> apiDtos = swaggerApiDocs.stream().map(swaggerApiDoc -> apiService.swagger2ApiDto(swaggerApiDoc)).collect(Collectors.toList());
        apiService.updateAll(apiDtos);
        return ResultUtil.success();
    }

    @GetMapping("/all")
    public Result findAll() {
        return ResultUtil.success(apiService.findAll());
    }

    @GetMapping("/user")
    public Result findByUser() {
        List<String> roles = ssoUserService.getRoles();
        SsoUser ssoUser = ssoUserService.getSsoUser();
        UserDto userDto = new UserDto();
        userDto.setUsername(ssoUser.getUsername());
        userDto.setSensitiveNum(ssoUser.getSensitiveLevel());
        if (roles.contains("SUPER_ADMIN") || roles.contains("ADMIN")) {
            return ResultUtil.success(apiService.findByStatusOn());
        } else {
            return ResultUtil.success(apiService.findByUserDto(userDto));
        }
    }

    @GetMapping("/type")
    public Result findByUserAndType() {
        List<ApiDto> apiDtos = apiService.findApisByType();
        return ResultUtil.success(apiDtos);
    }

    @GetMapping("/list")
    public Result findByUserAndType1() {
        List<ApiDto> apiDtos = apiService.findApisByType1();
        return ResultUtil.success(apiDtos);
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") Long id) {
        return ResultUtil.success(apiService.findById(id));
    }

    @GetMapping("/details/{id}")
    public Result findDetailsById(@PathVariable("id") Long id) {
        return ResultUtil.success(apiDetailsService.findById(id));
    }

    /**
     * 管理员
     * 更新指定接口状态为启用
     * @param ids
     * @return
     */
    @PostMapping("/on")
    public Result turnOnApis(@RequestBody List<Long> ids) {
        /**
         * 1.调用服务获取当前所有api
         * 2.判断api状态是否可执行当前操作(若异常，则用户前端返回：数据不一致，请刷新后重试)
         * 3.调用服务更新当前所有api状态 [此接口可共用]
         */
        if (ids.size() == 0) {
            throw new MSAPiException(ResultEnum.COMMON_ERROR.getCode(), "参数不能为空");
        }
        apiService.updateStatusOnBatch(ids);
        return ResultUtil.success();
    }

    /**
     * 管理员
     * 更新指定接口状态为停用
     * @param ids
     * @return
     */
    @PostMapping("/off")
    public Result turnOffApis(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new MSAPiException(ResultEnum.COMMON_ERROR.getCode(), "参数不能为空");
        }
        apiService.updateStatusOffBatch(ids);
        return ResultUtil.success();
    }

    /**
     * 管理员
     * 接入指定接口(待接入状态)
     * @param ids
     * @return
     */
    @PostMapping("/join")
    public Result joinApis(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new MSAPiException(ResultEnum.COMMON_ERROR.getCode(), "参数不能为空");
        }
        apiService.updateStatusJoinBatch(ids);
        return ResultUtil.success();
    }

    /**
     * 管理员
     * 移除指定接口(已失效状态)
     * @param ids
     * @return
     */
    @PostMapping("/del")
    public Result deleteApis(@RequestBody List<Long> ids) {
        /**
         * 1.查询是否处于失效状态
         * 2.删除指定接口
         */
        if (ids.size() == 0) {
            throw new MSAPiException(ResultEnum.COMMON_ERROR.getCode(), "参数不能为空");
        }
        apiService.deleteBatch(ids);
        return ResultUtil.success();
    }

    /**
     * 管理员
     * 更新接口敏感级别
     * @param sensitiveNum
     * @param ids
     * @return
     */
    @PostMapping("/sensitive/{sensitiveNum}")
    public Result updateSensitiveNum(@PathVariable("sensitiveNum") Integer sensitiveNum, @RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new MSAPiException(ResultEnum.COMMON_ERROR.getCode(), "参数不能为空");
        }
        apiService.updateSensBatch(ids, sensitiveNum);
        return ResultUtil.success();
    }
}
