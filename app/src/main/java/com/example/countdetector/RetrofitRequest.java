package com.example.countdetector;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetrofitRequest {
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("count")
    @Expose
    private int count;


    public RetrofitRequest(String key, int count) {
        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
