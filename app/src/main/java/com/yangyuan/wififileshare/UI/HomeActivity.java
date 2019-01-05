package com.yangyuan.wififileshare.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yangyuan.wififileshare.Base.BaseActivity;
import com.yangyuan.wififileshare.R;
import com.yangyuan.wififileshare.UI.UIUtils.CircleImageView;
import com.yangyuan.wififileshare.Utils.BlueToothShareUtil;
import com.yangyuan.wififileshare.config.AppConfig;
import com.yangyuan.wififileshare.wifUtils.WifiHelper;
/**
 * app主界面
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private TextView Username;
    private CircleImageView Userphoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        Userphoto.setImageResource(AppConfig.getPhotoResorce());
        Username.setText(AppConfig.userName);
    }
    private void initView(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.home_toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_history) {
                    startActivity(new Intent(HomeActivity.this,ReceiveHistoryActivity.class));
                } else if (menuItemId == R.id.action_about) {
                    startActivity(new Intent(HomeActivity.this,AboutActivity.class));
                }
                return true;
            }
        });

        findViewById(R.id.tv_share).setOnClickListener(this);
        findViewById(R.id.tv_find).setOnClickListener(this);
        findViewById(R.id.tv_inviteFriends).setOnClickListener(this);
        Username=(TextView)findViewById(R.id.iv_name);
        Userphoto=(CircleImageView)findViewById(R.id.iv_photo);
        Userphoto.setOnClickListener(this);
        Userphoto.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    Userphoto.setBorderColor(Color.parseColor("#888888"));
                else if(event.getAction() == MotionEvent.ACTION_UP)
                    Userphoto.setBorderColor(Color.parseColor("#FFFFFF"));
                return false;
            }
        });
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.tv_share:
                intent = new Intent(this, FileChoseActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_find:
                intent = new Intent(this, FindSharingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_inviteFriends:
                BlueToothShareUtil.SendFileByBlueTooth(this, getApplicationInfo().sourceDir);
                break;
            case R.id.iv_photo:
                startActivity(new Intent(this, SettingUserNamePhotoActivity.class));
                break;
        }
    }
     @Override
    protected void onDestroy() {
        WifiHelper wifiHelper=new WifiHelper();
        if (wifiHelper.isWifiEnabled())
            wifiHelper.setWifiEnabled(false);
        super.onDestroy();
    }
}
