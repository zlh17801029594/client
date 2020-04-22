package cn.adcc.client.controller;

import cn.adcc.client.DTO.MSApiDto;
import cn.adcc.client.DTOImport.SwaggerApiDoc;
import cn.adcc.client.VO.Result;
import cn.adcc.client.service.MSApiService;
import cn.adcc.client.service.SwaggerApiDocService;
import cn.adcc.client.service.UserService;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class MSApiController {
    @Autowired
    private MSApiService msApiService;
    @Autowired
    private SwaggerApiDocService swaggerApiDocService;
    @Autowired
    private UserService userService;

    /**
     * 管理员
     * 获取所有接口信息
     * @return
     */
    @GetMapping("/all")
    public Result getApis() {
        return ResultUtil.success(msApiService.findMSApi());
    }

    /**
     * 获取当前用户所有接口信息
     * @return
     */
    @GetMapping
    public Result getApisByUser() {
        /**
         * 1.获取用户敏感级别
         * 2.根据用户敏感级别获取当前 启用状态、铭感级别可见 的接口信息
         *  剔除 id not in下列接口
         * 查询 当前用户处于 待审批状态(0) 所有申请接口。
         * 过期申请接口、未通过申请接口、已通过但过期接口 可再次申请；
         * 待审批、已通过(未过期)所有接口 不可再次申请
         */
        return null;
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
        return null;
    }

    /**
     * 管理员
     * 更新指定接口状态为停用
     * @param ids
     * @return
     */
    @PostMapping("/off")
    public Result turnOffApis(@RequestBody List<Long> ids) {
        return null;
    }

    /**
     * 管理员
     * 删除指定接口(已失效状态)
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result deleteApis(@RequestBody List<Long> ids) {
        /**
         * 1.查询是否处于失效状态
         * 2.删除指定接口
         */
        return null;
    }

    /**
     * 管理员
     * 根据网关地址，更新网关接口信息
     * 1.请求网关api-doc，获取接口信息
     * 2.对获取的接口信息进行存储(插入/更新)，存储逻辑如下：
     *  新接口-状态：待接入
     *  已存在接口-状态：状态不变
     *  之前存在后续更新丢弃接口-状态：更新为已弃用
     * 3.对服务和接口生成排序码，方便后续排序
     * @param msUrl
     * @return
     * @throws Exception
     */
    @PostMapping("update")
    public Result updateApis(@RequestParam("msUrl") String msUrl) throws Exception {
        SwaggerApiDoc swaggerApiDoc = swaggerApiDocService.getSwaggerApiDoc(msUrl);
        MSApiDto msApiDto = msApiService.buildMSApiDto(swaggerApiDoc);
        msApiService.updateMSApi(Arrays.asList(msApiDto));
        return ResultUtil.success();
    }
}
