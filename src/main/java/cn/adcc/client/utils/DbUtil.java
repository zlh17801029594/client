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
                    generateLength(columnField);
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
        }  else if (sqlType.contains("int".toUpperCase())) {
            return "Integer";
        } else if (sqlType.contains("datetime".toUpperCase())) {
            return "Date";
        } else if (sqlType.contains("decimal".toUpperCase())) {
            return "BigDecimal";
        }
        return "String";
    }

    private static void generateLength(ColumnField columnField) {
        String sqlType = columnField.getType().toUpperCase();
        String javaType = columnField.getJavaType();
        int length = 0;
        int maxValue = 0;
        int minValue = 0;
        if (javaType.equals("String")) {
            if (sqlType.startsWith("VARCHAR")) {
                length = Integer.valueOf(sqlType.substring(sqlType.indexOf("(") + 1, sqlType.length() - 1));
            }
            // char、text类型默认不限制长度(无限大)
            columnField.setLength(length);
        } else if (javaType.equals("Integer")) {
            if (sqlType.startsWith("BIGINT")) {
                // bigint类型不提示最大最小值
                length = 19;
            } else if (sqlType.startsWith("INT")) {
                length = 10;
                minValue = -2147483648;
                maxValue = 2147483647;
            } else if (sqlType.startsWith("SMALLINT")) {
                length = 5;
                minValue = -32768;
                maxValue = 32767;
            } else if (sqlType.startsWith("TINYINT")) {
                length = 3;
                minValue = -128;
                maxValue = 127;
            }
            if (sqlType.contains("UNSIGNED")) {
                minValue = 0;
                maxValue = maxValue * 2 + 1;
            }
            // bigint 不提示范围值，其他未知int类型不限制长度
            columnField.setLength(length);
            columnField.setMinValue(minValue);
            columnField.setMaxValue(maxValue);
        }
    }
}
