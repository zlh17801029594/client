package cn.adcc.client.service.impl;

import cn.adcc.client.DO.MSApi;
import cn.adcc.client.DTO.*;
import cn.adcc.client.DTOImport.*;
import cn.adcc.client.enums.MSApiStatusEnum;
import cn.adcc.client.repository.MSApiRepository;
import cn.adcc.client.service.MSApiService;
import cn.adcc.client.service.SwaggerApiDocService;
import cn.adcc.client.service.UserService;
import cn.adcc.client.utils.BeanFindNullUtils;
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

@Service
@Slf4j
public class MSApiServiceImpl implements MSApiService {
    @Autowired
    private UserService userService;

    @Autowired
    private SwaggerApiDocService swaggerApiDocService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MSApiRepository msApiRepository;

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
    @Transactional
    public List<MSApiDto> updateMSApi(List<MSApiDto> msApiDtoList) {
        List<Long> ids = new ArrayList<>();
        saveApi(msApiDtoList, 0L, ids);
        disableApi(ids);
        return msApiDtoList;
    }

    @Override
    public List<MSApiDto> findMSApi() {
        /*获取用户敏感级别*/
        Integer sensitiveNum = 0;
        List<MSApiDto> msApiDtoList = new ArrayList<>();
        List<MSApi> msList = msApiRepository.findMSApisByPidOrderByOrderNumDesc(0L);
        if (EmptyUtils.isNotEmpty(msList)) {
            msList.stream()
                    .forEach(ms -> {
                        List<MSApi> apiList = null;
                        if (userService.getUser().getPermission().contains("SUPER_ADMIN"))
                            apiList = msApiRepository.findMSApisByPidOrderByOrderNumDesc(ms.getId());
                        /*普通用户*/
                        else
                            apiList = msApiRepository.findMSApisByPidAndStatusAndSensitiveNumLessThanEqualOrderByOrderNumDesc(ms.getId(), MSApiStatusEnum.JOIN.getCode(), sensitiveNum);
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
    public MSApiDto update(MSApiDto msApiDto) {
        return null;
    }

    /**
     * 将未出现接口置为失效
     * @param ids
     */
    private void disableApi(List<Long> ids) {
        List<MSApi> msApiList = msApiRepository.findMSApisByIdNotInAndPidNot(ids, 0L);
        if (EmptyUtils.isNotEmpty(msApiList)) {
            msApiList.stream()
                    .forEach(msApi -> {
                        msApi.setStatus(MSApiStatusEnum.DISABLED.getCode());
                        msApiRepository.save(msApi);
                    });
        }
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
                        MSApi msApi = null;
                        if (pid.equals(0L)) {
                            msApi = msApiRepository.findMSApiByUrl(url);
                        } else {
                            msApi = msApiRepository.findMSApiByUrlAndHttpMethod(url, method);
                        }
                        if (msApi == null) {
                            /**
                             * 新接口：
                             * 1.orderNum,pid赋值
                             */
                            msApi = new MSApi();
                            Long orderNum = 0L;
                            MSApi msApiOrderNum = msApiRepository.findFirstByPidOrderByOrderNumDesc(pid);
                            if (msApiOrderNum != null && msApiOrderNum.getOrderNum() != null) {
                                orderNum = msApiOrderNum.getOrderNum() + 1;
                            }
                            /*接口默认值*/
                            if (msApiDto.getChildren() == null) {
                                msApi.setSensitiveNum(0);
                                msApi.setStatus(0);
                            }
                            msApi.setOrderNum(orderNum);
                            msApi.setPid(pid);
                        } else {
                            /**
                             * 已存在接口：
                             * 1.判断是否失效，重置为待接入
                             */
                            if (MSApiStatusEnum.DISABLED.getCode().equals(msApi.getStatus())) {
                                msApi.setStatus(MSApiStatusEnum.JOIN.getCode());
                            }
                        }
                        BeanUtils.copyProperties(msApiDto, msApi, BeanFindNullUtils.findNull(msApiDto));
                        msApi = msApiRepository.save(msApi);
                        BeanUtils.copyProperties(msApi, msApiDto, BeanFindNullUtils.findNull(msApi));
                        if (msApiDto.getChildren() != null) {
                            saveApi(msApiDto.getChildren(), msApiDto.getId(), ids);
                        } else {
                            ids.add(msApi.getId());
                        }
                    });
        }
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
