package com.example.utilities.Gallery;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;

import org.checkerframework.checker.linear.qual.Linear;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by : ImageDetailViewActivity
 */
public class ImageSlideAdapter extends PagerAdapter {

    private Context context;
    private List<ImageItem> images;
    private LayoutInflater inflater;

    private boolean touched = false;


    public ImageSlideAdapter(Context context, List<ImageItem> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_image_sliding_adapter, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);
        final LinearLayout layout_top_tools = imageLayout.findViewById(R.id.layout_top_tools);
        final LinearLayout layout_bot_tools = imageLayout.findViewById(R.id.layout_bot_tools);

        Glide.with(context).load(images.get(position).getPath()).into(imageView); // placeholder()는 디폴트 이미지를 지정해줄 수 있다.

        imageView.setOnClickListener(v -> {
            Logger.print("TOUCH_TEST","TOUCHED");
            if (!touched) {
                layout_top_tools.setVisibility(View.VISIBLE);
                layout_bot_tools.setVisibility(View.VISIBLE);
                touched = true;
            } else {
                layout_top_tools.setVisibility(View.GONE);
                layout_bot_tools.setVisibility(View.GONE);
                touched = false;
            }
        });

        view.addView(imageLayout, 0); // 생성한 뷰를 컨테이너에 담아준다. 컨테이너 = 뷰페이저를 생성한 최외곽 레이아웃 개념
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
