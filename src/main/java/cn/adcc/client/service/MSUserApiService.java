package cn.adcc.client.service;

import cn.adcc.client.DTO.MSUserApiDto;

import java.util.List;

public interface MSUserApiService {
    /*管理员查询所有信息*/
    List<MSUserApiDto> findAllMSUserAPi();

    /*管理员更新信息*/
    List<MSUserApiDto> update(List<MSUserApiDto> msUserApiDtos);

    /*用户申请信息(插入新的、更新未通过的)*/
    List<MSUserApiDto> apply(List<MSUserApiDto> msUserApiDtos);

    /*用户根据名字查询*/
    List<MSUserApiDto> findMSUserApi();
}
