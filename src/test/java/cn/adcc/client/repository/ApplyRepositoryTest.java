package cn.adcc.client.repository;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DO.Apply;
import cn.adcc.client.DTO.ApplyDetailsDto;
import cn.adcc.client.DTO.ApplyDto;
import cn.adcc.client.utils.CopyUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Component
class ApplyRepositoryTest extends ClientApplicationTests {
    @Autowired
    private ApplyRepository applyRepository;

    @Test
    void findDistinctByStatusAndApplyDetailssIn() {
    }

    @Test
    void findByStatusAndUserId() {
    }

    @Test
    void findByUserIdAndStatus() {
    }

    @Test
    void findByStatusAndExpireTimeBefore() {
    }

    // 单独使用 EntityGraph未产生警告
    // 单独使用Page分页也不会产生警告
    @Test
    void findAll() {
        List<Apply> applyList = applyRepository.findAllBy();
        List<ApplyDto> applyDtoList = applyList.stream().map(apply1 -> {
            ApplyDto applyDto1 = CopyUtil.copy(apply1, ApplyDto.class);
            applyDto1.setApplyDetailsDtos(CopyUtil.copyList(apply1.getApplyDetailss(), ApplyDetailsDto.class));
            return applyDto1;
        }).collect(Collectors.toList());
        System.out.println(applyDtoList);
    }
}