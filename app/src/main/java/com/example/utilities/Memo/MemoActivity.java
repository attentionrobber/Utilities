package com.example.utilities.Memo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.utilities.R;
import com.example.utilities.data.DBHelper;
import com.example.utilities.domain.Memo;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoActivity extends AppCompatActivity {

    List<Memo> memos = new ArrayList<>();

    MemoAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    int index = -1;
    int position = 0; // Saved Scroll Position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        setWidget();

        try {
            loadData(); // 4. DB 에서 데이터 로드
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        init(); // 5. 어댑터 세팅
    }

    private void setWidget() {
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        findViewById(R.id.btn_new).setOnClickListener(v -> clickListener());
    }

    private void clickListener() {
        Intent intent = new Intent(MemoActivity.this, MemoNewActivity.class);
        startActivity(intent);
    }

    private void init() {
        adapter = new MemoAdapter(memos, this); // 2. Adapter 생성하기
        recyclerView.setAdapter(adapter); // 3. Recycler View에 Adapter 세팅하기
        recyclerView.setLayoutManager(layoutManager); // 4. Recycler View 매니저 등록하기(View의 모양(Grid, 일반, 비대칭Grid)을 결정한다.)
    }

    public void loadData() throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class); // static 불가
        //DBHelper dbHelper = new DBHelper(context);
        Dao<Memo, Integer> memoDao = dbHelper.getMemoDao();

        memos = memoDao.queryForAll();
        // 최신 날짜 기준으로 정렬
        Collections.sort(memos, (o1, o2) -> Long.compare(o2.getCurrentDate().getTime(), o1.getCurrentDate().getTime()));
    }

    @Override
    protected void onPause() {
        super.onPause();

        index = layoutManager.findFirstVisibleItemPosition();
        View v = recyclerView.getChildAt(0); // recyclerView 의 자식 뷰를 가져온다.(여기선 CardView 의 레이아웃)
        position = (v == null) ? 0 : (v.getTop() - recyclerView.getPaddingTop()); // 삼항연산자 아래 4줄 코드와 같은 역할
//        if (v == null)
//            position = 0;
//        else
//            position = v.getTop() - recyclerView.getPaddingTop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            loadData(); // 새 글을 쓰거나 수정하고난 뒤에 메인화면이 떴을 때
            init(); // Refresh 해준다.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (index != -1)
            layoutManager.scrollToPositionWithOffset(index, position); // Keep Scroll Position. 현재 스크롤 위치로 세팅
    }
}
