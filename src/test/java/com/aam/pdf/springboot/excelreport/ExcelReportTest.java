package com.aam.pdf.springboot.excelreport;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
class ExcelReportTest {

    /**
     * Пустой отчет, без заголовка
     */
    @Test
    void test1() {
        ExcelReport report = new ExcelReport(new ExcelConfig()
                .setOutputFileName("test1.xlsx")
        );
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<ArrayList<String>> batch = new ArrayList<>();
        report.createReport(headers, batch);
        report.getReport();
    }

    /**
     * Пустой отчет, с заголовком
     */
    @Test
    void test2() {
        ExcelReport report = new ExcelReport(new ExcelConfig()
                .setOutputFileName("test2.xlsx")
                .setHeader("Заголовок. Header.")
        );
        ArrayList<String> headers = new ArrayList<>();
        ArrayList<ArrayList<String>> batch = new ArrayList<>();
        report.createReport(headers, batch);
        report.getReport();
    }

    /**
     * Полноценный отчет, с заголовком
     */
    @Test
    void test3() {
        ExcelReport report = new ExcelReport(new ExcelConfig()
                .setOutputFileName("test3.xlsx")
                .setHeader("Сравнительная характеристика языков программирования.")
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList(
                "Название языка",
                "Год изобретения",
                "Парадигма",
                "Полнота по Тьюрингу",
                "Уровень",
                "Машинная зависимость",
                "Стандарт ISO",
                "Официальное положение"
        ));
        ArrayList<ArrayList<String>> firstBatch = new ArrayList<ArrayList<String>>();
        firstBatch.add(new ArrayList(Arrays.asList(
                "АЛГОЛ",
                "1958",
                "Структурный",
                "Да",
                "Высокий",
                "Нет",
                "ISO 1538",
                "ЯП"
        )));
        firstBatch.add(new ArrayList(Arrays.asList(
                "C++",
                "1983",
                "Мультипарадигменный",
                "Да",
                "Высокий",
                "Нет",
                "ISO/IEC 14882 C++",
                "ЯП"
        )));
        firstBatch.add(new ArrayList(Arrays.asList(
                "C",
                "1972",
                "Процедурный",
                "Да",
                "Низкий",
                "Да/Нет",
                "ISO/IEC 9899:2011",
                "ЯП"
        )));
        report.createReport(headers, firstBatch);
        ArrayList<ArrayList<String>> secondBatch = new ArrayList<ArrayList<String>>();
        secondBatch.add(new ArrayList(Arrays.asList(
                "Assembler",
                "1950",
                "Полнофункциональный",
                "Да",
                "Низкий",
                "Да",
                "",
                "ЯП"
        )));
        secondBatch.add(new ArrayList(Arrays.asList(
                "SQL",
                "1989",
                "Декларативный",
                "Нет",
                "Высокий",
                "Нет",
                "ISO/IEC 9075:1992",
                "Язык структурированных запросов"
        )));
        secondBatch.add(new ArrayList(Arrays.asList(
                "1C",
                "",
                "Предметно-ориентированный",
                "Да",
                "Высокий",
                "",
                "",
                "ЯП"
        )));
        secondBatch.add(new ArrayList(Arrays.asList(
                "Haskel",
                "1990",
                "Функциональный",
                "Да",
                "Высокий",
                "Нет",
                "",
                "ЯП"
        )));
        report.addBatch(secondBatch);
        ArrayList<ArrayList<String>> thirdBatch = new ArrayList<ArrayList<String>>();
        thirdBatch.add(new ArrayList(Arrays.asList(
                "HTML",
                "1986",
                "Декларативный",
                "Нет",
                "Высокий",
                "Нет",
                "ISO-8859-1",
                "Язык гипертекстовой разметки"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "CSS",
                "1996",
                "Декларативный",
                "Нет",
                "Высокий",
                "Нет",
                "",
                "Формальный язык разметки"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "Java",
                "1995",
                "Объектно-ориентированный",
                "Да",
                "Высокий",
                "Нет",
                "",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "JavaScript",
                "1995",
                "Объектно-ориентированный",
                "Да",
                "Высокий",
                "Нет",
                "",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "Python",
                "1991",
                "Объектно-ориентированный",
                "Да",
                "Высокий",
                "Нет",
                "",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "ПРОЛОГ",
                "1972",
                "Декларативный\nЛогический",
                "Да",
                "Высокий",
                "Нет",
                "ISO/IEC\nJTC1/SC22/WG17",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "XML",
                "1998",
                "Декларативный",
                "Нет",
                "Высокий",
                "Нет",
                "ISO 8879:1986",
                "Расширяемый язык разметки"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "Brainfuck",
                "1993",
                "Эзотерический",
                "Да",
                "Низкий",
                "Да",
                "",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "Whitespace",
                "2003",
                "Эзотерический",
                "Да",
                "Низкий",
                "Да",
                "",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "ДРАКОН",
                "1996",
                "Графический",
                "Да",
                "Высокий",
                "Да",
                "ISO 5807-85",
                "ЯП"
        )));
        report.addBatch(thirdBatch);
        report.getReport();
    }

    /**
     * Полноценный отчет, без заголовка, большой шрифт
     */
    @Test
    void test4() {
        ExcelReport report = new ExcelReport(new ExcelConfig()
                .setOutputFileName("test4.xlsx")
                .setFontSize(40)
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList(
                "Название языка",
                "Год изобретения",
                "Парадигма",
                "Полнота по Тьюрингу",
                "Уровень",
                "Машинная зависимость",
                "Стандарт ISO",
                "Официальное положение"
        ));
        ArrayList<ArrayList<String>> firstBatch = new ArrayList<ArrayList<String>>();
        firstBatch.add(new ArrayList(Arrays.asList(
                "АЛГОЛ",
                "1958",
                "Структурный",
                "Да",
                "Высокий",
                "Нет",
                "ISO 1538",
                "ЯП"
        )));
        firstBatch.add(new ArrayList(Arrays.asList(
                "C++",
                "1983",
                "Мультипарадигменный",
                "Да",
                "Высокий",
                "Нет",
                "ISO/IEC 14882 C++",
                "ЯП"
        )));
        firstBatch.add(new ArrayList(Arrays.asList(
                "C",
                "1972",
                "Процедурный",
                "Да",
                "Низкий",
                "Да/Нет",
                "ISO/IEC 9899:2011",
                "ЯП"
        )));
        report.createReport(headers, firstBatch);
        ArrayList<ArrayList<String>> secondBatch = new ArrayList<ArrayList<String>>();
        secondBatch.add(new ArrayList(Arrays.asList(
                "Assembler",
                "1950",
                "Полнофункциональный",
                "Да",
                "Низкий",
                "Да",
                "",
                "ЯП"
        )));
        secondBatch.add(new ArrayList(Arrays.asList(
                "SQL",
                "1989",
                "Декларативный",
                "Нет",
                "Высокий",
                "Нет",
                "ISO/IEC 9075:1992",
                "Язык структурированных запросов"
        )));
        secondBatch.add(new ArrayList(Arrays.asList(
                "1C",
                "",
                "Предметно-ориентированный",
                "Да",
                "Высокий",
                "",
                "",
                "ЯП"
        )));
        secondBatch.add(new ArrayList(Arrays.asList(
                "Haskel",
                "1990",
                "Функциональный",
                "Да",
                "Высокий",
                "Нет",
                "",
                "ЯП"
        )));
        report.addBatch(secondBatch);
        ArrayList<ArrayList<String>> thirdBatch = new ArrayList<ArrayList<String>>();
        thirdBatch.add(new ArrayList(Arrays.asList(
                "HTML",
                "1986",
                "Декларативный",
                "Нет",
                "Высокий",
                "Нет",
                "ISO-8859-1",
                "Язык гипертекстовой разметки"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "CSS",
                "1996",
                "Декларативный",
                "Нет",
                "Высокий",
                "Нет",
                "",
                "Формальный язык разметки"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "Java",
                "1995",
                "Объектно-ориентированный",
                "Да",
                "Высокий",
                "Нет",
                "",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "JavaScript",
                "1995",
                "Объектно-ориентированный",
                "Да",
                "Высокий",
                "Нет",
                "",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "Python",
                "1991",
                "Объектно-ориентированный",
                "Да",
                "Высокий",
                "Нет",
                "",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "ПРОЛОГ",
                "1972",
                "Декларативный\nЛогический",
                "Да",
                "Высокий",
                "Нет",
                "ISO/IEC\nJTC1/SC22/WG17",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "XML",
                "1998",
                "Декларативный",
                "Нет",
                "Высокий",
                "Нет",
                "ISO 8879:1986",
                "Расширяемый язык разметки"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "Brainfuck",
                "1993",
                "Эзотерический",
                "Да",
                "Низкий",
                "Да",
                "",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "Whitespace",
                "2003",
                "Эзотерический",
                "Да",
                "Низкий",
                "Да",
                "",
                "ЯП"
        )));
        thirdBatch.add(new ArrayList(Arrays.asList(
                "ДРАКОН",
                "1996",
                "Графический",
                "Да",
                "Высокий",
                "Да",
                "ISO 5807-85",
                "ЯП"
        )));
        report.addBatch(thirdBatch);
        report.getReport();
    }

}