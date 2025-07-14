package com.marco.turistafelicetest.Wikipedia;

import com.google.gson.annotations.SerializedName;

public class Page {

        @SerializedName("pageid")
        private long id;
        @SerializedName("title")
        private String title;
        @SerializedName("extract")
        private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
