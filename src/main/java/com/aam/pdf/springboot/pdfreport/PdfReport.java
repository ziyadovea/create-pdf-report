package com.aam.pdf.springboot.pdfreport;

import com.aam.pdf.springboot.report.Report;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Класс для построения отчета в формате .pdf
 */
public class PdfReport implements Report {

    private Document document;
    private ArrayList<String> headers;
    private int columnCount;
    private ArrayList<ArrayList<String>> data;
    private PdfConfig config;

    private int tableCount;
    private Map<Integer, Integer> colsInTable;
    private ArrayList<Integer> largeCell;

    /**
     * Конструктор класса
     * @param config задает конфигурацию pdf файла
     */
    public PdfReport(PdfConfig config) {
        this.config = config;
    }

    /**
     * Метод для начала построения отчета
     * @param headers список заголовков таблицы
     * @param batch пакет значений таблицы
     * @throws FileNotFoundException
     */
    @Override
    public void createReport(ArrayList<String> headers, ArrayList<ArrayList<String>> batch) {
        // Проверка параметров на null
        if (headers == null || batch == null) {
            throw new NullPointerException();
        }
        // Заносим заголовки и первый пакет данных в соответствующие переменные класса
        this.headers = new ArrayList<String>(headers);
        // Проверяем, задана ли колонка с нумерацией
        if (this.config.isNumbersColumn()) {
            this.headers.add(0, "№");
        }
        this.columnCount = this.headers.size();
        this.data = new ArrayList<ArrayList<String>>();
        if (this.columnCount > 0 && batch.size() > 0) {
            this.addBatchToData(batch);
        }
    }

    /**
     * Метод, который добавляется очередной пакет в общую матрицу данных для конечного построения отчета
     * @param batch пакет значений таблицы
     */
    private void addBatchToData(ArrayList<ArrayList<String>> batch) {
        // Цикл по всем строкам пакета
        for (ArrayList<String> row : batch) {
            ArrayList<String> dataRow = new ArrayList<String>(this.columnCount);
            // Цикл по количеству колонок в выходной таблице
            for (int i = 0; i < this.columnCount; i++) {
                // Если в текущей строке пакета есть данные для текущей колонки, то добавляем их
                if (i < row.size()) {
                    dataRow.add(row.get(i));
                // Иначе добавляем пустую строку
                } else {
                    dataRow.add("");
                }
            }
            this.data.add(dataRow);
        }
    }

    /**
     * Метод создает файл для отчета с параметрами, заданными в конфигурации
     * @throws FileNotFoundException
     */
    private void createDocument() {
        // Создаем файл для отчета
        File file = new File("./" + config.getOutputDirectory() + "/" + config.getOutputFileName());
        PdfWriter pdfWriter = null;
        try {
            pdfWriter = new PdfWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        this.document = new Document(pdfDocument, config.getPageSize());
        this.document.setFont(config.getFont());

        // Устанавливаем заголовки и/или нумерацию
        this.addHeaderAndNumbering(pdfDocument);

        // Проверка, задан ли язык для корректного переноса слов
        if (config.getLang() != null) {
            this.document.setHyphenation(
                    new HyphenationConfig(config.getLang(), config.getLang().toUpperCase(), 2, 2)
            );
        }
    }

    /**
     * Метод, который определяет размер страницы при заданной ориентации,
     * если не установлен автоматической подбор размеры страницы
     */
    private void calculatePageSize() {
        // Ширина меньше высоты и альбомная ориентация - надо перевернуть
        if (config.getPageSize().getWidth() < config.getPageSize().getHeight()
                && config.getOrientation() == Orientation.LANDSCAPE) {
            config.setPageSize(config.getPageSize().rotate());
        // Ширина больше высоты и портерная ориентация - надо перевернуть
        } else if (config.getPageSize().getWidth() > config.getPageSize().getHeight()
                && config.getOrientation() == Orientation.PORTRAIT) {
            config.setPageSize(config.getPageSize().rotate());
        }
    }

    /**
     * Метод, который устанавливает заголовок и/или нумерацию страниц
     */
    private void addHeaderAndNumbering(PdfDocument pdfDocument) {

        boolean isHeader = false;
        // Проверка, есть ли у документа заголовк
        if (config.getHeader() != null) {

            // Заголовок на каждой странице и нумерация
            if (config.isHeaderOnEachPage() && config.isPageNumeration()) {
                pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE,
                        new HeaderAndNumberingEventHandler(this.document, config.getHeader(), true, this.config));
            // Заголовок на первой странице и нумерация
            } else if (!config.isHeaderOnEachPage() && config.isPageNumeration()) {
                isHeader = true;
                pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,
                        new HeaderAndNumberingEventHandler(this.document, true, this.config));
            }
            // Заголовок на каждой странице без нумерации
            else if (config.isHeaderOnEachPage() && !config.isPageNumeration()) {
                pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,
                        new HeaderAndNumberingEventHandler(this.document, config.getHeader(), this.config));
            // Заголовок на первой странице без нумерации
            } else {
                isHeader = true;
            }
            // Проверка, есть ли у документа нумерация
        } else if (config.isPageNumeration()) {
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,
                    new HeaderAndNumberingEventHandler(this.document, true, this.config));
        }

        pdfDocument.addNewPage();

        // Отдельно случай, когда заголовок только на 1 странице
        // Не нужно переопределять событие - добавляем 1 раз вручную и все
        if (isHeader) {
            float coordX = ((config.getPageSize().getLeft() + document.getLeftMargin())
                    + (config.getPageSize().getRight() - document.getRightMargin())) / 2;
            float headerY = config.getPageSize().getTop() - document.getTopMargin() + 10;
            Canvas canvas = new Canvas(pdfDocument.getPage(1), config.getPageSize());
            canvas.showTextAligned(
                    new Paragraph(config.getHeader()).setFont(config.getFont()).setBold().setFontSize(14f),
                    coordX,
                    headerY,
                    TextAlignment.CENTER
            );
            canvas.close();
        }

    }

    /**
     * Метод, который добавляет в таблицу отчета еще один пакет значений
     * @param batch пакет значений таблицы
     */
    @Override
    public void addBatch(ArrayList<ArrayList<String>> batch) {
        // Проверка, что параметр не null, иначе - исключение
        if (batch == null) {
            throw new NullPointerException();
        }
        // Далее добавляем очередной пакет к данным
        if (batch.size() > 0 && this.columnCount > 0) {
            this.addBatchToData(batch);
        }
    }

    /**
     * Метод, который окончательно формуирет отчет и отдает пользователю
     */
    @Override
    public void getReport() {
        if (this.columnCount > 0) {
            // Создаем таблицы
            ArrayList<Table> tableArr = this.createTable();
            // Создаем документ
            this.createDocument();

            // Добавляем таблицы в документ
            for (Table table : tableArr) {
                this.document.add(table);
                this.document.add(new Paragraph("").setFontSize(10));
            }
        } else {
            // Учитываем заданный размер и ориентацию
            this.calculatePageSize();
            // Создаем документ
            this.createDocument();
        }
        // Закрываем документ
        this.document.close();
    }

    /**
     * Метод для создания окончательной таблицы в pdf файле по заголовкам и данным
     * @return таблица для отчета
     */
    private ArrayList<Table> createTable() {

        // Для того, что определиться с числом колонок (или шириной листа) - найдем максимально длинное слово
        // в каждой колонке
        ArrayList<String> longestWords = this.longestWordEachColumn();

        this.tableCount = 1;
        this.colsInTable = new HashMap<>();
        this.largeCell = new ArrayList<>(this.columnCount);
        float tableWidth = this.config.getPageSize().getWidth() - 55f;

        // Учитываем заданный размер и ориентацию
        this.calculatePageSize();

        // Если не задан автоматический подбор ширины страницы
        if (!this.config.isAutoPageSize()) {
            // Найдем количество таблиц и количество колонок в них
            this.calculateNumberTablesAndColumns(longestWords);
        } else {
            // Подсчитаем необходимый размер страницы для того,
            // чтобы вся таблица вместилась без переноса
            tableWidth = this.calculateTableWidth(longestWords);
            // Устанавливаем необходимый размер страницы
            config.setPageSize(new PageSize(tableWidth + 50, this.config.getPageSize().getHeight()));
        }

        // Теперь создадим непосредственно таблицы
        ArrayList<Table> result = new ArrayList<>(tableCount);
        for (int i = 0; i < tableCount; i++) {
            result.add(new Table(colsInTable.get(i + 1))
                    .setMarginLeft(-10)
                    .setMarginRight(-10)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setWidth(tableWidth)
                    .setAutoLayout()
                    .setFont(this.config.getFont()).setFontSize(this.config.getFontSize())
            );
        }

        // Добавляем в таблицы данные
        this.addDataToTables(result);

        return result;
    }

    /**
     * Метод, который рассчитвает необходимую ширину таблицы
     */
    private float calculateTableWidth(ArrayList<String> longestWords) {
        this.colsInTable.put(this.tableCount, this.columnCount);
        float currWidth = 0f;
        // Проходим по всем самым длинным словам из каждой колонки
        for (int i = 0; i < longestWords.size(); i++) {
            // Считаем текущую ширину, которые занимали бы самые длинные слова
            // Первое + 2 - чтобы учитывались также заголовки, второе + 6 - для ширины граней
            currWidth += this.config.getFont().getWidth(longestWords.get(i), this.config.getFontSize() + 2) + 6;
        }
        return currWidth;
    }

    // Смысл метода: в библиотеке iText7 нет автоматического переноса, если таблица не вмещается по ширине,
    // поэтому невмещающиеся колонки строим в других таблицах
    /**
     * Метод, который считает по размеру страницы, по размеру шрифта
     * и по самым длинным словам в колонкам число таблиц и колонок
     * @param longestWords массив саммых длинных слов в каждой колонке
     */
    private void calculateNumberTablesAndColumns(ArrayList<String> longestWords) {
        // Находим размеры страницы
        float pageWidth = this.config.getPageSize().getWidth();
        float pageHeight = this.config.getPageSize().getHeight();
        // Находим размер шрифта
        float fontSize = this.config.getFontSize();
        // Задаем ширину для таблицы
        float tableWidth = pageWidth - 55f;
        // Учитывая размеры шрифта и страницы, определяем число таблиц и число колонок в них
        float currWidth = 0f;
        int counter = 0;
        // Проходим по всем самым длинным словам из каждой колонки
        for (int i = 0; i < longestWords.size(); i++) {
            // Счетчик для числа колонок в каждой таблице
            counter++;
            // Считаем текущую ширину, которые занимали бы самые длинные слова
            // Первое + 2 - чтобы учитывались также заголовки, второе + 6 - для ширины граней
            currWidth += this.config.getFont().getWidth(longestWords.get(i), this.config.getFontSize() + 2) + 6;
            // Если слова уже не помещаются в таблицу
            if (currWidth >= tableWidth) {
                // Указываем число колонок в текущей таблице
                boolean oneLargeCell = counter == 1;
                this.colsInTable.put(this.tableCount, oneLargeCell ? counter : --counter);
                // Надо откатиться на 1 итерацию назад, если это не была одна большая ячейка
                if (!oneLargeCell) i -= 1; else this.largeCell.add(i);
                // Добавляем еще одну таблицу
                this.tableCount++;
                // Обнуляем текущую ширину и счетчик
                currWidth = 0f;
                counter = 0;
            }
        }
        if (counter != 0) {
            this.colsInTable.put(this.tableCount, counter);
        }
    }

    /**
     * Метод, который создает добавляет в таблицы данные
     */
    private void addDataToTables(ArrayList<Table> result) {
        // Цикл по таблицам
        int shift = 0; // Переменная, которая хранит в себе сдвиг данных для таблиц после 1-ой
        for (int i = 0; i < tableCount; i++) {
            // Если таблица не первая, то прибавляем сдвиг
            if (i > 0) {
                shift += colsInTable.get(i);
            }
            // Цикл по числу колонок
            for (int j = 0; j < colsInTable.get(i + 1); j++) {
                // Если это таблица с одним большим полем, то надо выставить фиксированную ширину
                if (largeCell.contains(j + shift)) {
                    result.get(i).setFixedLayout();
                }
                result.get(i).addHeaderCell(
                        new Cell()
                                .add(new Paragraph(this.headers.get(j + shift)).setBold())
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .setFontSize(this.config.getFontSize() + 2));
            }
            // Цикл по строкам данных для таблицы
            int counter = -1;
            for (ArrayList<String> row : this.data) {
                counter++;
                for (int k = 0; k < colsInTable.get(i + 1); k++) {
                    result.get(i).addCell(row.get(k + shift));
                }
            }
        }
    }

    /**
     * Метод для поиск самого длинного слова в каждой колонке
     * @return массив самых длинных слов каждой колонки
     */
    private ArrayList<String> longestWordEachColumn() {
        ArrayList<String> result = new ArrayList<String>(this.columnCount);
        // Сначала посчитаем в массиве заголовков
        for (int i = 0; i < this.columnCount; i++) {
            result.add(this.longestWord(this.headers.get(i)));
        }
        // Затем пройдемся по самой таблице
        for (ArrayList<String> row : this.data) {
            for (int i = 0; i < this.columnCount; i++) {
                result.set(
                        i,
                        result.get(i).length() > this.longestWord(row.get(i)).length() ?
                                result.get(i)
                                : this.longestWord(row.get(i))
                );
            }
        }
        return result;
    }

    /**
     * Метод для поиска саамых длинных слов в строке
     * @param s входная строка
     * @return самое длинное слово
     */
    private String longestWord(String s) {
        return Arrays.stream(s.split(" ")).max(Comparator.comparingInt(String::length)).orElse("");
    }

    /**
     * Класс, реализующий интерфейс для обработки событий для пдф файла
     * Используется, чтобы реализовать заголовок на каждой странице и/или нумерацию страниц
     */
    private static class HeaderAndNumberingEventHandler implements IEventHandler {

        protected Document doc;
        private String header;
        private boolean isPageNumeration = false;
        private PdfConfig config;

        public HeaderAndNumberingEventHandler(Document doc, PdfConfig config) {
            this.doc = doc;
            this.config = config;
        }

        public HeaderAndNumberingEventHandler(Document doc, boolean isPageNumeration, PdfConfig config) {
            this.doc = doc;
            this.isPageNumeration = isPageNumeration;
            this.config = config;
        }

        public HeaderAndNumberingEventHandler(Document doc, String header, PdfConfig config) {
            this.doc = doc;
            this.header = header;
            this.config = config;
        }

        public HeaderAndNumberingEventHandler(Document doc, String header, boolean isPageNumeration, PdfConfig config) {
            this.doc = doc;
            this.header = header;
            this.isPageNumeration = isPageNumeration;
            this.config = config;
        }

        @Override
        public void handleEvent(Event currentEvent) {

            PdfDocumentEvent docEvent = (PdfDocumentEvent)currentEvent;
            Rectangle pageSize = docEvent.getPage().getPageSize();

            float coordX = ((pageSize.getLeft() + doc.getLeftMargin())
                    + (pageSize.getRight() - doc.getRightMargin())) / 2;
            float headerY = pageSize.getTop() - doc.getTopMargin() + 10;
            float footerY = doc.getBottomMargin() - 25;
            int pageNum = docEvent.getDocument().getPageNumber(docEvent.getPage());
            Canvas canvas = new Canvas(docEvent.getPage(), pageSize);

            if (header != null) {
                canvas.showTextAligned(
                        new Paragraph(header).setFont(config.getFont()).setBold().setFontSize(14f),
                        coordX,
                        headerY,
                        TextAlignment.CENTER
                );
            }

            if (isPageNumeration) {
                canvas.showTextAligned(
                        new Paragraph(Integer.toString(pageNum)).setFont(config.getFont()).setFontSize(10f),
                        coordX,
                        footerY,
                        TextAlignment.CENTER
                );
            }

            canvas.close();

        }

    }

}
