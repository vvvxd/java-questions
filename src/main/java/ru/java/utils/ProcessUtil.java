package ru.java.utils;

import org.apache.commons.text.StringSubstitutor;
import ru.java.models.ThemeContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ProcessUtil {

    public static String[] processMdFile(File mdFile, String path, StringBuilder questions, String file) {
        try (FileInputStream input = new FileInputStream(mdFile)) {
            //TODO: OOM НЕ БУДИТ
            String md = new String(input.readAllBytes(), StandardCharsets.UTF_8);

            String[] parse = parseFile(questions, md);

            StringSubstitutor substitutor = new StringSubstitutor(Map.of("questions", questions.toString()));
            String substituted = substitutor.replace(file);

            FileUtils.createFile(path + mdFile.getName().replace(".md", ".html"), substituted);

            return parse;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] parseFile(StringBuilder questions, String md) {
        String[] parse = md.split("--------------------------------------------------------------------------------------------------------------------");

        String q = ThemeContext.question;

        for (String line : parse) {

            String[] aq = line.split("\r\n\r\n");

            String question = aq[0].replace("\r\n", "");
            StringBuilder answer = new StringBuilder();

            for (int i = 1; i < aq.length; i++) {
                answer.append(aq[i]);
            }

            String answerStr = PhotoUtil.findPhoto(answer.toString().replace("\r\n", "<br>").replace("\"", "'"));

            StringSubstitutor substitutor = new StringSubstitutor(Map.of("question", question, "answer", answerStr));

            String substituted = substitutor.replace(q);

            questions.append(substituted).append(",");
        }
        return parse;
    }
}
