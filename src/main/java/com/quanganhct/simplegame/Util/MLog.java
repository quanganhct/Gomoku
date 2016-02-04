package com.quanganhct.simplegame.Util;

import android.util.Log;

/**
 * Created by quanganh.nguyen on 1/31/2016.
 */
public class MLog {
    public static void w(String tag, String message) {
        Log.w(tag, "" + message);
    }

    public static void d(String tag, String message) {
        Log.d(tag, "" + message);
    }

    public static void v(String tag, String message) {
        Log.v(tag, "" + message);
    }
}
