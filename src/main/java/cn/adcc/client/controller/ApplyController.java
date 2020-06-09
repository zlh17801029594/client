package cn.adcc.client.controller;

import cn.adcc.client.DO.User;
import cn.adcc.client.DTO.ApplyDto;
import cn.adcc.client.VO.PageRequestDto;
import cn.adcc.client.VO.Result;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.MSApplyException;
import cn.adcc.client.service.*;
import cn.adcc.client.utils.EmptyUtils;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/apply")
public class ApplyController {
    @Autowired
    private ApplyService applyService;
    @Autowired
    private SsoUserService ssoUserService;
    @Autowired
    private UserService userService;

    /**
     * 管理员
     * 获取所有用户申请
     * @return
     */
    @PostMapping("/list")
    public Result getApplies(@RequestBody PageRequestDto<ApplyDto> pageRequestDto) {
        /**
         * 1.查询所有申请并按申请时间倒序
         */
        if (pageRequestDto.getData() == null) {
            pageRequestDto.setData(new ApplyDto());
        }
        pageRequestDto.getData().setDelAdmin(false);
        applyService.list(pageRequestDto);
        return ResultUtil.success(pageRequestDto);
    }

    /**
     * 普通用户
     * 查询自身申请
     * @param pageRequestDto
     * @return
     */
    @PostMapping("/user/list")
    public Result getAppliesByUser(@RequestBody PageRequestDto<ApplyDto> pageRequestDto) {
        /**
         * 1.查询所有申请并按申请时间倒序
         */
        if (pageRequestDto.getData() == null) {
            pageRequestDto.setData(new ApplyDto());
        }
        String username = ssoUserService.getSsoUser().getUsername();
        User user = userService.findByUsername(username);
        if (user != null) {
            pageRequestDto.getData().setUserId(user.getId());
            applyService.list(pageRequestDto);
        }
        return ResultUtil.success(pageRequestDto);
    }

    /**
     * 用户申请入库
     * @param applyDto
     * @return
     */
    @PostMapping("/create")
    public Result createApply(@RequestBody ApplyDto applyDto) {
        /**
         * 1.获取用户名
         * 2.查询这些接口是否处于可申请状态（接口可见、接口非待审批，接口非已通过(未过期)）
         * 3.申请、申请详情入库。
         */
        if (applyDto.getExpireTime().before(new Date())) {
            throw new MSApplyException(ResultEnum.COMMON_ERROR.getCode(), "到期时间不能早于当前时间");
        }
        if (!EmptyUtils.isNotEmpty(applyDto.getApplyDetailsDtos())) {
            throw new MSApplyException(ResultEnum.COMMON_ERROR.getCode(), "申请接口不能为空");
        }
        applyService.save(applyDto);
        return ResultUtil.success();
    }

    /**
     * 管理员
     * 通过用户申请
     * @param id
     * @return
     */
    @PostMapping("/pass/{id}")
    public Result passApply(@PathVariable("id") Long id) {
        applyService.updateStatusPass(id);
        return ResultUtil.success();
    }

    /**
     * 审批部分接口
     * @return
     */
    @PostMapping("/pass-some-api")
    public Result passApplySome(@RequestBody ApplyDto applyDto) {
        /**
         * 1.查询申请是否有效
         * 2.判断申请对应申请详情接口是否 containsAll 当前接口ids 是，正常， 否，数据不一致
         * 3.正常： set 申请状态 3部分通过；设置申请详情中出现id 为 1通过，未出现id为 0拒绝
         * 4.级联修改apply(apply处加上 cascade.merge)
         * 5.已通过接口加入用户接口关系表
         */
        if (!EmptyUtils.isNotEmpty(applyDto.getApplyDetailsDtos())) {
            throw new MSApplyException(ResultEnum.COMMON_ERROR.getCode(), "审批接口不能为空");
        }
        applyService.updateStatusPassSome(applyDto);
        return ResultUtil.success();
    }


    /**
     * 管理员
     * 拒绝用户申请
     * @param id
     * @return
     */
    @PostMapping("/deny/{id}")
    public Result denyApply(@PathVariable("id") Long id) {
        applyService.updateStatusDeny(id);
        return ResultUtil.success();
    }

    @PostMapping("/del/{id}")
    public Result delApply(@PathVariable("id") Long id) {
        applyService.deleteById(id);
        return ResultUtil.success();
    }
}