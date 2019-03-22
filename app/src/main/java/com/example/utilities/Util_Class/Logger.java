package com.hyunseok.android.memo.utility;

import android.util.Log;

/**
 * @author HS
 * @version 1.0
 * @since 2017-02-02
 */

public class Logger {

    public final static boolean DEBUG_MODE = true; // ture이면 디버그 false이면 릴리즈버전 BuildConfig.DEBUG

    /**
     * 로그내용을 콘솔에 출력
     * @param str
     * @param className
     */
    public static void print(String str, String className) {
        if (DEBUG_MODE) {
            Log.d(className, str);

            // TODO 로그내용을 로그파일에 저장.
            // File.append...()
        }
    }
}
