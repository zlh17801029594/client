package cn.adcc.client.service.impl;

import cn.adcc.client.DO.ApiDetails;
import cn.adcc.client.DTO.ApiDetailsDto;
import cn.adcc.client.repository.ApiDetailsRepository;
import cn.adcc.client.service.ApiDetailsService;
import cn.adcc.client.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiDetailsServiceImpl implements ApiDetailsService {
    @Autowired
    private ApiDetailsRepository apiDetailsRepository;
    @Autowired
    private ApiServiceImpl apiServiceImpl;

    /**
     * 需要转化OtherInfo信息
     * @param id
     * @return
     */
    @Override
    public ApiDetailsDto findById(Long id) {
        Optional<ApiDetails> apiDetailsOptional = apiDetailsRepository.findById(id);
        return apiDetailsOptional.map(apiDetails -> {
            ApiDetailsDto apiDetailsDto = CopyUtil.copy(apiDetails, ApiDetailsDto.class);
            if (apiDetailsDto.getOtherInfo() != null) {
                apiDetailsDto.setOtherInfoView(apiServiceImpl.tranOtherInfo(apiDetailsDto.getOtherInfo()));
            }
            return apiDetailsDto;
        }).orElse(null);
    }
}
