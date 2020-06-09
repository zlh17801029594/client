package cn.adcc.client.service.impl;

import cn.adcc.client.DO.*;
import cn.adcc.client.DTO.*;
import cn.adcc.client.VO.PageRequestDto;
import cn.adcc.client.enums.ApiStatusEnum;
import cn.adcc.client.enums.ApplyStatusEnum;
import cn.adcc.client.exception.BusinessException;
import cn.adcc.client.repository.ApplyRepository;
import cn.adcc.client.service.*;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Override
    public List<ApplyDto> findByUserIdAndStatusApply(Long userId) {
        List<Apply> applies = applyRepository.findByUserIdAndStatus(userId, ApplyStatusEnum.APPLY.getCode());
        return applies.stream().map(apply -> {
            ApplyDto applyDto = CopyUtil.copy(apply, ApplyDto.class);
            applyDto.setApplyDetailsDtos(CopyUtil.copyList(apply.getApplyDetailss(), ApplyDetailsDto.class));
            return applyDto;
        }).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void save(ApplyDto applyDto) {
        log.info("[新增申请], {}", applyDto);
        SsoUser ssoUser = ssoUserService.getSsoUser();
        User user = userService.findByUsername(ssoUser.getUsername());
        if (user == null) {
            user = new User();
            user.setUsername(ssoUser.getUsername());
            user.setSensitiveNum(ssoUser.getSensitiveLevel());
            userService.save(user);
        }
        applyDto.setUserId(user.getId());
        applyDto.setUsername(user.getUsername());
        applyDto.setApplyTime(new Date());
        applyDto.setStatus(ApplyStatusEnum.APPLY.getCode());
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
        List<ApiDto> apiDtos = apiService.findByTypeTrueAndStatusOnAndSensitive(user.getSensitiveNum());
        Set<Long> ids3 = apiDtos.stream()
                .map(ApiDto::getId)
                .collect(Collectors.toSet());
        if (ids3.containsAll(ids)) {
            /*待审批状态 接口*/
            List<ApplyDto> applyDtos = this.findByUserIdAndStatusApply(user.getId());
            Set<Long> ids1 = applyDtos.stream()
                    .flatMap(applyDto1 -> applyDto1.getApplyDetailsDtos().stream())
                    .map(ApplyDetailsDto::getApiId)
                    .collect(Collectors.toSet());
            /*未过期状态 接口*/
            List<UserApiDto> userApiDtos = userApiService.findByUserIdAndStatusNotExpire(user.getId());
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
        log.info("[更新申请状态] [待审批=>全部通过], {}", id);
        Apply apply = this.validate(id, ApplyStatusEnum.APPLY.getCode());
        this.savePassApply(apply);
    }

    private void savePassApply(Apply apply) {
        apply.setStatus(ApplyStatusEnum.PASS.getCode());
        List<UserApi> userApis = apply.getApplyDetailss().stream().map(applyDetails -> {
            UserApiKey userApiKey = new UserApiKey();
            userApiKey.setUserId(apply.getUserId());
            userApiKey.setApiId(applyDetails.getApiId());
            UserApi userApi = userApiService.findByKey(userApiKey);
            if (userApi == null) {
                userApi = new UserApi();
            }
            userApi.setUser(apply.getUser());
            userApi.setApplyTime(apply.getApplyTime());
            userApi.setExpireTime(apply.getExpireTime());
            userApi.setApi(applyDetails.getApi());
            userApi.setStatus(ApiStatusEnum.ON.getCode());
            return userApi;
        }).collect(Collectors.toList());
        applyRepository.save(apply);
        userApiService.saveBatch(userApis);
    }

    @Override
    @Transactional
    public void updateStatusPassSome(ApplyDto applyDto) {
        log.info("[更新申请状态] [待审批=>部分通过], {}", applyDto);
        Set<Long> applyDetailsDtoIds = applyDto.getApplyDetailsDtos().stream().map(ApplyDetailsDto::getId).collect(Collectors.toSet());
        Apply apply = this.validate(applyDto.getId(), ApplyStatusEnum.APPLY.getCode());
        Set<Long> applyDetailsIds = apply.getApplyDetailss().stream().map(ApplyDetails::getId).collect(Collectors.toSet());
        if (applyDetailsIds.containsAll(applyDetailsDtoIds)){
            if (applyDetailsDtoIds.size() == applyDetailsIds.size()) {
                /*全部通过*/
                log.info("[更新申请状态] [待审批=>全部通过], {}", applyDto);
                this.savePassApply(apply);
            } else {
                /*部分通过*/
                apply.setStatus(ApplyStatusEnum.PASS_SOME.getCode());
                List<UserApi> userApis = new ArrayList<>();
                apply.getApplyDetailss().forEach(applyDetails -> {
                    if (applyDetailsDtoIds.contains(applyDetails.getId())) {
                        applyDetails.setStatus(true);
                        UserApiKey userApiKey = new UserApiKey();
                        userApiKey.setUserId(apply.getUserId());
                        userApiKey.setApiId(applyDetails.getApiId());
                        UserApi userApi = userApiService.findByKey(userApiKey);
                        if (userApi == null) {
                            userApi = new UserApi();
                        }
                        userApi.setUser(apply.getUser());
                        userApi.setApplyTime(apply.getApplyTime());
                        userApi.setExpireTime(apply.getExpireTime());
                        userApi.setApi(applyDetails.getApi());
                        userApi.setStatus(ApiStatusEnum.ON.getCode());
                        userApis.add(userApi);
                    } else {
                        applyDetails.setStatus(false);
                    }
                });
                applyRepository.save(apply);
                /*redis操作放到最后，避免发生回滚，redis却更新了*/
                userApiService.saveBatch(userApis);
            }
        }else{
            /*审批数据不在申请中*/
            log.error("[数据不一致] [对应数据不在申请中], 申请详情数据：{},提交数据：{}", applyDetailsIds, applyDetailsDtoIds);
            throw new BusinessException();
        }
    }

    @Override
    @Transactional
    public void updateStatusDeny(Long id) {
        log.info("[更新申请状态] [待审批=>未通过], {}", id);
        Apply apply = this.validate(id, ApplyStatusEnum.APPLY.getCode());
        apply.setStatus(ApplyStatusEnum.DENY.getCode());
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
        Apply apply = this.validateDelete(id, ApplyStatusEnum.APPLY.getCode());
//        applyRepository.delete(apply);
        apply.setDelAdmin(true);
        applyRepository.save(apply);
    }

    private Apply validateDelete(Long id, Integer status) {
        Optional<Apply> applyOptional = applyRepository.findById(id);
        if (applyOptional.isPresent()) {
            Apply apply = applyOptional.get();
            /*状态是否一致*/
            if (status != null && status.equals(apply.getStatus())) {
                log.error("[数据不一致] [申请状态不能为: {}], {}", status, apply);
                throw new BusinessException();
            }
            return apply;
        }
        /*数据是否存在*/
        log.error("[数据不一致] [对应数据不存在], {}", id);
        throw new BusinessException();
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
        List<Apply> applies = applyRepository.findDistinctByStatusAndApplyDetailssIn(ApplyStatusEnum.APPLY.getCode(), applyDetailss);
        if (!applies.isEmpty()) {
            Set<Long> idSet = applyDetailss.stream().map(ApplyDetails::getId).collect(Collectors.toSet());
            applies.forEach(apply -> {
                List<String> badUrls = new ArrayList<>();
                apply.getApplyDetailss().stream().filter(applyDetails -> idSet.contains(applyDetails.getId())).forEach(applyDetails -> {
                    badUrls.add(applyDetails.getApiUrl() + "::" + applyDetails.getApiHttpMethod());
                });
                apply.setStatus(ApplyStatusEnum.DISABLED.getCode());
                apply.setReason(String.format("%s已被移除", badUrls));
            });
            log.info("[更新申请状态] [待审批=>已失效], {}", applies);
            applyRepository.saveAll(applies);
        }
    }

    @Override
    @Transactional
    public void updateApplyByApiSens(Set<ApplyDetails> applyDetailss, Integer apiSens) {
        List<Apply> applies = applyRepository.findDistinctByStatusAndApplyDetailssIn(ApplyStatusEnum.APPLY.getCode(), applyDetailss);
        if (!applies.isEmpty()) {
            Set<Long> idSet = applyDetailss.stream().map(ApplyDetails::getId).collect(Collectors.toSet());
            List<Apply> applyList = applies.stream().filter(apply -> apply.getUser().getSensitiveNum() < apiSens).map(apply -> {
                List<String> badUrls = new ArrayList<>();
                apply.getApplyDetailss().stream().filter(applyDetails -> idSet.contains(applyDetails.getId())).forEach(applyDetails -> {
                    badUrls.add(applyDetails.getApiUrl() + "::" + applyDetails.getApiHttpMethod());
                });
                apply.setStatus(ApplyStatusEnum.DISABLED.getCode());
                apply.setReason(String.format("[接口敏感级别调整]，%s敏感级别高于用户", badUrls));
                return apply;
            }).collect(Collectors.toList());
            if (!applyList.isEmpty()) {
                log.info("[更新申请状态] [待审批=>已失效], {}", applyList);
                applyRepository.saveAll(applyList);
            }
        }
    }

    @Override
    @Transactional
    public void updateApplyByUserSens(User user) {
        List<Apply> applies = applyRepository.findByStatusAndUserId(ApplyStatusEnum.APPLY.getCode(), user.getId());
        List<Apply> updateApplies = new ArrayList<>();
        applies.forEach(apply -> {
            List<String> badUrls = new ArrayList<>();
            apply.getApplyDetailss().forEach(applyDetails -> {
                int apiSens = applyDetails.getApi().getSensitiveNum();
                if (apiSens > user.getSensitiveNum()) {
                    badUrls.add(applyDetails.getApiUrl() + "::" + applyDetails.getApiHttpMethod());
                }
            });
            if (!badUrls.isEmpty()) {
                apply.setStatus(ApplyStatusEnum.DISABLED.getCode());
                apply.setReason(String.format("[用户敏感级别调整]，%s敏感级别高于用户", badUrls));
                updateApplies.add(apply);
            }
        });
        if (!updateApplies.isEmpty()) {
            log.info("[更新申请状态] [待审批=>已失效], {}", updateApplies);
            applyRepository.saveAll(updateApplies);
        }
    }

    @Override
    public void list(PageRequestDto<ApplyDto> pageRequestDto) {
        ApplyDto applyDto = pageRequestDto.getData();
        Apply apply = CopyUtil.copy(applyDto, Apply.class);
        String username = apply.getUsername();
        if (StringUtils.isEmpty(username)) {
            apply.setUsername(null);
        }
        // 构造ExampleMatcher
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Apply> example = Example.of(apply, matcher);
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

    @Override
    public List<Apply> findByStatusOnAndExpireTimeBeforeNow() {
        return applyRepository.findByStatusAndExpireTimeBefore(ApplyStatusEnum.APPLY.getCode(),
                new Date());
    }
}
