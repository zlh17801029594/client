package cn.adcc.client.repository;

import cn.adcc.client.DO.MSApi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MSApiRepository extends JpaRepository<MSApi, Long> {

    /*根据Url查询服务*/
    MSApi findMSApiByUrlAndDelFlagFalse(String url);

    /*根据Url和method查询接口*/
    MSApi findMSApiByUrlAndHttpMethodAndDelFlagFalse(String url, String httpMethod);

    /*查询orderNum*/
    MSApi findFirstByPidOrderByOrderNumDesc(Long pid);

    /*查询id不在ids中的其他接口信息，微服务不参与*/
    List<MSApi> findMSApisByIdNotInAndPidNotAndDelFlagFalse(List<Long> ids, Long pid);

    /*查询所有指定接口*/
    List<MSApi> findMSApisByPidAndDelFlagFalseOrderByOrderNumAsc(Long pid);

    /*查询所有启用状态接口*/
    List<MSApi> findMSApisByPidAndStatusAndDelFlagFalseOrderByOrderNumAsc(Long pid, Integer status);

    /*查询出 状态为启用、铭感级别可见 的所有接口*/
    List<MSApi> findMSApisByPidAndStatusAndSensitiveNumLessThanEqualAndDelFlagFalseOrderByOrderNumAsc(Long pid, Integer status, Integer sensitiveNum);

    /*查询id为以下的 未删除 接口*/
    List<MSApi> findMSApisByIdInAndPidNotAndDelFlagFalse(List<Long> ids, Long pid);

    /*查询用户可申请接口*/
    List<MSApi> findMSApisByPidNotAndStatusAndSensitiveNumLessThanEqualAndDelFlagFalse(Long pid, Integer status, Integer sensitiveNum);

    List<MSApi> findMSApisByPidNotAndStatusAndDelFlagFalse(Long pid, Integer status);
}
