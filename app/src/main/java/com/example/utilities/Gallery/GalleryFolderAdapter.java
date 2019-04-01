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

public class GalleryFolderAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<ImageBucket> items;


    public GalleryFolderAdapter(Context context, List<ImageBucket> items) {
        this.items = items;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gallery_folder_item, null);
        }

        TextView text = convertView.findViewById(R.id.tv_img_name);
        text.setText(items.get(position).getName());

        ImageView imageView = convertView.findViewById(R.id.imageView_gallery);
        Glide.with(convertView).load(items.get(position).getFirstImageContainedPath()).into(imageView);
//        Bitmap image = items.get(position).getImage();
//
//        if (image != null){
//            imageView.setImageBitmap(image);
//        }
//        else {
//            // If no image is provided, display a folder icon.
//            imageView.setImageResource(R.drawable.button);
//        }

        return convertView;
    }

}
