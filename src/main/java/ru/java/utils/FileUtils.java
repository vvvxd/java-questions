package ru.java.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtils {


    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static File createFile(String fileName, String content) {
        File file = new File(fileName);
        if (file.exists()) {
            write(file, content);
            return file;
        } else {
            try {
                file.createNewFile();
                write(file, content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return file;
        }
    }

    private static void write(File file, String content) {
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write("");
            fw.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createHtmlDir(String path, File child) {
        File file1 = new File(path + File.separator + child.getName() + "/html");
        if (!file1.exists()) {
            file1.mkdir();
        }
    }
}
