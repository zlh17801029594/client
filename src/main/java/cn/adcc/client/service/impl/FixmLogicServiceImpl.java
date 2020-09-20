package cn.adcc.client.service.impl;

import cn.adcc.client.DO.FixmLogic;
import cn.adcc.client.DO.FixmOrder;
import cn.adcc.client.DTO.FixmLogicDto;
import cn.adcc.client.DTO.FixmLogicVersion;
import cn.adcc.client.DTO.FixmOrderDto;
import cn.adcc.client.DTO.FixmProp;
import cn.adcc.client.VO.FixmLogicVO;
import cn.adcc.client.VO.SubversionCheckVO;
import cn.adcc.client.exception.BusinessException;
import cn.adcc.client.repository.FixmLogicRepository;
import cn.adcc.client.service.FixmLogicService;
import cn.adcc.client.service.FixmOrderService;
import cn.adcc.client.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @PersistenceContext
    private EntityManager entityManager;

    private static final String SPLIT_SIGN = "->";

    private Map<String, FixmOrder> fixmOrderMap;

    private List<String> routes;

    private List<FixmOrder> fixmOrders;

    {
        // 静态变量初始化
        // 场景一：
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

        // 场景二
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
        // 【兼容数据逻辑】，查询数据有重复
        Map<String, FixmLogic> fixmLogicMap = new HashMap<>();
        for (FixmLogic fixmLogic : fixmLogics) {
            String xsdnode = fixmLogic.getXsdnode();
            // 重复数据取第一条数据
            fixmLogicMap.putIfAbsent(xsdnode, fixmLogic);
        }
        fixmLogicMap.keySet().forEach(xsdnode -> {
            this.recursionA(fixmLogicDtos, xsdnode, fixmLogicMap.get(xsdnode));
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

    private void recursionA(List<FixmLogicDto> fixmLogicDtos, String xsdnode, FixmLogic fixmLogic) {
        String[] split = xsdnode.split("->", 2);
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
            // 目录isnode默认为true, isvalid为true
            // 调整：目录没有这些属性项，目录转化为节点时，前端再加上。
            // fixmLogicDto.setIsnode(true);
            // fixmLogicDto.setIsvalid(true);
        }
        if (xsdnode.contains("->")) {
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
             * 1.后者会覆盖前者（脏数据）ok 已解决(解决方式：提前对数据去重)
             */
            BeanUtils.copyProperties(fixmLogic, fixmLogicDto);
            Set<String> subversions = Collections.emptySet();
            String subversionStr = fixmLogic.getChildversion();
            if (StringUtils.hasText(subversionStr)) {
                subversions = new LinkedHashSet<>(Arrays.asList(StringUtils.tokenizeToStringArray(subversionStr, ",")));
            }
            fixmLogicDto.setSubversions(subversions);
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
     * @param version
     * @param fixmLogicVO
     * @return
     */
    @Override
    @Transactional
    public void add(String version, FixmLogicVO fixmLogicVO) {
        // 父节点
        String fatherXsdnode = fixmLogicVO.getFatherXsdnode();
        // 节点名
        String name = fixmLogicVO.getName();
        FixmProp fixmProp = FixmUtils.convert2fixmPropAdd(fatherXsdnode, name);
        BeanUtils.copyProperties(fixmProp, fixmLogicVO);
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
        BeanUtils.copyProperties(fixmLogicVO, fixmLogic, "testvalue");
        // 转换xmlkey
        String xmlkey = this.convert2xmlKey(xsdnode);
        fixmLogic.setXmlkey(xmlkey);
        fixmLogic.setIsproperty(!fixmLogic.getIsnode());
        fixmLogic.setSplitsign(SPLIT_SIGN);
        fixmLogic.setVersion(version);
        fixmLogicRepository.save(fixmLogic);
        // (4).保存父节点排序
        FixmOrderDto fixmOrderDto = this.buildLogic2fatherOrder(version, fixmLogicVO);
        log.info("[更新节点排序数据], {}", fixmOrderDto);
        fixmOrderService.save(fixmOrderDto);
        // 更新flight_info_test数据表
        if (!StringUtils.isEmpty(fixmLogicVO.getSrcColumn())) {
            this.updateFlightInfo(fixmLogicVO.getSrcColumn(), fixmLogicVO.getTestvalue());
        }
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
    public void update(String version, FixmLogicVO fixmLogicVO) {
        String xsdnode = fixmLogicVO.getXsdnode();
        String newName = fixmLogicVO.getNewName();
        Boolean isnode = fixmLogicVO.getIsnode();
        FixmProp fixmProp = FixmUtils.convert2fixmPropUpdate(xsdnode, null, newName);
        BeanUtils.copyProperties(fixmProp, fixmLogicVO);
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
            log.error("[节点数据不存在], xsdnode: {}", xsdnode);
            throw new BusinessException();
        }
        FixmLogic fixmLogic = fixmLogics.remove(0);
        Boolean dbIsnode = fixmLogic.getIsnode();
        if (isnode != dbIsnode) {
            // throw 节点属性(node/property)不可修改
            log.error("[节点属性(node/property)不可修改], {}", fixmLogic);
            throw new BusinessException();
        }
        if (newName != null && !newName.equals(name)) {
            // (2.1).检测是否同名冲突
            this.validateName(version, fatherXsdnodePrefix, newName);
            // (2.2).调用fixmOrder更新节点排序数据接口save()。更新父节点排序数据
            FixmOrderDto fixmOrderDto = this.buildLogic2fatherOrder(version, fixmLogicVO);
            log.info("[更新父节点排序数据], {}", fixmOrderDto);
            fixmOrderService.save(fixmOrderDto);
            // 更新节点名
            fixmLogicVO.setXsdnode(newXsdnode);
        }
        // (3).更新节点其他属性
        // copy剔除testvalue，testvalue用于更新flight_info_test数据表。
        BeanUtils.copyProperties(fixmLogicVO, fixmLogic, "testvalue");
        fixmLogicRepository.save(fixmLogic);
        if (!fixmLogics.isEmpty()) {
            log.info("[清理FixmLogic脏数据], {}", fixmLogics);
            fixmLogicRepository.deleteAll(fixmLogics);
        }
        // 更新flight_info_test数据表
        if (!StringUtils.isEmpty(fixmLogicVO.getSrcColumn())) {
            this.updateFlightInfo(fixmLogicVO.getSrcColumn(), fixmLogicVO.getTestvalue());
        }
    }

    private void updateFlightInfo(String srcColumn, Object testValue) {
        Map<String, Object> firstFlightInfo = this.findFirstFlightInfo();
        if (!firstFlightInfo.isEmpty()) {
            Long id = ((BigInteger)firstFlightInfo.get("id")).longValue();
            log.info("[更新flight_info_test] {}->{}", srcColumn, testValue);
            String sql = String.format("update integrate.integrate_flight_info_test set `%s` = ?1 where id = ?2", srcColumn);
            System.out.println(sql);
            // fixmLogicRepository.updateColumn(srcColumn, testValue, id);
            Query nativeQuery = entityManager.createNativeQuery(sql);
            nativeQuery.setParameter(1, testValue);
            nativeQuery.setParameter(2, id);
            nativeQuery.executeUpdate();
        }
    }

    // 更新name接口(此时isnode传不传值都为null)
    @Override
    @Transactional
    public void updateName(String version, FixmLogicVO fixmLogicVO) {
        String xsdnode = fixmLogicVO.getXsdnode();
        String newName = fixmLogicVO.getNewName();
        FixmProp fixmProp = FixmUtils.convert2fixmPropUpdate(xsdnode, null, newName);
        BeanUtils.copyProperties(fixmProp, fixmLogicVO);
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
            log.error("[节点数据不存在], xsdnode: {}, xsdnodePrefix: {}", xsdnode, xsdnodePrefix);
            throw new BusinessException();
        }
        if (newName != null && !newName.equals(name)) {
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
            FixmOrderDto fixmOrderDto = this.buildLogic2nameOrder(version, fixmLogicVO);
            log.info("[更新节点及子节点排序数据], {}", fixmOrderDto);
            fixmOrderService.updateXsdnode(fixmOrderDto);
            // (5).更新父节点排序数据
            FixmOrderDto fatherFixmOrderDto = this.buildLogic2fatherOrder(version, fixmLogicVO);
            log.info("[更新父节点排序数据], {}", fatherFixmOrderDto);
            fixmOrderService.save(fatherFixmOrderDto);
        }
    }

    // 更新fatherXsdnode接口
    @Override
    @Transactional
    public void updateFatherXsdnode(String version, FixmLogicVO fixmLogicVO) {
        String xsdnode = fixmLogicVO.getXsdnode();
        String newFatherXsdnode = fixmLogicVO.getNewFatherXsdnode();
        // 删除空目录节点
        String deleteXsdnode = fixmLogicVO.getDeleteXsdnode();
        FixmProp fixmProp = FixmUtils.convert2fixmPropUpdate(xsdnode, newFatherXsdnode, null);
        BeanUtils.copyProperties(fixmProp, fixmLogicVO);
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
        if (newFatherXsdnode != null && !newFatherXsdnode.equals(fatherXsdnode)) {
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
            FixmOrderDto fixmOrderDto = this.buildLogic2drawOrder(version, fixmLogicVO);
            log.info("[更新节点及子节点排序数据], {}", fixmOrderDto);
            fixmOrderService.updateXsdnode(fixmOrderDto);
            // (5).更新父节点排序数据order序列  和外层(3)合并逻辑
            // (6).更新新父节点排序数据order序列
            FixmOrderDto newFatherfixmOrderDto = this.buildLogic2newFatherOrder(version, fixmLogicVO);
            log.info("[更新新父节点排序数据], {}", newFatherfixmOrderDto);
            fixmOrderService.save(newFatherfixmOrderDto);
            // 删除空目录节点
            if (deleteXsdnode != null && xsdnode.startsWith(deleteXsdnode.concat(SPLIT_SIGN))) {
                // 节点数据
                List<FixmLogic> blankFixmLogics = fixmLogicRepository.findAllByVersionAndXsdnode(version, deleteXsdnode);
                // 子节点数据
                List<FixmLogic> blankChildFixmLogics = fixmLogicRepository.findAllByVersionAndXsdnodeStartingWith(version, deleteXsdnode.concat(SPLIT_SIGN));
                if (!blankFixmLogics.isEmpty()) {
                    log.info("[删除空目录节点数据]，{}", blankFixmLogics);
                    fixmLogicRepository.deleteAll(blankFixmLogics);
                }
                if (!blankChildFixmLogics.isEmpty()) {
                    log.info("[删除空目录子节点数据], {}", blankChildFixmLogics);
                    fixmLogicRepository.deleteAll(blankChildFixmLogics);
                }
                // 排序
                // 1.删除节点排序
                log.info("[删除空目录节点及子节点排序数据]");
                fixmOrderService.del(version, deleteXsdnode);
                // 空目录父节点
                String blankFatherXsdnode = "";
                if (deleteXsdnode.contains(SPLIT_SIGN)) {
                    blankFatherXsdnode = deleteXsdnode.substring(0, deleteXsdnode.lastIndexOf(SPLIT_SIGN));
                }
                fixmLogicVO.setFatherXsdnode(blankFatherXsdnode);
            }
        }
        // (3).更新排序(同级拖拽)
        FixmOrderDto fatherFixmOrderDto = this.buildLogic2fatherOrder(version, fixmLogicVO);
        log.info("[更新父节点排序数据], {}", fatherFixmOrderDto);
        fixmOrderService.save(fatherFixmOrderDto);
        // 拖拽若是保留空父节点，则在这里返回
        // return null;
        this.saveFather(version, fixmLogicVO);
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
    public void delete(String version, FixmLogicVO fixmLogicVO) {
        // 节点
        String xsdnode = fixmLogicVO.getXsdnode();
        FixmProp fixmProp = FixmUtils.convert2fixmPropUpdate(xsdnode, null, null);
        BeanUtils.copyProperties(fixmProp, fixmLogicVO);
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
        FixmOrderDto fatherFixmOrderDto = this.buildLogic2fatherOrder(version, fixmLogicVO);
        log.info("[更新父节点排序数据], {}", fatherFixmOrderDto);
        fixmOrderService.save(fatherFixmOrderDto);
        // 3.生成新节点
        // 同时满足，则考虑将父节点转换为数据项。
        // 1.同级节点个数为0[剔除该条件，通过前端判断删除，不管后端逻辑]、
        // 2.父节点不为空（空代表根节点）
        this.saveFather(version, fixmLogicVO);
    }

    @Override
    public List<String> findFlightInfoColumns() {
        return fixmLogicRepository.findIntegrateFlightInfoColumns();
    }

    @Override
    public Map<String, Object> findFirstFlightInfo() {
        return fixmLogicRepository.findFirstIntegrateFlightInfo();
    }

    @Override
    public List<ColumnField> findFlightInfoColumnsDesc() {
        List<Map<String, String>> integrateFlightInfoColumnsDesc = fixmLogicRepository.findIntegrateFlightInfoColumnsDesc();
        return DbUtil.generateColumnsDesc(integrateFlightInfoColumnsDesc);
    }

    @Override
    public List<String> findFixmVersions() {
        return fixmLogicRepository.findFixmVersions();
    }

    @Transactional
    @Override
    public void deleteByVersion(String version) {
        List<FixmLogic> allByVersion = fixmLogicRepository.findAllByVersion(version);
        if (!allByVersion.isEmpty()) {
            log.info("[删除版本：'{}'全部节点数据]", version);
            fixmLogicRepository.deleteAll(allByVersion);
        }
        log.info("[删除版本：'{}'全部节点排序数据]", version);
        fixmOrderService.deleteByVersion(version);
    }

    @Override
    public List<FixmLogicVersion> findFixmSubversions() {
        List<FixmLogicVersion> versionSubversions = new ArrayList<>();
        List<FixmLogic> fixmLogicList = fixmLogicRepository.findAll();
        // 方式一：
        // Map<String, List<FixmLogic>> versionFixmLogicList = fixmLogicList.stream().collect(Collectors.groupingBy(FixmLogic::getVersion));
        // 方式二：
        Map<String, List<FixmLogic>> versionFixmLogicList = new HashMap<>();
        // 记录版本先后顺序
        List<String> versionOrder = new ArrayList<>();
        fixmLogicList.forEach(fixmLogic -> {
            String version = fixmLogic.getVersion();
            List<FixmLogic> fixmLogicList1 = versionFixmLogicList.get(version);
            if (fixmLogicList1 == null) {
                fixmLogicList1 = new ArrayList<>();
                versionFixmLogicList.put(version, fixmLogicList1);
                versionOrder.add(version);
            }
            fixmLogicList1.add(fixmLogic);
        });
        versionFixmLogicList.keySet().forEach(version -> {
            FixmLogicVersion fixmLogicVersion = new FixmLogicVersion();
            fixmLogicVersion.setName(version);
            List<FixmLogic> fixmLogics = versionFixmLogicList.get(version);
            Set<String> subversions = fixmLogics.stream()
                    .map(FixmLogic::getChildversion)
                    .filter(StringUtils::hasText)
                    .flatMap(subversionStr -> Arrays.stream(StringUtils.tokenizeToStringArray(subversionStr, ",")))
                    .collect(Collectors.toSet());
            if (!subversions.isEmpty()) {
                fixmLogicVersion.setSubversions(subversions);
            }
            versionSubversions.add(fixmLogicVersion);
        });
        // 排序版本信息[选择排序]
        Map<String, Integer> integerMap = versionSubversions.stream().collect(Collectors.toMap(FixmLogicVersion::getName, versionSubversions::indexOf));
        for (int index = 0; index < versionOrder.size(); index++) {
            String version = versionOrder.get(index);
            Integer i = integerMap.get(version);
            if (i == index) {
                continue;
            }
            String tempVersion = versionSubversions.get(index).getName();
            Collections.swap(versionSubversions, i, index);
            integerMap.put(version, index);
            integerMap.put(tempVersion, i);
        }
        return versionSubversions;
    }

    /**
     * 更新子版本(用户勾选子版本节点)
     * 1.传递大版本、子版本、用户勾选的xsdnodes序列(xsdnode:树节点全路径)
     *
     * @param version
     * @param subversion
     * @param chkXsdnodes
     * @param cancelChkXsdnodes
     */
    @Transactional
    @Override
    public void updateSubversion(String version, String subversion, List<String> chkXsdnodes, List<String> cancelChkXsdnodes) {
        List<FixmLogic> fixmLogicList = new ArrayList<>();
        if (chkXsdnodes != null && !chkXsdnodes.isEmpty()) {
            // 勾选节点
            List<FixmLogic> chkFixmLogics = fixmLogicRepository.findAllByVersionAndXsdnodeIn(version, chkXsdnodes);
            if (!chkFixmLogics.isEmpty()) {
                chkFixmLogics.forEach(fixmLogic -> {
                    // 数据库中子版本集合(数据库查询结果varchar转List => null,""差异？ 使用tokenizeToStringArray方法 null,""都会会转为空list)
                    String subversionStr = fixmLogic.getChildversion();
                    Set<String> subversions = new LinkedHashSet<>();
                    if (StringUtils.hasText(subversionStr)) {
                        subversions = new LinkedHashSet<>(Arrays.asList(StringUtils.tokenizeToStringArray(subversionStr, ",")));
                    }
                    if (!subversions.contains(subversion)) {
                        // 勾选节点 数据条目不包含当前子版本，则需要添加子版本
                        subversions.add(subversion);
                        fixmLogic.setChildversion(StringUtils.collectionToCommaDelimitedString(subversions));
                        fixmLogicList.add(fixmLogic);
                    }
                });
            }
        }
        if (cancelChkXsdnodes != null && !cancelChkXsdnodes.isEmpty()) {
            // 取消勾选节点
            List<FixmLogic> cancelChkFixmlogics = fixmLogicRepository.findAllByVersionAndXsdnodeIn(version, cancelChkXsdnodes);
            if (!cancelChkFixmlogics.isEmpty()) {
                cancelChkFixmlogics.forEach(fixmLogic -> {
                    String subversionStr = fixmLogic.getChildversion();
                    if (StringUtils.hasText(subversionStr)) {
                        // 子节点字段存在数据
                        Set<String> subversions = new LinkedHashSet<>(Arrays.asList(StringUtils.tokenizeToStringArray(subversionStr, ",")));
                        if (subversions.contains(subversion)) {
                            // 取消勾选 数据条目包含当前子版本，则需要移除子版本
                            subversions.remove(subversion);
                            fixmLogic.setChildversion(StringUtils.collectionToCommaDelimitedString(subversions));
                            fixmLogicList.add(fixmLogic);
                        }
                    }
                });
            }
        }
        if (!fixmLogicList.isEmpty()) {
            log.info("更新数据: {}", fixmLogicList);
            fixmLogicRepository.saveAll(fixmLogicList);
        }
    }

    /**
     * 删除子版本
     * 1.大版本、子版本
     *
     * @param version
     * @param subversion
     */
    @Transactional
    @Override
    public void deleteSubversion(String version, String subversion) {
        List<FixmLogic> allByVersion = fixmLogicRepository.findAllByVersion(version);
        if (!allByVersion.isEmpty()) {
            // 记录更新节点数据
            List<FixmLogic> fixmLogicList = new ArrayList<>();
            allByVersion.forEach(fixmLogic -> {
                // 数据库中子版本集合(数据库查询结果varchar转List => null,""差异？)
                String subversionStr = fixmLogic.getChildversion();
                if (StringUtils.hasText(subversionStr)) {
                    Set<String> subversions = new LinkedHashSet<>(Arrays.asList(StringUtils.tokenizeToStringArray(subversionStr, ",")));
                    if (subversions.contains(subversion)) {
                        // 取消勾选 数据条目包含当前子版本，则需要移除子版本
                        subversions.remove(subversion);
                        fixmLogic.setChildversion(StringUtils.collectionToCommaDelimitedString(subversions));
                        fixmLogicList.add(fixmLogic);
                    }
                }
            });
            if (!fixmLogicList.isEmpty()) {
                log.info("数据更新：{}", fixmLogicList);
                fixmLogicRepository.saveAll(fixmLogicList);
            }
        }
    }

    private void saveFather(String version, FixmLogicVO fixmLogicVO) {
        String fatherXsdnode = fixmLogicVO.getFatherXsdnode();
        Boolean saveFather = fixmLogicVO.getSaveFather();
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
        }
    }

    private FixmOrderDto buildLogic2fatherOrder(String version, FixmLogicVO fixmLogicVO) {
        FixmOrderDto fixmOrderDto = new FixmOrderDto();
        fixmOrderDto.setVersion(version);
        // isvalid 前端暂时没变更需求
        fixmOrderDto.setIsvalid(true);
        fixmOrderDto.setXsdnode(fixmLogicVO.getFatherXsdnode());
        fixmOrderDto.setNodeOrder(fixmLogicVO.getNodeOrder());
        fixmOrderDto.setPropertyOrder(fixmLogicVO.getPropertyOrder());
        return fixmOrderDto;
    }

    private FixmOrderDto buildLogic2newFatherOrder(String version, FixmLogicVO fixmLogicVO) {
        FixmOrderDto fixmOrderDto = new FixmOrderDto();
        fixmOrderDto.setVersion(version);
        // isvalid 前端暂时没变更需求
        fixmOrderDto.setIsvalid(true);
        fixmOrderDto.setXsdnode(fixmLogicVO.getNewFatherXsdnode());
        fixmOrderDto.setNodeOrder(fixmLogicVO.getNewNodeOrder());
        fixmOrderDto.setPropertyOrder(fixmLogicVO.getNewPropertyOrder());
        return fixmOrderDto;
    }

    private FixmOrderDto buildLogic2nameOrder(String version, FixmLogicVO fixmLogicVO) {
        FixmOrderDto fixmOrderDto = new FixmOrderDto();
        fixmOrderDto.setVersion(version);
        // isvalid 前端暂时没变更需求
        fixmOrderDto.setIsvalid(true);
        fixmOrderDto.setXsdnode(fixmLogicVO.getXsdnode());
        fixmOrderDto.setNodeOrder(fixmLogicVO.getNodeOrder());
        fixmOrderDto.setPropertyOrder(fixmLogicVO.getPropertyOrder());
        // 更名 newFatherXsdnode为null
        fixmOrderDto.setNewFatherXsdnode(null);
        fixmOrderDto.setNewName(fixmLogicVO.getNewName());
        return fixmOrderDto;
    }

    private FixmOrderDto buildLogic2drawOrder(String version, FixmLogicVO fixmLogicVO) {
        FixmOrderDto fixmOrderDto = new FixmOrderDto();
        fixmOrderDto.setVersion(version);
        // isvalid 前端暂时没变更需求
        fixmOrderDto.setIsvalid(true);
        fixmOrderDto.setXsdnode(fixmLogicVO.getXsdnode());
        fixmOrderDto.setNodeOrder(fixmLogicVO.getNodeOrder());
        fixmOrderDto.setPropertyOrder(fixmLogicVO.getPropertyOrder());
        fixmOrderDto.setNewFatherXsdnode(fixmLogicVO.getNewFatherXsdnode());
        // 拖拽 newName为null
        fixmOrderDto.setNewName(null);
        return fixmOrderDto;
    }

    private void validateName(String version, String fatherXsdnodePrefix, String name) {
        String xsdnode = fatherXsdnodePrefix.concat(name);
        List<FixmLogic> fixmLogics = fixmLogicRepository.findAllByVersionAndXsdnode(version, xsdnode);
        if (!fixmLogics.isEmpty()) {
            log.error("[节点数据已存在], {}", fixmLogics);
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
            if (brothXsdnode.concat(SPLIT_SIGN).startsWith(xsdnode.concat(SPLIT_SIGN))) {
                log.error("[同级节点不能同名], {}", fixmLogic);
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

    @Deprecated
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
