Что такое Docker?

Docker — это платформа, которая предназначена для разработки, развёртывания и запуска приложений в контейнерах

--------------------------------------------------------------------------------------------------------------------

Что такое виртуальная машина?

Виртуальная машина - это система, которая действует точно так же, как компьютер. Виртуальные машины позволяют запускать операционную систему в приложении, которое ведет себя как полноценный отдельный компьютер. Вы можете использовать их для работы с различными операционными системами, запускать программное обеспечение, которое не может работать на вашей основной ОС.

--------------------------------------------------------------------------------------------------------------------

Docker vs Виртуальная машина. Поддержка операционной системы

Поддержка операционной системы виртуальной машины и контейнера Docker сильно отличается. На изображении выше вы можете видеть, что каждая виртуальная машина имеет свою гостевую операционную систему над основной операционной системой, что делает виртуальные машины тяжелыми. С другой стороны, контейнеры Docker используют общую операционную систему хоста, и поэтому они легковесны.
Совместное использование операционной системы хоста между контейнерами делает их очень легкими и помогает им загружаться всего за несколько секунд. Следовательно, накладные расходы на упраterm-6вление контейнерной системой очень низкие по сравнению с виртуальными машинами.
Docker контейнеры подходят для ситуаций, когда вы хотите запустить несколько приложений в одном ядре операционной системы. Но если у вас есть приложения или серверы, которые должны работать в разных версиях операционной системы, тогда требуются виртуальные машины.

--------------------------------------------------------------------------------------------------------------------

Docker vs Виртуальная машина. Безопасность

Виртуальная машина не имеет общей операционной системы, и в ядре хоста существует сильная изоляция. Следовательно, они более безопасны по сравнению с контейнерами. Контейнер имеет много угроз безопасности и уязвимостей, поскольку контейнеры имеют общее ядро хоста.
Кроме того, поскольку ресурсы докера являются общими и не имеют пространства имен, злоумышленник может использовать все контейнеры в кластере, если он получает доступ даже к одному контейнеру. В виртуальной машине вы не получаете прямого доступа к ресурсам, а гипервизор предназначен для ограничения использования ресурсов в виртуальной машине.

--------------------------------------------------------------------------------------------------------------------

Docker vs Виртуальная машина. Портативность

Контейнеры Docker легко переносимы, поскольку у них нет отдельных операционных систем. Контейнер может быть перенесен на другую ОС, и он может запуститься немедленно. С другой стороны, виртуальные машины имеют отдельную ОС, поэтому портирование виртуальной машины затруднено по сравнению с контейнерами, а также требуется много времени для портирования виртуальной машины из-за ее размера.
Для целей разработки, где приложения должны разрабатываться и тестироваться на разных платформах, контейнеры Docker являются идеальным выбором.

--------------------------------------------------------------------------------------------------------------------

Docker vs Виртуальная машина. Производительность

Сравнение виртуальных машин и контейнеров Docker было бы несправедливым, поскольку они оба используются для разных целей. Но легкая архитектура Docker и его менее ресурсоемкая функция делают его лучшим выбором, чем виртуальная машина. В результате контейнеры могут запускаться очень быстро по сравнению с виртуальными машинами, а использование ресурсов варьируется в зависимости от нагрузки или трафика в нем.
В отличие от виртуальных машин, нет необходимости постоянно выделять ресурсы для контейнеров. Масштабирование и дублирование контейнеров также является простой задачей по сравнению с виртуальными машинами, поскольку в них нет необходимости устанавливать операционную систему.

--------------------------------------------------------------------------------------------------------------------

Разница между виртуализацией и контейнеризацией?

Контейнеры предоставляют изолированную среду для запуска приложения.
Все пользовательское пространство явно выделено для приложения.
Любые изменения, сделанные внутри контейнера, никогда не отражаются на хосте или даже других контейнерах, работающих на том же хосте.
Контейнеры - это абстракция прикладного уровня.
Каждый контейнер - это отдельное приложение.
В виртуализации гипервизоры предоставляют гостю целую виртуальную машину, включая ядро.
Виртуальные машины - это абстракция аппаратного уровня.
Каждая виртуальная машина - это физическая машина.

--------------------------------------------------------------------------------------------------------------------

Что Такое 'Контейнеры'?

Контейнеры Docker являются инструментами, которые использует Docker для упаковки и доставки приложения разработчика в место назначения. Эти контейнеры являются самой важной особенностью Docker, так как они могут работать на любом типе устройств - другими словами, они не являются эксклюзивными только для одной ОС. Универсальность этих контейнеров стала ценным инструментом как для разработчиков, так и для программистов во многих сферах.

--------------------------------------------------------------------------------------------------------------------

Свойства Docker контейнера

В нём можно что-то хранить. Нечто может находиться либо в контейнере, либо за его пределами.

Его можно переносить. Контейнер Docker можно использовать на локальном компьютере, на компьютере коллеги, на сервере поставщика облачных услуг (вроде AWS). Это роднит контейнеры Docker с обычными контейнерами, в которых, например, перевозят разные милые сердцу безделушки при переезде в новый дом.

В контейнер удобно что-то класть и удобно что-то из него вынимать. У контейнеров Docker есть представляющее их интерфейс, то есть — механизмы, позволяющие им взаимодействовать с внешним миром. Например, у контейнера есть порты, которые можно открывать для того, чтобы к приложению, работающему в контейнере, можно было бы обращаться из браузера. Работать с контейнером можно и средствами командной строки.

Если вам нужен контейнер, его можно заказать в интернет-магазине. Пустой контейнер можно купить, например, на сайте Amazon. В этот магазин контейнеры попадают от производителей, которые делают их в огромных количествах, используя пресс-формы. В случае с контейнерами Docker то, что можно сравнить с пресс-формой, а именно — образ контейнера, хранится в специальном репозитории. Если вам нужен некий контейнер, вы можете загрузить из репозитория соответствующий образ, и, используя его, этот контейнер создать.

--------------------------------------------------------------------------------------------------------------------

Что Такое Dockerfile?

Файл Dockerfile содержит набор инструкций, следуя которым Docker будет собирать образ контейнера. Этот файл содержит описание базового образа, который будет представлять собой исходный слой образа. Среди популярных официальных базовых образов можно отметить python, ubuntu, alpine.В образ контейнера, поверх базового образа, можно добавлять дополнительные слои. Делается это в соответствии с инструкциями из Dockerfile. Например, если Dockerfile описывает образ, который планируется использовать для решения задач машинного обучения, то в нём могут быть инструкции для включения в промежуточный слой такого образа библиотек NumPy, Pandas и Scikit-learn.И, наконец, в образе может содержаться, поверх всех остальных, ещё один тонкий слой, данные, хранящиеся в котором, поддаются изменению. Это — небольшой по объёму слой, содержащий программу, которую планируется запускать в контейнере.

--------------------------------------------------------------------------------------------------------------------

Что такое Образ Docker?

Образ Docker является источником контейнера Docker. Образы контейнеров Docker можно сравнить с чертежами, с формочками для печенья, или с пресс-формами для изготовления пластиковых изделий. Образы — это неизменные шаблоны, которые используются для создания одинаковых контейнеров.
Или можно сказать, что образы Docker используются для создания контейнеров.
Когда пользователь запускает образ Docker, создается экземпляр контейнера.
Эти образы могут быть развернуты в любой среде Docker.
В образе контейнера Docker содержится образ базовой операционной системы, код приложения, библиотеки, от которого оно зависит. Всё это скомпоновано в виде единой сущности, на основе которой можно создать контейнер.

--------------------------------------------------------------------------------------------------------------------

Какие Существуют Три Основных Типа Компонентов Docker?

Клиент является компонентом, который передаёт хосту команды "run" и "build". Хост — это то место, где создаются все контейнеры и образы. После этого они отправляются в Реестр, для выполнения.

--------------------------------------------------------------------------------------------------------------------

Что такое Docker контейнер и каковы его преимущества?

Контейнеры Docker включают в себя приложение и все его зависимости.
Он разделяет ядро с другими контейнерами, работая как изолированные процессы в пространстве пользователя в операционной системе хоста.
Контейнеры Docker не нуждаются в какой-либо конкретной инфраструктуре, они работают в любой инфраструктуре и в любом облаке.
Docker-контейнеры - это в основном экземпляры Docker-образов во время выполнения.
Вот несколько основных преимуществ использования контейнера Docker -
Он предлагает эффективную и простую первоначальную настройку.
Он позволяет вам подробно описать жизненный цикл вашего приложения.
Простая настройка и взаимодействие с Docker Compose.
Документация предоставляет каждый бит информации.

--------------------------------------------------------------------------------------------------------------------

Платформа Docker что такое?

Платформа Docker (Docker Platform) — это программа, которая даёт нам возможность упаковывать приложения в контейнеры и запускать их на серверах. Платформа Docker позволяет помещать в контейнеры код и его зависимости. Как результат, системы, основанные на контейнерах, легко масштабировать, так как контейнеры можно переносить и воспроизводить.

--------------------------------------------------------------------------------------------------------------------

Движок Docker что такое?

Движок Docker (Docker Engine) — это клиент-серверное приложение. Компания Docker разделила движок Docker на два продукта. Docker Community Edition (CE) — это бесплатное ПО, во многом основанное на опенсорсных инструментах.Вероятно, вы будете пользоваться именно этой версией Docker. Docker Enterprise — это платная версия системы, дающая пользователям дополнительные возможности в области поддержки систем, управления ими и безопасности. Платная версия Docker даёт компании средства, необходимые для её существования.

--------------------------------------------------------------------------------------------------------------------

Клиент Docker что такое?

Клиент Docker (Docker Client) — это основное средство, которое используют для взаимодействия с Docker. Так, при работе с интерфейсом командной строки Docker (Docker Command Line Interface, CLI), в терминал вводят команды, начинающиеся с ключевого слова docker, обращаясь к клиенту. Затем клиент использует API Docker для отправки команд демону Docker.

--------------------------------------------------------------------------------------------------------------------

Демон Docker что такое?

Демон Docker (Docker Daemon) — это сервер Docker, который ожидает запросов к API Docker. Демон Docker управляет образами, контейнерами, сетями и томами.а

--------------------------------------------------------------------------------------------------------------------

Тома Docker что такое?

Тома Docker (Docker Volumes) представляют собой наиболее предпочтительный механизм постоянного хранения данных, потребляемых или производимых приложениями.

--------------------------------------------------------------------------------------------------------------------

Реестр Docker что такое?

Реестр Docker (Docker Registry) представляет собой удалённую платформу, используемую для хранения образов Docker. В ходе работы с Docker образы отправляют в реестр и загружают из него. Подобный реестр может быть организован тем, кто пользуется Docker. Кроме того, поставщики облачных услуг могут поддерживать и собственные реестры. Например, это касается AWS и Google Cloud.

--------------------------------------------------------------------------------------------------------------------

Что такое Docker Hub?

Docker hub - это облачный реестр, который помогает вам организовывать репозитории кода.
Позволяет создавать, тестировать, хранить образы в облаке Docker.
Вы также можете развернуть образ на своем хосте с помощью Docker Hub.

--------------------------------------------------------------------------------------------------------------------

Репозиторий Docker что такое?

Репозиторием Docker (Docker Repository) называют набор образов Docker, обладающих одинаковыми именами и разными тегами. Теги — это идентификаторы образов.Обычно в репозиториях хранятся разные версии одних и тех же образов. Например, Python — это имя популярнейшего официального репозитория Docker на хабе Docker. А вот Python:3.7-slim — это версия образа с тегом 3.7-slim в репозитории Python. В реестр можно отправить как целый репозиторий, так и отдельный образ.Теперь поговорим о терминах экосистемы Docker, имеющих отношение к масштабированию.

--------------------------------------------------------------------------------------------------------------------

Объясните архитектуру Docker?

Docker состоит из Docker Engine, который является клиент-серверным приложением:
Сервер, который является типом долго выполняющейся программы, называемой процессом демона (команда docker).
REST API, который определяет интерфейсы, которые программы могут использовать для общения с демоном и указания ему, что делать.
Клиент интерфейса командной строки (CLI) (команда docker).
Интерфейс командной строки использует API-интерфейс Docker REST для управления или взаимодействия с приложениями-демонами Docker с использованием базовых API и CLI.

--------------------------------------------------------------------------------------------------------------------

Сеть Docker что такое?

Сетевые механизмы Docker (Docker Networking) позволяют организовывать связь между контейнерами Docker. Соединённые с помощью сети контейнеры могут выполняться на одном и том же хосте или на разных хостах. Подробности о сетевой подсистеме Docker можно почитать здесь.

--------------------------------------------------------------------------------------------------------------------

Каковы важные особенности Docker?

Вот основные особенности Docker: -
Простое моделирование
Контроль версий
Гибкость приложений
Производительность разработчика
Операционная эффективность

--------------------------------------------------------------------------------------------------------------------

Каковы основные недостатки Docker?

Некоторые из недостатков Docker, которые вы должны иметь в виду:
Он не предоставляет опцию хранения.
Плохой вариант мониторинга.
Нет автоматического перепланирования неактивных узлов.
Сложная автоматическая установка горизонтального масштабирования.

--------------------------------------------------------------------------------------------------------------------

Что такое Docker Swarm?

Docker Swarm является родной кластеризацией для Docker.
Он превращает пул Docker-хостов в один виртуальный Docker-хост.
Docker Swarm обслуживает стандартный Docker API, любой инструмент, который уже взаимодействует с демоном Docker, может использовать Swarm для прозрачного масштабирования на несколько хостов.

--------------------------------------------------------------------------------------------------------------------

Что такое Docker Engine?

Демон Docker или движок Docker представляет сервер.
Демон docker и клиенты должны быть запущены на одном хосте, который может взаимодействовать через двоичный файл клиента командной строки и API-интерфейс RESTful.

--------------------------------------------------------------------------------------------------------------------

Объясните что такое (реджестри) реестры

Есть два типа реестра -
Общедоступный
Частный
Публичный реестр Docker называется Docker hub, который позволяет вам хранить образы в частном порядке.
В Docker Hub вы можете хранить миллионы образов.

--------------------------------------------------------------------------------------------------------------------

Файлы Dockerfile

В файлах Dockerfile содержатся инструкции по созданию образа. С них, набранных заглавными буквами, начинаются строки этого файла. После инструкций идут их аргументы. Инструкции, при сборке образа, обрабатываются сверху вниз. Вот как это выглядит:

FROM ubuntu:18.04
COPY . /app

Слои в итоговом образе создают только инструкции FROM, RUN, COPY, и ADD. Другие инструкции что-то настраивают, описывают метаданные, или сообщают Docker о том, что во время выполнения контейнера нужно что-то сделать, например — открыть какой-то порт или выполнить какую-то команду.Здесь мы исходим из предположения, в соответствии с которым используется образ Docker, основанный на Unix-подобной ОС. Конечно, тут можно воспользоваться и образом, основанным на Windows, но использование Windows — это менее распространённая практика, работать с такими образами сложнее. В результате, если у вас есть такая возможность, пользуйтесь Unix.

--------------------------------------------------------------------------------------------------------------------

Инструкций Dockerfile

FROM — задаёт базовый (родительский) образ.

LABEL — описывает метаданные. Например — сведения о том, кто создал и поддерживает образ.

ENV — устанавливает постоянные переменные среды. Инструкция ENV хорошо подходит для задания констант.

RUN — выполняет команду и создаёт слой образа. Используется для установки в контейнер пакетов.

COPY — копирует в контейнер файлы и папки.

ADD — копирует файлы и папки в контейнер, может распаковывать локальные .tar-файлы.

CMD — описывает команду с аргументами, которую нужно выполнить когда контейнер будет запущен. Аргументы могут быть переопределены при запуске контейнера. В файле может присутствовать лишь одна инструкция CMD.

WORKDIR — задаёт рабочую директорию для следующей инструкции.

ARG — задаёт переменные для передачи Docker во время сборки образа.

ENTRYPOINT — предоставляет команду с аргументами для вызова во время выполнения контейнера. Аргументы не переопределяются.

EXPOSE — указывает на необходимость открыть порт.

VOLUME — создаёт точку монтирования для работы с постоянным хранилищем.

--------------------------------------------------------------------------------------------------------------------

Кеширование Docker

Одной из сильных сторон Docker является кэширование. Благодаря этому механизму ускоряется сборка образов.
При сборке образа Docker проходится по инструкциям файла Dockerfile, выполняя их по порядку. В процессе анализа инструкций Docker проверяет собственный кэш на наличие в нём образов, представляющих собой то, что получается на промежуточных этапах сборки других образов. Если подобные образы удаётся найти, то система может ими воспользоваться, не тратя время на их повторное создание.
Если кэш признан недействительным, то инструкция, в ходе выполнения которой это произошло, выполняется, создавая новый слой без использования кэша. То же самое происходит и при выполнении инструкций, которые следуют за ней.
В результате, если в ходе выполнения инструкций из Dockerfile оказывается, что базовый образ имеется в кэше, то используется именно этот образ из кэша. Это называется «попаданием кэша». Если же базового образа в кэше нет, то весь процесс сборки образа будет происходить без использования кэша.
Затем следующая инструкция сопоставляется со всеми образами из кэша, в основе которых лежит тот же самый базовый образ, который уже обнаружен в кэше. Каждый кэшированный промежуточный образ проверяется на предмет того, имеется ли в нём то, что было создано такой же инструкцией. Если совпадения найти не удаётся, это называется «промахом кэша» и кэш считается недействительным. То же самое происходит до тех пор, пока не будет обработан весь файл Dockerfile.

--------------------------------------------------------------------------------------------------------------------

Многоступенчатая сборка образов что такое?

В Dockerfile, описывающем многоступенчатую сборку образа, используется несколько инструкций FROM. Создатель такого образа может настроить выборочное копирование файлов, называемых артефактами сборки, из одной ступени сборки в другую ступень. При этом появляется возможность избавиться от всего того, что в готовом образе не понадобится. Благодаря этому методу можно уменьшить размер готового образа.Вот как работает каждая инструкция FROM:
Она начинает новый шаг сборки.
Она не зависит от того, что было создано на предыдущем шаге сборки.
Она может использовать базовый образ, отличающийся от того, который применялся на предыдущем шаге.

--------------------------------------------------------------------------------------------------------------------

Какую команду запустить, чтобы увидеть все запущенные контейнеры в Docker?

$ docker ps

--------------------------------------------------------------------------------------------------------------------

Напишите команду, чтобы остановить Docker-контейнер.

$ sudo docker stop container name

--------------------------------------------------------------------------------------------------------------------

Какая команда используется для запуска образа как контейнера?

$ sudo docker run -i -t alpine /bin/bash

--------------------------------------------------------------------------------------------------------------------

Потеряете Ли Вы Всю Работу, Если Случайно Покинете Контейнер?

Нет, вы не потеряете никакой информации, данных и других параметров, если случайно покинете контейнер Docker. Единственным способом потерять весь достигнутый прогресс будет выполнение соответствующей команды для удаления контейнера - выход из контейнера не причинит вреда информации.

--------------------------------------------------------------------------------------------------------------------

Какое Единственное и Самое Важное Требование для Создания Контейнера Docker?

Самым важным требованием для создания контейнера с Docker является стандартный образ (default image). Этот стандартный образ может отличаться в зависимости от используемого вами кода. Чтобы найти (и получить доступ) к стандартному образу, вы должны перейти на Docker Hub и поискать нужный вам домен. Как только вы найдёте образ, то вам останется лишь разобраться с документацией - после этого создание контейнера на Docker будет лишь вопросом времени!

--------------------------------------------------------------------------------------------------------------------

Объясните метки объектов Docker (Docker object labels)

Метки объектов Docker - это метод применения метаданных к объектам Docker, включая образы, контейнеры, тома, сеть, ноды Swarm и сервисы.

--------------------------------------------------------------------------------------------------------------------

Как запустить несколько копий файла Compose на одном хосте?

Compose использует имя проекта, которое позволяет вам создавать уникальные идентификаторы для всех контейнеров проекта и других ресурсов.
Чтобы запустить несколько копий проекта, задайте пользовательское имя проекта с помощью параметра командной строки -a или переменной среды COMPOSE_PROJECT_NAME.

--------------------------------------------------------------------------------------------------------------------

Команды для управления контейнерами

Общая схема команд для управления контейнерами выглядит так:
docker container my_command
Вот команды, которые могут быть подставлены туда, где мы использовали my_command:
create — создание контейнера из образа.
start — запуск существующего контейнера.
run — создание контейнера и его запуск.
ls — вывод списка работающих контейнеров.
inspect — вывод подробной информации о контейнере.
logs — вывод логов.
stop — остановка работающего контейнера с отправкой главному процессу контейнера сигнала SIGTERM, и, через некоторое время, SIGKILL.
kill — остановка работающего контейнера с отправкой главному процессу контейнера сигнала SIGKILL.
rm — удаление остановленного контейнера.

--------------------------------------------------------------------------------------------------------------------

Команды для управления образами

Для управления образами используются команды, которые выглядят так:
docker image my_command
Вот некоторые из команд этой группы:
build — сборка образа.
push — отправка образа в удалённый реестр.
ls — вывод списка образов.
history — вывод сведений о слоях образа.
inspect — вывод подробной информации об образе, в том числе — сведений о слоях.
rm — удаление образа.

--------------------------------------------------------------------------------------------------------------------

Разные команды

docker version — вывод сведений о версиях клиента и сервера Docker.
docker login — вход в реестр Docker.
docker system prune — удаление неиспользуемых контейнеров, сетей и образов, которым не назначено имя и тег.

--------------------------------------------------------------------------------------------------------------------

Тома Docker

Том — это файловая система, которая расположена на хост-машине за пределами контейнеров. Созданием и управлением томами занимается Docker. Вот основные свойства томов Docker:
Они представляют собой средства для постоянного хранения информации.
Они самостоятельны и отделены от контейнеров.
Ими могут совместно пользоваться разные контейнеры.
Они позволяют организовать эффективное чтение и запись данных.
Тома можно размещать на ресурсах удалённого облачного провайдера.
Их можно шифровать.
Им можно давать имена.
Контейнер может организовать заблаговременное наполнение тома данными.
Они удобны для тестирования.
Как видите, тома Docker обладают замечательными свойствами. Давайте поговорим о том, как их создавать.

--------------------------------------------------------------------------------------------------------------------

Расскажите нам что-нибудь о Docker Compose.

Docker Compose - это файл YAML, который содержит сведения о службе, сети и томах для настройки приложения Docker.
Docker Compose - это инструмент, который упрощает развёртывание приложений, для работы которых требуется несколько контейнеров Docker. Docker Compose позволяет выполнять команды, описываемые в файле docker-compose.yml. Эти команды можно выполнять столько раз, сколько потребуется. Интерфейс командной строки Docker Compose упрощает взаимодействие с многоконтейнерными приложениями. Этот инструмент устанавливается при установке Docker.
Таким образом, вы можете использовать Docker compose для создания отдельных контейнеров, размещения их и обеспечения связи с другими контейнерами.

--------------------------------------------------------------------------------------------------------------------

Что такое Docker Compose?

Docker Compose — это инструментальное средство, входящее в состав Docker. Оно предназначено для решения задач, связанных с развёртыванием проектов.Изучая основы Docker, вы могли столкнуться с созданием простейших приложений, работающих автономно, не зависящих, например, от внешних источников данных или от неких сервисов. На практике же подобные приложения — редкость. Реальные проекты обычно включают в себя целый набор совместно работающих приложений.Как узнать, нужно ли вам, при развёртывании некоего проекта, воспользоваться Docker Compose? На самом деле — очень просто. Если для обеспечения функционирования этого проекта используется несколько сервисов, то Docker Compose может вам пригодиться. Например, в ситуации, когда создают веб-сайт, которому, для выполнения аутентификации пользователей, нужно подключиться к базе данных. Подобный проект может состоять из двух сервисов — того, что обеспечивает работу сайта, и того, который отвечает за поддержку базы данных.Технология Docker Compose, если описывать её упрощённо, позволяет, с помощью одной команды, запускать множество сервисов.

--------------------------------------------------------------------------------------------------------------------

Разница между Docker и Docker Compose

Docker применяется для управления отдельными контейнерами (сервисами), из которых состоит приложение.
Docker Compose используется для одновременного управления несколькими контейнерами, входящими в состав приложения. Этот инструмент предлагает те же возможности, что и Docker, но позволяет работать с более сложными приложениями.

--------------------------------------------------------------------------------------------------------------------

Типичный сценарий использования Docker Compose

Docker Compose — это, в умелых руках, весьма мощный инструмент, позволяющий очень быстро развёртывать приложения, отличающиеся сложной архитектурой. Сейчас мы рассмотрим пример практического использования Docker Compose, разбор которого позволит вам оценить те преимущества, которые даст вам использование Docker Compose.
Представьте себе, что вы являетесь разработчиком некоего веб-проекта. В этот проект входит два веб-сайта. Первый позволяет людям, занимающимся бизнесом, создавать, всего в несколько щелчков мышью, интернет-магазины. Второй нацелен на поддержку клиентов. Эти два сайта взаимодействуют с одной и той же базой данных.
Ваш проект становится всё популярнее, и оказывается, что мощности сервера, на котором он работает, уже недостаточно. В результате вы решаете перевести весь проект на другую машину.
К сожалению, нечто вроде Docker Compose вы не использовали. Поэтому вам придётся переносить и перенастраивать сервисы по одному, надеясь на то, что вы, в процессе этой работы, ничего не забудете.
Если же вы используете Docker Compose, то перенос вашего проекта на новый сервер — это вопрос, который решается выполнением нескольких команд. Для того чтобы завершить перенос проекта на новое место, вам нужно лишь выполнить кое-какие настройки и загрузить на новый сервер резервную копию базы данных.

--------------------------------------------------------------------------------------------------------------------