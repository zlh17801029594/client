package cn.adcc.client.service.impl;

import cn.adcc.client.DO.FixmOrder;
import cn.adcc.client.DTO.FixmOrderDto;
import cn.adcc.client.DTO.FixmProp;
import cn.adcc.client.repository.FixmOrderRepository;
import cn.adcc.client.service.FixmOrderService;
import cn.adcc.client.utils.FixmUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FixmOrderServiceImpl implements FixmOrderService {
    @Autowired
    private FixmOrderRepository fixmOrderRepository;

    private static final String SPLIT_SIGN = "->";

    @Override
    public Map<String, FixmOrder> list(String version) {
        Map<String, FixmOrder> fixmOrderMap = new HashMap<>();
        List<FixmOrder> fixmOrderList = fixmOrderRepository.findAllByVersion(version);
        // 【兼容数据逻辑】，查询数据有重复
        for (FixmOrder fixmOrder : fixmOrderList) {
            String xsdnode = fixmOrder.getXsdnode();
            // 有重复值时 取第一个数据
            fixmOrderMap.putIfAbsent(xsdnode, fixmOrder);
        }
        return fixmOrderMap;
    }

    /**
     * 保存节点排序数据
     * 1.存在更新、不存在插入
     * 2.都为null不更新
     * 3.都为"" 删除。
     * @param fixmOrderDto
     */
    @Override
    @Transactional
    public void save(FixmOrderDto fixmOrderDto) {
        String version = fixmOrderDto.getVersion();
        String xsdnode = fixmOrderDto.getXsdnode();
        String nodeOrder = fixmOrderDto.getNodeOrder();
        String propertyOrder = fixmOrderDto.getPropertyOrder();
        nodeOrder = this.convertOrder(nodeOrder);
        propertyOrder = this.convertOrder(propertyOrder);
        if (nodeOrder != null || propertyOrder != null) {
            List<FixmOrder> fatherFixmOrders = fixmOrderRepository.findAllByVersionAndXsdnode(version, xsdnode);
            if (nodeOrder != null && nodeOrder.isEmpty() && propertyOrder != null && propertyOrder.isEmpty()) {
                if (!fatherFixmOrders.isEmpty()) {
                    log.info("[删除节点排序数据], {}", fatherFixmOrders);
                    fixmOrderRepository.deleteAll(fatherFixmOrders);
                }
            }else {
                if (!fatherFixmOrders.isEmpty()) {
                    // 已有数据，更新(判断是否发生变更)、清理脏数据
                    // 变更： 已有数据，更新、清理脏数据
                    FixmOrder fixmOrder = fatherFixmOrders.remove(0);
                    BeanUtils.copyProperties(fixmOrderDto, fixmOrder, "id", "nodeOrder", "propertyOrder");
                    if (fixmOrderDto.getNodeOrder() != null) {
                        fixmOrder.setNodeOrder(fixmOrderDto.getNodeOrder());
                    }
                    if (fixmOrderDto.getPropertyOrder() != null) {
                        fixmOrder.setPropertyOrder(fixmOrderDto.getPropertyOrder());
                    }
                    fixmOrderRepository.save(fixmOrder);
                    /*String dbNodeOrder = fixmOrder.getNodeOrder();
                    String dbPropertyOrder = fixmOrder.getPropertyOrder();
                    // nodeOrder、propertyOrder为null，则不更新
                    if (!((nodeOrder == null || dbNodeOrder != null && dbNodeOrder.equals(nodeOrder)) &&
                            (propertyOrder == null || dbPropertyOrder != null && dbPropertyOrder.equals(propertyOrder)))) {
                        if (nodeOrder != null) {
                            fixmOrder.setNodeOrder(nodeOrder);
                        }
                        if (propertyOrder != null) {
                            fixmOrder.setPropertyOrder(propertyOrder);
                        }
                        log.info("[更新节点排序数据-order序列], {}", fixmOrder);
                        fixmOrderRepository.save(fixmOrder);
                    }*/
                    if (!fatherFixmOrders.isEmpty()) {
                        log.info("[清理FixmOrder脏数据], {}", fatherFixmOrders);
                        fixmOrderRepository.deleteAll(fatherFixmOrders);
                    }
                } else {
                    // 没有该数据，新建
                    FixmOrder fixmOrder = new FixmOrder();
                    BeanUtils.copyProperties(fixmOrderDto, fixmOrder, "id");
                    fixmOrder.setSplitsign(SPLIT_SIGN);
                    log.info("[新增节点排序数据], {}", fixmOrder);
                    fixmOrderRepository.save(fixmOrder);
                }
            }
        }
    }

    /**
     * 更新节点及子节点排序数据-xsdnode信息
     * 注意：version、xsdnode、newName等均未校验
     * @param fixmOrderDto
     */
    @Override
    @Transactional
    public void updateXsdnode(FixmOrderDto fixmOrderDto) {
        String version = fixmOrderDto.getVersion();
        // 节点
        String xsdnode = fixmOrderDto.getXsdnode();
        // 新节点名（更新节点名）为null表示不进行更新节点名操作；notBlank
        String newName = fixmOrderDto.getNewName();
        // 新父节点（拖拽） 为null表示不进行拖拽操作；为""表示根节点
        String newFatherXsdnode = fixmOrderDto.getNewFatherXsdnode();
        FixmProp fixmProp = FixmUtils.convert2fixmPropUpdate(xsdnode, newFatherXsdnode, newName);
        String xsdnodePrefix = fixmProp.getXsdnodePrefix();
        String newXsdnode = fixmProp.getNewXsdnode();
        if (!newXsdnode.equals(xsdnode)) {
            // 更新xsdnode了，才进行后续操作
            List<FixmOrder> fixmOrders = fixmOrderRepository.findAllByVersionAndXsdnode(version, xsdnode);
            if (!fixmOrders.isEmpty()) {
                FixmOrder fixmOrder = fixmOrders.remove(0);
                fixmOrder.setXsdnode(newXsdnode);
                log.info("[更新节点排序数据-xsdnode], {}", fixmOrder);
                fixmOrderRepository.save(fixmOrder);
                if (!fixmOrders.isEmpty()) {
                    log.info("[清理FixmOrder脏数据], {}", fixmOrders);
                    fixmOrderRepository.deleteAll(fixmOrders);
                }
            }
            List<FixmOrder> childFixmOrders = fixmOrderRepository.findAllByVersionAndXsdnodeStartingWith(version, xsdnodePrefix);
            if (!childFixmOrders.isEmpty()) {
                childFixmOrders.forEach(fixmOrder -> {
                    fixmOrder.setXsdnode(fixmOrder.getXsdnode().replaceFirst(xsdnode, newXsdnode));
                });
                log.info("[更新子节点排序数据-xsdnode], {}", childFixmOrders);
                fixmOrderRepository.saveAll(childFixmOrders);
            }

        }
        // 分批调用（不在此调用）
        // 注意： 若用户选择删除空目录，则这里传递的将不是fatherXsdnode
        // this.add(version, fatherXsdnode);
        // this.add(version, newFatherXsdnode);
    }

    /**
     * 删除节点及子节点相关排序数据
     * @param version
     * @param xsdnode
     */
    @Override
    @Transactional
    public void del(String version, String xsdnode) {
        String xsdnodePrefix = xsdnode.concat(SPLIT_SIGN);
        List<FixmOrder> fixmOrders = fixmOrderRepository.findAllByVersionAndXsdnode(version, xsdnode);
        if (!fixmOrders.isEmpty()) {
            log.info("[删除节点排序数据], {}", fixmOrders);
            fixmOrderRepository.deleteAll(fixmOrders);
        }
        List<FixmOrder> childFixmOrders = fixmOrderRepository.findAllByVersionAndXsdnodeStartingWith(version, xsdnodePrefix);
        if (!childFixmOrders.isEmpty()) {
            log.info("[删除子节点排序数据], {}", childFixmOrders);
            fixmOrderRepository.deleteAll(childFixmOrders);
        }

    }

    @Transactional
    @Override
    public void deleteByVersion(String version) {
        List<FixmOrder> allByVersion = fixmOrderRepository.findAllByVersion(version);
        if (!allByVersion.isEmpty()) {
            log.info("[删除版本：'{}'全部节点排序数据]", version);
            fixmOrderRepository.deleteAll(allByVersion);
        }
    }

    private String convertOrder(String order) {
        if (StringUtils.hasLength(order)) {
            order = order.trim();
        }
        return order;
    }
}
