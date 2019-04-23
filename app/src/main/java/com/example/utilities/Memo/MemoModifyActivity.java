package com.example.utilities.Memo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.utilities.Gallery.GalleryActivity;
import com.example.utilities.MapsActivity;
import com.example.utilities.R;
import com.example.utilities.data.DBHelper;
import com.example.utilities.domain.Memo;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Used by: MemoViewActivity
 * Called by: MemoViewActivity
 */
public class MemoModifyActivity extends AppCompatActivity {

    private static final String TAG = "MemoNewActivity";

    // Widget 관련
    EditText editText_title, editText_content;
    Button btn_OK, btn_cancel;
    ImageButton btn_addImg, btn_location;

    // 권한 관련
    private final int REQ_MYLOCATION = 99; // 내 위치 요청 코드
    private final int REQ_SEARCHLOCATION = 100; // 검색 위치 요청 코드
    private final int REQ_CAMERA = 101; // 카메라 요청 코드
    private final int REQ_GALLERY = 102; // 갤러리 요청 코드

    // 메모 데이터 관련
    List<Memo> memoList = new ArrayList<>();
    int position = 0; // Memo position

    // 이미지 Uri 관련
    Uri imgUri; // 카메라, 갤러리에서 받아오는 이미지의 Uri
    String uri_temp = ""; // DB 에 저장하기 위해 imgUri 를 여러개 저장하는 임시 변수

    // DB 관련
    DBHelper dbHelper;
    Dao<Memo, Integer> memoDao;

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

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = getIntent().getExtras();
            assert bundle != null;
            position = bundle.getInt("position");
        }

        try {
            initDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setMemo(position); // input loaded contents to View. 각 view 에 로드한 내용을 넣는다.
    }

    private void setWidget() {
        btn_OK = findViewById(R.id.btn_OK);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_addImg = findViewById(R.id.btn_addImg);
        btn_location = findViewById(R.id.btn_addLocation);

        editText_title = findViewById(R.id.textView_title);
        editText_content = findViewById(R.id.editText_content);

        // setListener
        btn_OK.setOnClickListener(clickListener);
        btn_cancel.setOnClickListener(clickListener);
        btn_addImg.setOnClickListener(clickListener);
        btn_location.setOnClickListener(clickListener);
    }

    /**
     * DB 초기화, 로드
     */
    private void initDB() throws SQLException {
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        memoDao = dbHelper.getMemoDao();

        memoList = memoDao.queryForAll();
    }

    /**
     * 수정한 메모를 DB 에 저장한다.
     */
    private void updateToDB(Memo memo) throws SQLException {

        String context = editText_content.getText().toString();

        memo.setTitle(editText_title.getText().toString());
        memo.setContent(context);
        memo.setCurrentDate(new Date(System.currentTimeMillis()));

        /*
         * strUri_temp 에 있는 uri 중 이미지를 넣었다 없앤 것을 제거해준다.
         */
        BufferedReader br = new BufferedReader(new StringReader(uri_temp));
        String line;
        String uri = "";
        try {
            while ( (line = br.readLine()) != null) { // BufferedReader 를 이용해 strUri_temp 를 한줄씩 읽는다.
                if (context.contains(line)) { // 메모 내용에 한줄씩 읽은 uri 가 포함된 경우에만
                    uri = uri.concat(line).concat("\n"); // String uri 에 추가한다.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        memo.setImgUri(uri);

        memoDao.update(memo);
    }

    /**
     * 각 view 에 내용을 세팅한다.
     */
    private void setMemo(int position) {
        // position 으로 Memo data 를 가져온다.
        String title = memoList.get(position).getTitle();
        String context = memoList.get(position).getContent();
        uri_temp = memoList.get(position).getImgUri();

        // 가져온 Memo data 를 widget 에 뿌려준다.
        editText_title.setText(title);
        editText_content.setText(setEditTextWithImage(context, uri_temp)); // ImageSpan 을 이용해 uri text 를 이미지로 표시한다.
   }

    private CharSequence setEditTextWithImage(String context, String strUri) {
        SpannableStringBuilder builder = new SpannableStringBuilder(); //
        builder.append(context);

        if (!strUri.equals("")) {
            try {
                BufferedReader br = new BufferedReader(new StringReader(strUri));
                String line;
                while ( (line = br.readLine()) != null ) {
                    int start = context.indexOf(line);
                    ImageSpan imageSpan = new ImageSpan(this, Uri.parse(line));
                    builder.setSpan(imageSpan, start, start + line.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder;
    }

    /**
     * Listener 계열
     */
    View.OnClickListener clickListener = v -> {
        switch (v.getId()) {
            case R.id.btn_OK:
                try {
                    updateToDB(memoList.get(position));
                    setResult(RESULT_OK, new Intent()); // MemoViewActivity 로 결과를 콜백한다.
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.btn_cancel:
                hideKeypad();
                // 제목이나 내용을 작성했을 경우에만 AlertDialog 나타나게함. // TODO: 작성 -> 수정으로
                if( !(editText_title.getText().toString().equals("")) || !(editText_content.getText().toString().equals(""))) {
                    AlertDialog.Builder alert_cancel = new AlertDialog.Builder(MemoModifyActivity.this);
                    alert_cancel.setTitle("CANCEL MODIFY A NOTE");
                    alert_cancel.setMessage("Exit without saving.");
                    alert_cancel.setPositiveButton("OK", (dialog, which) -> MemoModifyActivity.super.onBackPressed());
                    alert_cancel.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    alert_cancel.show();
                } else {
                    MemoModifyActivity.super.onBackPressed();
                }
                break;
            case R.id.btn_addImg:
                hideKeypad();
                alertAddImage();
                break;
            case R.id.btn_addLocation:
                alertAddLocation();
                break;
        }
    };

    /**
     * 이미지 추가 버튼 눌렀을때
     * 카메라, 갤러리 둘 중 선택하는 AlertDialog 띄우는 함수
     */
    private void alertAddImage() {
        AlertDialog.Builder alert_AddImg = new AlertDialog.Builder(MemoModifyActivity.this);// 1. Dialog 만들기

        final DialogItem[] items = {
                new DialogItem("CAMERA", android.R.drawable.ic_menu_camera),
                new DialogItem("GALLERY", android.R.drawable.ic_menu_gallery)
        };
//        List<DialogItem> items = new ArrayList<>();
//        items.add(new DialogItem("CAMERA", android.R.drawable.ic_menu_camera));
//        items.add(new DialogItem("GALLERY", android.R.drawable.ic_menu_gallery));

//        // TODO: Item Horizontal 으로 수정
//        ListAdapter dialogAdapter = new ArrayAdapter<DialogItem>(this, R.layout.item_alert_dialog, R.id.tv_alertDialog, items){
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//
//                TextView textView = view.findViewById(R.id.tv_alertDialog);
//                textView.setText(items[position].text);
//                textView.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);
//
//                //Add margin between image and text (support various screen densities)
////                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
////                textView.setCompoundDrawablePadding(dp5);
//
//                return view;
//            }
//        };
//
//        alert_AddImg.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                Intent intent;
//                switch (item) {
//                    case 0: // Camera
//                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        // 카메라 촬영 후 미디어 컨텐트 Uri 를 생성해서 외부저장소에 저장한다.
//                        // 마시멜로 이상 버전은 아래 코드를 반영해야함.
//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                            ContentValues values = new ContentValues(1);
//                            values.put(MediaStore.Images.Media.MIME_TYPE, "memo/jpg");
//                            imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
//                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                            // 컨텐트 Uri 강제 세팅
//                        }
//                        startActivityForResult(intent, REQ_CAMERA);
//                        break;
//                    case 1: // Gallery
//                        intent = new Intent(MemoModifyActivity.this, GalleryActivity.class);
//                        intent.putExtra("REQ_CODE", REQ_GALLERY);
//                        startActivityForResult(intent, REQ_GALLERY);
//                        break;
//                }
//            }
//        });


        View view = LayoutInflater.from(this).inflate(R.layout.layout_alert_dialog, null);
        HorizontalListView listView = view.findViewById(R.id.view_alertDialog); // CustomView

        ArrayAdapter dialogAdapter = new ArrayAdapter<DialogItem>(this, R.layout.item_alert_dialog, R.id.tv_alertDialog, items) {
            @NonNull
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageView imageView = view.findViewById(R.id.iv_alertDialog);
                TextView textView = view.findViewById(R.id.tv_alertDialog);

                imageView.setImageResource(items[position].icon);
                textView.setText(items[position].text);

//                textView.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0); // TextView 에 이미지 추가하는 방법
//                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f); // TextView 와 추가한 이미지에 margin
//                textView.setCompoundDrawablePadding(dp5);
                view.setOnClickListener(v -> {
                    Intent intent;
                    switch (position) {
                        case 0: // Camera
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 카메라 촬영 후 미디어 컨텐트 Uri 를 생성해서 외부저장소에 저장한다.
                        // 마시멜로 이상 버전은 아래 코드를 반영해야함.
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            ContentValues values = new ContentValues(1);
                            values.put(MediaStore.Images.Media.MIME_TYPE, "memo/jpg");
                            imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            // 컨텐트 Uri 강제 세팅
                        }
                        startActivityForResult(intent, REQ_CAMERA);
                        break;

                        case 1: // Gallery
                        intent = new Intent(MemoModifyActivity.this, GalleryActivity.class);
                        intent.putExtra("REQ_CODE", REQ_GALLERY);
                        startActivityForResult(intent, REQ_GALLERY);
                        break;
                    }
                });
                return view;
            }
        };
        listView.setAdapter(dialogAdapter);

        alert_AddImg.setTitle("INPUT IMAGE")
                .setView(view)
                .create()
                .show(); // 팝업창을 띄운다.
    }

    /**
     * 위치 추가 버튼
     * 현재 위치 추가할지, 장소 검색 후 위치 추가할지 선택하는 AlertDialog
     */
    private void alertAddLocation() {
        AlertDialog.Builder alertLocation = new AlertDialog.Builder(MemoModifyActivity.this);
        alertLocation.setTitle("Location Option");
        final CharSequence[] items_Location = {"Current Location", "Search Location"};
        alertLocation.setItems(items_Location, (dialog, which) -> {
            switch (which) {
                case 0 : // Current Location 선택시 구글맵으로 현재 위치를 띄워준다.
                    Intent intent = new Intent(MemoModifyActivity.this, MapsActivity.class);
                    intent.putExtra("case", 0);
                    startActivityForResult(intent, REQ_MYLOCATION);
                    break;
                case 1 : // Search Location 선택시 구글맵으로 검색할 수 있게 한다.
//                    searchByPlacePicker();
//                        checkPermission(REQ_PLACE_PICKER);
//                        if(PERM_RESULT == PERM_GRANT) {
//
//                        }
//                        intent = new Intent(MemoNewActivity.this, MapsActivity.class);
//                        intent.putExtra("case", 1);
//                        startActivityForResult(intent, REQ_LOCATION);
                    break;
            }
        });
        alertLocation.show(); // show함수로 팝업창을 띄운다.
    }

    // 키패드(키보드) 띄우기
    private void displayKeypad() {
        InputMethodManager imm;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    // 키패드(키보드) 없애기
    private void hideKeypad() {
        InputMethodManager imm = (InputMethodManager) getSystemService(MemoNewActivity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    //startActivityForResult() 후에 실행되는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 마시멜로버전 이상인 경우에만 getData()에 null 이 넘어올것임.
                        if (data != null && data.getData() != null)
                            imgUri = data.getData();
                    }
                    if (imgUri != null)
                        inputImageToEditText(editText_content, imgUri.toString());
                    imgUri = null;
                }
                break;

            case REQ_GALLERY:
                if (resultCode == RESULT_OK) {
                    imgUri = data.getData();
                    if (imgUri != null)
                        inputImageToEditText(editText_content, imgUri.toString());
                    imgUri = null;
                }
                break;

            case REQ_MYLOCATION:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        double latitude = bundle.getDouble("latitude");
                        double longitude = bundle.getDouble("longitude");
                        String url = "http://maps.google.com/?q="; // 구글맵 기본 url
                        String locationUrl = url + latitude + "," + longitude;
                        editText_content.append(locationUrl);
                    }
                }
                break;
        }
    }

    /**
     * ImageSpan 과 SpannableStringBuilder 를 이용해
     * EditText 안에 Uri 텍스트를 이미지로 추가
     */
    private void inputImageToEditText(EditText editText, String strUri) {
        //String strUri = uri.toString();
        ImageSpan imageSpan = new ImageSpan(this, Uri.parse(strUri));

        int start = editText.getSelectionStart(); // 커서 시작 위치
        int end = editText.getSelectionEnd(); // 커서 마지막 위치

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(editText.getText()); // builder 에 editText 의 내용을 붙인다.
        builder.replace(start, end, strUri); // 커서의 시작 위치부터 마지막 위치까지 strUri 로 대체된다.
        builder.setSpan(imageSpan, start, start + strUri.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        uri_temp = uri_temp.concat(strUri).concat("\n"); // 이미지 uri 를 임시로 추가한다.
        //Log.i("TESTS","// "+start+" // "+end+" // "+strUri);

        editText.setText(builder.append(" ")); // 공백이 없을경우 이미지 뒤에 커서를 위치했을때 커서 위치가 제대로 잡히지않았음
        editText.requestFocus();
        editText.setFocusableInTouchMode(true);
        editText.setSelection(end+strUri.length()+1);
        displayKeypad();
    }

    /**
     * 백버튼 입력시
     */
    @Override
    public void onBackPressed() {
        // 제목이나 내용이 입력됐을 경우
        if( !(editText_title.getText().toString().equals("")) || !(editText_content.getText().toString().equals(""))) {
            AlertDialog.Builder alert_cancel = new AlertDialog.Builder(MemoModifyActivity.this);
            alert_cancel.setTitle("CANCEL MODIFY A NOTE");
            alert_cancel.setMessage("Exit without saving.");
            alert_cancel.setPositiveButton("OK", (dialog, which) -> MemoModifyActivity.super.onBackPressed());
            alert_cancel.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            alert_cancel.show();
        }
        else super.onBackPressed();
    }
}
