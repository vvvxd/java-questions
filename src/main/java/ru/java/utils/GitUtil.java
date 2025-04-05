package ru.java.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDate;

public class GitUtil {

    public static LocalDate getLastModifyFile(File file) {
        try {
            String filePath = file.getAbsolutePath();
            Process process = Runtime.getRuntime().exec(
                    new String[]{"git", "log", "-1", "--pretty=format:%cd", "--date=iso", filePath});

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line  = reader.readLine();
            if (line != null) {
                reader.close();
                process.destroy();
                return LocalDate.parse(line.split(" ")[0]);
            } else {
                return LocalDate.MIN;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
