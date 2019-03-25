package com.example.utilities.Memo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.utilities.R;
import com.example.utilities.data.DBHelper;
import com.example.utilities.domain.Memo;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity {

    private static List<Memo> datas = new ArrayList<>();
    MemoAdapter adapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        setWidget();

        try {
            // 4. DB에서 데이터 로드
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 5. 어댑터 세팅
        init();
    }

    private void setWidget() {
        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.imgbtn_new).setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = null;
            switch (v.getId()) {
                case R.id.imgbtn_new :
                    intent = new Intent(MemoActivity.this, MemoNewActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void init() {

        // 2. Adapter 생성하기
        adapter = new MemoAdapter(datas, this);
        // 3. Recycler View에 Adapter 세팅하기
        recyclerView.setAdapter(adapter);
        // 4. Recycler View 매니저 등록하기(View의 모양(Grid, 일반, 비대칭Grid)을 결정한다.)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void loadData() throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class); // static 불가
        //DBHelper dbHelper = new DBHelper(context);
        Dao<Memo, Integer> memoDao = dbHelper.getMemoDao();

        datas = memoDao.queryForAll();
    }

    /**
     * 새 글을 쓰거나 수정하고난 뒤에 메인화면이 떴을 때 Refresh 해준다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        try {
            loadData();
            init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
