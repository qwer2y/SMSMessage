package com.example.TestTaxi.Utils.SMS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.TestTaxi.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huyao on 2017/10/11.
 * 短信拦截广播
 */

public class SmsReceiver extends BroadcastReceiver
{
    SmsTools smsTools;
    SmsMessage[] messages;
    String temp = null ;
    String body;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        smsTools = SmsTools.getInstance(context);

        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];  //解析短信
            for (int i = 0; i < pdus.length; i++)
            {
                byte[] pdu = (byte[]) pdus[i];
                messages[i] = SmsMessage.createFromPdu(pdu);
            }
        }

        SmsMessage sms = messages[0];
        Date date = new Date(sms.getTimestampMillis());  // 获取短信日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String receiveTime = format.format(date);
        //日期
        String data = receiveTime.trim();
        //电话号码
        temp = smsTools.interceptionTel(sms.getOriginatingAddress());
        //姓名
        String name = smsTools.getContactsName(temp,context);

        // 内容
        if (messages.length == 1) {
            body = sms.getDisplayMessageBody();
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < messages.length; i++) {
                sms = messages[i];
                sb.append(sms.getDisplayMessageBody());
            }
            body = body.toString();
        }
        //状态码
        int status = sms.getStatus();
        //服务中心中转号码
        String centerAddress = sms.getServiceCenterAddress();


        Log.e("cdzhhc","时间: "+data+" 电话："+temp+" 姓名："
                +name+" 内容："+ body+"状态码："+status+" 服务中心转发号码："+centerAddress);


        if (body.contains("####重启####"))
        {
            Intent intent2 = new Intent();
            intent2.setPackage(context.getPackageName());
            intent2.setAction("Voice");
            intent2.putExtra("voice", 2);
            intent2.putExtra(Constants.TEXT, "设备即将进行重启");
            context.startService(intent2);

            PowerManager pManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            pManager.reboot("重启");
        }


//        smsTools.getBroadcastData(intent);   // 接收广播的短信内容 写入数据库
        abortBroadcast();     // 终止广播接收
    }
}
