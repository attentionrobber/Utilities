package com.example.utilities.Gallery;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.utilities.R;

import java.util.List;

/**
 *
 */
public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private List<String> datas;

    GalleryRecyclerViewAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.imageUri = datas.get(position);
        //holder.imageView.setImageURI(holder.imageUri);
        Glide.with(context).load(viewHolder.imageUri).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        String imageUri;

        ViewHolder(View view) {
            super(view);
            imageUri = null;

            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);

            imageView = view.findViewById(R.id.imageView);
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = (metrics.widthPixels / 3);
            params.height = (metrics.widthPixels / 3);  // 가로세로 길이 동일하게 하려고 여기도 widthPixels 넣어줌

            imageView.setLayoutParams(params);

            imageView.setOnClickListener(v -> {
                // 클릭시 큰이미지로 보여주기
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra("image", imageUri);
//                context.startActivity(intent);
            });
        }
    }
}
