import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.hyphenation.Hyphenation;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.hyphenation.Hyphenator;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.splitting.DefaultSplitCharacters;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static com.itextpdf.kernel.pdf.PdfName.BaseFont;

/**
 * Первая работа с библиотекой
 */
public class testIText7 {

    protected String string = "this is a header";

    public static void main(String[] args) throws IOException {

        final String FONT_FILENAME = "ARIALUNI.TTF";
        final PdfFont font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);

        File file = new File("./reports/test.pdf");

        // Open PDF document in write mode
        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        // Create document to add new elements
        Document document = new Document(pdfDocument, PageSize.A4);
        /*document.setProperty(Property.SPLIT_CHARACTERS,
                new DefaultSplitCharacters() {
                    @Override
                    public boolean isSplitCharacter(GlyphLine text, int glyphPos) {
                        return true;
                    }
                });
        */

        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new TextFooterEventHandler(document, "Header"));

        // Set font to document
        document.setFont(font);

        String s = "एक गाव में एक किसान";
        String content = "Заголовок.";

        // Add Paragraph to document
        Paragraph paragraph1 = new Paragraph(s)
                .setFont(font)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph paragraph2 = new Paragraph(content)
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
        Text text = new Text("Подпись:").setFont(font).setFontSize(14);
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
        String str = "Hadoop — проект фонда Apache Software Foundation, " +
                "свободно распространяемый набор утилит, библиотек и фреймворк " +
                "для разработки и выполнения распределённых программ, работающих на " +
                "кластерах из сотен и тысяч узлов. Используется для реализации поисковых " +
                "и контекстных механизмов многих высоконагруженных веб-сайтов, в том числе, " +
                "для Yahoo! и Facebook[3]. Разработан на Java в рамках вычислительной парадигмы " +
                "MapReduce, согласно которой приложение разделяется на большое количество одинаковых " +
                "элементарных заданий, выполнимых на узлах кластера и естественным образом сводимых в конечный результат.";
        str = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        str = "verylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongwordverylongword";
        System.out.printf("\n\nstr size in pixeles: %f\n", font.getWidth(str, 10));
        System.out.printf("string length: %d\n", str.length());
        System.out.printf("page size: width X height: %f, %f\n", PageSize.A5.getWidth(), PageSize.A5.getHeight());
        System.out.printf("font size: %f\n\n", 10f);
        Table largeTable = createTable(5, str, font);
        document.add(largeTable);
        // Close document
        document.close();

    }

    private static Table createTable(int n, String content, PdfFont font) throws UnsupportedEncodingException {

        Table table = new Table(1);
        table.setMarginLeft(-10);
        table.setMarginRight(-10);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setWidth(PageSize.A4.getWidth() - 55f);
        table.setFixedLayout();
        //table.setAutoLayout();
        table.setFont(font).setFontSize(10);
        table.addHeaderCell(new Cell().add(new Paragraph("1")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("2")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("3")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("4")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("5")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        //table.setFixedLayout();
        //table.setHyphenation(new HyphenationConfig("ru", "RU", 2, 2));
        //table.setHyphenation(new HyphenationConfig("en", "en", 2, 2));

        for (int i = 0; i < n; i++) {
            if (i == 0 || i ==4 ) table.addCell("hi!");
            else
            table.addCell(new Paragraph(content)).setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.WHITE);
        }

        return table;
    }

    private static class TextFooterEventHandler implements IEventHandler {

        protected Document doc;
        private final String FONT_FILENAME = "ARIALUNI.TTF";
        private String header;

        public TextFooterEventHandler(Document doc) {
            this.doc = doc;
        }

        public TextFooterEventHandler(Document doc, String header) {
            this.doc = doc;
            this.header = header;
        }

        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            Rectangle pageSize = docEvent.getPage().getPageSize();
            PdfFont font = null;

            try {
                font =  PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            float coordX = ((pageSize.getLeft() + doc.getLeftMargin())
                    + (pageSize.getRight() - doc.getRightMargin())) / 2;
            float headerY = pageSize.getTop() - doc.getTopMargin() + 10;
            float footerY = doc.getBottomMargin();

            int pageNum = docEvent.getDocument().getPageNumber(docEvent.getPage());
            Canvas canvas = new Canvas(docEvent.getPage(), pageSize);
            canvas.setFont(font)
                    .setFontSize(10)
                    .showTextAligned(
                            new Paragraph(header)
                            .setBold()
                            .setFontSize(14f),
                            coordX, headerY, TextAlignment.CENTER
                    )
                    .showTextAligned(new Paragraph(Integer.toString(pageNum)), coordX, footerY, TextAlignment.CENTER)
                    .close();
        }
    }

}