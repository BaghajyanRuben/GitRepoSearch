package com.example.gitreposearch;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

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
    private GitRepoViewModel viewModel;

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

        viewModel = ViewModelProviders.of(this).get(GitRepoViewModel.class);

        viewModel.getRepsLiveData()
                .observe(this,  new Observer<List<GitRepo>>() {
            @Override
            public void onChanged(List<GitRepo> gitRepos) {
                Log.d("GitRepo", " onChanged " + (adapter.getItemCount()));
                buttonView.setEnabled(true);

                if (gitRepos.isEmpty()) {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.search_no_result);
                } else {
                    errorText.setVisibility(View.GONE);
                }
                loadingView.setVisibility(View.GONE);
                adapter.addAll(gitRepos);
            }
        });

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

        Log.d("GitRepo", "OnCreate " + adapter.getItemCount());

        if(adapter.getItemCount() == 0) {
            search("a");
        }

    }

    private void search(String query) {
        Log.d("GitRepo", "!!!!!!!!!!!! search for " + query);
        this.query = query;
        loadingView.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        adapter.clear();
        buttonView.setEnabled(false);
        viewModel.loadData(query);

    }

    @Override
    public void onRepoClicked() {

    }

    @Override
    public void loadMore() {
        bottomLoadingView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        Log.d("GitRepo", "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("GitRepo", "onRestart");

    }
}
