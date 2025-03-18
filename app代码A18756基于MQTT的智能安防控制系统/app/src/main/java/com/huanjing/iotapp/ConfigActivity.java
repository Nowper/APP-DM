package com.huanjing.iotapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.huanjing.iotapp.base.BaseActivity;
import com.huanjing.iotapp.bean.ConfigBean;
import com.huanjing.iotapp.databinding.ActivityConfigBinding;
import com.huanjing.iotapp.databinding.ActivityMainBinding;
import com.huanjing.iotapp.utils.Out;

/**
 * 阈值配置
 */
public class ConfigActivity extends BaseActivity {

    ActivityConfigBinding binding;
    ConfigBean configBean;

    @Override
    protected void initView() {
        binding.tvSave.setOnClickListener(v -> {

                if (configBean == null) {
                    configBean = new ConfigBean();
                }
                configBean.data1Max = binding.etData1Max.getText().toString().trim();

                configBean.data3Max = binding.etData3Max.getText().toString().trim();

                configBean.data4Max = binding.etData4Max.getText().toString().trim();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("config", JSON.toJSONString(configBean));
                editor.commit();
                Intent intent = new Intent();
                intent.putExtra("data", configBean);
                setResult(100, intent);
                Out.toast(context, "阈值保存成功");
                finish();

        });

    }


    @Override
    protected View bindView() {
        binding= ActivityConfigBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void loadData() {
        try {
            configBean=(ConfigBean) (getIntent().getSerializableExtra("configdata"));
        }catch (Exception exception){}
        if (configBean!=null){

        }else{
            configBean = new ConfigBean();
        }

        setData();
    }

    private void setData() {
        binding.etData1Max.setText(configBean.data1Max);


        binding.etData3Max.setText(configBean.data3Max);

        binding.etData4Max.setText(configBean.data4Max);
    }
}