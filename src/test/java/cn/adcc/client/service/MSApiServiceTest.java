package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DTO.MSApiDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Component
class MSApiServiceTest extends ClientApplicationTests {

    @Autowired
    private MSApiService msApiService;

    @Test
    void buildMSApiDto() {
    }

    @Test
    void updateMSApi() {
    }

    @Test
    void findAllMSApis() {
        List<MSApiDto> msApiDtos = msApiService.findAllMSApis();
        System.out.println(msApiDtos);
    }

    @Test
    void findMSApisBySensitiveAndOn() {
    }

    @Test
    void on() {
        msApiService.on(Arrays.asList(326L, 327L));
    }

    @Test
    void off() {
        msApiService.off(Arrays.asList(326L, 327L));
    }

    @Test
    void del() {
        msApiService.del(Arrays.asList(320L));
    }

    @Test
    void sensitive() {
        msApiService.sensitive(10, Arrays.asList(320L));
    }
}