package ru.java;

import ru.java.factory.Factory;
import ru.java.models.Context;
import ru.java.models.Thema;
import ru.java.models.ThemeContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {

    private static String path = "./templates";

    private static Factory factory = new Factory();

    private static final Set<String> notProcessed = Set.of("src", "target", "templates", ".idea", ".git", "html", "files", "pom.xml", "README.md", "image", "file", "main.html", "img.png");

    public static void main(String[] args) throws IOException {
        readTemplate();

        File currentDir = new File("").getCanonicalFile();

        process(currentDir, "./", true, null);
    }

    public static Thema process(File file, String path, boolean main, String back) {
        if (file.isDirectory()) {
            Map<String, Thema> map = new HashMap<>();
            for (File child : file.listFiles((dir, name) -> !notProcessed.contains(name))) {
                if (child.isDirectory()) {
                    File file1 = new File(path + File.separator + child.getName() + "/html");
                    if (!file1.exists()) {
                        file1.mkdir();
                    }
                    Thema thema = process(child, path + File.separator + child.getName(), false, main ? path + "/main.html" : path + "/html/main.html");
                    map.put(child.getName(), thema);
                } else {
                    Thema thema = factory.createFile(child, path + "/html/");
                    map.put(thema.name(), thema);
                }
            }

            if (!main) {
                Context context = new Context(map, path + "/html/", file.getName());
                return factory.createMainFile(context, false, back);
            } else {
                Context context = new Context(map, path, "Повторение");
                return factory.createMainFile(context, true, back);
            }
        }

        return null;
    }

    public static void readTemplate() {
        File file = new File(path);

        System.out.println(file.exists());

        for (File sub : file.listFiles()) {
            try (FileInputStream fis = new FileInputStream(sub)) {
                switch (sub.getName().split("\\.")[0]) {
                    case "item" -> {
                        ThemeContext.item = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
                    }
                    case "main" -> {
                        ThemeContext.main = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
                    }
                    case "main-item-dir" -> {
                        ThemeContext.main_item_dir = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
                    }
                    case "main-item-file" -> {
                        ThemeContext.main_item_file = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
                    }
                    case "question" -> {
                        ThemeContext.question = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
                    }
                    case "back" -> {
                        ThemeContext.back = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}