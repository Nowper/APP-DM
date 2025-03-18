package com.huanjing.iotapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Out {
    public final static String PHONE_PATTERN="^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(16([0-3]|[5-9]))|(19([0-3]|[5-9]))|(17([0,1,6,7,]))|(18[0-3,5-9]))\\d{8}$";
    public static void out(String str){
        Log.e("mydemo",str);
    }
    public static void toast(Context ct,String str){
        Toast.makeText(ct,str,Toast.LENGTH_SHORT).show();
    }
    public static boolean isPhone(String ss){

        boolean isPhone = Pattern.compile(PHONE_PATTERN).matcher(ss).matches();
        return isPhone;
    }
    /**
     * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
     *
     * @param view
     *            控件view
     * @param event
     *            焦点位置
     * @return 是否隐藏
     */
    public static void hideKeyboard(MotionEvent event, View view,
                                    Activity activity) {
        try {
            if (view != null && view instanceof EditText) {
                int[] location = { 0, 0 };
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getCurrentTime(long value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm") ;
        String time = format.format(new Date(value));
        return time;
    }
    public static String getnum2div(int a,int b){


        DecimalFormat df=new DecimalFormat("0.00");
        String jg=df.format((float)a/b);
        return jg;

    }
    public static String getnum2divlong(long a,long b){


        DecimalFormat df=new DecimalFormat("0.00");
        String jg=df.format((float)a/b);
        return jg;

    }
    public static String getnum2divff(float a,int b){


        DecimalFormat df=new DecimalFormat("0.00");
        String jg=df.format((float)a/b);
        return jg;

    }
    /**
     * view转bitmap
     */
    public static Bitmap getBitmapView(View v) {

        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        //创建一个空的bitmap，并且利用这个空的bitmap预先渲染一张白色画布

        v.layout(0, 0, w, h);
        v.draw(c);
        //将布局中的内容填充到画布
        return bmp;
    }
}
