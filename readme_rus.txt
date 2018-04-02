������! ������ �������� ������������ ������� ��������� ��� ��������� � ������� selenium ������.

������� ���������� �� ������������� ���������� ����.

������������ ������� ������ MAVEN.

���������� ������� ����� ���������� � �������� url, ���������� � CONFIG\tests.properties

��� �� ����� ������� ����� �����:

url = https://www.consumerreports.org/cro/a-to-z-index/products/index.htm
;seleniumGrid = http://10.8.11.55:4444/wd/hub

� ����� DRIVERS ���� ������������ ���������� ������ ���������. �� ������ ������ ����� ����� �������� ������ � CHROME, ��� ������ ��������� ��� ���� �� �����.

� ����� LOGS ����������� ����� � ������� ������ - LOG.HTML

� ����� PAGESOURCES ����������� HTML ��� �������� � ������ ������� �����.

� ����� SCREENSHOTS ����������� �������� �������� � ������ ������� �����. ��������� ����� ����� ������� � ������ LOG.HTML

������ PAGE OBJECT ����� ����������� � SRC\TEST\JAVA\PAGES

������ ������ ����� ����������� � SRC\TEST\JAVA\TESTS

� ����� UTILS ����������� ��������������� ������.

� ����� ������� ����� TESTNG.XML, � ������� ����� ����������� ����� ������ � ������� ���������.

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

����� ����� ��������� ����� IDE ��� ���� ������ TEST.
� IntelliJ IDEA ����� ����� ��������� ����� ���� RUN - EDIT CONFIGURATIONS, ������� ����������� ������������ (��������, TestNG - Suite).

����� ����������� �� ���������������, � �����������. ���������� ������� ����� ������������ � TESTNG.XML: thread-count="2" data-provider-thread-count="2"