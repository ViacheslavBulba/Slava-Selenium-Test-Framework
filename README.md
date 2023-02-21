# Just another simple framework for writing and running selenium tests (Java + Maven + TestNG + Selenium)

## Description

Specify start URL in config\tests.properties

There you can also specify selenium grid address for the remote launch of the tests:

```
url = https://www.consumerreports.org/cro/a-to-z-index/products/index.htm
;seleniumGrid = http://10.157.153.36:4444/wd/hub
timeout = 5
```

You need to add/update driver versions in 'drivers' folder. For simplicity - only Chrome browser is supported for now.

Test run report will be created in 'logs' folder.

The HTML code of the page is saved in the 'pagesources' folder if a test is failed.

The screenshot of the page is saved in the 'screenshot' folder if a test is failed. Screenshots can also be seen in the html report inside 'logs' folder.

Page Object classes - SRC \ TEST \ JAVA \ PAGES

Test scenarios classes - SRC \ TEST \ JAVA \ TESTS

## How to run tests

At the root of the project there is a testng.xml, where you can specify classes with tests to be run.

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
            <class name="tests.UiTests" />
        </classes>
    </test>
</suite>
```

Tests can be run through the IDE or maven goal "test".

In IntelliJ IDEA, tests can be run via Run -> Edit Configurations menu, add 'TestNG - Suite' configuration and specify the path to the testng.xml.

Tests can be run in parallel. The number of threads can be adjusted in testng.xml: thread-count = "2" data-provider-thread-count = "2"

Tests retry mechanism is implemented. Retry runs only when you run your tests on selenium grid and does not run when you run tests locally, otherwise it makes test debugging annoying.

## Allure report

allure serve allure-results

## Error zsh: command not found: mvn

```
cd ~/
code .zshrc
```

add lines and save, restart terminal

```
export M3_HOME="/Applications/IntelliJ IDEA CE.app/Contents/plugins/maven/lib/maven3"
export M3=$M3_HOME/bin
export PATH=$M3:$PATH
```