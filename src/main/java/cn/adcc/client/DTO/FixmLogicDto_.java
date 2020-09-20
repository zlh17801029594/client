package cn.adcc.client.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
public class FixmLogicDto_ {
    private Long id;

    // 父节点(用于 新增)
    @NotNull //""表示根节点
    private String fatherXsdnode;

    // 节点（用于 更新节点名/拖拽）
    private String xsdnode;

    // 节点名（用于 新增）
    @NotBlank
    private String name;

    // 父节点node排序
    private String nodeOrder;

    // 父节点property排序
    private String propertyOrder;

    // 新父节点（用于 拖拽）
    private String newFatherXsdnode;

    // 新节点名（用于 更新节点名）
    // 正则表达式(不能包含->,即只能更新为单级目录)
    private String newName;

    // 新父节点node排序
    private String newNodeOrder;

    // 新父节点property排序
    private String newPropertyOrder;

    // 拖拽时删除节点
    private String deleteXsdnode;

    // 是否保留父级空目录节点
    private Boolean saveFather = false;

    // private String xmlkey;

    private String srcColumn;

    private String explain;

    private Object testvalue;

    @NotNull //新增
    private Boolean isvalid;

    @NotNull //新增
    private Boolean isnode;

    // 扩展文件名
    private String fileextension;

    // 转换方法
    private String convextension;

    /*private Boolean isproperty;

    private String srcDb;

    private String srcTable;

    private String valueextension;

    private String extensionkey;

    private Boolean islist;

    private Boolean issequence;

    private Boolean containref;

    private String splitsign;

    private String valuesplit;

    private String res1;

    private String res2;

    private String res3;

    private String res4;*/

    private String version;

    private Set<String> subversions;

    // 查询时使用，新增等无需使用
    private List<FixmLogicDto_> children;
}
