package ru.java.factory;

import org.apache.commons.text.StringSubstitutor;
import ru.java.models.Context;
import ru.java.models.Thema;
import ru.java.models.ThemeContext;
import ru.java.models.Type;
import ru.java.utils.FileUtils;
import ru.java.utils.GitUtil;
import ru.java.utils.PhotoUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Factory {

    public Thema createMainFile(Context context, boolean isMain, String back) {

        StringBuilder refs = new StringBuilder();

        for (Map.Entry<String, Thema> thema : context.context().entrySet()) {
            switch (thema.getValue().type()) {
                case DIR -> {
                    String file = ThemeContext.main_item_dir;

                    String ref;
                    
                    if (isMain) {
                        ref = context.path() + "/" + thema.getKey() + "/html/main.html";
                    } else {
                        ref = "../" + thema.getKey() + "/html/main.html";
                    }


                    StringSubstitutor substitutor = new StringSubstitutor(Map.of("ref", ref, "name", thema.getKey(), "count", thema.getValue().countQuestions().toString()));

                    String substituted = substitutor.replace(file);

                    refs.append(substituted).append("\n");
                }
                case FILE -> {
                    String file = ThemeContext.main_item_file;
                    StringSubstitutor substitutor = new StringSubstitutor(Map.of("ref",  "./" + thema.getKey() + ".html", "name", thema.getKey(), "count", thema.getValue().countQuestions(), "last_date", thema.getValue().lastUpdate().toString()));

                    String substituted = substitutor.replace(file);

                    refs.append(substituted).append("\n");
                }
            }
        }

        if (refs.isEmpty()) {
            throw new RuntimeException("No references found");
        }

        String backHtml;

        if (back != null) {

            String backRef;

            if (isMain) {
                backRef = "../../" + back;
            } else {
                backRef = "../../../" + back;
            }
            StringSubstitutor substitutor = new StringSubstitutor(Map.of("back", backRef));
            backHtml = substitutor.replace(ThemeContext.back);
        } else {
            backHtml = "";
        }

        String main = ThemeContext.main;
        StringSubstitutor substitutor = new StringSubstitutor(Map.of("refs", refs.toString(), "name", context.name(), "back", backHtml));

        String substituted = substitutor.replace(main);

        FileUtils.createFile(context.path() + "main.html", substituted);

        return new Thema(context.name(), (long) context.context().size(), Type.DIR, null);
    }

    public Thema createFile(File mdFile, String path) {
        String file = ThemeContext.item;

        if (!mdFile.getName().contains(".md")) {
            throw new RuntimeException("File is not md");
        }

        try (FileInputStream input = new FileInputStream(mdFile)) {
            //TODO: OOM НЕ БУДИТ
            String md = new String(input.readAllBytes(), StandardCharsets.UTF_8);

            StringBuilder questions = new StringBuilder();

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

            StringSubstitutor substitutor = new StringSubstitutor(Map.of("questions", questions.toString()));
            String substituted = substitutor.replace(file);

            FileUtils.createFile(path + mdFile.getName().replace(".md", ".html"), substituted);

            return new Thema(mdFile.getName().split("\\.")[0], (long) parse.length, Type.FILE, GitUtil.getLastModifyFile(mdFile));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
