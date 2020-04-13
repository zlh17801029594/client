package cn.adcc.client.service;


import cn.adcc.client.DTO.MSApiDto;

import java.util.List;

public interface MSApiService {

    MSApiDto buildMSApiDto(String url) throws Exception;

    /*获取后台接口信息并进行维护*/
    //考虑添加统计信息，统计当前新增接口、及弃用接口数
    List<MSApiDto> updateMSApi(List<MSApiDto> msApiDtoLIst);

    /*查询出所有接口数据*/
    List<MSApiDto> findMSApi();

    MSApiDto update(MSApiDto msApiDto);
}
