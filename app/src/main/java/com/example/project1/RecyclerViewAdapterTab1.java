package com.example.project1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterTab1 extends RecyclerView.Adapter<RecyclerViewAdapterTab1.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapterTab1";
   // private String tel;

    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> Numbers = new ArrayList<>();
    private ArrayList<Bitmap> Photos = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapterTab1(ArrayList<String> Names, ArrayList<String> Numbers, ArrayList<Bitmap> Photos, Context mContext) {
        this.Names = Names;
        this.Numbers = Numbers;
        this.Photos = Photos;
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
        //Log.d(TAG, "onBindViewHolder: called.");  // this is simply for helping with debugging if needed

        holder.name.setText(Names.get(position));
        holder.number.setText(Numbers.get(position));
        holder.photo.setImageBitmap(Photos.get(position));

        final String tel = Numbers.get(position);
        // trying to open new page if you click a contact
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " +  tel);
                //String tel = "tel" + holder.name.getText();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + tel));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Names.size();
    }

//    @Override
//    public int getItemViewType(int position){
//        int viewType = 1;
//        return viewType;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView number;
        CircleImageView photo;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name1);
            number = itemView.findViewById(R.id.number1);
            photo = itemView.findViewById(R.id.photo1);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

