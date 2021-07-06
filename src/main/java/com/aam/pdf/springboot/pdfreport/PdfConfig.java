package com.aam.pdf.springboot.pdfreport;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;

import java.io.IOException;

/**
 * Класс, который задает конфигурацию для pdf файла
 */
public class PdfConfig {

    private final String FONT_FILENAME = "ARIALUNI.TTF";

    private String lang;
    private String outputFileName;
    private PageSize pageSize;
    private PdfFont font;
    private Orientation orientation;
    private String header;
    private float fontSize;
    private boolean headerOnEachPage;
    private boolean pageNumeration;

    public PdfConfig() {
        // Устанавливаем значения по умолчания
        this.lang = null;
        this.outputFileName = "report.pdf";
        this.pageSize = PageSize.A4;
        this.orientation = Orientation.PORTRAIT;
        this.header = null;
        this.fontSize = 10f;
        this.headerOnEachPage = false;
        this.pageNumeration = true;
        try {
            this.font = PdfFontFactory.createFont(FONT_FILENAME, PdfEncodings.IDENTITY_H);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLang() {
        return lang;
    }

    public PdfConfig setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public PdfConfig setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
        return this;
    }

    public PageSize getPageSize() {
        return pageSize;
    }

    public PdfConfig setPageSize(PageSize pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PdfFont getFont() {
        return font;
    }

    public PdfConfig setFont(PdfFont font) {
        this.font = font;
        return this;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public PdfConfig setOrientation(Orientation orientation) {
        this.orientation = orientation;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public PdfConfig setHeader(String header) {
        this.header = header;
        return this;
    }

    public float getFontSize() {
        return fontSize;
    }

    public PdfConfig setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public boolean isHeaderOnEachPage() {
        return headerOnEachPage;
    }

    public PdfConfig setHeaderOnEachPage(boolean headerOnEachPage) {
        this.headerOnEachPage = headerOnEachPage;
        return this;
    }

    public boolean isPageNumeration() {
        return pageNumeration;
    }

    public PdfConfig setPageNumeration(boolean pageNumeration) {
        this.pageNumeration = pageNumeration;
        return this;
    }

}

