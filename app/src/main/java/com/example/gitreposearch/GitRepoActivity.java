package com.example.gitreposearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;

public class GitRepoActivity extends AppCompatActivity
        implements RepoClickListener,
LoadMoreCallback{

    private View loadingView;
    private View bottomLoadingView;
    private EditText searchInput;
    private View buttonView;
    private TextView errorText;
    private RecyclerView recyclerView;
    private GitRepoService apiService;
    private Adapter adapter;
    private String query;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_repo);

        recyclerView = findViewById(R.id.recycler_view);

        adapter = new Adapter(this, this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));

        searchInput = findViewById(R.id.et_search);
        buttonView = findViewById(R.id.btn_search);
        loadingView = findViewById(R.id.loading_view);
        bottomLoadingView = findViewById(R.id.load_more_view);
        errorText = findViewById(R.id.error_text);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GitRepoService.class);

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchInput.getText().toString();
                if (!view.isEnabled()){
                    return;
                }

                if (!TextUtils.isEmpty(query)) {
                    search(query);
                } else {
                    Toast.makeText(GitRepoActivity.this, "input text", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void search(String query) {
        Log.d("GitRepo", "!!!!!!!!!!!! search for " + query);
        this.query = query;
        loadingView.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        adapter.clear();
        buttonView.setEnabled(false);
        apiService.searchRepo(query, 1, 100).enqueue(new Callback<GitResponse>() {
            @Override
            public void onResponse(@NotNull Call<GitResponse> call,
                                   @NotNull Response<GitResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("GitRepo", "successfilly -> total count = " + response.body().totalCount);
                    List<GitRepo> repos = response.body().repos;
                    if (repos.isEmpty()){
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText(R.string.search_no_result);
                    } else {
                        adapter.addAll(repos);

                        Log.d("GitRepo", "successfilly -> item count = " + repos.size());

                        errorText.setVisibility(View.GONE);
                    }
                }
                loadingView.setVisibility(View.GONE);
                buttonView.setEnabled(true);
            }

            @Override
            public void onFailure(Call<GitResponse> call, Throwable t) {
                loadingView.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                errorText.setText(R.string.error_text);
                buttonView.setEnabled(true);

            }
        });
    }

    @Override
    public void onRepoClicked() {

    }

    @Override
    public void loadMore() {
        bottomLoadingView.setVisibility(View.VISIBLE);
        apiService.searchRepo(query, 2, 100).enqueue(new Callback<GitResponse>() {
            @Override
            public void onResponse(@NotNull Call<GitResponse> call,
                                   @NotNull Response<GitResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("GitRepo", "successfilly -> total count = " + response.body().totalCount);
                    List<GitRepo> repos = response.body().repos;
                    if (!repos.isEmpty()){
                        adapter.addAll(repos);
                        Log.d("GitRepo", "successfilly -> item count = " + repos.size());
                    }
                }
                bottomLoadingView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GitResponse> call, Throwable t) {
                bottomLoadingView.setVisibility(View.GONE);
            }
        });
    }


}
