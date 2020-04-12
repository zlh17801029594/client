package cn.adcc.client.controller;

import cn.adcc.client.DTO.MSApiDto;
import cn.adcc.client.VO.Result;
import cn.adcc.client.service.MSApiService;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/apis")
public class MsApiController {
    @Autowired
    private MSApiService MSApiService;

    @GetMapping
    public Result getServices() {
        return ResultUtil.success(MSApiService.findMSApi());
    }

    @GetMapping("/user")
    public List<MSApiDto> getServicesByUserSensitive() {
        /*获取用户sensitive*/
        return null;
    }

    @PostMapping("/on")
    public void setOnServices(@RequestParam("ids") List<Integer> ids) {
    }

    @PostMapping("/off")
    public void setOffServices(@RequestParam("ids") List<Integer> ids) {

    }

    @PostMapping("update")
    public Result updateService(@RequestParam("msUrl") String msUrl) throws Exception {
        if (msUrl == null) {
            throw new RuntimeException("url异常");
        }
        MSApiDto msApiDto = MSApiService.buildMSApiDto(msUrl);
        MSApiService.updateMSApi(Arrays.asList(msApiDto));
        return ResultUtil.success();
    }
}
