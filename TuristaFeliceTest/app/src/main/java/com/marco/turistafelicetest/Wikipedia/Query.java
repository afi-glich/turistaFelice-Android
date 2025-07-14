package com.marco.turistafelicetest.Wikipedia;

import com.google.gson.annotations.SerializedName;
import com.marco.turistafelicetest.Wikipedia.Page;

import java.util.Map;

public class Query {
    @SerializedName("pages")
    private Map<String, Page> pages;

    public Map<String, Page> getPages() {
        return pages;
    }

    public void setPages(Map<String, Page> pages) {
        this.pages = pages;
    }
}
