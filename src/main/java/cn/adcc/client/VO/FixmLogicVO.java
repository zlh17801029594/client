package cn.adcc.client.VO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
public class FixmLogicVO {
    // 父节点(用于 新增)
    private String fatherXsdnode;

    // 节点（用于 更新节点名/拖拽）
    private String xsdnode;

    // 节点名（用于 新增）
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

    private String srcColumn;

    private Object testvalue;

    private Boolean isnode;

    private String explain;

    // 扩展文件名
    private String fileextension;

    // 转换方法
    private String convextension;

    private Boolean isvalid;
}
