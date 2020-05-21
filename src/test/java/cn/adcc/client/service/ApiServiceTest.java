package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DTO.ApiDetailsDto;
import cn.adcc.client.DTO.ApiDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Component
class ApiServiceTest extends ClientApplicationTests {
    @Autowired
    private ApiService apiService;

    @Test
    void save() {
        ApiDto apiDto = new ApiDto();
        apiDto.setName("小明");
        apiDto.setUrl("/misc");
        apiDto.setHttpMethod("/a");
        apiDto.setType(false);
        ApiDetailsDto apiDetailsDto = new ApiDetailsDto();
        apiDetailsDto.setOtherInfo("小周");
        apiDto.setApiDetailsDto(apiDetailsDto);
//        apiService.save(apiDto);
    }

    @Test
    void delete() {
        apiService.delete(463L);

    }

    @Test
    void deleteBatch() {
        List<Long> ids = Arrays.asList(463L, 480L);
//        apiService.deleteBatch(ids);
        apiService.updateSensBatch(ids, 6);
    }

    @Test
    void findById() {
        ApiDto apiDto = apiService.findById(963L);
        System.out.println(apiDto);
    }
}