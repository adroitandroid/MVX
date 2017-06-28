package com.adroitandroid.mvx;

/**
 * Created by pv on 28/06/17.
 */

class Log {
    private static boolean isDebugMode = false;

    static void log(String message) {
        if (isDebugMode) {
            android.util.Log.d("MVX", message);
        }
    }
}
