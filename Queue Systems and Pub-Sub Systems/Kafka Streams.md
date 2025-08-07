### 1. Stream Processing: Потоковая vs. Пакетная обработка

- **Потоковая обработка**: Непрерывная обработка данных по мере поступления с минимальной задержкой. Данные — бесконечный поток событий (
  key-value pairs). Примеры: анализ логов, мониторинг транзакций.
- **Пакетная обработка**: Обработка больших наборов данных с задержкой. Примеры: отчеты за день, статистика за месяц.
- **Различия**:
    - Потоковая: низкая задержка, малые объемы данных, постоянная работа.
    - Пакетная: высокая задержка, большие объемы, запланированная обработка.
- **Принципы потоковой обработки**:
    - Событийный подход: каждая запись обрабатывается независимо или в контексте.
    - Масштабируемость: распределяется по партициям топиков.
    - Exactly-Once: семантика точной обработки без дублирования.
    - Состояние: хранится в локальных State Stores, синхронизируемых через Kafka.
---

### 2. DSL и Processor API

Kafka Streams предоставляет два уровня API для создания приложений:

- **DSL (Domain-Specific Language)**:
    - Высокоуровневый API для простых операций (`map`, `filter`, `join`, `groupBy`, `aggregate`).
    - Используется для большинства задач с простой логикой.
    - Пример:
      ```java
      KStream<String, String> stream = builder.stream("input-topic");
      KTable<String, Long> counts = stream
          .flatMapValues(text -> Arrays.asList(text.split(" ")))
          .groupBy((key, word) -> word)
          .count();
      counts.toStream().to("output-topic", Produced.with(Serdes.String(), Serdes.Long()));
      ```

- **Processor API**:
    - Низкоуровневый API для сложной логики и управления топологией.
    - Используется для кастомных операций или работы с несколькими топиками.
    - Пример:
      ```java
      public class MyProcessor implements Processor<String, String> {
          private ProcessorContext context;
          private KeyValueStore<String, Long> store;
          @Override
          public void init(ProcessorContext context) {
              this.context = context;
              this.store = context.getStateStore("my-store");
          }
          @Override
          public void process(String key, String value) {
              Long count = store.get(key);
              count = (count == null) ? 1L : count + 1;
              store.put(key, count);
              context.forward(key, count);
          }
          @Override
          public void close() {}
      }
      ```

---

### 3. Топологии

Топология в Kafka Streams — это граф, описывающий, как данные проходят через приложение. Она определяет последовательность операций обработки данных.

**Компоненты топологии:**
- **Источник (Source):** Точка входа, где данные читаются из топика Kafka. Например, `builder.stream("input-topic")` создает источник.
- **Процессор (Processor):** Узел, выполняющий обработку данных (например, фильтрация, трансформация, агрегация). В DSL процессоры создаются автоматически при вызове операций `map`, `filter` и т.д.
- **Сток (Sink):** Точка выхода, где данные записываются в выходной топик Kafka. Например, `stream.to("output-topic")`.

**Как строится топология:**
- Топология задается программно через API (DSL или Processor API).
- Kafka Streams компилирует топологию в граф, который распределяется между инстансами приложения для параллельной обработки.
- Каждая партиция топика обрабатывается отдельной задачей (task), что обеспечивает масштабируемость.

**Пример топологии (DSL):**
```java
StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> stream = builder.stream("input-topic");
stream.filter((key, value) -> value != null)
      .mapValues(value -> value.toUpperCase())
      .to("output-topic");
Topology topology = builder.build();
```

**Особенности:**
- Топология неизменяема после запуска приложения.
- Kafka Streams автоматически управляет распределением задач между инстансами приложения (используя consumer groups).

---

### 4. KStream и KTable

Kafka Streams оперирует двумя основными абстракциями для представления данных: **KStream** и **KTable**.

#### KStream
- **Описание:** Представляет бесконечный поток записей, где каждая запись обрабатывается независимо. Это "поток событий" (event stream).
- **Когда использовать:** Для обработки данных, где порядок и независимость событий важны (например, логи, клики, транзакции).
- **Характеристики:**
  - Каждая запись в KStream — это отдельное событие (key-value pair).
  - Не хранит состояние, если не используется агрегация.
  - Поддерживает операции: `map`, `filter`, `join`, `groupBy`, и т.д.
- **Пример:** Подсчет кликов пользователей в реальном времени.

#### KTable
- **Описание:** Представляет "таблицу" данных, которая отражает текущее состояние (snapshot) по ключу. KTable обновляется, когда приходят новые записи с тем же ключом.
- **Когда использовать:** Для задач, где важно текущее состояние, например, профили пользователей, балансы счетов.
- **Характеристики:**
  - KTable хранит только последнюю запись для каждого ключа.
  - Использует **changelog stream** (журнал изменений) для обновления состояния.
  - Поддерживает операции: `join`, `groupBy`, `aggregate`.
- **Пример:** Хранение текущего баланса пользователя, обновляемого при каждой транзакции.

**Разница между Event Stream и Changelog Stream:**
- **Event Stream (KStream):** Поток событий, где каждая запись независима и обрабатывается как новое событие. Пример: логирование каждого клика пользователя.
- **Changelog Stream (KTable):** Поток изменений, где каждая запись обновляет состояние для определенного ключа. Пример: обновление баланса пользователя при каждой транзакции.

**Пример кода (KStream vs KTable):**
```java
// KStream: обработка потока логов
KStream<String, String> logs = builder.stream("logs-topic");
logs.filter((key, value) -> value.contains("ERROR"))
    .to("error-logs-topic");

// KTable: хранение текущего состояния пользователей
KTable<String, String> users = builder.table("users-topic");
users.filter((key, value) -> value != null)
     .toStream()
     .to("active-users-topic");
```

**KStream vs KTable: сравнение:**
- **KStream:** Поток событий, не хранит состояние, подходит для обработки "один ко многим".
- **KTable:** Таблица состояния, хранит последнее значение для ключа, подходит для операций, зависящих от состояния.

---

### 5. Глобальные таблицы (GlobalKTable)

**Описание:**
- **GlobalKTable** — это специальная версия KTable, которая доступна всем задачам (tasks) приложения, независимо от партиций.
- **Когда использовать:** Для данных, которые должны быть доступны глобально, например, справочные таблицы (lookup tables), такие как конфигурации, словари или правила.
- **Характеристики:**
  - GlobalKTable реплицируется на все инстансы приложения, в отличие от обычной KTable, которая распределяется по партициям.
  - Использует больше памяти, так как каждая нода хранит полную копию данных.
  - Подходит для join-операций с KStream или KTable, когда данные не зависят от партиционирования.
- **Ограничения:**
  - GlobalKTable не поддерживает агрегацию или группировку, так как это "read-only" структура.
  - Обновления GlobalKTable медленнее, так как данные синхронизируются между всеми нодами.

**Пример кода (GlobalKTable):**
```java
StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> stream = builder.stream("orders-topic");
GlobalKTable<String, String> configTable = builder.globalTable("config-topic");

stream.join(configTable,
    (orderKey, orderValue) -> orderKey, // Ключ для join
    (orderValue, configValue) -> orderValue + ":" + configValue) // Объединение данных
    .to("enriched-orders-topic");
```

**Пример сценария:**
- У вас есть поток заказов (`KStream`) и справочная таблица с конфигурациями продуктов (`GlobalKTable`). Вы можете объединить заказы с конфигурациями, чтобы добавить дополнительную информацию к каждому заказу.


### 3. **Состояние и State Stores**

### 1. Stateful Processing

**Stateful Processing** (обработка с сохранением состояния) — это операции в Kafka Streams, которые требуют хранения промежуточных или итоговых данных для выполнения вычислений. В отличие от stateless операций (например, `map` или `filter`), которые обрабатывают каждую запись независимо, stateful операции зависят от предыдущих данных или контекста.

#### Основные примеры stateful операций:
- **Агрегации:** Подсчет суммы, среднего или количества записей по ключу (например, `count`, `reduce`, `aggregate`).
- **Группировки:** Группировка записей по ключу с последующей обработкой (`groupBy`, `groupByKey`).
- **Оконные операции (Windowing):** Обработка данных в заданных временных окнах, таких как скользящие (sliding), сессионные (session) или фиксированные (tumbling) окна.
  - **Tumbling windows:** Непересекающиеся окна фиксированной длины (например, каждые 5 минут).
  - **Sliding windows:** Скользящие окна с перекрытием (например, каждые 5 секунд для окна в 10 секунд).
  - **Session windows:** Окна, основанные на активности сессий (группировка событий по активности пользователя).
- **Joins:** Объединение двух потоков или таблиц (например, `KStream.join(KTable)`), где требуется доступ к состоянию одной из сторон.

#### Управление состоянием:
- Kafka Streams сохраняет состояние в **State Stores** — локальных хранилищах, которые содержат промежуточные или итоговые данные (например, текущие суммы или счетчики).
- Состояние синхронизируется между нодами через **changelog-топики** (журналы изменений), чтобы обеспечить восстановление данных в случае сбоев.

**Пример stateful обработки (агрегация):**
```java
KStream<String, Long> stream = builder.stream("orders-topic");
KTable<String, Long> totalByUser = stream
    .groupByKey()
    .aggregate(
        () -> 0L, // Инициализатор
        (key, value, aggregate) -> aggregate + value, // Агрегатор
        Materialized.as("total-by-user-store") // Имя хранилища состояния
    );
totalByUser.toStream().to("totals-topic");
```
В этом примере создается `KTable`, которая хранит сумму заказов для каждого пользователя. Состояние (текущая сумма) сохраняется в локальном хранилище `total-by-user-store`.

---

### 2. State Stores

**State Stores** — это локальные хранилища, используемые Kafka Streams для хранения состояния, необходимого для stateful операций. Они могут быть как временными (in-memory), так и постоянными (persistent), и синхронизируются с Kafka через changelog-топики.

#### Типы State Stores:
1. **In-Memory State Store:**
  - Хранит данные в оперативной памяти.
  - Быстрее, но данные теряются при сбое приложения, если не используется changelog-топик.
  - Подходит для приложений с низкими требованиями к восстановлению.
2. **Persistent State Store:**
  - Хранит данные на диске (например, с использованием RocksDB).
  - Обеспечивает долговечность данных и восстановление после сбоев.
  - Используется по умолчанию для большинства stateful операций.

#### Changelog-топики:
- Для каждого State Store Kafka Streams создает **changelog-топик** в Kafka, куда записываются все изменения состояния.
- Changelog-топик используется для:
  - Восстановления состояния при сбое или перезапуске приложения.
  - Репликации состояния между нодами для обеспечения отказоустойчивости.
- Changelog-топик автоматически создается с именем вида: `<application-id>-<store-name>-changelog`.

#### RocksDB:
- **RocksDB** — это встроенная библиотека, используемая Kafka Streams по умолчанию для persistent State Stores.
- Преимущества:
  - Высокая производительность для операций чтения/записи.
  - Поддержка больших объемов данных (хранятся на диске).
  - Компактное хранение благодаря сжатию.
- Настройка RocksDB:
  - Можно настроить параметры RocksDB (например, размер кэша, сжатие) через `RocksDBConfigSetter`.
  ```java
  public class CustomRocksDBConfig implements RocksDBConfigSetter {
      @Override
      public void setConfig(String storeName, Options options, Map<String, Object> configs) {
          options.setCompressionType(CompressionType.LZ4_COMPRESSION);
          options.setWriteBufferSize(16 * 1024 * 1024); // 16 MB
      }
  }
  ```
  - Указать кастомный конфигуратор в настройках Kafka Streams:
  ```java
  properties.put(StreamsConfig.ROCKSDB_CONFIG_SETTER_CLASS_CONFIG, CustomRocksDBConfig.class);
  ```

#### Кастомные State Stores:
- Kafka Streams позволяет создавать кастомные хранилища, реализуя интерфейс `StateStore`.
- Пример использования: интеграция с внешними хранилищами (например, Redis, Cassandra) для специфических сценариев.
- Для кастомного хранилища нужно:
  1. Реализовать интерфейс `StateStore` и методы для чтения/записи.
  2. Зарегистрировать хранилище в топологии через `StreamsBuilder.addStateStore()`.

**Пример создания State Store:**
```java
KStream<String, String> stream = builder.stream("input-topic");
stream.process(() -> new MyCustomProcessor(), "my-state-store");
```

---

### 3. Fault Tolerance

Kafka Streams обеспечивает **отказоустойчивость** (fault tolerance) для stateful приложений через несколько механизмов:

#### 1. Changelog-топики:
- Как упомянуто выше, все изменения в State Store записываются в changelog-топик.
- При сбое или перезапуске приложения Kafka Streams восстанавливает состояние, читая данные из changelog-топика.
- Changelog-топики реплицируются в Kafka (с заданным replication factor), что обеспечивает надежность даже при отказе брокеров.

#### 2. Standby Tasks:
- Kafka Streams поддерживает **standby tasks** — резервные задачи, которые дублируют состояние основного приложения на других инстансах.
- Если основной инстанс (task) выходит из строя, standby task может быстро взять на себя обработку, минимизируя время простоя.
- Настройка standby tasks:
  ```java
  properties.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 1); // Один standby task на каждую задачу
  ```
- Standby tasks синхронизируются через changelog-топики, поэтому они всегда имеют актуальное состояние.

#### 3. Репликация и партиционирование:
- Kafka Streams распределяет задачи (tasks) между инстансами приложения, используя партиции топиков.
- Каждая задача обрабатывает одну или несколько партиций и хранит свое состояние локально.
- Если инстанс выходит из строя, Kafka Streams автоматически переназначает задачи другим инстансам, используя механизм consumer groups.

#### 4. Точно один раз (Exactly-Once):
- Kafka Streams поддерживает семантику **exactly-once** (включена по умолчанию с версии Kafka 2.5).
- Это гарантирует, что каждая запись обрабатывается ровно один раз, даже при сбоях.
- Реализуется через транзакции Kafka и координацию между consumer и producer:
  - Состояние и результаты обработки записываются в рамках одной транзакции.
  - Настройка:
    ```java
    properties.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);
    ```

#### 5. Восстановление после сбоев:
- При перезапуске приложения Kafka Streams восстанавливает состояние из changelog-топика.
- Процесс восстановления:
  1. Kafka Streams читает все записи из changelog-топика для соответствующего State Store.
  2. Восстанавливает данные в локальном хранилище (например, RocksDB).
  3. Продолжает обработку с того места, где остановилось.
- Время восстановления зависит от размера changelog-топика и скорости чтения.



### 4. **Временная обработка (Time and Windows)**

### 1. Типы времени

В Kafka Streams временные метки критически важны для обработки данных. Основные типы времени:

1. **Event Time**:
    - Время создания события (например, покупка в 10:00).
    - Извлекается из данных записи или задается при записи в топик.
    - Используется для точной аналитики (например, подсчет транзакций за час).

2. **Processing Time**:
    - Время обработки события приложением (системное время).
    - Подходит для операций, зависящих от текущего момента (например, логирование).
    - Чувствительно к задержкам обработки.

3. **Ingestion Time**:
    - Время записи события в топик Kafka (при `log.message.timestamp.type=LogAppendTime`).
    - Полезно, когда event time недоступен, но нужна согласованность.

**Важность**:
- Временные метки определяют порядок событий в оконных операциях.
- **Event time** предпочтителен для точности аналитики.
- Настройка через `TimestampExtractor`:
  ```java
  public class CustomTimestampExtractor implements TimestampExtractor {
      @Override
      public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
          return ((MyEvent) record.value()).getTimestamp();
      }
  }
  ```
  Настройка:
  ```java
  properties.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, CustomTimestampExtractor.class);
  ```

---

### 2. Оконные операции
Оконные операции в Kafka Streams группируют события по временным интервалам для операций `count`, `aggregate`, `reduce`. Типы окон:

1. **Tumbling Windows (Фиксированные окна)**:
    - Непересекающиеся окна фиксированной длины.
    - Пример: Подсчет транзакций за 5 минут (10:00–10:05, 10:05–10:10).
    - Код:
      ```java
      TimeWindows.of(Duration.ofMinutes(5))
      ```

2. **Hopping Windows (Скользящие окна с шагом)**:
    - Окна фиксированной длины, перекрывающиеся с шагом.
    - Пример: 10-минутные окна, сдвиг каждые 2 минуты (10:00–10:10, 10:02–10:12).
    - Код:
      ```java
      TimeWindows.of(Duration.ofMinutes(10)).advanceBy(Duration.ofMinutes(2))
      ```

3. **Sliding Windows (Скользящие окна)**:
    - Окна вокруг события для операций `join` с заданным временным диапазоном.
    - Пример: Объединение потоков, если события отличаются не более чем на 1 минуту.
    - Код:
      ```java
      JoinWindows.of(Duration.ofMinutes(1))
      ```

4. **Session Windows (Сессионные окна)**:
    - Динамические окна по активности ключа с периодом неактивности.
    - Пример: Группировка кликов пользователя в сессии с интервалом не более 5 минут.
    - Код:
      ```java
      SessionWindows.with(Duration.ofMinutes(5))
      ```

**Grace Period**:
- Период для учета опоздавших событий (late events).
- Настройка:
  ```java
  TimeWindows.of(Duration.ofMinutes(5)).grace(Duration.ofMinutes(1))
  ```
- После grace period окно закрывается, результаты не обновляются.

**Пример**:
```java
KStream<String, Long> stream = builder.stream("transactions-topic");
TimeWindows window = TimeWindows.of(Duration.ofMinutes(5)).grace(Duration.ofMinutes(1));
KTable<Windowed<String>, Long> counts = stream
    .groupByKey()
    .windowedBy(window)
    .count();
counts.toStream().to("counts-topic");
```
Подсчет транзакций по ключу в 5-минутных окнах с grace period 1 минута.

---

### 3. Suppression

**Suppression** в Kafka Streams ограничивает промежуточные результаты оконных операций, отправляя только финальные результаты после закрытия окна.

**Зачем нужна**:
- Снижает объем данных в выходных топиках.
- Упрощает обработку, исключая промежуточные обновления.

**Как работает**:
- Применяется к `KTable` после оконной операции.
- Оператор `suppress` задерживает вывод до закрытия окна (включая grace period).

**Стратегии**:
1. `Suppress.untilWindowCloses`: Ждет закрытия окна.
2. `Suppress.untilTimeLimit`: Ждет заданное время.

**Пример**:
```java
KStream<String, Long> stream = builder.stream("transactions-topic");
TimeWindows window = TimeWindows.of(Duration.ofMinutes(5)).grace(Duration.ofMinutes(1));
KTable<Windowed<String>, Long> counts = stream
    .groupByKey()
    .windowedBy(window)
    .count()
    .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()));
counts.toStream().to("counts-topic");
```
- Подсчет транзакций в 5-минутных окнах.
- Финальный результат отправляется после закрытия окна и grace period.

**Настройка буфера**:
- `BufferConfig.unbounded()`: Неограниченный буфер.
- `BufferConfig.maxRecords(n)`: Лимит по записям.
- `BufferConfig.maxBytes(n)`: Лимит по памяти.
  ```java
  .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.maxBytes(10_000_000)))
  ```

**Ограничения**:
- Только для оконных `KTable`.
- Требует памяти для буфера.
- Увеличивает задержку вывода.

---

### 5. **Сериализация и десериализация**

### 1. SerDes (Сериализаторы и Десериализаторы)

**SerDes** — это сокращение от **Serializer/Deserializer**, механизмов, которые используются в Kafka для преобразования объектов в байтовые массивы (сериализация) и обратно (десериализация). В Kafka Streams SerDes необходимы для работы с ключами и значениями записей в топиках.

#### Понимание SerDes
- **Сериализация:** Преобразование данных (например, Java-объекта, JSON, Avro) в байтовый формат для записи в топик Kafka.
- **Десериализация:** Преобразование байтов из топика обратно в объект или структуру данных для обработки в приложении.
- Kafka Streams требует, чтобы для каждого потока (`KStream`, `KTable`) были определены SerDes для ключей и значений.

#### Популярные форматы данных
1. **JSON:**
  - Прост в использовании, читаем человеком.
  - Подходит для небольших данных или прототипирования.
  - Недостатки: больший объем данных по сравнению с бинарными форматами, отсутствие строгой схемы.
  - Пример библиотеки: `Gson` или `Jackson` для сериализации/десериализации JSON.

2. **Avro:**
  - Компактный бинарный формат, разработанный для Kafka.
  - Поддерживает схемы данных (Schema Registry), что обеспечивает совместимость и валидацию.
  - Преимущества: компактность, высокая производительность, поддержка эволюции схем.
  - Пример: Используется в крупных системах для обработки больших объемов данных.

3. **Protobuf (Protocol Buffers):**
  - Бинарный формат от Google, компактный и быстрый.
  - Поддерживает схемы, но менее интегрирован с Kafka, чем Avro.
  - Преимущества: высокая производительность, поддержка кросс-языковой совместимости.
  - Недостатки: сложнее в настройке по сравнению с Avro для Kafka.

#### Настройка SerDes в Kafka Streams
Kafka Streams предоставляет встроенные SerDes для стандартных типов данных (например, `String`, `Long`, `Double`) через класс `Serdes`. Для пользовательских типов или форматов (JSON, Avro, Protobuf) требуется настройка кастомных SerDes.

**Пример: Встроенные SerDes**
```java
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

Properties props = new Properties();
props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
```

---

### 2. Schema Registry

**Confluent Schema Registry** — это инструмент для управления схемами данных (например, Avro, JSON Schema, Protobuf) в Kafka. Он обеспечивает централизованное хранение схем, их версионирование и проверку совместимости.

#### Зачем нужен Schema Registry?
- **Управление схемами:** Хранит схемы для данных, используемых в топиках Kafka.
- **Совместимость:** Гарантирует, что изменения в схемах (например, добавление поля) не нарушат работу существующих приложений.
- **Валидация данных:** Проверяет, что данные в топиках соответствуют зарегистрированным схемам.
- **Компактность:** Схемы хранятся в Schema Registry, а в сообщениях передается только ID схемы, что уменьшает размер данных.

#### Как работает Schema Registry?
1. **Регистрация схемы:** Производитель (producer) регистрирует схему данных (например, Avro) в Schema Registry при записи в топик.
2. **Сериализация:** При сериализации в сообщение добавляется ID схемы, а сами данные сериализуются в соответствии со схемой.
3. **Десериализация:** Потребитель (consumer) запрашивает схему по ID из Schema Registry для десериализации данных.
4. **Проверка совместимости:** Schema Registry проверяет, совместимы ли новые версии схемы с предыдущими (например, backward, forward или full compatibility).

---

### 1. Параллелизм

Kafka Streams обеспечивает параллелизм и масштабируемость через партиции топиков и распределение задач между инстансами.

#### Масштабирование через партиции и задачи
- **Партиции**: Данные делятся на партиции, которые обрабатываются независимо для параллелизма.
- **Задачи (Tasks)**: Каждая задача обрабатывает одну или несколько партиций входного топика.
    - Пример: Топик с 10 партициями → до 10 задач.
    - Задачи распределяются по инстансам на основе топологии.
- **Инстансы**: Каждый инстанс обрабатывает несколько задач, ограничено партициями и потоками.

**Механизм масштабирования**:
- Добавление инстансов распределяет задачи через consumer groups.
- Параллелизм ограничен числом партиций (например, 4 партиции → максимум 4 задачи).
- Пример: Топик с 8 партициями, 4 инстанса → каждый инстанс обрабатывает ~2 задачи.

#### Роль consumer groups
- **Механизм**: Инстансы (часть consumer group, заданной `application.id`) делят партиции через протокол ребалансировки Kafka.
- **Ребалансировка**: При добавлении/сбое инстанса задачи перераспределяются.
- **Standby tasks**: Дублируют состояние для быстрого восстановления.
  ```java
  properties.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 1); // 1 резервная задача
  ```

**Ограничения**:
- Параллелизм ограничен числом партиций.
- Ребалансировка может вызывать задержки из-за перераспределения задач и восстановления состояния.
---

### 2. Оптимизация

Оптимизация Kafka Streams направлена на повышение производительности, снижение задержек и эффективное использование ресурсов. Рассмотрим ключевые аспекты.

#### Настройка параметров

1. **num.stream.threads**:
    - Количество потоков в инстансе Kafka Streams.
    - По умолчанию: 1.
    - Рекомендация: Установить по числу ядер CPU или чуть больше для I/O-задач.
   ```java
   properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 4); // 4 потока
   ```

2. **Buffer Sizes**:
    - **cache.max.bytes.buffering**: Размер кэша для буферизации перед записью.
        - По умолчанию: 10 MB.
        - Увеличение снижает записи, но требует больше памяти.
      ```java
      properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 50 * 1024 * 1024); // 50 MB
      ```
    - **commit.interval.ms**: Интервал коммита обработанных записей.
        - По умолчанию: 30 сек (100 мс для exactly-once).
        - Меньший интервал увеличивает частоту коммитов, снижая производительность.
      ```java
      properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000); // 1 сек
      ```

3. **RocksDB настройки**:
    - Используется для persistent State Stores.
    - Настройка кэша и сжатия:
   ```java
   public class CustomRocksDBConfig implements RocksDBConfigSetter {
       @Override
       public void setConfig(String storeName, Options options, Map<String, Object> configs) {
           options.setWriteBufferSize(32 * 1024 * 1024); // 32 MB
           options.setCompressionType(CompressionType.LZ4_COMPRESSION);
       }
   }
   properties.put(StreamsConfig.ROCKSDB_CONFIG_SETTER_CLASS_CONFIG, CustomRocksDBConfig.class);
   ```

#### Кэширование для changelog
- **Преимущества**:
    - Снижает нагрузку на Kafka за счет буферизации.
    - Ускоряет обработку, храня данные в памяти.
- **Настройка**:
    - `cache.max.bytes.buffering` задает размер кэша.
    - Отключение (`0`) увеличивает нагрузку на Kafka.
  ```java
  properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 100 * 1024 * 1024); // 100 MB
  ```
- **Трейд-офф**:
    - Большой кэш: выше производительность, больше памяти, задержка при коммите.
    - Маленький кэш: меньше памяти, выше нагрузка на Kafka.

#### Балансировка нагрузки
- **Распределение партиций**: Количество партиций топика должно быть кратно числу инстансов.
- **Увеличение партиций**: Повышает параллелизм.
  ```bash
  kafka-topics.sh --alter --topic input-topic --partitions 16
  ```
- **Оптимизация топологии**: Избегайте лишних `groupBy`, вызывающих ребалансировку.
- **Standby tasks**: Ускоряют восстановление после сбоев.
  ```java
  properties.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 2); // 2 резервные копии
  ```

---

### 3. Мониторинг и отладка

Эффективный мониторинг и отладка критически важны для диагностики проблем и обеспечения надежной работы приложений Kafka Streams.

#### Метрики Kafka Streams
Kafka Streams предоставляет множество метрик через **JMX** (Java Management Extensions), которые можно интегрировать с системами мониторинга, такими как **Prometheus** или **Grafana**.

1. **Типы метрик:**
  - **Stream-level метрики:** Общая производительность приложения (например, `record-latency-avg` — средняя задержка обработки записей).
  - **Task-level метрики:** Информация о задачах, таких как количество обработанных записей или время коммита.
  - **State Store метрики:** Производительность хранилищ состояния (например, `put-latency-avg` для RocksDB).
  - **Consumer/Producer метрики:** Метрики, связанные с чтением из топиков или записью в них (например, `records-consumed-total`).

2. **Настройка JMX:**
  - Включите JMX в конфигурации приложения:
    ```java
    properties.put("com.sun.management.jmxremote", "true");
    properties.put("com.sun.management.jmxremote.port", "9999");
    properties.put("com.sun.management.jmxremote.authenticate", "false");
    properties.put("com.sun.management.jmxremote.ssl", "false");
    ```
  - Используйте инструменты, такие как JConsole или VisualVM, для просмотра метрик.

3. **Интеграция с Prometheus:**
  - Используйте библиотеку `kafka-streams-prometheus-metrics` или JMX Exporter для передачи метрик в Prometheus.

#### Логирование
- Kafka Streams использует библиотеку SLF4J для логирования. Убедитесь, что настроен подходящий логгер (например, Log4j или Logback).
- Настройка уровня логирования:
  ```xml
  <!-- log4j2.xml -->
  <Logger name="org.apache.kafka.streams" level="INFO"/>
  ```
- Полезные логи:
  - Информация о создании задач и топологий.
  - Ошибки десериализации или обработки записей.
  - События ребалансировки consumer group.

#### Обработка ошибок
1. **ProductionExceptionHandler:**
  - Используется для обработки ошибок при записи результатов в выходные топики.
  - По умолчанию: `DefaultProductionExceptionHandler` (останавливает приложение при ошибке).


2. **DeserializationExceptionHandler:**
  - Используется для обработки ошибок десериализации записей из топиков.
  - По умолчанию: `LogAndFailExceptionHandler` (логирует ошибку и останавливает приложение).
`

3. **Обработка late events:**
  - Для обработки опоздавших событий в оконных операциях можно использовать `TimestampExtractor` или фильтровать такие события:
    ```java
    stream.filter((key, value) -> context.recordTimestamp() >= windowStartTime);
    ```

---



