package com.example.utilities.Memo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.utilities.R;
import com.example.utilities.data.DBHelper;
import com.example.utilities.domain.Memo;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MemoModifyActivity extends AppCompatActivity {

    private static final String TAG = "MemoNewActivity";

    // 권한 관련
    private final int REQ_MYLOCATION = 99; // 내 위치 요청 코드
    private final int REQ_SEARCHLOCATION = 100; // 검색 위치 요청 코드
    private final int REQ_CAMERA = 101; // 카메라 요청 코드
    private final int REQ_GALLERY = 102; // 갤러리 요청 코드

    // 메모 데이터 관련
    List<Memo> datas;
    int position = 0;

    // 이미지 Uri 관련
    Uri imageUri;
    String strUri;

    // 액티비티 호출 관련
    Intent intent;
    Bundle bundle;

    // DB 관련
    DBHelper dbHelper;
    Dao<Memo, Integer> memoDao;

    // Widget 관련
    EditText editText_title, editText_content;
    Button btn_OK, btn_cancle;
    ImageButton imgbtn_addimg;
    ImageView imageView;

    // 위치 관련
    private LocationManager manager; // 위치정보 관리자

    public LocationManager getLocationManager() {
        return manager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_modify);

        setWidget();
        initDB();
        setListener();
    }

    private void updateToDB(Memo memo) throws SQLException {
        memo.setTitle(editText_title.getText().toString());
        memo.setContent(editText_content.getText().toString());
        memo.setCurrentDate(new Date(System.currentTimeMillis()));
        if (imageUri != null) {
            strUri = imageUri.toString(); // Uri를 String으로 변환해서
        } else {
            strUri = "";
        }
        memo.setImgUri(strUri); // memo클래스에 넣는다.
        memoDao.update(memo);
    }

    private void initDB() {
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        try {
            memoDao = dbHelper.getMemoDao();
            datas = memoDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        position = bundle.getInt("position");
    }

    private void setWidget() {
        editText_title = (EditText) findViewById(R.id.textView_title);
        editText_content = (EditText) findViewById(R.id.editText_content);
        btn_OK = (Button) findViewById(R.id.btn_OK);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        imgbtn_addimg = (ImageButton) findViewById(R.id.imgbtn_addimg);
        imageView = (ImageView) findViewById(R.id.imageView);

        intent = getIntent();
        bundle = intent.getExtras();
        editText_title.setText(bundle.getString("title"));
        editText_content.setText(bundle.getString("content"));
        // Image가 있을 경우에만 세팅한다.
        strUri = bundle.getString("imageUri");
        if (strUri != null) {
            Uri imageUri = Uri.parse(strUri);
            Glide.with(this).load(imageUri).into(imageView);
        }
    }

    private void setListener() {
        btn_OK.setOnClickListener(clickListener);
        btn_cancle.setOnClickListener(clickListener);
        imgbtn_addimg.setOnClickListener(clickListener);
        imageView.setOnClickListener(clickListener);
    }

    /**
     * Listener 계열
     */
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_OK:
                    try {
                        updateToDB(datas.get(position));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;
                case R.id.btn_cancle:
                    MemoModifyActivity.super.onBackPressed();
                    break;
                case R.id.imgbtn_addimg :
                    // 1. 팝업창 만들기
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MemoModifyActivity.this);
                    // 2. 팝업창 제목
                    alertDialog.setTitle("Input Image");
                    // 3. Items 만들기
                    final CharSequence[] items = {"Camera", "Gallery"};
                    alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = null;
                            switch (which) {
                                case 0 :
                                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    // 카메라 촬영 후 미디어 컨텐트 Uri를 생성해서 외부저장소에 저장한다.
                                    // 마시멜로 이상 버전은 아래 코드를 반영해야함.
                                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                        ContentValues values = new ContentValues(1);
                                        values.put(MediaStore.Images.Media.MIME_TYPE, "memo/jpg");
                                        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                        // 컨텐트 Uri강제 세팅
                                    }
                                    startActivityForResult(intent, REQ_CAMERA);
                                    break;
                                case 1 :
                                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setType("image/*"); // 외부저장소에 있는 이미지만 가져오기위한 필터링.
                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_GALLERY); // createChooser로 타이틀을 붙여줄 수 있다.
                                    break;
                            }
                        }
                    });
                    alertDialog.show(); // 4. show함수로 팝업창을 띄운다.
                    break;
                case R.id.imageView :
                    AlertDialog.Builder alertImageView = new AlertDialog.Builder(MemoModifyActivity.this);
                    alertImageView.setTitle("Image Option");
                    final CharSequence[] items_ImageView = {"Change", "Delete"};
                    alertImageView.setItems(items_ImageView, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0 : // Change
                                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setType("image/*"); // 외부저장소에 있는 이미지만 가져오기위한 필터링.
                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_GALLERY); // createChooser로 타이틀을 붙여줄 수 있다.
                                    break;
                                case 1 : // Delete
                                    imageView.setImageResource(0);
                                    imageUri = null;
                                    break;
                            }
                        }
                    });
                    alertImageView.show(); // show함수로 팝업창을 띄운다.
                    break;
            }
        }
    };

    //startActivityForResult() 후에 실행되는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CAMERA:
                // 마시멜로버전 이상인 경우에만 getData()에 null이 넘어올것임.
                if (resultCode == RESULT_OK) { // resultCode OK이면 완료되었다는 뜻.
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        if (data != null && data.getData() != null) {
                            imageUri = data.getData();
                        }
                    }
                    if (imageUri != null) {
                        Glide.with(this).load(imageUri).into(imageView);
                        //imageView.setImageURI(fileUri);
                    } else {

                    }
                } else {
                    // TODO: reulstCode가 uri가 남아있는데 삭제처리해야함.
                }
                break;
            case REQ_GALLERY :
                //if(data != null && data.getData() != null) {
                if(resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    Glide.with(this).load(imageUri).into(imageView);
                } else {

                }
                break;
        }
    }
}
