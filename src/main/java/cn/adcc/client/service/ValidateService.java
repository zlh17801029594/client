package cn.adcc.client.service;

import cn.adcc.client.utils.XsdFile;

import java.util.List;

public interface ValidateService {
    String getValidateMapFile(String version);

    /*根据版本获取相应验证文件映射值*/
    List<String> getValidateMap(String version);

    /*更新版本验证文件映射值*/
    void updateValidateMap(String version, List<String> filePaths);

    /*移除版本验证文件映射值*/
    void removeValidateMap(String version);

    /*获取指定版本验证文件列表(包含层级关系)*/
    List<XsdFile> getValidateFiles(String version);

    /*验证fixm数据*/
    void validateFixm(String version);
}
