package com.example.utilities.Gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.utilities.R;

import java.io.Serializable;
import java.util.List;

/**
 * Used by : ImageDetailViewActivity
 */
public class ScrollViewAdapter extends RecyclerView.Adapter<ScrollViewAdapter.ViewHolder> {

    private Context context;
    private List<ImageItem> items;

    ScrollViewAdapter(Context context, List<ImageItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_scroll_view_image, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Glide.with(context).load(items.get(position).getPath()).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ViewHolder(View view) {
            super(view);

//            DisplayMetrics metrics = new DisplayMetrics();
//            WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//            windowManager.getDefaultDisplay().getMetrics(metrics);

            imageView = view.findViewById(R.id.scrollView_image);
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
//            params.width = (metrics.widthPixels / 3);
//            params.height = (metrics.widthPixels / 3);  // 가로세로 길이 동일하게 하려고 여기도 widthPixels 넣어줌

            imageView.setLayoutParams(params);

            imageView.setOnClickListener(v -> { // ImageView 클릭시 ImageDetailViewActivity 로 넘어감.
                // TODO :
            });
        }
    }
}