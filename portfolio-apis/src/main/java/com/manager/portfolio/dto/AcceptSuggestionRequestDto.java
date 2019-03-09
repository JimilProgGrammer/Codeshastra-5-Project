package com.manager.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AcceptSuggestionRequestDto {

    @NotNull
    @JsonProperty(value = "email_id")
    private String emailId;

    @NotNull
    @JsonProperty(value = "symbol")
    private String symbol;

    @NotNull
    @JsonProperty(value = "qty")
    private int quantity;

    @NotNull
    @JsonProperty(value = "current_price")
    private double currentPrice;

    @NotNull
    @JsonProperty(value = "amt_payable")
    private double amt;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    /**
     * Converts the dto to mongo document type.
     *
     * @return
     */
    public Document toDocument() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(this);
            return Document.parse(json);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
