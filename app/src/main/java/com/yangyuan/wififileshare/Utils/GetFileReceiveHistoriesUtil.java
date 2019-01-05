package com.yangyuan.wififileshare.Utils;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yangyuan.wififileshare.bean.FileReceiveHistory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangy on 2017/3/14.
 */
/**
 * 获取接收文件信息工具类
 */
public class GetFileReceiveHistoriesUtil {
    public static ArrayList<FileReceiveHistory> doAction(){
        ArrayList<FileReceiveHistory> fileReceiveHistories=new ArrayList<>();
        String filePath= Environment.getExternalStorageDirectory().getPath()+"/WifiSharingSaveDir/FileReceiveHistory.db";
        try {
            FileUtil.CreateDirAndFile(filePath);
            File file=new File(filePath);
            BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
            String str =bufferedReader.readLine();
            if(str!=null){
                Gson gson=new Gson();
                fileReceiveHistories=gson.fromJson(str, new TypeToken<List<FileReceiveHistory>>() {
                }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileReceiveHistories;
    }
}
