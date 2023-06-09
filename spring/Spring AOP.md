Что такое AOP? Как это относиться к IoC?

Аспектно-ориентированное программирование (АОП) - парадигма программирования, основанная на идее разделения функциональности для улучшения разбиения программы на модули. AOP и Spring - взаимодополняющие технологии, которые позволяют решать сложные проблемы путем разделения функционала на отдельные модули. АОП предоставляет возможность реализации сквозной логики - т.е. логики, которая применяется к множеству частей приложения - в одном месте и обеспечения автоматического применения этой логики по всему приложению. Подход Spring к АОП заключается в создании "динамических прокси" для целевых объектов и "привязывании" объектов к конфигурированному совету для выполнения сквозной логики.

--------------------------------------------------------------------------------------------------------------------

Почему Spring желает создавать прокси?

Потому что это позволяет Spring дать вашим компонентам дополнительные функции без изменения кода. В сущности, это то, что является аспектно-ориентированным (или: AOP) программированием.

Давайте рассмотрим самый популярный пример AOP — аннотацию Spring @Transactional.

--------------------------------------------------------------------------------------------------------------------

В чем разница между Сквозной Функциональностью (Cross Cutting Concerns) и АОП (аспектно оринтированное программирование)?

Сквозная Функциональность — функциональность, которая может потребоваться вам на нескольких различных уровнях — логирование, управление производительностью, безопасность и т.д.

АОП — один из подходов к реализации данной проблемы

--------------------------------------------------------------------------------------------------------------------

Почему возвращаемое значение при применении аспекта @Around может потеряться? Назовите причины.

Метод, помеченный аннотацией @Around, должен возвращать значение, которое он (метод) получил из joinpoint.proceed()

@Around("trackTimeAnnotation()")
public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
long startTime = System.currentTimeMillis();
Object retVal = joinPoint.proceed();
long timeTaken = System.currentTimeMillis() - startTime;
logger.info("Time taken by {} is equal to {}",joinPoint, timeTaken);
return retVal;
}

--------------------------------------------------------------------------------------------------------------------

Что такое Aspect в АОП?

Аспект (англ. aspect) - модуль или класс, реализующий сквозную функциональность. Аспект изменяет поведение остального кода, применяя совет в точках соединения, определённых некоторым срезом.

@Aspect
@Component
public class MyAspect {
...
}

--------------------------------------------------------------------------------------------------------------------

Что такое Advice в АОП?

Совет (англ. advice) - фрагмент кода, который должен выполняться в отдельной точке соединения (Pointcut). Существует несколько типов советов, совет может быть выполнен до, после или вместо точки соединения.

Before — перед вызовом метода
After — после вызова метода
After returning — после возврата значения из функции
After throwing — в случае exception
After finally — в случае выполнения блока finally
Around — можно сделать пред., пост., обработку перед вызовом метода, а также вообще обойти вызов метода.

на один Pointcut можно «повесить» несколько Advice разного типа.

--------------------------------------------------------------------------------------------------------------------

Что такое Pointcut в АОП?

Срез (англ. pointcut) - набор точек соединения. Срез определяет, подходит ли данная точка соединения к данному совету. Самые удобные реализации АОП используют для определения срезов синтаксис основного языка (например, в AspectJ применяются Java-сигнатуры) и позволяют их повторное использование с помощью переименования и комбинирования.


@Pointcut("execution(public * com.example.demoAspects.MyService.*(..))")
public void callAtMyServicePublic() { }

--------------------------------------------------------------------------------------------------------------------

Что такое JointPoint в АОП?

Точка соединения (англ. joinpoint) - это четко определенная точка в выполняемой программе, где следует применить совет. Типовые примеры точек соединения включают обращение к методу, собственно Method Invocation, инициализацию класса и создание экземпляра объекта. Многие реализации АОП позволяют использовать вызовы методов и обращения к полям объекта в качестве точек соединения.

--------------------------------------------------------------------------------------------------------------------

Что такое weaving, target и introduction в АОП?

Связывание(англ. weaving) представляет собой процесс действительной вставки аспектов в определенную точку кода приложения. Для решений АОП времени компиляции это делается на этапе компиляции, обычно в виде дополнительного шага процесса сборки. Аналогично, для решений АОП времени выполнения связывание происходит динамически во время выполнения. В AspectJ поддерживается еще один механизм связывания под названием связывание во время загрузки (load-time weaving - LTW), который перехватывает лежащий в основе загрузчик классов JVM и обеспечивает связывание с байт-кодом, когда он загружается загрузчиком классов.

Цель(англ. target) - это объект, поток выполнения которого изменяется каким-то процессом АОП. На целевой объект часто ссылаются как на объект, снабженный советом.

Внедрение (англ. introduction, введение) - представляет собой процесс, посредством которого можно изменить структуру объекта за счет введения в него дополнительных методов или полей, изменение иерархии наследования для добавления функциональности аспекта в инородный код. Обычно реализуется с помощью некоторого метаобъектного протокола (англ. metaobject protocol, MOP).

--------------------------------------------------------------------------------------------------------------------

В чем разница между Spring AOP и AspectJ АОП?

AspectJ де-факто является стандартом реализации АОП. Реализация АОП от Spring имеет некоторые отличия:Spring AOP немного проще, т.к. нет необходимости следить за процессом связывания.

Spring AOP поддерживает аннотации AspectJ, таким образом мы можем работать в спринг проекте похожим образом с AspectJ проектом.Spring AOP поддерживает только proxy-based АОП и может использовать только один тип точек соединения - Method Invocation. AspectJ поддерживает все виды точек соединения.

Недостатком Spring AOP является работа только со своими бинами, которые существуют в Spring Context.

--------------------------------------------------------------------------------------------------------------------

Что такое Advice в Spring?

Advice - это действие, предпринятое в данной точке соединения. AOП использует Advice в качестве перехватчика до завершения выполнения метода.

--------------------------------------------------------------------------------------------------------------------

Каковы типы рекомендаций для структуры Spring?

До: Это советы, которые выполняются до методов joinpoint. Они помечены знаком @before.

После возврата: они выполняются после того, как метод joinpoint завершит выполнение без проблем. Они помечены знаком аннотации @AfterReturning.

После выполнения: Они выполняются только в том случае, если метод joinnpoint заканчивается созданием исключения. Они помечены с помощью метки аннотации @AfterThrowing.

После: Они выполняются после метода joinpoint, независимо от того, как он завершается. Они помечены знаком @After.

Вокруг: Они выполняются до и после точки соединения и помечаются с помощью метки @Around аннотации.

--------------------------------------------------------------------------------------------------------------------

Что такое Weaving?

Weaving Spring - это процесс связывания элементов с другими типами приложений или объектами для создания рекомендуемых объектов.

--------------------------------------------------------------------------------------------------------------------

Что такое прокси-объекты и какие типы прокси-объектов может создавать Spring?

Прокси это специальный объект, который имеет такие же публичные методы как и бин, но у которого есть дополнительная функциональность.Два вида прокси:

JDK dynamic proxy — динамическое прокси. API встроены в JDK. Объекты создаются на основе интерфейсов.

CGLib proxy — не встроен в JDK. Используется когда интерфейс объекта недоступен, он создает классы наследники.

Плюсы прокси-объектов:Позволяют добавлять доп. логику — управление транзакциями, безопасность, логирование

Отделяет некоторый код(логирование и т.п.) от основной логики

--------------------------------------------------------------------------------------------------------------------

Как Spring AOP создает прокси-объекты?

для создания прокси объектов может использоваться как JDK так и CGLib, но предпочтение должно отдаваться JDK. И, если класс имеет хотя бы один интерфейс, то именно JDK dynamic proxy и будет использоваться (хотя это можно изменить, явно задав флаг proxy-target-class). При создании прокси объекта с помощью JDK на вход передаются все интерфейсы класса и метод для имплементации нового поведения. В результате получаем объект, который абсолютно точно реализует паттерн Proxy. Все это происходит на этапе создания бинов, поэтому, когда начинается внедрение зависимостей, то в реальности внедрен будет этот самый прокси-объект. И все обращения будут производиться именно к нему. Но выполнив свою часть функционала, он обратиться к объекту исходного класса и передаст ему управление. Если же этот объект сам обратиться к одному из своих методов, то это будет уже прямой вызов без всяких прокси.

--------------------------------------------------------------------------------------------------------------------

Нужно ли Spring использовать прокси Cglib?

Прокси являются выбором по умолчанию при программировании AOP с помощью Spring. Однако вы не ограничены использованием прокси, вы также можете пройти полный маршрут AspectJ, который при желании изменяет ваш фактический байт-код.

--------------------------------------------------------------------------------------------------------------------

Что на самом деле выполнится (с точки зрения транзакций), если вызвать method1()?

public class MyServiceImpl {

@Transactional
public void method1() {
//do something
method2();
}

@Transactional (propagation=Propagation.REQUIRES_NEW)
public void method2() {
//do something
}

}

В связи с тем, что для поддержки транзакций через аннотации используется Spring AOP, в момент вызова method1() на самом деле вызывается метод прокси объекта. Создается новая транзакция и далее происходит вызов method1() класса MyServiceImpl. А когда из method1() вызовем method2(), обращения к прокси нет, вызывается уже сразу метод нашего класса и, соответственно, никаких новых транзакций создаваться не будет

--------------------------------------------------------------------------------------------------------------------