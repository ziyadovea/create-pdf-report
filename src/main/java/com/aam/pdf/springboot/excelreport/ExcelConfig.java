package com.aam.pdf.springboot.excelreport;

/**
 * Класс, который задает конфигурацию для excel файла
 */
public class ExcelConfig {

    private String outputFileName;
    private String header;
    private String fontName;
    private float fontSize;

    public ExcelConfig() {
        this.outputFileName = "report.xlsx";
        this.header = null;
        this.fontName = "Times New Roman";
        this.fontSize = 12;
    }

    public ExcelConfig(String outputFileName, String header, String fontName, float fontSize) {
        this.outputFileName = outputFileName;
        this.header = header;
        this.fontName = fontName;
        this.fontSize = fontSize;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public ExcelConfig setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public ExcelConfig setHeader(String header) {
        this.header = header;
        return this;
    }

    public String getFontName() {
        return fontName;
    }

    public ExcelConfig setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public float getFontSize() {
        return fontSize;
    }

    public ExcelConfig setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }
}
