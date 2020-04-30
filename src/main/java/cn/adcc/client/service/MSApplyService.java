package cn.adcc.client.service;

import cn.adcc.client.DO.MSApply;
import cn.adcc.client.DO.MSUser;

import java.util.Date;
import java.util.List;

public interface MSApplyService {
    /**
     * 查询所有申请并按申请时间倒序
     *
     * @return
     */
    List<MSApply> findMSApplies();

    /**
     * 根据用户名获取当前用户所有申请并按申请时间倒序
     *
     * @param username
     * @return
     */
    List<MSApply> findMSAppliesByUsername(String username);

    /*新建申请*/
    void createApply(MSUser msUser, List<Long> ids, Date expireTime);

    /*通过申请*/
    void passApply(Long id);

    /*拒绝申请*/
    void denyApply(Long id);
}
