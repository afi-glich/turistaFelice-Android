package com.marco.turistafelicetest.Wikipedia;

import com.google.gson.annotations.SerializedName;
import com.marco.turistafelicetest.Wikipedia.Query;

public class Result {
    @SerializedName("batchcomplete")
    private String result;
    @SerializedName("query")
    private Query query;

    public Result(String result, Query query) {
        this.result = result;
        this.query = query;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
