package com.example.utilities.Gallery;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 핸드폰에 있는 모든 이미지를 폴더별로 나타낸다.
 * Shows all images on mobile phone by folder
 */
public class GalleryActivity extends AppCompatActivity {

    final String TAG = "TAGGalleryActivity";

    private int mColumnCount = 3;

    GridView gridView; // 폴더를 표현하는 뷰
    RecyclerView recyclerView; // 폴더에 있는 이미지를 표현하는 뷰

    List<ImageBucket> buckets; // Bucket containing folders with images. 사진이 있는 폴더를 담는 버켓
    List<Integer> countOfEachBuckets = new ArrayList<>(); // 각 버켓(폴더) 안의 이미지 갯수
    List<ImageItem> images; // Images in bucket. bucket 안의 이미지
    GalleryFolderAdapter adapter; // Adapter to apply to buckets. 버켓에 적용할 어댑터
    GalleryRecyclerViewAdapter imgAdapter; // Adapter to apply to images. images 에 적용할 어댑터.

//    Uri fileUri = null; // Image // 사진 촬영 후 임시로 저장할 공간 // TODO : 카메라 버튼 추가
//    private final int REQ_PERMISSION = 100; // 권한 요청 코드
//    private final int REQ_CAMERA = 101; // 카매라 요청 코드
//    private final int REQ_GALLERY = 102; // 갤러리 요청 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        setWidget();

        buckets = getImageBuckets(this); // 이미지가 들어있는 폴더들의 List 를 생성한다.
        adapter = new GalleryFolderAdapter(this, buckets, countOfEachBuckets);
        gridView.setAdapter(adapter);
    }

    private void setWidget() {
        gridView = findViewById(R.id.gridView);
        recyclerView = findViewById(R.id.rv_gallery);

        gridView.setOnItemClickListener(this::onItemClick);
        //recyclerView.setOnClickListener(this); // RecyclerView 의 ClickListener 는 GalleryRecyclerViewAdapter 에 있음.
    }

    /**
     * URI 로 Image 를 필터링 하고 Image 가 속해 있는 폴더 이름 별로
     * Bucket List 에 담는다. => 이미지가 있는 폴더를 찾아 Bucket 에 담는 함수.
     */
    public List<ImageBucket> getImageBuckets(Context mContext){

        List<ImageBucket> buckets = new ArrayList<>();
        List<String> bucketSet = new ArrayList<>(); // List of Image folder name. 이미지가 들어있는 폴더명의 List.
        List<String> bucketCount = new ArrayList<>(); // List to find count of images in Folder. 폴더 안의 이미지 갯수를 구하기 위한 List.

        String bucketName, firstImage; // 이미지가 속한 폴더 이름, 대표 이미지

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = { MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // Name of image Folder
                                 MediaStore.Images.Media.DATA }; // image
        String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, orderBy);
        if(cursor != null){
            File file;
            while (cursor.moveToNext()) {
                bucketName = cursor.getString(cursor.getColumnIndex(projection[0]));
                firstImage = cursor.getString(cursor.getColumnIndex(projection[1]));
                bucketCount.add(bucketName); // 이미지가 속해있는 폴더명(bucketName)을 이미지 개수만큼 add 한다.
                file = new File(firstImage);
                if (file.exists() && !bucketSet.contains(bucketName)) { // 이미지가 존재하는 파일이면서 BucketName 이 중복되지 않을 경우
                    bucketSet.add(bucketName); // 중복되지 않는 bucketName 일 경우 bucketSet 에 add 한다. -> 폴더명만 추출할 수 있다.
                    buckets.add(new ImageBucket(bucketName, firstImage)); // 폴더명과 폴더의 첫이미지 객체를 생성한다.
                }
            }
            cursor.close();
        }
        /*
           bucketCount 에서 같은 폴더이름을 가진 것들의 개수를 구한다.
           -> 각 폴더에 들어있는 이미지의 개수를 구할 수 있다.
         */
        for (int i = 0; i < bucketSet.size(); i++) {
            countOfEachBuckets.add( Collections.frequency(bucketCount, bucketSet.get(i)) );
        }

//        File[] files = new File(ROOT_DIR).listFiles(new ImageFileFilter());
//        //File[] files = new File(DCIM_PATH).listFiles(new ImageFileFilter());
//
//        for (File file : files) {
//            // Add the directories containing images or sub-directories
//            if ( file.isDirectory() && (file.listFiles(new ImageFileFilter()).length > 0) ) {
//                buckets.add(new Bucket(file.getName(), file.getAbsolutePath()));
//            }
//        }

        return buckets;
    }

    /**
     * Buckets 에 있는 image 들을 String List 에 담는다.
     */
    public List<ImageItem> getImagesByBucket(@NonNull String bucketName){

        List<ImageItem> images = new ArrayList<>();
        String path, title, date, size;

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Images.Media.DATA, // uri(path)
                                MediaStore.Images.Media.TITLE, // name
                                MediaStore.Images.Media.DATE_ADDED,
                                MediaStore.Images.Media.SIZE };
        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" =?"; // 폴더명으로 선별(bucketName)
        String orderBy = MediaStore.Images.Media.DATE_ADDED+" DESC";

        Cursor cursor = getContentResolver().query(uri, projection, selection, new String[]{bucketName}, orderBy);
        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                path = cursor.getString(cursor.getColumnIndex(projection[0]));
                title = cursor.getString(cursor.getColumnIndex(projection[1]));
                date = cursor.getString(cursor.getColumnIndex(projection[2]));
                size = cursor.getString(cursor.getColumnIndex(projection[3]));
                file = new File(path);
                if (file.exists() && !images.contains(path)) { // images 에 path 가 없을 경우( = 같은 파일이 아닐 경우)
                    images.add(new ImageItem(path, title, date, size));
                }
            }
            cursor.close();
        }
        return images;
    }

    /**
     * Checks the file to see if it has a compatible extension.
     * Add to possible other formats WANTS
     */
    private boolean isImageFile(String filePath) {
        // jpg 이거나 png 형식일 경우 true 리턴한다.
        return filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png") || filePath.endsWith(".gif");
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
     * Bucket(이미지를 담는 폴더) 클릭 시 이벤트
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        images = getImagesByBucket(buckets.get(position).getName());

        gridView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        imgAdapter = new GalleryRecyclerViewAdapter(this, images);
        recyclerView.setAdapter(imgAdapter);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Recycler View 매니저 등록하기(View의 모양(Grid, 일반, 비대칭Grid)을 결정한다.)
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }
    }

    /**
     * Event when BackButton is clicked
     * 백버튼 클릭 시 이벤트
     */
    @Override
    public void onBackPressed() {
        if (gridView.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        }
        gridView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
}