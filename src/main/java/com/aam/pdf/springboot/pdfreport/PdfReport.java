package com.aam.pdf.springboot.pdfreport;

import com.aam.pdf.springboot.report.Report;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.Color;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PdfReport implements Report {

    private final String FONT_FILENAME = "ARIALUNI.TTF";
    private PdfFont font;
    private Document document;
    private ArrayList<Table> tableArray;
    private int columnsCount;

    public PdfReport(PdfConfig config) throws IOException {

        // Создаем шрифт
        this.font = config.getFont();

        // Создаем файл для отчета
        File file = new File("./reports" + config.getOutputFileName());
        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.addNewPage();

        // Определяем размер страницы
        PageSize pageSize;
        // Ширина меньше высоты и портреная ориентация - все верно
        if (config.getPageSize().getWidth() < config.getPageSize().getHeight()
                && config.getOrientation() == Orientation.PORTRAIT) {
            pageSize = config.getPageSize();
        // Ширина меньше высоты и альбомная ориентация - надо перевернуть
        } else if (config.getPageSize().getWidth() < config.getPageSize().getHeight()
                && config.getOrientation() == Orientation.LANDSCAPE) {
            pageSize = config.getPageSize().rotate();
        // Ширина больше высоты и альбомная ориентация - все верно
        } else if (config.getPageSize().getWidth() > config.getPageSize().getHeight()
                && config.getOrientation() == Orientation.LANDSCAPE) {
            pageSize = config.getPageSize();
        // Ширина больше высоты и портерная ориентация - надо перевернуть
        } else {
            pageSize = config.getPageSize().rotate();
        }

        this.document = new Document(pdfDocument, pageSize);
        this.document.setFont(font);

        // Проверка, есть ли у документа заголовк
        if (config.getHeader() != null) {

            // Заголовок на каждой странице и нумерация
            if (config.isHeaderOnEachPage() && config.isPageNumeration()) {
                pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,
                        new HeaderAndNumberingEventHandler(this.document, config.getHeader(), true));
            // Заголовок на первой странице и нумерация
            } else if (!config.isHeaderOnEachPage() && config.isPageNumeration()) {
                this.document.add(new Paragraph(config.getHeader())
                        .setBold()
                        .setFontSize(14f)
                        .setTextAlignment(TextAlignment.CENTER)
                );
                pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,
                        new HeaderAndNumberingEventHandler(this.document, true));
            }
            // Заголовок на каждой странице без нумерации
            else if (config.isHeaderOnEachPage() && !config.isPageNumeration()) {
                pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,
                        new HeaderAndNumberingEventHandler(this.document, config.getHeader()));
            // Заголовок на первой странице без нумерации
            } else {
                this.document.add(new Paragraph(config.getHeader())
                        .setBold()
                        .setFontSize(14f)
                        .setTextAlignment(TextAlignment.CENTER)
                );
            }
        // Проверка, есть ли у документа нумерация
        } else if (config.isPageNumeration()) {
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,
                    new HeaderAndNumberingEventHandler(this.document, true));
        }

        // Проверка, задан ли язык для корректного переноса слов
        if (config.getLang() != null) {
            this.document.setHyphenation(
                    new HyphenationConfig(config.getLang(), config.getLang().toUpperCase(), 2, 2)
            );
        }

    }

    @Override
    public void createReport(ArrayList<String> headers, ArrayList<ArrayList<String>> batch) {
        this.columnsCount = headers.size();
        if (this.columnsCount > 0) {

        }
    }

    @Override
    public void addBatch(ArrayList<ArrayList<String>> batch) {
        if (batch.size() > 0 && this.tableArray != null) {

        }
    }

    @Override
    public void getReport() {
        if (this.tableArray != null) {
            this.tableArray.forEach(table -> this.document.add(table));
        }
        this.document.close();
    }

    private static class HeaderAndNumberingEventHandler implements IEventHandler {

        protected Document doc;
        private String header;
        private boolean isPageNumeration = false;

        public HeaderAndNumberingEventHandler(Document doc) {
            this.doc = doc;
        }

        public HeaderAndNumberingEventHandler(Document doc, boolean isPageNumeration) {
            this.doc = doc;
            this.isPageNumeration = isPageNumeration;
        }

        public HeaderAndNumberingEventHandler(Document doc, String header) {
            this.doc = doc;
            this.header = header;
        }

        public HeaderAndNumberingEventHandler(Document doc, String header, boolean isPageNumeration) {
            this.doc = doc;
            this.header = header;
            this.isPageNumeration = isPageNumeration;
        }

        @Override
        public void handleEvent(Event currentEvent) {

            PdfDocumentEvent docEvent = (PdfDocumentEvent)currentEvent;
            Rectangle pageSize = docEvent.getPage().getPageSize();

            float coordX = ((pageSize.getLeft() + doc.getLeftMargin())
                    + (pageSize.getRight() - doc.getRightMargin())) / 2;
            float headerY = pageSize.getTop() - doc.getTopMargin() + 10;
            float footerY = doc.getBottomMargin();
            int pageNum = docEvent.getDocument().getPageNumber(docEvent.getPage());
            Canvas canvas = new Canvas(docEvent.getPage(), pageSize);

            if (header != null) {
                canvas.showTextAligned(
                        new Paragraph(header).setBold().setFontSize(14f),
                        coordX,
                        headerY,
                        TextAlignment.CENTER
                );
            }

            if (isPageNumeration) {
                canvas.showTextAligned(
                        new Paragraph(Integer.toString(pageNum)).setFontSize(10f),
                        coordX,
                        footerY,
                        TextAlignment.CENTER
                );
            }

            canvas.close();

        }

    }

}
