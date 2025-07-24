</details>

## Сериализация в Java

### 1. Что такое сериализация и десериализация в Java?

<details> <summary>Ответ</summary>

Сериализация — процесс преобразования объекта в поток байтов. Десериализация — восстановление объекта из байтов. В Java используется интерфейс `Serializable` и классы `ObjectOutputStream`/`ObjectInputStream`.


</details>

---

### 2. Как работает интерфейс `Serializable`?

<details> <summary>Ответ</summary>

`Serializable` — маркерный интерфейс, указывающий, что объект можно сериализовать. JVM использует рефлексию для сериализации нестатических и непереходных (`transient`) полей.

**Особенности**:
- Игнорируются `static` и `transient` поля.
- Сериализация рекурсивна для всех ссылок на объекты.

</details>

---

### 3. Что такое `serialVersionUID` и зачем он нужен?

<details> <summary>Ответ</summary>

`serialVersionUID` — уникальный идентификатор версии класса для проверки совместимости при десериализации.

**Зачем нужен**:
- Предотвращает `InvalidClassException` при изменении структуры класса.
- Рекомендуется задавать явно: `private static final long serialVersionUID = 1L;`.

**Пример**:
```java
class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
}
```

</details>

---

### 4. Как JVM обрабатывает сериализацию?

<details> <summary>Ответ</summary>

JVM использует `ObjectOutputStream` для сериализации и `ObjectInputStream` для десериализации:
1. Записывает заголовок, метаданные класса и значения полей.
2. Обрабатывает ссылки через дескрипторы (`handle`).
3. При десериализации создаёт объект без вызова конструктора.

**Кастомизация**:
- Методы `writeObject` и `readObject`.

</details>

---

### 5. Как использовать `transient` для исключения полей?

<details> <summary>Ответ</summary>

`transient` исключает поле из сериализации. Полезно для паролей или временных данных.

**Пример**:
```java
class User implements Serializable {
    private String username;
    private transient String password;
}
```

**Нюанс**: `transient`-поля получают значения по умолчанию при десериализации.


</details>

---

### 6. Как кастомизировать сериализацию с `writeObject` и `readObject`?

<details> <summary>Ответ</summary>

Методы `writeObject` и `readObject` позволяют управлять сериализацией.


</details>

---

### 7. Что такое `Externalizable` и как он отличается от `Serializable`?

<details> <summary>Ответ</summary>

`Externalizable` требует явной реализации `writeExternal` и `readExternal`.

**Отличия**:
- Полный контроль, но больше кода.
- Компактнее, без метаданных.
- Требуется конструктор без параметров.

</details>

---

### 8. Как сериализуются сложные графы объектов?

<details> <summary>Ответ</summary>

JVM использует дескрипторы для обработки ссылок и циклических зависимостей.

**Пример**:
```java
class Node implements Serializable {
    private Node next;
    private String data;
}
```

**Нюанс**: Большие графы могут вызвать `StackOverflowError`.


</details>

---

### 9. Как влияет наследование на сериализацию?

<details> <summary>Ответ</summary>

- Если родительский класс `Serializable`, подклассы тоже сериализуемы.
- Если родитель не `Serializable`, его поля игнорируются, требуется конструктор без параметров.

**Пример**:
```java
class Parent {
    int parentField;
    public Parent() {}
}

class Child extends Parent implements Serializable {
    int childField;
}
```

</details>

---

### 10. Как обеспечить совместимость версий?

<details> <summary>Ответ</summary>

- Задавайте `serialVersionUID`.
- Обрабатывайте новые/удалённые поля через `writeObject`/`readObject`.

**Пример**:
```java
class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String newField;

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (newField == null) newField = "default";
    }
}
```

</details>

---

### 11. Как сериализация работает с массивами и коллекциями?

<details> <summary>Ответ</summary>

- Массивы сериализуются, если элементы `Serializable`.
- Коллекции (`ArrayList`, `HashMap`) сериализуют содержимое рекурсивно.

**Нюанс**: Несериализуемые элементы вызывают `NotSerializableException`.


</details>

---

### 12. Какие исключения могут возникнуть?

<details> <summary>Ответ</summary>

- `NotSerializableException`
- `InvalidClassException`
- `StreamCorruptedException`
- `ClassNotFoundException`
- `IOException`

</details>

---

### 13. Как обеспечить безопасность при сериализации?

<details> <summary>Ответ</summary>

- Используйте `ObjectInputFilter` (Java 9+).
- Реализуйте `readResolve`.
- Избегайте ненадёжных источников.

**Пример**:
```java
ObjectInputFilter filter = ObjectInputFilter.Config.createFilter("com.example.Person;!*");
ObjectInputStream in = new ObjectInputStream(inputStream);
in.setObjectInputFilter(filter);
```

</details>

---

### 15. Как сериализация работает с перечислениями (`enum`)?

<details> <summary>Ответ</summary>

Перечисления сериализуются как имя элемента и класс. Восстанавливаются через `Enum.valueOf`.


</details>

---

### 16. Как сериализация взаимодействует с прокси-объектами?

<details> <summary>Ответ</summary>

Прокси сериализуются, если интерфейсы `Serializable`. Сохраняется `InvocationHandler`.


</details>

---

### 17. Как оптимизировать производительность сериализации?

<details> <summary>Ответ</summary>

- Используйте `Externalizable`.
- Помечайте поля `transient`.
- Кэшируйте `serialVersionUID`.

</details>

---

### 18. Как сериализация работает в распределённых системах?

<details> <summary>Ответ</summary>

Используется для передачи объектов по сети (например, RMI). Требует сериализуемости и совместимости версий.


</details>

---

### 19. Как альтернативные механизмы сравниваются с Java?

<details> <summary>Ответ</summary>

| Механизм   | Преимущества                     | Недостатки                       |
|------------|----------------------------------|----------------------------------|
| **Kryo**   | Быстрее, компактнее             | Требует регистрации классов      |
| **Jackson**| Читаемый JSON, кроссплатформенный| Медленнее для бинарных данных    |
| **Protobuf**| Компактный, быстрый             | Требует схемы                    |

**Недостатки Java-сериализации**:
- Медленная из-за рефлексии.
- Уязвимости при десериализации.

</details>

---

### 20. Как отлаживать проблемы с сериализацией?

<details> <summary>Ответ</summary>

- Включите `-Dsun.io.serialization.extendedDebugInfo=true`.
- Используйте `javap -v` для анализа байт-кода.
- Проверяйте в тестах.


---

</details>

## Jackson

### 21. Что такое Jackson и его основные компоненты?

<details> <summary>Ответ</summary>

Jackson — библиотека для работы с JSON и другими форматами (YAML, XML, CBOR). Компоненты:
- `jackson-core`: Потоковый API (`JsonParser`, `JsonGenerator`).
- `jackson-databind`: Привязка данных (`ObjectMapper`).
- `jackson-annotations`: Аннотации (`@JsonProperty`).
- Модули: Для Avro, YAML, Joda и др.

**Нюанс**: `jackson-jr` — лёгкая версия для простых задач.


</details>

---

### 22. Как настроить Jackson в проекте?

<details> <summary>Ответ</summary>

Добавьте зависимость в Maven:
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.19.0</version>
</dependency>
```

**Нюанс**: Совместимость версий модулей обязательна.


</details>

---

### 23. Как работает `ObjectMapper`?

<details> <summary>Ответ</summary>

`ObjectMapper` управляет сериализацией (`writeValue`) и десериализацией (`readValue`).

**Пример**:
```java
ObjectMapper mapper = new ObjectMapper();
String json = "{\"name\":\"Alice\",\"age\":30}";
Person person = mapper.readValue(json, Person.class);
```

**Нюанс**: Потокобезопасен, используйте один экземпляр.


</details>

---

### 24. Что такое потоковый API и когда его использовать?

<details> <summary>Ответ</summary>

Потоковый API (`JsonParser`, `JsonGenerator`) обрабатывает JSON построчно.

**Пример**:
```java
JsonFactory factory = new JsonFactory();
JsonParser parser = factory.createParser(new File("data.json"));
while (parser.nextToken() != null) {
    if (parser.getCurrentName() != null) {
        System.out.println(parser.getCurrentName() + ": " + parser.getText());
    }
}
```

**Когда использовать**: Для больших JSON-файлов.


</details>

---

### 25. Как работает модель дерева (Tree Model)?

<details> <summary>Ответ</summary>

Модель дерева использует `JsonNode` для представления JSON.

**Пример**:
```java
ObjectMapper mapper = new ObjectMapper();
JsonNode root = mapper.readTree("{\"name\":\"Bob\",\"age\":13}");
String name = root.get("name").asText();
```

**Нюанс**: Подходит для динамических структур.


</details>

---

### 26. Какие аннотации Jackson наиболее часто используются?

<details> <summary>Ответ</summary>

- `@JsonProperty`: Переименовывает поле.
- `@JsonIgnore`: Исключает поле.
- `@JsonInclude`: Управляет `null`-полями.
- `@JsonPropertyOrder`: Задаёт порядок полей.
- `@JsonSerialize`/`@JsonDeserialize`: Пользовательские сериализаторы.

**Пример**:
```java
@JsonProperty("full_name")
private String name;
```

</details>

---

### 27. Как настроить пользовательские сериализаторы и десериализаторы?

<details> <summary>Ответ</summary>

Наследуйте `JsonSerializer` и `JsonDeserializer`.

**Пример**:
```java
public class LocalDateSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
```

**Нюанс**: Используйте `jackson-datatype-jsr310` для стандартных типов.


</details>

---

### 28. Как Jackson обрабатывает сложные структуры данных?

<details> <summary>Ответ</summary>

Автоматически сериализует коллекции и карты, если элементы поддерживаются.

**Пример**:
```java
class ComplexData {
    private List<Person> persons;
    private Map<String, Integer> scores;
}
```

**Нюанс**: Используйте `TypeReference` для обобщённых типов.


</details>

---

### 29. Как Jackson поддерживает полиморфизм?

<details> <summary>Ответ</summary>

Используйте `@JsonTypeInfo` и `@JsonSubTypes`.

**Пример**:
```java
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Dog.class, name = "dog"),
    @JsonSubTypes.Type(value = Cat.class, name = "cat")
})
interface Animal {}
```

</details>

---

### 30. Как обрабатывать неизвестные поля в JSON?

<details> <summary>Ответ</summary>

Отключите `FAIL_ON_UNKNOWN_PROPERTIES` или используйте `@JsonAnySetter`.

**Пример**:
```java
mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
```

</details>

---

### 31. Как Jackson работает с датами и временем?

<details> <summary>Ответ</summary>

Используйте `@JsonFormat` или модуль `jackson-datatype-jsr310`.

**Пример**:
```java
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate date;
```

</details>

---

### 32. Как Jackson обрабатывает производительность и оптимизацию?

<details> <summary>Ответ</summary>

- Потоковый API для больших данных.
- Модуль Afterburner для ускорения.
- Кэширование метаданных.

</details>

---

### 33. Как обеспечить безопасность при десериализации?

<details> <summary>Ответ</summary>

- Используйте `ObjectInputFilter` или `PolymorphicTypeValidator`.
- Избегайте ненадёжных источников.

**Пример**:
```java
mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
```

</details>

---

### 34. Как Jackson поддерживает другие форматы данных?

<details> <summary>Ответ</summary>

Поддерживает YAML, XML, CBOR через модули.

**Пример (YAML)**:
```java
ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
```


