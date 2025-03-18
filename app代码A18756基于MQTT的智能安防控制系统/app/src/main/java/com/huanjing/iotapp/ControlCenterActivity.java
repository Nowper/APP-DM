package com.huanjing.iotapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.huanjing.iotapp.base.BaseActivity;
import com.huanjing.iotapp.bean.ConfigBean;
import com.huanjing.iotapp.bean.MyEnventBusMessage;
import com.huanjing.iotapp.databinding.ActivityConfigBinding;
import com.huanjing.iotapp.databinding.ActivityControlcenterBinding;
import com.huanjing.iotapp.utils.Constant;
import com.huanjing.iotapp.utils.Out;

import org.greenrobot.eventbus.EventBus;

/**
 * 控制中心
 */
public class ControlCenterActivity extends BaseActivity {

    ActivityControlcenterBinding binding;
    ConfigBean configBean;

    @Override
    protected void initView() {

        binding.txOpen0.setOnClickListener(v -> {
            Constant.isAuto=true;
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("isauto",true);
            editor.commit();
            Out.toast(context,"已发送指令~");
        });
        binding.txEnd0.setOnClickListener(v -> {
            Constant.isAuto=false;
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("isauto",false);
            editor.commit();
            Out.toast(context,"已发送指令~");
        });
        binding.txOpen.setOnClickListener(v -> {
            EventBus.getDefault().post(MyEnventBusMessage.getInstance("open"));
            Out.toast(context,"已发送指令~");
        });
        binding.txEnd.setOnClickListener(v -> {
            EventBus.getDefault().post(MyEnventBusMessage.getInstance("close"));
            Out.toast(context,"已发送指令~");
        });
        binding.txOpen2.setOnClickListener(v -> {
            EventBus.getDefault().post(MyEnventBusMessage.getInstance("open2"));
            Out.toast(context,"已发送指令~");
        });
        binding.txEnd2.setOnClickListener(v -> {
            EventBus.getDefault().post(MyEnventBusMessage.getInstance("close2"));
            Out.toast(context,"已发送指令~");
        });

        binding.txOpen3.setOnClickListener(v -> {
            EventBus.getDefault().post(MyEnventBusMessage.getInstance("open3"));
            Out.toast(context,"已发送指令~");
        });
        binding.txEnd3.setOnClickListener(v -> {
            EventBus.getDefault().post(MyEnventBusMessage.getInstance("close3"));
            Out.toast(context,"已发送指令~");
        });
    }


    @Override
    protected View bindView() {
        binding= ActivityControlcenterBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void loadData() {


    }

    private void setData() {

    }
}