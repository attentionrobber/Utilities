package com.example.utilities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.utilities.Gallery.GalleryActivity;
import com.example.utilities.Memo.MemoActivity;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    //private final int REQ_PLACEPICKER = 98; // PlacePicker(장소 선택) 요청 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWidget();
        //setListener();
    }

    private void setWidget() {
        findViewById(R.id.button_calc).setOnClickListener(this::buttonClickListener);
        findViewById(R.id.button_memo).setOnClickListener(this::buttonClickListener);
        findViewById(R.id.button_unit).setOnClickListener(this::buttonClickListener);
        findViewById(R.id.button_maps).setOnClickListener(this::buttonClickListener);
        findViewById(R.id.button_search).setOnClickListener(this::buttonClickListener);
        findViewById(R.id.button_gallery).setOnClickListener(this::buttonClickListener);
    }

    /**
     * click listener 를 함수 형태로 만듬
     * View.OnClickListener buttonClicklistener = v -> {}
     * 위와 같은 기능을 하는 함수
     */
    private void buttonClickListener(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_calc:
                intent = new Intent(MainActivity.this, CalcActivity.class);
                startActivity(intent);
                break;
            case R.id.button_memo:
                intent = new Intent(MainActivity.this, MemoActivity.class);
                startActivity(intent);
                break;
            case R.id.button_unit:
                intent = new Intent(MainActivity.this, UnitActivity.class);
                startActivity(intent);
                break;
            case R.id.button_maps:
                intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
//                searchByPlacePicker();
                break;
            case R.id.button_search:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.button_gallery:
                intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
//                // 사진 관련 앱(사용할 앱을 선택하세요.) 선택 창 띄우는 방법
//                final int OPEN_GALLERY = 1;
//                intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, ""), OPEN_GALLERY);
                break;
        }
    }
    /*
    View.OnClickListener buttonClicklistener = v -> {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_calc:
                intent = new Intent(MainActivity.this, CalcActivity.class);
                startActivity(intent);
                break;
            case R.id.button_memo:
                intent = new Intent(MainActivity.this, MemoActivity.class);
                startActivity(intent);
                break;
            case R.id.button_unit:
                intent = new Intent(MainActivity.this, UnitActivity.class);
                startActivity(intent);
                break;
            case R.id.button_maps:
                intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                //searchByPlacePicker();
                break;
            case R.id.button_search:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.button_gallery:
                intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
                break;
        }
    };
    */
}
