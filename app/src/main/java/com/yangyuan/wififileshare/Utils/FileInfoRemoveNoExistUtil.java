package com.yangyuan.wififileshare.Utils;

import com.yangyuan.wififileshare.bean.FileType;
import com.yangyuan.wififileshare.bean.ServiceFileInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yangy on 2017/3/18.
 */
/**
 * 移除分享信息中文件不存在的信息的工具类
 */
public class FileInfoRemoveNoExistUtil {
    public static void DoAction(){
        ArrayList<ServiceFileInfo> serviceFileInfos=GetServiceFileInfosFromSdUtil.doAction();
        for (int i=0;i<serviceFileInfos.size();i++){
            try {
                if (serviceFileInfos.get(i).getFileType()!= FileType.app){
                    File file=new File(serviceFileInfos.get(i).getFilepath());
                    if (file==null||!file.exists()){
                        serviceFileInfos.remove(i);
                    }
                }
            } catch (Exception e) {
                serviceFileInfos.remove(i);
            }
        }
        SaveFileInfo2SdUtil.save(serviceFileInfos);
    }
}
