package cn.adcc.client.service;

import cn.adcc.client.DTO.ApiDto;
import cn.adcc.client.DTO.UserDto;
import cn.adcc.client.DTOImport.SwaggerApiDoc;

import java.util.List;

public interface ApiService {
    /*swagger接口文档转apiDto*/
    ApiDto swagger2ApiDto(SwaggerApiDoc swaggerApiDoc);

    /*更新微服务信息*/
    void updateAll(List<ApiDto> apiDtos);

    /*获取所有接口信息*/
    List<ApiDto> findAll();

    /*获取所有已启用信息*/
    List<ApiDto> findByStatusOn();

    /*获取所有已启用敏感级别合适信息*/
    List<ApiDto> findByStatusOnAndSensitive(Integer sensitiveNum);

    /*查询当前用户接口列表（带接口状态）*/
    List<ApiDto> findByUserDto(UserDto userDto);

    List<ApiDto> findByTypeTrueAndStatusOnAndSensitive(Integer sensitiveNum);

    void updateSens(Long id, Integer sens);

    /*批量更新敏感级别*/
    void updateSensBatch(List<Long> ids, Integer sens);

    void updateStatusOn(Long id);

    /*批量启用接口*/
    void updateStatusOnBatch(List<Long> ids);

    void updateStatusOff(Long id);

    /*批量停用接口*/
    void updateStatusOffBatch(List<Long> ids);

    void updateStatusJoin(Long id);

    /*批量接入接口*/
    void updateStatusJoinBatch(List<Long> ids);

    /*删除接口*/
    void delete(Long id);

    /*批量删除接口*/
    void deleteBatch(List<Long> ids);

    /*根据id查询接口完整信息(申请处查询接口详情需要)*/
    ApiDto findById(Long id);

    /*根据id查询接口局部信息(新建申请需要)*/
    ApiDto findApiById(Long id);
}
