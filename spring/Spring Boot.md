Что такое Spring Boot

По сути, Spring Boot это просто набор классов конфигурации, которые создают нужные бины в контексте. Точно так же их можно создать руками, просто Boot это автоматизирует. При этом помогая решить проблему конфликтов разных версий компонентов.
Чтобы ускорить процесс управления зависимостями, Spring Boot неявно упаковывает необходимые сторонние зависимости для каждого типа приложения на основе Spring и предоставляет их разработчику посредством так называемых starter-пакетов (spring-boot-starter-web, spring-boot-starter-data-jpa и т.д.)

--------------------------------------------------------------------------------------------------------------------

Starter-пакеты в Spring Boot

Starter-пакеты представляют собой набор удобных дескрипторов зависимостей, которые можно включить в свое приложение. Это позволит получить универсальное решение для всех, связанных со Spring технологий, избавляя программиста от лишнего поиска примеров кода и загрузки из них требуемых дескрипторов зависимостей. Например, если вы хотите начать использовать Spring Data JPA для доступа к базе данных, просто включите в свой проект зависимость spring-boot-starter-data-jpa и все будет готово (вам не придется искать совместимые драйверы баз данных и библиотеки Hibernate)

Например, если вы добавите Spring-boot-starter-web, Spring Boot автоматически сконфигурирует такие зарегистрированные бины, как DispatcherServlet, ResourceHandlers, MessageSource. Если вы используете spring-boot-starter-jdbc, Spring Boot автоматически регистрирует бины DataSource, EntityManagerFactory, TransactionManager и считывает информацию для подключения к базе данных из файла application.properties

--------------------------------------------------------------------------------------------------------------------

В чем заключается магия Spring Boot

В основе "магии" Spring Boot нет ничего магического, он использует совершенно базовые понятия из Spring Framework. В кратком виде процесс можно описать так:

Аннотация @SpringBootApplication включает сканирование компонентов и авто-конфигурацию через аннотацию @EnableAutoConfiguration

@EnableAutoConfiguration импортирует класс EnableAutoConfigurationImportSelector

EnableAutoConfigurationImportSelector загружает список конфигураций из файла META-INF/spring.factories

Каждая конфигурация пытается сконфигурить различные аспекты приложения (web, JPA, AMQP etc), регистрируя нужные бины и используя различные условия (наличие / отсутствие бина, настройки, класса и т.п.)

Созданный в итоге AnnotationConfigEmbeddedWebApplicationContext ищет в том же DI контейнере фабрику для запуска embedded servlet container

Servlet container запускается, приложение готово к работе!

--------------------------------------------------------------------------------------------------------------------

Автоконфигурация в Spring Boot

По сути, это просто набор конфигурационных классов, которые создают и регистрируют определенные бины в приложении. По большому счету, даже сам Embedded Servlet Container — это просто еще один бин, который можно сконфигурировать! Пара важных моментов, которые важно знать об автоконфигурации:

Включается аннотацией @EnableAutoConfiguration

Работает в последнюю очередь, после регистрации пользовательских бинов

Принимает решения о конфигурации на основании доступных в classpath классов, свойств в application.properties и т.п.

Можно включать и выключать разные аспекты автоконфигурации, и применять ее частично (например, только MySQL + JPA, но не веб)

Всегда отдает приоритет пользовательским бинам.
Если ваш код уже зарегистрировал бин DataSource — автоконфигурация не будет его перекрывать

Логика при регистрации бинов управляется набором @ConditionalOn* аннотаций. Можно указать, чтобы бин создавался при наличии класса в classpath (@ConditionalOnClass), наличии существующего бина (@ConditionalOnBean), отсуствии бина (@ConditionalOnMissingBean) и т.п.

Отключить ненужные автоконфигурации можно при помощи свойств exclude и excludeName аннотаций @EnableAutoConfiguration, @ImportAutoConfiguration и @SpringBootApplication. Или в property задать SpringAutoconfiguration exclude и передать имена классов.

Можно отказаться от использования механизма автоконфигурации, вместо этого указывая необходимые автоконфигурации вручную. Для этого надо избавиться от аннотаций @SpringBootApplication и @EnableAutoConfiguration в коде вашего проекта, а для указания нужных конфигурационных классов использовать аннотации @SpringBootConfiguration и @ImportAutoConfiguration. Однако стоит помнить, что используемые автоконфигурации всё ещё могут содержать неиспользуемые компоненты.

--------------------------------------------------------------------------------------------------------------------

Как происходит автоконфигурация в Spring Boot:

1) Отмечаем main класс аннотацией @SpringBootApplication (аннотация инкапсулирует в себе:@SpringBootConfiguration, @ComponentScan, @EnableAutoConfiguration), таким образом наличие @SpringBootApplication включает сканирование компонентов, автоконфигурацию и показывает разным компонентам Spring (например, интеграционным тестам), что это Spring Boot приложение.
   @SpringBootApplication public class DemoApplication { public static void main(String[] args) { SpringApplication.run(DemoApplication.class, args); } }

2) @EnableAutoConfiguration импортирует класс EnableAutoConfigurationImportSelector. Этот класс не объявляет бины сам, а использует так называемые фабрики.

3) Класс EnableAutoConfigurationImportSelector смотрит в файл META-INF/spring.factories и загружает оттуда список значений, которые являются именами классов (авто)конфигураций, которые Spring Boot импортирует. Т.е. аннотация @EnableAutoConfiguration просто импортирует ВСЕ (более 150) перечисленные в spring.factories конфигурации, чтобы предоставить нужные бины в контекст приложения.

4) Каждая из этих конфигураций пытается сконфигурировать различные аспекты приложения(web, JPA, AMQP и т.д.), регистрируя нужные бины. Логика при регистрации бинов управляется набором @ConditionalOn* аннотаций. Можно указать, чтобы бин создавался при наличии класса в classpath (@ConditionalOnClass), наличии существующего бина (@ConditionalOnBean), отсуствии бина (@ConditionalOnMissingBean) и т.п. Таким образом наличие конфигурации не значит, что бин будет создан, и в большинстве случаев конфигурация ничего делать и создавать не будет.

5) Созданный в итоге AnnotationConfigEmbeddedWebApplicationContext ищет в том же DI контейнере фабрику для запуска embedded servlet container.
6) Servlet container запускается, приложение готово к работе!

--------------------------------------------------------------------------------------------------------------------

Как создать свой Starter packs

Чтобы ускорить процесс управления зависимостями, Spring Boot неявно упаковывает необходимые сторонние зависимости для каждого типа приложения на основе Spring и предоставляет их разработчику посредством так называемых starter-пакетов. Starter-пакеты представляют собой набор удобных дескрипторов зависимостей, которые можно включить в свое приложение.

Делаем свой Starter-пакет:

1) Создаем AutoConfiguration-класс, который Spring Boot находит при запуске приложения и использует для автоматического создания и конфигурирования бинов.

@Configuration
//указываем, что наш класс является конфигурацией

@ConditionalOnClass({SocialConfigurerAdapter.class, VKontakteConnectionFactory.class})
//означает, что бины будут создаваться при наличии в classpath SocialConfigurerAdapter и VKontakteConnectionFactory. Таким образом, без нужных для нашего стартера зависимостей бины создаваться не будут.

@ConditionalOnProperty(prefix= "ru.shadam.social-vkontakte", name = { "client-id", "client-secret"})
//означает, что бины будут создаваться только при наличии property ru.shadam.social-vkontakte.client-id и ru.shadam.social-vkontakte.client-secret.

@AutoConfigureBefore(SocialWebAutoConfiguration.class)

@AutoConfigureAfter(WebMvcAutoConfiguration.class)
//означает, что наш бин будет инициализироваться после WebMvc и до SocialWeb. Это нужно, чтобы к моменту инициализации SocialWeb наши бины уже были зарегистрированы.

public class VKontakteAutoConfiguration { }

2) Расширяем SocialConfigurationAdapter, который нужен для того чтобы зарегистрировать нашу ConnectionFactory. Для этого переопределяем метод addConnectionFactories(ConnectionFactoryConfigurer, Environment)

3) Создание файла, позволяющего SpringBoot найти наш AutoConfiguration класс. Для этого существует специальный файл spring.factories, который нужно поместить в META-INF папку получающегося jar-файла. В этом файле нам надо указать наш AutoConfiguration-класс.

4) Подключить получившийся jar-файл к Spring Boot проекту и задать в конфигурации пути на класс, который создали до этого.

--------------------------------------------------------------------------------------------------------------------

Что делает @Conditional

Часто бывает полезно включить или отключить весь класс @Configuration, @Component или отдельные методы @Bean в зависимости от каких-либо условий.

Аннотация @Conditional указывает, что компонент имеет право на регистрацию в контексте только тогда, когда все условия соответствуют.
Может применяться:

над классами прямо или косвенно аннотированными @Component, включая классы @Configuration;

над методами @Bean;

как мета-аннотация при создании наших собственных аннотаций-условий.

Условия проверяются непосредственно перед тем, как должно быть зарегистрировано BeanDefinition компонента, и они могут помешать регистрации данного BeanDefinition. Поэтому нельзя допускать, чтобы при проверке условий мы взаимодействовали с бинами (которых еще не существует), с их BeanDefinition-ами можно.

Условия мы определяем в специально создаваемых нами классах, которые должны имплементировать функциональный интерфейс Condition с одним единственным методом, возвращающим true или false:
boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata)

Создав свой класс и переопределив в нем метод matches() с нашей логикой, мы должны передать этот класс в аннотацию @Conditional в качестве параметра:

@Configuration
@Conditional(OurConditionClass.class)
class MySQLAutoconfiguration { //... }
//Для того, чтобы проверить несколько условий, можно передать в @Conditional несколько классов с условиями:

@Bean
@Conditional(HibernateCondition.class, OurConditionClass.class)
Properties additionalProperties() { //... }

Если класс @Configuration помечен как @Conditional, то на все методы @Bean, аннотации @Import и аннотации @ComponentScan, связанные с этим классом, также будут распространяться указанные условия.
Для более детальной настройки классов, аннотированных @Configuration, предлагается использовать интерфейс ConfigurationCondition.
В одном классе - одно условие. Для создания более сложных условий можно использовать классы AnyNestedCondition, AllNestedConditions и NoneNestedConditions.
В Spring Framework имеется множество готовых аннотаций (и связанных с ними склассами-условиями, имплементирующими интерфейс Condition), которые можно применять совместно над одним определением бина:
ConditionalOnBean Условие выполняется, в случае если присутствует нужный бин в BeanFactory. ConditionalOnClass Условие выполняется, если нужный класс есть в classpath.
ConditionalOnCloudPlatform Условие выполняется, когда активна определенная платформа. ConditionalOnExpression Условие выполняется, когда SpEL выражение вернуло положительное значение. ConditionalOnJava Условие выполняется, когда приложение запущено с определенной версией JVM. ConditionalOnJndi Условие выполняется, только если через JNDI доступен определенный ресурс. ConditionalOnMissingBean Условие выполняется, в случае если нужный бин отсутствует в контейнере. ConditionalOnMissingClass Условие выполняется, если нужный класс отсутствует в classpath. ConditionalOnNotWebApplication Условие выполняется, если контекст приложения не является веб контекстом. ConditionalOnProperty Условие выполняется, если в файле настроек заданы нужные параметры. ConditionalOnResource Условие выполняется, если присутствует нужный ресурс в classpath. ConditionalOnSingleCandidate Условие выполняется, если bean-компонент указанного класса уже содержится в контейнере и он единственный. ConditionalOnWebApplication Условие выполняется, если контекст приложения является веб контекстом.

--------------------------------------------------------------------------------------------------------------------

Как внедрить java.util.Properties в Spring Bean

Используя SpEL
@Value("${maxReadResults}") private int maxReadResults;
Или определить propertyConfigure bean в XML.

--------------------------------------------------------------------------------------------------------------------

Что нового в Spring 5

Используется JDK 8+ (Optional, CompletableFuture, Time API, java.util.function, default methods)

Поддержка Java 9 (Automatic-Module-Name in 5.0, module-info in 6.0+, ASM 6)

Поддержка HTTP/2 (TLS, Push), NIO/NIO.2, Kotlin

Поддержка Kotlin

Реактивность (Web on Reactive Stack)

Null-safety аннотации(@Nullable), новая документация

Совместимость с Java EE 8 (Servlet 4.0, Bean Validation 2.0, JPA 2.2, JSON Binding API 1.0)

Поддержка JUnit 5 + Testing Improvements (conditional and concurrent)

Удалена поддержка: Portlet, Velocity, JasperReports, XMLBeans, JDO, Guava

--------------------------------------------------------------------------------------------------------------------