package com.example.utilities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class SearchActivity extends AppCompatActivity {

    WebView webView;
    View view; // Holder형태로 만들어 처리함. 메모리를 중복해서 생성하지 않아도 되기 때문에 성능 향상.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


    }
}
