
</details>


## 1. Цель класса Optional

<details> <summary>Ответ</summary>

Класс `Optional` в Java предназначен для представления опциональных значений вместо использования `null`-ссылок. Это контейнер, который может содержать объект типа `T` или быть пустым, помогая избежать `NullPointerException` и улучшая читаемость кода.

**Описание**:
- `Optional` — это обёртка, которая явно указывает на возможность отсутствия значения.
- Способствует функциональному программированию и явной обработке случаев, когда значение может быть `null`.


</details>

---

## 2. Как создать Optional?

<details> <summary>Ответ</summary>

Существует три основных способа создания объекта `Optional`:

### 2.1. Пустой Optional

Для создания пустого `Optional` используется статический метод `empty()`:

```java
Optional<String> empty = Optional.empty();
```

### 2.2. Optional с помощью of()

Метод `of()` создаёт `Optional`, содержащий непустое значение. Аргумент не должен быть `null`, иначе будет выброшен `NullPointerException`:

```java
Optional<String> opt = Optional.of("Baeldung");
```

### 2.3. Optional с помощью ofNullable()

Метод `ofNullable()` создаёт `Optional`, который может быть пустым, если передан `null`:

```java
String name = null;
Optional<String> opt = Optional.ofNullable(name); // Возвращает пустой Optional
```


</details>

---

## 3. Как проверить наличие значения в Optional?

<details> <summary>Ответ</summary>

### 3.1. Метод isPresent()

Метод `isPresent()` возвращает `true`, если в `Optional` есть значение, и `false`, если он пустой:

```java
Optional<String> opt = Optional.of("Baeldung");
System.out.println(opt.isPresent()); // true
```

### 3.2. Метод isEmpty()

С Java 11 метод `isEmpty()` возвращает `true`, если `Optional` пустой:

```java
Optional<String> opt = Optional.of("Baeldung");
System.out.println(opt.isEmpty()); // false
```


</details>

---

## 4. Что делает ifPresent()?

<details> <summary>Ответ</summary>

Метод `ifPresent()` выполняет действие над значением, если оно присутствует, избегая явной проверки на `null`.

**До Optional**:
```java
if (name != null) {
    System.out.println(name.length());
}
```

**С Optional**:
```java
Optional<String> opt = Optional.of("baeldung");
opt.ifPresent(name -> System.out.println(name.length())); // 8
```

**Преимущества**:
- Уменьшает вероятность `NullPointerException`.
- Заставляет явно обрабатывать случаи отсутствия значения.
- Поддерживает функциональный стиль.


</details>

---

## 5. Что делает ifPresentOrElse()?

<details> <summary>Ответ</summary>

Метод `ifPresentOrElse()` выполняет одно действие, если значение присутствует, и другое, если `Optional` пустой. Принимает два параметра: `Consumer` для значения и `Runnable` для пустого случая.

**Пример**:
```java
personRepository.findById(id)
    .ifPresentOrElse(
        person -> System.out.println(person.getFirstName() + " " + person.getLastName()),
        () -> System.out.println("Иван Иванов")
    );
```


</details>

---

## 6. Что делает orElse()?

<details> <summary>Ответ</summary>

Метод `orElse()` возвращает значение из `Optional`, если оно присутствует, или значение по умолчанию, если `Optional` пустой.

**Пример**:
```java
@Test
public void whenOrElseWorks_thenCorrect() {
    String nullName = null;
    String name = Optional.ofNullable(nullName).orElse("john");
    assertEquals("john", name);
}
```


</details>

---

## 7. Что делает orElseGet()?

<details> <summary>Ответ</summary>

Метод `orElseGet()` возвращает значение из `Optional` или вызывает `Supplier`, если `Optional` пустой.

**Пример**:
```java
@Test
public void whenOrElseGetWorks_thenCorrect() {
    String nullName = null;
    String name = Optional.ofNullable(nullName).orElseGet(() -> "john");
    assertEquals("john", name);
}
```


</details>

---

## 8. Разница между orElse() и orElseGet()

<details> <summary>Ответ</summary>

Хотя `orElse()` и `orElseGet()` кажутся похожими, их различие существенно влияет на производительность.

**Ключевое различие**:
- `orElse()` всегда вычисляет значение по умолчанию, даже если `Optional` содержит значение.
- `orElseGet()` вычисляет значение по умолчанию только если `Optional` пустой.

**Пример**:
```java
public String getMyDefault() {
    System.out.println("Getting Default Value");
    return "Default Value";
}

@Test
public void whenOrElseGetAndOrElseDiffer_thenCorrect() {
    String text = "Text present";
    
    System.out.println("Using orElseGet:");
    String defaultText = Optional.ofNullable(text).orElseGet(this::getMyDefault);
    assertEquals("Text present", defaultText);
    
    System.out.println("Using orElse:");
    defaultText = Optional.ofNullable(text).orElse(getMyDefault());
    assertEquals("Text present", defaultText);
}
```

**Вывод**:
```
Using orElseGet:
Using orElse:
Getting Default Value...
```

**Выводы**:
- `orElseGet()` эффективнее, если значение по умолчанию дорого вычислять (например, запрос к БД).
- Используйте `orElse()` для простых констант.


</details>

---

## 9. Исключения с помощью orElseThrow()

<details> <summary>Ответ</summary>

Метод `orElseThrow()` выбрасывает исключение, если `Optional` пустой, вместо возврата значения по умолчанию.

**Пример (до Java 10)**:
```java
String nullName = null;
String name = Optional.ofNullable(nullName).orElseThrow(IllegalArgumentException::new);
```

**Пример (Java 10+)**:
```java
String nullName = null;
String name = Optional.ofNullable(nullName).orElseThrow(); // NoSuchElementException
```


</details>

---

## 10. Возвращение значения с помощью get()

<details> <summary>Ответ</summary>

Метод `get()` возвращает значение из `Optional`, но выбрасывает `NoSuchElementException`, если `Optional` пустой.

**Пример**:
```java
@Test
public void givenOptional_whenGetsValue_thenCorrect() {
    Optional<String> opt = Optional.of("baeldung");
    String name = opt.get();
    assertEquals("baeldung", name);
}

@Test(expected = NoSuchElementException.class)
public void givenOptionalWithNull_whenGetThrowsException_thenCorrect() {
    Optional<String> opt = Optional.ofNullable(null);
    String name = opt.get();
}
```

**Недостаток**:
- Использование `get()` противоречит цели `Optional`, так как не поощряет явную обработку пустого случая.
- Рекомендуется использовать `orElse()`, `orElseGet()` или `orElseThrow()`.


</details>

---

## 11. Как работает метод filter() для Optional?

<details> <summary>Ответ</summary>

Метод `filter()` возвращает `Optional` с текущим значением, если оно удовлетворяет условию (`Predicate`), или пустой `Optional`, если условие не выполнено или `Optional` пустой.

**Пример**:
```java
personRepository.findById(id)
    .filter(person -> person.getAge() > 18);
```

**Использование**:
- Подходит для проверки условий на значение внутри `Optional`.


</details>

---

## 12. Как работает метод map() для Optional?

<details> <summary>Ответ</summary>

Метод `map()` применяет функцию (`Function`) к значению в `Optional` и возвращает новый `Optional` с результатом. Если `Optional` пустой, возвращается пустой `Optional`.

**Пример**:
```java
personRepository.findById(id)
    .map(person -> person.getFirstName() + " " + person.getLastName());
```

**Использование**:
- Для преобразования значения внутри `Optional` в другой тип.


</details>

---

## 13. Как работает метод flatMap() для Optional?

<details> <summary>Ответ</summary>

Метод `flatMap()` применяется, если функция (`Function`) возвращает `Optional`. Он "распаковывает" результат, избегая вложенных `Optional<Optional<T>>`.

**Пример**:
```java
Optional<String> optUserPhoneNumber = personRepository.findById(1L)
    .flatMap(person -> phoneNumberRepository.findByPersonId(person.getId()));
```

**Сравнение с map()**:
```java
// С map() — возвращает Optional<Optional<String>>
Optional<Optional<String>> nested = personRepository.findById(1L)
    .map(person -> phoneNumberRepository.findByPersonId(person.getId()));
```

**Использование**:
- Для работы с методами, возвращающими `Optional`, без вложенности.


</details>

---

## 14. Метод or()

<details> <summary>Ответ</summary>

С Java 11 метод `or()` возвращает текущий `Optional`, если он не пустой, или новый `Optional`, созданный `Supplier`.

**Пример**:
```java
personRepository.findById(id)
    .or(() -> Optional.of(new Person(-1L, "anon", "anon", "anon", 0L)));
```

**Особенности**:
- Не изменяет существующий `Optional`, а возвращает новый.
- Полезно для предоставления альтернативного `Optional`.


</details>

---

## 15. Комбинирование методов в Optional

<details> <summary>Ответ</summary>

Методы `Optional` возвращают новый `Optional`, что позволяет создавать цепочки, подобные Stream API.

**Пример**:
```java
final LocalDateTime now = LocalDateTime.now();
final DayOfWeek dayWeek = Optional.of(now)
    .map(LocalDateTime::getDayOfWeek)
    .filter(dayOfWeek -> DayOfWeek.SUNDAY.equals(dayOfWeek))
    .orElse(DayOfWeek.MONDAY);
```

**Использование**:
- Комбинируйте `map()`, `filter()`, `orElse()` и другие методы для обработки данных.


</details>

---

## 16. Когда стоит использовать Optional?

<details> <summary>Ответ</summary>

Согласно Javadoc, `Optional` предназначен для:
- Использования в качестве возвращаемого типа метода, когда результат может отсутствовать.
- Явного представления "отсутствия значения" вместо `null` для предотвращения ошибок.

**Пример**:
```java
Optional<User> findUserById(Long id) {
    return Optional.ofNullable(userRepository.findById(id));
}
```


</details>

---

## 17. Как НЕ стоит использовать Optional?

<details> <summary>Ответ</summary>

### 17.1. Как параметр метода

`Optional` в параметрах метода может привести к передаче `null` вместо `Optional.empty()`, что вызывает `NullPointerException`.

**Неправильно**:
```java
void process(Optional<String> param) { ... }
```

**Решение**:
- Используйте обычный тип и проверку на `null` или переработайте логику.

### 17.2. Как свойство класса

Использование `Optional` в полях класса нежелательно:
- **Проблемы с фреймворками**: Hibernate/Spring Data не поддерживают `Optional` без кастомных конвертеров.
- **Производительность**: `Optional` создаёт дополнительный объект, который может долго храниться.
- **Сериализация**: `Optional` не реализует `Serializable`, что усложняет сериализацию объекта.

**Решение**:
- Используйте `Optional` в геттерах, но это не поддерживается Lombok.

### 17.3. Как обёртка коллекции

Оборачивание коллекций в `Optional` избыточно, так как коллекции сами являются контейнерами.

**Неправильно**:
```java
Optional<List<String>> optionalList = Optional.ofNullable(list);
```

**Решение**:
- Используйте `Collections.emptyList()`, `Collections.emptySet()` и т.д.


</details>

---

## 18. Optional не должен равняться null

<details> <summary>Ответ</summary>

Присваивание `null` вместо `Optional` нарушает его цель. Всегда используйте `Optional.empty()` для пустого значения.

**Неправильно**:
```java
Optional<String> opt = null;
```

**Правильно**:
```java
Optional<String> opt = Optional.empty();
```


</details>

---

## 19. Примитивы и Optional

<details> <summary>Ответ</summary>

Для работы с примитивами существуют `OptionalInt`, `OptionalLong`, `OptionalDouble`. Они избегают автобоксинга, но имеют ограниченный функционал (нет `map()`, `filter()`).

**Пример**:
```java
OptionalInt opt = OptionalInt.of(42);
int value = opt.orElse(0);
```

**Особенности**:
- Используются редко из-за ограниченной функциональности.
- Доступны методы: `getAsInt()`, `orElse()`, `orElseGet()`, `orElseThrow()`, `ifPresent()`, `isPresent()`.
