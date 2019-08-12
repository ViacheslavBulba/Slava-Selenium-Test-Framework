# Just another simple framework for writing and running selenium tests (Java + Maven + TestNG + Extent Reports)

## Description

The execution of each test begins with the opening of the url specified in CONFIG\tests.properties

There you can also specify the grid address for the remote launch of the tests:

```
url = https://www.consumerreports.org/cro/a-to-z-index/products/index.htm
;seleniumGrid = http://10.157.153.36:4444/wd/hub
timeout = 5
```

In the DRIVERS folder, you need to add the current driver versions. At the moment, the tests will work only in CHROME, for other browsers the code has not been yet written.

The test run report is stored in the LOGS folder - LOG.HTML

The HTML code of the page is saved in the PAGESOURCES folder if a test is failed.

The screenshot of the page is saved in the SCREENSHOTS folder in case of a fail in a test. Screenshots can also be seen in the report inside LOG folder.

PAGE OBJECT classes must be located in SRC \ TEST \ JAVA \ PAGES

The test classes must be located in SRC \ TEST \ JAVA \ TESTS

## How to run tests

At the root of the project there is a TESTNG.XML, in which it is necessary to prescribe which classes with tests to be run.

```
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
```

Tests can be run through the IDE or maven goal "test".

In IntelliJ IDEA, tests can be run via the RUN - EDIT CONFIGURATIONS menu, adding the necessary configuration (for example, TestNG - Suite).

Tests are run in parallel. The number of threads can be adjusted in TESTNG.XML: thread-count = "2" data-provider-thread-count = "2"