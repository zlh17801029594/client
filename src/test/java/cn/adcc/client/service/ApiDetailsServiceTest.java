package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DO.ApiDetails;
import cn.adcc.client.DTO.ApiDetailsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Component
class ApiDetailsServiceTest extends ClientApplicationTests {
    @Autowired
    private ApiDetailsService apiDetailsService;

    @Test
    void findById() {
        ApiDetailsDto apiDetailsDto = apiDetailsService.findById(999L);
        System.out.println(apiDetailsDto);
    }
}