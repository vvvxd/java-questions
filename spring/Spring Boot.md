Что такое XML Hell и ручная настройка?

Представьте, что вы хотите создать простое веб-приложение на "классическом" Spring Framework (до версии 4.x). Ваши шаги были бы примерно такими:

1.  **Управление зависимостями:** Вам нужно было вручную добавить в `pom.xml` (или другой сборщик) десятки зависимостей: `spring-core`, `spring-web`, `spring-webmvc`, `jackson-databind` для JSON, `hibernate-validator` для валидации, `tomcat-embed-core` для сервера и т.д. Самая большая боль — найти **совместимые** версии всех этих библиотек. Это был настоящий "ад зависимостей" (dependency hell).

2.  **Конфигурация в XML:** Вам нужно было создать несколько XML-файлов конфигурации:
   *   `web.xml`: Стандартный дескриптор развертывания Java EE. Здесь вы прописывали `DispatcherServlet` от Spring, фильтры, слушатели. Километры boilerplate-кода.
   *   `applicationContext.xml`: Основной контекст приложения. Здесь вы объявляли бины (сервисы, репозитории), настраивали `DataSource` для базы данных, `TransactionManager` для транзакций, `DataSource` для подключения к БД и т.д.
   *   `dispatcher-servlet.xml` (или `mvc-config.xml`): Конфигурация для веб-слоя. Здесь вы настраивали `ViewResolver` (чтобы Spring знал, где искать ваши JSP/Thymeleaf страницы), обработчики статических ресурсов, конвертеры JSON и многое другое.

3.  **Развертывание:** Вы собирали ваше приложение в WAR-файл и вручную развертывали его на отдельно установленном сервере приложений (Tomcat, JBoss, GlassFish). Вам нужно было управлять этим сервером отдельно.

**Итог:** На создание простейшего "Hello, World" REST-контроллера уходили часы рутинной настройки. Порог входа был очень высоким, а процесс — медленным и подверженным ошибкам.

---

### Что такое Spring Boot? Ключевая идея

**Spring Boot** — это не замена Spring Framework. Это **надстройка** над ним, которая кардинально упрощает и ускоряет процесс разработки, конфигурирования и развертывания Spring-приложений.

Его философия — **"Convention over Configuration"** (Соглашение вместо конфигурации). Spring Boot говорит: "Я знаю, как обычно настраивают веб-приложение, приложение для работы с базой данных или для отправки сообщений. Просто скажи мне, что ты хочешь сделать, и я настрою все за тебя по умолчанию. Если тебе не понравятся мои настройки, ты сможешь их легко переопределить".

По сути, Spring Boot это просто набор классов конфигурации, которые создают нужные бины в контексте. Точно так же их можно создать руками, просто Boot это автоматизирует. При этом помогая решить проблему конфликтов разных версий компонентов.
Чтобы ускорить процесс управления зависимостями, Spring Boot неявно упаковывает необходимые сторонние зависимости для каждого типа приложения на основе Spring и предоставляет их разработчику посредством так называемых starter-пакетов (spring-boot-starter-web, spring-boot-starter-data-jpa и т.д.)

---

### Какие главные нововведения Spring Boot?

#### 1. Автоконфигурация (Autoconfiguration)

Это самая "магическая" и важная часть Spring Boot.

*   **Как это работает?** В основе лежит аннотация `@EnableAutoConfiguration` (которая спрятана внутри главной аннотации `@SpringBootApplication`). При запуске приложения Spring Boot анализирует ваш **classpath** (т.е. какие библиотеки у вас подключены).
*   **Пример:**
   *   Spring Boot видит в classpath `spring-boot-starter-web` и, следовательно, библиотеку `spring-webmvc`. Он думает: "Ага, разработчик хочет создать веб-приложение!". И он автоматически настраивает `DispatcherServlet`, `Tomcat` как встроенный сервер, `Jackson` для (де)сериализации JSON, `ViewResolver` и многое другое.
   *   Затем он видит `spring-boot-starter-data-jpa` и драйвер для H2/PostgreSQL. Он думает: "Похоже, нужна работа с базой данных!". И автоматически создает бины `DataSource`, `EntityManagerFactory` и `PlatformTransactionManager`, используя данные из вашего `application.properties`.

*   **Под капотом:** Эта магия реализуется через механизм **условных аннотаций** (`@ConditionalOnClass`, `@ConditionalOnBean`, `@ConditionalOnProperty` и т.д.). Внутри библиотеки `spring-boot-autoconfigure.jar` лежат сотни классов конфигурации, каждый из которых активируется только при выполнении определенного условия. Например: `TomcatServletWebServerFactory` будет создан только при условии `@ConditionalOnClass(ServletRequest.class)` и `@ConditionalOnMissingBean(ServletWebServerFactory.class)` (т.е. если в classpath есть сервлеты и вы сами не создали фабрику серверов).

#### 2. "Стартеры" (Starter Dependencies)

Это решение проблемы "ада зависимостей".

*   **Что это?** Стартеры — это просто удобные "мета-зависимости" в Maven/Gradle. Это POM-файлы, которые не содержат кода, а лишь декларируют набор других, согласованных между собой по версиям, зависимостей.
*   **Пример:** Вместо того чтобы подключать 10-15 библиотек для веб-приложения, вы просто добавляете одну:
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    ```
    И эта одна зависимость транзитивно подтянет Spring MVC, Tomcat, Jackson, Validation API и все остальное, что нужно для старта. Вам больше не нужно беспокоиться о совместимости версий. Есть стартеры почти для всего: `spring-boot-starter-data-jpa`, `spring-boot-starter-security`, `spring-boot-starter-amqp` (для RabbitMQ) и т.д.

#### 3. Встроенный сервер (Embedded Server)

Это нововведение кардинально изменило процесс разработки и развертывания.

*   **Что это?** Spring Boot по умолчанию упаковывает ваше приложение не в WAR-файл, а в исполняемый **JAR-файл**. Внутри этого JAR-файла находится не только ваш код, но и полноценный веб-сервер (Tomcat по умолчанию, но можно легко заменить на Jetty или Netty).
*   **Преимущества:**
   *   **Простота запуска:** Вы просто запускаете приложение командой `java -jar my-app.jar`. Никаких внешних серверов, которые нужно устанавливать и настраивать.
   *   **Портативность:** Приложение полностью автономно.
   *   **Идеально для микросервисов и Docker:** Упаковать такой JAR-файл в Docker-контейнер — тривиальная задача. Это сильно упрощает CI/CD и развертывание в облачных средах.

#### 4. Внешняя конфигурация (Externalized Configuration) и Actuator

*   **Внешняя конфигурация:** Spring Boot предоставляет мощный и гибкий механизм для управления конфигурацией через файлы `application.properties` или `application.yml`. Вы можете легко определять разные профили (`dev`, `test`, `prod`) и переопределять свойства через переменные окружения или параметры командной строки, что крайне важно для современных приложений.
   *   *Пример:* Задать порт сервера: `server.port=8081` в `application.properties`.
   *   *Пример:* Запустить с другим портом: `java -jar my-app.jar --server.port=9000`.

*   **Spring Boot Actuator:** Это еще один стартер (`spring-boot-starter-actuator`), который добавляет в ваше приложение готовые к использованию в продакшене конечные точки (endpoints) для мониторинга и управления.
   *   `/actuator/health`: Проверяет состояние приложения (например, доступность базы данных). Используется в Kubernetes (liveness/readiness probes).
   *   `/actuator/metrics`: Предоставляет детальные метрики (использование памяти, количество запросов, время ответа), которые можно интегрировать с системами мониторинга вроде Prometheus.
   *   `/actuator/info`: Показывает общую информацию о сборке.
   *   `/actuator/env`: Показывает текущие переменные окружения.

--------------------------------------------------------------------------------------------------------------------

Автоконфигурация в Spring Boot как работают?

### Отправная точка: Аннотация `@SpringBootApplication`

Все начинается здесь. Это не просто маркер, а мощная **составная аннотация**, которая объединяет в себе три ключевые аннотации:

1.  `@SpringBootConfiguration`: По сути, это псевдоним для `@Configuration`. Он помечает класс как источник определений бинов для контекста приложения.
2.  `@ComponentScan`: Указывает Spring сканировать компоненты (бины, сервисы, контроллеры и т.д.), начиная с пакета, в котором находится этот класс.
3.  **`@EnableAutoConfiguration`**: **Это и есть тот самый спусковой крючок!** Именно эта аннотация запускает весь механизм автоконфигурации.

### Шаг 1: Обнаружение "Кандидатов" через `spring.factories`

Как `@EnableAutoConfiguration` узнает, *что именно* нужно сконфигурировать? Она не содержит жестко закодированной логики. Вместо этого она использует старый, но надежный механизм **ServiceLoader** из JDK, реализованный в Spring через файл `spring.factories`.
@EnableAutoConfiguration импортирует класс EnableAutoConfigurationImportSelector. Этот класс не объявляет бины сам, а использует так называемые фабрики. Именно он смотрит в  в classpath

1.  **Поиск файла:** При старте приложения Spring ищет во всех JAR-файлах в classpath файл с путем `META-INF/spring.factories`.
2.  **Чтение "каталога":** Внутри этого файла находится простой список "ключ-значение". Нас интересует ключ `org.springframework.boot.autoconfigure.EnableAutoConfiguration`.

Заглянем, например, в `spring-boot-autoconfigure.jar`. Мы увидим там примерно такой `spring.factories`:

   ```properties
   # Auto Configuration Import Listeners
   org.springframework.boot.autoconfigure.AutoConfigurationImportListener=...

   # Auto Configuration Import Filters
   org.springframework.boot.autoconfigure.AutoConfigurationImportFilter=...

   # Auto Configure
   org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
   org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
   org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
   org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
   org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
   org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
   org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration,\
   org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
   org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration,\
   ... и еще около 200 классов ...
   ```
3.  **Формирование списка:** Spring собирает полный список всех классов, перечисленных под этим ключом, из всех `spring.factories` в classpath. Теперь у него есть огромный список **потенциальных классов автоконфигурации**.

### Шаг 2: Фильтрация и Принятие Решений через Условные Аннотации (`@Conditional...`)

Иметь список из 200+ кандидатов — это только полдела. Не все они нужны в каждом приложении. Теперь начинается самое интересное — **условная обработка**.

Каждый класс из этого списка (например, `DataSourceAutoConfiguration` или `WebMvcAutoConfiguration`) — это обычный `@Configuration` класс, но он "обвешан" условными аннотациями. Эти аннотации говорят Spring: "Применяй эту конфигурацию, только если выполнены определенные условия".

### Шаг 3: Создание Бинов

После того как Spring отфильтровал список и оставил только те классы автоконфигурации, чьи условия выполнены, он обрабатывает их как обычные `@Configuration` классы.

Он заходит внутрь каждого и выполняет их `@Bean` методы (которые, в свою очередь, тоже могут быть условными!), создавая и настраивая необходимые бины.


--------------------------------------------------------------------------------------------------------------------
Расскажи про Starter в Spring Boot?

Чтобы ускорить процесс управления зависимостями, Spring Boot неявно упаковывает необходимые сторонние зависимости для каждого типа приложения на основе Spring и предоставляет их разработчику посредством так называемых starter-пакетов. Starter-пакеты представляют собой набор удобных дескрипторов зависимостей, которые можно включить в свое приложение.

В техническом плане, это просто **`pom.xml` файл** (для Maven), который:
*   **Не содержит Java-кода.**
*   **Содержит список других, транзитивных зависимостей**, которые необходимы для работы определенной функциональности (веб, данные, безопасность).
*   **Предоставляет "мнение" (opinionated view)** о том, какие библиотеки и каких версий лучше всего работают вместе.

Когда вы добавляете один стартер, вы получаете целый, согласованный набор библиотек, избавляя себя от ручного управления зависимостями.

Стартеры сами по себе решают только проблему управления зависимостями. Их истинная сила раскрывается в связке с **Автоконфигурацией**.

Эта связка работает как идеально слаженный механизм:

1.  **Вы (Разработчик):** Говорите Spring Boot, **что** вы хотите сделать. Например: "Я хочу веб-приложение".
2.  **Стартер (`spring-boot-starter-web`):** Добавляет в ваш classpath **всё, что для этого нужно** (Spring MVC, Tomcat, Jackson и т.д.).
3.  **Автоконфигурация (`@EnableAutoConfiguration`):** **Реагирует** на появление этих библиотек в classpath. Она видит: "Ага, в classpath есть `DispatcherServlet`, значит, нужно настроить Spring MVC, создать встроенный Tomcat и зарегистрировать `ObjectMapper` для JSON".

> **Стартеры приносят "инструменты", а Автоконфигурация знает, как ими пользоваться.**

Без стартера Автоконфигурации не на что было бы реагировать. Без Автоконфигурации стартер был бы просто набором библиотек, которые вам все равно пришлось бы настраивать вручную.

---

### 5. Как создать свой собственный Стартер?

Это высший пилотаж, который показывает глубокое понимание системы. Создание своего стартера полезно, когда вы хотите легко интегрировать вашу внутреннюю библиотеку или часто используемый набор конфигураций в разные проекты.

**Правильная структура состоит из двух модулей:**

1.  **`my-library-spring-boot-autoconfigure`**: Модуль, содержащий **логику**.
2.  **`my-library-spring-boot-starter`**: Модуль, содержащий **зависимости**.

#### Шаг 1: Создаем модуль автоконфигурации (`autoconfigure`)

Это "мозг" нашего стартера.

1.  **Создайте `@Configuration` класс:**
    ```java
    @Configuration
    @ConditionalOnClass(MyLibraryClient.class) // Активировать, только если наш клиент есть в classpath
    @EnableConfigurationProperties(MyLibraryProperties.class) // Поддержка кастомных properties
    public class MyLibraryAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean // Создать, только если пользователь не создал свой бин
        public MyLibraryClient myLibraryClient(MyLibraryProperties properties) {
            // Логика создания клиента на основе properties
            return new MyLibraryClient(properties.getApiKey(), properties.getEndpoint());
        }
    }
    ```

2.  **Создайте класс для свойств:**
    ```java
    @ConfigurationProperties(prefix = "my.library")
    public class MyLibraryProperties {
        private String apiKey;
        private String endpoint = "https://api.example.com";
        // getters and setters
    }
    ```

3.  **Зарегистрируйте автоконфигурацию:** Это самый важный шаг. Создайте файл:
    `src/main/resources/META-INF/spring.factories`

    И впишите в него:
    ```properties
    org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    com.example.mylibrary.MyLibraryAutoConfiguration
    ```
    *Для Spring Boot 3+ рекомендуется новый формат в файле `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`:*
    ```
    com.example.mylibrary.MyLibraryAutoConfiguration
    ```

#### Шаг 2: Создаем модуль стартера (`starter`)

Это "обертка". Его `pom.xml` очень простой:

```xml
<dependencies>
    <!-- Главная зависимость - наш модуль с автоконфигурацией -->
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>my-library-spring-boot-autoconfigure</artifactId>
        <version>${project.version}</version>
    </dependency>

    <!-- Если наша библиотека требует чего-то еще, добавляем сюда -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
</dependencies>
```
Этот POM не содержит Java-кода. Он просто говорит: "Если вы хотите использовать `my-library`, вам нужен модуль с автоконфигурацией и, например, Jackson".

#### Шаг 3: Использование

Теперь в любом другом проекте разработчику достаточно добавить всего одну зависимость:
```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>my-library-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
И он сразу получит:
*   Все необходимые библиотеки.
*   Автоматически сконфигурированный бин `MyLibraryClient`.
*   Возможность настраивать его через `application.properties` (например, `my.library.api-key=...`).


--------------------------------------------------------------------------------------------------------------------

Что делает @Conditional?

Часто бывает полезно включить или отключить весь класс @Configuration, @Component или отдельные методы @Bean в зависимости от каких-либо условий.

Аннотация @Conditional это мета-аннотация, которая позволяет создавать более специализированные условные аннотации.
Она указывает, что компонент имеет право на регистрацию в контексте только тогда, когда все условия соответствуют.

Может применяться:

над классами прямо или косвенно аннотированными @Component, включая классы @Configuration;

над методами @Bean;

как мета-аннотация при создании наших собственных аннотаций-условий.

Условия проверяются непосредственно перед тем, как должно быть зарегистрировано BeanDefinition компонента, и они могут помешать регистрации данного BeanDefinition. Поэтому нельзя допускать, чтобы при проверке условий мы взаимодействовали с бинами (которых еще не существует), с их BeanDefinition-ами можно.

--------------------------------------------------------------------------------------------------------------------
Какие есть Conditional?


#### Условия, основанные на классах и бинах:

*   **`@ConditionalOnClass`**: Условие выполняется, если указанные классы **присутствуют** в classpath.
*   **`@ConditionalOnMissingClass`**: Условие выполняется, если указанные классы **отсутствуют** в classpath.
*   **`@ConditionalOnBean`**: Условие выполняется, если бин указанного типа (или с указанным именем) **уже существует** в контексте.
*   **`@ConditionalOnMissingBean`**: Условие выполняется, если бин указанного типа **еще не был определен** в контексте. Это ключевая аннотация для переопределения стандартного поведения.
*   **`@ConditionalOnSingleCandidate`**: Более строгая версия `@ConditionalOnBean`. Условие выполняется, если в контексте есть ровно один бин-кандидат указанного типа (и он помечен как основной, если есть несколько).

#### Условия, основанные на конфигурации и окружении:

*   **`@ConditionalOnProperty`**: Условие выполняется, если свойство в `Environment` (например, из `application.properties`) существует и/или имеет определенное значение. Очень гибкая: есть атрибуты `prefix`, `name`, `havingValue`, `matchIfMissing`.
*   **`@ConditionalOnExpression`**: Условие выполняется, если указанное SpEL-выражение (Spring Expression Language) возвращает `true`. Это самый мощный и гибкий вариант, позволяющий писать сложную логику.
    *   *Пример:* `@ConditionalOnExpression("${my.custom.flag:true} and ${spring.profiles.active}!='prod'")`

#### Условия, основанные на ресурсах:

*   **`@ConditionalOnResource`**: Условие выполняется, если указанный ресурс (файл) существует в classpath (например, `classpath:/my-config.xml`).

#### Условия, основанные на типе приложения:

*   **`@ConditionalOnWebApplication`**: Условие выполняется, если приложение является веб-приложением (т.е. использует `WebApplicationContext`). Можно уточнить тип: `SERVLET` или `REACTIVE`.
*   **`@ConditionalOnNotWebApplication`**: Противоположное условие. Для консольных приложений или обработчиков очередей.

#### Другие полезные условия:

*   **`@ConditionalOnJava`**: Условие выполняется, если приложение запущено на определенной версии Java.
*   **`@ConditionalOnJndi`**: Условие выполняется, если указанное JNDI-имя доступно.

---

 На каком этапе поднятия контекста они работают?

Это самый важный и тонкий момент. Условия проверяются не один раз, а на разных, четко определенных этапах жизненного цикла `ApplicationContext`.

Spring делит классы, помеченные `@Configuration`, на две большие группы:

1.  **Обычные конфигурации (`@Configuration`)**: Это ваши собственные конфигурационные классы, найденные через `@ComponentScan`.
2.  **Классы автоконфигурации (`Auto-configuration classes`)**: Это классы, импортированные через механизм `@EnableAutoConfiguration` (из `spring.factories`).

Логика проверки `@Conditional` для этих двух групп немного отличается по таймингу.

#### Этап 1: Парсинг обычных конфигураций

*   **Когда происходит:** Сразу после сканирования компонентов (`@ComponentScan`), но **до** обработки автоконфигурации.
*   **Что происходит:** Spring находит ваши классы, помеченные `@Configuration`. Он проверяет условные аннотации **на уровне класса**. Если условие на классе не выполняется, весь класс и все его `@Bean` методы немедленно отбрасываются и даже не рассматриваются дальше.
*   **Какие условия здесь работают?** В основном, те, что не зависят от бинов, которые еще не созданы. Это:
    *   `@ConditionalOnProperty`
    *   `@ConditionalOnClass`
    *   `@ConditionalOnResource`
    *   `@ConditionalOnExpression` (если выражение не ссылается на бины)
*   **Какие условия здесь могут вести себя неожиданно?** `@ConditionalOnBean` / `@ConditionalOnMissingBean` на этом этапе могут не сработать так, как вы ожидаете, потому что они проверяются на основе `BeanDefinition`'ов, а не созданных экземпляров, и полный список `BeanDefinition`'ов еще не сформирован (автоконфигурация не отработала).

#### Этап 2: Парсинг и импорт автоконфигураций

*   **Когда происходит:** Сразу после обработки ваших обычных конфигураций.
*   **Что происходит:**
    1.  Запускается механизм `@EnableAutoConfiguration`.
    2.  Spring получает полный список **кандидатов** автоконфигурации из `spring.factories`.
    3.  **Первый проход фильтрации:** Spring пробегается по этому списку и проверяет условные аннотации **на уровне класса**. На этом этапе он уже знает о ваших бинах, определенных на этапе 1. Классы, не прошедшие проверку, отбрасываются.
    4.  **Сортировка:** Оставшиеся классы автоконфигурации сортируются с помощью аннотаций `@AutoConfigureBefore`, `@AutoConfigureAfter`, `@AutoConfigureOrder`. Это очень важно! Например, `DataSourceAutoConfiguration` должна выполниться **до** `JpaRepositoriesAutoConfiguration`.
    5.  **Импорт:** Отсортированные и отфильтрованные классы автоконфигурации импортируются в контекст. Теперь их `@Bean` методы рассматриваются как потенциальные источники бинов.

#### Этап 3: Оценка условий на `@Bean`-методах

*   **Когда происходит:** После того как все конфигурации (и обычные, и автоконфигурации) были импортированы и их `BeanDefinition`'ы загружены в контекст. Это происходит на фазе **пост-обработки фабрики бинов** (`BeanFactoryPostProcessor`), но до инстанцирования самих бинов.
*   **Что происходит:** Теперь Spring проходит по всем `BeanDefinition`'ам, полученным из `@Bean`-методов (и ваших, и автоконфигурационных), и проверяет условные аннотации, которые стоят **непосредственно на этих методах**.
*   **Какие условия здесь работают?** На этом этапе Spring имеет **полную картину** всех `BeanDefinition`'ов. Поэтому здесь надежно работают **ВСЕ** типы условий, включая:
    *   `@ConditionalOnBean`
    *   `@ConditionalOnMissingBean`
    *   `@ConditionalOnSingleCandidate`
    

| Этап жизненного цикла | Что происходит | Какие условия надежно работают |
| :--- | :--- | :--- |
| **1. Парсинг пользовательских `@Configuration`** | Проверка `@Conditional` на **классах** из `@ComponentScan`. | `@ConditionalOnClass`, `@ConditionalOnProperty`, `@ConditionalOnResource` и т.д. |
| **2. Парсинг автоконфигураций** | Проверка `@Conditional` на **классах** из `spring.factories`. | Те же, что и на этапе 1. Позволяет отсечь целые модули автоконфигурации. |
| **3. Пост-обработка `BeanFactory`** | Проверка `@Conditional` на **`@Bean`-методах** внутри всех (и ваших, и авто) конфигураций. | **ВСЕ**, включая `@ConditionalOnBean` и `@ConditionalOnMissingBean`, так как виден полный список "чертежей" бинов. |
| **4. Инстанцирование бинов** | Условия уже отработали. Spring просто создает экземпляры на основе оставшихся `BeanDefinition`'ов. | - |

--------------------------------------------------------------------------------------------------------------------
Как создать свой Conditional?


### 1. Создать класс с логикой (`Condition`)

Создайте класс, который реализует интерфейс `Condition`. Вся логика проверки помещается в метод `matches()`.

```java
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

// Класс с логикой: будет ли создан бин?
public class MyCustomCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Ваша логика здесь. Например, проверяем системное свойство:
        String myFlag = System.getProperty("my.flag");
        return "true".equalsIgnoreCase(myFlag); // вернет true, если флаг установлен в "true"
    }
}
```

### 2. Создать свою аннотацию

Создайте аннотацию и свяжите ее с вашим классом-условием с помощью мета-аннотации `@Conditional`.

```java
import org.springframework.context.annotation.Conditional;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD}) // Где можно использовать
@Retention(RetentionPolicy.RUNTIME)           // Доступна в рантайме
@Conditional(MyCustomCondition.class)         // <-- Связываем с логикой
public @interface ConditionalOnMyFlag {
}
```

### 3. Использовать аннотацию

Теперь просто используйте вашу новую аннотацию на бинах или конфигурациях.

```java
@Configuration
public class MyConfig {

    @Bean
    @ConditionalOnMyFlag // <-- Применяем наше условие
    public MyService myService() {
        return new MyService(); // Этот бин будет создан, только если java -Dmy.flag=true
    }
}
```

--------------------------------------------------------------------------------------------------------------------

Что такое parent в spring?


**`<parent>` в Maven `pom.xml` — это механизм наследования.** Он позволяет дочернему POM-файлу наследовать конфигурацию от родительского POM-файла.

Когда вы создаете проект в Spring Boot, вы почти всегда видите этот блок:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.5</version>
    <relativePath/> <!-- Означает, что родительский POM нужно искать в репозитории, а не локально -->
</parent>
```
Это означает, что ваш проект является "ребенком" проекта `spring-boot-starter-parent`.

### Что именно ваш проект наследует от `spring-boot-starter-parent`?

Вот три главные "суперсилы", которые дает этот `parent`:

#### 1. Централизованное управление версиями (через BOM)

Это **самая важная** функция. `spring-boot-starter-parent` сам по себе наследуется от `spring-boot-dependencies`. Этот `spring-boot-dependencies` — это и есть тот самый **BOM (Bill of Materials)**, о котором мы говорили.

*   **Что это дает?** Ваш проект получает "в наследство" огромную секцию `<dependencyManagement>`, в которой прописаны версии для сотен популярных библиотек (Spring Framework, Jackson, Hibernate, Tomcat и т.д.), которые были тщательно протестированы на совместимость командой Spring.
*   **Результат:** Вы можете добавлять зависимости в свой проект, **не указывая версию**. Maven автоматически возьмет ее из родительского BOM.

    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <!-- Версия не нужна! Она унаследована от parent. -->
    </dependency>
    ```

#### 2. Разумные настройки по умолчанию (Sensible Defaults)

`parent` устанавливает множество полезных настроек по умолчанию, избавляя вас от рутины:

*   **Версия Java:** По умолчанию устанавливается Java 1.8 (в старых версиях) или Java 17 (в новых, начиная с Boot 3.x). Вам не нужно прописывать `maven-compiler-plugin` с указанием версии.
*   **Кодировка исходных файлов:** Устанавливается в `UTF-8`.
*   **Фильтрация ресурсов:** Настраивается обработка файлов в `src/main/resources`, например, для подстановки версий из `pom.xml` (например, `@project.version@`).

#### 3. Конфигурация плагинов

`parent` заранее настраивает ключевые плагины Maven для работы со Spring Boot.

*   **`spring-boot-maven-plugin`**: Самый главный плагин. Он настраивается в `parent`'e. Его задача — упаковать ваше приложение в **исполняемый "fat" JAR-файл**. Этот JAR содержит не только ваш код, но и все зависимости, а также встроенный веб-сервер (Tomcat). Именно благодаря этому плагину вы можете запустить приложение простой командой `java -jar myapp.jar`.


--------------------------------------------------------------------------------------------------------------------
### Как работают Embedded Server?

Вместо того чтобы вы деплоили свой код на сервер, **сервер теперь деплоится внутрь вашего кода**.

Приложение собирается в один исполняемый JAR-файл, который можно запустить командой `java -jar myapp.jar`.

**Анатомия "Fat JAR":**
Если вы распакуете такой JAR-файл, вы увидите интересную структуру:

```
myapp.jar
|
+-- BOOT-INF/
|   +-- classes/  <-- Ваш скомпилированный код (.class файлы)
|   |   +-- com/
|   |       +-- example/
|   |           +-- MyApplication.class
|   |           +-- MyController.class
|   |
|   +-- lib/      <-- Все ваши зависимости, включая сам сервер!
|   |   +-- spring-boot-starter-web-2.7.5.jar
|   |   +-- tomcat-embed-core-9.0.68.jar  <-- Вот он, встроенный Tomcat!
|   |   +-- spring-webmvc-5.3.23.jar
|   |   +-- ... (десятки других .jar)
|
+-- META-INF/
|   +-- MANIFEST.MF <-- Манифест, указывающий на специальный загрузчик
|
+-- org/
    +-- springframework/
        +-- boot/
            +-- loader/
                +-- JarLauncher.class <-- Специальный класс-загрузчик!
                +-- ...
```

**Ключевые моменты:**
*   **Вложенные JAR'ы:** Стандартный `ClassLoader` в Java не умеет загружать классы из JAR-файлов, которые лежат внутри другого JAR-файла.
*   **`JarLauncher`:** Spring Boot решает эту проблему с помощью своего кастомного загрузчика. Когда вы запускаете `java -jar myapp.jar`, `MANIFEST.MF` указывает JVM запустить не ваш `main` метод, а `org.springframework.boot.loader.JarLauncher`.
*   **Кастомный `ClassLoader`:** `JarLauncher` создает специальный `ClassLoader`, который "знает" о структуре `BOOT-INF/lib/` и умеет читать классы из вложенных JAR'ов.
*   **Вызов вашего `main`:** После настройки `ClassLoader`'а, `JarLauncher` находит ваш главный класс (указанный в манифесте) и вызывает его `main` метод.

---

### 3. Как Spring Boot запускает сервер? 

Теперь, когда приложение запущено и классы доступны, как именно стартует Tomcat?

Этот процесс является частью **автоконфигурации** Spring Boot.

1.  **Обнаружение:** При старте контекста `AutoConfiguration` видит, что в classpath есть `spring-boot-starter-web` и, следовательно, классы `Servlet.class` и `Tomcat.class`.
2.  **Активация `ServletWebServerFactoryAutoConfiguration`:** Это триггерит соответствующую автоконфигурацию.
3.  **Создание Фабрики:** Внутри этой конфигурации есть условные бины. Сработает тот, для которого есть класс в classpath. Например:
    ```java
    @Configuration
    @ConditionalOnClass(Tomcat.class)
    class TomcatServletWebServerFactoryConfiguration {
        @Bean
        public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
            return new TomcatServletWebServerFactory();
        }
    }
    ```
    Spring Boot создает бин `TomcatServletWebServerFactory`. Это еще не сам сервер, а **фабрика**, которая знает, как создать и настроить экземпляр Tomcat. Если бы в classpath был Jetty, была бы создана `JettyServletWebServerFactory`.

---

### 4. Жизненный цикл: От `main()` до обработки запроса

Давайте проследим всю цепочку:

1.  `java -jar myapp.jar` -> `JarLauncher` запускается.
2.  `JarLauncher` настраивает `ClassLoader` и вызывает `MyApplication.main(args)`.
3.  `SpringApplication.run(MyApplication.class, args)` запускает создание `ApplicationContext`.
4.  В процессе создания контекста срабатывает **автоконфигурация**:
    *   Создается бин `DispatcherServlet` (центральный сервлет Spring MVC).
    *   Создается бин `TomcatServletWebServerFactory`.
5.  **Ключевой момент:** После того как все бины созданы и контекст почти готов (`refresh()`), Spring находит все бины типа `ServletWebServerFactory`.
6.  Он вызывает у этой фабрики метод `getWebServer(ServletContextInitializer... initializers)`. В качестве `initializers` передается, в том числе, `DispatcherServlet`.
7.  **Фабрика выполняет свою работу:**
    *   `TomcatServletWebServerFactory` создает экземпляр `org.apache.catalina.startup.Tomcat`.
    *   Она программно настраивает его: устанавливает порт (из `server.port` в `application.properties`), контекстный путь, обработчики ошибок и т.д.
    *   Она регистрирует `DispatcherServlet` в созданном экземпляре Tomcat.
    *   Вызывает `tomcat.start()`. **Сервер запущен и слушает порт!**
8.  Приложение полностью запущено. Когда приходит HTTP-запрос, встроенный Tomcat принимает его и, согласно своей конфигурации, передает его на обработку в `DispatcherServlet`, который дальше направляет его в ваш `@RestController`.
-----------------------------------------------------------------------------------------------------------