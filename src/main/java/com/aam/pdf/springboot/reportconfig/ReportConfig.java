package com.aam.pdf.springboot.reportconfig;

/**
 * Класс задает конфиг для построение отчета
 */
public class ReportConfig {

    private boolean buildPdf = true;
    private boolean buildExcel = false;
    private String dataBaseQuery;

    public ReportConfig() {
        this.buildPdf = true;
        this.buildPdf = false;
        // this.dataBaseQuery = // какой-то запрос по умолчанию (надо ли вообще?)
    }

    public ReportConfig(boolean buildPdf, boolean buildExcel, String dataBaseQuery) {
        this.buildPdf = buildPdf;
        this.buildExcel = buildExcel;
        this.dataBaseQuery = dataBaseQuery;
    }


    public boolean isBuildPdf() {
        return buildPdf;
    }

    public ReportConfig setBuildPdf(boolean buildPdf) {
        this.buildPdf = buildPdf;
        return this;
    }

    public boolean isBuildExcel() {
        return buildExcel;
    }

    public ReportConfig setBuildExcel(boolean buildExcel) {
        this.buildExcel = buildExcel;
        return this;
    }

    public String getDataBaseQuery() {
        return dataBaseQuery;
    }

    public ReportConfig setDataBaseQuery(String dataBaseQuery) {
        this.dataBaseQuery = dataBaseQuery;
        return this;
    }
}
