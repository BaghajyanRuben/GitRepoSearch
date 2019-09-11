package com.example.gitreposearch;

import java.util.List;

public interface DataLoadListenr {
    void onDataLoaded(List<GitRepo> repos);
}
