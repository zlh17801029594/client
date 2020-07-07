package cn.adcc.client.service.impl;

import cn.adcc.client.DO.*;
import cn.adcc.client.DTO.*;
import cn.adcc.client.DTOSwagger.*;
import cn.adcc.client.enums.ApiStatusEnum;
import cn.adcc.client.enums.ApplyStatusEnum;
import cn.adcc.client.enums.UserApiStatusEnum;
import cn.adcc.client.exception.BusinessException;
import cn.adcc.client.repository.ApiRepository;
import cn.adcc.client.service.*;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.ConvertUtils;
import cn.adcc.client.utils.CopyUtil;
import cn.adcc.client.utils.EmptyUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApiServiceImpl implements ApiService {
    @Autowired
    private ApiRepository apiRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserApiService userApiService;
    @Autowired
    private SsoUserService ssoUserService;

    private Api save(ApiDto apiDto) {
        String url = apiDto.getUrl();
        String method = apiDto.getHttpMethod();
        Long pid = apiDto.getPid();
        Api api;
        if (apiDto.getType()) {
            /*接口*/
            api = apiRepository.findByTypeTrueAndPidAndUrlAndHttpMethod(pid, url, method);
            if (api == null) {
                api = new Api();
                api.setPid(pid);
                int orderNum = 0;
                Api numDesc = apiRepository.findFirstByTypeTrueAndPidOrderByOrderNumDesc(api.getPid());
                if (numDesc != null) {
                    orderNum = numDesc.getOrderNum() + 1;
                }
                api.setOrderNum(orderNum);
                api.setSensitiveNum(0);
                api.setStatus(ApiStatusEnum.JOIN.getCode());
                ApiDetails apiDetails = new ApiDetails();
                apiDetails.setApi(api);
                api.setApiDetails(apiDetails);
                log.info("[新增接口], {}", api);
            } else {
                if (ApiStatusEnum.DISABLED.getCode().equals(api.getStatus())) {
                    api.setStatus(ApiStatusEnum.JOIN.getCode());
                }
                log.info("[更新接口], {}", api);
            }
            BeanUtils.copyProperties(apiDto, api, "id", "pid", "orderNum", "sensitiveNum", "status");
            BeanUtils.copyProperties(apiDto.getApiDetailsDto(), api.getApiDetails(), "id");
        } else {
            /*微服务*/
            api = apiRepository.findByTypeFalseAndUrl(url);
            if (api == null) {
                api = new Api();
                api.setPid(0L);
                int orderNum = 0;
                Api numDesc = apiRepository.findFirstByTypeFalseOrderByOrderNumDesc();
                if (numDesc != null) {
                    orderNum = numDesc.getOrderNum() + 1;
                }
                api.setOrderNum(orderNum);
                log.info("[新增微服务], {}", api);
            } else {
                log.info("[更新微服务], {}", api);
            }
            BeanUtils.copyProperties(apiDto, api, "id", "pid", "orderNum", "sensitiveNum", "status");
        }
        apiRepository.save(api);
        return api;
    }

    @Override
    public ApiDto swagger2ApiDto(SwaggerApiDoc swaggerApiDoc) {
        ApiDto apiDto = new ApiDto();
        if (swaggerApiDoc != null) {
            /*微服务基础信息*/
            apiDto.setType(false);
            Info info = swaggerApiDoc.getInfo();
            if (info != null) {
                apiDto.setName(info.getTitle());
                apiDto.setDescription(info.getDescription());
            }
            apiDto.setHost(swaggerApiDoc.getHost());
            apiDto.setUrl(swaggerApiDoc.getBasePath());
            /*difinition对象信息*/
            Map<String, Definition> definitionMap = swaggerApiDoc.getDefinitions();
            if (EmptyUtils.isNotEmpty(definitionMap)) {
                definitionMap.keySet().forEach(definitionName -> {
                    Definition definition = definitionMap.get(definitionName);
                    if (definition != null) {
                        Map<String, Schema> properties = definition.getProperties();
                        if (EmptyUtils.isNotEmpty(properties)) {
                            properties.keySet()
                                    .forEach(propertyName -> {
                                        Schema propertyDetails = properties.get(propertyName);
                                        if (propertyDetails != null) {
                                            this.convert(propertyDetails, definitionMap);
                                        }
                                    });
                        }
                    }
                });
            }

            /*接口信息*/
            List<ApiDto> apiDtoList = new ArrayList<>();
            Map<String, Map<String, SwaApiDetails>> paths = swaggerApiDoc.getPaths();
            if (EmptyUtils.isNotEmpty(paths)) {
                paths.keySet()
                        .forEach(pathName -> {
                            Map<String, SwaApiDetails> swaApiDetailsMap = paths.get(pathName);
                            if (EmptyUtils.isNotEmpty(swaApiDetailsMap)) {
                                swaApiDetailsMap.keySet()
                                        .forEach(method -> {
                                            SwaApiDetails swaApiDetails = swaApiDetailsMap.get(method);
                                            if (swaApiDetails != null) {
                                                ApiDto apiDtoChild = new ApiDto();
                                                apiDtoChild.setType(true);
                                                apiDtoChild.setName(swaApiDetails.getSummary());
                                                apiDtoChild.setDescription(swaApiDetails.getDescription());
                                                String apiUrl = "";
                                                String basePath = apiDto.getUrl();
                                                /*这里将微服务url拼接到接口url。接口表直接存储完整url*/
                                                if (basePath.endsWith("/")) {
                                                    apiUrl = basePath.substring(0, basePath.length() - 1) + pathName;
                                                } else {
                                                    apiUrl = basePath + pathName;
                                                }
                                                apiDtoChild.setUrl(apiUrl);
                                                apiDtoChild.setHttpMethod(method);
                                                /*表分片，分开存储*/
                                                ApiDetailsDto apiDetailsDto = new ApiDetailsDto();
                                                apiDtoChild.setApiDetailsDto(apiDetailsDto);
                                                apiDetailsDto.setDeprecated(swaApiDetails.getDeprecated());

                                                List<ApiParameterDetails> apiParameterDetailsList = swaApiDetails.getParameters();
                                                if (EmptyUtils.isNotEmpty(apiParameterDetailsList)) {
                                                    apiParameterDetailsList.stream()
                                                            .filter(Objects::nonNull)
                                                            .forEach(apiParameterDetails -> {
                                                                Schema schema = apiParameterDetails.getSchema();
                                                                if (schema != null) {
                                                                    this.convert(schema, definitionMap);
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
                                                otherInfo.setConsumes(swaApiDetails.getConsumes());
                                                otherInfo.setProduces(swaApiDetails.getProduces());
                                                otherInfo.setParameters(swaApiDetails.getParameters());
                                                List<ResponseDesc> responseDescList = new ArrayList<>();
                                                otherInfo.setResponses(responseDescList);

                                                Map<String, ResponseDetails> responseDetailsMap = swaApiDetails.getResponses();
                                                if (EmptyUtils.isNotEmpty(responseDetailsMap)) {
                                                    responseDetailsMap.keySet()
                                                            .forEach(code -> {
                                                                ResponseDetails responseDetails = responseDetailsMap.get(code);
                                                                if (responseDetails != null) {
                                                                    ResponseDesc responseDesc = new ResponseDesc();
                                                                    responseDesc.setCode(code);
                                                                    responseDesc.setDescription(responseDetails.getDescription());
                                                                    responseDescList.add(responseDesc);
                                                                    Schema schema = responseDetails.getSchema();
                                                                    if (schema != null) {
                                                                        this.convert(schema, definitionMap);
                                                                        otherInfo.setResult(schema);
                                                                    }
                                                                }
                                                            });
                                                }
                                                String otherInfoJson = null;
                                                try {
                                                    otherInfoJson = objectMapper.writeValueAsString(otherInfo);
                                                } catch (JsonProcessingException e) {
                                                    //json转化异常（不会发生）
                                                    e.printStackTrace();
                                                }
                                                apiDetailsDto.setOtherInfo(otherInfoJson);
                                                apiDtoList.add(apiDtoChild);
                                            }
                                        });
                            }
                        });
            }
            apiDto.setApiDtos(apiDtoList);
        }
        return apiDto;
    }

    @Override
    @Transactional
    public void updateAll(List<ApiDto> apiDtos) {
        Set<Long> ids = new HashSet<>();
        if (EmptyUtils.isNotEmpty(apiDtos)) {
            apiDtos.forEach(apiDto -> {
                if (EmptyUtils.isNotEmpty(apiDto.getApiDtos())) {
                    Api api = save(apiDto);
                    apiDto.getApiDtos().forEach(apiDtoChild -> {
                        apiDtoChild.setPid(api.getId());
                        Api apiChild = save(apiDtoChild);
                        ids.add(apiChild.getId());
                    });
                }
            });
        }
        List<Api> apis;
        if (!ids.isEmpty()) {
            apis = apiRepository.findByTypeTrueAndStatusNotAndIdNotIn(ApiStatusEnum.DISABLED.getCode(), ids);
        } else {
            apis = apiRepository.findByTypeTrueAndStatusNot(ApiStatusEnum.DISABLED.getCode());
        }
        if (EmptyUtils.isNotEmpty(apis)) {
            log.info("[更新接口状态] [=>未生效], {}", apis);
            List<Api> apisRedis = new ArrayList<>();
            apis.forEach(api -> {
                if (ApiStatusEnum.ON.getCode().equals(api.getStatus())) {
                    apisRedis.add(api);
                }
                api.setStatus(ApiStatusEnum.DISABLED.getCode());
                apiRepository.save(api);
            });
            redisService.updateRedisApi(ConvertUtils.apis2UrlSens(apisRedis), false);
        }
    }

    @Override
    public List<ApiDto> findAll() {
        List<ApiDto> apiDtos = new ArrayList<>();
        List<Api> apis = apiRepository.findByTypeFalseOrderByOrderNum();
        if (EmptyUtils.isNotEmpty(apis)) {
            apis.forEach(api -> {
                List<Api> apiChilds = apiRepository.findByTypeTrueAndPidOrderByOrderNum(api.getId());
                if (EmptyUtils.isNotEmpty(apiChilds)) {
                    ApiDto apiDto = new ApiDto();
                    apiDtos.add(apiDto);
                    BeanUtils.copyProperties(api, apiDto);
                    List<ApiDto> apiDtoChilds = new ArrayList<>();
                    apiDto.setApiDtos(apiDtoChilds);
                    apiChilds.forEach(apiChild -> {
                        ApiDto apiDtoChild = new ApiDto();
                        apiDtoChilds.add(apiDtoChild);
                        BeanUtils.copyProperties(apiChild, apiDtoChild);
                    });
                }
            });
        }
        return apiDtos;
    }

    @Override
    public List<ApiDto> findByStatusOn() {
        List<ApiDto> apiDtos = new ArrayList<>();
        List<Api> apis = apiRepository.findByTypeFalseOrderByOrderNum();
        if (EmptyUtils.isNotEmpty(apis)) {
            apis.forEach(api -> {
                List<Api> apiChilds = apiRepository.findByTypeTrueAndPidAndStatusOrderByOrderNum(api.getId(), ApiStatusEnum.ON.getCode());
                if (EmptyUtils.isNotEmpty(apiChilds)) {
                    ApiDto apiDto = new ApiDto();
                    apiDtos.add(apiDto);
                    BeanUtils.copyProperties(api, apiDto);
                    List<ApiDto> apiDtoChilds = new ArrayList<>();
                    apiDto.setApiDtos(apiDtoChilds);
                    apiChilds.forEach(apiChild -> {
                        ApiDto apiDtoChild = new ApiDto();
                        apiDtoChilds.add(apiDtoChild);
                        BeanUtils.copyProperties(apiChild, apiDtoChild);
                    });
                }
            });
        }
        return apiDtos;
    }

    @Override
    public List<ApiDto> findByStatusOnAndSensitive(Integer sensitiveNum) {
        List<ApiDto> apiDtos = new ArrayList<>();
        List<Api> apis = apiRepository.findByTypeFalseOrderByOrderNum();
        if (EmptyUtils.isNotEmpty(apis)) {
            apis.forEach(api -> {
                List<Api> apiChilds = apiRepository.findByTypeTrueAndPidAndStatusAndSensitiveNumLessThanEqualOrderByOrderNum(api.getId(), ApiStatusEnum.ON.getCode(), sensitiveNum);
                if (EmptyUtils.isNotEmpty(apiChilds)) {
                    ApiDto apiDto = new ApiDto();
                    apiDtos.add(apiDto);
                    BeanUtils.copyProperties(api, apiDto);
                    List<ApiDto> apiDtoChilds = new ArrayList<>();
                    apiDto.setApiDtos(apiDtoChilds);
                    apiChilds.forEach(apiChild -> {
                        ApiDto apiDtoChild = new ApiDto();
                        apiDtoChilds.add(apiDtoChild);
                        BeanUtils.copyProperties(apiChild, apiDtoChild);
                    });
                }
            });
        }
        return apiDtos;
    }

    public List<ApiDto> findByUserDto(UserDto userDto) {
        User user = userService.findByUsername(userDto.getUsername());
        Integer sensitiveNum = userDto.getSensitiveNum();
        List<ApiDto> apiDtos = this.findByStatusOnAndSensitive(sensitiveNum);
        if(user != null) {
            Long userId = user.getId();
            Map<Long, Integer> idStatus = new HashMap<>();
            /*接口：待审批申请*/
            List<ApplyDto> applyDtos = applyService.findByUserIdAndStatusApply(userId);
            applyDtos.forEach(applyDto -> applyDto.getApplyDetailsDtos().forEach(applyDetailsDto -> idStatus.put(applyDetailsDto.getApiId(), ApplyStatusEnum.APPLY.getCode())));
            /*接口：未过期用户接口*/
            List<UserApiDto> userApiDtos = userApiService.findByUserIdAndStatusNotExpire(userId);
            userApiDtos.forEach(userApiDto -> idStatus.put(userApiDto.getApiId(), userApiDto.getStatus()));
            /*设置用户接口状态*/
            apiDtos.forEach(apiDto -> {
                List<ApiDto> apiDtoChilds = apiDto.getApiDtos();
                apiDtoChilds.forEach(apiDtoChild -> {
                    Integer userApiStatus = idStatus.get(apiDtoChild.getId());
                    if (userApiStatus != null) {
                        apiDtoChild.setUserApiStatus(userApiStatus);
                    }
                });
            });
        }
        return apiDtos;
    }

    @Override
    public List<ApiDto> findByTypeTrueAndStatusOnAndSensitive(Integer sensitiveNum) {
        List<Api> apis = apiRepository.findByTypeTrueAndStatusAndSensitiveNumLessThanEqual(ApiStatusEnum.ON.getCode(), sensitiveNum);
        return CopyUtil.copyList(apis, ApiDto.class);
    }

    @Override
    @Transactional
    public void updateSens(Long id, Integer sens) {
        log.info("[更新接口敏感级别] [{}], {}",sens, id);

    }

    @Override
    @Transactional
    public void updateSensBatch(List<Long> ids, Integer sens) {
        log.info("[更新接口敏感级别] [{}], {}", sens, ids);
        List<Api> apis = ids.stream().map(id -> this.validate(id, null)).collect(Collectors.toList());
        List<Api> redisApis = new ArrayList<>();
        List<Api> applyApis = new ArrayList<>();
        apis.stream()
                .filter(api -> !sens.equals(api.getSensitiveNum()))
                .forEach(api -> {
                    if (ApiStatusEnum.ON.getCode().equals(api.getStatus())) {
                        redisApis.add(api);
                    }
                    if (api.getSensitiveNum() < sens) {
                        applyApis.add(api);
                    }
                    api.setSensitiveNum(sens);
                });
        /*更新api 敏感级别信息*/
        apiRepository.saveAll(apis);
        /*更新申请状态*/
        Set<ApplyDetails> applyDetailss = applyApis.stream().flatMap(api -> api.getApplyDetailss().stream()).collect(Collectors.toSet());
        applyService.updateApplyByApiSens(applyDetailss, sens);
        /*更新redis*/
        redisService.updateRedisApi(ConvertUtils.apis2UrlSens(redisApis), true);
    }

    @Override
    @Transactional
    public void updateStatusOn(Long id) {
        log.info("[启用接口], {}", id);
    }

    @Override
    @Transactional
    public void updateStatusOnBatch(List<Long> ids) {
        log.info("[更新接口状态] [已停用=>已启用], {}", ids);
        List<Api> apis = ids.stream().map(id -> this.validate(id, ApiStatusEnum.OFF.getCode())).collect(Collectors.toList());
        apis.forEach(api -> {
            api.setStatus(ApiStatusEnum.ON.getCode());
        });
        /*更新状态为启用*/
        apiRepository.saveAll(apis);
        /*更新redis信息*/
        redisService.updateRedisApi(ConvertUtils.apis2UrlSens(apis), true);
    }

    @Override
    @Transactional
    public void updateStatusOff(Long id) {
        log.info("[停用接口], {}", id);
    }

    @Override
    @Transactional
    public void updateStatusOffBatch(List<Long> ids) {
        log.info("[更新接口状态] [已启用=>已停用], {}", ids);
        List<Api> apis = ids.stream().map(id -> this.validate(id, ApiStatusEnum.ON.getCode())).collect(Collectors.toList());
        apis.forEach(api -> {
            api.setStatus(ApiStatusEnum.OFF.getCode());
        });
        /*更新状态为停用*/
        apiRepository.saveAll(apis);
        /*更新redis信息*/
        redisService.updateRedisApi(ConvertUtils.apis2UrlSens(apis), false);
    }

    @Override
    @Transactional
    public void updateStatusJoin(Long id) {
        log.info("[接入接口], {}", id);
    }

    @Override
    @Transactional
    public void updateStatusJoinBatch(List<Long> ids) {
        log.info("[更新接口状态] [待接入=>已启用], {}", ids);
        List<Api> apis = ids.stream().map(id -> this.validate(id, ApiStatusEnum.JOIN.getCode())).collect(Collectors.toList());
        apis.forEach(api -> {
            api.setStatus(ApiStatusEnum.ON.getCode());
        });
        /*更新状态为启用*/
        apiRepository.saveAll(apis);
        /*更新redis信息*/
        redisService.updateRedisApi(ConvertUtils.apis2UrlSens(apis), true);
    }

    private Api validate(Long id, Integer status) {
        Optional<Api> apiOptional = apiRepository.findById(id);
        if (apiOptional.isPresent()) {
            Api api = apiOptional.get();
            /*状态是否一致*/
            if (status != null && !status.equals(api.getStatus())) {
                log.error("[数据不一致] [接口状态应为: {}], {}", status, api);
                throw new BusinessException();
            }
            /*是否接口*/
            if (!api.getType()) {
                log.error("[数据不一致] [不是接口数据], {}", api);
                throw new BusinessException();
            }
            return api;
        }
        /*数据是否存在*/
        log.error("[数据不一致] [对应数据不存在], {}", id);
        throw new BusinessException();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("[删除接口], {}", id);
        /*1.获取检验成功api*/
        Api api = this.validate(id, ApiStatusEnum.DISABLED.getCode());
        /*2.用户接口关系受影响数据*/
        List<UserApi> userApis = api.getUserApis().stream().filter(userApi -> UserApiStatusEnum.ON.getCode().equals(userApi.getStatus()))
                .collect(Collectors.toList());
        Map<String, Set<String>> userUrls = ConvertUtils.userApis2UserUrls(userApis);
        /*3.申请受影响数据*/
        Set<ApplyDetails> applyDetailss = api.getApplyDetailss();
        //更新“待审批”申请=>“已失效”
        applyService.updateApplyByApiDel(applyDetailss);
        Long pid = api.getPid();
        apiRepository.delete(api);
        int i = apiRepository.countByTypeTrueAndPid(pid);
        if (i == 0) {
            log.info("[删除微服务], {}", pid);
            apiRepository.deleteById(pid);
        }
        //状态为启用的需要移除redis用户接口关系信息
        redisService.updateRedisUserApi(userUrls, false);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        log.info("[删除接口], {}", ids);
        /*1.获取检验成功api*/
        List<Api> apis = ids.stream().map(id -> this.validate(id, ApiStatusEnum.DISABLED.getCode()))
                .collect(Collectors.toList());
        /*2.用户接口关系受影响数据*/
        List<UserApi> userApis = apis.stream()
                .flatMap(api -> api.getUserApis().stream().filter(userApi -> UserApiStatusEnum.ON.getCode().equals(userApi.getStatus())))
                .collect(Collectors.toList());
        Map<String, Set<String>> userUrls = ConvertUtils.userApis2UserUrls(userApis);
        /*3.申请受影响数据*/
        Set<ApplyDetails> applyDetailss = apis.stream()
                .flatMap(api -> api.getApplyDetailss().stream())
                .collect(Collectors.toSet());
        //更新“待审批”申请=>“已失效”
        applyService.updateApplyByApiDel(applyDetailss);
        /*4.判断微服务是否删除了所有接口，删除空微服务*/
        Set<Long> pidSet = apis.stream().map(Api::getPid)
                .collect(Collectors.toSet());
        /*5.删除接口*/
        apiRepository.deleteAll(apis);
        pidSet.forEach(pid -> {
            int i = apiRepository.countByTypeTrueAndPid(pid);
            if (i == 0) {
                log.info("[删除微服务], {}", pid);
                apiRepository.deleteById(pid);
            }
        });
        //状态为启用的需要移除redis用户接口关系信息
        redisService.updateRedisUserApi(userUrls, false);
    }

    @Override
    public ApiDto findById(Long id) {
        Optional<Api> apiOptional = apiRepository.findById(id);
        return apiOptional.map(api -> {
            ApiDto apiDto = CopyUtil.copy(api, ApiDto.class);
            ApiDetailsDto apiDetailsDto = CopyUtil.copy(api.getApiDetails(), ApiDetailsDto.class);
            apiDto.setApiDetailsDto(apiDetailsDto);
            if (apiDetailsDto.getOtherInfo() != null) {
                apiDetailsDto.setOtherInfoView(tranOtherInfo(apiDetailsDto.getOtherInfo()));
            }
            return apiDto;
        }).orElse(null);
    }

    @Override
    public ApiDto findApiById(Long id) {
        Optional<Api> apiOptional = apiRepository.findById(id);
        return apiOptional.map(api -> CopyUtil.copy(api, ApiDto.class)).orElse(null);
    }

    @Override
    public List<ApiDto> findApisByType() {
        SsoUser ssoUser = ssoUserService.getSsoUser();
        int sensitiveLevel = ssoUser.getSensitiveLevel();
        List<Api> apis = apiRepository.findByTypeTrueAndStatusAndSensitiveNumLessThanEqual(ApiStatusEnum.ON.getCode(), sensitiveLevel);
        return CopyUtil.copyList(apis, ApiDto.class);
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
                // 弃用link
                /*schema.setType(Type.OBJECT);
                ref = getDefinition(ref);
                schema.setRef(ref);
                Definition definition = definitionMap.get(ref);
                schema.setLink(definition);*/
                ref = getDefinition(ref);
                Definition definition = definitionMap.get(ref);
                BeanUtils.copyProperties(definition, schema);
            }
        }
    }

    public OtherInfoView tranOtherInfo(String otherInfoJson) {
        OtherInfoView otherInfoView = new OtherInfoView();
        OtherInfo otherInfo = null;
        try {
            otherInfo = objectMapper.readValue(otherInfoJson, OtherInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        List<ApiParameterDetails> apiParameterDetailsList = otherInfo.getParameters();
        List<ApiParaDetails> apiParaDetailsList = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(apiParameterDetailsList)) {
            apiParameterDetailsList
                    .forEach(apiParameterDetails -> {
                        Object value = tranSchema(apiParameterDetails);
                        ApiParaDetails apiParaDetails = new ApiParaDetails();
                        BeanUtils.copyProperties(apiParameterDetails, apiParaDetails);
                        if (apiParaDetails.getExample() == null) {
                            apiParaDetails.setExample(value);
                        }
                        apiParaDetailsList.add(apiParaDetails);
                    });
        }
        BeanUtils.copyProperties(otherInfo, otherInfoView);
        otherInfoView.setParameters(apiParaDetailsList);

        Schema result = otherInfo.getResult();
        // result原json
        otherInfoView.setResult1(result);
        // result转化example样例json
        Object value = null;
        if (result != null) {
            value = tranSchema(result);
        }
        otherInfoView.setResult(value);
        // result转化description描述map
        Map<String, String> descMap = new HashMap<>();
        if (result != null) {
            tranSchema2DescMap(null, result, descMap);
        }
        otherInfoView.setResultDesc(descMap);
        return otherInfoView;
    }

    private void tranSchema2DescMap(String propertyName, Schema schema, Map<String, String> descMap) {
        String type = schema.getType();
        Schema items = schema.getItems();
        // 描述信息
        String description = schema.getDescription();
        if (type != null) {
            if (Type.INT.equalsIgnoreCase(type) ||
                    Type.STRING.equalsIgnoreCase(type) ||
                    Type.BOOLEAN.equalsIgnoreCase(type)) {
                if (propertyName != null) {
                    descMap.put(propertyName, description);
                }
            } else if (Type.OBJECT.equalsIgnoreCase(type)) {
                /*循环转换schemas*/
                Map<String, Schema> schemas = schema.getProperties();
                if (schemas != null) {
                    schemas.keySet()
                            .forEach(propertyNameTemp -> {
                                tranSchema2DescMap(propertyNameTemp, schemas.get(propertyNameTemp), descMap);
                            });
                }
            } else if (Type.ARRAY.equalsIgnoreCase(type)) {
                /*递归转换items*/
                tranSchema2DescMap(null, items, descMap);
            }
        }
    }

    private Object tranSchema(Schema schema) {
        String type = schema.getType();
        String format = schema.getFormat();
        Schema items = schema.getItems();
        // 兼容非基础类型样例
        Object example = schema.getExample();
        // 弃用link
        Definition link = schema.getLink();
        if (type != null) {
            if (Type.INT.equalsIgnoreCase(type)) {
                /*数字类型*/
                if (example != null) {
                    return example;
                } else {
                    return 0;
                }
            } else if (Type.STRING.equalsIgnoreCase(type)) {
                if (example != null) {
                    return example;
                } else {
                    if (Format.DATETIME.equalsIgnoreCase(format)) {
                        /*时间类型*/
                        return new Timestamp(System.currentTimeMillis());
                    } else {
                        /*普通字符串*/
                        return "string";
                    }
                }
            } else if (Type.BOOLEAN.equalsIgnoreCase(type)) {
                /*布尔类型*/
                if (example != null) {
                    return example;
                } else {
                    return true;
                }
            } else if (Type.OBJECT.equalsIgnoreCase(type)) {
                /*循环转换schemas*/
                Map<String, Object> objSchema = new HashMap<>();
                Map<String, Schema> schemas = schema.getProperties();
                if (schemas != null) {
                    schemas.keySet()
                            .forEach(propertyName -> {
                                objSchema.put(propertyName, tranSchema(schemas.get(propertyName)));
                            });
                } else {
                    // 兼容Map集合加example情况
                    if (example != null) {
                        return example;
                    }
                }
                return objSchema;
            } else if (Type.ARRAY.equalsIgnoreCase(type)) {
                /*递归转换items*/
                Object item = tranSchema(items);
                // 兼容ist集合加example情况
                if (item instanceof Map && ((Map) item).isEmpty() && example != null) {
                    return example;
                }
                return Collections.singletonList(item);
            }
        }
        return null;
    }

    private Object tranSchema_bak(Schema schema) {
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
                /*循环转换schemas*/
                Map<String, Object> objSchema = new HashMap<>();
                if (link != null) {
                    /*处理link*/
                    Map<String, Schema> schemas = link.getProperties();
                    schemas.keySet()
                            .forEach(propertyName -> {
                                objSchema.put(propertyName, tranSchema(schemas.get(propertyName)));
                            });
                }
                return objSchema;
            } else if (Type.ARRAY.equalsIgnoreCase(type)) {
                /*递归转换items*/
                return Collections.singletonList(tranSchema(items));
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
