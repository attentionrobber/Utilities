package com.example.utilities.Gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.utilities.R;

import java.util.List;

/**
 * 이미지 크게 보기에 사용되는 한장씩 표현하는 PagerAdapter
 * Used by : ImageDetailViewActivity
 */
public class ImageSlideAdapter extends PagerAdapter {

    private Context context;
    private List<ImageItem> images;
    private SynchronizeAdapter syncAdapter;
    private LayoutInflater inflater;

    ImageSlideAdapter(Context context, List<ImageItem> images, SynchronizeAdapter syncAdapter) {
        this.context = context;
        this.images = images;
        this.syncAdapter = syncAdapter;
        inflater = LayoutInflater.from(context); // As follows: inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        final ZoomableImageView image_detailView = itemView.findViewById(R.id.image_detailView); // ImageView image_detailView = itemView.findViewById(R.id.image_detailView);
        image_detailView.syncAdapter = syncAdapter; // TODO: Is this Real? Does it Work WELL? SO SO SO GOOD!!
                                                    // Interface 만들고 Activity 에서 implements 한 interface 를
                                                    // Activity 에서 this 로 넘겨주고
                                                    // Adapter 에서 생성자로 넘겨받고
                                                    // Adapter 에서 CustomImageView 에 있는 interface 를 초기화하고
                                                    // CustomImageView 에서 실행시킴.

        Glide.with(context).load(images.get(position).getPath()).into(image_detailView);  // placeholder()는 디폴트 이미지를 지정해줄 수 있다.

        viewGroup.addView(itemView, 0); // 생성한 뷰를 컨테이너에 담아준다. 컨테이너 = 뷰페이저를 생성한 최외곽 레이아웃 개념
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

}

