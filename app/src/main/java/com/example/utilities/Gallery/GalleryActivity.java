package com.example.utilities.Gallery;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final String TAG = "GalleryActivityTEST";

    GridView gridView;

    List<GridViewItem> gridItems;

    String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    final String DCIM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    final String PICS_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();



    List<ImageBucket> buckets; // 사진이 있는 폴더를 담는 버켓
    GalleryFolderAdapter adapter; // 버켓에 적용할 어댑터


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        buckets = getImageBuckets(this);
        adapter = new GalleryFolderAdapter(this, buckets);
        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

    }

    public List<ImageBucket> getImageBuckets(Context mContext){

        List<ImageBucket> buckets = new ArrayList<>();
        List<String> bucketSet = new ArrayList<>(); // List of Image folder name

        String bucketName, fisrtImage; // 이미지가 속한 폴더 이름, 대표 이미지

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = { MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // image folder name
                                 MediaStore.Images.Media.DATA}; // image
        String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, orderBy);
        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                bucketName = cursor.getString(cursor.getColumnIndex(projection[0]));
                fisrtImage = cursor.getString(cursor.getColumnIndex(projection[1]));
                file = new File(fisrtImage);
                if (file.exists() && !bucketSet.contains(bucketName)) {
                    bucketSet.add(bucketName);
                    buckets.add(new ImageBucket(bucketName, fisrtImage));
                }
            }
            cursor.close();
        }

//        File[] files = new File(ROOT_DIR).listFiles(new ImageFileFilter());
//        //File[] files = new File(DCIM_PATH).listFiles(new ImageFileFilter());
//
//        for (File file : files) {
//            // Add the directories containing images or sub-directories
//            if ( file.isDirectory() && (file.listFiles(new ImageFileFilter()).length > 0) ) {
//                buckets.add(new Bucket(file.getName(), file.getAbsolutePath()));
//
//            }
//        }

        return buckets;
    }

    public List<String> getImagesByBucket(@NonNull String bucketPath) {

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // image folder name
                MediaStore.Images.Media.DATA, // image
                MediaStore.Images.Media.TITLE }; // image name
        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" =?";
        String orderBy = MediaStore.Images.Media.DATE_ADDED+" DESC";

        List<String> images = new ArrayList<>();

        //Cursor cursor = mContext.getContentResolver().query(uri, projection, selection,new String[]{bucketPath}, orderBy);
        Cursor cursor = getContentResolver().query(uri, projection, selection,new String[]{bucketPath}, orderBy);

        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(projection[0]));
                file = new File(path);
                if (file.exists() && !images.contains(path)) {
                    images.add(path);
                }
            }
            cursor.close();
        }
        return images;
    }





    /**
     * Go through the specified directory, and create items to display in our
     * GridView
     */
    private List<GridViewItem> createGridItems(String directoryPath) {

        List<GridViewItem> items = new ArrayList<>();

        // List all the items within the folder.
        File[] files = new File(directoryPath).listFiles(new ImageFileFilter());

        for (File file : files) {
            // Add the directories containing images or sub-directories
            if (file.isDirectory() && file.listFiles(new ImageFileFilter()).length > 0) {
                //items.add(new GridViewItem(file.getAbsolutePath(), true, null));
            }
            else { // Add the images
                //Bitmap image = BitmapHelper.decodeBitmapFromFile(file.getAbsolutePath(), 50, 50);
                //items.add(new GridViewItem(file.getAbsolutePath(), false, image));
            }
        }

        return items;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (gridItems.get(position).isDirectory()) { // 폴더일 경우
            //setGridAdapter(gridItems.get(position).getPath());
        }
        else {
            // TODO: Bigger Display the image
        }
    }

}