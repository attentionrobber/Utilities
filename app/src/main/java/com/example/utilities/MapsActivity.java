package com.example.utilities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.utilities.Util_Class.LoadingDialog;
import com.example.utilities.Util_Class.Logger;
import com.example.utilities.Util_Class.PermissionControl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * GPS 를 이용해 자신의 위치를 찾아준다.
 * Used by: MainActivity, MemoNewActivity, MemoModifyActivity
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Dialog dialog;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    LocationManager locationManager;

    LatLng myPosition; // 내 위치

    private final int REQ_PLACEPICKER = 98; // PlacePicker(장소 선택) 요청 코드
    private final int REQ_LOCATION = 99; // 내 위치 요청 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 어떤 옵션으로 지도서비스를 이용하는지 받아온다.
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //option = bundle.getInt("case"); // case0: My Location, case1: Search Location
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        alertGPS(); // GPS 센서가 꺼져있으면 알림 띄워주는 함수

        // 디폴트 지도 위치 설정 : 서울 신사역 37.516066 127.019361
        LatLng sinsa = new LatLng(37.516066, 127.019361);
        mMap.addMarker(new MarkerOptions().position(sinsa).title("Seoul in Sinsa"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sinsa, 15));

        // Set loading Dialog
//        dialog = new Dialog(this);
//        dialog.setTitle("Loading...");
//        dialog.show();
        LoadingDialog.setProgressDialog(this);



        searchMyLocation(); // 현재 위치 찾기 리스너 등록
        //searchByPlacePicker();
    }


    /**
     * locationListener로 현재 위치 찾는 함수
     */
    private void searchMyLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionControl.checkPermission(MapsActivity.this, REQ_LOCATION);
            Logger.print("if","ErrorCheck");
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 핸드폰의 GPS 센서로 받는 위치(정확도가 더 높음)
                3000, 10, locationListener); // 통지사이의 최소 시간간격(ms), 통지사이의 최소 변경거리(m)

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 통신회사에서 받는 위치
                3000, 10, locationListener);
        Logger.print("MyLocation","ErrorCheck");
    }

    /**
     * 자신의 위치를 찾는 Location 리스너
     */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude(); // 경도
            double latitude = location.getLatitude();   // 위도
            //double altitude = location.getAltitude();   // 고도
            //float accuracy = location.getAccuracy();    // 정확도
            //String provider = location.getProvider();   // 위치제공자

            // 내 위치
            myPosition = new LatLng(latitude, longitude); // 위도, 경도
            mMap.addMarker(new MarkerOptions().position(myPosition).title("I'm here")); // 내 위치와 마커 클릭시 나오는 텍스트
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 18)); // 화면을 내 위치로 이동시키는 함수, Zoom Level 설정

            Intent intent = new Intent();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            setResult(RESULT_OK, intent);
            LoadingDialog.dismissDialog();
            //dialog.dismiss();
            Log.i("LOCATE", ""+longitude+" // "+latitude);
        }

        @Override // Provider 의 상태 변경시 호출
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override // GPS 가 사용할 수 없었다가 사용할 수 있을 때 호출
        public void onProviderEnabled(String provider) {

        }

        @Override // GPS 가 사용할 수 없을 때 호출
        public void onProviderDisabled(String provider) {

        }
    };

    // GPS 센서가 켜져있는지 체크 롤리팝 이하버전
    private boolean gpsCheck() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
            String gps = android.provider.Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return gps.matches(",*gps.*");
        }
    }
    // GPS 센서가 꺼져있을 때 alertDialog 으로 GPS 켤지 묻기
    private void alertGPS() {
        // GPS 센서가 켜져있는지 확인
        // 꺼져있다면 GPS를 켜는 페이지로 이동.
        if( !gpsCheck() ) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this); // 0. 팝업창 만들기
            alertDialog.setTitle("GPS 켜기"); // 1. 팝업창 제목
            alertDialog.setMessage("GPS가 꺼져 있습니다.\n설정창으로 이동하시겠습니까?"); // 2. 팝업창 메시지
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // 3. Yes 버튼 만들기
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // 4. Cancel 버튼 만들기
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show(); // 5. 팝업창을 띄운다.
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 리스너 해제
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionControl.checkPermission(MapsActivity.this, REQ_LOCATION);
        }
        locationManager.removeUpdates(locationListener);
    }
}
