package com.aam.pdf.springboot.report;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public interface Report {
    void createReport(ArrayList<String> headers, ArrayList<ArrayList<String>> batch) throws FileNotFoundException;
    void addBatch(ArrayList<ArrayList<String>> batch);
    void getReport();
}
