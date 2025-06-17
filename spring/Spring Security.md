Что такое аутентификация и авторизация?

Аутентификация — процесс верификации пользователя компьютерной системы.Вот как он происходит в Spring:
1) Полученные пароль и имя пользователя преобразуются в экземпляр UsernamePasswordAuthenticationToken. Он реализует интерфейс Authentication.
2) Токен передается объекту AuthenticationManager для проверки
3) В случае удачной проверки AM возвращает заполненный объект Authentication
4) Устанавливается security context, с помощью вызова SecurityContextHolder.getContext().setAuthentication(...)

Авторизация — это процесс удостоверения в том, что у пользователя есть роль, требуемая чтобы сделать какое-либо действие. При авторизации проверяется, есть ли у вас соответствующие права на доступ к ресурсу.
Процесс:
1) По принципалу(principal) пользователя отображается его роль
2) Роль пользователя сверяется с ролью ресурса
   Сначала происходит аутентификация, а потом — авторизация.

-------------------------------------------------------------------------------------------------------------------- 
Что такое Spring Security?

Spring Security решает задачи обеспечения безопасности веб-приложений, REST API, микросервисов и других типов приложений. Основные цели:

- Аутентификация: Проверка, кто пользователь (например, через логин/пароль, JWT, OAuth2).
- Авторизация: Определение, что пользователь может делать (роли, разрешения).
- Защита от атак: Предотвращение CSRF, XSS, SQL-инъекций, фиксация сессий и других уязвимостей.
- Интеграция: Поддержка LDAP, SAML, OAuth2, OpenID Connect и других стандартов.

Spring Security построен на концепции фильтров (Servlet Filters) и использует цепочку фильтров (FilterChain) для обработки запросов. Это позволяет гибко настраивать процесс безопасности.

--------------------------------------------------------------------------------------------------------------------

Как Security работает внутри?

В Spring Security цепочка фильтров реализуется через **Servlet Filter**, который является частью спецификации Java Servlet API. Spring Security использует специальный фильтр под названием `FilterChainProxy`, который выступает в роли центрального элемента, управляющего цепочкой фильтров безопасности.

- **FilterChainProxy**: Это основной фильтр, зарегистрированный в контейнере сервлетов (например, Tomcat). Он перехватывает все HTTP-запросы и передает их через цепочку фильтров безопасности.
- **Security Filter Chain**: Внутри `FilterChainProxy` содержится одна или несколько цепочек фильтров безопасности (`SecurityFilterChain`). Каждая цепочка фильтров соответствует определенному набору URL-шаблонов, определенных в конфигурации (например, `/api/**` или `/admin/**`).

Каждая цепочка фильтров состоит из набора фильтров (`Filter`), которые выполняются последовательно. Каждый фильтр выполняет свою задачу и передает управление следующему фильтру в цепочке или конечному ресурсу (например, контроллеру Spring MVC).

---

### Как работает цепочка фильтров?
Когда HTTP-запрос поступает в приложение, он проходит через следующие этапы:

1. **Перехват запроса**:
    - Контейнер сервлетов (например, Tomcat) принимает HTTP-запрос и передает его зарегистрированным фильтрам.
    - `FilterChainProxy` (реализует интерфейс `javax.servlet.Filter`) перехватывает запрос и определяет, какая цепочка фильтров (`SecurityFilterChain`) должна быть применена на основе URL запроса.

2. **Выбор цепочки фильтров**:
    - В конфигурации Spring Security (обычно через `@Configuration` или `SecurityFilterChain` в Java-конфигурации) задаются правила соответствия URL-шаблонов и связанные с ними цепочки фильтров.
    - Например, запрос к `/api/**` может использовать одну цепочку фильтров, а запрос к `/public/**` — другую или вообще обойти фильтры безопасности.

3. **Выполнение фильтров**:
    - Каждый фильтр в цепочке вызывается методом `doFilter(ServletRequest, ServletResponse, FilterChain)`.
    - Фильтр может:
        - Выполнить свою задачу (например, проверить аутентификацию).
        - Прервать цепочку, отправив ответ клиенту (например, 403 Forbidden).
        - Передать управление следующему фильтру в цепочке через `chain.doFilter(request, response)`.
    - Если ни один фильтр не прерывает выполнение, запрос достигает конечного ресурса (например, контроллера).

4. **Обработка ответа**:
    - После обработки запроса фильтры могут также модифицировать HTTP-ответ (например, добавить заголовки безопасности, такие как `X-Frame-Options`).

---

### Какие есть ключевые фильтры Spring Security?
Spring Security предоставляет множество встроенных фильтров, которые добавляются в цепочку в зависимости от конфигурации. Вот некоторые из наиболее важных:

1. **SecurityContextPersistenceFilter**:
    - Отвечает за управление `SecurityContext`, который хранит информацию об аутентифицированном пользователе.
    - На старте запроса загружает `SecurityContext` из репозитория (например, из `HttpSession`).
    - После обработки запроса сохраняет `SecurityContext` обратно в репозиторий.
    - Это позволяет сохранять состояние аутентификации между запросами.

2. **UsernamePasswordAuthenticationFilter**:
    - Обрабатывает аутентификацию по логину и паролю (обычно для формы входа).
    - Извлекает данные из запроса (например, `username` и `password`) и передает их в `AuthenticationManager` для проверки.
    - При успешной аутентификации создает объект `Authentication` и помещает его в `SecurityContext`.

3. **BasicAuthenticationFilter**:
    - Обрабатывает HTTP Basic Authentication.
    - Проверяет заголовок `Authorization` в запросе и аутентифицирует пользователя.

4. **CsrfFilter**:
    - Защищает от атак CSRF (Cross-Site Request Forgery).
    - Проверяет наличие CSRF-токена в запросах (например, для POST-запросов).

5. **AuthorizationFilter**:
    - Проверяет, имеет ли пользователь доступ к запрашиваемому ресурсу.
    - Использует `AccessDecisionManager` для принятия решений на основе конфигурации (например, аннотаций `@PreAuthorize` или правил в `SecurityFilterChain`).

6. **ExceptionTranslationFilter**:
    - Обрабатывает исключения безопасности, такие как `AccessDeniedException` или `AuthenticationException`.
    - Перенаправляет пользователя на страницу входа или возвращает ошибку (например, 403 или 401).

7. **FilterSecurityInterceptor**:
    - Выполняет финальную проверку доступа к ресурсу на основе конфигурации.
    - Работает с `AccessDecisionManager` и `SecurityMetadataSource` для определения прав доступа.

Порядок фильтров в цепочке строго определен, но его можно настроить. Например, `SecurityContextPersistenceFilter` всегда идет первым, чтобы загрузить контекст безопасности, а `FilterSecurityInterceptor` — последним, чтобы проверить доступ.

-----------------------------------------------------------------------------------------------------------------

Основные объекты, участвующие в Spring Security

SecurityContextHolder — содержит и предоставляет доступ к SecurityContext в приложении.
SecurityContext — дефолтная реализация Spring Security содержащая объект Authentication.
Authentication — предоставляет токен для запроса аутентификации или для принципала, который прошел аутентификацию. Также содержит список полномочий, к которым получил доступ принципал.
GrantedAuthority — содержит полномочия выданные прошедшему проверку принципалу.
UserDetails — содержит информацию о пользователе: пароль, логин, полномочия. Эта информация используется для создания объекта Authentication после удачной аутентификации.
UserDetailsService — этот сервис извлекает информацию о пользователе из хранилища(память программы, бд, и т.п.) и кладет ее в UserDetails.

--------------------------------------------------------------------------------------------------------------------

Что такое делегирующий прокси фильтр?

Класс DelegatingFilterProxy — это класс, который реализует интерфейс javax.Servlet.Filter.
Это специальный фильтр, который делегирует работу другим бинам, которые также являются фильтрами.

--------------------------------------------------------------------------------------------------------------------

Что такое security filter chain?

Цепочка фильтров имплементит интерфейс SecurityFilterChain. Имплементацией, поставляемой Spring Security, является DefaultSecurityFilterChain.
Конструктор DSFC принимает несколько параметров. Первый параметр — request matcher. Остальные параметры — это фильтры, реализующие интерфейс servlet.Filter. Вот все фильтры, принимаемые DSFC:
ChannelProcessingFilter
SecurityContextPersistenceFilter
ConcurrentSessionFilter
Любой auth. фильтр: UserNamePasswordAuthenticationFilter / CasAythenticationFilter / BasicAuthenticationFilter
SecurityContextHolderAwareRequestFilter
JaasApiIntegrationFilter
RemeberMeAuthenticationFilter
AnonymusAuthenticationFilter
ExceptionTranslationFilter
FilterSecurityInterceptor

--------------------------------------------------------------------------------------------------------------------

Что такое security context?

Основной объект — это SecurityContextHolder. Это место, где хранятся детали о текущем security context, например детали принципала который в текущий момент пользуется приложением. По умолчанию для хранения используется ThreadLocal.
//получение SecurityContext SecurityHolderContext.getContext()
Объект, возвращаемый методом getContext() это SecurityContext. Он позволяет получать и устанавливать объект Authentication.
Authentication представляет следующие свойства:
Коллекцию полномочий выданных принципалу
Данные для удостоверения пользователя(логин, пароль)
Details — доп. информация, если она нужна. Может быть равно null
Принципал
Authentication flag — boolean переменная, которая показывает успешно ли прошел проверку принципал

--------------------------------------------------------------------------------------------------------------------

Как установить перехват перехода пользователя по определенным URL?

@Override protected void configure(HttpSecurity http) throws Exception { http.authorizeRequests()
//игнорирование всех запросов на /resources .antMatchers("/resources/**").permitAll()
//для остальных запросов требуется одна из 2 ролей
.antMatchers("/").hasAnyRole("ANONYMOUS", "USER") .antMatchers("/login)*").hasAnyRole("ANONYMOUS", "USER") .antMatchers("/logoutr").hasAnyRole("ANONYMOUS", "USER")
//запрос на ресурсы ниже требуют роль ADMIN .antMatchers("iadmin/*").hasRole("ADMIN") .antMatchers("/events/").hasRole("ADMIN") }

--------------------------------------------------------------------------------------------------------------------

Что означает * в методах antMatchers и mvcMatchers()?

Это выражение означает "любой".
Есть 2 вида:
* — перехватывает только на том уровне, на котором используется.
  Например, паттерн "/orders/*" проверит права пользователя, если пользователь перейдет по
  /orders/aliens или /orders/1, но не /orders/alien/1.
  ** — перехватывает на всех уровнях.Будут проверены любые запросы, /orders/aliens, /orders/1, /orders/alien/1.

--------------------------------------------------------------------------------------------------------------------

Почему mvcMatcher более защищенный чем antMatcher?

Потому что antMatcher("/service") сопоставляет путь запроса только с "/service", в то время как mvcMatcher("/service") сопоставляет с "/service", "/service.html", "/service.abc".

--------------------------------------------------------------------------------------------------------------------

Spring поддерживает хэширование паролей? Что такое соль?

Да, поддерживает. Для хэширования существует интерфейс PasswordEncoder, который содержит только один метод:
static PasswordEncoder createDelegatingPasswordEncoder(), который возвращает DelegatePasswordEncoder, настроенный по умолчанию.
Соль используется для вычисления хеш-значения пароля. Это последовательность рандомных чисел, которые используются для преобразования текстового пароля в хеш. Соль хранится в открытом виде рядом с хеш-паролем и может использоваться в дальнейшем при конвертации чистого пароля в хеш при новом логине пользователя.

--------------------------------------------------------------------------------------------------------------------

Зачем нужна защита для методов? Как ее установить?

Spring Security поддерживает защиту отдельных методов в бинах(например, в контроллерах). Это дополнительный слой защиты для приложения.
Ее требуется указать явно, используя аннотацию @EnableGlobalMethodSecurity.

--------------------------------------------------------------------------------------------------------------------

Что делает аннотация @RolesAllowed?

Эта аннотация основана на JSR-250.@RolesAllowed позволяет настроить доступ к методам(например, в классе-контроллере) с помощью ролей.Пример: @RolesAllowed("ADMIN") будет пропускать только пользователей с ролью ADMIN
Для использования нужно установить @EnableGlobalMethodSecurity(jsr250Enabled=true) на @Configuration классе + нужно чтобы эта аннотация была в classpath.

--------------------------------------------------------------------------------------------------------------------

Расскажите про @PreAuthorize

@PreAuthorize позволяет настроить доступ к методу используя SpEL.Для использования нужно установить @EnableGlobalMethodSecurity(prePostEnabled=true)

--------------------------------------------------------------------------------------------------------------------

Как реализованы все эти аннотации?

Используется сквозная функциональность, с помощью Spring AOP(прокси-объекты).

--------------------------------------------------------------------------------------------------------------------

Опишите работу AuthenticationManager

public interface AuthenticationManager {
Authentication authenticate(Authentication authentication) throws AuthenticationException;
}

AuthenticationManager представляет из себя интрефейс, который принимает Authentication и возвращает тоже Authentication.В нашем случае в имплементацией Authentication будет UsernamePasswordAuthenticationToken.Можно было бы реализовать AuthenticationManager самому, но смысла в этом мало, существует дефолтная реализация — ProviderManager.ProviderManager авторизацию делегирует другому интерфейсу:

public interface AuthenticationProvider {

Authentication authenticate(Authentication authentication) throws AuthenticationException;

boolean supports(Class<?> authentication);
}

Когда мы передаем объект Authentication в ProviderManager, он перебирает существующие AuthenticationProvider-ры и проверяет суппортит ли AuthenticationProvider эту имплементацию Authentication

public boolean supports(Class<?> authentication) {
return (UsernamePasswordAuthenticationToken.class .isAssignableFrom(authentication));
}

В результате внутри AuthenticationProvider.authenticate мы уже можем скастить переданный Authentication в нужную реализацию без каст эксепшена. Далее из конкретной реализации вытаскиваем креденшеналы. Если аутентификация не удалась AuthenticationProvider должен бросить эксепшен ,ProviderManagerпоймает его и попробует следующий AuthenticationProvider из списка, если ни один AuthenticationProvider не вернет успешную аутентификацию, то ProviderManager пробросит последний пойманный эксепшен.
Далее BasicAuthenticationFilter сохраняет полученный Authentication в SecurityContextHolderSecurityContextHolder.getContext().setAuthentication(authResult);
Процесс аутентификации на этом завершен.
Если выбросится AuthenticationException то будет сброшен SecurityContextHolder.clearContext();контекст и вызовится AuthenticationEntryPoint.

public interface AuthenticationEntryPoint {
void commence(HttpServletRequest request, HttpServletResponse response,
AuthenticationException authException) throws IOException, ServletException;
}

Задачей AuthenticationEntryPoint явялется записать в ответ информацию о том что аутентификация не удалась.В случае бейсик аутентификации это будет:

response.addHeader("WWW-Authenticate", "Basic realm=\"" + realmName + "\"");
response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

В результате браузер покажет окошко basic авторизации.

--------------------------------------------------------------------------------------------------------------------

Расскажите про фильтры в SecurityFilterChain

0 = {WebAsyncManagerIntegrationFilter}
Нам не очень интересен, согласно документации он «интегрирует» SecurityContext с WebAsyncManager который отвественнен за асинхронные запросы.

1 = {SecurityContextPersistenceFilter}
Ищет SecurityContext в сессии и заполняет SecurityContextHolder если находит.По умолчанию используется ThreadLocalSecurityContextHolderStrategy которая хранит SecurityContext в ThreadLocal переменной.

2 = {HeaderWriterFilter}
Просто добавляет заголовки в response.Отключаем кэш:
- Cache-Control: no-cache, no-store, max-age=0, must-revalidate
- Pragma: no-cache- Expires: 0

Не разрешаем браузерам автоматически определять тип контента:
- X-Content-Type-Options: nosnif

Не разрешаем iframe
- X-Frame-Options: DENY

Включаем встроенную зашиту в браузер от cross-site scripting (XSS)
- X-XSS-Protection: 1; mode=block

3 = {CsrfFilter}
Пожалуй нет ни одного разработчика который при знакомстве с SS не столкнулся бы с ошибкой «отсутсвия csrf токена».Почему мы не встречали эту ошибку ранее? Все просто, мы запускали методы на которых нет csrf защиты.

4 = {LogoutFilter}
Далее идет logout фильтр, он проверяет совпадает ли url c паттерномAnt [pattern='/logout', POST] - по умолчаниюи запускает процедуру логаута

по дефолту происходит следующие:
Удаляется Csrf токен.
Завершается сессия
Чистится SecurityContextHolder

5 = {BasicAuthenticationFilter}
Теперь мы добрались непосредственно до аутентификации. Что происходит внутри?Фильтр проверяет, есть ли заголовок Authorization со значением начинающийся на BasicЕсли находит, извлекает логин\пароль и передает их вAuthenticationManager

6 = {RequestCacheAwareFilter}
Для чего нужен этот фильтр? Представим сценарий:
1. Пользователь заходит на защишенный url.
2. Его перекидывает на страницу логина.
3. После успешной авторизации пользователя перекидывает на страницу которую он запрашивал в начале.

Именно для для восстановления оригинального запроса существует этот фильтр.Внутри проверяется есть ли сохраненный запрос, если есть им подменяется текущий запрос.Запрос сохраняется в сессии, на каком этапе он сохраняется будет написанно ниже.

7 = {SecurityContextHolderAwareRequestFilter}
Оборачивает существущий запрос в SecurityContextHolderAwareRequestWrapper

8 = {AnonymousAuthenticationFilter}
Если к моменту выполнения этого фильтра SecurityContextHolder пуст, т.е. не произошло аутентификации фильтр заполняет объект SecurityContextHolder анонимной аутентификацией — AnonymousAuthenticationToken с ролью «ROLE_ANONYMOUS».Это гарарантирует что в SecurityContextHolder будет объект, это позволяет не бояться NP, а также более гибко подходить к настройке доступа для неавторизованных пользователей.

9 = {SessionManagementFilter}
На это этапе производятся действия связанные с сессией.Это может быть:
— смена идентификатора сессии
— ограничени количества одновременных сессий
— сохранение SecurityContext в securityContextRepository

В нашем случае происходит следующе: SecurityContextRepository с дефолтной реализацией HttpSessionSecurityContextRepository сохраняет SecurityContext в сессию.Вызывается sessionAuthenticationStrategy.onAuthentication

Происходят 2 вещи:
1. По умолчанию включенна защита от session fixation attack, т.е. после аутенцификации меняется id сессии.
2. Если был передан csrf токен, генерируется новый csrf токен

10 = {ExceptionTranslationFilter}
К этому моменту SecurityContext должен содеражть анонимную, либо нормальную аутентификацию.ExceptionTranslationFilter прокидывает запрос и ответ по filter chain и обрабатывает возможные ошибки авторизации.

SS различает 2 случая:
1. AuthenticationException Вызывается sendStartAuthentication, внутри которого происходит следующиее:
   SecurityContextHolder.getContext().setAuthentication(null;— отчищает SecurityContextHolder

requestCache.saveRequest(request, response);— сохраняет в requestCache текущий запрос, чтобы RequestCacheAwareFilter было что восстанавливать.

authenticationEntryPoint.commence(request, response, reason);— вызывает authenticationEntryPoint — который записывает в ответ сигнал о том что необходимо произвести аутентификацию (заголовки \ редирект)

2. AccessDeniedExceptionТут опять возможны 2 случая:
1. Пользователь с анонимной аутентификацией, или с аутентификацией по rememberMe токену вызывается sendStartAuthentication
2. Пользователь с полной, не анонимной аутентификацией вызывается:accessDeniedHandler.handle(request, response, (AccessDeniedException) exception)который по дефолту проставляет ответ forbidden 403

11 = {FilterSecurityInterceptor}
На последнем этапе происходит авторизация на основе url запроса.FilterSecurityInterceptor наследуется от AbstractSecurityInterceptor и решает, имеет ли текущий пользователь доступ до текущего url.Существует другая реализация MethodSecurityInterceptor который отвественнен за допуск до вызова метода, при использовании аннотаций @Secured\@PreAuthorize.Внутри вызывается AccessDecisionManagerЕсть несколько стратегий принятия решения о том давать ли допуск или нет, по умолчанию используется: AffirmativeBased

--------------------------------------------------------------------------------------------------------------------

Основные фильтры в Spring Security

WebAsyncManagerIntegrationFilter — Интегрирует SecurityContext с WebAsyncManager

SecurityContextPersistenceFilter — Ищет SecurityContext в сессии и заполняет SecurityContextHolder если находит

HeaderWriterFilter — Добавляет «security» заголовки в ответ

CsrfFilter — Проверяет на наличие сsrf токена

LogoutFilter — Выполняет logout

BasicAuthenticationFilter — Производит basic аутентификацию

RequestCacheAwareFilter — Восстанавливает сохраненный до аутентификации запрос, если такой есть

SecurityContextHolderAwareRequestFilter — Оборачивает существущий запрос в SecurityContextHolderAwareRequestWrapper

AnonymousAuthenticationFilter — Заполняет SecurityContext ананонимной аутентификацией

SessionManagementFilter — Выполняет действия связанные с сессией

ExceptionTranslationFilter — Обрабатывает AuthenticationException\AccessDeniedException которые происходят ниже по стеку.

FilterSecurityInterceptor — Проверяет имеет ли текущей пользователь доступ к текущему url.

FilterComparator — здесь можно посмотреть список фильтров и их возможный порядок.

Основные сущности:
AuthenticationManager — интерфейс, ответственнен за аутентификацию

ProviderManager — реализация AuthenticationManager, которая использует внутри использует AuthenticationProvider

AuthenticationProvider — интерфейс, отвественнен за аутентификаци конкретной реализации Authentication.

SecurityContextHolder — хранит в себе аутентификацию обычно в ThreadLocal переменной.

AuthenticationEntryPoint — модифицирует ответ, чтобы дать понять клиенту что необходима аутентификация (заголовки, редирект на страницу логина, т.п.)

AccessDecisionManager решает имеет ли Authentication доступ к какому-то ресурсу.

AffirmativeBased — стратегия используемая AccessDecisionManager по умолчанию.

--------------------------------------------------------------------------------------------------------------------