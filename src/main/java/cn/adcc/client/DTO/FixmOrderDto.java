package cn.adcc.client.DTO;

import lombok.Data;

@Data
public class FixmOrderDto {
    private Long id;

    // 节点(用于 更新节点名/拖拽)
    private String xsdnode;

    // 新节点名（用于 更新节点名）
    private String newName;

    // 新父节点(用于 拖拽)
    private String newFatherXsdnode;

    private String propertyOrder;

    private String nodeOrder;

    private Boolean isvalid;

    /*private String splitsign;

    private String res1;

    private String res2;*/

    private String version;
}
