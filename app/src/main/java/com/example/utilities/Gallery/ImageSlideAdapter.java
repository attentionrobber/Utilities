package com.example.utilities.Gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;

import java.util.List;

/**
 * Used by : ImageDetailViewActivity
 */
public class ImageSlideAdapter extends PagerAdapter {

    private Context context;
    private List<ImageItem> images;
    private LayoutInflater inflater;

    //----- tools
    private boolean touched = false;
    private MotionEvent event;

    ImageSlideAdapter(Context context, List<ImageItem> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int position) {
        View itemView = inflater.inflate(R.layout.item_image_sliding_adapter, viewGroup, false);
        assert itemView != null;

        //ImageView image_detailView = itemView.findViewById(R.id.image_detailView);
        final ZoomViewPagerImageView image_detailView = itemView.findViewById(R.id.image_detailView);
        final LinearLayout layout_preview = itemView.findViewById(R.id.layout_imgPreview);

        Glide.with(context).load(images.get(position).getPath()).into(image_detailView);  // placeholder()는 디폴트 이미지를 지정해줄 수 있다.

//        image_detailView.setOnClickListener(v -> {
//            if (!touched) {
//                layout_preview.setVisibility(View.VISIBLE);
//                touched = true;
//            } else {
//                layout_preview.setVisibility(View.GONE);
//                touched = false;
//            }
//        });

        viewGroup.addView(itemView, 0); // 생성한 뷰를 컨테이너에 담아준다. 컨테이너 = 뷰페이저를 생성한 최외곽 레이아웃 개념
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}

