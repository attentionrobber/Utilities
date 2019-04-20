package com.example.utilities.Memo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
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

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    String[] strUri = new String[10];

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
        tv_content.setMovementMethod(new ScrollingMovementMethod()); // TextView Scrolling
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
        //tv_content.setText(content);


        Log.i("TESTS", ""+strUri.toString());

        // Image 를 Uri 로 저장해야함. String 을 그대로 옮기기는 힘듬.
        SpannableStringBuilder builder = new SpannableStringBuilder(context);

        // context 에서 strUri 의 시작, 마지막 위치를 찾는다.
//        if (context.contains(strUri.get(0))) {
//            int start = context.indexOf(strUri.get(0));
//            int end = context.lastIndexOf(strUri.get(0));
//            Uri uri = null;
//            for (int i = 0; i < strUri.size(); i++) {
//                uri = Uri.parse(strUri.get(i));
//                ImageSpan imageSpan = new ImageSpan(this, uri); // strUri 있는갯수만큼
//                builder.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }




//        builder.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//                builder.delete(11, 13);
//                tv_content.setText(""); // to be MODIFIED
//            }
//        }, 11, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        tv_content.setText(context);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance()); // TextView 에 있는 이미지 클릭 가능하도록함




//        if (strUri.length() != 0) { // 이미지가 존재할 경우만 세팅한다.
//            Glide.with(this).load(Uri.parse(strUri)).into(imageView);
//        }
    }

    /**
     * ImageSpan 과 SpannableStringBuilder 를 이용해
     * Uri 로 된 이미지를 TextView 에 표시하기
     */
    public CharSequence addSpans(Context context, CharSequence msg) {
        SpannableStringBuilder builder = new SpannableStringBuilder(msg);
        Pattern pattern = Pattern.compile("\\[([^\\[\\]]+)\\]");
        if(pattern != null) {
            Matcher matcher = pattern.matcher( msg );
            int matchesSoFar = 0;
            while(matcher.find()) {
                CharSequence cs = matcher.group().subSequence(1, matcher.group().length()-1);
                int value = Integer.parseInt(cs.toString());
                System.out.println("pattern is::"+matcher.group().subSequence(1, matcher.group().length()-1));
                int start = matcher.start() - (matchesSoFar * 2);
                int end = matcher.end() - (matchesSoFar * 2);
                Drawable Smiley = context.getResources().getDrawable(value);
                Smiley.setBounds(0, 0,15,15);
                builder.setSpan(new ImageSpan(Smiley), start + 1, end - 1, 0 );
                builder.delete(start, start + 1);
                builder.delete(end - 2, end -1);
                matchesSoFar++;
            }
        }
        return builder;
    }
    public CharSequence spans(String context) {


        return spans(context);
    }

    private void modifyMemo() {

        Intent intent = new Intent(this, MemoModifyActivity.class);
        intent.putExtra("position", position);

        //finish();
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
                    modifyMemo(); break; // TODO: 수정이 완료된 후 refresh 해주기

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
