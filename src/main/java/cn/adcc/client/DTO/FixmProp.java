package cn.adcc.client.DTO;

import lombok.Data;

@Data
public class FixmProp {
    private String fatherXsdnode;

    private String fatherXsdnodePrefix;

    private String xsdnode;

    private String xsdnodePrefix;

    private String name;

    // 新增多层级节点时 节点名(用于 父节点排序)
    private String nodeName;

    private String newFatherXsdnode;

    private String newFatherXsdnodePrefix;

    private String newXsdnode;

    private String newXsdnodePrefix;

    private String newName;
}
