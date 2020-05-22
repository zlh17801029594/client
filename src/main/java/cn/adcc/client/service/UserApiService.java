package cn.adcc.client.service;

import cn.adcc.client.DO.UserApi;
import cn.adcc.client.DO.UserApiKey;
import cn.adcc.client.DTO.UserApiDto;

import java.util.List;

public interface UserApiService {
    List<UserApiDto> findByUserIdAndStatusNotExpire(Long userId);

    void save(UserApiDto userApiDto);

    void delete(UserApiDto userApiDto);

    void saveBatch(List<UserApi> userApis) ;

    void updateStatusOnBatch(List<UserApiKey> ids);

    void updateStatusOffBatch(List<UserApiKey> ids);

    void deleteBatch(List<UserApiKey> ids);

    List<UserApiDto> findByUserId(Long userId);

    List<UserApiDto> findByApiId(Long apiId);

    UserApi findByKey(UserApiKey userApiKey);
}
