package com.example.utilities.Gallery;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.utilities.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 3;

    RecyclerView recyclerView;
    GalleryRecyclerViewAdapter adapter;

    private static View view = null;

    List<String> imgdatas = new ArrayList<>(); // 사진정보 데이터 저장소
    Uri fileUri = null; // Image // 사진 촬영 후 임시로 저장할 공간

    private final int REQ_PERMISSION = 100; // 권한 요청 코드
    private final int REQ_CAMERA = 101; // 카매라 요청 코드
    private final int REQ_GALLERY = 102; // 갤러리 요청 코드

    Button btn_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        setWidget();
        imgdatas = loadData();
        init(); //어댑터 세팅
    }

    private void setWidget() {
        recyclerView = findViewById(R.id.rv_gallery);
    }

    private void init() {
        adapter = new GalleryRecyclerViewAdapter(this, imgdatas);  // Adapter 생성하기

        recyclerView.setAdapter(adapter); // Recycler View에 Adapter 세팅하기
        //recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Recycler View 매니저 등록하기(View의 모양(Grid, 일반, 비대칭Grid)을 결정한다.)
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }
    }

    private List<String> loadData() {

        List<String> datas = new ArrayList<>();

        // 폰에서 이미지를 가져온 후 datas에 세팅한다.
        ContentResolver resolver = getContentResolver();
        // 1. 데이터 URI 정의
        Uri target = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 2. Projection 정의
        String projection[] = { MediaStore.Images.Media.DATA }; // DATA : image 경로가 있는 컬럼명
        // 정렬 추가
        String order = MediaStore.MediaColumns.DATE_ADDED + " DESC";
        // 3. 데이터 가져오기
        Cursor cursor = resolver.query(target, projection, null, null, order);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                //int idx = cursor.getColumnIndex(projection[0]);
                String uriStr = cursor.getString(0);
                datas.add(uriStr);
            }
            cursor.close();
        }
        return datas;
    }
}
