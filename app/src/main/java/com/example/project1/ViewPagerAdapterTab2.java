//package com.example.project1;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import androidx.viewpager.widget.PagerAdapter;
//
//import java.util.ArrayList;
//
//public class ViewPagerAdapterTab2 extends PagerAdapter {
//
//    private Activity activity;
//    private ArrayList<GalleryInfo> gallery_images;
//    private LayoutInflater inflater;
//
//    public ViewPagerAdapterTab2(Activity activity, ArrayList<GalleryInfo> images) {
//        this.activity = activity;
//        this.gallery_images = images;
//    }
//
//    @Override
//    public int getCount() {
//        return gallery_images.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object){
//        return view==(RelativeLayout) object;
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        inflater = (LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.image_fullscreen_preview, container, false);
//
//        ImageView fullscreen_image = view.findViewById(R.id.fullscreen_preview);
//
//        fullscreen_image.setImageResource(gallery_images.get(position).getThumbnail());
//
//        container.addView(view);
//
//        return view;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        (container).removeView((RelativeLayout) object); //or (view)?
//    }
//}
