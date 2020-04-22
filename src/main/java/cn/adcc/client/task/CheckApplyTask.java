package cn.adcc.client.task;

import cn.adcc.client.service.MSApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 1.检查待审批状态申请是否过期(审批时再次判断)
 * 2.检查待审批状态申请是否失效(msApiService可以代替这个)
 */
@Component
public class CheckApplyTask {

    @Autowired
    private MSApplyService msApplyService;

    @Scheduled(fixedRate = 5000)
    public void checkApply() {

    }
}
