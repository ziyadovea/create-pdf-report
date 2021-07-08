package com.aam.pdf.springboot.excelreport;

import com.aam.pdf.springboot.report.Report;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Класс для построения отчета в формате .xls или .xlsx
 */
public class ExcelReport implements Report {

    public ExcelReport() {

    }

    @Override
    public void createReport(ArrayList<String> headers, ArrayList<ArrayList<String>> batch) throws FileNotFoundException {

    }

    @Override
    public void addBatch(ArrayList<ArrayList<String>> batch) {

    }

    @Override
    public void getReport() {

    }

}
