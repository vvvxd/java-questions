Что такое сервлет?

Сервлет является интерфейсом Java, реализация которого расширяет функциональные возможности сервера. Сервлет взаимодействует с клиентами посредством принципа запрос-ответ.Хотя сервлеты могут обслуживать любые запросы, они обычно используются для расширения веб-серверов. Для таких приложений технология Java Servlet определяет HTTP-специфичные сервлет классы. Пакеты javax.servlet и javax.servlet.http обеспечивают интерфейсы и классы для создания сервлетов.

--------------------------------------------------------------------------------------------------------------------
Что такое контейнер сервлетов?

Контейнер сервлетов — программа, представляющая собой сервер, который занимается системной поддержкой сервлетов и обеспечивает их жизненный цикл в соответствии с правилами, определёнными в спецификациях.
Известные реализации:Apache Tomcat, Jetty, JBoss, GlassFish, IBM WebSphere, Oracle Weblogic.

--------------------------------------------------------------------------------------------------------------------
Какие задачи, функциональность контейнера сервлетов?

Контейнер сервлетов может работать как полноценный самостоятельный веб-сервер, быть поставщиком страниц для другого веб-сервера, например Apache, или интегрироваться в Java EE сервер приложений. Обеспечивает обмен данными между сервлетом и клиентами, берёт на себя выполнение таких функций, как создание программной среды для функционирующего сервлета, идентификацию и авторизацию клиентов, организацию сессии для каждого из них.

--------------------------------------------------------------------------------------------------------------------
Что вы знаете о сервлет фильтрах?

Сервлетный фильтр, в соответствии со спецификацией, это Java-код, пригодный для повторного использования и позволяющий преобразовать содержание HTTP-запросов, HTTP-ответов и информацию, содержащуюся в заголовках HTTP. Сервлетный фильтр занимается предварительной обработкой запроса, прежде чем тот попадает в сервлет, и/или последующей обработкой ответа, исходящего из сервлета.Сервлетные фильтры могут:

— перехватывать инициализацию сервлета прежде, чем сервлет будет инициирован;
— определить содержание запроса прежде, чем сервлет будет инициирован;
— модифицировать заголовки и данные запроса, в которые упаковывается поступающий запрос;
— модифицировать заголовки и данные ответа, в которые упаковывается получаемый ответ;
— перехватывать инициализацию сервлета после обращения к сервлету.

Сервлетный фильтр может быть сконфигурирован так, что он будет работать с одним сервлетом или группой сервлетов. Основой для формирования фильтров служит интерфейс javax.servlet.Filter, который реализует три метода:void init (FilterConfig config) throws ServletException;void destroy();void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;
Метод init() вызывается прежде, чем фильтр начинает работать,и настраивает конфигурационный объект фильтра. Метод doFilter выполняет непосредственно работу фильтра. Таким образом, сервер вызывает init() один раз, чтобы запустить фильтр в работу, а затем вызывает doFilter() столько раз, сколько запросов будет сделано непосредственно к данному фильтру. После того, как фильтр заканчивает свою работу, вызывается метод destroy().

--------------------------------------------------------------------------------------------------------------------
Зачем нужны слушатели в сервлетах?

Слушатели контекста и сессий — это классы, которые могут следить за тем, когда контекст или сессия были инициализированы, или отслеживать время, когда они должны быть уничтожены, и когда атрибуты были добавлены или удалены из контекста или сессии. Servlet 2.4 расширяет модель слушателей запроса, позволяя отслеживать, как запрос создается и уничтожается, и, как атрибуты добавляются и удаляются из сервлета. В Servlet 2.4 добавлены следующие классы:
ServletRequestListener
ServletRequestEvent
ServletRequestAttributeListener
ServletRequestAttributeEvent

--------------------------------------------------------------------------------------------------------------------
Как обработать исключения, выброшенные другим сервлетом в приложении?

Т.к. браузер понимает только HTTP, то когда приложение выбросит исключение контейнер сервлетов обработает исключение и создаст HTTP response. Это аналогично тому что происходит при кодах ошибок вроде 404, 403 и т.д. Servlet API предоставляет поддержку собственных сервлетов для обработки исключений и ошибок, которые мы можем задать в дескрипторе развертывания. Главная задача таких сервлетов — обработать ошибку или исключение и отправить понятный HTTP ответ пользователю. Например, можно предоставить ссылку на главную страницу, а так же описание некоторых деталей об ошибке.

--------------------------------------------------------------------------------------------------------------------
Что такое дескриптор развертывания?

Дескриптор развертывания — это конфигурационный файл артефакта, который будет развернут в контейнере сервлетов. В спецификации Java Platform, Enterprise Edition дескриптор развертывания описывает то, как компонент, модуль или приложение (такое, как веб-приложение или приложение предприятия) должно быть развернуто.
Этот конфигурационный файл указывает параметры развертывания для модуля или приложения с определенными настройками, параметры безопасности и описывает конкретные требования к конфигурации. Для синтаксиса файлов дескриптора развертывания используется язык XML.

--------------------------------------------------------------------------------------------------------------------
Как реализовать запуск сервлета с запуском приложения?

Контейнер сервлетов обычно загружает сервлет при первом запросе клиента, но иногда необходимо загрузить сервлет прямо на старте приложения (например если сервлет объемный и будет долго грузиться). Для этого необходимо использовать элемент load-on-startup в дескрипторе (или аннотацию loadOnStartup), который укажет необходимость загрузки сервлета при запуске.
<servlet>
<servlet-name>foo</servlet-name>
<servlet-class>com.foo.servlets.Foo</servlet-class>
<load-on-startup>5</load-on-startup>
</servlet>
Значение должно быть int. Если значение отрицательное, то сервлет будет загружен при запросе клиента, а если 0 и далее, то загрузится на старте приложения. Чем меньше число, тем раньше в очереди на загрузку будет сервлет.

--------------------------------------------------------------------------------------------------------------------
Что представляет собой объект ServletConfig?

Интерфейс javax.servlet.ServletConfig используется для передачи конфигурационной информации сервлету. Каждый сервлет имеет свой собственный объект ServletConfig, за создание экземпляра которого ответственен контейнер сервлетов. Для установки параметров конфигурации используются init параметры в web.xml (или аннотации WebInitParam). Для получения объекта ServletConfig данного сервлета используется метод getServletConfig().

--------------------------------------------------------------------------------------------------------------------
Что представляет собой объект ServletContext?

Интерфейс javax.servlet.ServletContext предоставляет доступ к параметрам веб приложения сервлету. Объект ServletContext является уникальным и доступен всем сервлетам веб приложения. Мы можем использовать объект ServletContext, когда нам необходимо предоставить доступ одному или нескольким сервлетам к инициализированным параметрам веб приложения. Для этого используется элемент <context-param> в web.xml. Объект ServletContext можно получить с помощью метода getServletContext() у интерфейса ServletConfig.Контейнеры сервлетов так же могут предоставлять context объекты, уникальные для группы сервлетов. Каждая из групп будет связана со своим набором URL путей хоста.ServletContext был расширен в спецификации Servlet 3 и предоставляет программное добавление слушателей и фильтров в приложение. Так же у этого интерфейса имеются множество полезных методов вроде getMimeType(), getResourceAsStream() и т.д..

--------------------------------------------------------------------------------------------------------------------
В чем отличия ServletContext и ServletConfig?

ServletConfig является уникальным объектом для каждого сервлета, в то время как ServletContext уникальный для всего приложения.

ServletConfig используется для предоставления параметров инициализации сервлету, а ServletContext для предоставления параметров инициализации приложения для всех сервлетов.

У нас нет возможности устанавливать атрибуты в объекте ServletConfig, в то время как можно установить атрибуты в объекте ServletContext, которые будут доступны другим сервлетам.

--------------------------------------------------------------------------------------------------------------------
Что такое Request Dispatcher?

Интерфейс RequestDispatcher используется для передачи запроса другому ресурсу (это может быть HTML, JSP или другой сервлет в том же приложении). Мы можем использовать это для добавления контента другого ресурса к ответу. Этот интерфейс используется для внутренней коммуникации между сервлетами в одном контексте. В интерфейсе реализовано два метода:void forward(ServletRequest var1, ServletResponse var2) — передает запрос из сервлета к другому ресурсу (сервлету, JSP или HTML файлу) на сервере.void include(ServletRequest var1, ServletResponse var2) — включает контент ресурса (сервлет, JSP или HTML страница) в ответ.Доступ к интерфейсу можно получить с помощью метода ServletContext getRequestDispatcher(String s). Путь должен начинаться с / , который будет интерпретироваться относительным текущего корневого пути контекста.

--------------------------------------------------------------------------------------------------------------------
Как можно создать блокировку (deadlock) в сервлете?

Дедлок можно получить реализовав зацикленный вызов метода, например вызвав метод doPost() в методе doGet() и вызвать doGet() в методе doPost().

--------------------------------------------------------------------------------------------------------------------
Как получить адрес сервлета на сервере?

Для получения актуального пути сервлета на сервере можно использовать эту конструкцию: getServletContext().getRealPath(request.getServletPath())

--------------------------------------------------------------------------------------------------------------------
Как получить информацию о сервере из сервлета?

Информацию о сервере можно получить с использованием объекта ServletContext с помощью метода getServerInfo(). Т.е. getServletContext().getServerInfo().

--------------------------------------------------------------------------------------------------------------------
Как получить ip адрес клиента на сервере?

Использовать request.getRemoteAddr() для получения ip клиента в сервлете.

--------------------------------------------------------------------------------------------------------------------
Что вы знаете о классах обертках (wrapper) для сервлетов?

В Servlet HTTP API предоставляются два класса обертки — HttpServletRequestWrapper и HttpServletResponseWrapper. Они помогают разработчикам реализовывать собственные реализации типов request и response сервлета. Мы можем расширить эти классы и переопределить только необходимые методы для реализации собственных типов объектов ответов и запросов. Эти классы не используются в стандартном программировании сервлетов.

--------------------------------------------------------------------------------------------------------------------
Каков жизненный цикл сервлета и когда какие методы вызываются?

Контейнер сервлетов управляет четырьмя фазами жизненного цикла сервлета:

Загрузка класса сервлета — когда контейнер получает запрос для сервлета, то происходит загрузка класса сервлета в память и вызов конструктора без параметров.

Инициализация класса сервлета — после того как класс загружен контейнер инициализирует объект ServletConfig для этого сервлета и внедряет его через init() метод. Это и есть место где сервлет класс преобразуется из обычного класса в сервлет.

Обработка запросов — после инициализации сервлет готов к обработке запросов. Для каждого запроса клиента сервлет контейнер порождает новую нить (поток) и вызывает метод service() путем передачи ссылки на объект ответа и запроса.

Удаление из Service — когда контейнер останавливается или останавливается приложение, то контейнер сервлетов уничтожает классы сервлетов путем вызова destroy() метода.

Можно описать как последовательность вызова методов: init(), service(), destroy().
public void init(ServletConfig config) - используется контейнером для инициализации сервлета. Вызывается один раз за время жизни сервлета.

public void service(ServletRequest request, ServletResponse response) - вызывается для каждого запроса. Метод не может быть вызван раньше выполнения init() метода.

public void destroy() - вызывается для уничтожения сервлета (один раз за время жизни сервлета).

--------------------------------------------------------------------------------------------------------------------
В каком случае вы будете переопределять метод service()?

Метод service() переопределяется, когда мы хотим, чтобы сервлет обрабатывал как GET так и POST запросы в одном методе. Когда контейнер сервлетов получает запрос клиента, то происходит вызов метода service(), который в свою очередь вызывает doGet(), doPost() методы, основанные на HTTP методе запроса. Есть мнение, что метод service() переопределять особого смысла нет, кроме указанного вначале случая использования одного метода на два типа запросов.

--------------------------------------------------------------------------------------------------------------------
Есть ли смысл определить конструктор для сервлета, как лучше инициализировать данные?

Такая возможность есть, но считается бессмысленной. Инициализировать данные лучше переопределив метод init(), в котором получить доступ к параметрам инициализации сервлета через использование объекта ServletConfig.

--------------------------------------------------------------------------------------------------------------------
В чем отличия GenericServlet и HttpServlet?

Абстрактный класс GenericServlet — независимая от используемого протокола реализация интерфейса Servlet. HttpServlet, как понятно из название, реализация интерфейса сервлета для протокола HTTP. Следует отметить, что HttpServlet extends GenericServlet.

--------------------------------------------------------------------------------------------------------------------
Как вызвать из сервлета другой сервлет этого же и другого приложения?

Если необходимо вызывать сервлет из того же приложения, то необходимо использовать механизм внутренней коммуникации сервлетов (inter-servlet communication mechanisms). Мы можем вызвать другой сервлет с помощью RequestDispatcher forward() и include() методов для доступа к дополнительным атрибутам в запросе для использования в другом сервлете. Метод forward() используется для передачи обработки запроса в другой сервлет. Метод include() используется, если мы хотим вложить результат работы другого сервлета в возвращаемый ответ.
Если необходимо вызывать сервлет из другого приложения, то использовать RequestDispatcher уже не получится (определен для приложения). Поэтому можно использовать ServletResponse sendRedirect() метод и предоставить полный URL из другого сервлета. Для передачи данных можно использовать cookies как часть ответа сервлета, а потом использовать их в нашем сервлете.

--------------------------------------------------------------------------------------------------------------------
Стоит ли волноваться о "многопоточной безопасности" работая с сервлетами?

Методы класса HTTPServlet init() и destroy() вызываются один раз за жизненный цикл сервлета — поэтому по поводу них беспокоиться не стоит. Методы doGet(), doPost() вызываются на каждый запрос клиента и т.к. сервлеты используют многопоточность, то здесь нужно задумываться о потокобезопасной работе.
В случае наличия локальных переменных в этих методах нет необходимости думать о многопоточной безопасности, т.к. они будут созданы отдельно для каждой нити. Но если используются глобальные ресурсы, то необходимо использовать синхронизацию как и в любом многопоточном приложении Java.

--------------------------------------------------------------------------------------------------------------------
В чем отличие между веб сервером и сервером приложений?

Веб сервер необходим для обработки HTTP request от браузера клиента и ответа клиенту с помощью HTTP response. Веб сервер понимает язык HTTP и запускается по HTTP протоколу. Примером веб сервера может служить реализация от Apache — Tomcat.
Сервер приложений предоставляет дополнительные возможности, такие как поддержка JavaBeans, JMS Messaging, Transaction Management и др. Можно сказать, что сервер приложений — это веб сервер с дополнительными возможностями, которые помогают разрабатывать корпоративные приложения.

--------------------------------------------------------------------------------------------------------------------
Какой метод HTTP не является неизменяемым?

HTTP метод называется неизменяемым, если он всегда возвращает одинаковый результат. HTTP методы GET, PUT, DELETE, HEAD, OPTIONS являются неизменяемыми. Необходимо реализовывать приложение так, чтобы эти методы возвращали одинаковый результат. К изменяемым методам относится HTTP метод POST. Post метод используется для реализации чего-либо, что изменяется при каждом запросе.
К примеру, для доступа к HTML странице или изображению необходимо использовать метод GET, т.к. он возвращает одинаковый результат. Но если нам необходимо сохранить информацию о заказе в базе данных, то нужно использовать POST метод. Неизменяемые методы так же известны как безопасные методы и нет необходимости заботиться о повторяющихся запросах от клиента для этих методов.

--------------------------------------------------------------------------------------------------------------------
В чем разница между методами GET и POST?

GET метод является неизменяемым, тогда как POST — изменяемый.
С помощью метода GET можно посылать ограниченное кол-во данных, которые будут пересланы в заголовке URL. В случае POST метода мы можем пересылать большие объемы данных, т.к. они будут находится в теле метода.
Данные GET метода передаются в открытом виде, что может использоваться в зловредных целях. POST данные передаются в теле запроса и скрыты от пользователя.
GET метод является HTTP методом по умолчанию, а POST метод необходимо указывать явно, чтобы отправить запрос.
GET метод используется гиперссылками на странице.

--------------------------------------------------------------------------------------------------------------------
Что такое MIME-тип?

MIME (произн. «майм», англ. Multipurpose Internet Mail Extensions — многоцелевые расширения интернет-почты) — стандарт, описывающий передачу различных типов данных по электронной почте, а также, в общем случае, спецификация для кодирования информации и форматирования сообщений таким образом, чтобы их можно было пересылать по Интернету. Content-Type response header это и есть MIME тип. Сервер посылает MIME тип клиенту для того, чтобы он понял какой тип данных пересылается. Это помогает верно отобразить полученные данные на клиенте. Наиболее часто используемые MIME типы: text/html, text/xml, application/xml и многие др.
В ServletContext существует метод getMimeType() для получения корректного MIME типа файла и дальнейшего использования этой информации для указания типа контента в ответе.

--------------------------------------------------------------------------------------------------------------------
Какие различные методы управления сессией в сервлетах вы знаете?

Сессия является обычным состоянием взаимодействия сервера и клиента и может содержать в себе множество запросов и ответов клиент-сервер. Т.к. HTTP и веб сервер не запоминают состояния (stateless), то единственным способом поддерживать сессию является пересылка уникальной информации (session id) в каждом запросе и ответе между клиентом и сервером.
Существуют несколько распространенных способов управления сессией в сервлетах:
Аутентификация пользователя
HTML hidden field (скрытое поле)
Cookies
URL Rewriting
Session Management API

--------------------------------------------------------------------------------------------------------------------
Как применяются Cookies в сервлетах?

Cookies (куки) используются в клиент-серверном взаимодействии и они не являются чем-то конкретным к Java. Servlet API предоставляет поддержку cookies через класс javax.servlet.http.Cookie implements Serializable, Cloneable. Для получения массива cookies из запроса необходимо воспользоваться методом HttpServletRequest getCookies(). Для добавления cookies в запрос методов не предусмотрено.
Аналогично HttpServletResponse addCookie(Cookie c) — может добавить cookie в response header, но не существует геттера для этого типа передачи данных.

--------------------------------------------------------------------------------------------------------------------