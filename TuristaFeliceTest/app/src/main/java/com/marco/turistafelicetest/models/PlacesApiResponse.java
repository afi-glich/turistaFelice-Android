package com.marco.turistafelicetest.models;

import java.util.List;

public class PlacesApiResponse {
    private String status;
    private String next_page_token;
    private List<Place> results;

    public PlacesApiResponse(String status, String next_page_token, List<Place> results) {
        this.status = status;
        this.next_page_token = next_page_token;
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public String getNext_page_token() {
        return next_page_token;
    }

    public List<Place> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "PlacesApiResponse{" +
                "status='" + status + '\'' +
                ", next_page_token='" + next_page_token + '\'' +
                ", results=" + results +
                '}';
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public void setResults(List<Place> results) {
        this.results = results;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
