package cn.adcc.client.service.impl;

import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DO.MSUserApi;
import cn.adcc.client.DTO.MSUserApiDto;
import cn.adcc.client.enums.MSApiStatusEnum;
import cn.adcc.client.enums.MSUserApiStatusEnum;
import cn.adcc.client.exception.MSUserApiException;
import cn.adcc.client.repository.MSUserApiRepository;
import cn.adcc.client.repository.MSUserRepository;
import cn.adcc.client.service.MSUserApiService;
import cn.adcc.client.service.UserService;
import cn.adcc.client.utils.BeanFindNullUtils;
import cn.adcc.client.utils.EmptyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class MSUserApiServiceImpl implements MSUserApiService {
    @Autowired
    private UserService userService;

    @Autowired
    private MSUserApiRepository msUserApiRepository;

    @Autowired
    private MSUserRepository msUserRepository;

    @Override
    public List<MSUserApiDto> findAllMSUserAPi() {
        List<MSUserApiDto> msUserApiDtos = new ArrayList<>();
        List<MSUserApi> msUserApiList = msUserApiRepository.findAll();
        Map<String, List<MSUserApiDto>> userApiListMap = new HashMap<>();
        if (EmptyUtils.isNotEmpty(msUserApiList)) {
            msUserApiList.forEach(msUserApi -> {
                MSUserApiDto msUserApiDto = new MSUserApiDto();
                BeanUtils.copyProperties(msUserApi, msUserApiDto);
//                String username = msUserApi.getUsername();
                String username = "admin";
                List<MSUserApiDto> msUserApiDtoList = userApiListMap.get(username);
                if (msUserApiDtoList == null) {
                    msUserApiDtoList = new ArrayList<>();
                    userApiListMap.put(username, msUserApiDtoList);
                    msUserApiDtoList.add(msUserApiDto);
                } else {
                    msUserApiDtoList.add(msUserApiDto);
                }
            });
        }
        Long i = 10000L;
        if (EmptyUtils.isNotEmpty(userApiListMap)) {
            userApiListMap.keySet()
                    .forEach(username -> {
                        MSUserApiDto msUserApiDto = new MSUserApiDto();
                        /*前端展示需要，临时id*/
                        msUserApiDto.setId(i.longValue() + 1);
                        msUserApiDto.setUsername(username);
                        msUserApiDto.setChildren(userApiListMap.get(username));
                        msUserApiDtos.add(msUserApiDto);
                    });
        }
        return msUserApiDtos;
    }

    @Override
    public List<MSUserApiDto> update(List<MSUserApiDto> msUserApiDtos) {
        return null;
    }

    @Override
    public List<MSUserApiDto> apply(List<MSUserApiDto> msUserApiDtos) {
        /*真实环境换成通过当前用户获取username*/
        String username = userService.getUser().getUsername();
        if (EmptyUtils.isNotEmpty(msUserApiDtos)) {
            msUserApiDtos.forEach(msUserApiDto -> {
                Long apiRef = msUserApiDto.getApiRef();
                Date expiringTime = msUserApiDto.getExpiringTime();
//                MSUserApi msUserApi = msUserApiRepository.findMSUserApiByUsernameAndApiRef(username, apiRef);
                MSUserApi msUserApi = null;
                if (msUserApi == null) {
                    msUserApi = new MSUserApi();
                    BeanUtils.copyProperties(msUserApiDto, msUserApi, BeanFindNullUtils.findNull(msUserApiDto));
//                    msUserApi.setUsername(username);
                    msUserApi.setApplyTime(new Timestamp(System.currentTimeMillis()));
                    //msUserApi.setStatus(MSUserApiStatusEnum.APPROVE.getCode());
                    msUserApiRepository.save(msUserApi);
                } else {
                    /*if (MSUserApiStatusEnum.DENY.getCode().equals(msUserApi.getStatus())) {
                        BeanUtils.copyProperties(msUserApiDto, msUserApi, BeanFindNullUtils.findNull(msUserApiDto));
                        msUserApi.setApplyTime(new Timestamp(System.currentTimeMillis()));
                        msUserApi.setStatus(MSUserApiStatusEnum.APPROVE.getCode());
                        msUserApiRepository.save(msUserApi);
                    } else {
                        throw new MSUserApiException(0, "数据操作异常、刷新后再试");
                    }*/
                }
            });
        }
        /*局部更新数据需要返回，vuex需要返回*/
        return msUserApiDtos;
    }

    @Override
    public List<MSUserApiDto> findMSUserApi() {
        return null;
    }

    @Override
    public MSUser findMSUserByUsername(String username) {
        MSUser msUser = msUserRepository.findMSUserByUsername(username);
        return msUser;
    }
}
