package com.example.utilities.Gallery;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.utilities.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageDetailViewActivity extends AppCompatActivity {

    ImageView imageDetailView;

    List<ImageItem> images = new ArrayList<>(); // 사진정보 데이터 저장소

    Uri fileUri = null; // Image // 사진 촬영 후 임시로 저장할 공간

    private final int REQ_PERMISSION = 100; // 권한 요청 코드
    private final int REQ_CAMERA = 101; // 카매라 요청 코드
    private final int REQ_GALLERY = 102; // 갤러리 요청 코드

    final String SD_PATH = "/storage/sdcard/DCIM/";
    final String DCIM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail_view);

        setWidget();
        //imgdatas = loadData();
        //images = getIntent().getStringArrayListExtra("images");
        String uri = getIntent().getStringExtra("image");
        Glide.with(this).load(uri).into(imageDetailView);
        init(); // 어댑터 세팅
    }

    private void setWidget() {
        imageDetailView = findViewById(R.id.imageDetailView);
    }

    private void init() {

    }
}
