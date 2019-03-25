package com.example.utilities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.utilities.Memo.MemoActivity;


public class MainActivity extends AppCompatActivity {

    // Button button_calc, button_memo, button_unit, button_maps, button_search, button_gallery;

    private final int REQ_PLACEPICKER = 98; // PlacePicker(장소 선택) 요청 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWidget();
        setListener();
    }

    private void setWidget() {
        findViewById(R.id.button_calc).setOnClickListener(buttonClicklistener);
        findViewById(R.id.button_memo).setOnClickListener(buttonClicklistener);
        findViewById(R.id.button_unit).setOnClickListener(buttonClicklistener);
        findViewById(R.id.button_maps).setOnClickListener(buttonClicklistener);
        findViewById(R.id.button_search).setOnClickListener(buttonClicklistener);
        findViewById(R.id.button_gallery).setOnClickListener(buttonClicklistener);
    }

    private void setListener() {
//        button_calc.setOnClickListener(buttonClicklistener);
//        button_memo.setOnClickListener(buttonClicklistener);
//        button_unit.setOnClickListener(buttonClicklistener);
//        button_maps.setOnClickListener(buttonClicklistener);
//        button_search.setOnClickListener(buttonClicklistener);
//        button_gallery.setOnClickListener(buttonClicklistener);

    }

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
//                searchByPlacePicker();
                break;
            case R.id.button_search:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.button_gallery:
                intent = new Intent(MainActivity.this, CalcActivity.class);
                startActivity(intent);
                break;
        }
    };

}
