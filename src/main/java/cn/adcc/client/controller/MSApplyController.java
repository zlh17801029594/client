package cn.adcc.client.controller;

import cn.adcc.client.DO.MSApply;
import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DTO.MSApiDto;
import cn.adcc.client.DTO.MSApplyDto;
import cn.adcc.client.VO.ApplyDetails;
import cn.adcc.client.VO.Result;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.MSApplyException;
import cn.adcc.client.service.MSApiService;
import cn.adcc.client.service.MSApplyService;
import cn.adcc.client.service.MSUserService;
import cn.adcc.client.service.UserService;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/apply")
public class MSApplyController {
    @Autowired
    private MSApplyService msApplyService;
    @Autowired
    private UserService userService;
    @Autowired
    private MSUserService msUserService;
    @Autowired
    private MSApiService msApiService;

    /**
     * 管理员
     * 获取所有用户申请
     * @return
     */
    @GetMapping("/all")
    public Result getApplies() {
        /**
         * 1.查询所有申请并按申请时间倒序
         */
        List<MSApply> msApplies = msApplyService.findMSApplies();
        List<MSApplyDto> msApplyDtos = new ArrayList<>();
        msApplies.forEach(msApply -> {
            MSApplyDto msApplyDto = new MSApplyDto();
            BeanUtils.copyProperties(msApply, msApplyDto);
            msApplyDto.setUsername(msApply.getMsUser().getUsername());
            List<MSApiDto> msApiDtoList = new ArrayList<>();
            msApplyDto.setMsApiDtos(msApiDtoList);
            msApply.getMsApis()
                    .forEach(msApi -> {
                        msApiDtoList.add(msApiService.trans(msApi));
                    });
            msApplyDtos.add(msApplyDto);
        });
        return ResultUtil.success(msApplyDtos);
    }

    @GetMapping("/page")
    public Result getAppliesPage() {
        /**
         * 1.查询所有申请并按申请时间倒序
         */
        List<MSApply> msApplies = msApplyService.findMSApplies();
        List<MSApplyDto> msApplyDtos = new ArrayList<>();
        msApplies.forEach(msApply -> {
            MSApplyDto msApplyDto = new MSApplyDto();
            BeanUtils.copyProperties(msApply, msApplyDto);
            msApplyDto.setUsername(msApply.getMsUser().getUsername());
            msApplyDtos.add(msApplyDto);
        });
        return ResultUtil.success(msApplyDtos);
    }

    /**
     * 获取当前用户申请
     * @return
     */
    @GetMapping
    public Result getAppliesByUser() {
        /**
         * 1.获取用户名
         * 2.查询当前用户所有申请并按申请时间倒序
         */
        String username = userService.getUser().getUsername();
        return ResultUtil.success(msApplyService.findMSAppliesByUsername(username));
    }

    /**
     * 用户申请入库
     * @param applyDetails
     * @return
     */
    @PostMapping("/create")
    public Result createApply(@RequestBody ApplyDetails applyDetails) {
        /**
         * 1.获取用户名
         * 2.查询这些接口是否处于可申请状态（接口可见、接口非待审批，接口非已通过(未过期)）
         * 3.申请、申请详情入库。
         */
        SsoUser ssoUser = userService.getUser();
        MSUser msUser = msUserService.findMSUserBySsoUser(ssoUser);
//        System.out.println(applyDetails.getExpireTime());
//        System.out.println(new Date());
//        System.out.println(new Timestamp(System.currentTimeMillis()));
        if (applyDetails.getExpireTime().before(new Date())) {
            //System.out.println("before date");
            throw new MSApplyException(ResultEnum.COMMON_ERROR.getCode(), "到期时间不能早于当前时间");
        }
//        if (applyDetails.getExpireTime().before(new Timestamp(System.currentTimeMillis()))) {
//            System.out.println("before timestamp");
//        }
        msApplyService.createApply(msUser, applyDetails.getIds(), applyDetails.getExpireTime());
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
        msApplyService.passApply(id);
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
        msApplyService.denyApply(id);
        return ResultUtil.success();
    }
}
