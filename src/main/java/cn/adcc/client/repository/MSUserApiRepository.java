package cn.adcc.client.repository;

import cn.adcc.client.DO.MSApi;
import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DO.MSUserApi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MSUserApiRepository extends JpaRepository<MSUserApi, Long> {

    /*获取当前用户所有用户接口关系*/
    List<MSUserApi> findMSUserApisByMsUser(MSUser msUser);

    /*查询用户启用/停用状态的已过期接口*/
    List<MSUserApi> findMSUserApisByStatusNotAndExpireTimeBefore(Integer status, Date nowTime);

    /*查询用户 未过期接口*/
    List<MSUserApi> findMSUserApisByMsUserAndStatusNot(MSUser msUser, Integer status);

    List<MSUserApi> findMSUserApisByIdIn(List<Long> ids);

    List<MSUserApi> findMSUserApisByStatus(Integer status);

    MSUserApi findMSUserApiByMsUserAndMsApi(MSUser msUser, MSApi msApi);
}
