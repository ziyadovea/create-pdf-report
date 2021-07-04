package com.aam.pdf.springboot.report;

import java.util.ArrayList;
import java.util.List;

public interface Report {
    void createReport(ArrayList<String> headers, ArrayList<ArrayList<String>> batch);
    void addBatch(ArrayList<ArrayList<String>> batch);
    void getReport();
}
