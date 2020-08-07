package cn.adcc.client.utils;

import lombok.Data;

import java.util.List;

@Data
public class XsdFile {
    private String name;
    private List<XsdFile> children;
}
