package com.manager.portfolio.repositories;

import com.manager.portfolio.constants.CollectionConstants;
import com.manager.portfolio.utils.MongoImpl;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepository extends MongoImpl {

    public List<Document> fetchNSuggestions(Document queryDoc) throws Exception {
        return super.findRecord(CollectionConstants.NOTIFICATION, queryDoc, 0, Projections.exclude("_id"));
    }

    public Document findDocuments(String collectionName, Document queryDoc) throws Exception {
        return super.findOneRecordWithProjection(collectionName, queryDoc, Projections.exclude("_id"));
    }

    public void updateCollection(String collectionName, Document queryDoc, Document updateDoc) throws Exception {
        super.updateRecords(queryDoc, updateDoc, collectionName);
    }

    public Document fetchUserDoc(Document queryDoc) throws Exception {
        return super.findOneRecordWithProjection(CollectionConstants.USERS
                , queryDoc
                , Projections.include("balance"));
    }

}
