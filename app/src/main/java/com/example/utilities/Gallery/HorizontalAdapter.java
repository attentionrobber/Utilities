package com.example.utilities.Gallery;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.utilities.R;

import java.util.List;
import java.util.function.LongFunction;

/**
 * 이미지 크게 보기에서 하단부에 있는 Preview
 * 수평으로 스크롤되는 RecyclerView 로 적용되는 RecyclerViewAdapter
 * Used by : ImageDetailViewActivity
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Context context;
    private List<ImageItem> items;
    private SynchronizeAdapter syncAdapter;
    private int selectedPosition = 0;

    HorizontalAdapter(Context context, List<ImageItem> items, SynchronizeAdapter syncAdapter) {
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
        if (selectedPosition == position)
            viewHolder.layout.setBackgroundResource(R.drawable.button); // Highlight selected Item
        else
            viewHolder.layout.setBackgroundResource(0);

        viewHolder.imageView.setOnClickListener(v -> { // ImageView Item 클릭시 큰이미지(PagerAdapter)도 변경
            // String path = items.get(getAdapterPosition()).getPath();
            syncAdapter.setPagerAdapter(position);
            selectedPosition = position;
            notifyDataSetChanged();
        });

        Glide.with(context).load(items.get(position).getPath()).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        RelativeLayout layout;

        ViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.image_scrollView);
            layout = view.findViewById(R.id.layout_scrollView);

            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            imageView.setLayoutParams(params);

//            imageView.setOnClickListener(v -> { // ImageView Item 클릭시 큰이미지(PagerAdapter)도 변경
//                // String path = items.get(getAdapterPosition()).getPath();
//                syncAdapter.setPagerAdapter(getAdapterPosition());
//                selectedPosition = getAdapterPosition();
//                notifyDataSetChanged();
//            });
        }
    }
}

//public class HorizontalAdapter extends BaseAdapter {
//
//    private List<ImageItem> items;
//    private SynchronizeAdapter syncAdapter;
//    private LayoutInflater inflater;
//
//    HorizontalAdapter(Context context, List<ImageItem> items, SynchronizeAdapter syncAdapter) {
//        this.items = items;
//        this.syncAdapter = syncAdapter;
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public int getCount() {
//        return items.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return items.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        if (view == null)
//            view = inflater.inflate(R.layout.item_scroll_view_image, null);
//
//        ImageView imageView = view.findViewById(R.id.image_scrollView);
//        Glide.with(view).load(items.get(position).getPath()).into(imageView);
//
//        return view;
//    }
//}