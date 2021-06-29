package com.aam.pdf.springboot.pdfreport;

import com.aam.pdf.springboot.report.Report;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
        pdfDocument.addNewPage();
        this.document = new Document(pdfDocument, PageSize.A4);
        this.document.setFont(font);
    }

    private String toUnicode(String s) throws UnsupportedEncodingException {
        return new String(s.getBytes("cp1251"), "utf8");
    }

    @Override
    public void createReport(List<String> header, List<String> batch) {
        if (header.size() > 0) {
            // Set base properties for table
            this.table = new Table(Math.min(5, header.size()));
            this.table.setMarginLeft(-10)
                      .setMarginRight(-10)
                      .setHorizontalAlignment(HorizontalAlignment.CENTER)
                      .setWidth(540)
                      .setAutoLayout()
                      .setFont(font)
                      .setFontSize(10);
        }
    }

    @Override
    public void nextBatch(List<String> batch) {
        if (batch.size() > 0 && this.table != null) {

        }
    }

    @Override
    public void getReport() {
        if (this.table != null) {
            this.document.add(this.table);
        }
        this.document.close();
    }


}
