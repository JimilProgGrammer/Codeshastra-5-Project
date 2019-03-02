package com.manager.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponseDto {

    @JsonProperty(value = "data")
    private Object data;
    @JsonProperty(value = "error")
    private Object error;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
