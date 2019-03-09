package com.manager.portfolio.controllers;

import com.manager.portfolio.dto.BaseResponseDto;
import com.manager.portfolio.services.ProfileService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(value = "/portfolio/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @RequestMapping(value = "/get_user_details/{email_id}", method = RequestMethod.GET)
    public BaseResponseDto getProfileSummary(@PathVariable(value = "email_id") String emailId) {
	System.out.println("Controller hit");
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        try {
            if(emailId != null && !emailId.isEmpty()) {
                Document result = profileService.getProfileSummary(emailId);
                if(result != null) {
                    baseResponseDto.setData(result);
		    System.out.println(baseResponseDto.getData());
                } else {
                    baseResponseDto.setError("User Does Not Exist!");
                }
            }
        } catch(Exception e) {
            baseResponseDto.setError(e);
        }
        return baseResponseDto;
    }

    @RequestMapping(value = "/get_share_summary/{email_id}", method = RequestMethod.GET)
    public BaseResponseDto getPortfolioSummary(@PathVariable(value = "email_id")String emailId) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        try {
            if(emailId != null && !emailId.isEmpty()) {
		        System.out.println("Email ID = " + emailId);
                List<Document> results = profileService.getPortfolioSummary(emailId);
                if(results != null) {
                    System.out.println("Results Fetched");
                    System.out.println(results);
                    baseResponseDto.setData(results);
                }
            }
        } catch(Exception e) {
            baseResponseDto.setError(e);
        }
        return baseResponseDto;
    }

}
