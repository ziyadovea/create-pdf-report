package com.aam.pdf.springboot.dataproducer;

import java.util.ArrayList;

// От этого класса в дальнейшем будем получать данные для построение отчета
// Так понимаю, через этот класс будет коннект с БД и выборка информации?
public class DataProducer {

    private String query; // запрос к БД

    public DataProducer(String query) {
        this.query = query;
    }

    public ArrayList<String> getHeaders() {
        return new ArrayList<String>();
    }

    public ArrayList<ArrayList<String>> getBatch() {
        return new ArrayList<ArrayList<String>>();
    }

}
