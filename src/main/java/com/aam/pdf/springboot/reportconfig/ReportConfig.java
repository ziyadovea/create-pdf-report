package com.aam.pdf.springboot.reportconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * Класс задает конфиг для построение отчета
 */
@AllArgsConstructor
@NoArgsConstructor
public class ReportConfig {

    private @Getter @Setter boolean buildPdf = true;
    private @Getter @Setter boolean buildExcel = false;
    private @Getter @Setter String dataBaseQuery;

}
