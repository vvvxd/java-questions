1)Что такое StepVerifier?

StepVerifier  – текучий (fluent) API для построения конвейера тестирования любого издателя Publisher

--------------------------------------------------------------------------------------------------------------------
2)Напишите легкий тест на StepVerifier.

StepVerifier
    .create(Flux.just("foo", "bar"))
    .expectSubscription()
    .expectNext("foo")
    .expectNext("bar")
    .expectComplete()
    .verify()

--------------------------------------------------------------------------------------------------------------------
3)Назовите основные методы в StepVerifier.

create(Publisher<? extends T> publisher): Создает новый экземпляр StepVerifier, который будет использоваться для тестирования реактивного потока данных, предоставленного Publisher.

expectNext(T... values): Ожидает получение следующих значений в потоке данных. Можно указать несколько значений через запятую.

expectNextMatches(Predicate<? super T> predicate): Ожидает получение следующего значения, которое соответствует заданному предикату. Можно использовать лямбда-выражения или ссылки на методы для определения предиката.

expectNextCount(long count): Ожидает получение указанного количества следующих значений в потоке данных.

expectComplete(): Ожидает успешное завершение потока данных без дополнительных значений.

expectError(): Ожидает ошибку в потоке данных. Этот метод не проверяет конкретный тип ошибки.

expectError(Class<? extends Throwable> exceptionType): Ожидает ошибку указанного типа в потоке данных.

verify(): Запускает выполнение теста и проверяет, соответствует ли реальное поведение потока данных ожидаемым значениям, ошибкам и завершению.

--------------------------------------------------------------------------------------------------------------------
4)Что такое WebTestClient?
WebTestClient - это класс из модуля Spring WebFlux, который предоставляет возможность выполнения интеграционных тестов для реактивных веб-приложений. Он предоставляет удобный API для отправки HTTP-запросов к приложению и проверки полученных HTTP-ответов.

WebTestClient основан на клиентской стороне WebClient из Spring WebFlux, который сам по себе представляет реактивный клиент для выполнения HTTP-запросов. Однако, в отличие от WebClient, WebTestClient предназначен специально для написания тестов и предлагает дополнительные функции для проверки и верификации HTTP-ответов.

--------------------------------------------------------------------------------------------------------------------
5)Напишите основные методы для работы с WebTestClient.

Основные методы для написания тестов с WebTestClient в Spring Reactive Web:

- get(), post(), put(), delete(), patch(), options(): Методы для отправки соответствующих HTTP-запросов. Вы можете указать путь, параметры, заголовки и тело запроса при необходимости.

    Пример:
    webTestClient.get().uri("/api/users")
        .exchange()
        .expectStatus().isOk()
        .expectBody().json("[{\"id\":1,\"name\":\"John\"},{\"id\":2,\"name\":\"Jane\"}]");

- uri(String uriTemplate, Object... uriVariables): Метод для указания пути запроса с возможностью использования шаблона URI и переменных пути.

    Пример:
    webTestClient.get().uri("/api/users/{id}", 1)
        .exchange()
        .expectStatus().isOk()
        .expectBody().json("{\"id\":1,\"name\":\"John\"}");

- header(String name, String value): Метод для добавления заголовка к запросу.

    Пример:
    webTestClient.post().uri("/api/users")
        .header("Content-Type", "application/json")
        .body(BodyInserters.fromValue(user))
        .exchange()
        .expectStatus().isCreated();

- body(BodyInserter<?, ? super ClientHttpRequest> inserter): Метод для установки тела запроса. BodyInserter может быть создан с помощью статических методов BodyInserters.

    Пример:
    User user = new User("John");
    webTestClient.post().uri("/api/users")
        .body(BodyInserters.fromValue(user))
        .exchange()
        .expectStatus().isCreated();

- exchange(): Метод, который отправляет HTTP-запрос и возвращает WebClient.ResponseSpec для проверки и верификации HTTP-ответа.

    Пример:
    webTestClient.get().uri("/api/users")
        .exchange()
        .expectStatus().isOk()
        .expectBody().json("[{\"id\":1,\"name\":\"John\"},{\"id\":2,\"name\":\"Jane\"}]");

- expectStatus(): Метод для проверки статуса HTTP-ответа.

    Пример:
    webTestClient.get().uri("/api/users")
        .exchange()
        .expectStatus().isOk();

- expectBody(): Метод для проверки тела HTTP-ответа. Вы можете использовать методы, такие как json(), jsonPath(), xml(), xpath() и другие для проверки содержимого тела ответа.

    Пример:
    webTestClient.get().uri("/api/users")
        .exchange()
        .expectBody().json("[{\"id\":1,\"name\":\"John\"},{\"id\":2,\"name\":\"Jane\"}]");

- expectHeader(): Метод для проверки заголовков HTTP-ответа.

    Пример:

    webTestClient.get().uri("/api/users")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectHeader().valueEquals("Cache-Control", "no-cache");

--------------------------------------------------------------------------------------------------------------------