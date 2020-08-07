package cn.adcc.client.controller;

import cn.adcc.client.VO.Result;
import cn.adcc.client.service.FixmLogicService;
import cn.adcc.client.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 1.获取所有fixm version
 * 2.获取flight_info_test表信息
 */
@Slf4j
@RestController
@RequestMapping("/fixm")
public class FixmController {
    @Autowired
    private FixmLogicService fixmLogicService;

    @GetMapping("/versions")
    public Result findFixmVersions() {
        return ResultUtil.success(fixmLogicService.findFixmVersions());
    }

    /**
     * 删除指定版本数据[fixmlogic数据、fixmorder数据、删除验证文件包、删除props]
     * @param version
     * @return
     */
    @DeleteMapping("/{version}")
    public Result deleteFixmByVersion(@PathVariable("version") String version) {
        log.info("[删除版本：'{}'全部节点数据]", version);
        fixmLogicService.deleteByVersion(version);
        return ResultUtil.success();
    }
}
