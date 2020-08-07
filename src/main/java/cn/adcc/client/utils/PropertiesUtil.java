package cn.adcc.client.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {
    public static String readProp(String filePath, String key) {
        Properties props = new Properties();
        try {
            InputStream is = new FileInputStream(filePath);
            props.load(is);
            return props.getProperty(key);
        } catch (FileNotFoundException e) {
            log.error("配置文件不存在", e);
            throw new RuntimeException("配置文件不存在");
        } catch (IOException e) {
            log.error("读取配置文件异常", e);
            throw new RuntimeException("读取配置文件异常");
        }
    }

    public static void writeProp(String filePath, String key, String value) {
        Properties props = new Properties();
        try {
            // closable ?
            InputStream is = new FileInputStream(filePath);
            props.load(is);
            props.setProperty(key, value);
            OutputStream os = new FileOutputStream(filePath);
            props.store(os, "update '" + key + "' value");
        } catch (FileNotFoundException e) {
            log.error("配置文件不存在", e);
            throw new RuntimeException("配置文件不存在");
        } catch (IOException e) {
            log.error("读取配置文件异常", e);
            throw new RuntimeException("读取配置文件异常");
        }
    }

    public static void removeProp(String filePath, String key) {
        Properties props = new Properties();
        try {
            InputStream is = new FileInputStream(filePath);
            props.load(is);
            if (props.containsKey(key)) {
                props.remove(key);
            }
            OutputStream os = new FileOutputStream(filePath);
            props.store(os, "remove '" + key + "' value");
        } catch (FileNotFoundException e) {
            log.error("配置文件不存在", e);
            throw new RuntimeException("配置文件不存在");
        } catch (IOException e) {
            log.error("读取配置文件异常", e);
            throw new RuntimeException("读取配置文件异常");
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getSecurityManager());
        // 读取src目录下配置文件
        // System.out.println(PropertiesUtil.class.getClassLoader().getResource("map.properties").getPath());
        // readProp("./xsd/map.properties", "core4.1");
        // removeProp("./xsd/map.properties", "core4.1");
        // writeProp("./xsd/map.properties", "core4.1", "abc");
    }
}
