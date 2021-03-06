package com.example.utilities.Gallery;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 핸드폰에 있는 모든 이미지를 폴더별로 나타낸다.
 * Shows all images on mobile phone by folder
 * Used by: MemoNewActivity, MemoModifyActivity
 */
public class GalleryActivity extends AppCompatActivity {

    final String TAG = "TAGGalleryActivity";

    private int mColumnCount = 3;

    TextView tv_galleryTitle;
    ImageButton btn_camera;

    GridView gridView; // 폴더를 표현하는 뷰
    GalleryFolderAdapter folderAdapter; // Adapter to apply to buckets. 폴더(버켓)에 적용할 어댑터
    RecyclerView recyclerView; // 폴더에 있는 이미지를 표현하는 뷰
    GalleryRecyclerViewAdapter imgAdapter; // Adapter to apply to images. 이미지에 적용할 어댑터

    List<ImageBucket> buckets = new ArrayList<>(); // Bucket containing folders with images. 사진이 있는 폴더를 담는 버켓
    List<Integer> countOfEachBuckets = new ArrayList<>(); // 각 버켓(폴더) 안의 이미지 갯수
    List<ImageItem> images = new ArrayList<>(); // Images in bucket. bucket 안의 이미지
    String bucketName = ""; // 선택된 버켓(폴더) 이름

    public int req_code = 0; // 다른 액티비티에서 넘어오는 requestCode
//    private final int REQ_PERMISSION = 100; // 권한 요청 코드
    private final int REQ_CAMERA = 101; // 카메라 요청 코드
//    private final int REQ_GALLERY = 102; // 갤러리 요청 코드
    // TODO: recyclerView saved Position 사진 클릭해서 크게 본 후 되돌아와도 위치 기억하기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        if (getIntent() != null)
            req_code = getIntent().getIntExtra("REQ_CODE", 0);

        setWidget();
        init();
    }

    private void setWidget() {
        tv_galleryTitle = findViewById(R.id.tv_galleryTitle);
        btn_camera = findViewById(R.id.btn_camera);
        gridView = findViewById(R.id.gridView);
        recyclerView = findViewById(R.id.rv_gallery);

        btn_camera.setOnClickListener(this::btnClickListener);
        gridView.setOnItemClickListener(this::onItemClick);
        //recyclerView.setOnClickListener(this); // RecyclerView 의 ClickListener 는 GalleryRecyclerViewAdapter 에 있음.
    }

    private void init() {
        if (!buckets.isEmpty() && !images.isEmpty() && !countOfEachBuckets.isEmpty()) { // refresh 될 때 List 초기화
            countOfEachBuckets.clear();
            buckets.clear();
            images.clear();
        }

        buckets = getImageBuckets(this); // 이미지가 들어있는 폴더들의 List 를 생성한다.
        folderAdapter = new GalleryFolderAdapter(this, buckets, countOfEachBuckets);
        //folderAdapter.notifyDataSetChanged();
        gridView.setAdapter(folderAdapter);

        if (!bucketName.equals("")) { // bucketName 이 초기화 됐을 경우
            images = getImagesByBucket(bucketName);
            imgAdapter = new GalleryRecyclerViewAdapter(this, this, images);
            recyclerView.setAdapter(imgAdapter);
        }
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
            Logger.print("TAG", i+" COUNT: "+countOfEachBuckets.get(i).toString());
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
        String path, uri, title, date, size;
        int position = -1;

        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Images.Media.DATA, // path
                                MediaStore.Images.Media._ID, // uri
                                MediaStore.Images.Media.TITLE, // name
                                MediaStore.Images.Media.DATE_ADDED,
                                MediaStore.Images.Media.SIZE };
        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" =?"; // 폴더명으로 선별(bucketName)
        String orderBy = MediaStore.Images.Media.DATE_ADDED+" DESC";

        Cursor cursor = getContentResolver().query(contentUri, projection, selection, new String[]{bucketName}, orderBy);
        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                path = cursor.getString(cursor.getColumnIndex(projection[0]));
                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ""+cursor.getString(cursor.getColumnIndex(projection[1]))).toString();
                title = cursor.getString(cursor.getColumnIndex(projection[2]));
                date = cursor.getString(cursor.getColumnIndex(projection[3]));
                size = cursor.getString(cursor.getColumnIndex(projection[4]));
                file = new File(path);
                if (file.exists() && !images.contains(path)) { // images 에 path 가 없을 경우( = 같은 파일이 아닐 경우)
                    images.add(new ImageItem(path, uri, title, date, size, position++));
                }
            }
            cursor.close();
        }
        return images;
    }

    /**
     * Bucket(이미지를 담는 폴더) 클릭 시 이벤트
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        bucketName = buckets.get(position).getName();
        images = getImagesByBucket(bucketName);

        gridView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        tv_galleryTitle.setText(bucketName + " (" + countOfEachBuckets.get(position) + ")");

        imgAdapter = new GalleryRecyclerViewAdapter(this, this, images);
        recyclerView.setAdapter(imgAdapter);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Recycler View 매니저 등록하기(View의 모양(Grid, 일반, 비대칭Grid)을 결정한다.)
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }
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
     * Camera Button Event
     */
    private void btnClickListener(View view) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        startActivityForResult(intent, REQ_CAMERA);
    }
    //startActivityForResult() 후에 실행되는 메소드
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CAMERA:
                if (resultCode == RESULT_OK) {
                    init();
                    //Logger.print("TAG", "Activity Result");
                }
                break;
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
        tv_galleryTitle.setText("GALLERY");
        gridView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}