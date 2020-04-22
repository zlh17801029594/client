package cn.adcc.client.controller;

import cn.adcc.client.VO.Result;
import cn.adcc.client.service.MSApplyService;
import cn.adcc.client.service.UserService;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/apply")
public class MSApplyController {
    @Autowired
    private MSApplyService msApplyService;
    @Autowired
    private UserService userService;

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
        return ResultUtil.success(msApplyService.findMSApplies());
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
     * @param ids
     * @return
     */
    @PostMapping("/create")
    public Result createApply(List<Long> ids) {
        /**
         * 1.获取用户名
         * 2.查询这些接口是否处于可申请状态
         * 3.申请、申请详情入库。
         */
        return null;
    }

    /**
     * 管理员
     * 通过用户申请
     * @param id
     * @return
     */
    @PostMapping("/pass")
    public Result passApply(Long id) {
        return null;
    }


    /**
     * 管理员
     * 拒绝用户申请
     * @param id
     * @return
     */
    @PostMapping("/deny")
    public Result denyApply(Long id) {
        return null;
    }
}
