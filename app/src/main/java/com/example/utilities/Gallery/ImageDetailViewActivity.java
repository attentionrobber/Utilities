package com.example.utilities.Gallery;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Called from : GalleryActivity 에서 호출되는 액티비티
 * 이미지 크게 보기,
 */
public class ImageDetailViewActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout layout_top_tools, layout_bot_tools;

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
    }

    private void setWidget() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ImageSlideAdapter(this, images));
        viewPager.setCurrentItem(position);

        //layout_top_tools = findViewById(R.id.layout_top_tools);
        //layout_bot_tools = findViewById(R.id.layout_bot_tools);
    }
}
