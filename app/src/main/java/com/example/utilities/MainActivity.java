package com.example.utilities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.utilities.Gallery.GalleryActivity;
import com.example.utilities.Memo.MemoActivity;
import com.example.utilities.Memo.MemoNewActivity;


public class MainActivity extends AppCompatActivity {

    EditText et_search;
    ImageButton button_search_small;
    TextView tv_search;

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
        findViewById(R.id.button_gallery).setOnClickListener(this::buttonClickListener);
        findViewById(R.id.button_search).setOnClickListener(this::buttonClickListener);
        button_search_small = findViewById(R.id.button_search_small);
        button_search_small.setOnClickListener(this::buttonClickListener);
        et_search = findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(editorActionListener);
        tv_search = findViewById(R.id.tv_search);
    }

    /**
     * ClickListener 를 함수 형태로 만듬
     * View.OnClickListener buttonClickListener = v -> {}
     */
    private void buttonClickListener(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_calc:
                hideKeypad();
                intent = new Intent(this, CalcActivity.class);
                startActivity(intent);
                break;
            case R.id.button_memo:
                hideKeypad();
                intent = new Intent(this, MemoActivity.class);
                startActivity(intent);
                break;
            case R.id.button_unit:
                intent = new Intent(this, UnitActivity.class);
                startActivity(intent);
                break;
            case R.id.button_maps:
                hideKeypad();
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                //searchByPlacePicker(); // Deprecated by Google API(Place API)
                break;
            case R.id.button_search:
                if (et_search.getVisibility() == View.GONE) {
                    displayKeypad(); // 키패드 띄우기
                    setVisibility(true); // 검색창 보이기
                    et_search.requestFocus(); // EditText 에 focus 주기
                    et_search.setFocusableInTouchMode(true);
                } else {
                    hideKeypad(); // 키패드 감추기
                    setVisibility(false); // 검색창 숨기기
                }
                break;
            case R.id.button_gallery:
                hideKeypad();
                intent = new Intent(this, GalleryActivity.class);
                startActivity(intent);
//                // 사진 관련 앱(사용할 앱을 선택하세요.) 선택 창 띄우는 방법
//                final int OPEN_GALLERY = 1;
//                intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, ""), OPEN_GALLERY);
                break;
            case R.id.button_search_small:
                intent = new Intent(this, SearchActivity.class);
                intent.putExtra("search", et_search.getText().toString());
                startActivity(intent);
                break;
        }
    }

    /**
     * 키패드의 Search 버튼 눌렀을때 리스너
     */
    TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeypad(); // 키패드 감추기
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("search", et_search.getText().toString());
                startActivity(intent);
                return true;
            }
            return false;
        }
    };


    /**
     * search 기능에서 검색창을 보이게하거나 숨긴다.
     */
    private void setVisibility(boolean enabled) {
        if (enabled) {
            et_search.setVisibility(View.VISIBLE);
            button_search_small.setVisibility(View.VISIBLE);
            tv_search.setVisibility(View.GONE);
        }
        else {
            et_search.setVisibility(View.GONE);
            button_search_small.setVisibility(View.GONE);
            tv_search.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 키보드(키패드) 띄우기
     */
    public void displayKeypad() {
        InputMethodManager imm;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    /**
     *  키패드(키보드) 없애기
     */
    private void hideKeypad() {
        InputMethodManager imm = (InputMethodManager) getSystemService(MemoNewActivity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if (et_search.getVisibility() == View.VISIBLE) {
            setVisibility(false); // 검색창 숨기기
            hideKeypad(); // 키패드 감추기
        } else
            super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //et_search.clearFocus();
        setVisibility(false);
    }
}
