package cn.adcc.client.utils;

import lombok.Data;

@Data
public class ColumnField {
    private String name;
    private String type;
    private String javaType;
    private Boolean nullAble;
}
