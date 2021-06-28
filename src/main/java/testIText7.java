import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class testIText7 {

    public static void main(String[] args) throws IOException {

        final String FONT_FILENAME = "ARIALUNI.TTF";
        final PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);

        File file = new File("./reports/test.pdf");

        // Open PDF document in write mode
        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        // Create document to add new elements
        Document document = new Document(pdfDocument);

        // Set font to document
        document.setFont(font);

        String s = "एक गाव में एक किसान";
        String content = "Заголовок.";
        String out = new String(s.getBytes("cp1251"), "UTF-8");

        // Add Paragraph to document
        Paragraph paragraph1 = new Paragraph(out)
                .setFont(font)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph paragraph2 = new Paragraph("\u0417\u0430\u0433\u043e\u043b\u043e\u0432\u043e\u043a.")
                .setFont(font)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph paragraph3 = new Paragraph("Prepared by experienced English teachers, the texts, articles and conversations " +
                "are brief and appropriate to your level of proficiency. Take the multiple-choice " +
                "quiz following each text, and you'll get the results immediately. You will feel " +
                "both challenged and accomplished! You can even download (as PDF) and print the " +
                "texts and exercises. It's enjoyable, fun and free. Good luck!")
                .setFont(font)
                .setFontSize(14);

        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);

        // Add Text to document
        Text text = new Text(new String("Подпись:".getBytes("cp1251"), "UTF-8")).setFont(font).setFontSize(14);
        document.add(new Paragraph(text).setTextAlignment(TextAlignment.RIGHT));

        // Add new page to document
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        // Add image to document
        ImageData imgData = ImageDataFactory.create("./images/cat.jpg");
        Image img = new Image(imgData);
        document.add(img);

        // Add new page to document
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        // Add list to document
        List list = new List();
        list.add("Java");
        list.add("C++");
        list.add("C#");

        document.add(list);

        // Add new page to document
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        // Add table to document
        ArrayList<String> tableHeaders = new ArrayList<String>();
        tableHeaders.add("First header.");
        tableHeaders.add("Second header.");
        tableHeaders.add("Third header.");
        tableHeaders.add("");

        ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
        values.add(new ArrayList(Arrays.asList("a", "b", "c", "")));
        values.add(new ArrayList(Arrays.asList("d", "e", "f", "")));
        values.add(new ArrayList(Arrays.asList("g", "h", "i", "")));

        Table table = new Table(tableHeaders.size());
        table.setMarginLeft(-10);
        table.setMarginRight(-10);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setWidth(540);
        table.setAutoLayout();
        table.setFont(font).setFontSize(10); // or 12?
        for (String item : tableHeaders) {
            table.addCell(new Cell().add(new Paragraph(item)).setBackgroundColor(ColorConstants.LIGHT_GRAY)).setTextAlignment(TextAlignment.CENTER);
        }
        for (ArrayList<String> row : values) {
            for (String item : row) {
                table.addCell(new Cell().add(new Paragraph(item))).setTextAlignment(TextAlignment.CENTER);
            }
        }

        document.add(table);

        // Test large table

        // Add new page to document
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        Table largeTable = createTable(100_000, "Опа!", font);
        document.add(largeTable);

        // Close document
        document.close();

    }

    private static Table createTable(int n, String content, PdfFont font) throws UnsupportedEncodingException {

        Table table = new Table(5);
        table.setMarginLeft(-10);
        table.setMarginRight(-10);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setWidth(540);
        table.setAutoLayout();
        table.setFont(font).setFontSize(10);

        for (int i = 0; i < n; i++) {
            table.addCell(
                    new Cell().add(
                            new Paragraph(
                                    new String(content.getBytes("cp1251"), "UTF-8")
                            )
                    )
            ).setTextAlignment(TextAlignment.CENTER);
        }

        return table;
    }

}