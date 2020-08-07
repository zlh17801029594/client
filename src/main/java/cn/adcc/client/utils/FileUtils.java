package cn.adcc.client.utils;

import ch.qos.logback.core.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<XsdFile> listFiles(String filename) {
        List<XsdFile> xsdFiles = new ArrayList<>();
        File file = new File(filename);
        if (file.isDirectory()) {
            for (File fileChild : file.listFiles()) {
                xsdFiles.add(recursionFiles(fileChild));
            }
        }
        return xsdFiles;
    }

    private static XsdFile recursionFiles(File file) {
        XsdFile xsdFile = new XsdFile();
        xsdFile.setName(file.getName());
        boolean isDirectory = file.isDirectory();
        if (isDirectory) {
            List<XsdFile> xsdFiles = new ArrayList<>();
            xsdFile.setChildren(xsdFiles);
            File[] files = file.listFiles();
            for (File fileChild : files) {
                xsdFiles.add(recursionFiles(fileChild));
            }
        }
        return xsdFile;
    }

    private static List<XsdFile> recursionFiles1(File parentFile) {
        List<XsdFile> xsdFiles = new ArrayList<>();
        File[] files = parentFile.listFiles();
        for (File file : files) {
            XsdFile xsdFile = new XsdFile();
            xsdFile.setName(file.getName());
            if (file.isDirectory()) {
                // 目录
                xsdFile.setChildren(recursionFiles1(file));
            }
            xsdFiles.add(xsdFile);
        }
        return xsdFiles;
    }

    public static void readFiles() throws IOException {
        // 1.尝试现成工具吧
        // FileUtil.
        File file = new File("./xsd");
        File fileTest = new File("./xsd1");
        // 2.测试方法功能
        // file.toURI();
        // 3.感兴趣接口
        // file.renameTo(new XsdFile("/新位置"));
        File[] files = file.listFiles();
        printFiles(files);
        for (File fileTemp : files) {
            File[] files1 = fileTemp.listFiles();
            printFiles(files1);
        }
    }

    public static void printFiles(File[] files) throws IOException {
        for (File fileTemp : files) {
            StringBuilder sb = new StringBuilder(fileTemp.getName()).append("[").append("\n");
            sb.append("getName: ").append(fileTemp.getName()).append("\n");
            sb.append("absolutePath: ").append(fileTemp.getAbsolutePath()).append("\n");
            sb.append("canonicalPath: ").append(fileTemp.getCanonicalPath()).append("\n");
            sb.append("getParent: ").append(fileTemp.getParent()).append("\n");
            sb.append("getPath(): ").append(fileTemp.getPath()).append("\n");
            sb.append("isDirectory: ").append(fileTemp.isDirectory()).append("\n");
            sb.append("isFile: ").append(fileTemp.isFile()).append("\n");
            sb.append("]");
            System.out.println(sb);
        }
    }

    public static void main(String[] args) {
        try {
            readFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
