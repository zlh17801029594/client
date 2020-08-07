package cn.adcc.client.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbUtil {
    public static List<ColumnField> generateColumnsDesc(List<Map<String, String>> mapList) {
        return mapList.stream()
                .map(map -> {
                    ColumnField columnField = new ColumnField();
                    String field = map.get("Field");
                    String type = map.get("Type");
                    String aNull = map.get("Null");
                    columnField.setName(field);
                    columnField.setType(type);
                    columnField.setJavaType(sqlTypeToJavaType(type));
                    columnField.setNullAble("YES".equals(aNull));
                    return columnField;
                })
                .collect(Collectors.toList());
    }

    private static String sqlTypeToJavaType(String sqlType) {
        sqlType = sqlType.toUpperCase();
        if (sqlType.contains("varchar".toUpperCase())
                || sqlType.contains("char".toUpperCase())
                || sqlType.contains("text".toUpperCase())) {
            return "String";
        /*} else if (sqlType.contains("tinyint".toUpperCase())) {
            return "Boolean";*/
        } else if (sqlType.contains("bigint".toUpperCase())) {
            return "Long";
        } else if (sqlType.contains("int".toUpperCase())) {
            return "Integer";
        } else if (sqlType.contains("datetime".toUpperCase())) {
            return "Date";
        } else if (sqlType.contains("decimal".toUpperCase())) {
            return "BigDecimal";
        }
        return "String";
    }
}
