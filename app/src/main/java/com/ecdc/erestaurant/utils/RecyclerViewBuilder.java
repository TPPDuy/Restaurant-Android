package com.ecdc.erestaurant.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public final class RecyclerViewBuilder {

    private RecyclerView recyclerView;
    private static RecyclerViewBuilder instance;

    private RecyclerViewBuilder() {

    }

    @NonNull
    public static RecyclerViewBuilder with() {
        if (instance == null)
            synchronized (RecyclerViewBuilder.class) {
                if (instance == null)
                    instance = new RecyclerViewBuilder();
            }
        return instance;
    }

    public RecyclerViewBuilder setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.recyclerView.setLayoutManager(layoutManager);
        return this;
    }

    public RecyclerViewBuilder into(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public RecyclerViewBuilder initializeWithDefaultLinearLayout(int orientation) {
        return initializeWithDefaults().setLinearLayout(orientation, false);
    }

    public RecyclerViewBuilder initializeWithDefaultGridLayout(int spanCount) {
        return initializeWithDefaults().setLayoutManager(
                new GridLayoutManager(this.recyclerView.getContext(), spanCount));
    }

    public RecyclerViewBuilder setLinearLayout(int orientation, boolean reverseLayout) {
        this.recyclerView.setLayoutManager(
                new LinearLayoutManager(this.recyclerView.getContext(), orientation, reverseLayout));
        return this;
    }

    public RecyclerViewBuilder addItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.addItemDecoration(decor);
        return this;
    }

    public void setAdapter(@NonNull RecyclerView.Adapter adapter) {
        this.recyclerView.setAdapter(adapter);
    }

    public RecyclerViewBuilder initializeWithDefaults() {
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setNestedScrollingEnabled(false);
        return this;
    }
}
