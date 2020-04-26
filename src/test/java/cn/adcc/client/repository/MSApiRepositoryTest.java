package cn.adcc.client.repository;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DO.MSApi;
import cn.adcc.client.enums.MSApiStatusEnum;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Component
class MSApiRepositoryTest extends ClientApplicationTests {

    @Autowired
    private MSApiRepository msApiRepository;


    @Test
    public void testFind() {
    }

    @Test
    public void testMsApi() {
//        select();
//        insert();
        update();
    }

    private void select() {
        Optional<MSApi> msApiOptional = msApiRepository.findById(1L);
        if (!msApiOptional.isPresent()){
            throw new RuntimeException("无数据");
        }
        MSApi msApi = msApiOptional.get();
        if (msApi.getHttpMethod() != null && msApi.getHttpMethod().equalsIgnoreCase("get")) {
            msApi.setHttpMethod("post");
        } else {
            msApi.setHttpMethod("get");
        }
        msApiRepository.save(msApi);
    }

    private void insert() {
        MSApi msApi = new MSApi();
        msApi.setPid(0L);
        msApi.setName("航班信息服务");
        msApi.setUrl("/flightinfo");
        msApi.setOrderNum(0);
        msApi.setSensitiveNum(0);
//        msApi.setHttpMethod("post");
        msApi.setDeprecated(false);
        msApi.setStatus(MSApiStatusEnum.ON.getCode());
        msApi = msApiRepository.save(msApi);
        System.out.println(String.format("返回值：{%s}", msApi));
        Assert.notNull(msApi, "没错吧");
    }

    private void update() {
        MSApi msApi = new MSApi();
        msApi.setId(4L);
        msApi.setPid(0L);
        msApi.setName("航班信息服务");
        msApi.setUrl("/flightinfo");
        msApi.setOrderNum(0);
        msApi.setSensitiveNum(1);
        msApi.setDeprecated(false);
        msApi.setStatus(MSApiStatusEnum.ON.getCode());

        /*通常处理*/
        Optional<MSApi> msApiOptional = msApiRepository.findById(msApi.getId());
        if (!msApiOptional.isPresent()){
            throw new RuntimeException("无数据");
        }
        MSApi msApi1 = msApiOptional.get();
        String[] ignoredProperties = Arrays.array("updateTime", "createTime");
        BeanUtils.copyProperties(msApi, msApi1, ignoredProperties);
        msApi = msApiRepository.save(msApi1);

//        msApi = msApiRepository.save(msApi);
        System.out.println(String.format("返回值：{%s}", msApi));
        Assert.notNull(msApi, "没错吧");
    }
}