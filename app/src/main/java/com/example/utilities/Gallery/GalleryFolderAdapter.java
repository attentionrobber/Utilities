package com.example.utilities.Gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.utilities.R;

import java.util.List;

/**
 * 이미지가 들어있는 폴더를 표현하는 어댑터
 * Used by : GalleryActivity
 */
public class GalleryFolderAdapter extends BaseAdapter {

    private List<ImageBucket> items;
    private List<Integer> countOfEachBuckets;
    private LayoutInflater inflater;

    GalleryFolderAdapter(Context context, List<ImageBucket> items, List<Integer> countOfEachBuckets) {
        this.items = items;
        this.countOfEachBuckets = countOfEachBuckets;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = inflater.inflate(R.layout.gallery_folder_item, null);
        }

        TextView text = view.findViewById(R.id.tv_img_name);
        text.setText(items.get(position).getName() + " (" + countOfEachBuckets.get(position) + ")");

        ImageView imageView = view.findViewById(R.id.imageView_folder);
        Glide.with(view).load(items.get(position).getFirstImageContainedPath()).into(imageView);

//        Bitmap image = items.get(position).getImage();
//
//        if (image != null){
//            imageView.setImageBitmap(image);
//        }
//        else {
//            // If no image is provided, display a folder icon.
//            imageView.setImageResource(R.drawable.button);
//        }

        return view;
    }

}
