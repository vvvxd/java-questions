package ru.java.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotoUtil {

    private static Pattern pattern = Pattern.compile("!\\[.*\\]\\(.*\\)");

    public static String findPhoto(String answer) {

        Matcher matcher = pattern.matcher(answer);
        String result = answer;

        while (matcher.find()) {
            String value = matcher.group();
            String path = value.replaceAll("!\\[.*\\]\\(", "").replaceAll("\\)", "");
            StringBuilder photo = new StringBuilder();
            photo.append("<div style='display: flex; justify-content: center; margin: 10px 0;'>")  // Центрируем содержимое
                    .append("<br>")
                    .append("<img src='../").append(path).append("' alt='Photo' style='max-width: 100%;'>")
                    .append("<br>")
                    .append("</div>");
            result = result.replace(value, photo.toString());
        }

        return result;
    }
}
