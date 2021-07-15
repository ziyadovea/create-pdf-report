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

    @Getter @Setter private boolean buildPdf = true;
    @Getter @Setter private boolean buildExcel = false;
    @Getter @Setter private String dataBaseQuery;

}
