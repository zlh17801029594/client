package cn.adcc.client.service;

import cn.adcc.client.DO.Apply;
import cn.adcc.client.DO.ApplyDetails;
import cn.adcc.client.DO.User;
import cn.adcc.client.DTO.ApplyDto;
import cn.adcc.client.VO.PageRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface ApplyService {
    /*查找用户待审批状态申请(判断用户可申请接口需要)*/
    List<ApplyDto> findByUserIdAndStatusApply(Long userId);

    /*新建申请*/
    void save(ApplyDto applyDto);

    void delete(ApplyDto applyDto);

    /*更新申请状态=》已通过*/
    void updateStatusPass(Long id);

    /*更新申请状态=》部分通过*/
    void updateStatusPassSome(ApplyDto applyDto);

    /*更新申请状态=》未通过*/
    void updateStatusDeny(Long id);

    /*删除申请*/
    void deleteById(Long id);

    /*接口删除、申请状态变更*/
    void updateApplyByApiDel(Set<ApplyDetails> applyDetailss);

    /*接口敏感级别调高、申请状态变更*/
    void updateApplyByApiSens(Set<ApplyDetails> applyDetailss, Integer apiSens);

    /*用户敏感级别调低、申请状态变更*/
    void updateApplyByUserSens(User user);

    void list(PageRequestDto<ApplyDto> pageRequestDto);

    @Deprecated
    void listByUser(PageRequestDto<ApplyDto> pageRequestDto);

    /*判断过期申请*/
    @Deprecated
    List<Apply> findByStatusOnAndExpireTimeBeforeNow();
}
