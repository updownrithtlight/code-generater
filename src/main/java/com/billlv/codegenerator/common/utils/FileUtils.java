package com.billlv.codegenerator.common.utils;

import java.nio.file.*;
import java.io.IOException;
import java.util.Comparator;

public class FileUtils {

    /**
     * 删除指定目录及其内容（如果存在），然后重新创建该目录。
     *
     * @param dirPath 要操作的目录路径
     * @throws IOException 如果删除或创建目录失败
     */
    public static void recreateDirectory(Path dirPath) throws IOException {
        // 删除目录（如果存在）
        if (Files.exists(dirPath)) {
            Files.walk(dirPath)
                    .sorted(Comparator.reverseOrder()) // 确保子文件或子目录先被删除
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete: " + path, e);
                        }
                    });
        }

        // 创建目录
        Files.createDirectories(dirPath);
        System.out.println("Directory recreated: " + dirPath);
    }

    public static void main(String[] args) {
        Path tempDir = Paths.get(System.getProperty("user.dir"), "temp", "codegen");

        try {
            recreateDirectory(tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
