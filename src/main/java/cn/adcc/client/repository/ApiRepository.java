package cn.adcc.client.repository;

import cn.adcc.client.DO.Api;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ApiRepository extends JpaRepository<Api, Long> {

    /**
     * 根据url、method查询接口
     *
     * @param url
     * @param httpMethod
     * @return
     */
    Api findByTypeTrueAndPidAndUrlAndHttpMethod(Long pid, String url, String httpMethod);

    /**
     * 根据url、查询微服务
     *
     * @param url
     * @return
     */
    Api findByTypeFalseAndUrl(String url);

    /*查询微服务排序码*/
    Api findFirstByTypeFalseOrderByOrderNumDesc();

    /*查询接口排序码*/
    Api findFirstByTypeTrueAndPidOrderByOrderNumDesc(Long pid);

    /*查询未出现在本次更新的非“未生效”接口*/
    List<Api> findByTypeTrueAndStatusNotAndIdNotIn(Integer status, Set<Long> ids);

    /*查询未出现在本次更新的非“未生效”接口2  适用于ids为空情况*/
    List<Api> findByTypeTrueAndStatusNot(Integer status);

    /**
     * 根据pid，查询接口数目
     */
    int countByTypeTrueAndPid(Long pid);

    /**
     * 根据pid、查询接口(排序)
     */
    List<Api> findByTypeTrueAndPidOrderByOrderNum(Long pid);

    /**
     * 根据pid、status查询接口(排序)
     * @param pid
     * @param status
     * @return
     */
    List<Api> findByTypeTrueAndPidAndStatusOrderByOrderNum(Long pid, Integer status);

    /**
     * 根据pid、sensitiveNum、status查询接口(排序)
     * @param pid
     * @param sensitiveNum
     * @param status
     * @return
     */
    List<Api> findByTypeTrueAndPidAndStatusAndSensitiveNumLessThanEqualOrderByOrderNum(Long pid, Integer status, Integer sensitiveNum);

    /*查询所有用户可申请接口（只查接口）*/
    List<Api> findByTypeTrueAndStatusAndSensitiveNumLessThanEqual(Integer status, Integer sens);

    /**
     * 查询所有微服务(排序)
     */
    List<Api> findByTypeFalseOrderByOrderNum();

    /**
     * 根据ids 查询接口
     * @param ids
     * @return
     */
    List<Api> findByTypeTrueAndIdIn(List<Long> ids);

    /*redis同步接口*/
    List<Api> findByTypeTrueAndStatus(Integer status);
}
