package com.study.fullscreendemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;

import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by zhixueyun on 2017/8/29.
 */

public class PhoneUtils {




    public static void setMiuiStatusBarDarkMode(Activity activity, boolean darkmode, String color) {
//        setStatusBarBackgroundColor(activity, color);
        String manu = getManu();
        if ("小米".equals(manu)&&Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            MUIfuncation(activity, darkmode);
            return;

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            versionM(activity, darkmode);
        } else {
            if ("OPPO".equals(manu)) {
                setStatusBarBackgroundColor(activity, "#000000");
            }
        }
    }

    /**
     * 6.0以上
     *
     * @param activity
     * @param darkmode
     */
    private static void versionM(Activity activity, boolean darkmode) {
        View decorView = activity.getWindow().getDecorView();
        if (decorView != null) {
            int vis = decorView.getSystemUiVisibility();
            if (darkmode) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
    }

    /**
     * @param activity
     * @param darkmode
     * @return
     */
    private static boolean MUIfuncation(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获得厂商名
     *
     * @return
     */
    public static String getManu() {
        String manufacturer = android.os.Build.MANUFACTURER;
        String trim = manufacturer.replace(" ", "").trim();
        String substringOne = trim.substring(0, 1);
        String substringTwo = trim.substring(1, 2);
        if ("s".equalsIgnoreCase(substringOne)) {//三星
            return "三星";
        } else if ("m".equalsIgnoreCase(substringOne)) {
            if ("e".equalsIgnoreCase(substringTwo)) {//魅族
                return "魅族";
            }
        } else if ("h".equalsIgnoreCase(substringOne)) {
            if ("u".equalsIgnoreCase(substringTwo)) {//华为
                return "华为";
            }
        } else if ("o".equalsIgnoreCase(substringOne)) {//oppo
            return "OPPO";
        } else if ("v".equalsIgnoreCase(substringOne)) {//vivo
            return "VIVO";
        } else if ("x".equalsIgnoreCase(substringOne)) {
            if ("i".equalsIgnoreCase(substringTwo)) {//小米
                return "小米";
            }
        }
        return manufacturer;

    }

    /**
     * 设置状态栏颜色
     *
     * @param colorPref
     */
    private static void setStatusBarBackgroundColor(Activity activity, final String colorPref) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 21) {
            if (colorPref != null && !colorPref.isEmpty()) {
                // Method and constants not available on all SDKs but we want to be able to compile this code with any SDK
                window.clearFlags(0x04000000); // SDK 19: WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(0x80000000); // SDK 21: WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                try {
                    // Using reflection makes sure any 5.0+ device will work without having to compile with SDK level 21
                    window.getClass().getMethod("setStatusBarColor", int.class).invoke(window, Color.TRANSPARENT);
                } catch (IllegalArgumentException ignore) {
                } catch (Exception ignore) {
                }
            }
        }
    }


    public static void immersive(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //导航栏颜色也可以正常设置
            window.setNavigationBarColor(Color.BLACK);
        } else {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            window.setAttributes(attributes);
        }
    }


    public static void disImmersive(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();

            int systemUiVisibility = decorView.getSystemUiVisibility();

            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            systemUiVisibility &= ~option;

            decorView.setSystemUiVisibility(systemUiVisibility);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
            //导航栏颜色也可以正常设置
            window.setNavigationBarColor(Color.BLACK);
        } else {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags &= ~flagTranslucentStatus;
            window.setAttributes(attributes);
        }

    }


    public static int getNavigationBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static int getStatusBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}
