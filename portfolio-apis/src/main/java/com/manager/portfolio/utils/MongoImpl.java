package com.manager.portfolio.utils;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MongoImpl {

    private MongoClient mongo;
    private MongoDatabase db;

    /**
     * On-demand initialization of Mongo client
     */
    private void lazyInitMongoDB() {
        //initialize Mongo connection
        this.mongo = new MongoClient();
        this.db = mongo.getDatabase("portfolio_manager");
    }

    /**
     * Inserts documents from a list of documents into MongoDB if it does not exist
     * Checks are done on the basis of two keys that are passed
     * Each document in insertDoc must have both the keys that are passed
     *
     * @param collectionName - Name of the collection in which data is to be inserted
     * @param key1 - the key on which searching is applied
     * @param key2 - another key on which searching is applied
     * @param insertDoc - the list of documents to insert
     * @return
     * @throws Exception
     */
    public boolean insertIfNotExist(String collectionName, String key1, String key2, List<Document> insertDoc) throws Exception {
        boolean flag = false;
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            for(Document doc: insertDoc) {
                if(key1 != null && key2 != null && !key1.isEmpty() && !key2.isEmpty() &&
                        collection.count(Filters.and(Filters.eq(key1, doc.get(key1)), Filters.eq(key2, doc.get(key2)))) == 0L) {
                    //System.out.println("Inserting --> " + doc);
                    collection.insertOne(doc);
                    flag = true;
                }
            }
        } catch(Exception e) {
            throw new Exception(e);
        }
        return flag;
    }

    /**
     * Inserts documents from a list of documents into MongoDB if it does not exist
     * Checks are done on the basis of two keys that are passed
     * Each document in insertDoc must have both the keys that are passed
     *
     * @param collectionName
     * @param key
     * @param insertDoc
     * @return
     * @throws Exception
     */
    public boolean insertIfNotExist(String collectionName, String key, List<Document> insertDoc) throws Exception {
        boolean flag = false;
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            for(Document doc: insertDoc) {
                if(key != null && !key.isEmpty() &&
                        collection.count(Filters.eq(key, doc.get(key))) == 0L) {
                    //System.out.println("Inserting --> " + doc);
                    collection.insertOne(doc);
                    flag = true;
                }
            }
        } catch(Exception e) {
            throw new Exception(e);
        }
        return flag;
    }

    /**
     * Inserts single document to DB if it does not already exist
     *
     * @param collectionName
     * @param key1
     * @param doc
     * @return
     * @throws Exception
     */
    public boolean insertSingleDocumentIfNotExist(String collectionName, String key1, Document doc) throws Exception {
        boolean flag = false;
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            if(key1 != null && !key1.isEmpty() &&
                    collection.count(Filters.eq(key1, doc.get(key1))) == 0L) {
                //System.out.println("Inserting --> " + doc);
                collection.insertOne(doc);
                flag = true;
            }
        } catch(Exception e) {
            throw new Exception(e);
        }
        return flag;
    }

    /**
     * Returns all documents from a collection
     *
     * @param collectionName - Name of the collection from which data is to be fetched
     * @return
     * @throws Exception
     */
    public List<Document> getDataByCollectionName(String collectionName) throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> users = this.db.getCollection(collectionName);
            return (List<Document>)users.find().into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Query a collection and return the data as specified in the projection.
     * Projection in mongoDB means fetching only the data that is mentioned. For
     * example a document has 10 fields and you want 2 fields in the result use
     * a projection in this case.
     *
     * @param collectionName
     * @param projection
     * @return
     * @throws Exception
     */
    public List<Document> findRecordsWithProjection(String collectionName, Bson projection) throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            MongoCursor<Document> dataCursor = collection.find()
                    .projection(projection).iterator();
            List<Document> data = new ArrayList<Document>();
            while (dataCursor.hasNext()) {
                data.add(dataCursor.next());
            }
            return data;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Returns 'limit' number of documents from DB with projection
     *
     * @param collectionName
     * @param projection
     * @param counter
     * @param limit
     * @return
     * @throws Exception
     */
    public List<Document> findRecordsWithProjectionAndLimit(String collectionName,
                                                            Bson projection, int counter, int limit)
            throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }

        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            FindIterable<Document> find = collection.find();
            if (projection != null) {
                find.projection(projection);
            }
            if (counter > 0) {
                find.skip(counter);
            }
            if (limit > 0) {
                find.limit(limit);
            }
            return find.into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Finds record with specified query and limits number of documents fetched
     *
     * @param collectionName
     * @param limit
     * @return
     * @throws Exception
     */
    public List<Document> findRecord(String collectionName,
                                     int limit)
            throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            FindIterable<Document> find = collection.find();
            if (limit > 0) {
                find.limit(limit);
            }
            return find.into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Finds record with specified query and limits number of documents fetched
     *
     * @param collectionName
     * @param queryDoc
     * @param limit
     * @return
     * @throws Exception
     */
    public List<Document> findRecord(String collectionName,
                                     Bson queryDoc, int limit)
            throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            FindIterable<Document> find = collection.find(queryDoc);
            if (limit > 0) {
                find.limit(limit);
            }
            return find.into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Finds records by query and projections and limits the number of documents fetched
     *
     * @param collectionName
     * @param queryDoc
     * @param limit
     * @param projection
     * @return
     * @throws Exception
     */
    public List<Document> findRecord(String collectionName,
                                     Bson queryDoc, int limit, Bson projection)
            throws Exception{
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try{
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            FindIterable<Document> find = collection.find(queryDoc).projection(projection);
            if (limit > 0) {
                find.limit(limit);
            }
            return find.into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Finds sorted results according to query and projection and limits the number of documents fetched
     *
     * @param collectionName
     * @param queryDoc
     * @param sortDoc
     * @param projection
     * @param limit
     * @return
     * @throws Exception
     */
    public List<Document> findRecord(String collectionName,
                                     Bson queryDoc, Bson sortDoc, Bson projection, int limit)
            throws Exception{
        if(this.mongo ==  null) {
            lazyInitMongoDB();
        }
        try{
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            FindIterable<Document> find = collection.find(queryDoc)
                    .projection(projection).sort(sortDoc);
            if (limit > 0) {
                find.limit(limit);
            }
            return find.into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Finds sorted results according to query and and limits the number of documents fetched
     *
     * @param collectionName
     * @param queryDoc
     * @param sortDoc
     * @param limit
     * @return
     * @throws Exception
     */
    public List<Document> findRecordSortLimit(String collectionName,
                                              Bson queryDoc, Bson sortDoc, int limit)
            throws Exception{
        if(this.mongo ==  null) {
            lazyInitMongoDB();
        }
        try{
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            FindIterable<Document> find = collection.find(queryDoc).sort(sortDoc);
            if (limit > 0) {
                find.limit(limit);
            }
            //find.sort(sortDoc);
            return find.into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    public List<Document> recordSortLimit(String collectionName, Bson sortDoc, int limit) throws Exception{
        if(this.mongo ==  null) {
            lazyInitMongoDB();
        }
        try{
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            FindIterable<Document> find = collection.find().sort(sortDoc);
            if (limit > 0) {
                find.limit(limit);
            }
            //find.sort(sortDoc);
            return find.into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }




    /**
     * Find records with paginated results from the database.
     *
     * @param collectionName
     * @param queryDoc
     * @param counter
     * @param limit
     * @return
     * @throws Exception
     */
    public List<Document> findRecord(String collectionName,
                                     Bson queryDoc, int counter, int limit)
            throws Exception {
        if(this.mongo  == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            FindIterable<Document> find = null;
            if (queryDoc == null)
                find = collection.find();
            else
                find = collection.find(queryDoc);
            if (counter > 0) {
                find.skip(counter);
            }
            if (limit > 0) {
                find.limit(limit);
            }
            return find.into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Query the collection and return the first record found for the matching
     * criteria.
     *
     * @param collectionName
     * @param queryDoc
     * @return
     * @throws Exception
     */
    public Document findOneRecord(String collectionName, Bson queryDoc) throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            return collection.find(queryDoc).first();
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Finds single record from the database for the particular query and the
     * default projection which excludes the auto generated _id field from the
     * record.
     *
     * @param collectionName
     * @param queryDoc
     * @return
     * @throws Exception
     */
    public Document findOneRecordWithProjection(String collectionName,
                                                Bson queryDoc)
            throws Exception {
        if(this.mongo == null){
            lazyInitMongoDB();
        }
        try{
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            Bson projection = Projections.exclude("_id");
            return collection.find(queryDoc).projection(projection).first();
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Returns records from Collection excluding the _id field
     *
     * @param collectionName
     * @return
     * @throws Exception
     */
    public List<Document> findRecordsWithProjection(String collectionName) throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db
                    .getCollection(collectionName);
            Bson projection = Projections.exclude("_id");
            MongoCursor<Document> cursor = collection.find().projection(projection)
                    .iterator();
            List<Document> records = new ArrayList<Document>();
            while (cursor.hasNext()) {
                records.add(cursor.next());
            }
            return records;
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     *
     * @param collectionName
     * @param queryDoc
     * @param projection
     * @return
     * @throws Exception
     */
    public Document findOneRecordWithProjection(String collectionName,
                                                Bson queryDoc, Bson projection)
            throws Exception{
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db
                    .getCollection(collectionName);
            return collection.find(queryDoc).projection(projection).first();
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Generates sorted records based on the key provided in the argument. Uses
     * the mongo aggregation framework.
     *
     * @param collectionName
     * @param key
     * @return
     * @throws Exception
     */
    public List<Document> findAscendingSortedResults(String collectionName, String key, Bson projection)
            throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try{
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            return collection.find(Filters.exists(key)).sort(Sorts.ascending(key))
                    .projection(projection).into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Generates sorted records based on the key provided in the argument. Uses
     * the mongo aggregation framework.
     *
     * @param collectionName
     * @param key
     * @return
     * @throws Exception
     */
    public List<Document> findDescendingSortedResults(
            String collectionName, String key, Bson projection) throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            return collection.find(Filters.exists(key)).sort(Sorts.descending(key))
                    .projection(projection).into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Generates sorted records based on provided key and also uses skip()
     * limit() function
     *
     * @param collectionName
     * @param key
     * @param count
     * @param limit
     * @return
     * @throws Exception
     */
    public List<Document> findDescendingSortedResultsWithCountLimitAndProjection
    (String collectionName, String key, int count, int limit) throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            return collection.find(Filters.exists(key)).sort(Sorts.descending(key))
                    .skip(count).limit(limit).projection(Projections.exclude("_id")).into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Finds sorted results from a collection based on the key , passed in the
     * argument.
     *
     * @param collectionName
     * @param key
     * @return
     * @throws Exception
     */
    public List<Document> findSortedResults(String collectionName,
                                            String key)
            throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionName);
            return collection.find(Filters.exists(key)).sort(Sorts.ascending(key))
                    .into(new ArrayList<Document>());
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Gets the number of records from the collection by running the count
     * function on the collection.
     *
     * @param collectionsName
     * @return
     * @throws Exception
     */
    public long getRecordCount(String collectionsName) throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionsName);
            return collection.count();
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Gets the number of records matching the queryDoc from the collection
     * by running the count function of collection and passing the queryDoc.
     *
     * @param collectionsName
     * @param queryDoc
     * @return
     * @throws Exception
     */
    public long getRecordCount(String collectionsName, Bson queryDoc) throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionsName);
            return collection.count(queryDoc);
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Uses group by for two fields
     * @param collectionsName
     * @return
     * @throws Exception
     */
    public AggregateIterable<Document> getGroupbyMin(String collectionsName) throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        try {
            MongoCollection<Document> collection = this.db.getCollection(collectionsName);
            AggregateIterable<Document> iterable = collection.aggregate(Arrays.asList(
                    new Document("$group",
                            new Document("_id", new Document("year", "$year"))
                                    .append("low", new Document("$min", "$low")))));
            return iterable;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Deletes a record from the collection.
     *
     * @param collectionName
     * @param queryDoc
     */
    public void deleteRecord(String collectionName, Bson queryDoc) {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        MongoCollection<Document> collection = this.db
                .getCollection(collectionName);
        collection.deleteOne(queryDoc);
    }

    /**
     * @param collectionsName
     * @param queryDoc
     * @param updateDoc
     * @return
     */
    public Document findOneAndUpdate(String collectionsName , Bson queryDoc
            ,Document updateDoc) {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        MongoCollection<Document> collection = this.db
                .getCollection(collectionsName);
        return collection.findOneAndUpdate(queryDoc, updateDoc);
    }

    /**
     * Upsert Multiple records in a particular collection.
     *
     * @param queryDoc
     * @param updatedRecord
     * @param collectionName
     * @throws Exception
     */
    public void updateRecords(Bson queryDoc, Document updatedRecord,
                              String collectionName) throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        MongoCollection<Document> collection = this.db
                .getCollection(collectionName);
        UpdateResult result = collection.updateMany(queryDoc, new Document(
                "$set", updatedRecord));
        if (result.getMatchedCount() == 0) {
            collection.insertOne(updatedRecord);
        }
    }

    public MongoIterable<String> getCollectionList() throws Exception {
        if(this.mongo == null) {
            lazyInitMongoDB();
        }
        return this.db.listCollectionNames();
    }

}
