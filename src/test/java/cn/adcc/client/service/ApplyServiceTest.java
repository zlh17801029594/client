package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DO.Api;
import cn.adcc.client.DO.Apply;
import cn.adcc.client.DO.ApplyDetails;
import cn.adcc.client.DTO.ApplyDetailsDto;
import cn.adcc.client.DTO.ApplyDto;
import cn.adcc.client.repository.ApiRepository;
import cn.adcc.client.repository.ApplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
class ApplyServiceTest extends ClientApplicationTests {
    @Autowired
    private ApplyService applyService;
    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private ApiRepository apiRepository;

    @Test
    void save() {
        Apply apply = new Apply();
        apply.setUserId(11L);
        apply.setStatus(0);
        apply.setUsername("xiaoxiao");
        applyRepository.save(apply);
    }

    @Test
    void delete() {
    }

    @Test
    void saveApplyDto() {
        ApplyDto applyDto = new ApplyDto();
        applyDto.setExpireTime(new Date());
        ApplyDetailsDto applyDetails = new ApplyDetailsDto();
        applyDetails.setApiId(532L);
        applyDto.setApplyDetailsDtos(Collections.singletonList(applyDetails));
        applyService.save(applyDto);
    }

    @Test
    void findByApply() {
        Set<ApplyDetails> applyDetailss = new HashSet<>();
        Api api = apiRepository.getOne(463L);
        System.out.println(api);
        applyDetailss.addAll(api.getApplyDetailss());
        System.out.println(applyDetailss);
        api = apiRepository.getOne(480L);
        System.out.println(api);
        applyDetailss.addAll(api.getApplyDetailss());
        System.out.println(applyDetailss);
        applyService.updateApplyByApiDel(applyDetailss);
    }

    @Test
    void list() {
    }

    @Test
    void listByUser() {
        Apply apply = applyRepository.findById(1L).get();
        System.out.println(apply);
        System.out.println(apply.getUser());
    }
}