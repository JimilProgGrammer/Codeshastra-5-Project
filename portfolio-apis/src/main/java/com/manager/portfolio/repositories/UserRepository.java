package com.manager.portfolio.repositories;

import com.manager.portfolio.constants.CollectionConstants;
import com.manager.portfolio.utils.MongoImpl;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository extends MongoImpl {

    public boolean insertUserDoc(Document insertDoc) throws Exception {
        List<Document> insertList = new ArrayList<>();
        insertList.add(insertDoc);
        return super.insertIfNotExist(CollectionConstants.USERS, "email_id", insertList);
    }

}
