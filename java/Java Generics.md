## Оглавление

1. [Что такое обобщения (Generics)?](#1-что-такое-обобщения-generics)
2. [Как реализованы обобщения в Java?](#2-как-реализованы-обобщения-в-java)
3. [Что такое Type Erasure?](#3-что-такое-type-erasure)
4. [Как работает Type Erasure?](#4-как-работает-type-erasure)
5. [Почему используется Type Erasure?](#5-почему-используется-type-erasure)
6. [Какие последствия Type Erasure?](#6-какие-последствия-type-erasure)
7. [Обходные пути и особенности Type Erasure](#7-обходные-пути-и-особенности-type-erasure)
8. [Принцип подстановки Барбары Лисков и его связь с обобщениями](#8-принцип-подстановки-барбары-лисков-и-его-связь-с-обобщениями)
9. [Ковариантность, контравариантность и инвариантность](#9-ковариантность-контравариантность-и-инвариантность)
10. [Расскажите про ковариантность](#10-расскажите-про-ковариантность)
11. [Расскажите про контравариантность](#11-расскажите-про-контравариантность)
12. [Почему массивы в Java ковариантны?](#12-почему-массивы-в-java-ковариантны)
13. [Почему обобщения инвариантны?](#13-почему-обобщения-инвариантны)
14. [Что произойдёт, если опустить универсальный тип при создании объекта?](#14-что-произойдёт-если-опустить-универсальный-тип-при-создании-объекта)
15. [Чем универсальный метод отличается от универсального типа?](#15-чем-универсальный-метод-отличается-от-универсального-типа)
16. [Что такое Wildcard в Java и зачем они нужны?](#16-что-такое-wildcard-в-java-и-зачем-они-нужны)
17. [Какие виды Wildcards существуют в Java?](#17-какие-виды-wildcards-существуют-в-java)
18. [Как работает Unbounded Wildcard (`?`) и в каких случаях его использовать?](#18-как-работает-unbounded-wildcard--и-в-каких-случаях-его-использовать)
19. [Что такое Upper Bounded Wildcard (`? extends T`) и как его использовать?](#19-что-такое-upper-bounded-wildcard-extends-t-и-как-его-использовать)
20. [Что такое Lower Bounded Wildcard (`? super T`) и как его использовать?](#20-что-такое-lower-bounded-wildcard-super-t-и-как-его-использовать)
21. [В чём разница между `List<Object>` и `List<?>`?](#21-в-чём-разница-между-listobject-и-list)
22. [Что такое принцип PECS (Producer Extends, Consumer Super)?](#22-что-такое-принцип-pecs-producer-extends-consumer-super)
23. [Можно ли использовать Wildcard в объявлении классов или полей?](#23-можно-ли-использовать-wildcard-в-объявлении-классов-или-полей)
24. [Как Wildcards взаимодействуют с иерархией типов?](#24-как-wildcards-взаимодействуют-с-иерархией-типов)
25. [Какие ограничения есть у Wildcards?](#25-какие-ограничения-есть-у-wildcards)
26. [Как Wildcards взаимодействуют с Raw Types?](#26-как-wildcards-взаимодействуют-с-raw-types)
27. [Почему в примере возникает ошибка компиляции с `List<? extends Number>`?](#27-почему-в-примере-возникает-ошибка-компиляции-с-list-extends-number)
28. [Почему нельзя получить элемент из `List<? super T>`?](#28-почему-нельзя-получить-элемент-из-list-super-t)
29. [Что такое Wildcard Capture?](#29-что-такое-wildcard-capture)
30. [Практики использования Wildcards](#30-практики-использования-wildcards)
31. [Что такое переменные типа?](#31-что-такое-переменные-типа)
32. [Multiple Bounds (множественные ограничения)](#32-multiple-bounds-множественные-ограничения)
33. [Почему возникает ошибка компиляции в примере с `Comparable<Name>`?](#33-почему-возникает-ошибка-компиляции-в-примере-с-comparablename)
34. [Что такое Reifiable типы в Java?](#34-что-такое-reifiable-типы-в-java)
35. [Почему нельзя создать параметризованный `Exception`?](#35-почему-нельзя-создать-параметризованный-exception)
36. [Что такое Unchecked Warnings?](#36-что-такое-unchecked-warnings)
37. [Что такое Heap Pollution и почему оно происходит?](#37-что-такое-heap-pollution-и-почему-оно-происходит)
38. [Какие последствия Heap Pollution?](#38-какие-последствия-heap-pollution)
39. [Как связаны Reflection и Generics?](#39-как-связаны-reflection-и-generics)
40. [Можно ли передать `List<String>` методу, который принимает `List<Object>`?](#40-можно-ли-передать-liststring-методу-который-принимает-listobject)
41. [Можно ли использовать Generics с массивами?](#41-можно-ли-использовать-generics-с-массивами)
42. [Как подавить непроверенные предупреждения в Java?](#42-как-подавить-непроверенные-предупреждения-в-java)
43. [Что такое Type Inference и как он работает в Java?](#43-что-такое-type-inference-и-как-он-работает-в-java)
44. [Где в Java используется Type Inference?](#44-где-в-java-используется-type-inference)

---

## 1. Что такое обобщения (Generics)?

Обобщения (Generics) в программировании — это механизм, позволяющий создавать классы, интерфейсы и методы, которые работают с типами данных, задаваемыми в качестве параметров. Это обеспечивает типобезопасность, повторное использование кода и уменьшение дублирования. В Java обобщения были введены в версии 1.5 и позволяют указывать, с какими типами данных будет работать класс или метод, избегая приведения типов и потенциальных ошибок во время выполнения.

**Основные преимущества generics:**
- **Типобезопасность**: Компилятор проверяет корректность типов на этапе компиляции.
- **Устранение приведения типов**: Нет необходимости использовать явное приведение типов (например, `(String) obj`).
- **Повторное использование кода**: Один класс или метод может работать с разными типами данных.

Обобщения задаются с использованием угловых скобок `<T>`, где `T` — это параметр типа. Например:

```java
class Box<T> {
    private T item;
    public void setItem(T item) { this.item = item; }
    public T getItem() { return item; }
}
```

Здесь `T` — это обобщённый тип, который будет заменён конкретным типом (например, `String`, `Integer`) при создании экземпляра класса:

```java
Box<String> stringBox = new Box<>();
stringBox.setItem("Hello");
String value = stringBox.getItem(); // Не требуется приведение типов
```

---

## 2. Как реализованы обобщения в Java?

В Java обобщения реализованы с помощью механизма, называемого **type erasure** (стирание типов). Это означает, что информация о типах, указанных в обобщениях, удаляется во время компиляции, и в байт-коде используется "сырой" тип (raw type) или `Object`. Это сделано для обеспечения обратной совместимости с кодом, написанным до введения обобщений.

---

## 3. Что такое Type Erasure?

Type Erasure — это процесс, при котором компилятор Java удаляет всю информацию об обобщённых типах во время компиляции, заменяя их либо на их верхнюю границу (bounded type), либо на `Object`, если границы не указаны. В результате в байт-коде (коде, выполняемом JVM) нет информации о конкретных типах, использованных в обобщениях. Это означает, что в runtime обобщённые типы недоступны, и JVM работает с "сырыми" типами или их границами.

---

## 4. Как работает Type Erasure?

1. **Замена обобщённых типов**:
    - Если обобщённый тип не имеет ограничений (например, `<T>`), он заменяется на `Object`.
      ```java
      class Box<T> {
          private T item;
          public void setItem(T item) { this.item = item; }
          public T getItem() { return item; }
      }
      ```
      После компиляции с type erasure:
      ```java
      class Box {
          private Object item;
          public void setItem(Object item) { this.item = item; }
          public Object getItem() { return item; }
      }
      ```

    - Если указана верхняя граница (bounded type), например `<T extends Number>`, то `T` заменяется на эту границу (`Number`).
      ```java
      class NumericBox<T extends Number> {
          private T item;
          public NumericBox(T item) { this.item = item; }
      }
      ```
      После компиляции:
      ```java
      class NumericBox {
          private Number item;
          public NumericBox(Number item) { this.item = item; }
      }
      ```

2. **Удаление информации об обобщённых типах**:
    - В байт-коде не сохраняется информация о том, какой конкретный тип (например, `String`, `Integer`) использовался. Например:
      ```java
      Box<String> stringBox = new Box<>();
      Box<Integer> intBox = new Box<>();
      ```
      В runtime оба объекта будут просто `Box` (raw type), и JVM не сможет различить `Box<String>` или `Box<Integer>`.

3. **Добавление приведения типов (type casts)**:
    - Компилятор вставляет неявные приведения типов для сохранения типобезопасности. Например:
      ```java
      Box<String> box = new Box<>();
      box.setItem("Hello");
      String value = box.getItem();
      ```
      После компиляции:
      ```java
      Box box = new Box();
      box.setItem("Hello");
      String value = (String) box.getItem(); // Компилятор добавляет приведение
      ```

4. **Обработка wildcards**:
    - Wildcards (`<? extends T>`, `<? super T>`, `<?>`) также стираются. Например:
      ```java
      List<? extends Number> list;
      ```
      После стирания становится просто `List`, с добавлением проверок и привидений типов.

5. **Мостовые методы (bridge methods)**:
    - Компилятор создаёт синтетические мостовые методы для корректного наследования обобщённых классов. Например:
      ```java
      class Node<T> {
          public T data;
          public void setData(T data) { this.data = data; }
      }
      class IntegerNode extends Node<Integer> {
          public void setData(Integer data) { this.data = data; }
      }
      ```
      После type erasure:
      ```java
      class IntegerNode extends Node {
          public Integer data;
          public void setData(Integer data) { this.data = data; }
          // Мостовой метод
          public void setData(Object data) { setData((Integer) data); }
      }
      ```

---

## 5. Почему используется Type Erasure?

Type Erasure был выбран по следующим причинам:

1. **Обратная совместимость**:
    - До Java 1.5 коллекции (например, `ArrayList`) использовали raw types. Type erasure позволило новым обобщённым классам быть совместимыми с legacy-кодом.

2. **Упрощение JVM**:
    - JVM не пришлось модифицировать, так как типы обрабатываются на этапе компиляции, сохраняя производительность.

3. **Уменьшение размера байт-кода**:
    - Отсутствие информации о типах в runtime снижает объём байт-кода.

---

## 6. Какие последствия Type Erasure?

1. **Отсутствие информации о типах в runtime**:
    - Нельзя проверить, является ли объект обобщённым типом:
      ```java
      if (list instanceof List<String>) { ... } // Ошибка компиляции
      ```

2. **Ограничения на создание объектов обобщённых типов**:
    - Нельзя создавать экземпляры `T`:
      ```java
      class Box<T> {
          T create() { return new T(); } // Ошибка компиляции
      }
      ```

3. **Ограничения на массивы обобщённых типов**:
    - Нельзя создать массив `T[]`:
      ```java
      T[] array = new T[10]; // Ошибка компиляции
      ```

4. **Ограничения на статические члены**:
    - `T` нельзя использовать в статических полях или методах:
      ```java
      class Box<T> {
          static T item; // Ошибка компиляции
      }
      ```

5. **Проблемы с перегрузкой методов**:
    - Конфликт сигнатур после стирания:
      ```java
      void process(List<String> list) { ... }
      void process(List<Integer> list) { ... } // Ошибка компиляции
      ```

6. **Heap Pollution**:
    - Неправильное использование raw types может привести к ошибкам:
      ```java
      List rawList = new ArrayList();
      rawList.add("Hello");
      List<Integer> intList = rawList; // Предупреждение
      Integer i = intList.get(0); // ClassCastException
      ```

---

## 7. Обходные пути и особенности Type Erasure

1. **Type Tokens**:
    - Передача `Class<T>` для сохранения информации о типе:
      ```java
      class Box<T> {
          private Class<T> type;
          public Box(Class<T> type) { this.type = type; }
          public T create() throws Exception {
              return type.getDeclaredConstructor().newInstance();
          }
      }
      Box<String> box = new Box<>(String.class);
      ```

2. **Рефлексия**:
    - Получение информации о типах через `ParameterizedType`:
      ```java
      Field field = MyClass.class.getDeclaredField("list");
      Type type = field.getGenericType();
      ```

3. **Raw Types**:
    - Используются для совместимости, но не рекомендуются.

4. **Bounded Wildcards**:
    - Использование `<? extends T>` и `<? super T>` для управления типами.

---

## 8. Принцип подстановки Барбары Лисков и его связь с обобщениями

Принцип подстановки Лисков гласит: если `S` — подтип `T`, то объекты типа `T` могут быть заменены объектами типа `S` без изменения свойств программы. Для обобщений это означает, что подтипы должны сохранять корректность типизации.

**Примеры отношений тип/подтип**:
- `Number` → `Integer`
- `List<E>` → `ArrayList<E>`
- `Collection<E>` → `List<E>`
- `Iterable<E>` → `Collection<E>`

**Пример**:
```java
Number n = Integer.valueOf(42);
List<Number> aList = new ArrayList<>();
Collection<Number> aCollection = aList;
Iterable<Number> iterable = aCollection;
```

Обобщения используют этот принцип через wildcards для управления ковариантностью и контравариантностью.

---

## 9. Ковариантность, контравариантность и инвариантность

- **Ковариантность**: Сохраняет иерархию наследования. Если `Cat` — подтип `Animal`, то `Set<Cat>` — подтип `Set<Animal>`.
  ```java
  Set<Animal> animals = new HashSet<Cat>();
  ```

- **Контравариантность**: Обращает иерархию. `Set<Animal>` — подтип `Set<Cat>`.
  ```java
  Set<Cat> cats = new HashSet<Animal>();
  ```

- **Инвариантность**: Отсутствие наследования между производными типами. `Set<Cat>` и `Set<Animal>` не связаны.

---

## 10. Расскажите про ковариантность

Тип ковариантен, если подтип базового типа можно подставить вместо супертипа (`S` — подтип `T`, тогда `Type<S>` — подтип `Type<T>`).

**В Java**:
- Достигается через `<? extends T>` для чтения данных (производители).
- Запись запрещена для сохранения типобезопасности.

**Пример**:
```java
List<? extends Number> numbers = new ArrayList<Integer>();
Number n = numbers.get(0); // OK
// numbers.add(42); // Ошибка
```

**Ковариантность массивов**:
- Массивы ковариантны, но это может привести к `ArrayStoreException`:
  ```java
  String[] strings = new String[10];
  Object[] objects = strings;
  objects[0] = 42; // ArrayStoreException
  ```

**Применение**:
- Чтение данных из коллекций с общим супертипом:
  ```java
  void printNumbers(List<? extends Number> numbers) {
      for (Number n : numbers) {
          System.out.println(n);
      }
  }
  ```

---

## 11. Расскажите про контравариантность

Тип контравариантен, если супертип можно подставить вместо подтипа (`S` — подтип `T`, тогда `Type<T>` — подтип `Type<S>`).

**В Java**:
- Достигается через `<? super T>` для записи данных (потребители).
- Чтение ограничено типом `Object`.

**Пример**:
```java
List<? super Integer> integers = new ArrayList<Number>();
integers.add(42); // OK
Object o = integers.get(0); // OK
// Integer n = integers.get(0); // Ошибка
```

**Применение**:
- Добавление элементов в коллекции:
  ```java
  void addIntegers(List<? super Integer> list) {
      list.add(42);
      list.add(100);
  }
  ```

---

## 12. Почему массивы в Java ковариантны?

Массивы ковариантны: если `S` — подтип `T`, то `S[]` — подтип `T[]`.

**Пример**:
```java
String[] strings = new String[] {"a", "b", "c"};
Object[] arr = strings;
arr[0] = 42; // ArrayStoreException
```

**Почему?**
- Ковариантность массивов позволяет присваивать подтипы, но требует проверки типов в runtime, что может привести к ошибкам. Обобщения сделали инвариантными для устранения таких проблем.

---

## 13. Почему обобщения инвариантны?

Обобщённые типы инвариантны: `Type<A>` не является подтипом или супертипом `Type<B>`, даже если `A` и `B` связаны наследованием.

**Пример**:
```java
List<String> strings = new ArrayList<>();
// List<Object> objects = strings; // Ошибка
```

**Почему?**
- Инвариантность предотвращает ошибки типобезопасности, например, добавление `Integer` в `List<String>` через `List<Object>`.
- Wildcards (`<? extends T>`, `<? super T>`) используются для ковариантности и контравариантности.

---

## 14. Что произойдёт, если опустить универсальный тип при создании объекта?

Если опустить тип, используется raw type, и код компилируется, но компилятор выдаёт предупреждение:

```java
List list = new ArrayList(); // Raw type
```

**Последствия**:
- Теряется типобезопасность.
- Требуется приведение типов.
- Это плохая практика, допустимая только для совместимости.

---

## 15. Чем универсальный метод отличается от универсального типа?

Универсальный метод вводит параметр типа в рамках метода, а универсальный тип — в рамках класса.

**Пример метода**:
```java
public static <T> T returnType(T argument) { return argument; }
```

- Тип `T` определяется при вызове метода.
- Вывод типа позволяет вызывать без указания `T`.

**Пример типа**:
```java
class Box<T> {
    private T item;
}
```

- Тип `T` фиксируется при создании экземпляра класса.

---

## 16. Что такое Wildcard в Java и зачем они нужны?

Wildcard (`?`) — это синтаксис обобщений, обозначающий неизвестный тип или группу типов. Они увеличивают гибкость кода, сохраняя типобезопасность, и упрощают API.

**Пример**:
```java
List<?> list = new ArrayList<String>();
```

---

## 17. Какие виды Wildcards существуют в Java?

1. **Unbounded Wildcard (`?`)**: Любой тип.
   ```java
   List<?> list = new ArrayList<String>();
   ```

2. **Upper Bounded Wildcard (`? extends T`)**: Подтипы `T`.
   ```java
   List<? extends Number> list = new ArrayList<Integer>();
   ```

3. **Lower Bounded Wildcard (`? super T`)**: Супертипы `T`.
   ```java
   List<? super Integer> list = new ArrayList<Number>();
   ```

---

## 18. Как работает Unbounded Wildcard (`?`) и в каких случаях его использовать?

`?` обозначает любой тип, но добавление элементов (кроме `null`) запрещено.

**Пример**:
```java
public void printList(List<?> list) {
    for (Object obj : list) {
        System.out.println(obj);
    }
}
```

**Когда использовать**:
- Для чтения данных, когда тип не важен.
- Для универсальных методов.

---

## 19. Что такое Upper Bounded Wildcard (`? extends T`) и как его использовать?

Ограничивает типы подтипами `T`. Подходит для чтения данных.

**Пример**:
```java
public void sum(List<? extends Number> numbers) {
    double total = 0;
    for (Number num : numbers) {
        total += num.doubleValue();
    }
    System.out.println("Sum: " + total);
}
```

**Ограничение**:
- Нельзя добавлять элементы (кроме `null`).

---

## 20. Что такое Lower Bounded Wildcard (`? super T`) и как его использовать?

Ограничивает типы супертипами `T`. Подходит для записи данных.

**Пример**:
```java
public void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
}
```

**Ограничение**:
- Чтение возвращает `Object`.

---

## 21. В чём разница между `List<Object>` и `List<?>`?

- **`List<Object>`**: Конкретный тип, принимает любые объекты, но не совместим с `List<String>`.
  ```java
  List<Object> objList = new ArrayList<>();
  objList.add("Hello");
  // objList = new ArrayList<String>(); // Ошибка
  ```

- **`List<?>`**: Wildcard, принимает `List` любого типа, но не позволяет добавлять элементы (кроме `null`).
  ```java
  List<?> wildList = new ArrayList<String>();
  wildList = new ArrayList<Integer>();
  ```

---

## 22. Что такое принцип PECS (Producer Extends, Consumer Super)?

PECS: **Producer Extends, Consumer Super**.

- **Producer (`? extends T`)**: Для чтения данных.
  ```java
  List<? extends Number> producer = new ArrayList<Integer>();
  Number num = producer.get(0);
  ```

- **Consumer (`? super T`)**: Для записи данных.
  ```java
  List<? super Integer> consumer = new ArrayList<Number>();
  consumer.add(42);
  ```

**Пример**:
```java
public void copy(List<? extends Number> source, List<? super Number> destination) {
    for (Number number : source) {
        destination.add(number);
    }
}
```

---

## 23. Можно ли использовать Wildcard в объявлении классов или полей?

Wildcards нельзя использовать в объявлении классов или полей:

```java
class MyClass<? extends Number> { } // Ошибка
List<? extends Number> numbers; // Ошибка
```

**Решение**:
- Использовать параметризованные типы:
  ```java
  class MyClass<T extends Number> {
      List<T> numbers;
  }
  ```

---

## 24. Как Wildcards взаимодействуют с иерархией типов?

Wildcards учитывают иерархию типов:
- `? extends Number`: Принимает `List<Integer>`, `List<Double>`.
- `? super Integer`: Принимает `List<Integer>`, `List<Number>`, `List<Object>`.

**Пример**:
```java
List<? extends Number> numbers = new ArrayList<Integer>();
List<? super Integer> integers = new ArrayList<Number>();
```

---

## 25. Какие ограничения есть у Wildcards?

1. **Ограничения на запись**:
    - `List<?>` и `List<? extends T>`: Нельзя добавлять (кроме `null`).
    - `List<? super T>`: Чтение ограничено `Object`.

2. **Нельзя использовать в классах/полях**.
3. **Сложность в лямбда-выражениях**.
4. **Усложнение читаемости кода**.

---

## 26. Как Wildcards взаимодействуют с Raw Types?

Raw types (`List`) игнорируют обобщения, что может привести к предупреждениям:

```java
List rawList = new ArrayList();
List<?> wildcardList = rawList; // Предупреждение
```

**Рекомендация**:
- Избегать raw types для сохранения типобезопасности.

---

## 27. Почему в примере возникает ошибка компиляции с `List<? extends Number>`?

```java
List<Integer> ints = new ArrayList<Integer>();
ints.add(1);
ints.add(2);
List<? extends Number> nums = ints;
nums.add(3.14); // Ошибка
```

`? extends Number` позволяет только чтение. Добавление запрещено, так как тип неизвестен. Можно добавить только `null`.

---

## 28. Почему нельзя получить элемент из `List<? super T>`?

```java
public static <T> T getFirst(List<? super T> list) {
    return list.get(0); // Ошибка
}
```

Чтение из `List<? super T>` возвращает `Object`, так как тип может быть любым супертипом `T`. Решение:

```java
public static <T> Object getFirst(List<? super T> list) {
    return list.get(0);
}
```

---

## 29. Что такое Wildcard Capture?

Wildcard Capture — это механизм, позволяющий "захватить" неизвестный тип `?` и присвоить ему временный тип `T` для выполнения операций.

**Пример с ошибкой**:
```java
public static void reverse(List<?> list) {
    List<Object> tmp = new ArrayList<Object>(list);
    for (int i = 0; i < list.size(); i++) {
        list.set(i, tmp.get(list.size()-i-1)); // Ошибка
    }
}
```

**Решение с Wildcard Capture**:
```java
public static void reverse(List<?> list) { rev(list); }

private static <T> void rev(List<T> list) {
    List<T> tmp = new ArrayList<T>(list);
    for (int i = 0; i < list.size(); i++) {
        list.set(i, tmp.get(list.size()-i-1));
    }
}
```

---

## 30. Практики использования Wildcards

- Используйте `? extends T` для чтения.
- Используйте `? super T` для записи.
- Избегайте wildcards, если нужны обе операции.
- Не используйте raw types, предпочитайте `?`.

---

## 31. Что такое переменные типа?

Переменные типа (`<T>`, `<E>`) — это идентификаторы, используемые в качестве типов в классах или методах. Могут быть ограничены сверху (`extends`).

**Пример**:
```java
public static <T extends Comparable<T>> T max(Collection<T> coll) {
    T candidate = coll.iterator().next();
    for (T elt : coll) {
        if (candidate.compareTo(elt) < 0) candidate = elt;
    }
    return candidate;
}
```

---

## 32. Multiple Bounds (множественные ограничения)

Ограничивают переменную типа несколькими типами через `&`. Первое ограничение используется для type erasure.

**Пример**:
```java
<T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll)
```

---

## 33. Почему возникает ошибка компиляции в примере с `Comparable<Name>`?

```java
public class Name implements Comparable<Name> {
    private final String value;
    public Name(String value) { this.value = value; }
    @Override
    public String toString() { return "value='" + value + "'"; }
    @Override
    public int compareTo(Name o) { return value.compareTo(o.value); }
    public int compareTo(Object o) { return value.compareTo(((Name)o).value); }
}
```

Ошибка возникает, так как type erasure добавляет мостовой метод `compareTo(Object)`, который конфликтует с существующим методом.

**Без конфликта**:
```java
public class Name implements Comparable {
    private final String value;
    public Name(String value) { this.value = value; }
    @Override
    public String toString() { return "value='" + value + "'"; }
    public int compareTo(Name o) { return value.compareTo(o.value); }
    @Override
    public int compareTo(Object o) { return value.compareTo(((Name)o).value); }
}
```

---

## 34. Что такое Reifiable типы в Java?

Reifiable типы — это типы, полностью доступные в runtime. Они не теряют информацию из-за type erasure.

**Примеры**:
- Примитивы: `int`, `double`.
- Raw types: `List`, `Map`.
- Классы: `String`, `Integer`.
- Массивы: `String[]`, `int[]`.

**Не-reifiable**:
- `List<String>`, `List<?>`, `T`.

---

## 35. Почему нельзя создать параметризованный `Exception`?

```java
class MyException<T> extends Exception { T t; } // Ошибка
```

`catch` использует `instanceof`, требующий reifiable тип. Параметризованные типы не reifiable из-за type erasure.

---

## 36. Что такое Unchecked Warnings?

Unchecked Warnings — это предупреждения компилятора о небезопасном использовании типов, например, при работе с raw types. Их можно игнорировать, но лучше исправить.

---

## 37. Что такое Heap Pollution и почему оно происходит?

Heap Pollution — это ситуация, когда в коллекцию или массив добавляются объекты неверного типа, нарушая типобезопасность.

**Причины**:
- Type erasure.
- Использование raw types.
- Небезопасное приведение типов.

**Пример**:
```java
List<String> stringList = new ArrayList<>();
List rawList = stringList;
rawList.add(42);
String str = stringList.get(0); // ClassCastException
```

---

## 38. Какие последствия Heap Pollution?

1. **Ошибки в runtime** (`ClassCastException`).
2. **Нарушение типобезопасности**.
3. **Сложность отладки**.

---

## 39. Как связаны Reflection и Generics?

Из-за type erasure обобщённые типы недоступны в runtime, но рефлексия позволяет получить ограниченную информацию (например, через `ParameterizedType`).

**Пример**:
```java
List<Integer> ints = new ArrayList<>();
Class<? extends List> k = ints.getClass();
assert k == ArrayList.class;
```

---

## 40. Можно ли передать `List<String>` методу, который принимает `List<Object>`?

Нет, это вызовет ошибку компиляции из-за инвариантности обобщений:

```java
List<Object> objectList;
List<String> stringList;
objectList = stringList; // Ошибка
```

---

## 41. Можно ли использовать Generics с массивами?

Массивы не поддерживают обобщения:

```java
T[] array = new T[10]; // Ошибка
```

**Решение**:
- Использовать `Object[]` с приведением или `List`.

---

## 42. Как подавить непроверенные предупреждения в Java?

Используйте аннотацию `@SuppressWarnings("unchecked")`:

```java
@SuppressWarnings("unchecked")
List<String> rawList = new ArrayList();
```

---

## 43. Что такое Type Inference и как он работает в Java?

Type Inference — это процесс, при котором компилятор определяет тип данных на основе контекста.

**Пример**:
```java
List<String> list = new ArrayList<>();
var number = 42; // Java 10+: int
```

---

## 44. Где в Java используется Type Inference?

1. **Обобщения**:
    - Diamond operator (`<>`):
      ```java
      List<String> list = new ArrayList<>();
      ```

2. **Локальные переменные (Java 10+)**:
    - `var`:
      ```java
      var str = "Hello"; // String
      ```

3. **Лямбда-выражения**:
   ```java
   Function<String, Integer> func = s -> s.length();
   ```

4. **Обобщённые методы**:
   ```java
   public static <T> T getFirst(List<T> list) { return list.get(0); }
   ```
