package com.huanjing.iotapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.huanjing.iotapp.base.BaseActivity;
import com.huanjing.iotapp.bean.ConfigBean;
import com.huanjing.iotapp.bean.MsgData;
import com.huanjing.iotapp.bean.MyEnventBusMessage;
import com.huanjing.iotapp.databinding.ActivityConfigBinding;
import com.huanjing.iotapp.databinding.ActivityDatashowBinding;
import com.huanjing.iotapp.utils.Out;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 实时数据
 */
public class DataShowActivity extends BaseActivity {

    ActivityDatashowBinding binding;
    @Override
    protected void initView() {


    }


    @Override
    protected View bindView() {
        binding= ActivityDatashowBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void loadData() {


    }

    private void setData() {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MyEnventBusMessage myEnventBusMessage) {
        String data=myEnventBusMessage.message;
        try {
            MsgData msgData=JSON.parseObject(data,MsgData.class);

            //这里设置数据
            binding.tvData1.setText(msgData.data1);
            binding.tvData2.setText(msgData.data2);
            binding.tvData3.setText(msgData.data3);
            binding.tvData4.setText(msgData.data4);
            binding.tvData5.setText(msgData.data5);
            binding.tvData6.setText(msgData.data6);

        }catch (Exception exception){}

    }
}