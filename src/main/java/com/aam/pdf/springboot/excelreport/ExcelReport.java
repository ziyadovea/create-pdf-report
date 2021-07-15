package com.aam.pdf.springboot.excelreport;

import com.aam.pdf.springboot.report.Report;
import com.microsoft.schemas.office.visio.x2012.main.CellType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс для построения отчета в формате .xlsx
 */
public class ExcelReport implements Report {

    private ArrayList<String> headers;
    private ArrayList<ArrayList<String>> data;
    private int columnCount;
    private ExcelConfig config;

    public ExcelReport(ExcelConfig config) {
        this.config = config;
    }

    /**
     * Метод для начала построения отчета
     * @param headers массив заголовков
     * @param batch первая пачка данных
     */
    @Override
    public void createReport(ArrayList<String> headers, ArrayList<ArrayList<String>> batch) {
        // Проверка параметров на null
        if (headers == null || batch == null) {
            throw new NullPointerException();
        }
        // Заносим заголовки и первый пакет данных в соответствующие переменные класса
        this.headers = new ArrayList<String>(headers);
        this.columnCount = this.headers.size();
        this.data = new ArrayList<ArrayList<String>>();
        if (this.columnCount > 0 && batch.size() > 0) {
            this.addBatchToData(batch);
        }
    }

    /**
     * Метод добавляет очередной пакет данных к общей матрице данных
     * @param batch очередной пакет значений
     */
    private void addBatchToData(ArrayList<ArrayList<String>> batch) {
        // Цикл по всем строкам пакета
        for (ArrayList<String> row : batch) {
            ArrayList<String> dataRow = new ArrayList<>(this.columnCount);
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
     * Метод добавляет пачку данных ко всем данным
     * @param batch очередной пакет значений
     */
    @Override
    public void addBatch(ArrayList<ArrayList<String>> batch) {
        // Проверка параметра на null
        if (batch == null) {
            throw new NullPointerException();
        }
        // Добавляем очередной пакет данных
        if (batch.size() > 0 && this.columnCount > 0) {
            this.addBatchToData(batch);
        }
    }

    /**
     * Добавляет данные в эксель
     * @param wb эксель-книга
     */
    private void createTable(Workbook wb) {
        // Создаем лист
        Sheet sheet = wb.createSheet(config.getHeader() == null ? "Лист1" : config.getHeader());
        // Добавляем заголовок в 1 ячейку, если он есть
        if (config.getHeader() != null) {
            Row row0 = sheet.createRow(0);
            Cell header = row0.createCell(0);
            header.setCellValue(config.getHeader());
            CellStyle cs = this.createHeaderStyleCell(wb, true);
            header.setCellStyle(cs);
        }
        // Затем добавляем саму таблицу
        this.addTable(sheet, wb);
        // Добавим автоматические расширение ячеeк таблицы под данные
        sheet.autoSizeColumn(0);
        for (int i = 1; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Добавляет таблицу в эксель
     * @param sheet лист эксель-книги
     */
    private void addTable(Sheet sheet, Workbook wb) {
        // Стили для ячеек
        CellStyle headerCellStyle = this.createHeaderStyleCell(wb, false);
        CellStyle defaultCellStyle = this.createDefaultStyleCell(wb, false);
        CellStyle defaultBottomCellStyle = this.createDefaultStyleCell(wb, true);
        // Строка, с которой начинается заноситься таблица
        int startRow = config.getHeader() == null ? 0 : 1;
        // Добавим заголовки
        Row row = sheet.createRow(startRow);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(headerCellStyle);
            cell.setCellValue(headers.get(i));
        }
        // Добавим данные таблицы
        startRow++;
        int counter = -1;
        for (ArrayList<String> dataRow : data) {
            counter++;
            row = sheet.createRow(startRow);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(i < dataRow.size() ? dataRow.get(i) : "");
                if (counter < data.size() - 1) {
                    cell.setCellStyle(defaultCellStyle);
                } else {
                    cell.setCellStyle(defaultBottomCellStyle);
                }
            }
            startRow++;
        }
    }

    /**
     * Задает стиль для обычной ячейки
     * @param wb эксель-книга
     * @return
     */
    private CellStyle createDefaultStyleCell(Workbook wb, boolean isBottom) {
        // Стиль ячейки
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setBorderLeft(BorderStyle.THICK);
        cellStyle.setBorderRight(BorderStyle.THICK);
        if (isBottom) {
            cellStyle.setBorderBottom(BorderStyle.THICK);
        }
        // Шрифт
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) config.getFontSize());
        font.setFontName(config.getFontName());
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * Задает стиль для ячейки с заголовком
     * @param wb эксель-книга
     * @return
     */
    private CellStyle createHeaderStyleCell(Workbook wb, boolean isMainHeader) {
        // Стиль ячейки
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if (!isMainHeader) {
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setBorderBottom(BorderStyle.THICK);
            cellStyle.setBorderTop(BorderStyle.THICK);
            cellStyle.setBorderLeft(BorderStyle.THICK);
            cellStyle.setBorderRight(BorderStyle.THICK);
        } else {
            cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            cellStyle.setBorderRight(BorderStyle.THIN);
        };
        // Шрифт
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) (config.getFontSize() + 2f));
        font.setFontName(config.getFontName());
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * Метод отдает построенный отчет
     */
    @Override
    public void getReport() {
        // Создаем эксель-книгу
        Workbook wb = new XSSFWorkbook();
        this.createTable(wb);
        try (OutputStream fileOut = new FileOutputStream("./" + config.getOutputDirectory() +
                "/" + config.getOutputFileName())) {
            // Записывам все в файл
            wb.write(fileOut);
            wb.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

