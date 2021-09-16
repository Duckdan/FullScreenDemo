package com.study.fullscreendemo;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.study.fullscreendemo.system_webview.FullScreenWithSysWebViewActivity;
import com.study.fullscreendemo.x5webview.FullScreenActivity;

/**
 * 视频播放地址
 * 这是西瓜视频的介绍，h5代码里面使用了字节跳动xgplayer的开源框架做视频播放
 * https://lf9-cdn-tos.bytecdntp.com/cdn/expire-1-M/byted-player-videos/1.0.0/xgplayer-demo.mp4
 * 如果想要替换视频播放地址，在源代码里面app/src/main/assets/dist全局搜索上面这个地址，替换即可
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        PhoneUtils.immersive(this);
        //状态栏半透明的情况下下面方法失效，无法调节状态栏字体是亮色还是非亮色
        PhoneUtils.setMiuiStatusBarDarkMode(this, true, "#ffffff");
        findViewById(R.id.bt_fullscreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        findViewById(R.id.bt_smallscreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
        findViewById(R.id.bt_hideBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        });
        findViewById(R.id.bt_showBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        });

        //使用系统webView
        findViewById(R.id.bt_sys_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FullScreenWithSysWebViewActivity.class);
                startActivity(intent);
            }
        });

        //使用x5webview
        findViewById(R.id.bt_x5_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FullScreenActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        Log.e("fangxiang", newConfig.orientation + "==11=" + Integer.toHexString(getWindow().getAttributes().flags));

    }

}
