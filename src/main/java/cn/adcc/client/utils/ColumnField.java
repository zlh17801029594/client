package cn.adcc.client.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ColumnField {
    private String name;
    /*数据库类型信息前端不可见*/
    @JsonIgnore
    private String type;
    private String javaType;
    // 最大长度
    private int length;
    // 最大最小值范围(针对Integer类型)；
    private int maxValue;
    private int minValue;
    private Boolean nullAble;
}
