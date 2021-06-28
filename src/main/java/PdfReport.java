import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PdfReport implements Report {

    private final String FONT_UNICODE = "ARIALUNI.TTF";
    private PdfFont font;
    private Document document;
    private Table table;

    public PdfReport(String fileName) throws IOException {
        // Create font
        this.font = PdfFontFactory.createFont(FONT_UNICODE, PdfEncodings.IDENTITY_H);
        // Create output PDF document
        File file = new File(fileName);
        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        this.document = new Document(pdfDocument, PageSize.A4);
        this.document.setFont(font);
    }

    private String toUnicode(String s) throws UnsupportedEncodingException {
        return new String(s.getBytes("cp1251"), "utf8");
    }

    @Override
    public void createReport(String header, String[][] batch) {
        // Set base properties for table
        Table table = new Table(batch[0].length);
        table.setMarginLeft(-10);
        table.setMarginRight(-10);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setWidth(540);
        table.setAutoLayout();
        table.setFont(font).setFontSize(10);
    }

    @Override
    public void nextBatch(String[][] batch) {

    }

    @Override
    public void getReport() {
        document.add(table);
    }
}
