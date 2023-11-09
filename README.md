# Just another simple framework for writing and running selenium tests (Java + Maven + TestNG + Selenium)

## Description

First of all, for the sake of transparency - I use Java 1.8 Oracle Open JDK 1.8.0_361 to run this project.

So unless you are forced to use Java 11 or any other Java version > 8, please install and use JDK 1.8.0_XXX.

Probably you can also simply set in IDE settings: File - Project Structure - Project - Language level = 8, but I did not test those configurations.

Config can be found in "config/tests.properties"

```
#startUrlForAllTests = https://www.target.com
#seleniumGrid = http://10.157.153.36:4444/wd/hub
implicitWaitTimeout = 5
#browser = chrome
browser = firefox
```

Test run report will be created in 'logs' folder.

HTML page sources are saved to the 'pagesources' folder for failed tests.

Screenshots are saved to the 'screenshots' folder for failed tests.

Create your Page Object classes in - src/test/java/pages

Create your Test scenarios classes in - src/test/java/tests

## How to run tests

At the root of the project there is a couple of testng xml files, where you can specify which classes with tests to be run.
You will need these files when running from CI/CD like Jenkins etc.
Most likely you are not going to use them during local development of your tests on your machine.

testNgUITestSuite.xml
testNgApiTestSuite.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Test suite name in xml - UI tests" parallel="methods" thread-count="3" data-provider-thread-count="2">
    <listeners>
        <listener class-name="listeners.TestListener" />
        <listener class-name="listeners.retry.RetryTestListener" />
    </listeners>
    <test name="Test class run name in xml - UI tests">
        <classes>
            <class name="tests.UiTests" />
        </classes>
    </test>
</suite>
```

Tests can also be run through the IDE itself or maven goal "test". From the root of the project run:

```
mvn clean test
```

Please keep in mind that `mvn clean test` will run whatever is specified in the pom.xml inside <suiteXmlFiles> tag

```
<suiteXmlFiles>
  <suiteXmlFile>testNgUITestSuite.xml</suiteXmlFile>
</suiteXmlFiles>
```

If you try to run via `mvn clean test` and see the error
```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.10.1:testCompile (default-testCompile) on project tests: Compilation failure
[ERROR] No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?
```
Try to add JAVA_HOME variable with your JDK path into your .zshrc config file for your terminal (if you use zsh and not bash, for bash you will need to add it to .bash_profile config) which should be located in your user home folder (cd ~/ on a Mac).
```
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_361.jdk/Contents/Home
```

In IntelliJ IDEA, tests can be run via Run -> Edit Configurations menu, add 'TestNG - Suite' configuration and specify the path to the testng xml file.

But most likely, during local development, you will be running your tests using green "run" icon on the left of your test method names.

Tests can be run in parallel. The number of threads can be adjusted in testng xml file, e.g.: thread-count = "2" data-provider-thread-count = "2"

Tests retry mechanism is also implemented and in place. By default, retry runs only when you run your tests on selenium grid and does not run when you run tests locally, otherwise it makes test debugging annoying.

## HTML report

To generate and see Allure report, install Allure command line tools first, for example from here https://www.npmjs.com/package/allure-commandline

Run `npm install -g allure-commandline` if you have npm installed.

Then you can run command from the root of the project:

```
allure serve allure-results
```

Allure report will be generated and automatically opened in your default browser.

Additional standalone HTML report file (Extent Reports) is created in 'logs' folder (when running via maven).

# Troubleshooting

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

Or do a separate maven installation outside IDE.