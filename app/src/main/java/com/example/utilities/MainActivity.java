package com.example.utilities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.utilities.Gallery.GalleryActivity;
import com.example.utilities.Memo.MemoActivity;


public class MainActivity extends AppCompatActivity {

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
     * ClickListener 를 함수 형태로 만듬
     * View.OnClickListener buttonClickListener = v -> {}
     */
    private void buttonClickListener(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_calc:
                intent = new Intent(this, CalcActivity.class);
                startActivity(intent);
                break;
            case R.id.button_memo:
                intent = new Intent(this, MemoActivity.class);
                startActivity(intent);
                break;
            case R.id.button_unit:
                intent = new Intent(this, UnitActivity.class);
                startActivity(intent);
                break;
            case R.id.button_maps:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                //searchByPlacePicker(); // Deprecated by Google API(Place API)
                break;
            case R.id.button_search:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.button_gallery:
                intent = new Intent(this, GalleryActivity.class);
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
                intent = new Intent(MainActivity.this, ImageDetailViewActivity.class);
                startActivity(intent);
                break;
        }
    };
    */
}
