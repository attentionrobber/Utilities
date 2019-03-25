package com.example.utilities.Util_Class;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

/**
 * GPS, Bluetooth 등 센서가 켜져있는지 확인 후 알림창 띄워줌
 * Created by KHS on 2019-03-22.
 */
public class SensorControl {

//    // 센서 목록
//    public static final String SENSOR_ARR[] = {
//            LocationManager.GPS_PROVIDER
//    };
//
//    // GPS 센서가 켜져있는지 체크 롤리팝 이하버전
//    private boolean gpsCheck(Activity activity) {
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } else {
//            String gps = android.provider.Settings.Secure.getString(getContentResolver(),
//                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//            if(gps.matches(",*gps.*")) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }
}
