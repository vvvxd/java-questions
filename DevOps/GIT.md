Что Такое Git?

Git — это технология, разработанная для отслеживания и ведения журнала изменений в любом типе компьютерных файлов, особенно, когда дело касается файлов используемых двумя или несколькими людьми (или даже всей компанией). Другими словами, компании используют Git для управления их рабочим процессом и отслеживания прогресса для различных проектов. Эта технология в основном используется в программной инженерии, но многие компании по всему миру - даже не имеющие никакого отношения к программированию - начали применять Git в своей повседневной работе. Однако особенную популярность (и важность) она приобрела среди разработчиков и программистов - на сегодняшний день Git можно назвать основным инструментом для достижения и поддержания философии DevOps.

--------------------------------------------------------------------------------------------------------------------

В Чём Различие Между Чистым Репозиторием и Рабочим Каталогом?

Очень популярный шаблон вопросов на собеседованиях. Вам часто нужно будет сравнивать два термина, команды или что-либо ещё. Лучше всего отвечать на эти вопросы без лишних слов, чётко и просто, но в это же время продемонстрировать ваше понимание того, о чём вы говорите. В данном случае Git и его основы создают идеальную почву для вопросов-определений и сравнений.
Чистый репозиторий, как понятно из названия, не содержит каких-либо рабочих файлов, которые используются в Git. Здесь нет подкаталогов, нет контроля версии - просто чистый шаблон. С другой стороны, рабочий каталог содержит всё вышеперечисленное - рабочие файлы (и историю их изменений), подкаталоги и т.д. Можно сказать, что они являются прямыми противоположностями друг друга.

--------------------------------------------------------------------------------------------------------------------

В Чём Различие Между Форками и Ветками?

Концепт форка (fork) не уникален или эксклюзивен для технологии Git. Вы можете найти упоминания форка во многих других областях. Хорошим примером могут стать форки криптовалют - крупные изменения в работе всей криптовалюты. Однако в Git форки менее спорны, с технологией используемой во "внутренних" сценариях.
В Git форк является копией репозитория, которая полностью отличается от оригинала.
Ветка (branch), в отличие от него, является чем-то используемым для изменения определённых частей программы (с точки зрения разработки), чтобы затем объединиться с оригинальным ядром.
Здесь можно провести хорошую параллель, где форк позволяет изменять всю внешность автомобиля, а ветка изменять только лишь шины.

--------------------------------------------------------------------------------------------------------------------

Вы Создали Коммит и Отправили Его, Теперь Он Стал Публичным. Однако Вы Заметили, Что Некоторым Вещам До Сих Пор Необходима Доработка. Можете Ли Вы Сделать Это На Стадии Коммита? Если Да, То Как?

Ответ на этот вопрос - да, вы до сих пор можете вносить изменения, даже если уже отправили коммит и сделали его публичным. Для этого вам нужно будет выполнить команду git revertcommand. Такого рода команды Git очень полезны и используются многими разработчиками на повседневной основе.
Команда будет служить в качестве "патча" для коммита, который вы указали для изменения. В таком случае, даже если вы упустили что-то до момента развёртывания коммита в лайв версию, вы всё равно можете изменить и исправить это.

--------------------------------------------------------------------------------------------------------------------

Что Такое Cherry-Picking?

Выборочное представление фактов случается тогда, когда вы решаете выбрать один из коммитов из ветки Git и затем применить его функции к другой ветке. Почему этот процесс называется выборочным представлением фактов? Всё просто - команды Git, основанные на передаче коммитов, предназначены для одновременного копирования нескольких коммитов. С помощью выборочного представления фактов, вы выбираете конкретный коммит, который вы хотите применить для другой ветки.

--------------------------------------------------------------------------------------------------------------------

Что Такое Stash (Скрытие)?

В Git Stash является любимой многими функцией, которой пользуются многие разработчики и менеджеры проектов. Она работает таким образом, что позволяет вам быстро скрыть вашу текущую работу в тайник (stash) и сразу же перейти к работе над другой частью проекта без беспокойства о том, что вы потеряете файлы или вам придётся сделать коммит недоработанной части. Тайник выступает в качестве хранилища - он сохраняет ваш проект и все связанные с ним функции, чтобы вы могли вернуться и продолжить работу там, где вы прервались.

--------------------------------------------------------------------------------------------------------------------

Как Вы Разрешаете Конфликты в Git?

Если вы работаете над коммитом, а затем наконец решаете провести слияние, то Git проверяет наличие дублирующих изменений, которые могут присутствовать в коммите на данный момент. В случае нахождения дубликатов, Git выдаст сообщение о конфликте - это происходит так как Git не может сам решить какие изменения правильные, а какие должны быть отброшены. Если не уделить этому аспекту должного внимания, то это может оказать негативное влияние на весь проект - это делает данную особенность Git очень важной и полезной.
Для решения конфликтов есть некоторые стандартные команды Git (вроде git add и git commit). После их выполнения Git упорядочит коммиты по порядку и завершит работу.

--------------------------------------------------------------------------------------------------------------------

Что Такое Pull Request?

Если ветка является другой версией кода, то запрос на внесение изменений (pull request), это когда вы берёте репозиторий и делаете из него ветку. После этого, вы вносите изменения и затем пытаетесь провести слияние ветки обратно с основным проектом. По понятным причинам, запрос на внесение изменений требует одобрения других членов этого проекта.

--------------------------------------------------------------------------------------------------------------------

Каков Самый Эффективный Способ Найти Плохой Коммит?

Конечно, вы можете пройтись по каждому коммиту пытаясь найти тот, который вызывает проблемы, но никто этим не занимается. Просто потому что для этого есть команда, способная разрешить проблемы с подобными коммитами быстро и просто, делая процесс поиска более эффективным.
Команда называется git bisect.

--------------------------------------------------------------------------------------------------------------------

В Чём Различие Между Get и Pull?

Когда вы выполняете команду "pull" для данных, то они скачиваются, а затем немедленно сливаются с текущим рабочим файлом. При использовании команды "get", данные скачиваются, но это уже происходит без слияния файлов.

--------------------------------------------------------------------------------------------------------------------

Как можно отменить коммит в Git, если он уже был опубликован ?

Как вариант, сделав обратный коммит с помощью команды git revert [commit SHA]. Это создаст коммит отменяющий изменения указанного коммита. После необходимо отправить изменения в удаленный репозиторий с помощью команды git push [repository]/branch-name. Если коммит сделан на личной ветке, не используемой никем кроме автора, ошибочный коммит можно отметить флагом drop в интерактивном режиме ребейза (git rebase -i), что полностью удалит указанный коммит из истории. После необходимо отправить изменения в удаленный репозиторий с помощью команды git push [repository]/branch-name с флагом force.

--------------------------------------------------------------------------------------------------------------------

В чем заключается разница между git pull и git fetch ?

Git pull извлекает (fetch) данные с сервера и автоматически делает слияние (merge) их с кодом текущей ветки. Git fetch — связывается с удаленным репозиторием и получает данные, которые отсутствуют в локальном. При выполнении этой команды слияние не происходит.

--------------------------------------------------------------------------------------------------------------------

Что такое "staging area" или "index" в Git ?

Staging area (область подготовленных файлов) — файл, который как правило, располагается в Git-директории и содержит информацию об изменениях, которые попадут в следующий коммит.

--------------------------------------------------------------------------------------------------------------------

Как найти список файлов, которые изменились в определенном коммите?

Это достигается просмотром истории коммитов с помощью команды git log с применением определенных флагов:
stat под каждым из коммитов появится список и количество измененных файлов, количество строк, добавленных и удаленных в каждом из файлов. В конце, под списком, будет выведена суммарная статистика.
name-status — показывает список файлов, которые были изменены, удалены.
Для поиска списка файла в конкретном коммите можно выполнить команду git show и указать хеш коммита.

--------------------------------------------------------------------------------------------------------------------

Из чего состоит коммит в Git ?

Коммит — это односвязный список, который состоит из объектов с измененными файлами и ссылки на предыдущий коммит. Также указываются имена автора, метки времени и сообщения коммита.

--------------------------------------------------------------------------------------------------------------------

Как объединить несколько отдельных коммитов в один цельный коммит ?

Это можно сделать с помощью перебазирования в интерактивном режиме работы (rebase). Например, для того, чтобы объединить, 3 последних коммита в один, необходимо выполнить команду git rebase -i HEAD~3 (3 соответствует числу коммитов, которые нужно объединить, отсчет от HEAD). Откроется окно редактора, в котором первые 3 строки соответствуют последним трем коммитам:
pick ab37583 Added feature 1.
pick 3ab2b83 Added feature 2.
pick 3ab5683 Added feature 3
В начале каждой строки стоит слово pick. Нужно поменять его на squash или s, сохранить изменения, закрыть редактор, после чего git попросит задать сообщение нового коммита, содержащего в себе изменения трех исходных.

--------------------------------------------------------------------------------------------------------------------

Какой командой создается репозиторий GIT?

git init

--------------------------------------------------------------------------------------------------------------------

Какой командой загружается имеющийся репозиторий?

git clone [url]
Например: git clone https://github.com/jquery/jquery

--------------------------------------------------------------------------------------------------------------------

Как вывести список имеющихся веток GIT с указанием последнего коммита в них?

git branch -av

--------------------------------------------------------------------------------------------------------------------

Как переключиться на ветку в GIT?

git checkout [branch]
Например: git checkout master

--------------------------------------------------------------------------------------------------------------------

Как создать новую ветку в GIT?

git branch [новая-ветка]
Например: git branch development

--------------------------------------------------------------------------------------------------------------------

Как создать отслеживаемую ветку GIT на основе имеющейся на удаленном репозитории?

git checkout --track [репозиторий/ветка]
Например: git checkout --track origin/master

--------------------------------------------------------------------------------------------------------------------

Как удалить локальную ветку в GIT?

git branch -d [ветка]
Например: git branch -d hotfix

--------------------------------------------------------------------------------------------------------------------

Как пометить текущий коммит тэгом в GIT?

git tag [имя-тэга]

--------------------------------------------------------------------------------------------------------------------

Как добавить файл в коммит GIT?

git add [имя_файла]

--------------------------------------------------------------------------------------------------------------------

Как сказать GIT-у "не следить больше за этим файлом"?

git rm [имя_файла]

--------------------------------------------------------------------------------------------------------------------

Как закоммитить все изменившиеся отслеживаемые GIT-ом файлы одной командой?

git commit -a
git commit -am "Комментарий к коммиту"

--------------------------------------------------------------------------------------------------------------------

Как сделать коммит в GIT?

git commit -m "comment"

--------------------------------------------------------------------------------------------------------------------

Как вернуть исходное состояние измененного, но не закоммиченного файла в GIT?

git reset [имя_файла]

--------------------------------------------------------------------------------------------------------------------

Как просмотреть историю коммитов?

git log

--------------------------------------------------------------------------------------------------------------------

Как просмотреть историю изменений конкретного файла в GIT?

git log -p [имя_файла]

--------------------------------------------------------------------------------------------------------------------

Как вывести список имеющихся удаленных репозиториев в GIT?

git remote -v

--------------------------------------------------------------------------------------------------------------------

Как добавить удаленный репозиторий в GIT?

git remote add [локальное_имя] [url]

--------------------------------------------------------------------------------------------------------------------

Что делает команда git fetch

git fetch [remote]
Загружает изменения с удалённого реопзитория [remote] в локальный репозиторий, но не применяет изменения к локальным файлам. То есть - не делает merge.

--------------------------------------------------------------------------------------------------------------------

Приведите пример работы с git fetch

git remote add pb git://github.com/paul/ticgit.git
git fetch pb
git checkout myOrigin/master
git merge pb/master

--------------------------------------------------------------------------------------------------------------------

Как в GIT получить данные из удаленного репозитория и применить их к локальным файлам?

git pull [remote] - обновить все локальные ветки
git pull [remote] [ветка] - только одну
Например:
git pull origin - все локальные ветки обновятся до версии в origin
git pull origin master - локальная ветка master обновится до версии из origin/master

--------------------------------------------------------------------------------------------------------------------

Как в GIT записать локальные коммиты на удаленный сервер?

git push [remote] [ветка]
Например: git push origin master

--------------------------------------------------------------------------------------------------------------------

Как удалить ветку в удаленном репозитории?

git push [remote] --delete <branch>
Например: git push origin --delete asdf

--------------------------------------------------------------------------------------------------------------------

Как объединить ветки в GIT?

git merge [ветка]

--------------------------------------------------------------------------------------------------------------------

Что делает команда git rebase?

git rebase - переносит текущий заголовок HEAD на состояние указанной ветки

--------------------------------------------------------------------------------------------------------------------

В чем отличие git push и git push -f

git push -f означает --force
В случае, если мы меняем историю коммитов (например, делаем rebase), git нас об этом предупреждает - и требует "пропихивать" решение, если мы в нем так уверены

--------------------------------------------------------------------------------------------------------------------

Что делает команда git reset?

Отменяет изменения, созданные в локальном репозитории и возвращает их в состояние "до коммита".

--------------------------------------------------------------------------------------------------------------------

Как отменить уже запушенный коммит?

git revert [коммит] - создает новый коммит, который отменяет изменения этого коммита.

--------------------------------------------------------------------------------------------------------------------

Как "сбросить" все локальные изменения, в том числе удалив и созданные файлы, на состояние последнего коммита? На состояние определенной ветки?

git reset --hard HEAD
git reset --hard [ветка]

--------------------------------------------------------------------------------------------------------------------

(GIT) Мы сделали какую-то работу, находясь в ветке *master*. Хотим закоммитить работу в новую ветку develop. Как это сделать?

git stash
git checkout -b develop
git stash pop
git commit
Объяснение: прячем изменения (stash), переходим на другую ветку (checkout), открываем изменения (pop) и коммитим.

--------------------------------------------------------------------------------------------------------------------

Какой Язык Используется в Git?

Здесь всё просто, Git использует язык "C". Самым логичным продолжением этого вопроса станет "почему именно этот язык?". Причиной также очень проста, язык "C" позволяет Git быть невероятно быстрым - этого было бы очень сложно достичь с более высокоуровневыми языками программирования.

--------------------------------------------------------------------------------------------------------------------

Что Такое Head?

Этот термин не так часто используется, но Head относится к объекту коммита. Head расположен в репозитории и каждый репозиторий по умолчанию имеет head под названием "Master". Кроме стандартного head,.репозиторий одновременно.может иметь несколько различных версий head.

--------------------------------------------------------------------------------------------------------------------

Можно Ли Починить Сломанные Коммиты?

Да, можно. Сделать это можно с помощью выполнения команды Git: git commit - amend. Эта команда найдёт сломанный коммит и восстановит его функционал, удаляя сообщение об ошибке в процессе.

--------------------------------------------------------------------------------------------------------------------

Как настроить Git-репозиторий для запуска инструментов проверки работоспособности кода непосредственно перед выполнением коммитов и предотвращения их в случае сбоя теста?

С помощью хука pre-commit. Для этого нужно определить в конфигурационном файле pre-commit (в папке .git/hooks), код проверки работоспособности внесенных изменений. После этого, Git будет запускать указанную проверку перед каждым коммитом. В случае если проверка вернет код отличный от нуля, коммит не будет применен.

--------------------------------------------------------------------------------------------------------------------

Какие вы знаете модели ветвления в Git? Опишите их.

Одной из самых популярных моделей ветвления Git является git flow. Говоря кратко, работая по этой модели в репозитории должны быть две постоянные ветки (master, develop) и любое количество временных веток (feature- , release-, hotfix-) которые вливаются в основные. Порядок подготовки релизов от начала разработки и непосредственно к осуществлению релиза четко диктуется моделью. Подробнее можно почитать по ссылке.

--------------------------------------------------------------------------------------------------------------------

Каким образом можно установить было ли слияние ветки в master?

Команда git branch —merged отфильтровывает ветки, которые были слиты.

--------------------------------------------------------------------------------------------------------------------

Способ слияния (merge)

Проще всего слияние ветки main в функциональную ветку выполняется с помощью следующей команды:

git checkout feature git merge main

При желании этот код можно записать в одну строку:
git merge feature main

Эта операция создает в ветке feature новый «коммит слияния», связывающий истории обеих веток.

Слияние (merge) — это отличная неразрушающая операция. Существующие ветки никак не изменяются. Эта операция позволяет избегать потенциальных проблем, связанных с выполнением команды rebase (и описанных ниже).
С другой стороны, это означает, что каждый раз, когда вам будет необходимо включить вышестоящие изменения, в функциональную ветку feature будет попадать внешний коммит слияния. Если работа в главной ветке main ведется активно, история вашей функциональной ветки быстро засорится. Хотя эту проблему можно устранить, используя продвинутые варианты команды git log, другим разработчикам будет тяжело разобраться в истории проекта.

Плюсы:
простота;
сохраняет полную историю и хронологический порядок;
поддерживает контекст ветки.

Минусы:
история коммитов может быть заполнена (загрязнена) множеством коммитов;
отладка с использованием git bisect может стать сложнее.

--------------------------------------------------------------------------------------------------------------------

Способ перебазирования (rebase)

Вместо слияния можно выполнить перебазирование функциональной ветки feature на главную ветку main с помощью следующих команд:

git checkout feature git rebase main

В результате вся функциональная ветка feature окажется поверх главной ветки main, включая в себя все новые коммиты в ветке main. Если вместо команды merge при коммитах используется rebase, эта команда перезаписывает историю проекта, создавая новые коммиты для каждого коммита в исходной ветке.

Главное преимущество rebase — более чистая история проекта. Во-первых, эта команда устраняет ненужные коммиты слияния, необходимые для git merge. Во-вторых, как показано на рисунке выше, команда rebase создает идеальную линейную историю проекта — вы сможете отследить функционал до самого начала проекта без каких-либо форков. Это упрощает навигацию в проекте с помощью таких команд, как git log, git bisect и gitk.

Однако такая безупречная история коммитов требует определенных жертв: жертвовать приходится безопасностью и отслеживаемостью. Если не следовать Золотому правилу Rebase, перезапись истории проекта может обернуться катастрофическими последствиями для совместных рабочих процессов. Кроме того, при выполнении rebase теряется контекст, доступный в коммите со слиянием: вы не сможете увидеть, когда вышестоящие изменения были включены в функционал.

Плюсы:
Упрощает потенциально сложную историю
Упрощение манипуляций с единственным коммитом
Избежание слияния коммитов в занятых репозиториях и ветках
Очищает промежуточные коммиты, делая их одним коммитом, что полезно для DevOps команд

Минусы:
Сжатие фич до нескольких коммитов может скрыть контекст
Перемещение публичных репозиториев может быть опасным при работе в команде
Появляется больше работы
Для восстановления с удаленными ветками требуется принудительный пуш. Это приводит к обновлению всех веток, имеющих одно и то же имя, как локально, так и удаленно, и это ужасно.

--------------------------------------------------------------------------------------------------------------------

Интерактивное перебазирование

Интерактивная операция rebase позволяет изменять коммиты при их перемещении в новую ветку. Этот вариант предоставляет еще больше возможностей, чем автоматическое выполнение rebase, поскольку дает полный контроль над историей коммитов ветки. Обычно его используют для очистки запутанной истории, перед тем как сливать функциональную ветку в главную ветку main.
Чтобы запустить интерактивное перебазирование, передайте параметр i команде git rebase:

git checkout feature
git rebase -i main

Откроется текстовый редактор. В нем будут перечислены все коммиты, подготовленные к перемещению:

pick 33d5b7a Message for commit #1
pick 9480b3d Message for commit #2
pick 5c67e61 Message for commit #3

Этот список точно отражает, как будет выглядеть ветка после перебазирования. Изменяя команду pick и (или) порядок коммитов, вы можете придать истории ветки нужный вид. Так, если второй коммит содержит исправление небольшой проблемы в первом, их можно объединить с помощью команды fixup:

pick 33d5b7a Message for commit #1
fixup 9480b3d Message for commit #2
pick 5c67e61 Message for commit #3

Когда вы сохраните и закроете файл, Git выполнит перебазирование в соответствии с вашими указаниями.

Удаление незначительных коммитов помогает быстрее разобраться в истории функциональной ветки. Команда git merge просто не в состоянии этого сделать.

--------------------------------------------------------------------------------------------------------------------

Золотое правило перебазирования

Разобравшись с возможностями rebase, необходимо в первую очередь понять, когда эту команду не нужно использовать. Золотое правило для команды git rebase — никогда не использовать ее в публичных ветках.

К примеру, представьте, что произойдет, если вы выполните rebase главной ветки main на свою функциональную ветку feature.

Перебазирование перемещает все коммиты ветки main в конец ветки feature. Проблема в том, что это происходит только в вашем репозитории, в то время как другие разработчики продолжают работать с исходной веткой main. Поскольку в результате перебазирования создаются абсолютно новые коммиты, Git будет считать, что история вашей главной ветки main разошлась с остальными.

Единственный способ синхронизировать две главные ветки main — выполнить их обратное слияние. Это приведет к дополнительному коммиту слияния и двум наборам коммитов, которые содержат одни и те же изменения (исходные изменения и изменения из вашей ветки после rebase). Нужно ли говорить, что ситуация получится крайне запутанная?

Поэтому перед выполнением команды git rebase следует убедиться, что текущую ветку не просматривает кто-то другой. Если в ней действительно ведется работа, прекратите любые действия и подумайте, как можно внести изменения неразрушающим способом (например, с помощью команды git revert). В остальных случаях вы можете свободно перезаписывать историю при необходимости.

--------------------------------------------------------------------------------------------------------------------

Принудительная отправка изменений

Git заблокирует попытку поместить перебазированную ветку main обратно в удаленный репозиторий, поскольку она вступит в конфликт с удаленной веткой main. Но эту операцию можно выполнить принудительно, добавив флаг --force:

#Будьте крайне осторожны с этой командой! git push --force

При этом удаленная ветка main станет соответствовать ветке в вашем репозитории после rebase. В итоге путаться начнут и ваши коллеги. Поэтому будьте внимательны и используйте эту команду только в том случае, если полностью понимаете, чего хотите добиться.
Одна из немногих ситуаций, требующих форсированного помещения кода, — это локальная очистка после помещения частной функциональной ветки в удаленный репозиторий (например, для создания резервной копии). Это равноценно заявлению: «Ой, я ведь не хотел отправлять исходную версию этой функциональной ветки. Лучше возьмите текущую версию». Здесь также важно, чтобы никто после коммитов не начал работу из исходной версии функциональной ветки.

--------------------------------------------------------------------------------------------------------------------

Преимущества Rebase перед merge

Вы разрабатываете локально: если вы не делились своей работой с кем-либо еще. На данный момент вы должны предпочесть перемещение слиянию, чтобы сохранить свою историю в порядке. Если у вас есть личная вилка репозитория, которая не используется совместно с другими разработчиками, вы можете делать rebase даже после того, как переместились в свою ветку.

Ваш код готов к ревью: вы создали пулл реквест. Другие анализируют вашу работу и потенциально стягивают ее к своей вилке для локального ревью. На данный момент вы не должны перемещать свою работу. Вы должны создать коммит «переделать» и обновить ветку. Это помогает отслеживать запросы на пулл реквест и предотвращает случайную поломку истории.

Ревью сделано и готово к интеграции в целевую ветку. Поздравляем! Вы собираетесь удалить свою ветку feature. Учитывая, что с этого момента другие разработчики не будут fetch-merging эти изменения, это ваш шанс изменить вашу историю. На этом этапе вы можете переписать историю и сбросить оригинальные коммиты, и эти надоедливые «переделки» и «слияние» сливаются в небольшой набор целенаправленных коммитов. Создание явного слияния для этих коммитов является необязательным, но имеет значение. Он записывает, когда функция достигла master.

--------------------------------------------------------------------------------------------------------------------