package com.example.widget_management.Model;

import org.springframework.http.HttpStatus;

public class CustomResponse {
    private HttpStatus status;
    private String message;
    private Boolean success;
    public CustomResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
        this.setStatusBasedOnSuccess(success);
    }
    private void setStatusBasedOnSuccess(Boolean success) {
        if (success) {
            this.status = HttpStatus.OK;
        } else {
            this.status = HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
