package cn.adcc.client.service;

import cn.adcc.client.DO.MSApply;

import java.util.List;

public interface MSApplyService {
    /**
     * 查询所有申请并按申请时间倒序
     * @return
     */
    List<MSApply> findMSApplies();

    /**
     * 根据用户名获取当前用户所有申请并按申请时间倒序
     * @param username
     * @return
     */
    List<MSApply> findMSAppliesByUsername(String username);
}
