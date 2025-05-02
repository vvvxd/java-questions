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

import static ru.java.utils.FileUtils.createHtmlDir;

public class Main {

    private static String path = "./templates";

    private static Factory factory = new Factory();

    private static final Set<String> notProcessed = Set.of("src", "target", "templates", ".idea", ".git", "html", "files", "pom.xml", "README.md", "image", "file", "index.html", "img.png");

    public static void main(String[] args) throws IOException {
        readTemplate();

        File currentDir = new File("").getCanonicalFile();

        System.out.println("Processing MD file start...");
        process(currentDir, "./", true, null);
        System.out.println("Processing MD file end.");
    }

    public static Thema process(File file, String path, boolean main, String back) {
        if (file.isDirectory()) {
            System.out.println("Process directory:" + path + " start.");
            Map<String, Thema> map = new HashMap<>();
            for (File child : file.listFiles((dir, name) -> !notProcessed.contains(name))) {
                if (child.isDirectory()) {
                    createHtmlDir(path, child);
                    Thema thema = process(child, path + File.separator + child.getName(), false, main ? path + "/index.html" : path + "/html/index.html");
                    map.put(child.getName(), thema);
                } else {
                    Thema thema = factory.createFile(child, path + "/html/");
                    map.put(thema.name(), thema);
                }
            }

            Context context = main ? new Context(map, path, "Повторение", back, main) : new Context(map, path + "/html/", file.getName(), back, main);

            System.out.println("Process directory:" + path + " end.");

            return factory.createMainFile(context);

        }

        return null;
    }

    public static void readTemplate() {

        System.out.println("Loading templates...");

        File file = new File(path);

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

        System.out.println("Loaded templates.");

    }
}