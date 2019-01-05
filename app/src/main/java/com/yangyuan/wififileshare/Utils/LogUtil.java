package com.yangyuan.wififileshare.Utils;

import android.util.Log;

import com.yangyuan.wififileshare.config.BuildConfig;

/**
 * Created by yangy on 2017/3/1.
 */
/**
 * Log工具类
 */
public class LogUtil
{
    public static void v(Object o, String message)
    {
        if (BuildConfig.DEBUG)
            Log.v(o.getClass().getSimpleName(), message);
    }

    public static void d(Object o, String message)
    {
        if (BuildConfig.DEBUG)
            Log.d(o.getClass().getSimpleName(), message);
    }

    public static void e(Object o, String message)
    {
        if (BuildConfig.DEBUG)
            Log.e(o.getClass().getSimpleName(), message);
    }

    public static void i(Object o, String message)
    {
        if (BuildConfig.DEBUG)
            Log.i(o.getClass().getSimpleName(), message);
    }

    public static void w(Object o, String message)
    {
        if (BuildConfig.DEBUG)
            Log.w(o.getClass().getSimpleName(), message);
    }


}

