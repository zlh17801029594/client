package cn.adcc.client.repository;

import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DO.MSUserApi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MSUserApiRepository extends JpaRepository<MSUserApi, Long> {

    /*获取当前用户所有用户接口关系*/
    List<MSUserApi> findMSUserApisByMsUser(MSUser msUser);

    /*获取用户已启用接口*/
    List<MSUserApi> findMSUserApisByMsUserAndStatus(MSUser msUser, Integer status);

    /*查询用户启用/停用状态的已过期接口*/
    MSUserApi findMSUserApisByStatusNotAndExpireTimeBefore(Integer status, Date nowTime);

}
