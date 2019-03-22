package com.example.utilities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.utilities.Util_Class.Logger;
import com.example.utilities.Util_Class.PermissionControl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // 리스너 등록
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 핸드폰의 GPS 센서로 받는 위치(정확도가 더 높음)
                3000, 10, locationListener); // 통지사이의 최소 시간간격(ms), 통지사이의 최소 변경거리(m)

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 통신회사에서 받는 위치
                3000, 10, locationListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 리스너 해제
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                return;
            }
        }
        locationManager.removeUpdates(locationListener);
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
            LatLng myPosition = new LatLng(latitude, longitude); // 위도, 경도
            mMap.addMarker(new MarkerOptions().position(myPosition).title("I'm here")); // 내 위치와 마커 클릭시 나오는 텍스트
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 18)); // 화면을 내 위치로 이동시키는 함수, Zoom Level 설정
        }

        @Override // Provider의 상태 변경시 호출
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override // GPS가 사용할 수 없었다가 사용할 수 있을 때 호출
        public void onProviderEnabled(String provider) {

        }

        @Override // GPS가 사용할 수 없을 때 호출
        public void onProviderDisabled(String provider) {

        }
    };


}
