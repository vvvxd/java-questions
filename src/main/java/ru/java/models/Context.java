package ru.java.models;

import java.util.Map;

public record Context(Map<String, Thema> context, String path, String name, String back, boolean main) {

}
