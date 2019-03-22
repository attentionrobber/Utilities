package com.hyunseok.android.memo.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 권한 처리를 담당하는 클래스
 * 권한 변경시 PERMISSION_ARR의 값과 Manifest에 추가만 하면 된다.
 * Created by Administrator on 2017-02-10.
 */

public class PermissionControl {

    // 요청할 권한 목록
    public static final String PERMISSION_ARR[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    // 1. 권한 체크
    @TargetApi(Build.VERSION_CODES.M) // Target 지정 Annotation
    public static boolean checkPermission(Activity activity, int req_persmission) {
        // 1.1 런타임 권한 체크
        // 위에 설정한 권한 목록을 반복문을 돌며 처리한다.
        boolean permCheck = true;
        for(String perm : PERMISSION_ARR) {
            if(activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                permCheck = false;
                break;
            }
        }
        if( permCheck ){ // Permission이 모두 true이면 요청 없이 프로그램 실행
            return true;
        } else { // Permission이 있을 경우.
            activity.requestPermissions(PERMISSION_ARR, req_persmission); // 1.3 시스템에 권한 요청.
            return false;
        }
    }

    // 2. 권한 체크 후 콜백처리(사용자가 확인 후 시스템이 호출하는 함수)
    public static boolean onCheckResult(int[] grantResults) {

        boolean checkResult = true;

        // 권한 처리 결과값을 반복문을 돌면서 확인한 후 하나라도 승인되지 않았으면 false를 리턴.
        for(int result : grantResults) {
            if(result != PackageManager.PERMISSION_GRANTED) { // 배열에 넘긴 런타임권한을 체크해서 승인이 된 경우 // 모든 권한 [0] [1] ... 모두 그랜트 되었을 경우.
                checkResult = false;
                break;
            }
        }
        return checkResult;
    }


}
