package com.yangyuan.wififileshare.Trans;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;

import com.yangyuan.wififileshare.Utils.ApNameUtil;
import com.yangyuan.wififileshare.Utils.ThreadPool;
import com.yangyuan.wififileshare.bean.ApNameInfo;
import com.yangyuan.wififileshare.reciver.ApStateBroadcastReciver;
import com.yangyuan.wififileshare.reciver.WifiStateBroadcastReciver;
import com.yangyuan.wififileshare.sendReciver.RecvingPrepareStateChangReciver;
import com.yangyuan.wififileshare.wifUtils.MobileDataHelper;
import com.yangyuan.wififileshare.wifUtils.WifiApHelper;
import com.yangyuan.wififileshare.wifUtils.WifiHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangy on 2017/3/1.
 */
/**
 * 热点相关的服务
 */
public class ServerService extends Service implements WifiStateBroadcastReciver.OnWIfiStateChangedListener, ApStateBroadcastReciver.OnApStateChangListener
{
    private ReceiveActionBinder binder = null;
    private final static int POOL_SIZE = 4;
    private WifiApHelper apHelper;
    private WifiHelper wifiHelper;
    private ApStateBroadcastReciver apStateBroadcastReciver;
    private WifiStateBroadcastReciver wifiStateBroadcastReciver;
    private String ssid;
    private WifiConfiguration apConfig = null;
    private ThreadPool threadPool;
    @Override
    public void onCreate()
    {
        super.onCreate();
        RecvingPrepareStateChangReciver.sendBroadcast(RecvingPrepareStateChangReciver.STATE_FINISH);
        wifiHelper = new WifiHelper();
        apHelper = new WifiApHelper(wifiHelper);
        binder = new ReceiveActionBinder();
        apStateBroadcastReciver = new ApStateBroadcastReciver();
        wifiStateBroadcastReciver = new WifiStateBroadcastReciver();

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    @Override
    public void onWifiStateChanged(int wifiSatate)
    {
        if (wifiSatate == WifiManager.WIFI_STATE_DISABLED)
        {
            wifiStateBroadcastReciver.unRegisterSelf();

            WifiConfiguration config = apHelper.openWifiAp(ssid, apConfig == null);
            if(config != null)
                apConfig = config;
        }
    }

    @Override
    public void onApClosing()
    {

    }

    @Override
    public void onApOpening()
    {

    }

    @Override
    public void onApOpened()
    {
        RecvingPrepareStateChangReciver.sendBroadcast(RecvingPrepareStateChangReciver.STATE_FINISH);
        apStateBroadcastReciver.setApStateChangListener(null);
        apStateBroadcastReciver.unRegisterSelf();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(12);
        fixedThreadPool.execute(new StartServiceSocket(fixedThreadPool));
    }

    @Override
    public void onApClosed()
    {
        wifiStateBroadcastReciver.unRegisterSelf();
        WifiConfiguration config = apHelper.openWifiAp(ssid, apConfig == null);
        if(config != null)
            apConfig = config;
    }

    public class ReceiveActionBinder extends Binder
    {
        public void prepareRecive(int photoId, String name)
        {
            RecvingPrepareStateChangReciver.sendBroadcast(RecvingPrepareStateChangReciver.STATE_PREPARING);
            ssid = ApNameUtil.encodeApName(new ApNameInfo(name, photoId));
            apStateBroadcastReciver.setApStateChangListener(ServerService.this);
            apStateBroadcastReciver.registerSelf();
            if (MobileDataHelper.getMobileDataState())
            {
               /* MobileDataHelper.setMobileData(false);*/
                MobileDataHelper.toggleMobileData(false);
            }
            if ((!apHelper.isApEnabled()) && (!wifiHelper.isWifiEnabled()))
            {
                WifiConfiguration config = apHelper.openWifiAp(ssid, apConfig == null);
                if(config != null)
                    apConfig = config;
                return;
            }

            wifiStateBroadcastReciver.setOnWIfiStateChangedListener(ServerService.this);
            wifiStateBroadcastReciver.registerSelf();
            apHelper.closeWifiAp(apConfig);
            wifiHelper.setWifiEnabled(false);
        }

        public void onlyCloseAP()
        {
            try{
                threadPool.closed();
            }catch (Exception e){

            }
            apHelper.closeWifiAp(apConfig);
        }
        public void stopReceiveService()
        {
            try{
                threadPool.closed();
            }catch (Exception e){

            }
        }
    }
}
