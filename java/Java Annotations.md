
## 1. Что такое аннотации в Java?
<details> <summary>Ответ</summary>
Аннотации — это метатеги, добавляемые к элементам Java-кода (пакетам, классам, конструкторам, методам, полям, параметрам, локальным переменным). Они содержат дополнительную информацию, не изменяя логику программы, и выступают как модификаторы.

**Пример**:
```java
@Override
public String toString() {
    return "Example";
}
```
</details>

---

## 2. Плюсы использования аннотаций
<details> <summary>Ответ</summary>
- **Информация для компилятора**: Обнаружение ошибок и подавление предупреждений.
- **Читаемость кода**: Упрощают понимание кода для разработчиков.
- **Обработка на этапе компиляции/развертывания**: Генерация кода, XML и других файлов.
- **Обработка во время выполнения**: Проверка аннотаций для тестов или логики.
- **Уменьшение дублирования кода**.
- **Автоматизация бойлерплейт-кода**: Например, через Lombok.
- **Отлов ошибок на этапе компиляции**: Например, потенциальных `NullPointerException`.
- **Настройка поведения в рантайме**: На основе наличия аннотаций.
</details>

---

## 3. Какие функции выполняют аннотации?
<details> <summary>Ответ</summary>
- Предоставляют информацию компилятору.
- Используются инструментами для генерации кода или конфигураций.
- Применяются во время выполнения программы для дополнительной логики.

**Пример популярной аннотации**:
- `@Override` — указывает, что метод переопределяет метод суперкласса.
</details>

---

## 4. Какие встроенные аннотации в Java?
<details> <summary>Ответ</summary>
Java SE включает следующие встроенные аннотации:
- **Из пакета `java.lang.annotation`**:
    - `@Retention`
    - `@Documented`
    - `@Target`
    - `@Inherited`
- **Из пакета `java.lang`**:
    - `@Override`
    - `@Deprecated`
    - `@SafeVarargs`
    - `@SuppressWarnings`
    - `@FunctionalInterface` (с Java 8)

Эти аннотации широко используются для упрощения кода и снижения связанности.
</details>

---

## 5. Что делают аннотации @Retention, @Documented, @Target, @Inherited?
<details> <summary>Ответ</summary>
### 5.1. Аннотация @Retention

Указывает жизненный цикл аннотации. Принимает значение из перечисления `RetentionPolicy`:
- `RetentionPolicy.SOURCE`  Существует только в исходном коде, удаляется при компиляции (например, `@Override`). 
- `RetentionPolicy.CLASS`   Записывается в `.class`-файл, но не доступна в рантайме (редко используется).  
- `RetentionPolicy.RUNTIME`  Записывается в `.class`-файл и доступна через рефлексию во время выполнения. 

**Пример**:
```java
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {}
```

### 5.2. Аннотация @Documented

Маркер, указывающий, что аннотация должна включаться в Javadoc.

**Пример**:
```java
@Documented
public @interface MyAnnotation {}
```

### 5.3. Аннотация @Target

Ограничивает элементы, к которым применима аннотация, через `ElementType`:
- `PACKAGE`, `TYPE`, `CONSTRUCTOR`, `METHOD`, `FIELD`, `PARAMETER`, `LOCAL_VARIABLE`, и др.

**Пример**:
```java
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface MyAnnotation {}
```

### 5.4. Аннотация @Inherited

Маркер, позволяющий аннотации класса наследоваться подклассами.

**Пример**:
```java
@Inherited
public @interface MyAnnotation {}
```
</details>

---

## 6. Что делают аннотации @Override, @Deprecated, @SafeVarargs, @SuppressWarnings?
<details> <summary>Ответ</summary>
### 1. **`@Override`**
#### Назначение и семантика
- **Цель**: Указывает, что метод подкласса переопределяет метод суперкласса или реализует метод интерфейса. Это помогает избежать ошибок, таких как неправильная сигнатура метода.
- **Retention**: `RetentionPolicy.SOURCE` — аннотация видна только в исходном коде и удаляется компилятором при генерации байт-кода.

#### Обработка компилятором (`javac`)
- Компилятор проверяет, что метод, помеченный `@Override`, действительно переопределяет или реализует метод из суперкласса/интерфейса. Проверяются:
  - Существование метода с такой же сигнатурой (имя, параметры, возвращаемый тип) в родительском классе или интерфейсе.
  - Совместимость модификаторов доступа (например, нельзя сузить видимость метода).
  - Совместимость исключений в `throws` (переопределяющий метод не может бросать более широкие проверяемые исключения).
- Если проверка не проходит, компилятор выдаёт ошибку компиляции, например:
  ```java
  class Parent {
      void doWork() {}
  }
  class Child extends Parent {
      @Override
      void doWork(int x) {} // Ошибка: метод не переопределяет ничего
  }
  ```

#### В байт-коде
- Поскольку `@Override` имеет `RetentionPolicy.SOURCE`, она **не сохраняется** в `.class` файле. В байт-коде нет никаких следов этой аннотации, и JVM не взаимодействует с ней.
- Для проверки байт-кода можно использовать инструмент, такой как `javap`. Например:
  ```java
  class Example {
      @Override
      public String toString() { return "Example"; }
  }
  ```
  Вывод `javap -v Example` не покажет следов `@Override`, так как она удаляется компилятором.

#### Роль JVM
- **Никакая**. JVM не видит и не обрабатывает `@Override`, так как аннотация не присутствует в байт-коде. Её задача выполняется исключительно на этапе компиляции.

#### Низкоуровневые детали
- Аннотация обрабатывается в процессе синтаксического анализа и проверки типов компилятором. Это часть фазы семантического анализа в `javac`.
- Если метод помечен `@Override`, но не переопределяет ничего, компилятор использует свою внутреннюю модель наследования (на основе дерева разбора и таблицы символов), чтобы выявить несоответствие.


### 2. **`@Deprecated`**
#### Назначение и семантика
- **Цель**: Помечает элемент программы (класс, метод, поле) как устаревший, чтобы сообщить разработчикам, что его использование не рекомендуется.
- **Retention**: `RetentionPolicy.RUNTIME` — аннотация сохраняется в байт-коде и доступна через Reflection API.
- **Параметры (с Java 9)**:
  - `since`: строка, указывающая версию, с которой элемент считается устаревшим.
  - `forRemoval`: булево значение, указывающее, планируется ли удаление элемента в будущем.

#### Обработка компилятором (`javac`)
- При использовании элемента, помеченного `@Deprecated`, компилятор выдаёт предупреждение (`warning`), например:
  ```java
  @Deprecated(since="1.8", forRemoval=true)
  class OldClass {}
  class Test {
      void use() {
          new OldClass(); // Предупреждение: OldClass is deprecated
      }
  }
  ```
- Предупреждение можно подавить с помощью `@SuppressWarnings("deprecation")`.
- Компилятор добавляет метаданные `@Deprecated` в `.class` файл, если аннотация применяется к классу, методу или полю.

#### В байт-коде
- Аннотация `@Deprecated` сохраняется в `.class` файле в разделе `attributes`:
  - Для классов: в атрибуте `RuntimeVisibleAnnotations` или как специальный атрибут `Deprecated`.
  - Для методов/полей: в их соответствующих структурах в байт-коде.
- Формат хранения в байт-коде (на основе JVM Specification):
  - Атрибут `Deprecated` — это отдельный флаг в структуре `ClassFile`, `MethodInfo` или `FieldInfo`.
  - Если используются параметры (`since`, `forRemoval`), они хранятся в `RuntimeVisibleAnnotations` как аннотация с соответствующими значениями.
- Пример байт-кода (для метода):
  ```java
  @Deprecated(since="9")
  public void oldMethod() {}
  ```
  Вывод `javap -v`:
  ```
  # Method descriptor
  Deprecated: true
  RuntimeVisibleAnnotations:
    0: #14(name=java/lang/Deprecated, values={since="9"})
  ```

#### Роль JVM
- JVM хранит метаданные `@Deprecated` в памяти при загрузке класса через `ClassLoader`.
- JVM не выполняет никакой логики для `@Deprecated`, но предоставляет доступ к аннотации через Reflection API, например:
  ```java
  Method method = MyClass.class.getMethod("oldMethod");
  Deprecated deprecated = method.getAnnotation(Deprecated.class);
  if (deprecated != null) {
      System.out.println("Method is deprecated, since: " + deprecated.since());
  }
  ```
- Инструменты (например, IDE, линтеры, фреймворки) могут использовать эту информацию для анализа или логирования.

#### Низкоуровневые детали
- Атрибут `Deprecated` в байт-коде — это пустой атрибут (без данных), просто флаг, указывающий на устаревание.
- Параметры `since` и `forRemoval` хранятся как элементы аннотации в формате, описанном в JVM Specification (раздел 4.7.20).
- Фреймворки, такие как Spring, могут использовать Reflection для анализа `@Deprecated` и, например, предлагать альтернативные API.


### 3. **`@SafeVarargs`**
#### Назначение и семантика
- **Цель**: Подавляет предупреждения компилятора о небезопасных операциях с переменным числом аргументов (`varargs`) в методах или конструкторах.
- **Retention**: `RetentionPolicy.RUNTIME` — сохраняется в байт-коде и доступна через Reflection.
- **Ограничения**:
  - Применяется только к методам или конструкторам, которые являются `static`, `final` или `private`.
  - Используется для методов с параметрами `varargs` (например, `T... args`), чтобы указать, что они безопасны с точки зрения типобезопасности.

#### Обработка компилятором (`javac`)
- Без `@SafeVarargs` компилятор может выдавать предупреждение `unchecked` для методов `varargs`, если они работают с обобщёнными типами. Это связано с риском **heap pollution** (нарушение типобезопасности из-за массива `Object[]` в реализации `varargs`).
- `@SafeVarargs` сообщает компилятору, что разработчик гарантирует безопасность метода. Например:
  ```java
  @SafeVarargs
  static <T> void printAll(T... items) {
      for (T item : items) {
          System.out.println(item);
      }
  }
  ```
- Если аннотация применяется к неподходящему методу (например, не `static` и не `final`), компилятор выдаёт ошибку.

#### В байт-коде
- Аннотация хранится в разделе `RuntimeVisibleAnnotations` структуры `MethodInfo` в `.class` файле.
- Пример байт-кода:
  ```java
  @SafeVarargs
  static <T> void printAll(T... items) {}
  ```
  Вывод `javap -v`:
  ```
  # Method descriptor
  RuntimeVisibleAnnotations:
    0: #14(name=java/lang/SafeVarargs)
  ```
- В байт-коде `varargs` метод преобразуется в метод с параметром-массивом (например, `T[] items`), и `@SafeVarargs` подтверждает, что этот массив не будет использован небезопасно.

#### Роль JVM
- JVM не обрабатывает `@SafeVarargs` напрямую, а лишь хранит её метаданные в памяти.
- Аннотация доступна через Reflection, но редко используется в runtime, так как её основная задача — подавление предупреждений компилятора.
- Пример доступа через Reflection:
  ```java
  Method method = MyClass.class.getMethod("printAll", Object[].class);
  if (method.isAnnotationPresent(SafeVarargs.class)) {
      System.out.println("Method is safe for varargs");
  }
  ```

#### Низкоуровневые детали
- Проблема `varargs` связана с тем, что в JVM `T...` реализуется как массив `Object[]` для обобщённых типов, что может привести к `ClassCastException` при неправильном использовании.
- `@SafeVarargs` подтверждает, что метод не выполняет небезопасных операций, таких как присваивание массива `Object[]` переменной другого типа.
- Компилятор проверяет ограничения применения `@SafeVarargs` на основе модификаторов метода, используя свою модель анализа.


### 4. **`@SuppressWarnings`**
#### Назначение и семантика
- **Цель**: Подавляет предупреждения компилятора для указанных категорий (например, `unchecked`, `deprecation`, `rawtypes`).
- **Retention**: `RetentionPolicy.SOURCE` — аннотация видна только в исходном коде и удаляется компилятором.
- **Параметры**: Принимает массив строк, указывающих типы предупреждений (например, `@SuppressWarnings({"unchecked", "deprecation"})`).

#### Обработка компилятором (`javac`)
- Компилятор интерпретирует `@SuppressWarnings` во время анализа исходного кода и отключает указанные предупреждения для аннотированного элемента (класса, метода, поля и т.д.).
- Примеры категорий предупреждений:
  - `unchecked`: для операций с обобщениями без проверки типов.
  - `deprecation`: для использования устаревших API.
  - `rawtypes`: для использования raw-типов вместо обобщённых.
- Пример:
  ```java
  @SuppressWarnings("unchecked")
  static void rawList() {
      java.util.List list = new java.util.ArrayList(); // Без предупреждения
      list.add("test");
  }
  ```

#### В байт-коде
- Поскольку `@SuppressWarnings` имеет `RetentionPolicy.SOURCE`, она **не сохраняется** в `.class` файле. В байт-коде нет никаких следов этой аннотации.

#### Роль JVM
- **Никакая**. JVM не видит `@SuppressWarnings`, так как она удаляется компилятором.

#### Низкоуровневые детали
- Компилятор обрабатывает `@SuppressWarnings` на этапе семантического анализа, используя внутреннюю таблицу предупреждений.
- Список поддерживаемых категорий предупреждений зависит от реализации компилятора (например, `javac` от Oracle или OpenJDK). Неподдерживаемые категории игнорируются без ошибки.
</details>

---

## 8. Как аннотации встроены в язык Java?
<details> <summary>Ответ</summary>
Аннотации — это синтаксический сахар, основанный на интерфейсах. Они реализуются через специальный интерфейс, расширяющий `java.lang.annotation.Annotation`.

**Пример аннотации**:
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyAnnotation {
    String value() default "default";
    int priority() default 1;
}
```

**Внутренняя реализация**:
- Компилятор (`javac`) преобразует `@interface` в интерфейс, наследующий `Annotation`.
- Элементы аннотации (`value`, `priority`) становятся абстрактными методами.
- JVM создаёт прокси-класс для аннотации в рантайме (для `RUNTIME`), реализующий доступ к значениям через рефлексию.
</details>

---

## 9. Как аннотации хранятся в байт-коде?
<details> <summary>Ответ</summary>
Аннотации записываются в `.class`-файл как атрибуты байт-кода:
- `RuntimeVisibleAnnotations`: Для `RetentionPolicy.RUNTIME`.
- `RuntimeInvisibleAnnotations`: Для `RetentionPolicy.CLASS`.
- `RuntimeVisibleParameterAnnotations`/`RuntimeInvisibleParameterAnnotations`: Для аннотаций параметров.

**Пример структуры**:
```
RuntimeVisibleAnnotations:
  annotation: MyAnnotation
    element_value_pairs:
      value: "test"
      priority: 5
```

</details>

---

## 10. Как происходит компиляция аннотаций?
<details> <summary>Ответ</summary>
**Процесс**:
1. **Парсинг**: Компилятор распознаёт аннотации и проверяет их `@Target`.
2. **Проверка типов**: Убеждается, что значения элементов соответствуют их типам.
3. **Генерация байт-кода**: Добавляет аннотации в `.class`-файл (для `CLASS` и `RUNTIME`).
4. **Обработка аннотаций**: Использует процессоры аннотаций (APT) для `SOURCE` или `CLASS`.

**Пример процессора**:
```java
@SupportedAnnotationTypes("com.example.MyAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MyAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(MyAnnotation.class)) {
            processingEnv.getMessager().printMessage(
                Diagnostic.Kind.NOTE,
                "Found @MyAnnotation on " + element.getSimpleName()
            );
        }
        return true;
    }
}
```
</details>

---

## 11. Рефлексия и обработка аннотаций во время выполнения
<details> <summary>Ответ</summary>
Для аннотаций с `RetentionPolicy.RUNTIME` JVM загружает их в память, а API рефлексии (`java.lang.reflect`) позволяет их читать.

**Пример**:
```java
Method method = MyClass.class.getMethod("myMethod");
if (method.isAnnotationPresent(MyAnnotation.class)) {
    MyAnnotation ann = method.getAnnotation(MyAnnotation.class);
    System.out.println(ann.value());
}
```

**Внутренний процесс**:
- JVM парсит `RuntimeVisibleAnnotations` из байт-кода.
- Создаёт прокси-объект через `java.lang.reflect.Proxy`.
- Методы аннотации возвращают значения через `InvocationHandler`.
</details>

---

## 12. Как работают повторяемые аннотации (Java 8+)?
<details> <summary>Ответ</summary>
Повторяемые аннотации оборачиваются в контейнерную аннотацию, которая хранит массив повторяемых аннотаций.

**Пример**:
```java
@Repeatable(MyAnnotations.class)
public @interface MyAnnotation {
    String value();
}

public @interface MyAnnotations {
    MyAnnotation[] value();
}

@MyAnnotation("first")
@MyAnnotation("second")
public void myMethod() {}
```

**В байт-коде**:
```
RuntimeVisibleAnnotations:
  annotation: MyAnnotations
    element_value_pairs:
      value: [{value: "first"}, {value: "second"}]
```
</details>

---

## 16. Как создать свою аннотацию?
<details> <summary>Ответ</summary>
Создание аннотации использует ключевое слово `@interface`.

**Пример**:
```java
public @interface About {
    String info() default "";
}
```

**Особенности**:
- `@interface` заменяет `class` или `interface`.
- Элементы аннотации — это методы с возвращаемыми значениями.
- `default` задаёт значение по умолчанию.
</details>

---

## 17. Атрибуты каких типов допустимы в аннотациях?
<details> <summary>Ответ</summary>
Допустимые типы атрибутов:
- Примитивы (`int`, `boolean`, и т.д.)
- `String`
- `Class` (или его параметризованные формы)
- Перечисления (`enum`)
- Аннотации
- Одномерные массивы вышеуказанных типов

**Пример**:
```java
public @interface MyAnnotation {

  String name();

  int[] priorities();

  Class<?> type();
}
```
</details>
