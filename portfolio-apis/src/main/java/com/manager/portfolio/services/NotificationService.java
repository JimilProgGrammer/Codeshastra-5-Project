package com.manager.portfolio.services;

import com.manager.portfolio.constants.CollectionConstants;
import com.manager.portfolio.repositories.NotificationRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Document> getSuggestionsForUser(String emailId) throws Exception {
        try {
            if(emailId != null && !emailId.isEmpty()) {
                Document queryDoc = new Document("email_id", emailId);
                return notificationRepository.fetchNSuggestions(queryDoc);
            }
        } catch(Exception e) {
            throw new Exception(e);
        }
        return null;
    }

    public boolean updatePortfolio(Document detailsDoc) throws Exception {
        try {
            // Step 1. Update current quantity and current price in portfolio_dtls
            Document queryDoc = new Document("email_id", detailsDoc.getString("email_id"))
                    .append("symbol", detailsDoc.getString("symbol"));
            Document portfolioDoc = notificationRepository.findDocuments(CollectionConstants.PORTFOLIO_DTLS, queryDoc);
            if(portfolioDoc != null) {
                portfolioDoc.replace("current_price", detailsDoc.getDouble("current_price"));
                portfolioDoc.replace("current_qty", portfolioDoc.getInteger("current_qty")-detailsDoc.getInteger("qty"));
                notificationRepository.updateCollection(CollectionConstants.PORTFOLIO_DTLS, queryDoc, portfolioDoc);
            }
            // Step 2. Make a tradebook order entry
            Document insertDoc = new Document("date", new Date().toString())
                    .append("email_id", detailsDoc.getString("email_id"))
                    .append("symbol", detailsDoc.getString("symbol"))
                    .append("qty", detailsDoc.getInteger("qty"))
                    .append("price", detailsDoc.getDouble("amt_payable"))
                    .append("type", "buy");
            notificationRepository.updateCollection(CollectionConstants.TRADEBOOK_DTLS, queryDoc, insertDoc);
            // Step 3. Update user's balance in user collection
            Document userDoc = notificationRepository.fetchUserDoc(new Document()
                    .append("email_id",detailsDoc.getString("email_id")));
            userDoc.replace("balance", userDoc.getDouble("balance") - detailsDoc.getDouble("amt_payable"));
            notificationRepository.updateCollection(CollectionConstants.USERS, new Document()
                    .append("email_id",detailsDoc.getString("email_id")), userDoc);
            return true;
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

}
