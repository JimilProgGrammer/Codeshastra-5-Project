package com.manager.portfolio.repositories;

import com.manager.portfolio.constants.CollectionConstants;
import com.manager.portfolio.utils.MongoImpl;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class ProfileRepository extends MongoImpl {

    public Document getProfileSummary(Document queryDoc) throws Exception {
        if(queryDoc != null) {
            return super.findOneRecordWithProjection(CollectionConstants.USERS
                    , queryDoc
                    , Projections.include("email_id","first_name","last_name","company_name"
                            ,"max_gainer_symbol","max_gainer_amt","max_loser_symbol","max_loser_amt","type"
                            ,"total_investment","unrealised_difference","balance"));
        }
        return null;
    }

    public List<Document> fetchPortfolioSummary(Document queryDoc) throws Exception {
        if(queryDoc != null) {
	        System.out.println("Entered Repository");
            return super.findRecord(CollectionConstants.PORTFOLIO_DTLS
                    , queryDoc
                    , 0
                    , Projections.exclude("_id"));
        }
	    System.out.println("Returning empty");
        return Collections.emptyList();
    }

}
