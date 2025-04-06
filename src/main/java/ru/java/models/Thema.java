package ru.java.models;

import java.time.LocalDate;

public record Thema(String name, Long countQuestions, Type type, LocalDate lastUpdate) {
}
