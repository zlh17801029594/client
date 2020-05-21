package cn.adcc.client.service.impl;

import cn.adcc.client.DO.Api;
import cn.adcc.client.DO.Apply;
import cn.adcc.client.DO.ApplyDetails;
import cn.adcc.client.DO.UserApi;
import cn.adcc.client.DTO.*;
import cn.adcc.client.VO.PageRequestDto;
import cn.adcc.client.enums.MSApiStatusEnum;
import cn.adcc.client.enums.MSApplyStatusEnum;
import cn.adcc.client.exception.BusinessException;
import cn.adcc.client.repository.ApplyRepository;
import cn.adcc.client.service.*;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApplyServiceImpl implements ApplyService {
    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private UserApiService userApiService;
    @Autowired
    private ApiService apiService;
    @Autowired
    private UserService userService;
    @Autowired
    private SsoUserService ssoUserService;
    @Autowired
    private ApplyService applyService;

    @Override
    public List<ApplyDto> findByUserIdAndStatusApply(Long userId) {
        List<Apply> applies = applyRepository.findByUserIdAndStatus(userId, MSApplyStatusEnum.APPLY.getCode());
        return applies.stream().map(apply -> {
            ApplyDto applyDto = CopyUtil.copy(apply, ApplyDto.class);
            applyDto.setApplyDetailsDtos(CopyUtil.copyList(apply.getApplyDetailss(), ApplyDetailsDto.class));
            return applyDto;
        }).collect(Collectors.toList());

    }

    @Override
    public void save(ApplyDto applyDto) {
        log.info("[新增申请], {}", applyDto);
        SsoUser ssoUser = ssoUserService.getSsoUser();
        UserDto userDto = userService.findByUsername(ssoUser.getUsername());
        if (userDto == null) {
            userDto = new UserDto();
            userDto.setUsername(ssoUser.getUsername());
            userDto.setSensitiveNum(ssoUser.getSensitiveLevel());
            userDto = userService.save(userDto);
        }
        applyDto.setUserId(userDto.getId());
        applyDto.setUsername(userDto.getUsername());
        applyDto.setApplyTime(new Date());
        applyDto.setStatus(MSApplyStatusEnum.APPLY.getCode());
        applyDto.getApplyDetailsDtos().forEach(applyDetailsDto -> {
            ApiDto apiDto = apiService.findApiById(applyDetailsDto.getApiId());
            if (apiDto == null) {
                log.error("[数据不一致] [对应接口数据不存在], {}", applyDetailsDto.getApiId());
                throw new BusinessException();
            }
            applyDetailsDto.setApiName(apiDto.getName());
            applyDetailsDto.setApiUrl(apiDto.getUrl());
            applyDetailsDto.setApiHttpMethod(apiDto.getHttpMethod());
        });
        Set<Long> ids = applyDto.getApplyDetailsDtos().stream().map(ApplyDetailsDto::getApiId).collect(Collectors.toSet());
        /*用户所有可见接口*/
        List<ApiDto> apiDtos = apiService.findByTypeTrueAndStatusOnAndSensitive(userDto.getSensitiveNum());
        Set<Long> ids3 = apiDtos.stream()
                .map(ApiDto::getId)
                .collect(Collectors.toSet());
        if (ids3.containsAll(ids)) {
            /*待审批状态 接口*/
            List<ApplyDto> applyDtos = this.findByUserIdAndStatusApply(userDto.getId());
            Set<Long> ids1 = applyDtos.stream()
                    .flatMap(applyDto1 -> applyDto1.getApplyDetailsDtos().stream())
                    .map(ApplyDetailsDto::getApiId)
                    .collect(Collectors.toSet());
            /*未过期状态 接口*/
            List<UserApiDto> userApiDtos = userApiService.findByUserIdAndStatusNotExpire(userDto.getId());
            Set<Long> ids2 = userApiDtos.stream()
                    .map(UserApiDto::getApiId)
                    .collect(Collectors.toSet());
            ids1.addAll(ids2);
            Set<Long> ids4 = ids.stream()
                    .filter(ids1::contains)
                    .collect(Collectors.toSet());
            if (ids4.isEmpty()) {
                Apply apply = CopyUtil.copy(applyDto, Apply.class);
                apply.setApplyDetailss(CopyUtil.copyList(applyDto.getApplyDetailsDtos(), ApplyDetails.class));
                apply.getApplyDetailss().forEach(applyDetails -> applyDetails.setApply(apply));
                applyRepository.save(apply);
                return;
            }
        }
        log.error("[数据不一致] [接口不可申请], {}", applyDto.getApplyDetailsDtos());
        throw new BusinessException();
    }

    @Override
    public void delete(ApplyDto applyDto) {

    }

    @Override
    @Transactional
    public void updateStatusPass(Long id) {
        log.info("[更新申请状态] [待审批=>已通过], {}", id);
        Apply apply = this.validate(id, MSApplyStatusEnum.APPLY.getCode());
        apply.setStatus(MSApplyStatusEnum.PASS.getCode());
        List<UserApi> userApis = apply.getApplyDetailss().stream().map(applyDetails -> {
            UserApi userApi = new UserApi();
            userApi.setUser(apply.getUser());
            userApi.setApplyTime(apply.getApplyTime());
            userApi.setExpireTime(apply.getExpireTime());
            userApi.setApi(applyDetails.getApi());
            userApi.setStatus(MSApiStatusEnum.ON.getCode());
            return userApi;
        }).collect(Collectors.toList());
        userApiService.saveBatch(userApis);
        applyRepository.save(apply);
    }

    @Override
    @Transactional
    public void updateStatusDeny(Long id) {
        log.info("[更新申请状态] [待审批=>未通过], {}", id);
        Apply apply = this.validate(id, MSApplyStatusEnum.APPLY.getCode());
        apply.setStatus(MSApplyStatusEnum.DENY.getCode());
        applyRepository.save(apply);
    }

    /**
     * 任何状态均可删除
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("[删除申请], {}", id);
        Apply apply = this.validate(id, null);
        applyRepository.delete(apply);
    }

    private Apply validate(Long id, Integer status) {
        Optional<Apply> applyOptional = applyRepository.findById(id);
        if (applyOptional.isPresent()) {
            Apply apply = applyOptional.get();
            /*状态是否一致*/
            if (status != null && !status.equals(apply.getStatus())) {
                log.error("[数据不一致] [申请状态应为: {}], {}", status, apply);
                throw new BusinessException();
            }
            return apply;
        }
        /*数据是否存在*/
        log.error("[数据不一致] [对应数据不存在], {}", id);
        throw new BusinessException();
    }

    @Override
    @Transactional
    public void updateApplyByApiDel(Set<ApplyDetails> applyDetailss) {
        List<Apply> applies = applyRepository.findDistinctByStatusAndApplyDetailssIn(MSApplyStatusEnum.APPLY.getCode(), applyDetailss);
        if (!applies.isEmpty()) {
            Set<Long> idSet = applyDetailss.stream().map(ApplyDetails::getId).collect(Collectors.toSet());
            applies.forEach(apply -> {
                List<String> badUrls = new ArrayList<>();
                apply.getApplyDetailss().stream().filter(applyDetails -> idSet.contains(applyDetails.getId())).forEach(applyDetails -> {
                    badUrls.add(applyDetails.getApiUrl() + "::" + applyDetails.getApiHttpMethod());
                });
                apply.setStatus(MSApplyStatusEnum.DISABLED.getCode());
                apply.setReason(String.format("%s已被移除", badUrls));
            });
            log.info("[更新申请状态] [待审批=>已失效], {}", applies);
            applyRepository.saveAll(applies);
        }
    }

    @Override
    @Transactional
    public void updateApplyByApiSens(Set<ApplyDetails> applyDetailss, Integer apiSens) {
        List<Apply> applies = applyRepository.findDistinctByStatusAndApplyDetailssIn(MSApplyStatusEnum.APPLY.getCode(), applyDetailss);
        if (!applies.isEmpty()) {
            Set<Long> idSet = applyDetailss.stream().map(ApplyDetails::getId).collect(Collectors.toSet());
            applies.stream().filter(apply -> apply.getUser().getSensitiveNum() < apiSens).forEach(apply -> {
                List<String> badUrls = new ArrayList<>();
                apply.getApplyDetailss().stream().filter(applyDetails -> idSet.contains(applyDetails.getId())).forEach(applyDetails -> {
                    badUrls.add(applyDetails.getApiUrl() + "::" + applyDetails.getApiHttpMethod());
                });
                apply.setStatus(MSApplyStatusEnum.DISABLED.getCode());
                apply.setReason(String.format("[接口敏感级别调整]，%s敏感级别高于用户", badUrls));
            });
            log.info("[更新申请状态] [待审批=>已失效], {}", applies);
            applyRepository.saveAll(applies);
        }
    }

    @Override
    public void list(PageRequestDto<ApplyDto> pageRequestDto) {
        ApplyDto applyDto = pageRequestDto.getData();

        Apply apply = CopyUtil.copy(applyDto, Apply.class);
        Example<Apply> example = Example.of(apply);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "applyTime");
        Sort sort = Sort.by(order);
        PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getLimit(), sort);
        Page<Apply> applyPage = applyRepository.findAll(example, pageRequest);
        List<Apply> applyList = applyPage.getContent();
        List<ApplyDto> applyDtoList = applyList.stream().map(apply1 -> {
            ApplyDto applyDto1 = CopyUtil.copy(apply1, ApplyDto.class);
            applyDto1.setApplyDetailsDtos(CopyUtil.copyList(apply1.getApplyDetailss(), ApplyDetailsDto.class));
            return applyDto1;
        }).collect(Collectors.toList());
        pageRequestDto.setList(applyDtoList);
        pageRequestDto.setTotal(applyPage.getTotalElements());
    }

    @Override
    public void listByUser(PageRequestDto<ApplyDto> pageRequestDto) {
    }
}
