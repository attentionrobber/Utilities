package com.example.utilities.Memo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;
import com.example.utilities.data.DBHelper;
import com.example.utilities.domain.Memo;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

/**
 * Used by: MemoAdapter
 * Called by: MemoAdapter
 */
public class MemoViewActivity extends AppCompatActivity {

    private static final String TAG = "MemoViewActivity";

    final int REQ_MODIFY = 100; // Activity Request code

    // Widget
    TextView tv_title, tv_content;
    //Button btn_modify, btn_delete;
    ImageView imageView;

    // 메모 데이터 관련
    List<Memo> memoList;
    int position = 0; // Memo position

    // 이미지 관련
    String strUri = "";

    // DB 관련
    DBHelper dbHelper;
    Dao<Memo, Integer> memoDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_view);

        setWidget();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras(); // 번들 단위로 받아온다.
            assert bundle != null;
            position = bundle.getInt("position"); // position 을 통해 Memo data 에 접근한다.
        }

        try {
            initDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setMemo(position); // input loaded contents to View. 각 view 에 로드한 내용을 넣는다.
    }

    private void setWidget() {
        findViewById(R.id.btn_modify).setOnClickListener(clickListener);
        findViewById(R.id.btn_delete).setOnClickListener(clickListener);
        findViewById(R.id.btn_cancel).setOnClickListener(clickListener);
        tv_title = findViewById(R.id.textView_title);
        tv_content= findViewById(R.id.textView_content);
        tv_content.setMovementMethod(new ScrollingMovementMethod()); // TextView Scrolling. 텍스트뷰 스크롤바 생기게 함. xml android:scrollbars="vertical" 외
        imageView = findViewById(R.id.imageView);
    }

    /**
     * DB 초기화 및 로드
     */
    public void initDB() throws SQLException {
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class); // static 불가
        memoDao = dbHelper.getMemoDao();

        memoList = memoDao.queryForAll();
    }

    /**
     * 각 view 에 내용을 세팅한다.
     */
    private void setMemo(int position) {

        // position 으로 Memo data 를 가져온다.
        String title = memoList.get(position).getTitle();
        String context = memoList.get(position).getContent();
        strUri = memoList.get(position).getImgUri();

        // 가져온 Memo data 를 뿌려준다.
        tv_title.setText(title);
        tv_content.setText(inputImageToTextView(context, strUri)); // ImageSpan 을 이용해 uri text 를 이미지로 표시한다.
    }

    /**
     * ImageSpan 과 SpannableStringBuilder 를 이용해
     * Uri 텍스트를 이미지로 TextView 에 표시하기
     */
    private CharSequence inputImageToTextView(String context, String strUri) {

        SpannableStringBuilder builder = new SpannableStringBuilder(context); // TextView 의 uri 를 text 가 아닌 이미지로 표현하기 위한 builder

        if (!strUri.equals("")) { // 이미지가 있을 경우에만 실행
            try {
                BufferedReader br = new BufferedReader(new StringReader(strUri));
                String line;
                while ((line = br.readLine()) != null) { // context 에서 strUri 의 시작, 마지막 위치를 찾는다.
                    if (context.contains(line)) {
                        int start = context.indexOf(line); // uri text 의 시작 위치
                        int end = start + line.length(); // uri text 의 마지막 위치
                        Uri uri = Uri.parse(line); // strUri 를 Uri 형식으로 바꿔준다.
                        ImageSpan imageSpan = new ImageSpan(this, uri); // ImageSpan 으로 uri 를 이미지로 나타낸다.
                        builder.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // builder 를 통해 Span 해준다.
                        //Log.i("TESTS", "" + start + " // " + end + " // " + line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        builder.setSpan(new ClickableSpan() { // span 이미지 클릭이벤트
//            @Override
//            public void onClick(@NonNull View widget) {
//                builder.delete(11, 13);
//                tv_content.setText(""); // to be MODIFIED
//            }
//        }, 11, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //tv_content.setMovementMethod(LinkMovementMethod.getInstance()); // TextView 에 있는 이미지 클릭 가능하도록함
        return builder;
    }

    private void modifyMemo() {
        Intent intent = new Intent(this, MemoModifyActivity.class);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQ_MODIFY);
    }

    private void deleteMemo(Memo memo) throws SQLException{
        memoDao.delete(memo);
        finish(); // Activity 종료
    }

    /**
     * Listener 계열
     */
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_modify:
                    modifyMemo(); break;

                case R.id.btn_delete:
                    AlertDialog.Builder alert_delete = new AlertDialog.Builder(MemoViewActivity.this);
                    alert_delete.setTitle("DELETE");
                    alert_delete.setMessage("Are you sure you want to delete?");
                    alert_delete.setPositiveButton("OK", (dialog, which) -> {
                        try {
                            deleteMemo(memoList.get(position));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    alert_delete.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    alert_delete.show();

                    break;
                case R.id.btn_cancel:
                    MemoViewActivity.super.onBackPressed();
                    break;
            }
        }
    }; // clickListener

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_MODIFY:
                if (resultCode == RESULT_OK) { // 수정이 완료된 경우
                    try {
                        initDB(); // DB 다시 로드
                        setMemo(position); // 수정된 내용으로 refresh
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.print("onStart 시작", TAG);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Logger.print("onResume 시작", TAG);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Logger.print("onPause 시작", TAG);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Logger.print("onStop 시작", TAG);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.print("onRestart 시작", TAG);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.print("onDestroy 시작", TAG);
    }
}
