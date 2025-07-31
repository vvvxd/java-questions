
Apache Kafka — это распределённая платформа для обработки потоков данных, а Spring Kafka предоставляет удобные инструменты для интеграции Kafka в приложения на Spring. Ниже я подробно и понятно разберу указанные аспекты работы с Spring Kafka, включая аннотацию **@KafkaListener**, **KafkaTemplate**, конфигурацию, обработку ошибок, транзакции, повторные попытки, пакетную обработку и конкурентность.

---

## 1. **@KafkaListener**: Аннотация для создания консьюмеров

**@KafkaListener** — это аннотация Spring Kafka, которая используется для создания потребителей (консьюмеров) сообщений из топиков Kafka. Она позволяет методу в Spring-приложении обрабатывать сообщения из указанных топиков.

### Основные характеристики:
- Аннотация применяется на уровне метода в бине (например, в классе, помеченном как `@Component` или `@Service`).
- Она указывает, из какого топика (или топиков) читать сообщения, и определяет параметры обработки.
- Работает в связке с **KafkaListenerContainerFactory**, которая управляет созданием и настройкой контейнеров для обработки сообщений.

### Основные атрибуты аннотации:
- **topics**: Указывает топики, из которых консьюмер будет читать сообщения.
  ```java
  @KafkaListener(topics = "my-topic")
  public void listen(String message) {
      System.out.println("Получено сообщение: " + message);
  }
  ```
- **groupId**: Идентификатор группы консьюмеров. Если не указан, берётся из конфигурации.
- **containerFactory**: Указывает фабрику контейнеров (по умолчанию `kafkaListenerContainerFactory`).
- **id**: Уникальный идентификатор листенера (опционально).
- **topicPattern**: Регулярное выражение для подписки на топики.
- **concurrency**: Количество потоков для обработки сообщений (для конкурентной обработки).

### Пример использования:
```java
@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consumeMessage(String message) {
        System.out.println("Получено: " + message);
    }
}
```
Этот код создаёт консьюмера, который читает сообщения из топика `my-topic` и выводит их в консоль.

### Особенности:
- **Типы данных**: Метод может принимать различные типы аргументов, такие как `String`, `ConsumerRecord`, `Message<T>`, или даже десериализованные объекты, если настроены соответствующие десериализаторы.
- **Поддержка заголовков**: Можно получить заголовки сообщений через параметр `@Header`:
  ```java
  @KafkaListener(topics = "my-topic")
  public void consumeMessage(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
      System.out.println("Сообщение: " + message + ", Partition: " + partition);
  }
  ```

---

## 2. **KafkaTemplate**: Инструмент для отправки сообщений (продюсер)

**KafkaTemplate** — это удобный класс Spring Kafka для отправки сообщений в топики Kafka. Он инкапсулирует логику работы с продюсером Kafka и предоставляет простой API для отправки сообщений.

### Основные возможности:
- Отправка сообщений в топик.
- Поддержка асинхронной и синхронной отправки.
- Работа с заголовками сообщений.
- Поддержка транзакций (если настроены).

### Пример использования:
```java
@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
```

### Асинхронная отправка с коллбэком:
```java
public void sendMessageWithCallback(String topic, String message) {
    ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
    future.addCallback(new ListenableFutureCallback<>() {
        @Override
        public void onSuccess(SendResult<String, String> result) {
            System.out.println("Сообщение отправлено: " + result.getRecordMetadata());
        }

        @Override
        public void onFailure(Throwable ex) {
            System.err.println("Ошибка отправки: " + ex.getMessage());
        }
    });
}
```

### Синхронная отправка:
```java
public void sendMessageSync(String topic, String message) {
    try {
        SendResult<String, String> result = kafkaTemplate.send(topic, message).get();
        System.out.println("Сообщение отправлено: " + result.getRecordMetadata());
    } catch (InterruptedException | ExecutionException e) {
        System.err.println("Ошибка отправки: " + e.getMessage());
    }
}
```

### Особенности:
- **KafkaTemplate** автоматически использует конфигурацию продюсера, заданную в `application.properties` или Java-конфигурации.
- Можно отправлять сообщения с ключами, заголовками и в определённые партиции:
  ```java
  kafkaTemplate.send("my-topic", "key", "message");
  ```

---

## 3. **Конфигурация продюсеров и консьюмеров**

Настройка продюсеров и консьюмеров в Spring Kafka обычно выполняется через файл `application.properties` или `application.yml`. Вот ключевые свойства и их описание:

### Основные свойства для продюсера:
- **bootstrap.servers**: Адреса брокеров Kafka (например, `localhost:9092`).
- **key.serializer**: Сериализатор для ключа сообщения (например, `org.apache.kafka.common.serialization.StringSerializer`).
- **value.serializer**: Сериализатор для значения сообщения (например, `org.apache.kafka.common.serialization.StringSerializer`).
- **acks**: Уровень подтверждения доставки:
  - `0`: Без подтверждения.
  - `1`: Подтверждение от лидера партиции.
  - `all`: Подтверждение от всех реплик (наиболее надёжный).
- **retries**: Количество повторных попыток отправки при сбое.
- **batch.size**: Размер пакета сообщений для отправки (в байтах).
- **linger.ms**: Время ожидания для формирования пакета сообщений.
- **buffer.memory**: Размер буфера памяти для сообщений.

Пример конфигурации продюсера:
```properties
spring.kafka.producer.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3
spring.kafka.producer.batch-size=16384
spring.kafka.producer.linger-ms=1
spring.kafka.producer.buffer-memory=33554432
```

### Основные свойства для консьюмера:
- **bootstrap.servers**: Адреса брокеров Kafka.
- **group.id**: Идентификатор группы консьюмеров (определяет, как распределяются партиции).
- **key.deserializer**: Десериализатор для ключа сообщения.
- **value.deserializer**: Десериализатор для значения сообщения.
- **auto.offset.reset**: Поведение при отсутствии сохранённого смещения:
  - `earliest`: Чтение с начала топика.
  - `latest`: Чтение только новых сообщений.
  - `none`: Ошибка, если смещение не найдено.
- **enable.auto.commit**: Автоматическое подтверждение смещений (по умолчанию `true`).
- **fetch.min.bytes**: Минимальный размер данных, который консьюмер запрашивает у брокера.
- **max.poll.records**: Максимальное количество записей за один вызов `poll`.

Пример конфигурации консьюмера:
```properties
spring.kafka.consumer.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=my-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.enable-auto-commit=true
spring.kafka.consumer.fetch-min-bytes=1
spring.kafka.consumer.max-poll-records=500
```

### Пример конфигурации через Java:
Если требуется более сложная настройка, можно использовать Java-конфигурацию:

```java
@Configuration
public class KafkaConfig {
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```

---

## 4. **Обработка ошибок**

Обработка ошибок в Spring Kafka важна для обеспечения надёжности приложения. Основные механизмы включают **ErrorHandler** и **Dead Letter Topics (DLT)**.

### ErrorHandler
Spring Kafka предоставляет интерфейсы `ErrorHandler` для обработки исключений в процессе обработки сообщений.

- **SeekToCurrentErrorHandler**: Повторяет обработку сообщения при сбоях и, при необходимости, отправляет сообщение в DLT.
- **DeadLetterPublishingRecoverer**: Отправляет неуспешные сообщения в Dead Letter Topic.

Пример настройки:
```java
@Bean
public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    
    // Настройка ErrorHandler
    SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler(
        new DeadLetterPublishingRecoverer(kafkaTemplate()), 
        new FixedBackOff(1000L, 3L) // 3 попытки с интервалом 1 сек
    );
    factory.setErrorHandler(errorHandler);
    return factory;
}
```

### Dead Letter Topics (DLT)
DLT — это топик, куда отправляются сообщения, которые не удалось обработать после всех попыток.

- **Настройка**: Используется `DeadLetterPublishingRecoverer` для перенаправления сообщений.
- **Конвенция**: Обычно DLT имеет имя исходного топика с суффиксом `.DLT` (например, `my-topic.DLT`).

Пример отправки в DLT:
```java
@KafkaListener(topics = "my-topic")
public void consumeMessage(String message) {
    if (message.contains("error")) {
        throw new RuntimeException("Ошибка обработки сообщения");
    }
    System.out.println("Обработано: " + message);
}
```

В случае исключения сообщение будет отправлено в `my-topic.DLT` (если настроен `DeadLetterPublishingRecoverer`).

---

## 5. **Транзакции**

Spring Kafka поддерживает транзакционную обработку сообщений, что позволяет отправлять и потреблять сообщения в рамках одной транзакции.

### Настройка транзакций:
1. Включите поддержку транзакций в продюсере:
   ```properties
   spring.kafka.producer.transaction-id-prefix=tx-
   ```
2. Настройте `KafkaTemplate` для работы в транзакционном режиме:
   ```java
   @Bean
   public KafkaTemplate<String, String> kafkaTemplate() {
       KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory());
       template.setTransactionIdPrefix("tx-");
       return template;
   }
   ```
3. Используйте аннотацию `@Transactional` в методе:
   ```java
   @Transactional
   public void sendMessageInTransaction(String topic, String message) {
       kafkaTemplate.send(topic, message);
       // Дополнительные операции
   }
   ```

### Транзакции для консьюмеров:
Для обработки транзакционных сообщений настройте `KafkaListenerContainerFactory`:
```java
@Bean
public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    factory.getContainerProperties().setTransactionManager(kafkaTransactionManager());
    return factory;
}

@Bean
public KafkaTransactionManager<String, String> kafkaTransactionManager() {
    return new KafkaTransactionManager<>(producerFactory());
}
```

### Особенности:
- Транзакции гарантируют, что сообщения отправляются и подтверждаются атомарно.
- Консьюмеры должны быть настроены с `isolation.level=read_committed` для чтения только подтверждённых сообщений.

---

## 6. **Ретрай и рекавери**

Spring Kafka предоставляет механизмы для повторных попыток (retries) и восстановления (recovery) при сбоях.

### Повторные попытки (Retries):
- Используется `SeekToCurrentErrorHandler` с `FixedBackOff` или `ExponentialBackOff` для настройки интервалов между попытками.
- Пример:
  ```java
  SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler(
      new DeadLetterPublishingRecoverer(kafkaTemplate()), 
      new FixedBackOff(1000L, 3L) // 3 попытки с интервалом 1 сек
  );
  ```

### Восстановление (Recovery):
- Если повторные попытки не помогли, сообщение отправляется в DLT с помощью `DeadLetterPublishingRecoverer`.
- Можно настроить собственный обработчик восстановления:
  ```java
  SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler((record, exception) -> {
      System.err.println("Не удалось обработать сообщение: " + record.value());
      // Логика восстановления
  }, new FixedBackOff(1000L, 3L));
  ```

---

## 7. **Batch Processing**

Spring Kafka поддерживает пакетную обработку сообщений, что позволяет обрабатывать сразу несколько сообщений за один вызов метода.

### Настройка пакетной обработки:
1. В `application.properties`:
   ```properties
   spring.kafka.consumer.max-poll-records=500
   spring.kafka.listener.type=batch
   ```
2. Настройка `KafkaListenerContainerFactory`:
   ```java
   @Bean
   public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
       ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
       factory.setConsumerFactory(consumerFactory());
       factory.setBatchListener(true); // Включаем пакетную обработку
       return factory;
   }
   ```

### Пример пакетного листенера:
```java
@KafkaListener(topics = "my-topic", containerFactory = "kafkaListenerContainerFactory")
public void consumeBatch(List<String> messages) {
    System.out.println("Получено " + messages.size() + " сообщений:");
    messages.forEach(System.out::println);
}
```

### Особенности:
- Метод может принимать `List<ConsumerRecord>` или `List<String>` (в зависимости от десериализатора).
- Пакетная обработка увеличивает пропускную способность, но требует осторожности при обработке ошибок, так как ошибка в одном сообщении может повлиять на весь пакет.

---

## 8. **ConcurrentKafkaListenerContainerFactory**

**ConcurrentKafkaListenerContainerFactory** используется для создания контейнеров, которые управляют конкурентной обработкой сообщений. Это позволяет распараллелить обработку сообщений из одного или нескольких топиков.

### Настройка конкурентности:
- **concurrency**: Количество потоков для обработки сообщений.
  ```java
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
      ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
      factory.setConsumerFactory(consumerFactory());
      factory.setConcurrency(3); // 3 потока
      return factory;
  }
  ```

### Пример использования:
```java
@KafkaListener(topics = "my-topic", containerFactory = "kafkaListenerContainerFactory")
public void consumeMessage(String message) {
    System.out.println("Обработано в потоке " + Thread.currentThread().getName() + ": " + message);
}
```

### Особенности:
- Количество потоков (`concurrency`) должно быть меньше или равно количеству партиций в топике, так как каждая партиция обрабатывается только одним потоком.
- Конкурентность увеличивает производительность, но требует синхронизации, если обработка сообщений взаимодействует с общими ресурсами.


---

### Оптимизация производительности

#### Продюсеры
- **linger.ms**: Время ожидания для формирования пакета (увеличивает задержку, но улучшает пропускную способность).
  ```properties
  spring.kafka.producer.linger-ms=5
  ```
- **batch.size**: Размер пакета в байтах.
  ```properties
  spring.kafka.producer.batch-size=16384
  ```
- **compression.type**: Тип сжатия (`gzip`, `snappy`, `lz4`).
  ```properties
  spring.kafka.producer.compression-type=gzip
  ```

#### Консьюмеры
- **max.poll.records**: Максимальное количество записей за один вызов `poll`.
  ```properties
  spring.kafka.consumer.max-poll-records=500
  ```
- **fetch.min.bytes**: Минимальный размер данных для возврата брокером.
  ```properties
  spring.kafka.consumer.fetch-min-bytes=1024
  ```
- **session.timeout.ms**: Тайм-аут сессии консьюмера.
  ```properties
  spring.kafka.consumer.session-timeout-ms=10000
  ```

#### Рекомендации:
- Увеличение `max.poll.records` и `fetch.min.bytes` повышает пропускную способность, но увеличивает нагрузку на память.
- Используйте сжатие (`gzip`) для уменьшения сетевого трафика.
- Настройте `concurrency` в `ConcurrentKafkaListenerContainerFactory` в соответствии с количеством партиций.



