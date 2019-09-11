package com.example.gitreposearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class GitRepoViewModel extends ViewModel {
     private MutableLiveData<List<GitRepo>> repsLiveData = new MutableLiveData<>();

     private GitRepoClient gitRepoClient;

    public GitRepoViewModel() {
        gitRepoClient = new GitRepoClient();
    }

    public LiveData<List<GitRepo>> getRepsLiveData() {
        return repsLiveData;
    }

    public void loadData(String query){

        gitRepoClient.search(query, new DataLoadListenr() {
            @Override
            public void onDataLoaded(List<GitRepo> repos) {
                repsLiveData.setValue(repos);
            }
        });

   }
}
