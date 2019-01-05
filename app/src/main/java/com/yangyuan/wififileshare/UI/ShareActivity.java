package com.yangyuan.wififileshare.UI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yangyuan.wififileshare.Base.BaseActivity;
import com.yangyuan.wififileshare.BaseApplication;
import com.yangyuan.wififileshare.Trans.ServerService;
import com.yangyuan.wififileshare.UI.serviceFragment.WaittingReciveFragment;
import com.yangyuan.wififileshare.bean.FileType;
import com.yangyuan.wififileshare.bean.Range;
import com.yangyuan.wififileshare.bean.SendStatus;
import com.yangyuan.wififileshare.config.AppConfig;
import com.yangyuan.wififileshare.sendReciver.RecvingPrepareStateChangReciver;
import com.yangyuan.wififileshare.sendReciver.SendStateChangedReciver;
import com.yangyuan.wififileshare.wifUtils.MobileDataHelper;

/**
 * Created by yangy on 2017/3/1.
 */
/**
 * 开启分享界面
 */
public class ShareActivity extends BaseActivity implements RecvingPrepareStateChangReciver.OnRecivePrepareStateChangedListener, SendStateChangedReciver.OnAllTasksStartListener, SendStateChangedReciver.OnSendStateChangedListener, SendStateChangedReciver.OnBeginTranListener
{

    private boolean isBind = false;
    private ServerService.ReceiveActionBinder binder;
    private ServiceConnection serviceConnection;
    private RecvingPrepareStateChangReciver recvingPrepareStateChangReciver = new RecvingPrepareStateChangReciver();
    private SendStateChangedReciver sendStateChangedReciver = new SendStateChangedReciver();
    private FragmentManager fragmentManager;
    private WaittingReciveFragment waittingReciveFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MobileDataHelper.setMobileData(false);
        serviceConnection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder binder)
            {
                ShareActivity.this.binder = ((ServerService.ReceiveActionBinder) binder);
                recvingPrepareStateChangReciver.registerSelf();
                ShareActivity.this.binder.prepareRecive(AppConfig.photoId, AppConfig.userName);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName)
            {
                binder.onlyCloseAP();
            }
        };
        isBind = BaseApplication.getInstance().bindService(new Intent(BaseApplication.getInstance(), ServerService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        recvingPrepareStateChangReciver.setOnRecivePrepareStateChangedListener(this);
        sendStateChangedReciver.setOnBeginTranListener(this);
        sendStateChangedReciver.setOnSendStateChangedListener(this);
        sendStateChangedReciver.setOnAllTasksStartListener(this);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction =fragmentManager.beginTransaction();
        waittingReciveFragment = new WaittingReciveFragment();
        transaction.replace(android.R.id.content, waittingReciveFragment);
        transaction.commit();
    }



    @Override
    public void onRecivePrepareStateChanged(int state) {
        if(waittingReciveFragment != null)
            waittingReciveFragment.onRecivePrepareStateChanged(state);
        switch (state)
        {
            case RecvingPrepareStateChangReciver.STATE_ERROR:
                recvingPrepareStateChangReciver.unRegisterSelf();
                break;
            case RecvingPrepareStateChangReciver.STATE_FINISH:
                recvingPrepareStateChangReciver.unRegisterSelf();
                sendStateChangedReciver.registerSelf();
                break;
            case RecvingPrepareStateChangReciver.STATE_PREPARING:
                break;
        }
    }

    @Override
    public void onAllTasksStart() {

    }

    @Override
    public void onBeginTranListener(String uuid, String filePath, String fileDesc, Range range, FileType type) {

    }

    @Override
    public void onSendStateChanged(String uuid, SendStatus state, float percent) {

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        try {
            binder.onlyCloseAP();
        }catch (Exception e){

        }
        recvingPrepareStateChangReciver.unRegisterSelf();
        if (isBind)
        {
            BaseApplication.getInstance().unbindService(serviceConnection);
            isBind = false;
        }
        super.onDestroy();
    }
}
