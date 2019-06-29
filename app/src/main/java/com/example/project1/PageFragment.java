package com.example.project1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;

public class PageFragment extends Fragment {

    //private int imageResource;
    private Bitmap bitmap;
    private Bitmap gallery_bitmap;

    public static PageFragment getInstance(Bitmap resourceBitmap) {
        PageFragment f = new PageFragment();
        Bundle args = new Bundle();

        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        args.putByteArray("image_bitmap", byteArray);*/

        args.putParcelable("image_bitmap", resourceBitmap);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*byte[] byteArray = getArguments().getByteArray("image_bitmap");
        gallery_bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);*/

        gallery_bitmap = getArguments().getParcelable("image_bitmap");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.fullscreen_preview);

        /*BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 4;
        o.inDither = false;
        bitmap = BitmapFactory.decodeResource(getResources(), imageResource, o);*/
        imageView.setImageBitmap(gallery_bitmap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gallery_bitmap.recycle();
        gallery_bitmap = null;
    }
}
