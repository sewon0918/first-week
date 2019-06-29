package com.example.project1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

//public class ViewPagerAdapterTab2 extends PagerAdapter {
public class ViewPagerAdapterTab2 extends FragmentStatePagerAdapter {

    //private Activity activity;
   // private ArrayList gallery_images;
    private ArrayList<Bitmap> gallery_images;
   // private LayoutInflater inflater;

    /*public ViewPagerAdapterTab2(Activity activity, ArrayList images) {
        this.activity = activity;
        this.gallery_images = images;
    }*/

    public ViewPagerAdapterTab2(FragmentManager fm, ArrayList<Bitmap> images) {
        super(fm);
        this.gallery_images = images;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.getInstance(gallery_images.get(position));
    }

    @Override
    public int getCount() {
        return gallery_images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view==(RelativeLayout) object;
    }

    /*@Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_fullscreen_preview, container, false);

        ImageView fullscreen_image = view.findViewById(R.id.fullscreen_preview);

        fullscreen_image.setImageResource(gallery_images.get(position).getThumbnail());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((RelativeLayout) object); //or (view)?
    }*/
}
