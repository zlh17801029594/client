package cn.adcc.client.repository;

import cn.adcc.client.DO.MSApi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MSApiRepository extends JpaRepository<MSApi, Long> {

    /*根据Url查询服务*/
    MSApi findMSApiByUrl(String url);

    /*根据Url和method查询接口*/
    MSApi findMSApiByUrlAndHttpMethod(String url, String httpMethod);

    /*查询orderNum*/
    MSApi findFirstByPidOrderByOrderNumDesc(Long pid);

    /*查询id不在ids中的其他接口信息，微服务不参与*/
    List<MSApi> findMSApisByIdNotInAndPidNot(List<Long> ids, Long pid);

    /*查询所有指定接口*/
    List<MSApi> findMSApisByPidOrderByOrderNumDesc(Long pid);

    /*查询所有敏感级别可见接口*/
    List<MSApi> findMSApisByPidAndStatusAndSensitiveNumLessThanEqualOrderByOrderNumDesc(Long pid, Integer status, Integer sensitiveNum);
}
