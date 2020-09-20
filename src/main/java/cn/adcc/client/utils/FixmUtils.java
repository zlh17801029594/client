package cn.adcc.client.utils;

import cn.adcc.client.DTO.FixmLogicDto;
import cn.adcc.client.DTO.FixmOrderDto;
import cn.adcc.client.DTO.FixmProp;
import org.springframework.util.StringUtils;

public class FixmUtils {
    public static final String SPLIT_SIGN = "->";

    /**
     * fatherXsdnode 为空白字符串时置为''
     * @param fatherXsdnode
     * @param name
     * @return
     */
    public static FixmProp convert2fixmPropAdd(String fatherXsdnode, String name) {
        FixmProp fixmProp = new FixmProp();
        // 父节点前缀
        String fatherXsdnodePrefix = "";
        if (StringUtils.hasText(fatherXsdnode)) {
            fatherXsdnodePrefix = fatherXsdnode.concat(SPLIT_SIGN);
        } else {
            fatherXsdnode = "";
        }
        fixmProp.setFatherXsdnode(fatherXsdnode);
        fixmProp.setFatherXsdnodePrefix(fatherXsdnodePrefix);
        // 节点
        String xsdnode = fatherXsdnodePrefix.concat(name);
        fixmProp.setName(name);
        fixmProp.setXsdnode(xsdnode);
        // 节点前缀
        String xsdnodePrefix = xsdnode.concat(SPLIT_SIGN);
        fixmProp.setXsdnodePrefix(xsdnodePrefix);
        // 节点名
        String nodeName = name.split(SPLIT_SIGN)[0];
        fixmProp.setNodeName(nodeName);
        return fixmProp;
    }

    /**
     * 【】标识输入参数
     * 后端 参数非空、notBlank、首尾及中间不能包含空格等在Validation中进行校验。
     * 前端 用户输入带空格，在传输前进行trim()
     * @param xsdnode
     * @param newFatherXsdnode
     * @param newName
     * @return
     */
    public static FixmProp convert2fixmPropUpdate(String xsdnode, String newFatherXsdnode, String newName) {
        FixmProp fixmProp = new FixmProp();
        // 【节点 xsdnode】
        // String xsdnode = fixmLogicDto.getXsdnode();
        // fixmProp.setXsdnode(xsdnode);
        // 父节点
        String fatherXsdnode = "";
        // 父节点前缀
        String fatherXsdnodePrefix = "";
        if (xsdnode.contains(SPLIT_SIGN)) {
            fatherXsdnode = xsdnode.substring(0, xsdnode.lastIndexOf(SPLIT_SIGN));
            fatherXsdnodePrefix = fatherXsdnode.concat(SPLIT_SIGN);
        }
        fixmProp.setXsdnode(xsdnode);
        fixmProp.setFatherXsdnode(fatherXsdnode);
        fixmProp.setFatherXsdnodePrefix(fatherXsdnodePrefix);
        // 节点前缀
        String xsdnodePrefix = xsdnode.concat(SPLIT_SIGN);
        fixmProp.setXsdnodePrefix(xsdnodePrefix);
        // 节点名
        String name = xsdnode.replaceFirst(fatherXsdnodePrefix, "");
        fixmProp.setName(name);

        // 【新父节点 newFatherXsdnode】（拖拽） 为null表示不进行拖拽操作；为""表示根节点
        // String newFatherXsdnode = fixmLogicDto.getNewFatherXsdnode();
        // 新父节点前缀
        String newFatherXsdnodePrefix = fatherXsdnodePrefix;
        if (newFatherXsdnode != null) {
            // 新父节点前缀(不传值不更新父节点，传空值当做根节点处理)
            newFatherXsdnodePrefix = "";
            if (StringUtils.hasText(newFatherXsdnode)) {
                newFatherXsdnodePrefix = newFatherXsdnode.concat(SPLIT_SIGN);
            } else {
                newFatherXsdnode = "";
            }
        } else {
            newFatherXsdnode = fatherXsdnode;
        }
        fixmProp.setNewFatherXsdnode(newFatherXsdnode);
        fixmProp.setNewFatherXsdnodePrefix(newFatherXsdnodePrefix);
        // 【新节点名】（更新节点名）为null表示不进行更新节点名操作；不可以为""、Blank
        // String newName = fixmLogicDto.getNewName();
        if (newName == null) {
            newName = name;
        }
        fixmProp.setNewName(newName);
        // 新节点
        String newXsdnode = newFatherXsdnodePrefix.concat(newName);
        fixmProp.setNewXsdnode(newXsdnode);
        // 新节点前缀
        String newXsdnodePrefix = newXsdnode.concat(SPLIT_SIGN);
        fixmProp.setNewXsdnodePrefix(newXsdnodePrefix);
        return fixmProp;
    }
}
