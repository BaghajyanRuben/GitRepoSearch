package com.example.gitreposearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<GitRepo> items;
    private WeakReference<RepoClickListener> repoClickListenerWeakReference;
    private WeakReference<LoadMoreCallback> loadMoreCallbackWeakReference;

    public Adapter(RepoClickListener clickListener, LoadMoreCallback loadMoreCallback) {
        items = new ArrayList<>();
        repoClickListenerWeakReference = new WeakReference<>(clickListener);
        loadMoreCallbackWeakReference = new WeakReference<>(loadMoreCallback);
    }

    public void addAll(List<GitRepo> data){
        int startPosition = items.isEmpty() ? 0 : items.size() -1;
        items.addAll(data);
        notifyItemRangeChanged(startPosition, data.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item_layout, parent, false),
                repoClickListenerWeakReference);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
        LoadMoreCallback loadMoreCallback = loadMoreCallbackWeakReference.get();
        if (position == items.size() - 1 && loadMoreCallback != null){
            loadMoreCallback.loadMore();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, fullName;

        ViewHolder(@NonNull View itemView, final WeakReference<RepoClickListener> weakReferenceClick) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            fullName = itemView.findViewById(R.id.full_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RepoClickListener clickListener = weakReferenceClick.get();
                    if (clickListener != null){
                        clickListener.onRepoClicked();
                    }
                }
            });
        }


        void bind(GitRepo repo){
            name.setText(repo.name);
            fullName.setText(repo.fullName);
        }

    }
}
