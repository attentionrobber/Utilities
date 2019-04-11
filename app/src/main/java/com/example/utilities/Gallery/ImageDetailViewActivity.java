package com.example.utilities.Gallery;

import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Called from : GalleryActivity 에서 호출되는 액티비티
 * 이미지 크게 보기,
 */
public class ImageDetailViewActivity extends AppCompatActivity implements SynchronizeAdapter {

    final String TAG = "TAG_ImageDetailViewActivity";

    ViewPager viewPager; // View Large, Detail Image
    ImageSlideAdapter slideAdapter; // ViewPager's Adapter
    RecyclerView rv_horizontal; // Bottom Preview Images
    HorizontalAdapter horizontalAdapter; // RecyclerView's Adapter

    ImageButton btn_img_delete, btn_img_detail;

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

        // TODO: 갤러리 작업 중 파일이 추가됐을때 Data Refresh
        Bundle extras = getIntent().getExtras();
        images = (List<ImageItem>) extras.getSerializable("images");
        position = extras.getInt("position");

        setWidget();
        init();

        //initScrollView();
    }

    private void init() {
        slideAdapter = new ImageSlideAdapter(this, images);
        viewPager.setAdapter(slideAdapter);
        viewPager.setCurrentItem(position);

        horizontalAdapter = new HorizontalAdapter(this, images, this);
        rv_horizontal.setAdapter(horizontalAdapter);
        rv_horizontal.scrollToPosition(position);
        rv_horizontal.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)); // RecyclerView 를 수평 방향으로 스크롤되게 함.
    }

    private void setWidget() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOnClickListener(clickListener);
        viewPager.addOnPageChangeListener(pageChangeListener);

        //gridView_horizontal = findViewById(R.id.gridView_horizontal);
        rv_horizontal = findViewById(R.id.rv_horizontal);
        btn_img_delete = findViewById(R.id.btn_img_delete);
        btn_img_detail = findViewById(R.id.btn_img_detail);
        btn_img_delete.setOnClickListener(clickListener);
        btn_img_detail.setOnClickListener(clickListener);
    }

    /**
     * 갤러리 작업 중 이미지 파일이 추가됐을때 목록을 refresh, update 해줌
     */
    private void updateRefresh(int position) {
        slideAdapter = new ImageSlideAdapter(this, images);
        viewPager.setAdapter(slideAdapter);
        viewPager.setCurrentItem(position);

        horizontalAdapter = new HorizontalAdapter(this, images, this);
        rv_horizontal.setAdapter(horizontalAdapter);
        rv_horizontal.scrollToPosition(position);
    }

    View.OnClickListener clickListener = v -> {
        AlertDialog.Builder builder;
        switch (v.getId()) {
            case R.id.btn_img_delete:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Image");
                builder.setMessage("Are you sure DELETE this image?");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    // todo 삭제
                    File file = new File(images.get(viewPager.getCurrentItem()).getPath());
                    boolean delete = file.delete();
                    images.remove(viewPager.getCurrentItem());
                    updateRefresh(viewPager.getCurrentItem());
                });
                builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                builder.show();

                break;
            case R.id.btn_img_detail:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Image");
                builder.setMessage("Are you sure DELETE this image?");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    // todo 상세정보

                });
                builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                builder.show();
                break;
            default: break;
        }
    };
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            // TODO : rv_horizontal selected item 중앙에 오도록 변경
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    horizontalAdapter.setCurrentPosition(position); // viewPager 가 선택한 아이템과 recyclerView 가 선택한 아이템이 같도록 설정
                    //horizontalAdapter.notifyItemChanged(position);
                    horizontalAdapter.notifyDataSetChanged();
                    rv_horizontal.scrollToPosition(position);
                }
            }, 200);

            Log.i("POSITION", ""+position);


        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    /**
     * Interface for Synchronizing ViewPager and Bottom Preview
     */
    @Override
    public void setPagerAdapter(int position) {
        viewPager.setCurrentItem(position);
    }
    @Override
    public void setRecyclerView(int position) {

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
