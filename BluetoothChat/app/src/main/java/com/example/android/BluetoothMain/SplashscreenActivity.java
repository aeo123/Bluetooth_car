package com.example.android.BluetoothMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by aeo on 2016/2/14.
 */
public class SplashscreenActivity extends Activity{
    /** Splash screen duration time in milliseconds */
    private static final int DELAY = 1000;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //首页启动图片显示2秒后跳转
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                runOnUiThread(new Runnable() {
                    /**
                     *跳转到MainTivity
                     */
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashscreenActivity.this, BluetoothMain.class));   //跳转到检测蓝牙界面
                        finish();   //销毁当前页面
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        // do nothing. Protect from exiting the application when splash screen is shown
    }
}
