package cn.adcc.client.service.impl;

import cn.adcc.client.DO.FixmLogic;
import cn.adcc.client.DO.FixmOrder;
import cn.adcc.client.DTO.FixmLogicDto;
import cn.adcc.client.DTO.FixmOrderDto;
import cn.adcc.client.DTO.FixmProp;
import cn.adcc.client.exception.BusinessException;
import cn.adcc.client.exception.ValidatorFixmException;
import cn.adcc.client.repository.FixmLogicRepository;
import cn.adcc.client.service.FixmLogicService;
import cn.adcc.client.service.FixmOrderService;
import cn.adcc.client.utils.BeanFindNullUtils;
import cn.adcc.client.utils.FixmUtils;
import cn.adcc.fixm.convertinterface.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FixmLogicServiceImpl implements FixmLogicService {

    @Autowired
    private FixmLogicRepository fixmLogicRepository;

    @Autowired
    private FixmOrderService fixmOrderService;

    private Validator validator = new Validator();

    private static final String INTEGRATE_DB = "INTEGRATE";

    private static final String SPLIT_SIGN = "->";

    private Map<String, FixmOrder> fixmOrderMap;

    private List<String> routes;

    private List<FixmOrder> fixmOrders;

    {
        routes = Arrays.asList(
                "fx:Flight->fx:aircraft->fx:aircraftAddress",
                "fx:Flight->fx:aircraft->fx:aircraftApproachCategory",
                "fx:Flight->fx:aircraft->fx:aircraftType->fx:numberOfAircraft",
                "fx:Flight->fx:aircraft->fx:aircraftType->fx:type->fx:icaoAircraftTypeDesignator",
                "fx:Flight->fx:aircraft->fx:aircraftType->fx:type->fx:otherAircraftType",
                "fx:Flight->fx:aircraft->fx:capabilities->fx:communication->fx:communicationCapabilityCode",
                "fx:Flight->fx:aircraft->fx:capabilities->fx:communication->fx:datalinkCommunicationCapabilityCode",
                "fx:Flight->fx:aircraft->fx:capabilities->fx:communication->fx:otherCommunicationCapabilities",
                "fx:Flight->fx:aircraft->fx:capabilities->fx:communication->fx:otherDatalinkCapabilities",
                "fx:Flight->fx:aircraft->fx:capabilities->fx:communication->fx:selectiveCallingCode",
                "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:cruiseClimbStart->fx:level->fb:flightLevelOrAltitudeRange->fb:upperBound->fb:altitude",
                "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:cruiseClimbStart->fx:level->fb:flightLevelOrAltitudeRange->fb:upperBound->fb:flightLevel",
                "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:cruiseClimbStart->fx:level->fb:flightLevelOrAltitudeValue->fb:altitude",
                "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:cruiseClimbStart->fx:level->fb:flightLevelOrAltitudeValue->fb:flightLevel",
                "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:cruiseClimbStart->fx:speed",
                "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:level->fx:activation",
                "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:level->fx:level->fb:altitude",
                "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:level->fx:level->fb:flightLevel",
                "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:speed->fx:activation",
                "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:speed->fx:speed"
        );

        FixmOrder fixmOrder1 = new FixmOrder();
        fixmOrder1.setXsdnode("fx:Flight->fx:aircraft");
        fixmOrder1.setNodeOrder("fx:aircraftType->fx:capabilities->fx:aircraftAddress->fx:aircraftApproachCategory->fx:coloursAndMarkings->fx:extension->fx:formationCount->fx:registration->fx:wakeTurbulence");
        FixmOrder fixmOrder2 = new FixmOrder();
        fixmOrder2.setXsdnode("fx:Flight->fx:aircraft->fx:aircraftType");
        fixmOrder2.setNodeOrder("fx:extension->fx:numberOfAircraft->fx:type");
        FixmOrder fixmOrder3 = new FixmOrder();
        fixmOrder3.setXsdnode("fx:Flight->fx:aircraft->fx:capabilities");
        fixmOrder3.setNodeOrder("fx:communication->fx:extension->fx:navigation->fx:standardCapabilities->fx:surveillance->fx:survival");
        fixmOrders = Arrays.asList(
                fixmOrder1,
                fixmOrder2,
                fixmOrder3);
    }

    private void orderTree(List<FixmLogicDto> fixmLogicDtos, String version) {
        fixmOrderMap = fixmOrderService.list(version);
        this.orderFixmLogicDtos(null, fixmLogicDtos);
    }

    private void orderFixmLogicDtos(String parentNode, List<FixmLogicDto> fixmLogicDtos) {
        FixmOrder fixmOrder = fixmOrderMap.get(parentNode);
        String nodeOrderStr = null;
        String propertyOrderStr = null;
        if (fixmOrder != null) {
            nodeOrderStr = fixmOrder.getNodeOrder();
            propertyOrderStr = fixmOrder.getPropertyOrder();
        }
        List<String> nodeOrder = nodeOrderStr != null ? Arrays.asList(nodeOrderStr.split("->")) : Collections.emptyList();
        List<String> propertyOrder = propertyOrderStr != null ? Arrays.asList(propertyOrderStr.split("->")) : Collections.emptyList();
        this.swap(fixmLogicDtos, nodeOrder, propertyOrder);
        fixmLogicDtos.forEach(fixmLogicDto -> {
            if (fixmLogicDto.getChildren() != null) {
                String childNode = parentNode != null ? parentNode.concat("->").concat(fixmLogicDto.getName()) : fixmLogicDto.getName();
                this.orderFixmLogicDtos(childNode, fixmLogicDto.getChildren());
            }
        });
    }

    private void swap(List<FixmLogicDto> fixmLogicDtos, List<String> nodeOrder, List<String> propertyOrder) {
        Map<String, Integer> labelIndexMap = fixmLogicDtos.stream().collect(Collectors.toMap(FixmLogicDto::getName, fixmLogicDtos::indexOf));
//        int nodeSize = fixmLogicDtos.stream().filter(FixmLogicDto::getIsnode).collect(Collectors.toSet()).size();
        int index = 0;
        for (String nodeStr : nodeOrder) {
            Integer i = labelIndexMap.get(nodeStr);
            if (i == null) {
                continue;
            }
            // 加上一层判断：避免nodeOrder中数据有误(包含property),将property误排在前。
            FixmLogicDto fixmLogicDto = fixmLogicDtos.get(i);
            // 目录一定是节点
            if (fixmLogicDto.getIsnode() == null || fixmLogicDto.getIsnode()) {
                if (i == index) {
                    index++;
                    continue;
                }
                String labelTemp = fixmLogicDtos.get(index).getName();
                Collections.swap(fixmLogicDtos, i, index);
                labelIndexMap.put(nodeStr, index);
                labelIndexMap.put(labelTemp, i);
                index++;
            }
        }
        for (int i = index; i < fixmLogicDtos.size(); i++) {
            FixmLogicDto fixmLogicDto = fixmLogicDtos.get(i);
            // 目录一定是节点
            if (fixmLogicDto.getIsnode() == null || fixmLogicDto.getIsnode()) {
                if (i != index) {
                    String labelTemp = fixmLogicDtos.get(index).getName();
                    Collections.swap(fixmLogicDtos, i, index);
                    labelIndexMap.put(fixmLogicDto.getName(), index);
                    labelIndexMap.put(labelTemp, i);
                }
                index++;
            }
        }
        for (String propertyStr : propertyOrder) {
            Integer i = labelIndexMap.get(propertyStr);
            if (i == null) {
                continue;
            }
            // 加上一层判断：避免propertyOrder中数据有误(包含node),将node误排在后。
            FixmLogicDto fixmLogicDto = fixmLogicDtos.get(i);
            // 目录一定是节点
            if (fixmLogicDto.getIsnode() != null && !fixmLogicDto.getIsnode()) {
                if (i == index) {
                    index++;
                    continue;
                }
                String labelTemp = fixmLogicDtos.get(index).getName();
                Collections.swap(fixmLogicDtos, i, index);
                labelIndexMap.put(propertyStr, index);
                labelIndexMap.put(labelTemp, i);
                index++;
            }
        }
    }

    @Override
    public List<FixmLogicDto> list2tree(String version) {
        List<FixmLogicDto> fixmLogicDtos = new ArrayList<>();
        List<FixmLogic> fixmLogics = fixmLogicRepository.findAllByVersion(version);
        fixmLogics.stream().forEach(fixmLogic -> {
            String route = fixmLogic.getXsdnode();
            this.recursionA(fixmLogicDtos, route, fixmLogic);
        });
//        fixmLogics.stream().map(FixmLogic::getXsdnode).forEach(item -> this.recursion(fixmNodes, item));
//        routes.forEach(item -> this.recursion(fixmNodes, item));
        this.orderTree(fixmLogicDtos, version);
        return fixmLogicDtos;
    }

    private void recursion(List<FixmLogicDto> fixmNodes, String route) {
        System.out.println(route);
        String[] split = route.split("->", 2);
        String currentNodeName = split[0];
        System.out.println(currentNodeName);
        FixmLogicDto fixmNodeItem = null;
        for (FixmLogicDto fixmNode : fixmNodes) {
            if (fixmNode.getName().equals(currentNodeName)) {
                fixmNodeItem = fixmNode;
                break;
            }
        }
        if (fixmNodeItem == null) {
            fixmNodeItem = new FixmLogicDto();
            // 加入list
            fixmNodes.add(fixmNodeItem);
            fixmNodeItem.setName(currentNodeName);
        }
        if (route.contains("->")) {
            if (fixmNodeItem.getChildren() == null) {
                // 设置子list
                List<FixmLogicDto> fixmNodeChildren = new ArrayList<>();
                fixmNodeItem.setChildren(fixmNodeChildren);
            }
            String otherNodeName = split[1];
            this.recursion(fixmNodeItem.getChildren(), otherNodeName);
        }
    }

    private void recursionA(List<FixmLogicDto> fixmLogicDtos, String route, FixmLogic fixmLogic) {
        String[] split = route.split("->", 2);
        String currentNodeName = split[0];
        FixmLogicDto fixmLogicDto = null;
        for (FixmLogicDto fixmLogicDto1 : fixmLogicDtos) {
            if (fixmLogicDto1.getName().equals(currentNodeName)) {
                fixmLogicDto = fixmLogicDto1;
                break;
            }
        }
        if (fixmLogicDto == null) {
            fixmLogicDto = new FixmLogicDto();
            // 加入list
            fixmLogicDtos.add(fixmLogicDto);
            fixmLogicDto.setName(currentNodeName);
        }
        if (route.contains("->")) {
            if (fixmLogicDto.getChildren() == null) {
                // 设置子list
                List<FixmLogicDto> fixmLogicDtoChildren = new ArrayList<>();
                fixmLogicDto.setChildren(fixmLogicDtoChildren);
            }
            String otherNodeName = split[1];
            this.recursionA(fixmLogicDto.getChildren(), otherNodeName, fixmLogic);
        } else {
            /**
             * 问题：
             * 1.后者会覆盖前者（脏数据）
             */
            BeanUtils.copyProperties(fixmLogic, fixmLogicDto);
        }
    }

    @Override
    @Transactional
    public void addLeaf(FixmLogicDto fixmLogicDto) {
        FixmLogic fixmLogic = new FixmLogic();
        BeanUtils.copyProperties(fixmLogicDto, fixmLogic);
        fixmLogic.setXsdnode(fixmLogicDto.getName());
        fixmLogicRepository.save(fixmLogic);
    }

    @Override
    @Transactional
    public void updateLeaf(Long id, FixmLogicDto fixmLogicDto) {
        FixmLogic fixmLogic = fixmLogicRepository.getOne(id);
        String xsdnode = fixmLogic.getXsdnode();
        int i = xsdnode.lastIndexOf("->");
        String concat = xsdnode.substring(0, i).concat("->").concat(fixmLogicDto.getName());
        fixmLogic.setXsdnode(concat);
        fixmLogicRepository.save(fixmLogic);
    }

    @Override
    @Transactional
    public void del(List<Long> ids) {
        ids.forEach(id -> fixmLogicRepository.deleteById(id));
    }

    /**
     * 操作：
     * (1).判断数据是否已存在
     * (2).验证重名冲突
     * (3).新增节点
     * (4).保存父节点排序
     * (5).转换fixmLogicDto返回
     * @param fixmLogicDto
     * @return
     */
    @Override
    @Transactional
    public FixmLogicDto add(FixmLogicDto fixmLogicDto) {
        // 版本
        String version = fixmLogicDto.getVersion();
        // 父节点
        String fatherXsdnode = fixmLogicDto.getFatherXsdnode();
        // 节点名
        String name = fixmLogicDto.getName();
        FixmProp fixmProp = FixmUtils.convert2fixmPropAdd(fatherXsdnode, name);
        BeanUtils.copyProperties(fixmProp, fixmLogicDto, "fatherXsdnode", "name");
        // 父节点前缀
        String fatherXsdnodePrefix = fixmProp.getFatherXsdnodePrefix();
        // 节点
        String xsdnode = fixmProp.getXsdnode();
        // 节点前缀
        String xsdnodePrefix = fixmProp.getXsdnodePrefix();
        // 节点名(第一级)
        String nodeName = fixmProp.getNodeName();
        // (1).判断数据是否已存在
        // (2).验证节点重名冲突
        this.validateName(version, fatherXsdnodePrefix, nodeName);
        // (3).新增节点
        FixmLogic fixmLogic = new FixmLogic();
        BeanUtils.copyProperties(fixmLogicDto, fixmLogic);
        // 转换xmlkey
        String xmlkey = this.convert2xmlKey(xsdnode);
        fixmLogic.setXsdnode(xsdnode);
        fixmLogic.setXmlkey(xmlkey);
        fixmLogic.setIsproperty(!fixmLogic.getIsnode());
        fixmLogic.setSplitsign(SPLIT_SIGN);
        fixmLogicRepository.save(fixmLogic);
        // (4).保存父节点排序
        FixmOrderDto fixmOrderDto = this.buildLogic2fatherOrder(fixmLogicDto);
        log.info("[更新节点排序数据], {}", fixmOrderDto);
        fixmOrderService.save(fixmOrderDto);

        List<String> gen = validator.gen(version, INTEGRATE_DB, "INTEGRATE_FLIGHT_INFO_TEST");
        if (gen != null && !gen.isEmpty()) {
            String xml = gen.get(0);
            // xsd_file_path: xsd文件路径
            boolean flag = false;
            try {
                flag = validator.validate_xml("./xsd/core4.1/Fixm.xsd", xml);
                if (flag) {
                    return this.convert2fixmLogicDto(fixmLogic);
                } else {
                    throw new ValidatorFixmException(606, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                String errorMessage = e.getMessage();
                throw new ValidatorFixmException(606, errorMessage);
            }
        }
        throw new ValidatorFixmException(606, "没有测试数据");

        // (5).转换fixmLogicDto返回
        // return this.convert2fixmLogicDto(fixmLogic);
    }

    // 更新节点全部属性接口(此时isnode不能为null)  即更新叶子节点
    @Override
    @Transactional
    /**
     * 操作：
     * 1.更新当前节点信息；更新所有子节点信息(xsdnode变更)
     * 1.1：更新xsdnode-修改名称
     * 1.2：更新xsdnode-跨级拖拽节点
     * 2.更新当前节点信息（xsdnode未变更，单纯其他属性变更[数据其他字段变更、排序码变更]）
     * 2.1：更新叶子结点xsdnode外信息（数据其他字段变更）
     * 2.2：同级拖拽节点 （排序吗变更）
     * 3.xsdnode变更+其他属性同时变更
     * 3.1：更新叶子节点全部属性（xsdnode + 数据其他字段变更）
     * 3.2：跨级拖拽节点（xsdnode + 排序吗变更）
     * 排序：
     * 1.对移动节点的原位置进行排序（父节点排序数据更新node/property序列）
     * 2.对移动节点的现位置进行排序（父节点排序数据更新node/property序列）
     * 3.更新当前节点的排序数据xsdnode(清理脏数据)；更新所有子节点的排序数据xsdnode
     */
    public void update(FixmLogicDto fixmLogicDto) {
        String version = fixmLogicDto.getVersion();
        String xsdnode = fixmLogicDto.getXsdnode();
        String newName = fixmLogicDto.getNewName();
        Boolean isnode = fixmLogicDto.getIsnode();
        FixmProp fixmProp = FixmUtils.convert2fixmPropUpdate(xsdnode, null, newName);
        BeanUtils.copyProperties(fixmProp, fixmLogicDto, "xsdnode", "newFatherXsdnode", "newName");
        String fatherXsdnode = fixmProp.getFatherXsdnode();
        String fatherXsdnodePrefix = fixmProp.getFatherXsdnodePrefix();
        String xsdnodePrefix = fixmProp.getXsdnodePrefix();
        String name = fixmProp.getName();
        String newFatherXsdnodePrefix = fixmProp.getNewFatherXsdnodePrefix();
        String newXsdnode = fixmProp.getNewXsdnode();
        // (1).节点相关数据是否存在
        List<FixmLogic> fixmLogics = fixmLogicRepository.findAllByVersionAndXsdnode(version, xsdnode);
        if (fixmLogics.isEmpty()) {
            // throw 节点数据不存在
            log.error("[节点数据不存在]");
            throw new BusinessException();
        }
        FixmLogic fixmLogic = fixmLogics.remove(0);
        Boolean dbIsnode = fixmLogic.getIsnode();
        if (isnode != dbIsnode) {
            // throw 节点属性(node/property)不可修改
            log.error("[节点属性(node/property)不可修改], {}", fixmLogic);
            throw new BusinessException();
        }
        if (newName != null && !newName.equalsIgnoreCase(name)) {
            // (2.1).检测是否同名冲突
            this.validateName(version, fatherXsdnodePrefix, newName);
            // (2.2).调用fixmOrder更新节点排序数据接口save()。更新父节点排序数据
            FixmOrderDto fixmOrderDto = this.buildLogic2fatherOrder(fixmLogicDto);
            log.info("[更新父节点排序数据], {}", fixmOrderDto);
            fixmOrderService.save(fixmOrderDto);
            // 更新节点名
            fixmLogicDto.setXsdnode(newXsdnode);
        }
        // (3).更新节点其他属性
        // copy剔除testvalue，testvalue用于更新flight_info_test数据表。
        BeanUtils.copyProperties(fixmLogicDto, fixmLogic, "id", "testvalue");
        fixmLogicRepository.save(fixmLogic);
        if (!fixmLogics.isEmpty()) {
            log.info("[清理FixmLogic脏数据], {}", fixmLogics);
            fixmLogicRepository.deleteAll(fixmLogics);
        }
        if (!StringUtils.isEmpty(fixmLogicDto.getSrcColumn())) {
            this.updateFlightInfo(fixmLogicDto.getSrcColumn(), fixmLogicDto.getTestvalue());
        }
    }

    private void updateFlightInfo(String srcColumn, Object testValue) {
        Map<String, Object> firstFlightInfo = this.findFirstFlightInfo();
        if (!firstFlightInfo.isEmpty()) {
            Long id = ((BigInteger)firstFlightInfo.get("id")).longValue();
            log.info("[更新flight_info_test] {}->{}", srcColumn, testValue);
            fixmLogicRepository.updateColumn(srcColumn, testValue, id);
        }
    }

    private void validateFixm() {

    }

    // 更新name接口(此时isnode传不传值都为null)
    @Override
    @Transactional
    public void updateName(FixmLogicDto fixmLogicDto) {
        String version = fixmLogicDto.getVersion();
        String xsdnode = fixmLogicDto.getXsdnode();
        String newName = fixmLogicDto.getNewName();
        FixmProp fixmProp = FixmUtils.convert2fixmPropUpdate(xsdnode, null, newName);
        BeanUtils.copyProperties(fixmProp, fixmLogicDto, "xsdnode", "newFatherXsdnode", "newName");
        String fatherXsdnode = fixmProp.getFatherXsdnode();
        String fatherXsdnodePrefix = fixmProp.getFatherXsdnodePrefix();
        String xsdnodePrefix = fixmProp.getXsdnodePrefix();
        String name = fixmProp.getName();
        String newFatherXsdnodePrefix = fixmProp.getNewFatherXsdnodePrefix();
        String newXsdnode = fixmProp.getNewXsdnode();
        // (1).节点相关数据是否存在
        List<FixmLogic> fixmLogics = fixmLogicRepository.findAllByVersionAndXsdnode(version, xsdnode);
        List<FixmLogic> childFixmLogics = fixmLogicRepository.findAllByVersionAndXsdnodeStartingWith(version, xsdnodePrefix);
        if (fixmLogics.isEmpty() && childFixmLogics.isEmpty()) {
            log.error("[节点数据不存在]");
            throw new BusinessException();
        }
        if (newName != null && !newName.equalsIgnoreCase(name)) {
            // (2).检测是否同名冲突
            this.validateName(version, fatherXsdnodePrefix, newName);
            // (3).更新节点及子节点xsdnode
            if (!fixmLogics.isEmpty()) {
                FixmLogic fixmLogic = fixmLogics.remove(0);
                fixmLogic.setXsdnode(newXsdnode);
                log.info("[更新节点数据], {}", fixmLogic);
                fixmLogicRepository.save(fixmLogic);
                if (!fixmLogics.isEmpty()) {
                    log.info("[清理FixmLogic脏数据], {}", fixmLogics);
                    fixmLogicRepository.deleteAll(fixmLogics);
                }
            }
            if (!childFixmLogics.isEmpty()) {
                childFixmLogics.forEach(fixmLogic -> fixmLogic.setXsdnode(fixmLogic.getXsdnode().replaceFirst(xsdnode, newXsdnode)));
                log.info("[更新子节点数据], {}", childFixmLogics);
                fixmLogicRepository.saveAll(childFixmLogics);
            }
            // (4).更新节点及子节点排序数据xsdnode
            FixmOrderDto fixmOrderDto = this.buildLogic2nameOrder(fixmLogicDto);
            log.info("[更新节点及子节点排序数据], {}", fixmOrderDto);
            fixmOrderService.updateXsdnode(fixmOrderDto);
            // (5).更新父节点排序数据
            FixmOrderDto fatherFixmOrderDto = this.buildLogic2fatherOrder(fixmLogicDto);
            log.info("[更新父节点排序数据], {}", fatherFixmOrderDto);
            fixmOrderService.save(fatherFixmOrderDto);
        }
    }

    // 更新fatherXsdnode接口
    @Override
    @Transactional
    public FixmLogicDto updateFatherXsdnode(FixmLogicDto fixmLogicDto) {
        String version = fixmLogicDto.getVersion();
        String xsdnode = fixmLogicDto.getXsdnode();
        String newFatherXsdnode = fixmLogicDto.getNewFatherXsdnode();
        FixmProp fixmProp = FixmUtils.convert2fixmPropUpdate(xsdnode, newFatherXsdnode, null);
        BeanUtils.copyProperties(fixmProp, fixmLogicDto, "xsdnode", "newFatherXsdnode", "newName");
        String fatherXsdnode = fixmProp.getFatherXsdnode();
        String xsdnodePrefix = fixmProp.getXsdnodePrefix();
        String name = fixmProp.getName();
        String newFatherXsdnodePrefix = fixmProp.getNewFatherXsdnodePrefix();
        String newXsdnode = fixmProp.getNewXsdnode();
        // (1).节点相关数据是否存在
        List<FixmLogic> fixmLogics = fixmLogicRepository.findAllByVersionAndXsdnode(version, xsdnode);
        List<FixmLogic> childFixmLogics = fixmLogicRepository.findAllByVersionAndXsdnodeStartingWith(version, xsdnodePrefix);
        if (fixmLogics.isEmpty() && childFixmLogics.isEmpty()) {
            log.error("[节点数据不存在]");
            throw new BusinessException();
        }
        if (newFatherXsdnode != null && !newFatherXsdnode.equalsIgnoreCase(fatherXsdnode)) {
            // (2).检测是否同名冲突
            this.validateName(version, newFatherXsdnodePrefix, name);
            // (3).更新节点及子节点xsdnode
            if (!fixmLogics.isEmpty()) {
                FixmLogic fixmLogic = fixmLogics.remove(0);
                fixmLogic.setXsdnode(newXsdnode);
                log.info("[更新节点数据], {}", fixmLogic);
                fixmLogicRepository.save(fixmLogic);
                if (!fixmLogics.isEmpty()) {
                    log.info("[清理FixmLogic脏数据], {}", fixmLogics);
                    fixmLogicRepository.deleteAll(fixmLogics);
                }
            }
            if (!childFixmLogics.isEmpty()) {
                childFixmLogics.forEach(fixmLogic -> fixmLogic.setXsdnode(fixmLogic.getXsdnode().replaceFirst(xsdnode, newXsdnode)));
                log.info("[更新子节点数据], {}", childFixmLogics);
                fixmLogicRepository.saveAll(childFixmLogics);
            }
            // (4).更新节点及子节点排序数据xsdnode
            FixmOrderDto fixmOrderDto = this.buildLogic2drawOrder(fixmLogicDto);
            log.info("[更新节点及子节点排序数据], {}", fixmOrderDto);
            fixmOrderService.updateXsdnode(fixmOrderDto);
            // (5).更新父节点排序数据order序列  和外层(3)合并逻辑
            // (6).更新新父节点排序数据order序列
            FixmOrderDto newFatherfixmOrderDto = this.buildLogic2newFatherOrder(fixmLogicDto);
            log.info("[更新新父节点排序数据], {}", newFatherfixmOrderDto);
            fixmOrderService.save(newFatherfixmOrderDto);
        }
        // (3).更新排序(同级拖拽)
        FixmOrderDto fatherFixmOrderDto = this.buildLogic2fatherOrder(fixmLogicDto);
        log.info("[更新父节点排序数据], {}", fatherFixmOrderDto);
        fixmOrderService.save(fatherFixmOrderDto);
        // 拖拽若是保留空父节点，则在这里返回
        // return null;
        return this.saveFather(fixmLogicDto);
    }

    @Override
    @Transactional
    /**
     * 操作：
     * 1.删除当前节点；删除当前节点所有子节点
     * 排序：
     * 1.对移除位置进行排序（父节点排序数据更新node/property序列）
     * 2.删除当前节点排序；删除当前节点所有子节点排序
     */
    public FixmLogicDto delete(FixmLogicDto fixmLogicDto) {
        String version = fixmLogicDto.getVersion();
        // 节点
        String xsdnode = fixmLogicDto.getXsdnode();
        FixmProp fixmProp = FixmUtils.convert2fixmPropUpdate(xsdnode, null, null);
        BeanUtils.copyProperties(fixmProp, fixmLogicDto, "xsdnode", "newFatherXsdnode", "newName");
        // 父节点
        String fatherXsdnode = fixmProp.getFatherXsdnode();
        // 父节点前缀
        String fatherXsdnodePrefix = fixmProp.getFatherXsdnodePrefix();
        // 节点数据
        List<FixmLogic> fixmLogics = fixmLogicRepository.findAllByVersionAndXsdnode(version, xsdnode);
        // 子节点数据
        List<FixmLogic> childFixmLogics = fixmLogicRepository.findAllByVersionAndXsdnodeStartingWith(version, xsdnode.concat(SPLIT_SIGN));
        // (1).节点相关数据是否存在
        if (fixmLogics.isEmpty() && childFixmLogics.isEmpty()) {
            log.error("[节点数据不存在]");
            throw new BusinessException();
        }
        if (!fixmLogics.isEmpty()) {
            log.info("[删除节点数据]，{}", fixmLogics);
            fixmLogicRepository.deleteAll(fixmLogics);
        }
        if (!childFixmLogics.isEmpty()) {
            log.info("[删除子节点数据], {}", childFixmLogics);
            fixmLogicRepository.deleteAll(childFixmLogics);
        }
        // 排序
        // 1.删除节点排序
        log.info("[删除节点及子节点排序数据]");
        fixmOrderService.del(version, xsdnode);
        // 2.更新父节点排序
        FixmOrderDto fatherFixmOrderDto = this.buildLogic2fatherOrder(fixmLogicDto);
        log.info("[更新父节点排序数据], {}", fatherFixmOrderDto);
        fixmOrderService.save(fatherFixmOrderDto);
        // 3.生成新节点
        // 同时满足，则考虑将父节点转换为数据项。
        // 1.同级节点个数为0[剔除该条件，通过前端判断删除，不管后端逻辑]、
        // 2.父节点不为空（空代表根节点）
        return this.saveFather(fixmLogicDto);
    }

    @Override
    public List<String> findFlightInfoColumns() {
        return fixmLogicRepository.findIntegrateFlightInfoColumns();
    }

    @Override
    public Map<String, Object> findFirstFlightInfo() {
        return fixmLogicRepository.findFirstIntegrateFlightInfo();
    }

    private FixmLogicDto saveFather(FixmLogicDto fixmLogicDto) {
        String fatherXsdnode = fixmLogicDto.getFatherXsdnode();
        String version = fixmLogicDto.getVersion();
        Boolean saveFather = fixmLogicDto.getSaveFather();
        if (saveFather && StringUtils.hasText(fatherXsdnode)) {
            log.info("[保存父节点数据]");
            List<FixmLogic> fatherFixmLogics = fixmLogicRepository.findAllByVersionAndXsdnode(version, fatherXsdnode);
            FixmLogic fatherFixmLogic;
            if (fatherFixmLogics.isEmpty()) {
                // 父节点不是数据项 新建数据项
                fatherFixmLogic = new FixmLogic();
                fatherFixmLogic.setXsdnode(fatherXsdnode);
                // 生成xmlkey
                String xmlkey = this.convert2xmlKey(fatherXsdnode);
                fatherFixmLogic.setXmlkey(xmlkey);
                fatherFixmLogic.setIsnode(true);
                fatherFixmLogic.setIsproperty(!fatherFixmLogic.getIsnode());
                fatherFixmLogic.setIsvalid(true);
                fatherFixmLogic.setSplitsign(SPLIT_SIGN);
                fatherFixmLogic.setVersion(version);
                log.info("[添加父节点数据], {}", fatherFixmLogic);
                fixmLogicRepository.save(fatherFixmLogic);
            } else {
                // 父节点是数据项 返回已有数据项 或不返回(前端即为相应值)
                // 查询只取第一个，查询阶段不进行脏数据清理。更新阶段再清理脏数据。
                fatherFixmLogic = fatherFixmLogics.get(0);
                log.info("[父节点数据已存在], {}", fatherFixmLogic);
            }
            return this.convert2fixmLogicDto(fatherFixmLogic);
        }
        return null;
    }

    private FixmOrderDto buildLogic2fatherOrder(FixmLogicDto fixmLogicDto) {
        FixmOrderDto fixmOrderDto = new FixmOrderDto();
        fixmOrderDto.setVersion(fixmLogicDto.getVersion());
        // isvalid 前端暂时没变更需求
        fixmOrderDto.setIsvalid(true);
        fixmOrderDto.setXsdnode(fixmLogicDto.getFatherXsdnode());
        fixmOrderDto.setNodeOrder(fixmLogicDto.getNodeOrder());
        fixmOrderDto.setPropertyOrder(fixmLogicDto.getPropertyOrder());
        return fixmOrderDto;
    }

    private FixmOrderDto buildLogic2newFatherOrder(FixmLogicDto fixmLogicDto) {
        FixmOrderDto fixmOrderDto = new FixmOrderDto();
        fixmOrderDto.setVersion(fixmLogicDto.getVersion());
        // isvalid 前端暂时没变更需求
        fixmOrderDto.setIsvalid(true);
        fixmOrderDto.setXsdnode(fixmLogicDto.getNewFatherXsdnode());
        fixmOrderDto.setNodeOrder(fixmLogicDto.getNewNodeOrder());
        fixmOrderDto.setPropertyOrder(fixmLogicDto.getNewPropertyOrder());
        return fixmOrderDto;
    }

    private FixmOrderDto buildLogic2nameOrder(FixmLogicDto fixmLogicDto) {
        FixmOrderDto fixmOrderDto = new FixmOrderDto();
        fixmOrderDto.setVersion(fixmLogicDto.getVersion());
        // isvalid 前端暂时没变更需求
        fixmOrderDto.setIsvalid(true);
        fixmOrderDto.setXsdnode(fixmLogicDto.getXsdnode());
        fixmOrderDto.setNodeOrder(fixmLogicDto.getNodeOrder());
        fixmOrderDto.setPropertyOrder(fixmLogicDto.getPropertyOrder());
        // 更名 newFatherXsdnode为null
        fixmOrderDto.setNewFatherXsdnode(null);
        fixmOrderDto.setNewName(fixmLogicDto.getNewName());
        return fixmOrderDto;
    }

    private FixmOrderDto buildLogic2drawOrder(FixmLogicDto fixmLogicDto) {
        FixmOrderDto fixmOrderDto = new FixmOrderDto();
        fixmOrderDto.setVersion(fixmLogicDto.getVersion());
        // isvalid 前端暂时没变更需求
        fixmOrderDto.setIsvalid(true);
        fixmOrderDto.setXsdnode(fixmLogicDto.getXsdnode());
        fixmOrderDto.setNodeOrder(fixmLogicDto.getNodeOrder());
        fixmOrderDto.setPropertyOrder(fixmLogicDto.getPropertyOrder());
        fixmOrderDto.setNewFatherXsdnode(fixmLogicDto.getNewFatherXsdnode());
        // 拖拽 newName为null
        fixmOrderDto.setNewName(null);
        return fixmOrderDto;
    }

    private void validateName(String version, String fatherXsdnodePrefix, String name) {
        String xsdnode = fatherXsdnodePrefix.concat(name);
        List<FixmLogic> fixmLogics = fixmLogicRepository.findAllByVersionAndXsdnode(version, xsdnode);
        if (!fixmLogics.isEmpty()) {
            log.error("[数据不一致] [节点数据已存在], {}", fixmLogics);
            throw new BusinessException();
        }
        List<FixmLogic> brothFixmLogics = fixmLogicRepository.findAllByVersionAndXsdnodeStartingWith(version, fatherXsdnodePrefix);
        for (FixmLogic fixmLogic : brothFixmLogics) {
            String brothXsdnode = fixmLogic.getXsdnode();
            /*
            条件:
            1.属性(不能有子元素)：新增节点名包含已存在属性数据
            2.节点、属性：新增节点名已存在(一模一样) 【更正：取消这个条件，上一步判断节点数据已存在。】
            3.属性(不能有子元素)：新增属性类型节点被已存在数据包含
            大小写不敏感【更正：后期调整，暂时统一进行toLowerCase】
            调整:
            1.contains 变更为 startsWith
            2.1xsdnodePrefix 变更为 xsdnode 两者相同情况不在此逻辑中。
            2.2brothXsdnode.concat(SPLIT_SIGN) 变更为 brothXsdnode   两者相同情况不在此逻辑中。
            */
            if (brothXsdnode.concat(SPLIT_SIGN).toLowerCase().startsWith(xsdnode.concat(SPLIT_SIGN).toLowerCase())) {
                log.error("[数据不一致] [同级节点不能同名], {}", fixmLogic);
                throw new BusinessException();
            }
        }
    }

    /**
     * 根据节点名xsdnode生成xmlkey
     * @param xsdnode
     * @return
     */
    private String convert2xmlKey(String xsdnode) {
        String name = xsdnode;
        if (name.contains(SPLIT_SIGN)) {
            name = name.substring(name.lastIndexOf(SPLIT_SIGN) + 2);
        }
        String xmlkey = name;
        if (xmlkey.contains(":")) {
            xmlkey = xmlkey.substring(xmlkey.indexOf(":") + 1);
        }
        return xmlkey;
    }

    private FixmLogicDto convert2fixmLogicDto(FixmLogic fixmLogic) {
        FixmLogicDto fixmLogicDto = new FixmLogicDto();
        BeanUtils.copyProperties(fixmLogic, fixmLogicDto);
        String name = fixmLogic.getXsdnode();
        if (name.contains(SPLIT_SIGN)) {
            name = name.substring(name.lastIndexOf(SPLIT_SIGN) + 2);
        }
        fixmLogicDto.setName(name);
        return fixmLogicDto;
    }
}
