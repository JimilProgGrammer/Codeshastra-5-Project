package com.manager.portfolio.services;

import com.manager.portfolio.repositories.UserRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean addUser(Document userDoc) throws Exception {
        try {
            if (userDoc != null) {
                boolean result = userRepository.insertUserDoc(userDoc);
                return result;
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return false;
    }

}
