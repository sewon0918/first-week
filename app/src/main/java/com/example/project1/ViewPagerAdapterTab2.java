package com.example.project1;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ViewPagerAdapterTab2 extends PagerAdapter {

    private Activity activity;
    private ArrayList<Gallery_Photo> gallery_images;
    private LayoutInflater inflater;

    public ViewPagerAdapterTab2(Activity activity, ArrayList<Gallery_Photo> images) {
        this.activity = activity;
        this.gallery_images = images;
    }

    @Override
    public int getCount() {
        return gallery_images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_fullscreen_preview, container, false);

        ImageView fullscreen_image = view.findViewById(R.id.fullscreen_image_preview);

        Gallery_Photo image = gallery_images.get(position);
        fullscreen_image.setImageResource(image.getThumbnail());

        /*DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);*/

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);
    }
}
