package com.study.fullscreendemo.system_webview;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

import com.study.fullscreendemo.R;


public class FullScreenWithSysWebViewActivity extends Activity {


	SystemWebView webView;
	private FrameLayout flWebView;
	private FrameLayout flFullVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_screen_sys_webview_layout);
		flWebView = findViewById(R.id.fl_webview);
		flFullVideo = findViewById(R.id.fl_full_video);
		webView = findViewById(R.id.web_filechooser);
		webView.loadUrl("file:///android_asset/dist/index.html");

		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		webView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
		webView.setWebChromeClient(new MyWebChromeClient());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// 屏幕旋转时候操作
		try {
			super.onConfigurationChanged(newConfig);
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	class MyWebChromeClient extends WebChromeClient {
		private View myView = null;
		// 全屏
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			super.onShowCustomView(view, callback);
			ViewGroup parent = (ViewGroup) webView.getParent();
			parent.removeView(webView);

			flWebView.addView(view);
			flFullVideo.setVisibility(View.VISIBLE);
			myView = view;
			setFullScreen();
		}

		// 退出全屏
		@Override
		public void onHideCustomView() {
			super.onHideCustomView();
			if (myView != null) {
				flFullVideo.removeAllViews();
				//要移除之前添加的myView，否则将会引起竖屏时的状态栏异常
				flWebView.removeView(myView);
				flWebView.addView(webView);
				flFullVideo.setVisibility(View.GONE);

				myView = null;
				quitFullScreen();
			}
		}
	}
	/**
	 * 设置全屏
	 */
	private void setFullScreen() {
		// 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * 退出全屏
	 */
	private void quitFullScreen() {
		this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

}
