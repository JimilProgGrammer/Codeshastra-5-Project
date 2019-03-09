package com.manager.portfolio.repositories;

import com.manager.portfolio.constants.CollectionConstants;
import com.manager.portfolio.utils.MongoImpl;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChartRepository extends MongoImpl {

    public List<Document> fetchChartData(Document queryDoc) throws Exception {
        return super.findRecord(CollectionConstants.PORTFOLIO_DTLS, queryDoc, 0, Projections.exclude("_id"));
    }

}
