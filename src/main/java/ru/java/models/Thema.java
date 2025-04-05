package ru.java.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Thema (String name, Long countQuestions, Type type, LocalDate lastUpdate) {
}
