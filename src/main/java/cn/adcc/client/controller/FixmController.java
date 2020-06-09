package cn.adcc.client.controller;

import cn.adcc.client.DTO.FixmLogicDto;
import cn.adcc.client.DTO.FixmNoLeaf;
import cn.adcc.client.DTO.FixmNode;
import cn.adcc.client.VO.Result;
import cn.adcc.client.service.FixmService;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fixm")
public class FixmController {
    @Autowired
    private FixmService fixmService;

    @GetMapping("/list")
    public Result list() {
        return ResultUtil.success(fixmService.convert2Tree());
    }

    @PostMapping("/leaf/{id}")
    public Result updateLeaf(@PathVariable("id") Long id, @RequestBody FixmLogicDto fixmLogicDto) {
        fixmService.updateLeaf(id, fixmLogicDto);
        return ResultUtil.success();
    }

    @PostMapping("/noleaf/label")
    public Result updateNoLeafName(@RequestBody FixmNoLeaf fixmNoLeaf) {
        fixmService.updateNoLeaf(fixmNoLeaf);
        return ResultUtil.success();
    }

    @PostMapping("/del")
    public Result delete(@RequestBody List<Long> ids) {
        fixmService.del(ids);
        return ResultUtil.success();
    }
}
