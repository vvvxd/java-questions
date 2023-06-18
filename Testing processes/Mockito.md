Что такое Mockito?

Во-первых, эта библиотека - это стандарт в тестирование Spring'а. Который фактически является стандартом в отрасли Java-backend разработки.

Во-вторых, тебе придется писать тесты для своего Spring-кода. Единственный способ понять, что написанный тобой бекенд работает как нужно - это вызывать методы его API. И сделать это с помощью тестов в 10 раз легче, чем без них. Ты сам в этом убедишься.

--------------------------------------------------------------------------------------------------------------------

Так что же такое эта Mockito и чем она так хороша?

В процессе развития и становления тестирования очень часто возникала необходимость вместо реального объекта подсунуть коду какую-нибудь «заглушку».
Например, тестируется код, который работает с базой данный и что-то там меняет. Хорошо чтобы перед каждым тестом состояние этой базы было одним и тем же (иначе тесты будут разные). И базу хотелось бы попроще, чтобы быстро эти состояния откатывать.
Или, например, ты тестируешь код, который рассылает полезные SMS. А для непосредственно рассылок он использует какой-нибудь платный SMS Gateway. Хорошо бы для тестирования кода подсунуть ему некий виртуальный Gateway, чтобы не рассылать сотни SMS непонятно кому.
Или твой код запрашивает данные у других веб-серверов, которые банально недоступны на тестовом сервере. Или ты пишешь код для интернет-платежей, который нужно 50 раз протестировать, и только потом допускать к реальным финансовым каналам.
Думаю, вы поняли... Виртуальные объекты или как их еще называют объекты-заглушки очень полезная штука.
И тут возникает сложность - в Java-то статическая типизация. Значит, чтобы вместо объекта типа ReadDatabase присвоить переменной ссылку на объект VirtualDatabase(), нужно унаследовать класс VirtualDatabase от RealDatabase.
Затем оказывается, что у класса RealDatabase куча приватных методов и переменных, которые хранят ссылки на другие реальные объекты, и нормальную заглушку таким образом не напишешь. В теории хороший, но на практике тупиковый вариант.
И тут нам на помощь приходит DynamicProxy (более подробно можно почитать), которые появились еще в Java 5. Она позволяет создавать виртуальные объекты, к которым у компилятора нет претензий.
Такие виртуальные объекты называют mock'ами (от слова mock - макет). Библиотека Mockito смогла вознести работу с этими моками на небывалую высоту. Отсюда, кстати, и название библиотеки.

--------------------------------------------------------------------------------------------------------------------

Аннотация @ExtendWith

Библиотека Mockito отлично работает вместе с JUnit, ее можно даже рассматривать как его расширение.
Есть два способа активировать работу библиотеки Mockito в ваших unit-тестах.

Первый способ - это добавить специальную аннотацию
@ExtendWith(MockitoExtension.class)

Второй способ - включить ее работу вызвав метод openMocks()

--------------------------------------------------------------------------------------------------------------------

Аннотация @Mock

Есть два способа работы с мок-объектами в Mockito. Первый - это создать полностью виртуальный объект, второй - это обернуть существующий объект в некую обертку. Начнем с первого.
Чтобы создать полностью виртуальный объект, нужно написать код:

ИмяКласса имяПеременной = Mockito.mock(ИмяКласса.class);

Или добавить аннатацию @Mock. Во втором случае MockitoExtension сам проанализирует код класса и создаст нужные заглушки. Вызывать метод Mockito.mock() не нужно. Одна аннотация и виртуальный объект готов. Красота.

--------------------------------------------------------------------------------------------------------------------

Аннотация @Spy

Второй важный тип объектов в Mockito - это обертки над существующими объектами. Они позволяют с одной стороны пользоваться уже существующими классами, а с другой - перехватывать обращение ко всем методам и переменным таких объектов: подкорректировать их работу, где это нужно. Используются так же часто, как и Mock-объекты.
Чтобы создать обертку над объектом, нужно написать код:

ИмяКласса имяПеременной = Mockito.spy(объект);

В самом простом варианте обращение к объекту-обертке просто перенаправляет вызовы к оригинальному объекту, ссылку на который он хранит у себя внутри. Все будет работать, как и с оригинальным объектом.
Создать обертку так же можно с помощью аннотации - @Spy.

--------------------------------------------------------------------------------------------------------------------

Допустим, ты создал фейковый мок-объект, но ведь нужно чтобы он как-то работал. При вызове определенных методов делалось что-то важное или методы возвращали определенный результат. Что делать?

Библиотека Mockito позволяет добавить мок-объекту нужное поведение.
Если ты хочешь, чтобы при вызове определенного метода, мок-объект вернул определенный результат, то это "правило" можно добавить объекту с помощью кода:

Mockito.doReturn(результат).when(объект).имяМетода();
Mockito.doReturn(10).when(mockList).size()

Видишь, в конце вызова метода имяМетода?
На самом деле никакого вызова тут не происходит. Метод doReturn() возвращает специальный proxy-объект с помощью которого следит за вызовами методов объекта и, таким образом, идет запись правила.
Еще раз. Это просто такой хитрый способ записать правило, которое нужно добавить к мок-обекту. Нужна определенная сноровка, чтобы правильно интерпретировать такой код в своей голове, когда его видишь. С опытом приходит.

--------------------------------------------------------------------------------------------------------------------

Метод when()

Есть еще один способ добавить правило поведения к мок-объекту - через вызов метода Mockito.when(). Выглядит вот так:

Mockito.when(объект.имяМетода()).thenReturn(результат);

Это такой же способ записи правила поведения мок-объекта, как и предыдущий.
Сравните:
Mockito.doReturn(результат).when(объект).имяМетода();

Тут происходит абсолютно одно и то же - конструирование нового правила.
Правда первый пример имеет два минуса:

вызов объект .имяМетода() сильно сбивает с толку.

не будет работать, если метод имяМетода() возвращает void.

--------------------------------------------------------------------------------------------------------------------

А как сделать так, чтобы чтобы метод мок-объекта кинул определенное исключение?

Чтобы метод не вернул, а именно выбросил (throw) исключение, нужно задать правило с помощью метода doThrow().

Mockito.doThrow(исключение.class).when(объект).имяМетода();

И сразу второй вариант:
Mockito.when(объект.имяМетода).thenThrow(исключение.class);

Если нужно выкинуть определенный объект-исключение, то воспользуйся конструкцией вида:

Mockito.doThrow(new Исключение()).when(объект).имяМетода();

Просто передай в метод doThrow() объект исключения и он будет выброшен во время вызова метода.

--------------------------------------------------------------------------------------------------------------------

А как создавать правила для методов с параметрами?

Если вы хотите, чтобы при определенном параметре метод возвращал что-то определенное, то правило можно записать так:

Mockito.doReturn(результат).when(объект).имяМетода(параметр);

--------------------------------------------------------------------------------------------------------------------

А как быть, если метод требует аргументы, но при любых значениях должен возвращать один и тот же результат?

Если ты хочешь добавить правило mock-объекту, которое действует для метода с любыми аргументами, то для этого есть специальный объект:

Mockito.any()

Наш пример с его помощью будет записан так:
Mockito.doReturn("Иван").when(mockList).get(any(int.class));

Есть тут пара нюансов. Объект Mockito.any() имеет тип Object, поэтому для параметров разных типов есть его аналоги: any() any(ClassName.class) anyInt() anyBoolean() anyDouble() anyList()

Более корректно наш пример будет выглядеть так:
Mockito.doReturn("Иван").when(mockList).get(anyInt());

--------------------------------------------------------------------------------------------------------------------

Рано или поздно наступит ситуация, когда ты захочешь, чтобы виртуальный метод имел сложное поведение. Например, он должен возвращать значения в зависимости от параметров, возводить число в квадрат.

Для этого есть специальный метод - doAnswer(), в который передается функция, которая делает то, что тебе нужно:

Mockito.doAnswer(функция).when(объект).имяМетода(параметр);


Mockito.doAnswer(invocation -> {
int parameter = invocation.getArgument(0);
return parameter * parameter; }).when(mockList).get(anyInt());

assertEquals(100, mockList.get(10));
assertEquals(25, mockList.get(5));

--------------------------------------------------------------------------------------------------------------------

Часто возникает еще одна интересная задача - убедиться, что тестируемый класс вызвал нужные методы нужных объектов. Более того, вызвал нужное число раз, с правильными параметрами и тому подобное. Как это сделать?

Для этого в Mockito тоже есть немного магии - семейство методов Mockito.verify(...). Общее правило, которым задается проверка вызова метода, имеет вид:

Mockito.verify(объект).имяМетода(параметр);

//вызов метода
String name = mockList.get(10);
//проверяем вызывался ли метод Mockito.verify(mockList).get(10);

Во время вызова метода verify() мы задали правило, что у объекта mockitoList должен вызваться метод get() с параметром 10.

--------------------------------------------------------------------------------------------------------------------

Тебе нужно проверить не просто факт, что метод вызывался, а например, что он вызывался 3 раза. Как это сделать?

Мы не будем спрашивать, можно ли это сделать, мы сразу спросим: как записать такое правило? И опять Mockito нас не подводит. Правило можно задать в виде:

Mockito.verify(объект,количество).имяМетода(параметр);

Важно! Количество - это не тип int, а специальный объект, которые может задавать различные шаблоны. Помнишь разные варианты метода any() ? Тут тоже самое - есть специальные методы, с помощью которых можно задавать различные сценарии: never() times(n) atLeast(n) atLeastOnce() atMost(n) only()

String name1 = mockList.get(1); //вызов метода
String name2 = mockList.get(2); //вызов метода
String name3 = mockList.get(3); //вызов метода
//проверяем, что метод get() вызывался 3 раза Mockito.verify(mockList, times(3)).get(anyInt());

Ты также можешь потребовать, чтобы кроме указанных вызовов метода, никаких других обращений к объекту не было. Для этого есть правило:
Mockito.verifyNoMoreInteractions(объект);

--------------------------------------------------------------------------------------------------------------------

Как проверить порядок вызова методов?

Жесткий порядок вызова методов можно задать с помощью специального объекта InOrder. Сначала его нужно создать:

InOrder inOrder = Mockito.inOrder(объект);

А затем уже ему добавлять правила посредством вызова методов verify().

List<String> mockedList = mock(MyList.class); mockedList.size();
mockedList.add("a parameter");
mockedList.clear();

InOrder inOrder = Mockito.inOrder(mockedList);
inOrder.verify(mockedList).size(); inOrder.verify(mockedList).add("a parameter"); inOrder.verify(mockedList).clear();

--------------------------------------------------------------------------------------------------------------------

Проверка исключений в Mockito

Факт того, что исключения возникли, проверяется немного по другому. Для этого нужно использовать метод assertThrows(). Общий формат такой проверки имеет вид:

Assertions.assertThrows(исключение.class, () -> объект.имяМетода());

//задаем поведение метода (нужно только для демонстрации) Mockito.when(mockList.size()).thenThrow(IllegalStateException.class);

//проверяем бросится ли IllegalStateException при вызове метода size assertThrows(IllegalStateException.class, () -> mockList.size());

--------------------------------------------------------------------------------------------------------------------

Мокирование статического метода mockStatic()

И еще один важный момент - мокирование и верификация статических методов. "А что в этом такого?", - спросишь ты. Да, статические, но ведь методы же. И будете неправы.

Помни, с чего мы начали изучение мок-объектов? С того, что эти объекты искусственно создаются через класс DynamicProxy. А статические методы ни к каким объектам не привязаны и перехватить вызовы к ним через DynamicProxy нельзя. Вот и все.

Но создатели Mockito и тут смогли извернуться - написали свой загрузчик классов и с его помощью смогли подменять классы на лету. Большая и сложная работа, но они все-таки смогли это сделать.

1 Создаем специальный мок-объект класса:
MockedStatic<ИмяКласса>управляющийОбъек
т = Mockito.mockStatic(ИмяКласса.class);

2 Добавляем к этому объекту правила работы:
К этому объекту правила нужно цеплять другими способами.
управляющийОбъект.when(ИмяКласса::имяМетода).thenReturn(результат);

3 Обязательно заворачиваем использование этого объекта в try-with-resources, чтобы объект сразу удалился и Mockito могло очистить связанные с ним правила.

--------------------------------------------------------------------------------------------------------------------