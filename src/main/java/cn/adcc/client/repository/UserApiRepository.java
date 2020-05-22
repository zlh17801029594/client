package cn.adcc.client.repository;

import cn.adcc.client.DO.UserApi;
import cn.adcc.client.DO.UserApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserApiRepository extends JpaRepository<UserApi, UserApiKey> {

    List<UserApi> findByUserIdAndStatusNot(Long userId, Integer status);

    List<UserApi> findByUserId(Long userId);

    List<UserApi> findByApiId(Long apiId);

    /*用户接口关系过期*/
    List<UserApi> findByStatusNotAndExpireTimeBefore(Integer status, Date nowTime);

    /*用户接口关系同步redis*/
    List<UserApi> findByStatus(Integer status);
}
