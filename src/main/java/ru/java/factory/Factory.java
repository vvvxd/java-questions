package ru.java.factory;

import ru.java.models.Context;
import ru.java.models.Thema;
import ru.java.models.ThemeContext;
import ru.java.models.Type;
import ru.java.utils.ContextUtil;
import ru.java.utils.GitUtil;
import ru.java.utils.ProcessUtil;

import java.io.File;
import java.util.Map;

public class Factory {

    public Thema createMainFile(Context context) {
        StringBuilder refs = new StringBuilder();

        for (Map.Entry<String, Thema> thema : context.context().entrySet()) {
            switch (thema.getValue().type()) {
                case DIR -> {
                    String substituted = ContextUtil.createDir(context, thema);
                    refs.append(substituted).append("\n");
                }
                case FILE -> {
                    String substituted = ContextUtil.createFile(thema);
                    refs.append(substituted).append("\n");
                }
            }
        }

        if (refs.isEmpty()) {
            throw new RuntimeException("No references found in file" + context.path());
        }

        String backHtml = ContextUtil.getBackHtml(context);
        ContextUtil.createMain(context, refs, backHtml);
        return new Thema(context.name(), (long) context.context().size(), Type.DIR, null);
    }

    public Thema createFile(File mdFile, String path) {
        String file = ThemeContext.item;

        if (!mdFile.getName().contains(".md")) {
            throw new RuntimeException("File " + mdFile.getName() + " is not md");
        }

        StringBuilder questions = new StringBuilder();

        String[] parse = ProcessUtil.processMdFile(mdFile, path, questions, file);

        return new Thema(mdFile.getName().split("\\.")[0], (long) parse.length, Type.FILE, GitUtil.getLastModifyFile(mdFile));
    }
}
