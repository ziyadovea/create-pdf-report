package com.aam.pdf.springboot.pdfreport;

import com.itextpdf.kernel.geom.PageSize;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс для тестирования построения отчета
 */
@SpringBootTest
class PdfReportTest {

    /**
     * Тест 1 - в конфиге меняется название файла, выключается нумерацмя
     * Пустые заголовки и пакет
     * @throws FileNotFoundException
     */
    @Test
    void test1() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test1.pdf")
                .setPageNumeration(false)
        );
        ArrayList<String> headers = new ArrayList<String>();
        ArrayList<ArrayList<String>> batch = new ArrayList<ArrayList<String>>();
        report.createReport(headers, batch);
        report.getReport();
    }

    /**
     * Тест 2 - в конфиге меняется название файла, заголовок на первой странице и нумерация
     * Пустые заголовки, непустой пакет
     * @throws FileNotFoundException
     */
    @Test
    void test2() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test2.pdf")
                .setHeader("Заголовок")
                .setHeaderOnEachPage(false)
                .setPageNumeration(true)
        );
        ArrayList<String> headers = new ArrayList<String>();
        ArrayList<ArrayList<String>> batch = new ArrayList<ArrayList<String>>();
        batch.add(new ArrayList(Arrays.asList("a", "b", "c")));
        batch.add(new ArrayList(Arrays.asList("d", "e", "f")));
        batch.add(new ArrayList(Arrays.asList("g", "h", "i")));
        report.createReport(headers, batch);
        report.getReport();
    }

    /**
     * Тест 3 - в конфиге меняется название файла и нумерация.
     * Заголовок не передается, но устанавливается параметр его дублирования
     * Непустые заголовки, пустой пакет
     * @throws FileNotFoundException
     */
    @Test
    void test3() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test3.pdf")
                .setHeaderOnEachPage(true)
                .setPageNumeration(true)
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList("Header 1", "Header 2", "Header 3"));
        ArrayList<ArrayList<String>> batch = new ArrayList<ArrayList<String>>();
        report.createReport(headers, batch);
        report.getReport();
    }

    /**
     * Заголовок на каждой странице, нумерация
     * Непустые заголовки, которые надо перенести, пустой пакет
     * @throws FileNotFoundException
     */
    @Test
    void test4() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test4.pdf")
                .setHeader("Главный заголовок")
                .setHeaderOnEachPage(true)
                .setPageNumeration(true)
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList(
                "Заголовок 1", "Заголовок 2", "Заголовок 3", "Заголовок 4", "Заголовок 5", "Заголовок 6", "Заголовок 7",
                "Заголовок 8", "Заголовок 9", "Заголовок 10", "Заголовок 11")
        );
        ArrayList<ArrayList<String>> batch = new ArrayList<ArrayList<String>>();
        report.createReport(headers, batch);
        report.getReport();
    }

    /**
     * Аналогично тесту 4, но меняются размеры страницы и ориентация
     * @throws FileNotFoundException
     */
    @Test
    void test5() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test5.pdf")
                .setHeader("Главный заголовок")
                .setHeaderOnEachPage(true)
                .setPageNumeration(true)
                .setPageSize(PageSize.A7)
                .setOrientation(Orientation.LANDSCAPE)
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList(
                "Заголовок 1", "Заголовок 2", "Заголовок 3", "Заголовок 4", "Заголовок 5", "Заголовок 6", "Заголовок 7",
                "Заголовок 8", "Заголовок 9", "Заголовок 10", "Заголовок 11")
        );
        ArrayList<ArrayList<String>> batch = new ArrayList<ArrayList<String>>();
        report.createReport(headers, batch);
        report.getReport();
    }

    /**
     * Тестируется случай, когда в колонке очень длинное слово, которое не переносится по правилам заданного языка
     * и для этой ячейки нужна отдельная колонка, которая перенесет данное слово на новую строку
     * @throws FileNotFoundException
     */
    @Test
    void test6() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test6.pdf")
                .setHeaderOnEachPage(false)
                .setPageNumeration(false)
                .setLang("ru")
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList(
                "verylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongword",
                "i'm small :(")
        );
        ArrayList<ArrayList<String>> batch = new ArrayList<ArrayList<String>>();
        report.createReport(headers, batch);
        report.getReport();
    }

    /**
     * Аналогично тесту 6, но колонка с очень длинным словом не первая
     * @throws FileNotFoundException
     */
    @Test
    void test7() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test7.pdf")
                .setHeaderOnEachPage(false)
                .setPageNumeration(false)
                .setLang("ru")
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList(
                "hi!",
                "verylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongword",
                "i'm small :(",
                "Salam!")
        );
        ArrayList<ArrayList<String>> batch = new ArrayList<ArrayList<String>>();
        report.createReport(headers, batch);
        report.getReport();
    }

    // Теперь тестируем не только заголовки, но и добавление пакетов.
    // Но так как они работают аналогично заголовкам, с ними все работать должно нормально.

    /**
     * Тестируются заголовки и добавления пакетов
     * Есть случай, когда в строке пакета не хватает данных (добавляется пустая ячейка)
     * и случай, когда в строке пакета слишком много данных (берутся только нужные)
     * @throws FileNotFoundException
     */
    @Test
    void test8() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test8.pdf")
                .setHeader("Основные сведения об отчетах в Access.")
                .setHeaderOnEachPage(true)
                .setPageNumeration(true)
                .setLang("ru")
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList(
                "Раздел",
                "Отображение раздела при печати",
                "Использование раздела"
        ));
        ArrayList<ArrayList<String>> firstBatch = new ArrayList<ArrayList<String>>();
        firstBatch.add(
                new ArrayList(Arrays.asList(
                        "Заголовок отчета.",
                        "В начале отчета.",
                        "В заголовок включается информация, обычно помещаемая на обложке, например эмблема компании, название отчета или дата. " +
                                "Если в заголовке отчета помещен вычисляемый элемент управления, использующий статистическую функцию Sum, сумма " +
                                "рассчитывается для всего отчета. Заголовок отчета печатается перед верхним колонтитулом."
                )
        ));
        firstBatch.add(
                new ArrayList(Arrays.asList(
                    "Верхний колонтитул.",
                    "Вверху каждой страницы.",
                    "Верхний колонтитул используется в тех случаях, когда нужно, чтобы название отчета повторялось на каждой странице."
                )
        ));
        firstBatch.add(
                new ArrayList(Arrays.asList(
                    "Заголовок группы.",
                    "В начале каждой новой группы записей.",
                    "Используется для печати названия группы. Например, если отчет сгруппирован по изделиям, в заголовках групп можно указать их названия. Если поместить в заголовок группы вычисляемый элемент управления, использующий статистическую функцию Sum, сумма будет рассчитываться для текущей группы. Заголовок группы может состоять из нескольких разделов в зависимости от добавленных уровней группирования. Дополнительные сведения о создании верхних и нижних колонтитулов группы см. раздел «Добавление группирования, сортировки или итоговых данных»."
                )
        ));
        report.createReport(headers, firstBatch);
        ArrayList<ArrayList<String>> secondBatch = new ArrayList<ArrayList<String>>();
        secondBatch.add(
                new ArrayList(Arrays.asList(
                        "Область данных.",
                        "Отображается один раз для каждой строки в источнике записей.",
                        "В нем размещаются элементы управления, составляющие основное содержание отчета.",
                        "Примечание группы можно использовать для печати сводной информации по группе. Нижний колонтитул группы может состоять из нескольких разделов в зависимости от добавленных уровней группирования."
                )
        ));
        secondBatch.add(
                new ArrayList(Arrays.asList(
                        "Примечание группы.",
                        "В конце каждой группы записей."
                )
        ));
        secondBatch.add(
                new ArrayList(Arrays.asList(
                        "Нижний колонтитул.",
                        "Внизу каждой страницы.",
                        "Используется для нумерации страниц и для печати постраничной информации."
                )
        ));
        report.addBatch(secondBatch);
        report.getReport();
    }

    /**
     * Аналогично тесту 9, но играемся со шрифтом.
     * Маленький шрифт.
     * @throws FileNotFoundException
     */
    @Test
    void test9() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test9.pdf")
                .setHeader("Основные сведения об отчетах в Access.")
                .setHeaderOnEachPage(true)
                .setPageNumeration(true)
                .setFontSize(5)
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList(
                "Раздел",
                "Отображение раздела при печати",
                "Использование раздела"
        ));
        ArrayList<ArrayList<String>> firstBatch = new ArrayList<ArrayList<String>>();
        firstBatch.add(
                new ArrayList(Arrays.asList(
                        "Заголовок отчета.",
                        "В начале отчета.",
                        "В заголовок включается информация, обычно помещаемая на обложке, например эмблема компании, название отчета или дата. " +
                                "Если в заголовке отчета помещен вычисляемый элемент управления, использующий статистическую функцию Sum, сумма " +
                                "рассчитывается для всего отчета. Заголовок отчета печатается перед верхним колонтитулом."
                )
                ));
        firstBatch.add(
                new ArrayList(Arrays.asList(
                        "Верхний колонтитул.",
                        "Вверху каждой страницы.",
                        "Верхний колонтитул используется в тех случаях, когда нужно, чтобы название отчета повторялось на каждой странице."
                )
                ));
        firstBatch.add(
                new ArrayList(Arrays.asList(
                        "Заголовок группы.",
                        "В начале каждой новой группы записей.",
                        "Используется для печати названия группы. Например, если отчет сгруппирован по изделиям, в заголовках групп можно указать их названия. Если поместить в заголовок группы вычисляемый элемент управления, использующий статистическую функцию Sum, сумма будет рассчитываться для текущей группы. Заголовок группы может состоять из нескольких разделов в зависимости от добавленных уровней группирования. Дополнительные сведения о создании верхних и нижних колонтитулов группы см. раздел «Добавление группирования, сортировки или итоговых данных»."
                )
                ));
        report.createReport(headers, firstBatch);
        ArrayList<ArrayList<String>> secondBatch = new ArrayList<ArrayList<String>>();
        secondBatch.add(
                new ArrayList(Arrays.asList(
                        "Область данных.",
                        "Отображается один раз для каждой строки в источнике записей.",
                        "В нем размещаются элементы управления, составляющие основное содержание отчета.",
                        "Примечание группы можно использовать для печати сводной информации по группе. Нижний колонтитул группы может состоять из нескольких разделов в зависимости от добавленных уровней группирования."
                )
                ));
        secondBatch.add(
                new ArrayList(Arrays.asList(
                        "Примечание группы.",
                        "В конце каждой группы записей."
                )
                ));
        secondBatch.add(
                new ArrayList(Arrays.asList(
                        "Нижний колонтитул.",
                        "Внизу каждой страницы.",
                        "Используется для нумерации страниц и для печати постраничной информации."
                )
                ));
        report.addBatch(secondBatch);
        report.getReport();
    }

    /**
     * Аналогично тесту 8, но играемся со шрифтом.
     * Большой шрифт.
     * @throws FileNotFoundException
     */
    @Test
    void test10() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test10.pdf")
                .setHeader("Основные сведения об отчетах в Access")
                .setHeaderOnEachPage(true)
                .setPageNumeration(true)
                .setFontSize(30)
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList(
                "Раздел",
                "Отображение раздела при печати",
                "Использование раздела"
        ));
        ArrayList<ArrayList<String>> firstBatch = new ArrayList<ArrayList<String>>();
        firstBatch.add(
                new ArrayList(Arrays.asList(
                        "Заголовок отчета.",
                        "В начале отчета.",
                        "В заголовок включается информация, обычно помещаемая на обложке, например эмблема компании, название отчета или дата. " +
                                "Если в заголовке отчета помещен вычисляемый элемент управления, использующий статистическую функцию Sum, сумма " +
                                "рассчитывается для всего отчета. Заголовок отчета печатается перед верхним колонтитулом."
                )
                ));
        firstBatch.add(
                new ArrayList(Arrays.asList(
                        "Верхний колонтитул.",
                        "Вверху каждой страницы.",
                        "Верхний колонтитул используется в тех случаях, когда нужно, чтобы название отчета повторялось на каждой странице."
                )
                ));
        firstBatch.add(
                new ArrayList(Arrays.asList(
                        "Заголовок группы.",
                        "В начале каждой новой группы записей.",
                        "Используется для печати названия группы. Например, если отчет сгруппирован по изделиям, в заголовках групп можно указать их названия. Если поместить в заголовок группы вычисляемый элемент управления, использующий статистическую функцию Sum, сумма будет рассчитываться для текущей группы. Заголовок группы может состоять из нескольких разделов в зависимости от добавленных уровней группирования. Дополнительные сведения о создании верхних и нижних колонтитулов группы см. раздел «Добавление группирования, сортировки или итоговых данных»."
                )
                ));
        report.createReport(headers, firstBatch);
        ArrayList<ArrayList<String>> secondBatch = new ArrayList<ArrayList<String>>();
        secondBatch.add(
                new ArrayList(Arrays.asList(
                        "Область данных.",
                        "Отображается один раз для каждой строки в источнике записей.",
                        "В нем размещаются элементы управления, составляющие основное содержание отчета.",
                        "Примечание группы можно использовать для печати сводной информации по группе. Нижний колонтитул группы может состоять из нескольких разделов в зависимости от добавленных уровней группирования."
                )
                ));
        secondBatch.add(
                new ArrayList(Arrays.asList(
                        "Примечание группы.",
                        "В конце каждой группы записей."
                )
                ));
        secondBatch.add(
                new ArrayList(Arrays.asList(
                        "Нижний колонтитул.",
                        "Внизу каждой страницы.",
                        "Используется для нумерации страниц и для печати постраничной информации."
                )
                ));
        report.addBatch(secondBatch);
        report.getReport();
    }

    /**
     * Тест для большой таблицы
     */
    @Test
    void test11() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test11.pdf")
                .setHeader("Сравнительная характеристика языков программирования.")
                .setHeaderOnEachPage(true)
                .setPageNumeration(true)
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
     * Как тест 11, только автоматический подбор ширины страницы
     */
    @Test
    void test12() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test12.pdf")
                .setHeader("Сравнительная характеристика языков программирования.")
                .setHeaderOnEachPage(true)
                .setPageNumeration(true)
                .setAutoPageSize(true)
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
     * Как тест 5, только автоматический подбор ширины страницы
     */
    @Test
    void test13() throws FileNotFoundException {
        PdfReport report = new PdfReport(new PdfConfig()
                .setOutputDirectory("reports")
                .setOutputFileName("test13.pdf")
                .setHeader("Главный заголовок")
                .setHeaderOnEachPage(true)
                .setPageNumeration(true)
                .setPageSize(PageSize.A7)
                .setOrientation(Orientation.LANDSCAPE)
                .setAutoPageSize(true)
        );
        ArrayList<String> headers = new ArrayList(Arrays.asList(
                "Заголовок 1", "Заголовок 2", "Заголовок 3", "Заголовок 4", "Заголовок 5", "Заголовок 6", "Заголовок 7",
                "Заголовок 8", "Заголовок 9", "Заголовок 10", "Заголовок 11")
        );
        ArrayList<ArrayList<String>> batch = new ArrayList<ArrayList<String>>();
        report.createReport(headers, batch);
        report.getReport();
    }

}