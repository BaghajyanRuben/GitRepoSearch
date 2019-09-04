package com.example.gitreposearch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GitResponse {
    @SerializedName("total_count")
    int totalCount = 0;

    @SerializedName("items")
    List<GitRepo> repos;
}
