package cn.adcc.client.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateRedisTask {

    @Scheduled(fixedRate = 6000)
    public void updateRedis() {

    }
}
