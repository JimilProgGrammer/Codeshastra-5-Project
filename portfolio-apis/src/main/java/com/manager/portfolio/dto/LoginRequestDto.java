package com.manager.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequestDto {

    @NotNull
    @JsonProperty(value = "email_id")
    private String userId;

    @NotNull
    @JsonProperty(value = "password")
    private String password;

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
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
