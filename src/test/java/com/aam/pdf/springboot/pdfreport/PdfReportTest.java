package com.aam.pdf.springboot.pdfreport;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PdfReportTest {

    @Test
    void test1() throws IOException {
        PdfReport report = new PdfReport("./reports/test1.pdf");
        report.createReport(new ArrayList<String>(), new ArrayList<String>());
        report.getReport();
    }

}