package com.manager.portfolio.services;

import com.manager.portfolio.repositories.ChartRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ChartService {

    @Autowired
    private ChartRepository chartRepository;

    public HashMap<String, List<Object>> getChartData(String emailId) throws Exception {
        HashMap<String, List<Object>> map = new HashMap<>();
        try {
            if(emailId != null && !emailId.isEmpty()) {
                Document queryDoc = new Document("email_id", emailId);
                List<Document> results = chartRepository.fetchChartData(queryDoc);
                if(!results.isEmpty()) {
                    List<Object> symbols = new ArrayList<>();
                    List<Object> values = new ArrayList<>();
                    for(Document doc: results) {
                        symbols.add(doc.getString("symbol"));
                        values.add(doc.getInteger("current_price") * doc.getInteger("current_qty"));
                    }
                    map.put("symbol", symbols);
                    map.put("value", values);
                }
            }
        } catch(Exception e) {
            throw new Exception(e);
        }
        return map;
    }

}
