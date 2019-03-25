package com.example.utilities.Memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;
import com.example.utilities.data.DBHelper;
import com.example.utilities.domain.Memo;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MemoViewActivity extends AppCompatActivity {

    private static final String TAG = "MemoViewActivity";

    List<Memo> datas;
    int position = 0;

    String strUri;

    DBHelper dbHelper;
    Dao<Memo, Integer> memoDao;

    TextView textView_title, textView_content;
    Button btn_modify, btn_delete;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_view);

        setWidget();
        setListener();

        try {
            setMemo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setMemo() throws SQLException{

        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class); // static 불가
        memoDao = dbHelper.getMemoDao();

        datas = memoDao.queryForAll();

        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getExtras(); // 번들 단위로 받아온다.

            // Bundle을 변수에 세팅
            position = bundle.getInt("position");
            String title = bundle.getString("title");
            String content = bundle.getString("content");

            // Bundle 로 받아온 변수를 각 Widget 에 세팅
            textView_title.setText(title);
            textView_content.setText(content);

            // Image가 있을 경우에만 세팅한다.
            strUri = bundle.getString("imageUri");
            if(strUri != null) {
                Uri imageUri = Uri.parse(strUri);
                Glide.with(this).load(imageUri).into(imageView);
            }
        }
    }

    private void modify() throws SQLException {

        // 2. New Activity를 editText에 내용을 받고 띄운다
        Intent intent = new Intent(this, MemoModifyActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("title", textView_title.getText().toString());
        intent.putExtra("content", textView_content.getText().toString());
        intent.putExtra("imageUri", strUri);
        // 1. View Activty 닫고
        finish();
        startActivity(intent);
    }

    private void delete(Memo memo) throws SQLException{
        memoDao.delete(memo);
        finish();
    }

    private void setWidget() {
        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_content = (TextView) findViewById(R.id.textView_content);
        btn_modify = (Button) findViewById(R.id.btn_modify);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    private void setListener() {
        btn_modify.setOnClickListener(clickListener);
        btn_delete.setOnClickListener(clickListener);
    }

    /**
     * Listener 계열
     */
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_modify :
                    try {
                        modify();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btn_delete :
                    AlertDialog.Builder alert_delete = new AlertDialog.Builder(MemoViewActivity.this);
                    alert_delete.setTitle("DELETE");
                    alert_delete.setMessage("Are you sure you want to delete?");
                    alert_delete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                delete(datas.get(position));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    alert_delete.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert_delete.show();

                    break;
            }
        }
    };


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
