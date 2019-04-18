package com.example.utilities.Memo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.utilities.MapsActivity;
import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;
import com.example.utilities.Util_Class.PermissionControl;
import com.example.utilities.data.DBHelper;
import com.example.utilities.domain.Memo;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;

/**
 * Used by: MemoActivity
 * Called by: MemoActivity
 */
public class MemoNewActivity extends AppCompatActivity {

    private static final String TAG = "MemoNewActivity";
    //private boolean keypad_toggle = false;

    // Widget 관련
    //Button btn_OK, btn_cancel;
    //ImageButton imgbtn_addimg, imgbtn_location;
    EditText editText_title, editText_content;
    ImageView imageView;

    // 권한 관련
    private final int PERM_GRANT = 1;
    private final int PERM_DENY = 2;
    private int PERM_RESULT = 0;
    private final int REQ_PERMISSION = 1; // 요청 코드
    private final int REQ_PLACE_PICKER = 98; // PlacePicker(장소 선택) 요청 코드
    private final int REQ_LOCATION = 99; // 내 위치 요청 코드
    private final int REQ_CAMERA = 101; // 카메라 요청 코드
    private final int REQ_GALLERY = 102; // 갤러리 요청 코드

    // 이미지 Uri 관련
    Uri imageUri;
    String strUri = "";

    // Location 관련
    Intent intent; // 지도 인텐트
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_new);

        displayKeypad();// 키패드(키보드) 자동으로 띄우기
        setWidget();
        //checkPermission();
    }

    private Memo makeMemo() {
        Memo memo = new Memo();

        memo.setTitle(editText_title.getText().toString());
        memo.setContent(editText_content.getText().toString());
        memo.setCurrentDate(new Date(System.currentTimeMillis()));

        if(imageUri != null) strUri = imageUri.toString();
        else strUri = "";
        memo.setImgUri(strUri);

        return memo;
    }

    public void saveToDB(Memo memo) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        Dao<Memo, Integer> memoDao = dbHelper.getMemoDao();
        memoDao.create(memo);
    }

    private void setWidget() {
        editText_title = findViewById(R.id.textView_title);
        editText_content = findViewById(R.id.editText_content);

        findViewById(R.id.btn_OK).setOnClickListener(this::clickListener);
        findViewById(R.id.btn_cancel).setOnClickListener(this::clickListener);
        findViewById(R.id.btn_addImg).setOnClickListener(this::clickListener);
        findViewById(R.id.btn_addLocation).setOnClickListener(this::clickListener);
        findViewById(R.id.imageView).setOnClickListener(this::clickListener);
    }

    /**
     * 이미지 추가 버튼 눌렀을때
     * 카메라, 갤러리 둘 중 선택하는 AlertDialog 띄우는 함수
     */
    private void alertAddImage() {
        AlertDialog.Builder alert_AddImg = new AlertDialog.Builder(MemoNewActivity.this);// 1. Dialog 만들기
        alert_AddImg.setTitle("Input Image"); // 2. Dialog 제목
        final CharSequence[] items_AddImg = {"Camera", "Gallery"}; // 3. Dialog Items 만들기
        alert_AddImg.setItems(items_AddImg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                switch (which) {
                    case 0 : // Camera
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 카메라 촬영 후 미디어 컨텐트 Uri 를 생성해서 외부저장소에 저장한다.
                        // 마시멜로 이상 버전은 아래 코드를 반영해야함.
                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            ContentValues values = new ContentValues(1);
                            values.put(MediaStore.Images.Media.MIME_TYPE, "memo/jpg");
                            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            // 컨텐트 Uri 강제 세팅
                        }
                        startActivityForResult(intent, REQ_CAMERA);
                        break;
                    case 1 : // Gallery
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*"); // 외부저장소에 있는 이미지만 가져오기위한 필터링.
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_GALLERY); // createChooser 으로 타이틀을 붙여줄 수 있다.
                        break;
                }
            }
        });
        alert_AddImg.show(); // 4. 팝업창을 띄운다.
    }

    /**
     * 위치 추가 버튼
     * 현재 위치 추가할지, 장소 검색 후 위치 추가할지 선택하는 AlertDialog
     */
    private void alertDialogLocation() {
        AlertDialog.Builder alertLocation = new AlertDialog.Builder(MemoNewActivity.this);
        alertLocation.setTitle("Location Option");
        final CharSequence[] items_Location = {"Current Location", "Search Location"};
        alertLocation.setItems(items_Location, (dialog, which) -> {
            switch (which) {
                case 0 : // Current Location 선택시 구글맵으로 현재 위치를 띄워준다.
                    intent = new Intent(MemoNewActivity.this, MapsActivity.class);
                    intent.putExtra("case", 0);
                    startActivityForResult(intent, REQ_LOCATION);
                    break;
                case 1 : // Search Location 선택시 구글맵으로 검색할 수 있게 한다.
                    // TODO 권한 요청하기
                    searchByPlacePicker();
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

    private void searchByPlacePicker(){
//        PlacePicker.IntentBuilder placepicker = new PlacePicker.IntentBuilder();
//
//        try {
//            startActivityForResult(placepicker.build(this), REQ_PLACE_PICKER);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * ImageView 클릭시 이미지 바꿀지 삭제할지 AlertDialog 띄우는 함수
     */
    private void alertClickImageView() {
        if (imageUri != null) { // 이미지가 삽입되었을때만 작동
            AlertDialog.Builder alertImageView = new AlertDialog.Builder(MemoNewActivity.this);
            alertImageView.setTitle("Image Option");
            final CharSequence[] items_ImageView = {"Change", "Delete"};
            alertImageView.setItems(items_ImageView, (dialog, which) -> {
                switch (which) {
                    case 0: // Image Change
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*"); // 외부저장소에 있는 이미지만 가져오기위한 필터링.
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_GALLERY); // createChooser로 타이틀을 붙여줄 수 있다.
                        break;
                    case 1: // Image Delete
                        imageView.setImageResource(0);
                        imageUri = null;
                        break;
                }
            });
            alertImageView.show(); // 팝업창을 띄운다.
        }
    }

    /**
     * EditText 안에 이미지를 글자처럼 추가
     */
    private void inputImageInsideEditText(String strUri) {

        ImageSpan imageSpan = new ImageSpan(this, Uri.parse(strUri));

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(editText_content.getText());

        int selStart = editText_content.getSelectionStart();

        // current selection is replace with imageId
        builder.replace(editText_content.getSelectionStart(), editText_content.getSelectionEnd(), strUri);

        // This adds a span to display image where the imageId is. If you do builder.toString() - the string will contain imageId where the imageSpan is.
        // you can use it later - if you want to find location of imageSpan in text;
        builder.setSpan(imageSpan, selStart, selStart + strUri.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText_content.setText(builder);
    }

    /**
     * ClickListener 함수화
     */
    private void clickListener(View v) {
        switch (v.getId()) {
            case R.id.btn_OK :
                hideKeypad();
                try {
                    Memo memo = makeMemo();
                    saveToDB(memo);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.btn_cancel :
                hideKeypad();
                // 제목이나 내용을 작성했을 경우에만 AlertDialog 나타나게함.
                if( !(editText_title.getText().toString().equals("")) || !(editText_content.getText().toString().equals(""))) {
                    AlertDialog.Builder alert_cancel = new AlertDialog.Builder(MemoNewActivity.this);
                    alert_cancel.setTitle("CANCEL WRITING A NOTE");
                    alert_cancel.setMessage("Exit without saving.");
                    alert_cancel.setPositiveButton("OK", (dialog, which) -> MemoNewActivity.super.onBackPressed());
                    alert_cancel.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    alert_cancel.show();
                } else {
                    MemoNewActivity.super.onBackPressed();
                }
                break;
            case R.id.btn_addImg : // Add Image 버튼 클릭시
                hideKeypad();
                alertAddImage();
                break;
            case R.id.btn_addLocation: // Location 버튼 클릭시
                hideKeypad();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PermissionControl.checkPermission(MemoNewActivity.this, REQ_LOCATION);
                } else {
                    alertDialogLocation(); // 내 위치 or 지도 검색할지 선택하는 alert
                }
                break;
            case R.id.imageView : // ImageView 클릭시
                hideKeypad();
                alertClickImageView();
                break;
        }
    }

    private void checkPermission(int permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 안드로이드 버전 체크
            if (PermissionControl.checkPermission(this, permission)) { // 권한 체크
                //init();
                PERM_RESULT = PERM_GRANT;
                Logger.print("111115", "ssibal");
            }
        } else {
            //init(); // 프로그램 실행
            PERM_RESULT = PERM_GRANT;
            Logger.print("222225", "ssibal");
        }
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


    /**
     * startActivityForResult() 후에 실행되는 콜백 함수
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.print("requestCode==========================="+requestCode,"MemoNewActivity");

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
                    }
                } else { // resultCode가 uri가 남아있는데 삭제처리해야함.
                    //imageUri = null;
                }
                break;
            case REQ_GALLERY:
                //if(data != null && data.getData() != null) {
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    inputImageInsideEditText(String.valueOf(imageUri)); // EditText 안에 이미지를 글자처럼 추가
                }
                break;
            case REQ_LOCATION: // 내 위치
                if (resultCode == RESULT_OK) {
                    bundle = data.getExtras();
                    if (bundle != null) {
                        double latitude = bundle.getDouble("latitude");
                        double longitude = bundle.getDouble("longitude");
                        String url = "http://maps.google.com/?q="; // 구글맵 기본 url
                        String locationUrl = url + latitude + "," + longitude;
                        editText_content.append(locationUrl);
                    }
                }
                break;
            case REQ_PLACE_PICKER:
                if (resultCode == RESULT_OK) {
//                    Place place = PlacePicker.getPlace(data, this);
//                    String address = String.valueOf(place.getAddress()); // 선택한 place의 주소
//                    String name = String.valueOf(place.getName()); // 선택한 place의 검색시 타이틀
//                    String url = "http://maps.google.com/?q="; // 구글맵 기본 url
//                    String latlngStr = String.valueOf(place.getLatLng()); // Latlng을 String으로 변환함
//                    String split[] = latlngStr.split(",");
//                    String lat = split[0].replaceAll("[^0-9|.]", ""); // 숫자와 .(dot)을 제외한 문자를 모두 없앤다.
//                    String lng = split[1].replaceAll("[^0-9|.]", "");
//                    editText_content.append("\n" + address+ " " + name);
//                    editText_content.append("\n" + url + lat + "," + lng);
                }
                break;
        }
    }

    /**
     * 권한 체크 후 콜백처리(사용자가 확인 후 시스템이 호출하는 함수)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_PLACE_PICKER) {
            if( PermissionControl.onCheckResult(grantResults) ) { // 권한이 GRANTED 될 경우
                //init(); // 프로그램 실행
                PERM_RESULT = PERM_GRANT;
                Logger.print("333335", "ssibal");
            } else {
                Toast.makeText(this, "권한을 실행하지 않으면 프로그램이 실행되지 않습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
//        else if(requestCode == REQ_CAMERA) {
//        }
    }

    /**
     * 백버튼 입력시
     */
    @Override
    public void onBackPressed() {
        // 제목이나 내용이 입력됐을 경우
        if( !(editText_title.getText().toString().equals("")) || !(editText_content.getText().toString().equals(""))) {
            AlertDialog.Builder alert_cancel = new AlertDialog.Builder(MemoNewActivity.this);
            alert_cancel.setTitle("CANCEL WRITING A NOTE");
            alert_cancel.setMessage("Exit without saving.");
            alert_cancel.setPositiveButton("OK", (dialog, which) -> MemoNewActivity.super.onBackPressed());
            alert_cancel.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            alert_cancel.show();
        }
        else super.onBackPressed();
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
