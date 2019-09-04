package com.example.gitreposearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitRepoActivity extends AppCompatActivity
        implements RepoClickListener {

    private View loadingView;
    private EditText searchInput;
    private View buttonView;
    private RecyclerView recyclerView;
    private GitRepoService apiService;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_repo);

        recyclerView = findViewById(R.id.recycler_view);

        adapter = new Adapter(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        searchInput = findViewById(R.id.et_search);
        buttonView = findViewById(R.id.btn_search);
        loadingView = findViewById(R.id.loading_view);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GitRepoService.class);

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchInput.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    search(query);
                } else {
                    Toast.makeText(GitRepoActivity.this, "input text", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void search(String query) {
        loadingView.setVisibility(View.VISIBLE);
        apiService.searchRepo(query).enqueue(new Callback<GitResponse>() {
            @Override
            public void onResponse(@NotNull Call<GitResponse> call,
                                   @NotNull Response<GitResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("GitRepo", "successfilly -> item count = " + response.body().totalCount);
                    adapter.addAll(response.body().repos);
                }
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GitResponse> call, Throwable t) {
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRepoClicked() {

    }
}
