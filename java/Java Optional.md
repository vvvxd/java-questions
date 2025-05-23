## Оглавление

1. [Цель класса Optional](#1-цель-класса-optional)
2. [Как создать Optional?](#2-как-создать-optional)
    - [Пустой Optional](#21-пустой-optional)
    - [Optional с помощью of()](#22-optional-с-помощью-of)
    - [Optional с помощью ofNullable()](#23-optional-с-помощью-ofnullable)
3. [Как проверить наличие значения в Optional?](#3-как-проверить-наличие-значения-в-optional)
    - [Метод isPresent()](#31-метод-ispresent)
    - [Метод isEmpty()](#32-метод-isempty)
4. [Что делает ifPresent()?](#4-что-делает-ifpresent)
5. [Что делает ifPresentOrElse()?](#5-что-делает-ifpresentorelse)
6. [Что делает orElse()?](#6-что-делает-orelse)
7. [Что делает orElseGet()?](#7-что-делает-orelseget)
8. [Разница между orElse() и orElseGet()](#8-разница-между-orelse-и-orelseget)
9. [Исключения с помощью orElseThrow()](#9-исключения-с-помощью-orselsethrow)
10. [Возвращение значения с помощью get()](#10-возвращение-значения-с-помощью-get)
11. [Как работает метод filter() для Optional?](#11-как-работает-метод-filter-для-optional)
12. [Как работает метод map() для Optional?](#12-как-работает-метод-map-для-optional)
13. [Как работает метод flatMap() для Optional?](#13-как-работает-метод-flatmap-для-optional)
14. [Метод or()](#14-метод-or)
15. [Комбинирование методов в Optional](#15-комбинирование-методов-в-optional)
16. [Когда стоит использовать Optional?](#16-когда-стоит-использовать-optional)
17. [Как НЕ стоит использовать Optional?](#17-как-не-стоит-использовать-optional)
- [Как параметр метода](#171-как-параметр-метода)
- [Как свойство класса](#172-как-свойство-класса)
- [Как обёртка коллекции](#173-как-обёртка-коллекции)
18. [Optional не должен равняться null](#18-optional-не-должен-равняться-null)
19. [Примитивы и Optional](#19-примитивы-и-optional)

---

## 1. Цель класса Optional

Класс `Optional` в Java предназначен для представления опциональных значений вместо использования `null`-ссылок. Это контейнер, который может содержать объект типа `T` или быть пустым, помогая избежать `NullPointerException` и улучшая читаемость кода.

**Описание**:
- `Optional` — это обёртка, которая явно указывает на возможность отсутствия значения.
- Способствует функциональному программированию и явной обработке случаев, когда значение может быть `null`.

[Назад к оглавлению](#оглавление)

---

## 2. Как создать Optional?

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

[Назад к оглавлению](#оглавление)

---

## 3. Как проверить наличие значения в Optional?

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

[Назад к оглавлению](#оглавление)

---

## 4. Что делает ifPresent()?

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

[Назад к оглавлению](#оглавление)

---

## 5. Что делает ifPresentOrElse()?

Метод `ifPresentOrElse()` выполняет одно действие, если значение присутствует, и другое, если `Optional` пустой. Принимает два параметра: `Consumer` для значения и `Runnable` для пустого случая.

**Пример**:
```java
personRepository.findById(id)
    .ifPresentOrElse(
        person -> System.out.println(person.getFirstName() + " " + person.getLastName()),
        () -> System.out.println("Иван Иванов")
    );
```

[Назад к оглавлению](#оглавление)

---

## 6. Что делает orElse()?

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

[Назад к оглавлению](#оглавление)

---

## 7. Что делает orElseGet()?

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

[Назад к оглавлению](#оглавление)

---

## 8. Разница между orElse() и orElseGet()

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

[Назад к оглавлению](#оглавление)

---

## 9. Исключения с помощью orElseThrow()

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

[Назад к оглавлению](#оглавление)

---

## 10. Возвращение значения с помощью get()

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

[Назад к оглавлению](#оглавление)

---

## 11. Как работает метод filter() для Optional?

Метод `filter()` возвращает `Optional` с текущим значением, если оно удовлетворяет условию (`Predicate`), или пустой `Optional`, если условие не выполнено или `Optional` пустой.

**Пример**:
```java
personRepository.findById(id)
    .filter(person -> person.getAge() > 18);
```

**Использование**:
- Подходит для проверки условий на значение внутри `Optional`.

[Назад к оглавлению](#оглавление)

---

## 12. Как работает метод map() для Optional?

Метод `map()` применяет функцию (`Function`) к значению в `Optional` и возвращает новый `Optional` с результатом. Если `Optional` пустой, возвращается пустой `Optional`.

**Пример**:
```java
personRepository.findById(id)
    .map(person -> person.getFirstName() + " " + person.getLastName());
```

**Использование**:
- Для преобразования значения внутри `Optional` в другой тип.

[Назад к оглавлению](#оглавление)

---

## 13. Как работает метод flatMap() для Optional?

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

[Назад к оглавлению](#оглавление)

---

## 14. Метод or()

С Java 11 метод `or()` возвращает текущий `Optional`, если он не пустой, или новый `Optional`, созданный `Supplier`.

**Пример**:
```java
personRepository.findById(id)
    .or(() -> Optional.of(new Person(-1L, "anon", "anon", "anon", 0L)));
```

**Особенности**:
- Не изменяет существующий `Optional`, а возвращает новый.
- Полезно для предоставления альтернативного `Optional`.

[Назад к оглавлению](#оглавление)

---

## 15. Комбинирование методов в Optional

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

[Назад к оглавлению](#оглавление)

---

## 16. Когда стоит использовать Optional?

Согласно Javadoc, `Optional` предназначен для:
- Использования в качестве возвращаемого типа метода, когда результат может отсутствовать.
- Явного представления "отсутствия значения" вместо `null` для предотвращения ошибок.

**Пример**:
```java
Optional<User> findUserById(Long id) {
    return Optional.ofNullable(userRepository.findById(id));
}
```

[Назад к оглавлению](#оглавление)

---

## 17. Как НЕ стоит использовать Optional?

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

[Назад к оглавлению](#оглавление)

---

## 18. Optional не должен равняться null

Присваивание `null` вместо `Optional` нарушает его цель. Всегда используйте `Optional.empty()` для пустого значения.

**Неправильно**:
```java
Optional<String> opt = null;
```

**Правильно**:
```java
Optional<String> opt = Optional.empty();
```

[Назад к оглавлению](#оглавление)

---

## 19. Примитивы и Optional

Для работы с примитивами существуют `OptionalInt`, `OptionalLong`, `OptionalDouble`. Они избегают автобоксинга, но имеют ограниченный функционал (нет `map()`, `filter()`).

**Пример**:
```java
OptionalInt opt = OptionalInt.of(42);
int value = opt.orElse(0);
```

**Особенности**:
- Используются редко из-за ограниченной функциональности.
- Доступны методы: `getAsInt()`, `orElse()`, `orElseGet()`, `orElseThrow()`, `ifPresent()`, `isPresent()`.

[Назад к оглавлению](#оглавление)