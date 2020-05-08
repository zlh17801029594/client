package cn.adcc.client.service.impl;

import cn.adcc.client.DO.MSApi;
import cn.adcc.client.DO.MSApply;
import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DO.MSUserApi;
import cn.adcc.client.DTO.*;
import cn.adcc.client.DTOImport.*;
import cn.adcc.client.enums.MSApiStatusEnum;
import cn.adcc.client.enums.MSApplyStatusEnum;
import cn.adcc.client.enums.MSUserApiStatusEnum;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.BusinessException;
import cn.adcc.client.exception.MSAPiException;
import cn.adcc.client.repository.MSApiRepository;
import cn.adcc.client.repository.MSApplyRepository;
import cn.adcc.client.repository.MSUserApiRepository;
import cn.adcc.client.repository.MSUserRepository;
import cn.adcc.client.service.MSApiService;
import cn.adcc.client.service.RedisService;
import cn.adcc.client.service.SwaggerApiDocService;
import cn.adcc.client.service.UserService;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.BeanFindNullUtils;
import cn.adcc.client.utils.ConvertUtils;
import cn.adcc.client.utils.EmptyUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MSApiServiceImpl implements MSApiService {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MSApiRepository msApiRepository;

    @Autowired
    private MSApplyRepository msApplyRepository;

    @Autowired
    private MSUserApiRepository msUserApiRepository;

    @Autowired
    private MSUserRepository msUserRepository;

    @Override
    public MSApiDto buildMSApiDto(SwaggerApiDoc swaggerApiDoc) {
        MSApiDto msApiDto = new MSApiDto();
        if (swaggerApiDoc != null) {
            /*微服务基础信息*/
            Info info = swaggerApiDoc.getInfo();
            if (info != null) {
                msApiDto.setName(info.getTitle());
                msApiDto.setDescription(info.getDescription());
            }
            msApiDto.setHost(swaggerApiDoc.getHost());
            msApiDto.setUrl(swaggerApiDoc.getBasePath());
            /*difinition对象信息*/
            Map<String, Definition> definitionMap = swaggerApiDoc.getDefinitions();
            if (EmptyUtils.isNotEmpty(definitionMap)) {
                definitionMap.keySet()
                        .stream().forEach(definitionName -> {
                            Definition definition = definitionMap.get(definitionName);
                            if (definition != null) {
                                Map<String, Schema> properties = definition.getProperties();
                                if (EmptyUtils.isNotEmpty(properties)) {
                                    properties.keySet()
                                            .stream()
                                            .forEach(propertyName -> {
                                                Schema propertyDetails = properties.get(propertyName);
                                                if (propertyDetails != null) {
                                                    convert(propertyDetails, definitionMap);
                                                }
                                            });
                                }
                            }
                        });
            }

            /*接口信息*/
            List<MSApiDto> msApiDtoList = new ArrayList<>();
            Map<String, Map<String, ApiDetails>> paths = swaggerApiDoc.getPaths();
            if (EmptyUtils.isNotEmpty(paths)) {
                paths.keySet()
                        .stream()
                        .forEach(pathName -> {
                            Map<String, ApiDetails> apiDetailsMap = paths.get(pathName);
                            if (EmptyUtils.isNotEmpty(apiDetailsMap)) {
                                apiDetailsMap.keySet()
                                        .stream()
                                        .forEach(method -> {
                                            ApiDetails apiDetails = apiDetailsMap.get(method);
                                            if (apiDetails != null) {
                                                MSApiDto msApiDtoChild = new MSApiDto();
                                                msApiDtoChild.setName(apiDetails.getSummary());
                                                msApiDtoChild.setDescription(apiDetails.getDescription());
                                                String apiUrl = "";
                                                String basePath = msApiDto.getUrl();
                                                if (basePath.endsWith("/")) {
                                                    apiUrl = basePath.substring(0, basePath.length() - 1) + pathName;
                                                } else {
                                                    apiUrl = basePath + pathName;
                                                }
                                                msApiDtoChild.setUrl(apiUrl);
                                                msApiDtoChild.setHttpMethod(method);
                                                msApiDtoChild.setDeprecated(apiDetails.getDeprecated());

                                                List<ApiParameterDetails> apiParameterDetailsList = apiDetails.getParameters();
                                                if (EmptyUtils.isNotEmpty(apiParameterDetailsList)) {
                                                    apiParameterDetailsList.stream()
                                                            .filter(apiParameterDetails -> apiParameterDetails != null)
                                                            .forEach(apiParameterDetails -> {
                                                                Schema schema = apiParameterDetails.getSchema();
                                                                if (schema != null) {
                                                                    convert(schema, definitionMap);
                                                                    apiParameterDetails.setType(schema.getType());
                                                                    apiParameterDetails.setFormat(schema.getFormat());
                                                                    apiParameterDetails.setItems(schema.getItems());
                                                                    apiParameterDetails.setLink(schema.getLink());
                                                                }
                                                                if (Type.STRING.equalsIgnoreCase(apiParameterDetails.getType()) && apiParameterDetails.getAllowEmptyValue() == null) {
                                                                    apiParameterDetails.setAllowEmptyValue(false);
                                                                }
                                                            });
                                                }

                                                /*otherInfo*/
                                                OtherInfo otherInfo = new OtherInfo();
                                                otherInfo.setConsumes(apiDetails.getConsumes());
                                                otherInfo.setProduces(apiDetails.getProduces());
                                                otherInfo.setParameters(apiDetails.getParameters());
                                                List<ResponseDesc> responseDescList = new ArrayList<>();
                                                otherInfo.setResponses(responseDescList);

                                                Map<String, ResponseDetails> responseDetailsMap = apiDetails.getResponses();
                                                if (EmptyUtils.isNotEmpty(responseDetailsMap)) {
                                                    responseDetailsMap.keySet()
                                                            .stream()
                                                            .forEach(code -> {
                                                                ResponseDetails responseDetails = responseDetailsMap.get(code);
                                                                if (responseDetails != null) {
                                                                    ResponseDesc responseDesc = new ResponseDesc();
                                                                    responseDesc.setCode(code);
                                                                    responseDesc.setDescription(responseDetails.getDescription());
                                                                    responseDescList.add(responseDesc);
                                                                    Schema schema = responseDetails.getSchema();
                                                                    if (schema != null) {
                                                                        convert(schema, definitionMap);
                                                                        otherInfo.setResult(schema);
                                                                    }
                                                                }
                                                            });
                                                }
                                                String otherInfoJson = null;
                                                try {
                                                    otherInfoJson = objectMapper.writeValueAsString(otherInfo);
                                                } catch (JsonProcessingException e) {
                                                    e.printStackTrace();
                                                }
                                                msApiDtoChild.setOtherInfo(otherInfoJson);
                                                msApiDtoList.add(msApiDtoChild);
                                            }
                                        });
                            }
                        });
            }
            msApiDto.setChildren(msApiDtoList);
        }
        return msApiDto;
    }


    @Override
    public void updateMSApi(List<MSApiDto> msApiDtoList) {
        List<MSApi> msApis = this.updateMSApi_(msApiDtoList);
        redisService.updateRedisApi(ConvertUtils.convertUrlSens(msApis), false);
    }



    @Transactional
    protected List<MSApi> updateMSApi_(List<MSApiDto> msApiDtoList) {
        List<Long> ids = new ArrayList<>();
        saveApi(msApiDtoList, 0L, ids);
        return disableApi(ids);
    }

    /**
     * 将未出现接口置为失效
     * @param ids
     */
    private List<MSApi> disableApi(List<Long> ids) {
        List<MSApi> redisMsApis = new ArrayList<>();
        List<MSApi> msApiList = msApiRepository.findMSApisByIdNotInAndPidNotAndDelFlagFalse(ids, 0L);
        if (EmptyUtils.isNotEmpty(msApiList)) {
            msApiList.stream()
                    .forEach(msApi -> {
                        if (MSApiStatusEnum.ON.getCode().equals(msApi.getStatus())) {
                            redisMsApis.add(msApi);
                        }
                        msApi.setStatus(MSApiStatusEnum.DISABLED.getCode());
                        msApiRepository.save(msApi);
                    });
        }
        return redisMsApis;
    }

    /**
     * 添加接口，更新接口
     * @param msApiDtoList
     * @param pid
     * @param ids
     */
    private void saveApi(List<MSApiDto> msApiDtoList, Long pid, List<Long> ids) {
        if (EmptyUtils.isNotEmpty(msApiDtoList)) {
            msApiDtoList.stream()
                    .forEach(msApiDto -> {
                        String url = msApiDto.getUrl();
                        String method = msApiDto.getHttpMethod();
                        MSApi msApi;
                        if (pid.equals(0L)) {
                            msApi = msApiRepository.findMSApiByUrlAndDelFlagFalse(url);
                        } else {
                            msApi = msApiRepository.findMSApiByUrlAndHttpMethodAndDelFlagFalse(url, method);
                        }
                        if (msApi == null) {
                            /**
                             * 新接口：
                             * 1.orderNum,pid赋值
                             */
                            msApi = new MSApi();
                            int orderNum = 0;
                            MSApi msApiOrderNum = msApiRepository.findFirstByPidOrderByOrderNumDesc(pid);
                            if (msApiOrderNum != null && msApiOrderNum.getOrderNum() != null) {
                                orderNum = msApiOrderNum.getOrderNum() + 1;
                            }
                            /*接口默认值*/
                            if (!pid.equals(0L)) {
                                msApi.setSensitiveNum(0);
                                msApi.setStatus(MSApiStatusEnum.JOIN.getCode());
                            }
                            msApi.setOrderNum(orderNum);
                            msApi.setPid(pid);
                        } else {
                            /**
                             * 已存在接口：
                             * 1.判断是否未生效，重置为待接入
                             */
                            if (MSApiStatusEnum.DISABLED.getCode().equals(msApi.getStatus())) {
                                msApi.setStatus(MSApiStatusEnum.JOIN.getCode());
                            }
                        }
                        BeanUtils.copyProperties(msApiDto, msApi, BeanFindNullUtils.findNull(msApiDto));
                        msApi = msApiRepository.save(msApi);
                        if (pid.equals(0L)) {
                            saveApi(msApiDto.getChildren(), msApi.getId(), ids);
                        } else {
                            ids.add(msApi.getId());
                        }
                    });
        }
    }

    @Override
    public List<MSApiDto> findAllMSApis() {
        List<MSApiDto> msApiDtoList = new ArrayList<>();
        List<MSApi> msList = msApiRepository.findMSApisByPidAndDelFlagFalseOrderByOrderNumAsc(0L);
        if (EmptyUtils.isNotEmpty(msList)) {
            msList.stream()
                    .forEach(ms -> {
                        List<MSApi> apiList = msApiRepository.findMSApisByPidAndDelFlagFalseOrderByOrderNumAsc(ms.getId());
                        if (EmptyUtils.isNotEmpty(apiList)) {
                            MSApiDto msApiDto = new MSApiDto();
                            msApiDtoList.add(msApiDto);
                            BeanUtils.copyProperties(ms, msApiDto);
                            List<MSApiDto> children = new ArrayList<>();
                            msApiDto.setChildren(children);
                            apiList.forEach(api -> {
                                MSApiDto msApiDtoChildren = new MSApiDto();
                                children.add(msApiDtoChildren);
                                BeanUtils.copyProperties(api, msApiDtoChildren);
                                /*if (api.getOtherInfo() != null) {
                                    msApiDtoChildren.setOtherInfoView(tranOtherInfo(api.getOtherInfo()));
                                }*/
                            });
                        }
                    });
        }
        return msApiDtoList;
    }

    @Override
    public MSApiDto findMSApiById(Long id){
        MSApi msApi = msApiRepository.findById(id).orElse(null);
        MSApiDto msApiDto = new MSApiDto();
        if (msApi != null) {
            BeanUtils.copyProperties(msApi, msApiDto);
            if (msApi.getOtherInfo() != null) {
                msApiDto.setOtherInfoView(tranOtherInfo(msApi.getOtherInfo()));
            }
        }
        return msApiDto;
    }

    @Override
    public List<MSApiDto> findMSApisByStatusOn() {
        List<MSApiDto> msApiDtoList = new ArrayList<>();
        List<MSApi> msList = msApiRepository.findMSApisByPidAndDelFlagFalseOrderByOrderNumAsc(0L);
        if (EmptyUtils.isNotEmpty(msList)) {
            msList.stream()
                    .forEach(ms -> {
                        List<MSApi> apiList = msApiRepository.findMSApisByPidAndStatusAndDelFlagFalseOrderByOrderNumAsc(ms.getId(), MSApiStatusEnum.ON.getCode());
                        if (EmptyUtils.isNotEmpty(apiList)) {
                            MSApiDto msApiDto = new MSApiDto();
                            msApiDtoList.add(msApiDto);
                            BeanUtils.copyProperties(ms, msApiDto);
                            List<MSApiDto> children = new ArrayList<>();
                            msApiDto.setChildren(children);
                            apiList.forEach(api -> {
                                MSApiDto msApiDtoChildren = new MSApiDto();
                                children.add(msApiDtoChildren);
                                BeanUtils.copyProperties(api, msApiDtoChildren);
                                if (api.getOtherInfo() != null) {
                                    msApiDtoChildren.setOtherInfoView(tranOtherInfo(api.getOtherInfo()));
                                }
                            });
                        }
                    });
        }
        return msApiDtoList;
    }

    @Override
    public List<MSApiDto> findMSApisBySensitiveAndStatusOn(Integer sensitiveNum) {
        List<MSApiDto> msApiDtoList = new ArrayList<>();
        List<MSApi> msList = msApiRepository.findMSApisByPidAndDelFlagFalseOrderByOrderNumAsc(0L);
        if (EmptyUtils.isNotEmpty(msList)) {
            msList.stream()
                    .forEach(ms -> {
                        List<MSApi> apiList = msApiRepository.findMSApisByPidAndStatusAndSensitiveNumLessThanEqualAndDelFlagFalseOrderByOrderNumAsc(ms.getId(), MSApiStatusEnum.ON.getCode(), sensitiveNum);
                        if (EmptyUtils.isNotEmpty(apiList)) {
                            MSApiDto msApiDto = new MSApiDto();
                            msApiDtoList.add(msApiDto);
                            BeanUtils.copyProperties(ms, msApiDto);
                            List<MSApiDto> children = new ArrayList<>();
                            msApiDto.setChildren(children);
                            apiList.forEach(api -> {
                                MSApiDto msApiDtoChildren = new MSApiDto();
                                children.add(msApiDtoChildren);
                                BeanUtils.copyProperties(api, msApiDtoChildren);
                                if (api.getOtherInfo() != null) {
                                    msApiDtoChildren.setOtherInfoView(tranOtherInfo(api.getOtherInfo()));
                                }
                            });
                        }
                    });
        }
        return msApiDtoList;
    }

    @Override
    @Transactional
    public void on(List<Long> ids) {
        List<MSApi> msApis = msApiRepository.findMSApisByIdInAndPidNotAndDelFlagFalse(ids, 0L);
        if (msApis.size() != ids.size()) {
            throw new BusinessException();
        }
        msApis.forEach(msApi -> {
            if (!MSApiStatusEnum.OFF.getCode().equals(msApi.getStatus())) {
                throw new BusinessException();
            }
            msApi.setStatus(MSApiStatusEnum.ON.getCode());
        });
        /*更新状态为启用*/
        msApis = msApiRepository.saveAll(msApis);
        /*更新redis信息*/
        redisService.updateRedisApi(ConvertUtils.convertUrlSens(msApis), true);
    }

    @Override
    @Transactional
    public void off(List<Long> ids) {
        List<MSApi> msApis = msApiRepository.findMSApisByIdInAndPidNotAndDelFlagFalse(ids, 0L);
        if (msApis.size() != ids.size()) {
            throw new BusinessException();
        }
        msApis.forEach(msApi -> {
            if (!MSApiStatusEnum.ON.getCode().equals(msApi.getStatus())) {
                throw new BusinessException();
            }
            msApi.setStatus(MSApiStatusEnum.OFF.getCode());
        });
        /*更新状态为停用*/
        msApis = msApiRepository.saveAll(msApis);
        /*更新redis信息*/
        redisService.updateRedisApi(ConvertUtils.convertUrlSens(msApis), false);
    }

    @Override
    @Transactional
    public void join(List<Long> ids) {
        List<MSApi> msApis = msApiRepository.findMSApisByIdInAndPidNotAndDelFlagFalse(ids, 0L);
        if (msApis.size() != ids.size()) {
            throw new BusinessException();
        }
        msApis.forEach(msApi -> {
            if (!MSApiStatusEnum.JOIN.getCode().equals(msApi.getStatus())) {
                throw new BusinessException();
            }
            msApi.setStatus(MSApiStatusEnum.ON.getCode());
        });
        /*接入->启用*/
        msApis = msApiRepository.saveAll(msApis);
        /*更新redis信息*/
        redisService.updateRedisApi(ConvertUtils.convertUrlSens(msApis), true);
    }

    @Override
    @Transactional
    public void del(List<Long> ids) {
        List<MSApi> msApis = msApiRepository.findMSApisByIdInAndPidNotAndDelFlagFalse(ids, 0L);
        if (msApis.size() != ids.size()) {
            throw new BusinessException();
        }
        msApis.forEach(msApi -> {
            if (!MSApiStatusEnum.DISABLED.getCode().equals(msApi.getStatus())) {
                throw new BusinessException();
            }
            msApi.setDelFlag(true);
        });
        /*伪删除api*/
        msApis = msApiRepository.saveAll(msApis);
        /*删除用户接口关系*/
        /*状态为启用的需要移除redis用户接口关系信息*/
        List<MSUserApi> msUserApis = msApis.stream()
                .flatMap(msApi -> msApi.getMsUserApis().stream().filter(msUserApi -> MSUserApiStatusEnum.ON.getCode().equals(msUserApi.getStatus())))
                .collect(Collectors.toList());
        Map<String, Set<String>> userUrls = ConvertUtils.convertUserUrls(msUserApis);
        msUserApiRepository.deleteAll(msUserApis);
        /*更新申请状态为“已失效”*/
        List<MSApply> msApplies = msApplyRepository.findMSAppliesByMsApisInAndStatus(msApis, MSApplyStatusEnum.APPLY.getCode());
        msApplies.forEach(msApply -> {
            List<String> badUrls = new ArrayList<>();
            msApply.getMsApis()
                    .forEach(msApi -> {
                        if (msApi.getDelFlag()){
                            badUrls.add(msApi.getUrl() + "::" + msApi.getHttpMethod());
                        }
                    });
            msApply.setStatus(MSApplyStatusEnum.DISABLED.getCode());
            msApply.setReason(String.format("%s已被移除", badUrls));
        });
        msApplyRepository.saveAll(msApplies);
        redisService.updateRedisUserApi(userUrls, false);
    }

    @Override
    @Transactional
    public void sensitive(Integer sensitiveNum, List<Long> ids) {
        List<MSApi> redisMsApis = new ArrayList<>();
        List<MSApi> applyMsApis = new ArrayList<>();
        List<MSApi> msApis = msApiRepository.findMSApisByIdInAndPidNotAndDelFlagFalse(ids, 0L);
        msApis.stream()
                .filter(msApi -> !sensitiveNum.equals(msApi.getSensitiveNum()))
                .forEach(msApi -> {
                    if (MSApiStatusEnum.ON.getCode().equals(msApi.getStatus())) {
                        redisMsApis.add(msApi);
                    }
                    if (msApi.getSensitiveNum() < sensitiveNum) {
                        applyMsApis.add(msApi);
                    }
                    msApi.setSensitiveNum(sensitiveNum);
                });
        /*更新api 敏感级别信息*/
        msApiRepository.saveAll(msApis);
        List<MSApply> msApplies = msApplyRepository.findMSAppliesByMsApisInAndStatus(applyMsApis, MSApplyStatusEnum.APPLY.getCode());
        msApplies.forEach(msApply -> {
            Integer userSensitiveNum = msApply.getMsUser().getSensitiveNum();
            List<String> badUrls = new ArrayList<>();
            msApply.getMsApis()
                    .forEach(msApi -> {
                        if (userSensitiveNum < msApi.getSensitiveNum()){
                            badUrls.add(msApi.getUrl() + "::" + msApi.getHttpMethod());
                        }
                    });
            if (badUrls.size() > 0) {
                msApply.setStatus(MSApplyStatusEnum.DISABLED.getCode());
                msApply.setReason(String.format("[接口敏感级别调整]，%s敏感级别高于用户", badUrls));
            }
        });
        msApplyRepository.saveAll(msApplies);
        /*更新redis*/
        redisService.updateRedisApi(ConvertUtils.convertUrlSens(redisMsApis), true);
    }

    @Override
    public MSApiDto trans(MSApi msApi) {
        MSApiDto msApiDto = new MSApiDto();
        BeanUtils.copyProperties(msApi, msApiDto);
        if (msApi.getOtherInfo() != null) {
            msApiDto.setOtherInfoView(tranOtherInfo(msApi.getOtherInfo()));
        }
        return msApiDto;
    }

    @Override
    public List<MSApiDto> findMsApisByMSUserAndStatusOn(SsoUser ssoUser) {
        MSUser msUser = msUserRepository.findMSUserByUsername(ssoUser.getUsername());
        Integer sensitiveNum = ssoUser.getSensitiveLevel();
        Map<Long, Integer> idStatus = new HashMap<>();
        if (msUser != null) {
            List<MSApply> msApplies = msApplyRepository.findMSAppliesByMsUserAndStatus(msUser, MSApplyStatusEnum.APPLY.getCode());
            msApplies.forEach(msApply ->
                            msApply.getMsApis()
                                    .forEach(msApi -> idStatus.put(msApi.getId(), msApply.getStatus()))
                    );
            List<MSUserApi> msUserApis = msUserApiRepository.findMSUserApisByMsUserAndStatusNot(msUser, MSUserApiStatusEnum.EXPIRE.getCode());
            msUserApis.forEach(msUserApi -> idStatus.put(msUserApi.getMsApi().getId(), msUserApi.getStatus()));
        }
        List<MSApiDto> msApiDtoList = new ArrayList<>();
        List<MSApi> msList = msApiRepository.findMSApisByPidAndDelFlagFalseOrderByOrderNumAsc(0L);
        if (EmptyUtils.isNotEmpty(msList)) {
            msList.stream()
                    .forEach(ms -> {
                        List<MSApi> apiList = msApiRepository.findMSApisByPidAndStatusAndSensitiveNumLessThanEqualAndDelFlagFalseOrderByOrderNumAsc(ms.getId(), MSApiStatusEnum.ON.getCode(), sensitiveNum);
                        if (EmptyUtils.isNotEmpty(apiList)) {
                            MSApiDto msApiDto = new MSApiDto();
                            msApiDtoList.add(msApiDto);
                            BeanUtils.copyProperties(ms, msApiDto);
                            List<MSApiDto> children = new ArrayList<>();
                            msApiDto.setChildren(children);
                            apiList.forEach(api -> {
                                MSApiDto msApiDtoChildren = new MSApiDto();
                                children.add(msApiDtoChildren);
                                BeanUtils.copyProperties(api, msApiDtoChildren);
                                Integer userApiStatus = idStatus.get(api.getId());
                                if (userApiStatus != null) {
                                    msApiDtoChildren.setUserApiStatus(userApiStatus);
                                }
                                if (api.getOtherInfo() != null) {
                                    msApiDtoChildren.setOtherInfoView(tranOtherInfo(api.getOtherInfo()));
                                }
                            });
                        }
                    });
        }
        return msApiDtoList;
    }

    private void convert(Schema schema, Map<String, Definition> definitionMap) {
        String type = schema.getType();
        String ref = schema.getRef();
        if (type != null) {
            if (Type.ARRAY.equalsIgnoreCase(type)) {
                convert(schema.getItems(), definitionMap);
            }
        } else {
            if (ref != null) {
                schema.setType(Type.OBJECT);
                ref = getDefinition(ref);
                schema.setRef(ref);
                Definition definition = definitionMap.get(ref);
                schema.setLink(definition);
            }
        }
    }

    private OtherInfoView tranOtherInfo(String otherInfoJson) {
        OtherInfoView otherInfoView = new OtherInfoView();
        OtherInfo otherInfo = null;
        try {
            otherInfo = objectMapper.readValue(otherInfoJson, OtherInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        List<ApiParameterDetails> apiParameterDetailsList = otherInfo.getParameters();
        List<MSApiParaDetails> msApiParaDetailsList = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(apiParameterDetailsList)) {
            apiParameterDetailsList.stream()
                    .forEach(apiParameterDetails -> {
                        Object value = tranSchema(apiParameterDetails);
                        MSApiParaDetails msApiParaDetails = new MSApiParaDetails();
                        BeanUtils.copyProperties(apiParameterDetails, msApiParaDetails);
                        if (msApiParaDetails.getExample() == null) {
                            msApiParaDetails.setExample(value);
                        }
                        msApiParaDetailsList.add(msApiParaDetails);
                    });
        }
        BeanUtils.copyProperties(otherInfo, otherInfoView);
        otherInfoView.setParameters(msApiParaDetailsList);

        Schema result = otherInfo.getResult();
        Object value = null;
        if (result != null) {
            value = tranSchema(result);
        }
        otherInfoView.setResult(value);
        return otherInfoView;
    }

    private Object tranSchema(Schema schema) {
        String type = schema.getType();
        String format = schema.getFormat();
        Schema items = schema.getItems();
        Definition link = schema.getLink();
        if (type != null) {
            if (Type.INT.equalsIgnoreCase(type)) {
                /*数字类型*/
                return 0;
            } else if (Type.STRING.equalsIgnoreCase(type)) {
                if (Format.DATETIME.equalsIgnoreCase(format)) {
                    /*时间类型*/
                    return new Timestamp(System.currentTimeMillis());
                } else {
                    /*普通字符串*/
                    return "string";
                }
            } else if (Type.BOOLEAN.equalsIgnoreCase(type)) {
                /*布尔类型*/
                return true;
            } else if (Type.OBJECT.equalsIgnoreCase(type)) {
                /*处理link*/
                Map<String, Schema> schemas = link.getProperties();
                /*循环转换schemas*/
                Map<String, Object> objSchema = new HashMap<>();
                schemas.keySet()
                        .stream()
                        .forEach(propertyName -> {
                            objSchema.put(propertyName, tranSchema(schemas.get(propertyName)));
                        });
                return objSchema;
            } else if (Type.ARRAY.equalsIgnoreCase(type)) {
                /*递归转换items*/
                return Arrays.asList(tranSchema(items));
            }
        }
        return null;
    }

    private String getDefinition(String ref) {
        return ref.substring(ref.lastIndexOf("/") + 1);
    }

    private interface Type {
        String INT = "integer";
        String STRING = "string";
        String BOOLEAN = "boolean";
        String ARRAY = "array";
        String OBJECT = "object";
    }

    private interface Format {
        String DATETIME = "date-time";
        String INT32 = "int32";
        String INT64 = "int64";
    }
}
