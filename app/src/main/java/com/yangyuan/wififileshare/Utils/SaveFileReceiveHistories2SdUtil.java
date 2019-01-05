package com.yangyuan.wififileshare.Utils;

import android.os.Environment;

import com.google.gson.Gson;
import com.yangyuan.wififileshare.bean.FileReceiveHistory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by yangy on 2017/3/14.
 */
/**
 * 保存文件接收历史到SD的工具类
 */
public class SaveFileReceiveHistories2SdUtil {
    public static void doAction(ArrayList<FileReceiveHistory> fileReceiveHistories){
        Gson gson=new Gson();
        String filePath= Environment.getExternalStorageDirectory().getPath()+"/WifiSharingSaveDir/FileReceiveHistory.db";
        try {
            FileUtil.CreateDirAndFile(filePath);
            FileOutputStream writerStream = new FileOutputStream(filePath);
            BufferedWriter oWriter = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
            oWriter.write(gson.toJson(fileReceiveHistories));
            oWriter.flush();
            oWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
