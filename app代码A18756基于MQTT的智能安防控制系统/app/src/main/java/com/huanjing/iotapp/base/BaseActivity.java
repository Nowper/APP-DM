package com.huanjing.iotapp.base;

import static android.content.ContentValues.TAG;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.huanjing.iotapp.R;
import com.huanjing.iotapp.utils.Out;
import com.huanjing.iotapp.view.TopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Map;


/**
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener ,TopBar.ITopBarClickListener{
    public FragmentManager manager;
    public Fragment currentFragment;
    public TopBar mTopBar;
    public Context context;
    public Handler hd;
    public SharedPreferences sharedPreferences;

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(bindView());

        EventBus.getDefault().register(this);
        initView();

        manager = getSupportFragmentManager();

//        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue));
        }
       context=this;
        hd=new Handler();
        sharedPreferences=getSharedPreferences("mmyx",MODE_PRIVATE);
       
        initTopBar();

        setListener();
        loadData();

    }


    @Override
    public void leftClick() {
        finish();
    }
    @Override
    public void rightClick() {

    }
    public void goToNextActivity(Class activity) {
        Intent intent = new Intent(this, activity);
       startActivity(intent);
    }

    public void goToNextActivity(Class activity,Bundle bundle) {
        Intent intent = new Intent(this, activity);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        try {
            EventBus.getDefault().unregister(this);
        }catch (Exception ex){}
    }

    private  void initTopBar(){
        try {

        } catch (Exception e) {
            // TODO: handle exception
        }

    }
    public void setRightClickText(String str) {
        if (mTopBar != null) {
            mTopBar.setRightText(str);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setListener(){
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    protected abstract void initView();
    protected abstract View bindView();
    protected abstract void loadData();

    @Override
    public void onClick(View v) {
    }

    public  void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public  boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 实现点击空白处，软键盘消失事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View view = getCurrentFocus();
            Out.hideKeyboard(ev, view, BaseActivity.this);//调用方法判断是否需要隐藏键盘
        }
        return super.dispatchTouchEvent(ev);
    }

}
