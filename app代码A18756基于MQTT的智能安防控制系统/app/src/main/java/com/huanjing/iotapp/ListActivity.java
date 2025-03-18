package com.huanjing.iotapp;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.huanjing.iotapp.base.BaseActivity;
import com.huanjing.iotapp.bean.ConfigBean;
import com.huanjing.iotapp.bean.DataBean;
import com.huanjing.iotapp.bean.ListData;
import com.huanjing.iotapp.bean.MsgData;
import com.huanjing.iotapp.bean.MyEnventBusMessage;
import com.huanjing.iotapp.databinding.ActivityDatashowBinding;
import com.huanjing.iotapp.databinding.ActivityListBinding;
import com.huanjing.iotapp.utils.Constant;
import com.huanjing.iotapp.utils.Out;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 历史数据
 */
public class ListActivity extends BaseActivity {

    public List<DataBean> listdata =new ArrayList<>();

     ActivityListBinding binding;
    @Override
    protected void initView() {
        //绑定按钮事件

        binding.listbtn1.setOnClickListener(v -> {
           //切换为1 的数据
                    String listdata1 = sharedPreferences.getString("listdata1", "");

                    if (!TextUtils.isEmpty(listdata1)) {
                        listdata = JSON.parseArray(listdata1, DataBean.class);
                    }else{
                        Out.toast(context, "暂无数据");
                        listdata = new ArrayList<>();
                    }
                    if(listdata != null){
                        //xuanrna列表
                        ListView listView = binding.list;
                        Collections.reverse(listdata);
                            listView.setAdapter(new ListAdapter(listdata,this));
                    }

        });

        binding.listbtn2.setOnClickListener(v -> {
            //切换为2 的数据
            String listdata2 = sharedPreferences.getString("listdata2", "");

            if (!TextUtils.isEmpty(listdata2)) {
                listdata = JSON.parseArray(listdata2, DataBean.class);
            }else{
                Out.toast(context, "暂无数据");
                listdata = new ArrayList<>();
            }
            if(listdata != null){
                //xuanrna列表
                ListView listView = binding.list;
                Collections.reverse(listdata);
                listView.setAdapter(new ListAdapter(listdata,this));
            }

        });

        binding.listbtn3.setOnClickListener(v -> {
            //切换为3 的数据
            String listdata3 = sharedPreferences.getString("listdata3", "");

            if (!TextUtils.isEmpty(listdata3)) {
                listdata = JSON.parseArray(listdata3, DataBean.class);
            }else{
                Out.toast(context, "暂无数据");
                listdata = new ArrayList<>();
            }
            if(listdata != null){
                //xuanrna列表
                ListView listView = binding.list;
                Collections.reverse(listdata);
                listView.setAdapter(new ListAdapter(listdata,this));
            }

        });

        binding.listbtn4.setOnClickListener(v -> {
            //切换为4 的数据
            String listdata4 = sharedPreferences.getString("listdata4", "");

            if (!TextUtils.isEmpty(listdata4)) {
                listdata = JSON.parseArray(listdata4, DataBean.class);
            }else{
                Out.toast(context, "暂无数据");
                listdata = new ArrayList<>();
            }
            if(listdata != null){
                //xuanrna列表
                ListView listView = binding.list;
                Collections.reverse(listdata);
                listView.setAdapter(new ListAdapter(listdata,this));
            }

        });

        binding.listbtn5.setOnClickListener(v -> {
            //切换为5 的数据
            String listdata5 = sharedPreferences.getString("listdata5", "");

            if (!TextUtils.isEmpty(listdata5)) {
                listdata = JSON.parseArray(listdata5, DataBean.class);
            }else{
                Out.toast(context, "暂无数据");
                listdata = new ArrayList<>();
            }
            if(listdata != null){
                //xuanrna列表
                ListView listView = binding.list;
                Collections.reverse(listdata);
                listView.setAdapter(new ListAdapter(listdata,this));
            }

        });

        binding.listbtn6.setOnClickListener(v -> {
            //切换为6 的数据
            String listdata6 = sharedPreferences.getString("listdata6", "");

            if (!TextUtils.isEmpty(listdata6)) {
                listdata = JSON.parseArray(listdata6, DataBean.class);
            }else{
                Out.toast(context, "暂无数据");
                listdata = new ArrayList<>();
            }
            if(listdata != null){
                //xuanrna列表
                ListView listView = binding.list;
                Collections.reverse(listdata);
                listView.setAdapter(new ListAdapter(listdata,this));
            }

        });

    }


    @Override
    protected View bindView() {
        binding= ActivityListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void loadData() {

        binding.listbtn1.callOnClick();


    }

    private void setData() {

    }

}