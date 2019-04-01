package com.example.utilities.Gallery;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.utilities.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PicsViewActivity extends AppCompatActivity {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 3;

    RecyclerView recyclerView;
    GalleryRecyclerViewAdapter adapter;
    List<GridViewItem> imgdatas = new ArrayList<>(); // 사진정보 데이터 저장소
    Uri fileUri = null; // Image // 사진 촬영 후 임시로 저장할 공간

    private final int REQ_PERMISSION = 100; // 권한 요청 코드
    private final int REQ_CAMERA = 101; // 카매라 요청 코드
    private final int REQ_GALLERY = 102; // 갤러리 요청 코드

    final String SD_PATH = "/storage/sdcard/DCIM/";
    final String DCIM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    final String PICS_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pics_view);

        setWidget();
        imgdatas = loadData();
        init(); // 어댑터 세팅
    }

    private void setWidget() {
        recyclerView = findViewById(R.id.rv_gallery);
    }

    private void init() {
        adapter = new GalleryRecyclerViewAdapter(this, imgdatas);  // Adapter 생성하기
        recyclerView.setAdapter(adapter); // RecyclerView 에 Adapter 세팅하기

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Recycler View 매니저 등록하기(View의 모양(Grid, 일반, 비대칭Grid)을 결정한다.)
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }
    }

    private List<GridViewItem> loadData() {

        List<GridViewItem> datas = new ArrayList<>();

        // List all the items within the folder.
        File[] files = new File(DCIM_PATH+"/CAMERA").listFiles(new PicsViewActivity.ImageFileFilter());

        sortFilesBy(files, "DATE"); // sort 파일 정렬

        for (File file : files) {
            // Add the directories containing images or sub-directories
            if (file.isDirectory() && file.listFiles(new PicsViewActivity.ImageFileFilter()).length > 0) {
                datas.add(new GridViewItem(file.getAbsolutePath(), true));
            }
            else { // Add the images
                //Bitmap image = BitmapHelper.decodeBitmapFromFile(file.getAbsolutePath(), 50, 50);
                datas.add(new GridViewItem(file.getAbsolutePath(), false));

            }
        }

        return datas;
    }

    /**
     * Checks the file to see if it has a compatible extension.
     */
    private boolean isImageFile(String filePath) {
        // Add to possible other formats WANTS
        // jpg 이거나 png 형식일 경우 true 리턴한다.
        return filePath.endsWith(".jpg") || filePath.endsWith(".png");
    }

    /**
     * This can be used to filter files.
     */
    private class ImageFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }

    /**
     * Method that how to sort files
     * 파일 정렬 방법 선택하는 함수
     */
    private void sortFilesBy(File[] files, String sort) {
        switch (sort) {
            case "DATE": // sort file by date desc 파일을 날짜별로 내림차순 정렬(최신순)
                Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
//                Arrays.sort(files, new Comparator<File>() {
//                    public int compare(File f1, File f2) {
//                        return Long.compare(f2.lastModified(), f1.lastModified());
//                    }
//                });
                break;
            case "NAME": // sort file by name 파일을 이름순으로 정렬
                Arrays.sort(files, (f1, f2) -> f1.getName().compareTo(f2.getName()));
                break;
        }

    }
}
