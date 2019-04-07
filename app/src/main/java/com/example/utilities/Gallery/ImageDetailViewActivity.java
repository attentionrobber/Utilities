package com.example.utilities.Gallery;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;

import org.checkerframework.checker.linear.qual.Linear;

import java.util.ArrayList;
import java.util.List;

/**
 * Called from : GalleryActivity 에서 호출되는 액티비티
 * 이미지 크게 보기,
 */
public class ImageDetailViewActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout layout_top_tools, layout_bot_tools;

    LinearLayout layout_scrollView;
    HorizontalScrollView scrollView;
    RecyclerView rv_horizontal;
    ScrollViewAdapter scrollViewAdapter;

    List<ImageItem> images = new ArrayList<>(); // 사진정보 데이터 저장소

    final String SD_PATH = "/storage/sdcard/DCIM/";
    final String DCIM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();

    //----- Related Slide Image ---------------------------------------
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private int position = 0; // Choose Image. 선택한 이미지

    //----- Related Top, Bottom Tools ---------------------------------
    private boolean touched = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail_view);

        Bundle extras = getIntent().getExtras();
        images = (List<ImageItem>) extras.getSerializable("images");
        position = extras.getInt("position");

        setWidget();
        //initScrollView();
        init();

    }

    private void setWidget() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ImageSlideAdapter(this, images));
        viewPager.setCurrentItem(position);
        viewPager.setOnClickListener(clickListener);

        scrollView = findViewById(R.id.scrollView);
        //layout_scrollView = findViewById(R.id.layout_scrollView);
        rv_horizontal = findViewById(R.id.rv_horizontal);
    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("TOUCHED", "ImageDetailViewActivity");
        }
    };


    private void init() {
        scrollViewAdapter = new ScrollViewAdapter(this, images);
        rv_horizontal.setAdapter(scrollViewAdapter);
        rv_horizontal.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)); // RecyclerView 를 수평 방향으로 스크롤되게 함.
    }

    private void initScrollView()
    {
        for (int i = 0; i < images.size(); i++) {
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
            layout.setGravity(Gravity.CENTER);

            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(145, 145));
            Glide.with(this).load(images.get(i).getPath()).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);
            layout_scrollView.addView(layout);
            // TODO : PagerAdapter랑 연동 Interface를 쓰는지 확인해보기
        }
    }

}
