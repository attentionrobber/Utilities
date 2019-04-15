package com.example.utilities.Gallery;

import android.annotation.SuppressLint;
import android.graphics.Point;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;

import org.checkerframework.checker.signature.qual.FieldDescriptor;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Called from : GalleryActivity 에서 호출되는 액티비티
 * 이미지 크게 보기,
 */
public class ImageDetailViewActivity extends AppCompatActivity implements SynchronizeAdapter {

    final String TAG = "TAG_ImageDetailView";

    //ViewPager viewPager; // View Large, Detail Image
    CustomViewPager viewPager; // View Large, Detail Image
    ImageSlideAdapter slideAdapter; // ViewPager's Adapter
    RecyclerView rv_horizontal; // Bottom Preview Images
    HorizontalAdapter horizontalAdapter; // RecyclerView's Adapter
    LinearLayoutManager horizontalLayoutManger;


    List<ImageItem> images = new ArrayList<>(); // 사진정보 데이터 저장소

    //final String SD_PATH = "/storage/sdcard/DCIM/";
    //private final String DCIM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();

    //----- Related Slide Image ---------------------------------------
    private int position = 0; // Choose Image. 선택된 이미지

    //----- Related Top, Bottom Tools ---------------------------------
    RelativeLayout layout_viewPager, layout_top_tools;
    LinearLayout layout_bot_tools, layout_imgInfo;
    TextView tv_imgInfo_title,tv_imgInfo_size, tv_imgInfo_date, tv_imgInfo_path;
    ImageButton btn_img_delete, btn_img_info;
    private boolean singleTouched = false; // Check the Tools display or hide
    private boolean infoFlag = false; // Check the Image Info display or hide


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail_view);

        Bundle extras = getIntent().getExtras();
        images = (List<ImageItem>) extras.getSerializable("images");
        position = extras.getInt("position");

        setWidget();
        init();
    }

    private void init() {
        slideAdapter = new ImageSlideAdapter(this, images, this);
        viewPager.setAdapter(slideAdapter);
        viewPager.setCurrentItem(position);

        horizontalAdapter = new HorizontalAdapter(this, images, this);
        rv_horizontal.setAdapter(horizontalAdapter);
        horizontalLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_horizontal.setLayoutManager(horizontalLayoutManger); // RecyclerView 를 수평 방향으로 스크롤되게 함.
        scrollToCenter(rv_horizontal, position); // Replace rv_horizontal.scrollToPosition(position);
    }

    private void setWidget() {
        viewPager = findViewById(R.id.viewPager);
        //viewPager.setOnClickListener(this::clickListener); // viewPager 의 ClickListener 는 ImageSlideAdapter 의 ZoomViewPagerImageView 에 있음.
        viewPager.addOnPageChangeListener(pageChangeListener);

        rv_horizontal = findViewById(R.id.rv_horizontal);
        btn_img_delete = findViewById(R.id.btn_img_delete);
        btn_img_info = findViewById(R.id.btn_img_info);
        btn_img_delete.setOnClickListener(btnClickListener);
        btn_img_info.setOnClickListener(btnClickListener);

        layout_viewPager = findViewById(R.id.layout_viewPager); // TODO: will be deleted if not needed

        layout_top_tools = findViewById(R.id.layout_top_tools);
        layout_bot_tools = findViewById(R.id.layout_bot_tools);

        layout_imgInfo = findViewById(R.id.layout_imgInfo);
        tv_imgInfo_title = findViewById(R.id.tv_imgInfo_title);
        tv_imgInfo_size = findViewById(R.id.tv_imgInfo_size);
        tv_imgInfo_date = findViewById(R.id.tv_imgInfo_date);
        tv_imgInfo_path = findViewById(R.id.tv_imgInfo_path);
    }

    /**
     * 갤러리 작업 중 이미지 파일이 추가됐을때 목록을 refresh, update 해줌
     */
    private void updateRefresh(int position) {
        slideAdapter = new ImageSlideAdapter(this, images, this);
        viewPager.setAdapter(slideAdapter);
        viewPager.setCurrentItem(position);

        horizontalAdapter = new HorizontalAdapter(this, images, this);
        rv_horizontal.setAdapter(horizontalAdapter);
        scrollToCenter(rv_horizontal, position); // Replace rv_horizontal.scrollToPosition(position);
    }

    /**
     * Center the Selected Position(Highlight) at Horizontal RecyclerView(Bottom Preview)
     * 하단부 프리뷰의 하이라이트를 정중앙에 오도록 하는 함수
     * (recyclerView.scrollToPosition() 을 대체함)
     */
    private void scrollToCenter(View v, int position) {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        int layoutSize = v.getWidth() / 2;
        if (layoutSize == 0) layoutSize = size.x / 2; // layoutSize 가 측정되지 않으면 스크린 사이즈로 함.

        int itemSize = horizontalAdapter.getLayoutSize() / 2;
        if (itemSize == 0) itemSize = (int) (layoutSize / 4.5); // 4.5 는 80dp 기준임. 추후 바꿔야함.

        int offSet = layoutSize - itemSize;

        horizontalLayoutManger.scrollToPositionWithOffset(position, offSet);
        //Log.i("scrollToCenter", "layout: "+layoutSize+" item: "+itemSize+" position: "+position);
    }

    /**
     * ClickListener of Top Tools as delete image, info image
     * 이미지 삭제, 이미지 정보 버튼 클릭리스너
     */
    View.OnClickListener btnClickListener = v -> {
        switch (v.getId()) {
            case R.id.btn_img_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("DELETE IMAGE");
                builder.setMessage("Are you sure DELETE this image?");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    File file = new File(images.get(viewPager.getCurrentItem()).getPath());
                    boolean delete = file.delete();
                    if (delete) images.remove(viewPager.getCurrentItem());

                    updateRefresh(viewPager.getCurrentItem());
                });
                builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                builder.show();
                break;
            case R.id.btn_img_info:
                // TODO: Zoom 상태로 Slide 안되도록.
                String size_orig = images.get(viewPager.getCurrentItem()).getSize();
                String date_orig = images.get(viewPager.getCurrentItem()).getDate();
                String path = images.get(viewPager.getCurrentItem()).getPath();
                String title = path.substring(path.lastIndexOf("/")+1); // 경로에서 마지막의 파일이름만 가져옴.
                String date = convertAndFormatDate(date_orig);
                String size = convertAndFormatSize(size_orig);

                if (!infoFlag) {
                    layout_imgInfo.setVisibility(View.VISIBLE);
                    infoFlag = true;
                    tv_imgInfo_title.append(title);tv_imgInfo_size.append(size);
                    tv_imgInfo_date.append(date);tv_imgInfo_path.append(path);

                    viewPager.setPagingEnabled(false); // when display image info don't work viewPager
                    enableDisableView(viewPager, false); // when display image info don't work viewPager
                    rv_horizontal.setVisibility(View.GONE);
                } else {
                    layout_imgInfo.setVisibility(View.GONE);
                    infoFlag = false;
                    tv_imgInfo_title.setText(R.string.imgInfo_title);tv_imgInfo_size.setText(R.string.imgInfo_size);
                    tv_imgInfo_date.setText(R.string.imgInfo_date);tv_imgInfo_path.setText(R.string.imgInfo_path);

                    viewPager.setPagingEnabled(true);
                    enableDisableView(viewPager, true);
                    rv_horizontal.setVisibility(View.VISIBLE);
                }
                break;
            default: break;
        }
    };
    private String convertAndFormatDate(String date) {
        long date_long = Long.parseLong(date) * 1000L;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        return sdf.format(new Date(date_long));
    }
    private String convertAndFormatSize(String size_orig) {
        long size = Long.parseLong(size_orig);
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    private void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    horizontalAdapter.setCurrentPosition(position); // viewPager 가 선택한 아이템과 recyclerView 가 선택한 아이템이 같도록 설정
                    //horizontalAdapter.notifyItemChanged(position);
                    horizontalAdapter.notifyDataSetChanged();
                    scrollToCenter(rv_horizontal, position); // Replace rv_horizontal.scrollToPosition(position);
                }
            }, 200);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    /**
     * Interface for Synchronizing ViewPager and Horizontal RecyclerView(Bottom Preview)
     */
    @Override
    public void setPagerAdapter(int position) {
        viewPager.setCurrentItem(position);
    }
    @Override
    public void setVisibleTools() {
        if (!singleTouched) {
            layout_top_tools.setVisibility(View.VISIBLE);
            layout_bot_tools.setVisibility(View.VISIBLE);
            singleTouched = true;
        } else {
            layout_top_tools.setVisibility(View.GONE);
            layout_bot_tools.setVisibility(View.GONE);
            singleTouched = false;
        }
        //Log.i("TOUCHED", "clickListener"+touched);
    }
    @Override
    public void enableSwipeViewPager(boolean enabled) {
        viewPager.setPagingEnabled(enabled);
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

    @Override
    public void onBackPressed() {
        if (layout_imgInfo.getVisibility() == View.VISIBLE) {
            layout_imgInfo.setVisibility(View.GONE);
            tv_imgInfo_title.setText(R.string.imgInfo_title);tv_imgInfo_size.setText(R.string.imgInfo_size);
            tv_imgInfo_date.setText(R.string.imgInfo_date);tv_imgInfo_path.setText(R.string.imgInfo_path);
            viewPager.setPagingEnabled(true);
            enableDisableView(viewPager, true);
            rv_horizontal.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}
