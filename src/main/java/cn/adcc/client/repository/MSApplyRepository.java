package cn.adcc.client.repository;

import cn.adcc.client.DO.MSApply;
import cn.adcc.client.DO.MSUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MSApplyRepository extends JpaRepository<MSApply, Long> {
    /*考虑排序*/
    List<MSApply> findMSAppliesByOrderByApplyTime();

    List<MSApply> findMSAppliesByMsUserOrderByApplyTime(MSUser msUser);


    /**
     * 查询 当前用户处于 待审批状态(0) 所有申请接口。
     * 过期申请接口、未通过申请接口、已通过但过期接口 可再次申请；
     * 待审批、已通过(未过期)所有接口 不可再次申请
     */
    List<MSApply> findMSAppliesByMsUserAndStatus(MSUser msUser, Integer status);
}
