package com.billlv.codegenerator.common.utils;


public class CodeGenUtils {

    // SQL 数据类型到 Java 类型的映射
    public static String mapSqlTypeToJavaType(String sqlType) {
        switch (sqlType.toUpperCase()) {
            case "CHAR":
            case "VARCHAR":
            case "TEXT":
            case "LONGTEXT":
            case "TINYTEXT":
            case "MEDIUMTEXT":
                return "String";
            case "INT":
            case "INTEGER":
            case "SMALLINT":
            case "TINYINT":
            case "MEDIUMINT":
                return "Integer";
            case "BIGINT":
                return "Long";
            case "FLOAT":
            case "REAL":
                return "Float";
            case "DOUBLE":
            case "DOUBLE PRECISION":
                return "Double";
            case "DECIMAL":
            case "NUMERIC":
                return "java.math.BigDecimal";
            case "DATE":
            case "TIME":
            case "TIMESTAMP":
            case "DATETIME":
                return "java.util.Date";
            case "BOOLEAN":
            case "BIT":
            case "BIT(1)":
                return "Boolean";
            default:
                // 处理未支持的类型
                System.out.println("Warning: Unsupported SQL type encountered: " + sqlType + ", defaulting to String.");
                return "String";
        }
    }


    // 将下划线命名转换为驼峰命名，首字母大写
    public static String convertToClassName(String name) {
        StringBuilder result = new StringBuilder();
        boolean toUpperCase = true; // 类名首字母大写

        for (char ch : name.toCharArray()) {
            if (ch == '_') {
                toUpperCase = true;
            } else if (toUpperCase) {
                result.append(Character.toUpperCase(ch)); // 转换为大写
                toUpperCase = false;
            } else {
                result.append(ch); // 保持原样
            }
        }

        return result.toString();
    }

    // 示例调用
    public static void main(String[] args) {
        String tableName = "act_hi_comment";
        String className = convertToClassName(tableName);
        System.out.println("Converted class name: " + className); // 输出: ActHiComment
    }


    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

}
