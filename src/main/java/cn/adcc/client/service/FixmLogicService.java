package cn.adcc.client.service;

import cn.adcc.client.DTO.FixmLogicDto;
import cn.adcc.client.DTO.FixmLogicVersion;
import cn.adcc.client.VO.FixmLogicVO;
import cn.adcc.client.VO.SubversionCheckVO;
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
     * @param version
     * @param fixmLogicVO
     * @return
     */
    void add(String version, FixmLogicVO fixmLogicVO);

    void update(String version, FixmLogicVO fixmLogicVO);

    void updateName(String version, FixmLogicVO fixmLogicVO);

    void updateFatherXsdnode(String version, FixmLogicVO fixmLogicVO);

    void delete(String version, FixmLogicVO fixmLogicVO);

    List<String> findFlightInfoColumns();

    Map<String, Object> findFirstFlightInfo();

    // 构建下拉字段详情
    List<ColumnField> findFlightInfoColumnsDesc();

    // 获取fixm versions
    List<String> findFixmVersions();

    // 删除指定版本的fixm数据
    void deleteByVersion(String version);

    List<FixmLogicVersion> findFixmSubversions();

    /**
     * 更新子版本(用户勾选子版本节点)
     * 1.传递大版本、子版本、用户勾选的xsdnodes序列(xsdnode:树节点全路径)
     * @param version
     * @param subversion
     * @param chkXsdnodes
     * @param cancelChkXsdnodes
     */
    void updateSubversion(String version, String subversion, List<String> chkXsdnodes, List<String> cancelChkXsdnodes);

    /**
     * 删除子版本
     * 1.大版本、子版本
     * @param version
     * @param subversion
     */
    void deleteSubversion(String version, String subversion);
}
