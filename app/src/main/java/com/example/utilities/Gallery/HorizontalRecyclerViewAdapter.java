package com.example.utilities.Gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.utilities.R;

import java.util.List;

/**
 * 이미지 크게 보기에서 하단부에 있는 Preview
 * 수평으로 스크롤되는 RecyclerView 로 적용되는 RecyclerViewAdapter
 * Used by : ImageDetailViewActivity
 */
public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<ImageItem> items;
    private SynchronizeAdapter syncAdapter;

    HorizontalRecyclerViewAdapter(Context context, List<ImageItem> items, SynchronizeAdapter syncAdapter) {
        this.context = context;
        this.items = items;
        this.syncAdapter = syncAdapter;
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

            imageView.setOnClickListener(v -> { // ImageView Item 클릭시 큰이미지(PagerAdapter)도 변경
                // String path = items.get(position).getPath();
                //int position = getAdapterPosition();
                syncAdapter.setPagerAdapter(getAdapterPosition());
            });
        }
    }
}