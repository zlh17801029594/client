package cn.adcc.client.service;

import cn.adcc.client.DTO.FixmLogicDto;
import cn.adcc.client.utils.ColumnField;
import cn.adcc.client.utils.XsdFile;

import java.util.*;

public interface FixmLogicService {

    List<FixmLogicDto> list2tree(String version);

    void addLeaf(FixmLogicDto fixmLogicDto);

    void updateLeaf(Long id, FixmLogicDto fixmLogicDto);

    void del(List<Long> ids);

    /**
     * 增加 叶子结点 or 多层节点
     * 1.可能是增加node，也可能是增加property
     * @param fixmLogicDto
     * @return
     */
    FixmLogicDto add(FixmLogicDto fixmLogicDto);

    void update(FixmLogicDto fixmLogicDto);

    void updateName(FixmLogicDto fixmLogicDto);

    FixmLogicDto updateFatherXsdnode(FixmLogicDto fixmLogicDto);

    FixmLogicDto delete(FixmLogicDto fixmLogicDto);

    List<String> findFlightInfoColumns();

    Map<String, Object> findFirstFlightInfo();

    // 构建下拉字段详情
    List<ColumnField> findFlightInfoColumnsDesc();

    // 获取fixm versions
    List<String> findFixmVersions();

    // 删除指定版本的fixm数据
    void deleteByVersion(String version);
}
