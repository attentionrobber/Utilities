package com.example.utilities.Gallery;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.utilities.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by : ImageDetailViewActivity
 */
public class ImageSlideAdapter extends PagerAdapter {

    private Context context;
    private List<ImageItem> images;
    private LayoutInflater inflater;


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

        Glide.with(context).load(images.get(position).getPath()).into(imageView); // placeholder()는 디폴트 이미지를 지정해줄 수 있다.

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
