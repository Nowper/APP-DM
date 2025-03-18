package com.huanjing.iotapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huanjing.iotapp.bean.DataBean;
import com.huanjing.iotapp.bean.ListData;
import com.huanjing.iotapp.utils.Out;

import java.util.List;

public class ListAdapter extends BaseAdapter {


    List<DataBean> data;

    public Context context;

    public ListAdapter(List<DataBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view =    LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
        }

         TextView dataView=  view.findViewById(R.id.data);

        dataView.setText(data.get(i).value);

        TextView time = view.findViewById(R.id.time);
        time.setText(data.get(i).time);

        Out.out("item :" + data.get(i).value);

        return view;
    }
}
