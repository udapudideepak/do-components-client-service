package com.test.components.exception;

import com.test.components.constants.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Objects;

//Common Exception class for components service
public class ComponentException extends Exception{

    HttpStatus httpStatusCode;
    ErrorCode errorCode;
    String userMessage;
    String detailedMessage;

    public ComponentException(HttpStatus httpStatusCode, ErrorCode errorCode, String userMessage, String detailedMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.userMessage = userMessage;
        this.detailedMessage = detailedMessage;
    }

    public ComponentException(){

    }


    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(HttpStatus httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentException that = (ComponentException) o;
        return httpStatusCode == that.httpStatusCode &&
                errorCode == that.errorCode &&
                Objects.equals(userMessage, that.userMessage) &&
                Objects.equals(detailedMessage, that.detailedMessage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(httpStatusCode, errorCode, userMessage, detailedMessage);
    }
}
