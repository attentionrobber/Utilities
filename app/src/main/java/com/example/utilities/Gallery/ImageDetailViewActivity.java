package com.example.utilities.Gallery;

import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;

import com.example.utilities.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Called from : GalleryActivity 에서 호출되는 액티비티
 * 이미지 크게 보기,
 */
public class ImageDetailViewActivity extends AppCompatActivity implements SynchronizeAdapter {

    ViewPager viewPager;
    ImageSlideAdapter slideAdapter;
    RecyclerView rv_horizontal;
    //GridView gridView_horizontal;
    HorizontalAdapter horizontalAdapter;

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
        init();

        //initScrollView();
    }

    private void setWidget() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOnClickListener(clickListener);
        viewPager.addOnPageChangeListener(pageChangeListener);

        //gridView_horizontal = findViewById(R.id.gridView_horizontal);
        rv_horizontal = findViewById(R.id.rv_horizontal);
    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("TOUCHED", "ImageDetailViewActivity");
        }
    };
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            // TODO : rv_horizontal selected item 변경
            //rv_horizontal.scrollToPosition(position);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rv_horizontal.scrollToPosition(position);
                }
            }, 200);

            Log.i("POSITION", ""+position);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void init() {
        slideAdapter = new ImageSlideAdapter(this, images);
        viewPager.setAdapter(slideAdapter);
        viewPager.setCurrentItem(position);

        // TODO : BaseAdapter 로 변경해보기
        horizontalAdapter = new HorizontalAdapter(this, images, this);
        rv_horizontal.setAdapter(horizontalAdapter);
        rv_horizontal.scrollToPosition(position);
        //Log.i("POSITION", ""+position);
        rv_horizontal.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)); // RecyclerView 를 수평 방향으로 스크롤되게 함.
//        horizontalAdapter = new HorizontalAdapter(this, images, this);
//        gridView_horizontal.setAdapter(horizontalAdapter);
    }

    @Override
    public void setPagerAdapter(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void setRecyclerView() {

    }


//    private void initScrollView() {
//        for (int i = 0; i < images.size(); i++) {
//            LinearLayout layout = new LinearLayout(getApplicationContext());
//            layout.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
//            layout.setGravity(Gravity.CENTER);
//
//            ImageView imageView = new ImageView(this);
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(145, 145));
//            Glide.with(this).load(images.get(i).getPath()).into(imageView);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            layout.addView(imageView);
//            layout_scrollView.addView(layout);
//        }
//    }

}
