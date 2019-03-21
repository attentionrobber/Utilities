package com.example.utilities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button_calc, button_memo, button_unit, button_maps, button_search, button_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWidget();
        setListener();
    }

    private void setWidget() {
        button_calc = findViewById(R.id.button_calc);
        button_memo = findViewById(R.id.button_memo);
        button_unit = findViewById(R.id.button_unit);
        button_maps = findViewById(R.id.button_maps);
        button_search = findViewById(R.id.button_search);
        button_gallery = findViewById(R.id.button_gallery);
    }

    private void setListener() {
        button_calc.setOnClickListener(buttonClicklistener);
        button_memo.setOnClickListener(buttonClicklistener);
        button_unit.setOnClickListener(buttonClicklistener);
        button_maps.setOnClickListener(buttonClicklistener);
        button_search.setOnClickListener(buttonClicklistener);
        button_gallery.setOnClickListener(buttonClicklistener);

    }

    View.OnClickListener buttonClicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

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
                    intent = new Intent(MainActivity.this, CalcActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_search:
                    intent = new Intent(MainActivity.this, CalcActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_gallery:
                    intent = new Intent(MainActivity.this, CalcActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

}
