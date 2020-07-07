package cn.adcc.client.controller;

import cn.adcc.client.DTO.FixmOrderDto;
import cn.adcc.client.VO.Result;
import cn.adcc.client.service.FixmOrderService;
import cn.adcc.client.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fixmorder/{version}")
@Slf4j
public class FixmOrderController {
    @Autowired
    private FixmOrderService fixmOrderService;

    @PutMapping
    public Result save(@PathVariable("version") String version, @RequestBody FixmOrderDto fixmOrderDto) {
        log.info("[保存FixmOrder数据], {}", fixmOrderDto);
        fixmOrderDto.setVersion(version);
        fixmOrderService.save(fixmOrderDto);
        return ResultUtil.success();
    }
}
