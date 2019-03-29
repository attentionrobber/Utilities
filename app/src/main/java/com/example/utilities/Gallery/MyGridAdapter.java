package com.example.utilities.Gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utilities.R;

import java.util.List;

public class MyGridAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<GridViewItem> items;


    public MyGridAdapter(Context context, List<GridViewItem> items) {
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
            convertView = inflater.inflate(R.layout.grid_item, null);
        }

        TextView text = convertView.findViewById(R.id.tv_img_name);
        text.setText(items.get(position).getPath());

        ImageView imageView = convertView.findViewById(R.id.imageView_gallery);
        Bitmap image = items.get(position).getImage();

        if (image != null){
            imageView.setImageBitmap(image);
        }
        else {
            // If no image is provided, display a folder icon.
            imageView.setImageResource(R.drawable.button);
        }

        return convertView;
    }

}
