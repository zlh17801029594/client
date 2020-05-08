package cn.adcc.client.service;


import cn.adcc.client.DO.MSApi;
import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DTO.MSApiDto;
import cn.adcc.client.DTOImport.SwaggerApiDoc;
import cn.adcc.client.sso.SsoUser;

import java.util.List;

public interface MSApiService {

    /*根据网关接口信息构建api表数据结构*/
    MSApiDto buildMSApiDto(SwaggerApiDoc swaggerApiDoc);

    /*获取后台接口信息并进行维护*/
    //考虑添加统计信息，统计当前新增接口、及弃用接口数
    void updateMSApi(List<MSApiDto> msApiDtoLIst);

    /*查询出所有接口数据（不包含接口otherInfo）*/
    List<MSApiDto> findAllMSApis();

    /*单独查询接口详情接口（包含接口otherInfo）*/
    MSApiDto findMSApiById(Long id);

    /*查询出所有已启用接口*/
    List<MSApiDto> findMSApisByStatusOn();

    /*查询出当前用户所有可见&启用接口*/
    List<MSApiDto> findMSApisBySensitiveAndStatusOn(Integer sensitiveNum);

    /*接口启用*/
    void on(List<Long> ids);

    /*接口停用*/
    void off(List<Long> ids);

    /*接口接入*/
    void join(List<Long> ids);

    /*接口移除(伪删除)*/
    void del(List<Long> ids);

    /*更新接口敏感级别*/
    void sensitive(Integer sensitiveNum, List<Long> ids);

    MSApiDto trans(MSApi msApi);

    List<MSApiDto> findMsApisByMSUserAndStatusOn(SsoUser ssoUser);
}
