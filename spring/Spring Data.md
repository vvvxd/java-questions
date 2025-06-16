Что такое Spring Data?

Spring Data — это модуль Spring Framework, который упрощает доступ к данным в различных хранилищах (реляционные базы, NoSQL, JPA и т.д.), предоставляя унифицированный API и автоматическую реализацию операций. Он минимизирует написание бойлерплейт-кода для CRUD, запросов, пагинации и других задач.

Для каждого типа хранилища Spring Data использует специфичные API:
   JPA: Под капотом работает EntityManager из Hibernate или другого провайдера JPA. Spring Data генерирует JPQL-запросы.
   MongoDB: Используется MongoTemplate для операций с MongoDB.
   Cassandra/Redis/Elasticsearch: Аналогично, через свои шаблоны (CassandraTemplate, RedisTemplate и т.д.).
Spring Data абстрагирует различия между хранилищами, предоставляя единый интерфейс

------------------
Что такое Spring Repository?

В Spring Data существует иерархия интерфейсов репозиториев, которые предоставляют функциональность для работы с данными. Эти интерфейсы можно разделить на базовые, расширенные и специфичные для определенных хранилищ.

### 1. Базовые интерфейсы репозиториев
Эти интерфейсы являются основой для всех модулей Spring Data и предоставляют общие методы для работы с данными.

- **Repository<T, ID>**:
   - Маркерный интерфейс, не содержит методов.
   - Используется как базовый для всех репозиториев.
   - Служит для обозначения интерфейса как репозитория, чтобы Spring Data мог его обработать.
   - Пример:
     ```java
     public interface UserRepository extends Repository<User, Long> {}
     ```

- **CrudRepository<T, ID>**:
   - Расширяет `Repository`.
   - Предоставляет стандартные CRUD-операции (Create, Read, Update, Delete).
   - Основные методы:
      - `save(T entity)` — сохраняет сущность.
      - `findById(ID id)` — поиск по идентификатору.
      - `findAll()` — получение всех сущностей.
      - `delete(T entity)` — удаление сущности.
      - `count()` — подсчет количества записей.
   - Пример:
     ```java
     public interface UserRepository extends CrudRepository<User, Long> {}
     ```

- **PagingAndSortingRepository<T, ID>**:
   - Расширяет `CrudRepository`.
   - Добавляет поддержку пагинации и сортировки.
   - Основные методы:
      - `findAll(Pageable pageable)` — возвращает страницу данных с учетом пагинации и сортировки.
      - `findAll(Sort sort)` — возвращает данные с сортировкой.
   - Пример:
     ```java
     public interface UserRepository extends PagingAndSortingRepository<User, Long> {
         Page<User> findAll(Pageable pageable);
     }
     ```
     
### 2. Специфичные интерфейсы для JPA
Эти интерфейсы предназначены для работы с реляционными базами данных через JPA (Java Persistence API).

- **JpaRepository<T, ID>**:
   - Расширяет `PagingAndSortingRepository`.
   - Добавляет методы, специфичные для JPA, такие как:
      - `saveAndFlush(T entity)` — сохраняет сущность и сразу синхронизирует с базой.
      - `deleteInBatch(Iterable<T> entities)` — удаляет коллекцию сущностей в одном запросе.
      - `flush()` — принудительно синхронизирует изменения с базой данных.
   - Автоматически включает транзакции для большинства операций.
   - Пример:
     ```java
     public interface UserRepository extends JpaRepository<User, Long> {}
     ```

- **JpaSpecificationExecutor<T>**:
   - Не расширяет другие интерфейсы, используется отдельно или в комбинации (например, с `JpaRepository`).
   - Поддерживает динамические запросы через `Specification` (Criteria API).
   - Основные методы:
      - `findAll(Specification<T> spec)` — поиск по спецификации.
      - `findAll(Specification<T> spec, Pageable pageable)` — пагинация с фильтрацией.
   - Пример:
     ```java
     public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
         List<User> findAll(Specification<User> spec);
     }
     ```

### 3. Репозитории для NoSQL и других хранилищ
Spring Data предоставляет специализированные интерфейсы для работы с NoSQL-базами и другими хранилищами. Они обычно расширяют `CrudRepository` или `PagingAndSortingRepository` и добавляют специфичные методы.

- **MongoRepository<T, ID>** (для MongoDB):

- **CassandraRepository<T, ID>** (для Apache Cassandra):

- **RedisRepository<T, ID>** (для Redis):

- **ElasticsearchRepository<T, ID>** (для Elasticsearch):

- **Neo4jRepository<T, ID>** (для Neo4j):

- **ReactiveCrudRepository<T, ID>**:

- **CoroutinesCrudRepository<T, ID>** (для Kotlin):

------------------
Имени метода Spring Data?

Запросы к сущности можно строить прямо из имени метода. Для этого используется механизм префиксов find...By, read...By, query...By, count...By, и get...By, далее от префикса метода начинает разбор остальной части. Вводное предложение может содержать дополнительные выражения, например, Distinct. Далее первый By действует как разделитель, чтобы указать начало фактических критериев. Можно определить условия для свойств сущностей и объединить их с помощью And и Or.

В документации определен весь перечень, и правила написания метода. В качестве результата могут быть сущность T, Optional, List, Stream

------------------
Как Spring создает автоматическую реализацию для репозиториев?


Spring Data создаёт реализацию интерфейса репозитория через **динамические прокси**, что позволяет избежать написания явных классов-реализаций. Давайте разберём этот процесс поэтапно:

#### Инициализация контекста и сканирование репозиториев

1. **Конфигурация**:
    - При использовании аннотации `@EnableJpaRepositories` (или аналогичных для других модулей) Spring регистрирует `RepositoryBeanDefinitionRegistrar`, который сканирует указанные пакеты на наличие интерфейсов, наследующихся от `Repository` (или его подинтерфейсов, таких как `CrudRepository`).
    - Spring также регистрирует `RepositoryFactoryBean`, который отвечает за создание bean’ов для репозиториев.

2. **Обнаружение интерфейсов**:
    - Spring использует `ClassPathScanningCandidateComponentProvider` для поиска интерфейсов, помеченных как репозитории. Интерфейсы должны соответствовать критериям (например, наследоваться от `Repository` или быть аннотированы `@Repository`).
    - Для каждого найденного интерфейса создаётся `BeanDefinition`, описывающий, как создать bean.

3. **Создание фабрики репозиториев**:
    - Для каждого модуля Spring Data существует своя реализация `RepositoryFactory` (например, `JpaRepositoryFactory` для JPA или `MongoRepositoryFactory` для MongoDB).
    - Фабрика анализирует интерфейс репозитория, извлекает метаданные (сущность, тип идентификатора, пользовательские методы) и создаёт прокси.

#### Создание динамического прокси

Spring Data использует **AOP** (аспектно-ориентированное программирование) для создания прокси-объекта, который реализует интерфейс репозитория. Процесс выглядит так:

1. **Выбор механизма прокси**:
    - Если интерфейс репозитория является чистым Java-интерфейсом, используется `java.lang.reflect.Proxy` (JDK Dynamic Proxy).
    - Если интерфейс расширяет другой интерфейс или требуется более сложная логика, используется библиотека **CGLIB** для создания прокси на основе подкласса.

2. **Интерцепторы**:
    - Прокси делегирует вызовы методов объекту `InvocationHandler` (для JDK Proxy) или `MethodInterceptor` (для CGLIB).
    - Основной интерцептор — это реализация `RepositoryProxyPostProcessor`, который определяет, как обрабатывать вызовы методов.

3. **Обработка методов**:
    - Прокси различает три типа методов:
        - **Методы из базовых интерфейсов** (например, `save`, `findById` из `CrudRepository`): Делегируются стандартной реализации (например, `SimpleJpaRepository`).
        - **Методы с именами по конвенции** (`findByLastName`): Парсятся для создания запросов.
        - **Пользовательские методы**: Выполняются через пользовательскую реализацию (если она есть) или через аннотации вроде `@Query`.

#### Метаданные репозитория

Spring Data собирает метаданные для каждого репозитория с помощью класса `RepositoryMetadata`:

- **Тип сущности**: Например, `User` в `UserRepository<User, Long>`.
- **Тип идентификатора**: Например, `Long`.
- **Методы**: Список методов интерфейса, включая их имена, параметры и возвращаемые типы.
- **Аннотации**: Например, `@Query`, `@Transactional`, `@Transactional`.

Эти метаданные хранятся в `RepositoryInformation` и используются для создания запросов и проверки корректности интерфейса.

#### Парсинг имен методов

Spring Data использует класс `PartTree` для анализа имен методов. Процесс следующий:

1. **Разбиение имени метода**:
    - Имя метода разбивается на части по camelCase. Например, `findByLastNameAndFirstName` разбивается на:
        - Префикс: `find`
        - Условия: `LastName`, `And`, `FirstName`

2. **Определение структуры запроса**:
    - **Префикс** определяет тип операции (`find`, `count`, `delete`).
    - **Условия** сопоставляются с полями сущности (используя рефлексию или метаданные Hibernate/MongoDB).
    - **Модификаторы** (например, `OrderBy`, `Distinct`) добавляют дополнительную логику.

3. **Генерация запроса**:
    - Для JPA создаётся JPQL-запрос (или SQL для нативных запросов).
    - Для MongoDB создаётся запрос в формате MongoDB` (например, `{ "lastName": "Doe"}`).
    - Для других хранилищ используются аналогичные форматы.

4. **Кэширование**:
    - Результаты парсинга имен методов кэшируются в `QueryMethod` для оптимизации повторных вызовов.

#### 2.5. Выполнение запросов

После создания запроса, Spring Data передает его в **Query Executor**, который зависит от хранилища:

- **JPA**:
    - Используется `EntityManager` для выполнения JPQL или SQL.
    - Результаты автоматически маппятся на сущности с помощью Hibernate (или другого JPA-провайдера).
    - Поддерживает ленивую загрузку (lazy loading) и кэширование первого уровня.

- **MongoDB**:
    - Используется `MongoTemplate` или `Template` или `MongoOperations`.
    - Запросы преобразуются в BSON и отправляются в MongoDB.
    - Результаты маппятся на POJO с помощью `MongoConverter`.

- **Cassandra**:
    - Используется `CassandraTemplate`.
    - Запросы преобразуются в CQL (Cassandra Query Language).

- **Redis**:
    - Используется `RedisTemplate`.
    - Запросы преобразуются в команды Redis (например, `GET`, `SET`).

------------------
Как происходит превращение методов репозитория в sql запросы и как они выполняються?

Когда приложение Spring стартует, аннотация `@EnableJpaRepositories` инициирует процесс сканирования пакетов для поиска интерфейсов, наследующихся от `Repository` (или его подинтерфейсов, таких как `JpaRepository`). Для каждого интерфейса создается bean с помощью `JpaRepositoryFactory`.

- **Метаданные**: Spring Data собирает метаданные через класс `RepositoryMetadata`:
    - Тип сущности (например, `User`).
    - Тип идентификатора (например, `Long`).
    - Список методов интерфейса (их имена, параметры, возвращаемые типы, аннотации).
- Эти метаданные используются для дальнейшего анализа методов.

#### 2.2. Парсинг имени метода

Spring Data использует класс `PartTree` для анализа имен методов, если они следуют конвенции именования (например, `findByLastName`). Процесс включает:

1. **Разбиение имени метода**:
    - Имя метода разбивается на части по camelCase. Например, метод `findByLastNameAndAgeGreaterThan` разбирается на:
        - Префикс: `find` (указывает на выборку).
        - Условия: `LastName`, `And`, `AgeGreaterThan`.
    - Поддерживаемые префиксы: `find`, `read`, `query`, `count`, `delete`, `exists`, и т.д.
    - Поддерживаемые ключевые слова: `And`, `Or`, `GreaterThan`, `LessThan`, `Like`, `IsNull`, `OrderBy`, и т.д.

2. **Создание структуры `PartTree`**:
    - `PartTree` представляет имя метода как дерево условий. Например:
        - Для `findByLastNameAndAgeGreaterThan` создается дерево с двумя узлами (`Part`):
            - `LastName` (оператор `=`).
            - `AgeGreaterThan` (оператор `>`).
        - Узлы объединяются через `And`.
    - Каждый узел (`Part`) связан с полем сущности (например, `lastName` или `age`) и оператором сравнения.

3. **Валидация**:
    - Spring Data проверяет, что поля (например, `lastName`) существуют в сущности, используя метаданные JPA (полученные через `EntityManager` или рефлексию).
    - Если поле отсутствует или метод нарушает конвенцию, выбрасывается исключение `InvalidDataAccessApiUsageException`.

Пример:
```java
List<User> findByLastNameAndAgeGreaterThan(String lastName, int age);
```
- Парсер извлекает:
    - Префикс: `find` → выборка.
    - Условия: `LastName = ?1` и `Age > ?2`.
    - Логический оператор: `And`.
- Результат: JPQL-запрос вида `SELECT u FROM User u WHERE u.lastName = ?1 AND u.age > ?2`.

#### 2.3. Генерация JPQL

После парсинга имени метода Spring Data создает JPQL-запрос с использованием класса `QueryMethod`. Вот как это происходит:

1. **Определение типа запроса**:
    - Префикс метода определяет тип запроса:
        - `find`, `read`, `query` → `SELECT`.
        - `count` → `SELECT COUNT`.
        - `delete`, `remove` → `DELETE`.
        - `exists` → `SELECT EXISTS`.

2. **Построение условий**:
    - Каждый узел `Part` из `PartTree` преобразуется в условие JPQL:
        - `LastName` → `u.lastName = :lastName`.
        - `AgeGreaterThan` → `u.age > :age`.
    - Условия объединяются через `AND`, `OR` и т.д.

3. **Учет модификаторов**:
    - Модификаторы, такие как `Distinct`, `Top`, `First`, `OrderBy`, добавляются к запросу:
        - `findDistinctByLastName` → `SELECT DISTINCT u FROM User u`.
        - `findTop10ByLastNameOrderByAgeDesc` → `SELECT u FROM User u ... ORDER BY u.age DESC` с лимитом 10.

4. **Обработка параметров**:
    - Параметры метода (например, `String lastName`, `int age`) привязываются к запросу через именованные параметры (`:lastName`, `:age`) или позиционные (`?1`, `?2`).
    - Spring Data использует `ParameterBinder` для маппинга аргументов метода на параметры запроса.

5. **Кэширование**:
    - Результат парсинга (структура `PartTree` и JPQL) кэшируется в `QueryMethod`, чтобы избежать повторного анализа при последующих вызовах.

Пример JPQL для метода `findByLastNameAndAgeGreaterThan`:
```sql
SELECT u FROM User u WHERE u.lastName = :lastName AND u.age > :age
```

#### 2.4. Аннотация `@Query`

Если метод использует аннотацию `@Query`, Spring Data пропускает парсинг имени и напрямую использует указанный JPQL или SQL:

```java
@Query("SELECT u FROM User u WHERE u.email = :email")
User findByEmail(@Param("email") String email);
```

- **JPQL**: Указанный запрос используется без изменений.
- **Нативный SQL** (если указано `nativeQuery = true`):
  ```java
  @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
  User findByEmail(@Param("email") String email);
  ```
    - В этом случае SQL передается напрямую в базу данных, но результаты всё равно маппятся на сущности.

#### 2.5. Преобразование JPQL в SQL

После создания JPQL-запроса он передается JPA-провайдеру (обычно Hibernate), который преобразует его в SQL. Этот процесс включает:

1. **Анализ метаданных сущности**:
    - JPA-провайдер использует метаданные сущности (полученные из аннотаций `@Entity`, `@Table`, `@Column`, `@JoinColumn` и т.д.) для сопоставления полей сущности с таблицами и столбцами базы данных.
    - Например, для сущности:
      ```java
      @Entity
      @Table(name = "users")
      public class User {
          @Id
          private Long id;
          @Column(name = "last_name")
          private String lastName;
          private int age;
      }
      ```
        - Поле `lastName` маппится на столбец `last_name` в таблице `users`.

2. **Генерация SQL**:
    - Hibernate парсит JPQL с помощью ANTLR (синтаксический анализатор) и преобразует его в SQL, учитывая диалект базы данных (например, MySQL, PostgreSQL).
    - Пример JPQL:
      ```sql
      SELECT u FROM User u WHERE u.lastName = :lastName AND u.age > :age
      ```
      Преобразуется в SQL (для MySQL):
      ```sql
      SELECT u.id, u.last_name, u.age FROM users u WHERE u.last_name = ? AND u.age > ?
      ```

3. **Обработка связей**:
    - Если запрос включает связи (`@OneToMany`, `@ManyToOne`), Hibernate добавляет `JOIN` в SQL:
      ```java
      @Query("SELECT u FROM User u JOIN u.orders o WHERE o.status = :status")
      List<User> findByOrderStatus(@Param("status") String status);
      ```
      SQL:
      ```sql
      SELECT u.id, u.last_name, u.age FROM users u INNER JOIN orders o ON u.id = o.user_id WHERE o.status = ?
      ```

4. **Оптимизации**:
    - Hibernate может применять оптимизации, такие как минимизация выборки столбцов или использование `JOIN FETCH` для предотвращения проблемы N+1.
    - Если используется `@EntityGraph`, Hibernate генерирует SQL с учетом графика загрузки.

#### 2.6. Выполнение запроса

1. **Передача в `EntityManager`**:
    - Spring Data использует `EntityManager` (инжектированный через `EntityManagerFactory`) для создания объекта `Query`:
      ```java
      Query query = entityManager.createQuery(jpql);
      ```
    - Параметры привязываются с помощью `query.setParameter`.

2. **Отправка SQL в базу данных**:
    - Hibernate передает сгенерированный SQL в JDBC-драйвер базы данных.
    - Если используется пул соединений (например, HikariCP), соединение берется из пула.

3. **Маппинг результатов**:
    - Результаты запроса (ResultSet) маппятся обратно в объекты Java (сущности, проекции или скаляры) с помощью `EntityManager` и `ResultSetMapper`.
    - Если метод возвращает `List<User>`, Hibernate создает экземпляры `User` и заполняет их данными из ResultSet.
    
------------------
Какие методы автоматически помечаются как @Transactional?


Spring Data автоматически добавляет транзакционную семантику к методам, определённым в стандартных интерфейсах репозиториев. Это реализуется в классах, таких как `SimpleJpaRepository` (для JPA) или `SimpleMongoRepository` (для MongoDB). Давайте разберём, какие методы получают аннотацию `@Transactional` по умолчанию и как это работает.

#### 2.1. Методы с аннотацией `@Transactional` в стандартных интерфейсах

В стандартных интерфейсах, таких как `CrudRepository`, `PagingAndSortingRepository` и `JpaRepository`, следующие методы автоматически помечаются как транзакционные:

1. **Методы, изменяющие данные**:
    - `save(T entity)`: Сохранение или обновление сущности.
    - `saveAll(Iterable<S> entities)`: Сохранение коллекции сущностей.
    - `delete(T entity)`: Удаление конкретной сущности.
    - `deleteById(ID id)`: Удаление сущности по идентификатору.
    - `deleteAll(Iterable<? extends T> entities)`: Удаление коллекции сущностей.
    - `deleteAll()`: Удаление всех сущностей.

   Эти методы аннотированы `@Transactional` в реализациях, таких как `SimpleJpaRepository`, с атрибутами по умолчанию (например, `propagation = Propagation.REQUIRED`).

   Пример из `SimpleJpaRepository`:
   ```java
   @Transactional
   public <S extends T> S save(S entity) {
       if (entityInformation.isNew(entity)) {
           entityManager.persist(entity);
           return entity;
       } else {
           return entityManager.merge(entity);
       }
   }
   ```

2. **Методы, читающие данные** (с некоторыми оговорками):
    - Методы чтения, такие как `findById`, `findAll`, `findAllById`, по умолчанию **не транзакционные** в Spring Data JPA, если только не указано иное в конфигурации или пользовательской аннотации.
    - Однако в некоторых модулях (например, Spring Data MongoDB) методы чтения могут быть помечены `@Transactional(readOnly = true)` для обеспечения согласованности данных в рамках транзакции (например, для моментальных снимков в MongoDB).

3. **Методы с пользовательскими запросами**:
    - Методы, определённые в интерфейсе репозитория (например, `findByLastName`), **не получают автоматическую аннотацию `@Transactional`**, если они не изменяют данные.
    - Если метод помечен аннотацией `@Query` с атрибутом `nativeQuery = true` или использует `@Modifying` (для обновления или удаления), он должен быть явно аннотирован `@Transactional`, так как Spring Data не добавляет транзакции автоматически для пользовательских запросов.
    
#### 2.2. Почему некоторые методы автоматически транзакционные?

- **Изменяющие операции**: Методы, такие как `save` или `delete`, требуют транзакций, чтобы обеспечить атомарность и согласованность данных. Например, сохранение сущности может включать несколько операций (вставка/обновление самой сущности и её связей), которые должны быть выполнены в одной транзакции.
- **Читающие операции**: Методы чтения (`findById`, `findAll`) обычно не требуют транзакций, так как они не изменяют данные. Однако в некоторых случаях (например, для обеспечения согласованности в распределённых системах) Spring Data может добавлять `@Transactional(readOnly = true)`.

------------------
 Механизм транзакций в Spring Data?

Spring Data интегрируется с **Spring Transaction Management**, который реализует транзакции через следующие компоненты:

1. **TransactionManager**:
    - Для JPA используется `JpaTransactionManager`, который взаимодействует с `EntityManager` и JPA-провайдером (например, Hibernate).
    - Для других хранилищ используются соответствующие менеджеры транзакций (например, `MongoTransactionManager` для MongoDB).

2. **AOP-прокси**:
    - Spring создаёт прокси для bean’ов репозиториев (например, через CGLIB или JDK Dynamic Proxy).
    - Прокси перехватывает вызовы методов, помеченных `@Transactional`, и вызывает логику транзакций через `TransactionInterceptor`.

3. **TransactionInterceptor**:
    - Этот компонент:
        - Начинает транзакцию (через `TransactionManager`) перед вызовом метода.
        - Фиксирует (commit) транзакцию, если метод завершился успешно.
        - Откатывает (rollback) транзакцию, если выброшено исключение (по умолчанию откат происходит для `RuntimeException` и `Error`).

4. **Пропагация транзакций**:
    - По умолчанию используется `Propagation.REQUIRED`:
        - Если транзакция уже существует, метод присоединяется к ней.
        - Если транзакции нет, создаётся новая.
    - Для методов чтения (если они транзакционные) может использоваться `readOnly = true`, что оптимизирует работу с базой данных (например, отключает грязные проверки в Hibernate).

------------------
@Transactional какие есть настройки?

Разработчики могут настраивать транзакции через атрибуты `@Transactional`:

1. **Propagation**:
    1. MANDATORY — использует существующую транзакцию. Если ее нет — бросает exception. Если используется для класса, то действует на все public методы.
    
    2. NESTED — вложенная транзакция (подтранзакция). Подтвержается вместе с внешней транзакцией. Если нет существующей транзакции — работает как REQUIRED.Можно применять в таких случаях, когда: Сработает — норм, не сработает — тоже норм. Самое главное чтобы внешняя транзакция не пострадала.
    
    3. NEVER — означает, что данный метод не должен выполняться в транзакции. Если транзакция запущена — бросает exception.
    
    4. NOT_SUPPORTED — означает не выполнять в текущей транзакции. Если транзакция запущена — она останавливается на время выполнения метода. Метод выполняется вне транзакции. Когда метод выполнился — транзакция запускается.
    
    5. REQUIRED — (по умолчанию) означает, что если запущена транзакция — выполнять внутри нее, иначе создает новую транзакцию. Если ошибка в запросе, то в базу ничего на запишется.
    
    6. REQUIRES_NEW — создает в любом случае новую транзакцию. Если запущена существующая транзакция — она останавливается на время выполнения метода, новый метод выполняется в новой транзакции, и дальше выполняется внешняя транзакция, если она есть.
    
    7. SUPPORTS — может выполняться внутри транзакции, если она запущена, иначе выполнять без транзакции (новую транзакцию не создает), т.е. методу не важно, будет транзакция или нет, он в любом случае выполнится, но если будет транзакция, то он выполнится внутри нее.


2. **Isolation**:
    - Уровень изоляции транзакции, например:
        - `Isolation.READ_COMMITTED`: Предотвращает чтение неподтверждённых данных.
        - `Isolation.REPEATABLE_READ`: Гарантирует неизменность прочитанных данных в рамках транзакции.

3. **readOnly**:
    - `readOnly = true`: Оптимизирует запросы только для чтения (например, отключает грязные проверки в Hibernate).
    - Используется для методов, таких как `findById` или пользовательских `findBy...`.

4. **rollbackOn**:
    - Указывает, какие исключения вызывают откат транзакции (по умолчанию `RuntimeException` и `Error`).

------------------

Как работает TransactionInterceptor?

`TransactionInterceptor` — это реализация интерфейса `MethodInterceptor` из Spring AOP, которая перехватывает вызовы методов, помеченных аннотацией `@Transactional` (или настроенных через XML). Его основная задача:

- Начать транзакцию перед вызовом метода.
- Выполнить целевой метод.
- Зафиксировать транзакцию, если метод завершился успешно, или откатить, если выброшено исключение.

`TransactionInterceptor` работает в связке с:
- **TransactionManager**: Компонент, управляющий транзакциями (например, `JpaTransactionManager` для JPA, `DataSourceTransactionManager` для JDBC).
- **AOP-прокси**: Прокси-объект, созданный Spring для перехвата вызовов методов.
- **TransactionAttributeSource**: Источник атрибутов транзакции (например, свойства `@Transactional`, такие как `propagation`, `isolation`, `readOnly`).

### Как включается `TransactionInterceptor`?

1. **Конфигурация транзакций**:
    - Аннотация `@EnableTransactionManagement` активирует декларативное управление транзакциями. Она регистрирует `TransactionManagementConfiguration` и связанные с ней bean’ы, включая `TransactionInterceptor`.

2. **Создание AOP-прокси**:
    - Spring создает прокси для bean’ов, содержащих методы с `@Transactional`. Прокси может быть:
        - **JDK Dynamic Proxy**: Если целевой объект реализует интерфейс.
        - **CGLIB**: Если целевой объект — класс без интерфейса.
    - Прокси перехватывает вызовы методов и делегирует их `TransactionInterceptor`.

3. **Регистрация аспекта**:
    - `AnnotationTransactionAttributeSource` сканирует методы и классы на наличие `@Transactional` и извлекает атрибуты транзакции (например, `propagation`, `isolation`).
    - `TransactionInterceptor` регистрируется как часть `BeanFactoryTransactionAspectSupport`, который интегрируется с инфраструктурой AOP.

### Внутренняя работа `TransactionInterceptor`

`TransactionInterceptor` реализует метод `invoke` интерфейса `MethodInterceptor`, который выполняется при каждом перехвате вызова метода. Процесс можно разбить на следующие этапы:

#### 3.1. Извлечение атрибутов транзакции

1. **Чтение `@Transactional`**:
    - `TransactionInterceptor` использует `TransactionAttributeSource` (обычно `AnnotationTransactionAttributeSource`) для извлечения атрибутов транзакции из аннотации `@Transactional` на методе или классе.
    - Атрибуты включают:
        - `propagation`: Тип распространения транзакции (например, `REQUIRED`, `REQUIRES_NEW`).
        - `isolation`: Уровень изоляции (например, `READ_COMMITTED`).
        - `timeout`: Тайм-аут транзакции.
        - `readOnly`: Флаг только для чтения.
        - `rollbackOn`: Какие исключения вызывают откат.
    - Если аннотация отсутствует, проверяется класс, а затем родительские классы или интерфейсы.

2. **Кэширование**:
    - Атрибуты транзакции кэшируются в `TransactionAttributeSource` для оптимизации (чтобы не парсить аннотации при каждом вызове).

#### 3.2. Определение необходимости транзакции

- Если метод не помечен `@Transactional` и нет активной транзакции, `TransactionInterceptor` просто вызывает целевой метод без создания транзакции.
- Если метод помечен `@Transactional`, `TransactionInterceptor` определяет, как управлять транзакцией, на основе атрибута `propagation`.

#### 3.3. Управление транзакцией

`TransactionInterceptor` вызывает метод `invokeWithinTransaction`, который выполняет основную логику:

1. **Получение `TransactionManager`**:
    - `TransactionInterceptor` получает `PlatformTransactionManager` из контекста Spring (например, `JpaTransactionManager` для JPA).
    - Если `TransactionManager` не указан явно, используется тот, который зарегистрирован в контейнере.

2. **Создание `TransactionInfo`**:
    - Создаётся объект `TransactionInfo`, который хранит:
        - `TransactionManager`.
        - `TransactionAttribute` (атрибуты транзакции).
        - Состояние текущей транзакции (если она существует).
    - `TransactionInfo` привязывается к текущему потоку через `TransactionAspectSupport`.

3. **Начало транзакции**:
    - `TransactionInterceptor` вызывает `TransactionManager.getTransaction(TransactionDefinition)` для получения или создания транзакции.
    - Поведение зависит от `propagation`

4. **Выполнение целевого метода**:
    - `TransactionInterceptor` вызывает целевой метод через `MethodInvocation.proceed()`.
    - Если метод завершается успешно, продолжается фиксация транзакции.
    - Если выброшено исключение, анализируется, нужно ли откатить транзакцию.

5. **Фиксация или откат**:
    - **Фиксация**:
        - Если метод завершился успешно, `TransactionInterceptor` вызывает `TransactionManager.commit(TransactionStatus)`.
        - Для JPA это приводит к вызову `EntityManager.flush()` (если есть изменения) и фиксации транзакции в базе данных.
    - **Откат**:
        - Если выброшено исключение, `TransactionInterceptor` проверяет атрибуты `rollbackOn` и `noRollbackFor`.
        - По умолчанию откат происходит для `RuntimeException` и `Error`, но не для проверяемых исключений (`CheckedException`).
        - Если требуется откат, вызывается `TransactionManager.rollback(TransactionStatus)`.

6. **Очистка**:
    - После фиксации или отката `TransactionInfo` очищается, и состояние транзакции отвязывается от потока.
    - Если использовалась приостановленная транзакция (например, при `REQUIRES_NEW`), она возобновляется.
    
### Технические детали

1. **Потокобезопасность**:
    - `TransactionInterceptor` использует `ThreadLocal` для хранения состояния транзакции (`TransactionInfo`) в текущем потоке. Это обеспечивает изоляцию транзакций между потоками.

2. **Кэширование**:
    - Атрибуты транзакции (из `@Transactional`) кэшируются в `TransactionAttributeSource` для повышения производительности.
    - `TransactionManager` также может кэшировать соединения с базой данных через пул (например, HikariCP).

3. **Обработка исключений**:
    - `TransactionInterceptor` анализирует исключения через метод `determineRollbackRules`:
        - Если исключение соответствует `rollbackOn`, транзакция откатывается.
        - Если исключение указано в `noRollbackFor`, транзакция фиксируется.

4. **Интеграция с JPA**:
    - `JpaTransactionManager` связывает транзакцию с `EntityManager`. Например:
        - Начало транзакции вызывает `EntityManager.getTransaction().begin()`.
        - Фиксация вызывает `EntityManager.flush()` и `EntityManager.getTransaction().commit()`.
    - Если `readOnly = true`, Hibernate отключает грязные проверки и оптимизирует запросы.

5. **Поддержка вложенных транзакций**:
    - Для `Propagation.NESTED` `TransactionInterceptor` использует savepoints (точки сохранения), если `TransactionManager` поддерживает их (например, `DataSourceTransactionManager`).
    - JPA не поддерживает вложенные транзакции нативно, поэтому `NESTED` часто реализуется через savepoints.
    
------------------
Специальные параметры Spring Data

В методах запросов, в их параметрах можно использовать специальные параметры Pageable, Sort, а также ограничения Top и First.

Pageable - содержит номер страницы (offset) и количество элементов на странице (limit)
Sort - сортировка по определенному полю, можно добавить в Pageable
Top - последний элемент
First - первый элемент




------------------
Какие проекты включены в Spring Data

• Spring Data JDBC - реализует репозитории для подключения к реляционной БД с помощью JDBC. Не путать с низкоуровневым Spring JDBC, просто упрощающим подключение по JDBC;

• Spring Data JPA - позволяет подключаться к реляционным БД с помощью JPA и выбрав Hibernate или EclipseLink в качестве JPA Provider-а;

• Spring Data R2DBC - специальный модуль для подключения к реляционным БД с асинхронным драйвером (H2, PostgreSQL, MS SQL) на реактивной основе с помощью технологии R2DBC;

• Spring Data MongoDB - позволяет подключаться к документ-ориентированной MongoDB; • Spring Data REST - совсем «Дзен», позволяет элементарно создать REST-интерфейс репозитория, основана на принципах HATEOAS;

• Spring Data Key Value - корневой проект для подключения к Key-Value NoSQL-базам данных. Также содержит дефолтную реализацию Key-Value-хранилища на основе HashMap (да-да!);

• Spring Data Redis - соответственно, для подключения к Redis. Использует абсолютно такие же подходы, что и Spring Data Key Value;

• и многие другие, включая поддерживаемые сообществом.

------------------
Расскажи про Spring Data Redis?


Spring Data Redis интегрируется с экосистемой Spring, используя знакомые паттерны Spring Data (например, репозитории) и инфраструктуру Spring (IoC, AOP, транзакции). Основные цели модуля:
- Упростить выполнение операций с Redis (например, работа с ключами, хэшами, списками, множествами).
- Предоставить единообразный API, независимый от конкретного клиента Redis (например, Jedis или Lettuce).
- Поддерживать декларативное управление транзакциями и кэшированием.
- Обеспечить интеграцию с другими модулями Spring (например, Spring Boot, Spring Cache).

Spring Data Redis поддерживает два основных клиента для взаимодействия с Redis:
- **Jedis**: Синхронный клиент, простой в использовании, но менее масштабируемый.
- **Lettuce**: Асинхронный и реактивный клиент, предпочтительный для современных приложений благодаря поддержке реактивного программирования и кластеров Redis.


#### RedisTemplate
`RedisTemplate` — центральный класс для выполнения операций с Redis. Он предоставляет методы для работы с различными структурами данных Redis (ключи, хэши, списки, множества, отсортированные множества).

- **Особенности**:
    - Типобезопасность: `RedisTemplate<K, V>` позволяет указать типы ключа (`K`) и значения (`V`).
    - Сериализация: Использует `RedisSerializer` для преобразования объектов Java в байты и обратно.
    - Управление соединениями: Инкапсулирует работу с пулом соединений.

- **Операции**:
    - `opsForValue()`: Работа с простыми значениями (строками, объектами).
    - `opsForHash()`: Работа с хэшами.
    - `opsForList()`: Работа с списками.
    - `opsForSet()`: Работа с множествами.
    - `opsForZSet()`: Работа с отсортированными множествами.
    - `opsForStream()`: Работа с потоками (Redis Streams).

#### RedisConnectionFactory
`RedisConnectionFactory` — фабрика для создания соединений с Redis. Она абстрагирует детали подключения (например, хост, порт, пароль) и поддерживает разные клиенты (Jedis, Lettuce).

- **Реализации**:
    - `JedisConnectionFactory`: Для клиента Jedis.
    - `LettuceConnectionFactory`: Для клиента Lettuce, поддерживает одиночные серверы, кластеры и Sentinel.


#### 2.3. Репозитории Spring Data Redis
Spring Data Redis поддерживает паттерн репозиториев, аналогичный другим модулям Spring Data (например, JPA). Вы можете определять интерфейсы репозиториев, а Spring Data автоматически генерирует их реализацию.

- **Как работает**:
    - Аннотация `@RedisHash` указывает, что сущность сохраняется как хэш в Redis.
    - Поля сущности маппятся на поля хэша (ключ: `User:<id>`).
    - Spring Data генерирует запросы для операций CRUD и методов с конвенцией именования (например, `findByName`).

#### 2.4. Сериализация
Redis хранит данные как байты, поэтому Spring Data Redis использует `RedisSerializer` для преобразования объектов Java в байты и обратно. Доступные сериализаторы:
- `StringRedisSerializer`: Для строк.
- `Jackson2JsonRedisSerializer`: Для JSON (использует Jackson).
- `JdkSerializationRedisSerializer`: Для сериализации Java-объектов (по умолчанию).
- `GenericToStringSerializer`: Для простых типов (например, чисел).

#### 2.5. Поддержка транзакций
Spring Data Redis поддерживает транзакции через аннотацию `@Transactional` и `RedisTransactionManager`. Транзакции в Redis реализуются с помощью команд `MULTI`, `EXEC` и `DISCARD`.

- **Как работает**:
    - `RedisTransactionManager` вызывает `MULTI` для начала транзакции.
    - Все операции помещаются в очередь.
    - При фиксации (commit) вызывается `EXEC`, при откате — `DISCARD`.
    
---

### Внутренняя реализация Spring Data Redis?

#### 3.1. Подключение к Redis
- `RedisConnectionFactory` создает соединения через клиент (Jedis или Lettuce).
- Lettuce использует Netty для асинхронного взаимодействия, поддерживая кластеры и Sentinel.
- Соединения управляются через пул (например, `GenericObjectPool` в Jedis или встроенный пул в Lettuce).

#### 3.2. Выполнение операций
- `RedisTemplate` делегирует операции в `RedisConnection`, который предоставляет низкоуровневый API для команд Redis (например, `SET`, `GET`, `HSET`).
- Команды преобразуются в байты и отправляются в Redis.
- Результаты десериализуются обратно в объекты Java.

#### 3.3. Репозитории
- Для репозиториев Spring Data Redis использует `RedisRepositoryFactory`, которая создает прокси-объекты.
- Методы с конвенцией именования (например, `findByName`) преобразуются в команды Redis (например, `HGETALL` для хэшей).
- Метаданные сущности (аннотации `@RedisHash`, `@Id`) используются для маппинга на структуры Redis.

#### 3.4. Транзакции
- `RedisTransactionManager` интегрируется с `TransactionInterceptor` (см. предыдущий ответ).
- Команды в транзакции помещаются в очередь с помощью `MULTI` и выполняются атомарно при `EXEC`.

---

### Возможности и сценарии использования Spring Data Redis?

1. **Кэширование**:
    - Spring Data Redis интегрируется с Spring Cache (`@Cacheable`, `@CachePut`, `@CacheEvict`).
    - Пример:
      ```java
      @Cacheable(value = "users", key = "#id")
      public User getUser(Long id) {
          return redisTemplate.opsForValue().get("user:" + id);
      }
      ```

2. **Публикация/подписка (Pub/Sub)**:
    - Spring Data Redis поддерживает модель Pub/Sub через `RedisMessageListenerContainer`.
    - Пример:
      ```java
      @Bean
      MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
          return new MessageListenerAdapter(subscriber, "onMessage");
      }
 
      @Bean
      RedisMessageListenerContainer container(RedisConnectionFactory factory,
                                             MessageListenerAdapter listenerAdapter) {
          RedisMessageListenerContainer container = new RedisMessageListenerContainer();
          container.setConnectionFactory(factory);
          container.addMessageListener(listenerAdapter, new PatternTopic("channel"));
          return container;
      }
      ```

3. **Работа с кластерами и Sentinel**:
    - Lettuce поддерживает Redis Cluster и Sentinel для высокой доступности.
    - Конфигурация:
      ```java
      @Bean
      public RedisConnectionFactory redisConnectionFactory() {
          RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
          clusterConfig.clusterNode("localhost", 7000);
          return new LettuceConnectionFactory(clusterConfig);
      }
      ```

4. **Потоки (Redis Streams)**:
    - Поддержка Redis Streams для обработки событий.
    - Пример:
      ```java
      StreamOperations<String, String, String> streamOps = redisTemplate.opsForStream();
      streamOps.add("mystream", Collections.singletonMap("key", "value"));
      ```

5. **Транзакции и Lua-скрипты**:
    - Поддержка выполнения Lua-скриптов для атомарных операций.
    - Пример:
      ```java
      RedisScript<Long> script = RedisScript.of("return redis.call('INCR', KEYS[1])", Long.class);
      Long result = redisTemplate.execute(script, Collections.singletonList("counter"));
      ```

------------------
Расскажи про Spring Data MongoDB?

MongoDB — это документоориентированная NoSQL СУБД, которая хранит данные в JSON-подобном формате.Интеграция Spring Data и MongoDB предоставляется Spring для облегчения взаимодействия обоих и удобства разработчиков, избавляя от необходимости написания множества запросов для вставки, обновления и удаления.
Ниже приведены некоторые из возможностей, предоставляемых проектом Spring Data MongoDB:

Spring Data позволяет использовать как класс @Configuration, так и XML-конфигурацию.

Иерархия исключений Data Access Spring используется для трансляции исключения.

Интегрированное сопоставление между Java POJO и документом MongoDB.

Класс MongoTemplate, который упрощает использование распространенных операций MongoDB.

В дополнение к MongoTemplate, можно использовать
классы MongoReader и MongoWriter для низкоуровневого отображения.
