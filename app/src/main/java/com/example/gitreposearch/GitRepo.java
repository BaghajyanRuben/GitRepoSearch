package com.example.gitreposearch;

import com.google.gson.annotations.SerializedName;

public class GitRepo {

    String name;

    @SerializedName("full_name")
    String fullName;
}
