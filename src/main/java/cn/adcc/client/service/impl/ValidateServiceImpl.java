package cn.adcc.client.service.impl;

import cn.adcc.client.exception.ValidatorFixmException;
import cn.adcc.client.service.ValidateService;
import cn.adcc.client.utils.FileUtils;
import cn.adcc.client.utils.PropertiesUtil;
import cn.adcc.client.utils.XsdFile;
import cn.adcc.fixm.convertinterface.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ValidateServiceImpl implements ValidateService {
    private final static String XSD_PREFIX = "./xsd";
    private final static String MAP_FILE = "map.properties";
    private final static String filename = XSD_PREFIX  + "/" + MAP_FILE;

    private Validator validator = new Validator();

    private static final String INTEGRATE_DB = "INTEGRATE";

    @Override
    public String getValidateMapFile(String version) {
        File file = new File(filename);
        if (!file.exists()) {
            // 映射文件不存在，无数据
            return null;
        }
        return PropertiesUtil.readProp(filename, version);
    }

    @Override
    public List<String> getValidateMap(String version) {
        String validateFile = this.getValidateMapFile(version);
        String filePrefix = XSD_PREFIX.concat("/").concat(version).concat("/");
        if (validateFile != null && validateFile.startsWith(filePrefix)) {
            String[] split = validateFile.substring(filePrefix.length()).split("/");
            return Arrays.asList(split);
        }
        return null;
    }

    @Override
    public void updateValidateMap(String version, List<String> filePaths) {
        File file = new File(filename);
        if (!file.exists()) {
            // 映射文件不存在，新建映射文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("[创建映射文件：{}异常]", MAP_FILE);
                throw new RuntimeException("创建文件异常");
            }
        }
        StringBuilder validateFile = new StringBuilder();
        if (filePaths != null && !filePaths.isEmpty()) {
            validateFile.append(XSD_PREFIX).append("/").append(version);
            filePaths.forEach(filePath -> {
                validateFile.append("/").append(filePath);
            });
        }
        PropertiesUtil.writeProp(filename, version, validateFile.toString());
    }

    @Override
    public void removeValidateMap(String version) {
        File file = new File(filename);
        if (!file.exists()) {
            // 映射文件不存在，删除结束
            return;
        }
        PropertiesUtil.removeProp(filename, version);
    }

    @Override
    public void validateFixm(String version) {
        String validateMapFile = this.getValidateMapFile(version);
        if (validateMapFile == null || validateMapFile.isEmpty()) {
            throw new ValidatorFixmException(606, "未配置验证文件！");
        }
        File file = new File(validateMapFile);
        if (!file.exists()) {
            throw new ValidatorFixmException(606, "验证文件不存在！");
        }
        List<String> gen = validator.gen(null, version, INTEGRATE_DB, "INTEGRATE_FLIGHT_INFO_TEST");
        if (gen != null && !gen.isEmpty()) {
            String xml = gen.get(0);
            try {
                // xsd_file_path: xsd文件路径 "./xsd/core4.1/Fixm.xsd"
                // 可提前判断验证文件1.是否存在， 2.是否文件isFile，3.不符合则抛出相关异常前端提示。
                boolean flag = validator.validate_xml(validateMapFile, xml);
                if (flag) {
                    log.info("验证通过");
                    return;
                } else {
                    throw new ValidatorFixmException(606, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[验证异常]", e);
                String errorMessage = e.getMessage();
                throw new ValidatorFixmException(606, errorMessage);
            }
        }
        throw new ValidatorFixmException(606, "没有测试数据！");
    }

    /**
     * 获取验证文件列表
     * @param version
     * @return
     */
    @Override
    public List<XsdFile> getValidateFiles(String version) {
        String filename = XSD_PREFIX.concat("/").concat(version);
        return FileUtils.listFiles(filename);
    }
}
