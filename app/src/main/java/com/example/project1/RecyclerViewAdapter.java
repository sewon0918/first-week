package com.example.project1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> Numbers = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> Names, ArrayList<String> Numbers, Context mContext) {
        this.Names = Names;
        this.Numbers = Numbers;
        this.mContext = mContext;
    }

    // video didn't have @NonNull
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // this is where the images and their corresponding names are loaded from layout_listitem.xml
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");  // this is simply for helping with debugging if needed

//        Glide.with(mContext)
//                .asBitmap()
//                .load(Numbers.get(position))                // this is the image URL
//                .into(holder.image);

        holder.number.setText(Names.get(position));

        // trying to open new page if you click a contact
        //holder.parentLayout.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Log.d(TAG, "onClick: clicked on: " +  mImageNames.get(position));

        //    }
        //});
    }

    @Override
    public int getItemCount() {
        return Names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView number;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
