package com.manager.portfolio.controllers;

import com.manager.portfolio.dto.AcceptSuggestionRequestDto;
import com.manager.portfolio.dto.BaseResponseDto;
import com.manager.portfolio.services.NotificationService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/portfolio/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/get_all/{email_id}", method = RequestMethod.GET)
    public BaseResponseDto getAllNotificationsForUser(@PathVariable(value = "email_id")String emailId) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        try {
            if(emailId != null && !emailId.isEmpty()) {
                List<Document> results = notificationService.getSuggestionsForUser(emailId);
                if(results != null) {
                    System.out.println("Notifications Fetched!");
		    System.out.println(results);
                    baseResponseDto.setData(results);
                }
            }
        } catch(Exception e) {
            baseResponseDto.setError(e);
        }
        return baseResponseDto;
    }

    @RequestMapping(value = "/accept_suggestion", method = RequestMethod.POST)
    public BaseResponseDto acceptSuggestion(@RequestBody AcceptSuggestionRequestDto acceptSuggestionRequestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        try {
            boolean result = notificationService.updatePortfolio(acceptSuggestionRequestDto.toDocument());
            if(result) {
                baseResponseDto.setData("Success");
            } else {
                baseResponseDto.setError("Error in Transaction. Please try again later.");
            }
        } catch(Exception e) {
            baseResponseDto.setError(e);
        }
        return baseResponseDto;
    }

}
