package cn.adcc.client.controller;

import cn.adcc.client.DTO.FixmLogicDto;
import cn.adcc.client.VO.Result;
import cn.adcc.client.service.FixmLogicService;
import cn.adcc.client.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fixmlogic/{version}")
@Slf4j
public class FixmLogicController {
    @Autowired
    private FixmLogicService fixmLogicService;

    @GetMapping("/list")
    public Result list(@PathVariable("version") String version) {
        return ResultUtil.success(fixmLogicService.list2tree(version));
    }

    @PostMapping
    public Result add(@PathVariable("version") String version, @Validated @RequestBody FixmLogicDto fixmLogicDto) {
        fixmLogicDto.setVersion(version);
        log.info("[添加FixmLogic节点], {}", fixmLogicDto);
        FixmLogicDto childFixmLogicDto = fixmLogicService.add(fixmLogicDto);
        return ResultUtil.success(childFixmLogicDto);
    }

    @PutMapping
    public Result update(@PathVariable("version") String version, @RequestBody FixmLogicDto fixmLogicDto) {
        fixmLogicDto.setVersion(version);
        log.info("[更新FixmLogic叶子节点], {}", fixmLogicDto);
        fixmLogicService.update(fixmLogicDto);
        return ResultUtil.success();
    }

    @PutMapping("/name")
    public Result updateName(@PathVariable("version") String version, @RequestBody FixmLogicDto fixmLogicDto) {
        fixmLogicDto.setVersion(version);
        log.info("[更新FixmLogic目录节点], {}", fixmLogicDto);
        fixmLogicService.updateName(fixmLogicDto);
        return ResultUtil.success();
    }

    @PutMapping("/draw")
    public Result draw(@PathVariable("version") String version, @RequestBody FixmLogicDto fixmLogicDto) {
        fixmLogicDto.setVersion(version);
        log.info("[拖拽FixmLogic节点], {}", fixmLogicDto);
        // 返回父节点fixmLogicDto
        FixmLogicDto fatherFixmLogicDto = fixmLogicService.updateFatherXsdnode(fixmLogicDto);
        return ResultUtil.success(fatherFixmLogicDto);
    }

    @DeleteMapping
    public Result delete(@PathVariable("version") String version, @RequestBody FixmLogicDto fixmLogicDto) {
        fixmLogicDto.setVersion(version);
        log.info("[删除FixmLogic节点], {}", fixmLogicDto);
        // 返回父节点fixmLogicDto
        FixmLogicDto fatherFixmLogicDto = fixmLogicService.delete(fixmLogicDto);
        return ResultUtil.success(fatherFixmLogicDto);
    }

    @GetMapping("/keys")
    public Result findKeys(@PathVariable("version") String version) {
        return ResultUtil.success(fixmLogicService.findFlightInfoColumns());
    }

    @GetMapping("/map")
    public Result findMap(@PathVariable("version") String version) {
        return ResultUtil.success(fixmLogicService.findFirstFlightInfo());
    }
}
