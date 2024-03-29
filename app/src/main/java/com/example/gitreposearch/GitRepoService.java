package com.example.gitreposearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitRepoService {

    @GET("/search/repositories")
    Call<GitResponse> searchRepo(@Query("q") String query);

    @GET("/search/repositories")
    Call<GitResponse> searchRepo(@Query("q") String query,
                                 @Query("page") int page,
                                 @Query("page_limit") int limit);
}
