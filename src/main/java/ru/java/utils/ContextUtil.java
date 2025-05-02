package ru.java.utils;

import org.apache.commons.text.StringSubstitutor;
import ru.java.models.Context;
import ru.java.models.Thema;
import ru.java.models.ThemeContext;

import java.util.Map;

public class ContextUtil {

    public static String getBackHtml(Context context) {
        String backHtml;
        if (context.back() != null) {

            String backRef;

            if (context.main()) {
                backRef = "../../" + context.back();
            } else {
                backRef = "../../../" + context.back();
            }
            StringSubstitutor substitutor = new StringSubstitutor(Map.of("back", backRef));
            backHtml = substitutor.replace(ThemeContext.back);
        } else {
            backHtml = "";
        }
        return backHtml;
    }

    public static String createDir(Context context, Map.Entry<String, Thema> thema) {
        String file = ThemeContext.main_item_dir;

        String ref = context.main()? context.path() + "/" + thema.getKey() + "/html/index.html": "../" + thema.getKey() + "/html/index.html";

        StringSubstitutor substitutor = new StringSubstitutor(Map.of("ref", ref, "name", thema.getKey(), "count", thema.getValue().countQuestions().toString()));

        return substitutor.replace(file);
    }

    public static String createFile(Map.Entry<String, Thema> thema) {
        String file = ThemeContext.main_item_file;

        StringSubstitutor substitutor = new StringSubstitutor(Map.of("ref", ("./" + thema.getKey() + ".html").replace("//", "/"), "name", thema.getKey(), "count", thema.getValue().countQuestions(), "last_date", thema.getValue().lastUpdate().toString()));

        return substitutor.replace(file);
    }

    public static void createMain(Context context, StringBuilder refs, String backHtml) {
        String main = ThemeContext.main;
        StringSubstitutor substitutor = new StringSubstitutor(Map.of("refs", refs.toString().replace("//", "/"), "name", context.name(), "back", backHtml.replace("//", "/")));

        String substituted = substitutor.replace(main);

        FileUtils.createFile(context.path() + "index.html", substituted);
    }

}
