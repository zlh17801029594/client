package cn.adcc.client.controller;

import cn.adcc.client.VO.FixmLogicVO;
import cn.adcc.client.VO.Result;
import cn.adcc.client.VO.SubversionCheckVO;
import cn.adcc.client.service.FixmLogicService;
import cn.adcc.client.service.ValidateService;
import cn.adcc.client.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/fixmlogic/{version}")
@Slf4j
public class FixmLogicController {
    @Autowired
    private FixmLogicService fixmLogicService;
    @Autowired
    private ValidateService validateService;

    @GetMapping("/list")
    public Result list(@PathVariable("version") String version) {
        return ResultUtil.success(fixmLogicService.list2tree(version));
    }

    @PostMapping
    public Result add(@PathVariable("version") String version, @Validated @RequestBody FixmLogicVO fixmLogicVO) {
        log.info("[添加FixmLogic节点], 版本:{} => {}", version, fixmLogicVO);
        fixmLogicService.add(version, fixmLogicVO);
        return ResultUtil.success();
    }

    @PutMapping
    public Result update(@PathVariable("version") String version, @RequestBody FixmLogicVO fixmLogicVO) {
        log.info("[更新FixmLogic叶子节点], 版本:{} => {}", version, fixmLogicVO);
        fixmLogicService.update(version, fixmLogicVO);
        return ResultUtil.success();
    }

    @PutMapping("/name")
    public Result updateName(@PathVariable("version") String version, @RequestBody FixmLogicVO fixmLogicVO) {
        log.info("[更新FixmLogic目录节点], 版本:{} => {}", version, fixmLogicVO);
        fixmLogicService.updateName(version, fixmLogicVO);
        return ResultUtil.success();
    }

    @PutMapping("/draw")
    public Result draw(@PathVariable("version") String version, @RequestBody FixmLogicVO fixmLogicVO) {
        log.info("[拖拽FixmLogic节点], 版本:{} => {}", version, fixmLogicVO);
        fixmLogicService.updateFatherXsdnode(version, fixmLogicVO);
        return ResultUtil.success();
    }

    @DeleteMapping
    public Result delete(@PathVariable("version") String version, @RequestBody FixmLogicVO fixmLogicVO) {
        log.info("[删除FixmLogic节点], 版本:{} => {}", version, fixmLogicVO);
        fixmLogicService.delete(version, fixmLogicVO);
        return ResultUtil.success();
    }

    /*这两个接口移动到FixmController ，原因：和version没有关系*/
    @GetMapping("/keys")
    public Result findKeys(@PathVariable("version") String version) {
        return ResultUtil.success(fixmLogicService.findFlightInfoColumnsDesc());
    }

    @GetMapping("/map")
    public Result findMap(@PathVariable("version") String version) {
        return ResultUtil.success(fixmLogicService.findFirstFlightInfo());
    }

    /**
     * 进行fixm验证(需根据映射关系使用指定验证文件，即[读取props])
     * @param version
     * @return
     */
    @GetMapping("/validate")
    public Result validateFixm(@PathVariable("version") String version) {
        log.info("[验证Fixm数据], 版本:{}", version);
        validateService.validateFixm(version);
        return ResultUtil.success();
    }

    /**
     * 上传验证文件夹到xsd指定版本目录
     * @param version
     * @param files
     * @return
     */
    @PostMapping("/uploadValidateFile")
    public Result uploadValidateFile(@PathVariable("version") String version, @RequestParam("file")MultipartFile[] files) {
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                StringBuilder sb = new StringBuilder(file.getName()).append("[").append("\n");
                sb.append("getName: ").append(file.getName()).append("\n");
                sb.append("getOriginalFilename: ").append(file.getOriginalFilename()).append("\n");
                try {
                    sb.append("getBytes: ").append(file.getBytes()).append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sb.append("getContentType: ").append(file.getContentType()).append("\n");
                sb.append("getSize(): ").append(file.getSize()).append("\n");
                sb.append("]");
                InputStream inputStream = null;
                try {
                    inputStream = file.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] bytes = null;
                try {
                    bytes = file.getBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Resource resource = file.getResource();
                System.out.println(sb);
            }
        }
        return ResultUtil.success();
    }

    /**
     * 获取xsd版本目录下所有验证文件
     * @param version
     * @return
     */
    @GetMapping("/validateFiles")
    public Result validateFiles(@PathVariable("version") String version) {
        log.info("[获取Fixm验证文件目录], 版本:{}", version);
        return ResultUtil.success(validateService.getValidateFiles(version));
    }

    /**
     * 更新指定版本验证文件映射([更新props])
     * @param version
     * @param filePaths
     * @return
     */
    @PostMapping("/updateValidateMap")
    public Result updateValidateMap(@PathVariable("version") String version, @RequestBody List<String> filePaths) {
        log.info("[更新Fixm验证文件映射关系], 版本:{} => {}", version, filePaths);
        validateService.updateValidateMap(version, filePaths);
        return ResultUtil.success();
    }

    /**
     * 获取指定版本的验证文件映射([获取props])
     * @param version
     * @return
     */
    @GetMapping("/getValidateMapValue")
    public Result getValidateMapValue(@PathVariable("version") String version) {
        log.info("[获取Fixm验证文件映射关系], 版本:{}", version);
        return ResultUtil.success(validateService.getValidateMap(version));
    }

    @PostMapping("/subversion/{subversion}")
    public Result updateSubversion(@PathVariable("version") String version,
                                   @PathVariable("subversion") String subversion,
                                   @RequestBody SubversionCheckVO subversionCheckVO) {
        List<String> chkXsdnodes = subversionCheckVO.getChkXsdnodes();
        List<String> cancelChkXsdnodes = subversionCheckVO.getCancelChkXsdnodes();
        log.info("[编辑子版本], 版本:{} => 子版本:{}, 勾选节点:{}，取消勾选:{}", version, subversion, chkXsdnodes, cancelChkXsdnodes);
        fixmLogicService.updateSubversion(version, subversion, chkXsdnodes, cancelChkXsdnodes);
        return ResultUtil.success();
    }

    @DeleteMapping("/subversion/{subversion}")
    public Result deleteSubversion(@PathVariable("version") String version,
                                   @PathVariable("subversion") String subversion) {
        log.info("[删除子版本], 版本:{} => 子版本:{}", version, subversion);
        fixmLogicService.deleteSubversion(version, subversion);
        return ResultUtil.success();
    }
}
