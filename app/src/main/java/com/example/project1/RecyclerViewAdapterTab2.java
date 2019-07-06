package com.example.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterTab2 extends RecyclerView.Adapter<RecyclerViewAdapterTab2.TabTwoViewHolder> {

    private static final String TAG = "RecyclerViewAdapterTab2";

    public Context mContext;
    public ArrayList<Bitmap> mData;
    public ArrayList<String> mName;


    public RecyclerViewAdapterTab2(Context mContext, ArrayList<Bitmap> mData, ArrayList<String> mName) {
        this.mContext = mContext;
        this.mData = mData;
        this.mName = mName;
    }

    @NonNull
    @Override
    public TabTwoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item, parent, false);
        return new TabTwoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TabTwoViewHolder holder, final int position) {
        holder.img_thumbnail.setImageBitmap(mData.get(position));

        // Set click listener
//        holder.cardView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: clicked on: " + mData.get(position));
//
//                // passing data to the fullscreen activity
//                Intent intent = new Intent(mContext, fullscreenActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("Gallery_Photos", mData);
//                //intent.putExtra("current_thumbnail", mData.get(position).getThumbnail());
//                intent.putExtras(bundle);
//                // start the activity
//                mContext.startActivity(intent);
//                Toast.makeText(mContext, position + 1 + " of " + mData.size(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class TabTwoViewHolder extends RecyclerView.ViewHolder {

        ImageView img_thumbnail;
        CardView cardView;

        public TabTwoViewHolder(View itemView){
            super(itemView);
            img_thumbnail = itemView.findViewById(R.id.gallery_image);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }
}
