package com.example.project1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterTab2 extends RecyclerView.Adapter<RecyclerViewAdapterTab2.TabTwoViewHolder> {

    private Context mContext;

    @NonNull
    @Override
    public TabTwoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TabTwoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class TabTwoViewHolder extends RecyclerView.ViewHolder {

        public TabTwoViewHolder(View itemView){
            super(itemView);
        }
    }
}
