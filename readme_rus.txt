Привет! Вашему вниманию предлагается простой фреймворк для написания и запуска selenium тестов.

Краткая инструкция по использованию приводится ниже.

Используется система сборки MAVEN.

Выполнение каждого теста начинается с открытия url, указанного в CONFIG\tests.properties

Там же можно указать адрес грида:

url = https://www.consumerreports.org/cro/a-to-z-index/products/index.htm
;seleniumGrid = http://10.8.11.55:4444/wd/hub

В папку DRIVERS надо подкладывать актуальные версии драйверов. На данный момент тесты будут работать только в CHROME, для других браузеров код пока не писал.

В папку LOGS сохраняется отчёт о прогоне тестов - LOG.HTML

В папку PAGESOURCES сохраняется HTML код страницы в случае падения теста.

В папку SCREENSHOTS сохраняется скриншот страницы в случае падения теста. Скриншоты также можно увидеть в отчёте LOG.HTML

Классы PAGE OBJECT нужно располагать в SRC\TEST\JAVA\PAGES

Классы тестов нужно располагать в SRC\TEST\JAVA\TESTS

В папке UTILS расположены вспомогательные классы.

В корне проекта лежит TESTNG.XML, в котором нужно прописывать какие классы с тестами запускать.

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Consumer Reports Tests" parallel="methods" thread-count="2" data-provider-thread-count="2">
    <listeners>
        <listener class-name="utils.TestListener" />
        <listener class-name="utils.SuiteListener" />
    </listeners>
    <test name="test name from testng.xml">
        <classes>
            <class name="tests.ConsumerReportsAZProductsPageTests" />
        </classes>
    </test>
</suite>

Тесты можно запускать через IDE или цель мавена TEST.
В IntelliJ IDEA тесты можно запустить через меню RUN - EDIT CONFIGURATIONS, добавив необходимую конфигурацию (например, TestNG - Suite).

Тесты запускаются не последовательно, а параллельно. Количество потоков можно регулировать в TESTNG.XML: thread-count="2" data-provider-thread-count="2"