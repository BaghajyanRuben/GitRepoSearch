package com.example.gitreposearch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitRepoClient {
    private GitRepoService service;


    public GitRepoClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(GitRepoService.class);
    }

    public void search(String query, final DataLoadListenr callback){
        service.searchRepo(query).enqueue(new Callback<GitResponse>() {
            @Override
            public void onResponse(Call<GitResponse> call, Response<GitResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    callback.onDataLoaded(response.body().repos);
                }
            }

            @Override
            public void onFailure(Call<GitResponse> call, Throwable t) {

            }
        });
    }
}
