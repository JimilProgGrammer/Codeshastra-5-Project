package com.manager.portfolio.services;

import com.manager.portfolio.repositories.ProfileRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Document getProfileSummary(String emailId) throws Exception {
        try {
            if(emailId != null && !emailId.isEmpty()) {
                Document queryDoc = new Document("email_id", emailId);
                return profileRepository.getProfileSummary(queryDoc);
            }
        } catch(Exception e) {
            throw new Exception(e);
        }
        return null;
    }

    public List<Document> getPortfolioSummary(String emailId) throws Exception {
        try {
            if(emailId != null && !emailId.isEmpty()) {
		        System.out.println("Entered Service");
                Document queryDoc = new Document("email_id", emailId);
                List<Document> results = profileRepository.fetchPortfolioSummary(queryDoc);
		        System.out.println("Service: " + results);
		        return results;
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return null;
    }

}
