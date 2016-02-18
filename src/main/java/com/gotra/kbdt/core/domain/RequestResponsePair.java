package com.gotra.kbdt.core.domain;

/**
 * @author gotra
 */

public class RequestResponsePair {
    private String request;
    private String response;

    public RequestResponsePair(String request, String response) {
        this.request = request;
        this.response = response;
    }

    public RequestResponsePair() {
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
