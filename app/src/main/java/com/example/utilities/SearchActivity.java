package com.example.utilities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SearchActivity extends AppCompatActivity {

    WebView webView;
    //View view; // Holder형태로 만들어 처리함. 메모리를 중복해서 생성하지 않아도 되기 때문에 성능 향상.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 1. 사용할 위젯을 가져온다.
        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings(); // 웹 세팅 선언, 객체화
        webSettings.setJavaScriptEnabled(true); // java Script를 사용하는 페이지를 사용 설정. (필수임)
        webSettings.setSupportZoom(true);// 화면 확대 기능(zoom) 사용 설정
        //webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 속도 향상(검증 필요)

        // 3. 웹뷰 클라이언트를 지정(하지 않으면 내장 웹브라우저가 팝업된다)
        webView.setWebViewClient(new WebViewClient());
        // 3.1 둘 다 세팅할 것 : 프로토콜에 따라 클라이언트가 선택되는 것으로 파악됨.
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("http://www.google.com");
    }
}
