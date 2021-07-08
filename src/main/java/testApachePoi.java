import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Первая работа с библиотекой
 */
public class testApachePoi {

    public static final String FILE = "./reports/test.xls";

    @SuppressWarnings("deprecation")
    public static void writeIntoExcel(String file) throws FileNotFoundException, IOException {

        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Birthdays");

        String header = "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789";
        Row row0 = sheet.createRow(3);
        Cell cell = row0.createCell(3);

        if(header.length() > 50){ // Length of String for my test
            sheet.setColumnWidth(3, 15000); // Set column width, you'll probably want to tweak the second int
            CellStyle style = book.createCellStyle(); // Create new style

            Font font = book.createFont();
            font.setFontHeightInPoints((short)24);
            font.setFontName("Courier New");
            font.setItalic(true);

            style.setFont(font);

            style.setWrapText(true); // Set wordwrap
            cell.setCellStyle(style); // Apply style to cell
            cell.setCellValue(header); // Write header
        }

        // Нумерация начинается с нуля
        Row row = sheet.createRow(0);

        // Мы запишем имя и дату в два столбца
        // имя будет String, а дата рождения --- Date,
        // формата dd.mm.yyyy
        Cell name = row.createCell(0);
        name.setCellValue("Джон\nJonh");

        CellStyle cs = book.createCellStyle();
        cs.setWrapText(true);
        name.setCellStyle(cs);

        Font font = book.createFont();
        font.setFontHeightInPoints((short)24);
        font.setFontName("Courier New");
        font.setItalic(true);
        font.setStrikeout(true);

        cs.setFont(font);

        Cell birthdate = row.createCell(1);

        DataFormat format = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
        birthdate.setCellStyle(dateStyle);

        // Нумерация лет начинается с 1900-го
        birthdate.setCellValue(new Date(110, 10, 10));

        // Меняем размер столбца
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        // Записываем всё в файл
        book.write(new FileOutputStream(file));
        book.close();

    }

    public static void main(String[] args) throws IOException {
        writeIntoExcel(FILE);
    }

}
