package com.yangyuan.wififileshare.Utils;

/**
 * Created by yangy on 2017/3/5.
 */
/**
 * 根据路径得到名字工具类
 */
public class GetnameByPathUtil {
    public static String getName(String path){
        return path.substring(path.lastIndexOf("/")+1);
    }
}
