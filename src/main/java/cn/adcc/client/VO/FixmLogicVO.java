package cn.adcc.client.VO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Deprecated
public class FixmLogicVO {
    // fatherXsdnode父节点全路径名
    private String fatherXsdnode;

    // oldXsdnode更新前全路径名
    private String oldXsdnode;

    // xsdnode节点全路径名/目录路径名
    private String xsdnode;

    // name节点名/多层级节点名
    /**
     * @NotNull 不能为null
     * @NotEmpty 不能为null 且 不能为空字符串（eg：str.length == 0）
     * @NotBlank 不能为null 且 不能为空字符串 且 不能为纯空格字符串（eg：" "）
     */
    @NotBlank
    private String name;

    // 后端处理赋值
    // private String xmlkey;

    private String srcColumn;

    private String explain;

    private String testvalue;

    @NotNull
    private Boolean isvalid;

    @NotNull
    private Boolean isnode;

    // 后端取反isnode赋值即可
    /*private Boolean isproperty;

    private String fileextension;

    private String convextension;

    private String srcDb;

    private String srcTable;

    private String valueextension;

    private String extensionkey;

    private Boolean islist;

    private Boolean issequence;

    private Boolean containref;
    // 后端赋值默认值->
    private String splitsign;

    private String valuesplit;

    private String res1;

    private String res2;

    private String res3;

    private String res4;
    // version版本通过接口path控制
    private String version;*/

    private String version;
}
