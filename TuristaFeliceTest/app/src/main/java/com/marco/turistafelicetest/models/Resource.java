package com.marco.turistafelicetest.models;

/**
 * Generic class that can be used to contain the data and the status of the request.
 *
 * @param <T> The type of data associated with the request.
 */
public class Resource<T> {

    private T data;
    private int statusCode;
    private boolean isLoading;
    private String next_page_token;

    public Resource() {}

    public Resource(T data, String next_page_token, int statusCode, boolean isLoading) {
        this.data = data;
        this.next_page_token = next_page_token;
        this.statusCode = statusCode;
        this.isLoading = isLoading;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getNextPageToken() {
        return next_page_token;
    }

    public void setNextPageToken(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "data=" + data +
                ", statusCode=" + statusCode +
                ", isLoading=" + isLoading +
                '}';
    }
}