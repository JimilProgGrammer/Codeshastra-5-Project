package com.manager.portfolio.controllers;

import com.manager.portfolio.dto.BaseResponseDto;
import com.manager.portfolio.services.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/portfolio/api/chart")
public class ChartController {

    @Autowired
    private ChartService chartService;

    @RequestMapping(value = "/get_chart_data/{email_id}", method = RequestMethod.GET)
    public BaseResponseDto getChartData(@PathVariable(value = "email_id")String emailId) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        try {
            HashMap<String, List<Object>> returnMap = chartService.getChartData(emailId);
            if(returnMap != null) {
                baseResponseDto.setData(returnMap);
            }
        } catch (Exception e) {
            baseResponseDto.setError(e);
        }
        return baseResponseDto;
    }

}
