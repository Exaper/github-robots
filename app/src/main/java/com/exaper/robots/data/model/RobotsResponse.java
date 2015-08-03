package com.exaper.robots.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RobotsResponse {
    @SerializedName("total_count")
    public final int count;

    @SerializedName("items")
    public final List<Robot> items;

    private RobotsResponse() {
        count = 0;
        items = null;
    }
}
