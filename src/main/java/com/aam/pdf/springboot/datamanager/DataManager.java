package com.aam.pdf.springboot.datamanager;

import com.aam.pdf.springboot.dataproducer.DataProducer;
import com.aam.pdf.springboot.excelreport.ExcelConfig;
import com.aam.pdf.springboot.excelreport.ExcelReport;
import com.aam.pdf.springboot.pdfreport.PdfConfig;
import com.aam.pdf.springboot.pdfreport.PdfReport;
import com.aam.pdf.springboot.report.Report;
import com.aam.pdf.springboot.reportconfig.ReportConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, который будет отвечать за создание отчетов и выдачу их клиентам
 */
@Service
public class DataManager {

    private Map<Integer, Report> idAndReports = new HashMap<>(); // соответствия id и отчета
    private Map<Report, String> reportAndPath = new HashMap<>(); // хранит соотвествия отчета и места его расположения
    private int id; // уникальный id отчета

    @Async
    public int buildReport(ReportConfig reportConfig) {
        id++;
        DataProducer dataProducer = new DataProducer(reportConfig.getDataBaseQuery());
        // есть заголовки => есть содержимое отчета
        if (dataProducer.getHeaders() != null) {
            if (reportConfig.isBuildPdf()) {
                // здесь я создаю новый пдф конфиг, но мне кажется было бы логичном
                // в глобальном конфиге reportConfig иметь поле конфиг для определенного формата
                // типа reportConfig.getPdfConfig() или reportConfig.getExcelConfig()
                PdfConfig pdfConfig = new PdfConfig();
                PdfReport report = new PdfReport(pdfConfig);
                report.createReport(dataProducer.getHeaders(), dataProducer.getBatch());
                while (dataProducer.getBatch() != null) {
                    report.addBatch(dataProducer.getBatch());
                }
                report.getReport();
                idAndReports.put(id, report);
                reportAndPath.put(report, "./" + pdfConfig.getOutputDirectory() + "/" + pdfConfig.getOutputFileName());
            } else if (reportConfig.isBuildExcel()) {
                // Тут еще можно код отрефакторить, тк в двух ифах очень похожий код. Загнать все в функцию
                ExcelConfig excelConfig = new ExcelConfig();
                ExcelReport report = new ExcelReport(excelConfig);
                report.createReport(dataProducer.getHeaders(), dataProducer.getBatch());
                while (dataProducer.getBatch() != null) {
                    report.addBatch(dataProducer.getBatch());
                }
                report.getReport();
                idAndReports.put(id, report);
                reportAndPath.put(report, "./" + excelConfig.getOutputDirectory() + "/" + excelConfig.getOutputFileName());
            }
        // нет заголовков => пустой отчет
        // здесь тоже рефакторинг не помешал бы
        } else {
            if (reportConfig.isBuildPdf()) {
                PdfConfig pdfConfig = new PdfConfig();
                PdfReport report = new PdfReport(pdfConfig);
                report.createReport(new ArrayList<>(), new ArrayList<>());
                report.getReport();
                idAndReports.put(id, report);
                reportAndPath.put(report, "./" + pdfConfig.getOutputDirectory() + "/" + pdfConfig.getOutputFileName());
            } else if (reportConfig.isBuildExcel()) {
                ExcelConfig excelConfig = new ExcelConfig();
                ExcelReport report = new ExcelReport(excelConfig);
                report.createReport(new ArrayList<>(), new ArrayList<>());
                report.getReport();
                idAndReports.put(id, report);
                reportAndPath.put(report, "./" + excelConfig.getOutputDirectory() + "/" + excelConfig.getOutputFileName());
            }
        }
        return id;
    }

    @Async
    public Report getReport(int id) {
    // здесь я думаю логику надо сменить: в RestController, который будет оперировать данным классом,
    // отдается отчет клиенту, а потом удаляется
        if (idAndReports.containsKey(id)) {
            var result = idAndReports.get(id);
            File file = new File(reportAndPath.get(result));
            file.delete();
            reportAndPath.remove(idAndReports.remove(id));
            idAndReports.remove(id);
            return result;
        } else {
            throw new IllegalArgumentException("Отчета с таким идентификатором нет!");
        }
    }

}
