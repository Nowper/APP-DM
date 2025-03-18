package com.huanjing.iotapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huanjing.iotapp.base.BaseActivity;
import com.huanjing.iotapp.bean.ConfigBean;
import com.huanjing.iotapp.bean.DataBean;
import com.huanjing.iotapp.bean.MsgData;
import com.huanjing.iotapp.bean.MyEnventBusMessage;
import com.huanjing.iotapp.databinding.ActivityLoginBinding;
import com.huanjing.iotapp.databinding.ActivityMainBinding;
import com.huanjing.iotapp.utils.Constant;
import com.huanjing.iotapp.utils.DateUtils;
import com.huanjing.iotapp.utils.NotificationUtils;
import com.huanjing.iotapp.utils.Out;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

public class MainActivity extends BaseActivity {


    public static String TOPIC = "4979";
    public static String TOPIC_PUSH = "4979_ctrl";


    ActivityMainBinding binding;
    ConfigBean configBean;
    MqttAndroidClient sampleClient;

    public static int a1 = 0;
    public static int a2 = 0;
    public static int a3 = 0;
    public static int buzzer = 0;

    public static int counter = 0;
    public static int prevCounter = 0;

    private NotificationUtils mNotificationUtils;
    @Override
    protected void initView() {
        binding.tvConfig.setOnClickListener(v -> {
            startActivityForResult(new Intent(context, ConfigActivity.class).putExtra("configdata", configBean), 1);

        });
        binding.tvDatashow.setOnClickListener(v -> {
            startActivity(new Intent(context, DataShowActivity.class));

        });
        binding.tvControlcenter.setOnClickListener(v -> {
            startActivity(new Intent(context, ControlCenterActivity.class));
        });
        binding.tvList.setOnClickListener(v -> {
            startActivity(new Intent(context, ListActivity.class));
        });

        mNotificationUtils = new NotificationUtils(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            configBean = (ConfigBean) data.getSerializableExtra("data");
        }
    }

    @Override
    protected View bindView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void loadData() {

        String configStr = sharedPreferences.getString("config", "");
        Constant.isAuto = sharedPreferences.getBoolean("isauto", false);
        if (!TextUtils.isEmpty(configStr)) {
            configBean = JSON.parseObject(configStr, ConfigBean.class);
        }
        initMqtt();

    }

    private void initMqtt() {

        try {
            if (sampleClient != null) {
                try {
                    sampleClient.close();
                } catch (Exception exception) {
                }

                try {
                    sampleClient.disconnect();
                } catch (Exception exception) {
                }
                sampleClient = null;

            }
        } catch (Exception e) {
        }

        sampleClient = new MqttAndroidClient(context, "tcp://120.27.235.176:1883", "iotioAt2" + TOPIC + "AAAAAA" + System.currentTimeMillis(), Ack.AUTO_ACK);
        Out.out("mqtt-serverURI:tcp://120.27.235.176:1883");
        Out.out("mqtt-clientId:" + "iotioAt2" + TOPIC + "AAAAAA" + System.currentTimeMillis());

        //创建服务连接
        try {
            MqttConnectOptions connOpts = createConnectOptions("root", "root");
            sampleClient.connect(connOpts, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //连接成功，需要上传客户端所有的订阅关系
                    sampleClient.subscribe(TOPIC, 0);
                    Out.out("连接成功AAA");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Out.out("mqtt连接失败：" + exception.getMessage());
                }
            });
        } catch (NoSuchAlgorithmException e) {
            Out.out("NoSuchAlgorithmException=" + e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            Out.out("InvalidKeyException=" + e.getMessage());
            e.printStackTrace();
        } catch (Exception ee) {
            Out.out("mqeee=" + ee.getMessage());
            ee.printStackTrace();
        }


        //创建额外的线程，20秒处理一次判断连接的情况，如果 本次计数器和上一次一样，说明数据没变化，则认为数据传输终止了

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        if(counter == prevCounter){
                            EventBus.getDefault().post(MyEnventBusMessage.getInstance("error"));
                        }

                        prevCounter = counter;

                        Thread.sleep(10000);

                    }catch ( Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        sampleClient.setCallback(new MqttCallback() {
            @Override//连接丢失后，会执行这里
            public void connectionLost(Throwable throwable) {
                Out.out("mqtt==connectionLost");
            }

            @Override//获取的消息会执行这里--arg0是主题,arg1是消息
            public void messageArrived(final String ssr, MqttMessage mqttMessage) throws Exception {

                String mqtt_message = mqttMessage.toString();//消息
                try {
                    Out.out("mqtt-msg=" + mqtt_message);

                    //处理硬件连接状态
                    //  有数据来一次，计数器 + 1；
                    counter ++;
                    binding.status.setText("硬件连接成功");

                    //这里需要进行转换
                    JSONObject json = JSON.parseObject(mqtt_message);

                    //判断报警情况

                    MsgData msgData = new MsgData();
                    msgData.data1 = json.getString("dht11_temper");
                    msgData.data2 = json.getString("dht11_humi");
                    msgData.data3 = json.getString("MQ2");
                    msgData.data4 = json.getString("MQ5");
                    msgData.data5 = json.getString("INPUT2");
                    msgData.data6 = json.getString("INPUT1");


                    msgData.data11 = msgData.data1;
                    msgData.data21 = msgData.data2;
                    msgData.data31 = msgData.data3;
                    msgData.data41 = msgData.data4;
                    msgData.data51 = msgData.data5;
                    msgData.data61 = msgData.data6;

                    boolean f = false;

                    try {


                        if (Double.parseDouble(msgData.data1) > Double.parseDouble(configBean.data1Max)) {//超过最大值
                            msgData.data1 += " ↑";
                        }

                        if (Double.parseDouble(msgData.data3) > Double.parseDouble(configBean.data3Max)) {//超过最大值
                            msgData.data3 += " ↑";
                            f = true;
                        }

                        if (Double.parseDouble(msgData.data4) > Double.parseDouble(configBean.data4Max)) {// 超过最大值
                            msgData.data4 += " ↑";
                            f = true;
                        }

                        if (Double.parseDouble(msgData.data5) ==1) {// 超过最大值
                            msgData.data5 = " 火焰异常";
                            f = true;
                        } else   {// 低于最小值
                            msgData.data5 = " 正常";
                        }

                        if (Double.parseDouble(msgData.data6)  ==1) {// 超过最大值
                            msgData.data6  = " 有人";
                        } else   {// 低于最小值
                            msgData.data6  = " 没有人";
                        }

                        if(f || (buzzer==1 && Double.parseDouble(msgData.data61)  ==1)){

                            NotificationUtils.showNotification(context,"警报！！！");


                            sendMqttMessage("OPEN-BUZZER");
                        }else{
                            sendMqttMessage("CLOSE-BUZZER");
                        }

// 【自动模式： 当烟雾，天然气异常，温度太高，开启窗户通风透气 + 开启风扇】
//【烟雾异常，天然气异常，火焰异常，蜂鸣器报警】
//【开启布防模式：红外检测到人，蜂鸣器报警】

                    } catch (Exception e) {
                        Out.out(e.getMessage());
                    }

                    try {
                        dataHandler(msgData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    EventBus.getDefault().post(MyEnventBusMessage.getInstance(JSONObject.toJSONString(msgData)));


                    if (Constant.isAuto) {//自动模式下才判断加发送消息

                        if (Double.parseDouble(msgData.data11) > Double.parseDouble(configBean.data1Max) ||
                                Double.parseDouble(msgData.data31) > Double.parseDouble(configBean.data3Max)||
                                Double.parseDouble(msgData.data41) > Double.parseDouble(configBean.data4Max)) {//超过最大值
                            if (a1 != 1) {
                                a1 = 1;

                                Thread.sleep(500);
                                sendMqttMessage("OPEN-OUTPUT1");
                                Thread.sleep(500);
                                sendMqttMessage("PWM1-1");

                                Thread.sleep(500);
                                sendMqttMessage("OPEN-OUTPUT1");
                                Thread.sleep(500);
                                sendMqttMessage("PWM1-1");


                            }
                        } else {
                            if (a1 != 0) {
                                a1 = 0;

                                Thread.sleep(500);
                                sendMqttMessage("CLOSE-OUTPUT1");
                                Thread.sleep(500);
                                sendMqttMessage("PWM1-0");


                                Thread.sleep(500);
                                sendMqttMessage("CLOSE-OUTPUT1");
                                Thread.sleep(500);
                                sendMqttMessage("PWM1-0");
                            }
                        }




                    }else{
                        a1=-1;
                        a2=-1;
                        a3=-1;

                    }


                } catch (Exception exception) {
                    Out.out("EEEE==" + exception.getMessage());
                }


            }

            @Override//订阅主题后会执行到这里
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Out.out("mqtt==deliveryComplete");

            }
        });
    }

    private MqttConnectOptions createConnectOptions(String userName, String passWord) throws NoSuchAlgorithmException, InvalidKeyException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(userName);
        connOpts.setPassword(passWord.toCharArray());
        connOpts.setAutomaticReconnect(true);
        // 设置连接超时时间, 单位为秒,默认30
        connOpts.setConnectionTimeout(30);
        // 设置会话心跳时间,单位为秒,默认20
        connOpts.setKeepAliveInterval(20);
        return connOpts;
    }

    //处理历史数据，保存到APP
    private void dataHandler(MsgData msgData) {
        if(msgData.data11 != null){

            String listdata1 = sharedPreferences.getString("listdata1", "");
            List<DataBean> listdata = new ArrayList<>();
            if (!TextUtils.isEmpty(listdata1)) {
                listdata = JSON.parseArray(listdata1, DataBean.class);
            }

            if(listdata.size()>50){
                listdata.remove(0);
            }

            //加入最新的数据
            DataBean newdata = new DataBean();
            newdata.time = DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN);
            newdata.value = msgData.data1 ;
            listdata.add(newdata);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("listdata1", JSON.toJSONString(listdata));
            editor.commit();


        }

        if(msgData.data21 != null){

            String listdata2 = sharedPreferences.getString("listdata2", "");
            List<DataBean> listdata = new ArrayList<>();
            if (!TextUtils.isEmpty(listdata2)) {
                listdata = JSON.parseArray(listdata2, DataBean.class);
            }

            if(listdata.size()>50){
                listdata.remove(0);
            }

            //加入最新的数据
            DataBean newdata = new DataBean();
            newdata.time = DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN);
            newdata.value = msgData.data2 ;
            listdata.add(newdata);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("listdata2", JSON.toJSONString(listdata));
            editor.commit();


        }

        if(msgData.data31 != null){

            String listdata3 = sharedPreferences.getString("listdata3", "");
            List<DataBean> listdata = new ArrayList<>();
            if (!TextUtils.isEmpty(listdata3)) {
                listdata = JSON.parseArray(listdata3, DataBean.class);
            }

            if(listdata.size()>50){
                listdata.remove(0);
            }

            //加入最新的数据
            DataBean newdata = new DataBean();
            newdata.time = DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN);
            newdata.value = msgData.data3 ;
            listdata.add(newdata);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("listdata3", JSON.toJSONString(listdata));
            editor.commit();


        }

        if(msgData.data41 != null){

            String listdata4 = sharedPreferences.getString("listdata4", "");
            List<DataBean> listdata = new ArrayList<>();
            if (!TextUtils.isEmpty(listdata4)) {
                listdata = JSON.parseArray(listdata4, DataBean.class);
            }

            if(listdata.size()>50){
                listdata.remove(0);
            }

            //加入最新的数据
            DataBean newdata = new DataBean();
            newdata.time = DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN);
            newdata.value = msgData.data4 ;
            listdata.add(newdata);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("listdata4", JSON.toJSONString(listdata));
            editor.commit();


        }

        if(msgData.data51 != null){

            String listdata5 = sharedPreferences.getString("listdata5", "");
            List<DataBean> listdata = new ArrayList<>();
            if (!TextUtils.isEmpty(listdata5)) {
                listdata = JSON.parseArray(listdata5, DataBean.class);
            }

            if(listdata.size()>50){
                listdata.remove(0);
            }

            //加入最新的数据
            DataBean newdata = new DataBean();
            newdata.time = DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN);
            newdata.value = msgData.data5 ;
            listdata.add(newdata);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("listdata5", JSON.toJSONString(listdata));
            editor.commit();


        }


        if(msgData.data61 != null){

            String listdata6 = sharedPreferences.getString("listdata6", "");
            List<DataBean> listdata = new ArrayList<>();
            if (!TextUtils.isEmpty(listdata6)) {
                listdata = JSON.parseArray(listdata6, DataBean.class);
            }

            if(listdata.size()>60){
                listdata.remove(0);
            }

            //加入最新的数据
            DataBean newdata = new DataBean();
            newdata.time = DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN);
            newdata.value = msgData.data6 ;
            listdata.add(newdata);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("listdata6", JSON.toJSONString(listdata));
            editor.commit();


        }


    }

    private void sendMqttMessage(String msg) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {


                    MqttMessage message = new MqttMessage();
                    message.setPayload(msg.getBytes());
                    sampleClient.publish(TOPIC_PUSH, message, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Out.out("发送成功mqtt:" + msg);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Out.out("发送失败:" + msg);

                        }
                    });
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
        });

        thread.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MyEnventBusMessage myEnventBusMessage) {
        String data = myEnventBusMessage.message;
        if ("error".equals(data)) {
            binding.status.setText("未连接硬件/连接断开");
        }





        if ("open".equals(data)) {
            sendMqttMessage("PWM1-1");
        }
        if ("close".equals(data)) {
            sendMqttMessage("PWM1-0");
        }

        if ("open2".equals(data)) {
            sendMqttMessage("OPEN-OUTPUT1");
        }
        if ("close2".equals(data)) {
            sendMqttMessage("CLOSE-OUTPUT1");
        }

        if ("open3".equals(data)) {
             buzzer =1;
        }
        if ("close3".equals(data)) {
            buzzer =0;
        }

    }
}

