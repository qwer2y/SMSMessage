package com.example.TestTaxi.Utils.SMS
        ;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.TestTaxi.Utils.smsUpdateData;
import com.example.TestTaxi.xia_project.Taxi_HomeActivity;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Andy on 2015/6/8.
 */
public class SmsTools {
    private Context mContext;
    private static SmsTools mTools = null;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    final String SMS_Action = "android.provider.Telephony.SMS_RECEIVED";

    /**
     * No need use out of the class
     *
     * @param context
     */
    public SmsTools(Context context) {
        this.mContext = context;
    }

    public static SmsTools getInstance(Context context) {
        if (mTools == null) {
            mTools = new SmsTools(context);
        }
        return mTools;
    }

    /**
     * @param context      上下文
     * @param ReceiverName 接收器名称
     *                     注册短信接收器
     */
    public void registerSmsReceiver(Context context, BroadcastReceiver ReceiverName) {
        ReceiverName = new SmsReceiver();     // 实例化一个广播接收器
        IntentFilter filter = new IntentFilter(SMS_Action);  // 设置Action
        context.registerReceiver(ReceiverName, filter);   // 注册广播接收器
    }

    /**
     * @param context      上下文
     * @param ReceiverName 短信接收器名称
     *                     取消短信接收器的注册
     */
    public void unregisterSmsReceiver(Context context, BroadcastReceiver ReceiverName) {
        context.unregisterReceiver(ReceiverName);
    }

    /**
     * 打开数据库
     *
     * @param context
     */
    private void opendb(Context context) {
        helper = new SmsDatabase(mContext);
        db = helper.getWritableDatabase();
    }

    /**
     * 关闭数据库
     *
     * @param context
     */
    public void closedb(Context context) {
        if (db.isOpen()) {
            db.close();
        }
    }

    /**
     * 向数据库中插入数据
     *
     * @param values
     */
    public void insert(ContentValues values) {
        opendb(mContext);
        db.insert("sms", null, values);
        closedb(mContext);
    }

    /**
     * 第一个参数String：表名
     * 第二个参数ContentValues：ContentValues对象
     * 第三个参数String：where字句，相当于sql语句where后面的语句，？号是占位符
     * 第四个参数String[]：占位符的值
     * 通过ID更新数据库数据
     */
    public int updatatableById(ContentValues values, int ID) {
        opendb(mContext);
        return db.update("sms", values, " id = ? ", new String[]{String.valueOf(ID)});
    }

    /**
     *  更新数据库的数据
     * @param values
     * @return
     */
    public int updatatableAll (ContentValues values) {
        opendb(mContext);
        return db.update("sms", values,null, null);
    }

    /**
     * 根据电话号码更新数据库数据
     *
     * @param values
     * @param Tel
     * @return
     */
    public int updatatableByTel(ContentValues values, String[] Tel) {
        opendb(mContext);
        return db.update("sms", values, " telphone = ? ", Tel);
    }

    /**
     * 根据日期更新数据库数据
     *
     * @param values
     * @param date
     * @return
     */
    public int updatatableByDate(ContentValues values, String[] date) {
        opendb(mContext);
        return db.update("sms", values, " date = ? ", date);
    }

    /**
     * 根据发送和接收状态更新数据库数据
     *
     * @param values
     * @param status
     * @return
     */
    public int updatatableByStatus(ContentValues values, int status) {
        opendb(mContext);
        return db.update("sms", values, " status = ? ", new String[]{String.valueOf(status)});
    }

    /**
     * 根据读过和未读状态更新数据库数据
     *
     * @param values
     * @param read
     * @return
     */
    public int updatatableByRead(ContentValues values, int read) {
        opendb(mContext);
        return db.update("sms", values, " read = ? ", new String[]{String.valueOf(read)});
    }

    public int updatatableByCondition(ContentValues values,Cursor cursor) {
        opendb(mContext);
        String[] condition = new String[]{cursor.getString(cursor.getColumnIndexOrThrow("date")),
                cursor.getString(cursor.getColumnIndexOrThrow("telphone")),
                cursor.getString(cursor.getColumnIndexOrThrow("body"))};
        return db.update("sms", values, " date = ? and telphone = ?  and body = ?", condition);
    }

    /**
     * 删除数据库所有数据
     * 第一个参数String：表名
     * 第二个参数String：条件语句
     * 第三个参数String[]：条件值
     */
    public void deleteAll() {
        opendb(mContext);
        try {

            db.delete("sms", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closedb(mContext);
        }
    }

    /**
     * 根据ID来删除数据库指定的数据
     *
     * @param id 指定ID
     */
    public void deleteById(int id) {
        opendb(mContext);
        try {

            db.delete("sms", "id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closedb(mContext);
        }
    }

    /**
     * 根据是否读取过来删除数据库指定的数据
     *
     * @param read
     */
    public void deleteByRead(int read) {

        opendb(mContext);
        try {
            db.delete("sms", "read = ?", new String[]{String.valueOf(read)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closedb(mContext);
        }
    }

    /**
     * 根据发送和接收状态来删除数据库的数据
     *
     * @param status
     */
    public void deleteByStatus(int status) {
        opendb(mContext);
        try {

            db.delete("sms", "status = ?", new String[]{String.valueOf(status)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closedb(mContext);
        }
    }

    /**
     * 根据电话号码删除数据库数据
     *
     * @param tel
     */
    public void deleteByTel(String[] tel) {
        opendb(mContext);
        try {
            db.delete("sms", "telphone = ?", tel);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closedb(mContext);
        }
    }

    /**
     * 根据日期删除指定数据库数据
     *
     * @param date
     */
    public void deleteByDate(String[] date) {
        opendb(mContext);
        try {
            db.delete("sms", "date = ?", date);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closedb(mContext);
        }
    }

    /**
     * 查询数据库所有内容
     * 第一个参数String：表名
     * 第二个参数String[]:要查询的列名
     * 第三个参数String：查询条件
     * 第四个参数String[]：查询条件的参数
     * 第五个参数String:对查询的结果进行分组
     * 第六个参数String：对分组的结果进行限制
     * 第七个参数String：对查询的结果进行排序
     */
    public Cursor queryAll() {
        opendb(mContext);
        return db.query("sms", null, null, null, null, null, null);
    }

//    /**
//     * 根据ID来查询数据库数据
//     */
//    public Cursor queryById(int id) {
//        opendb(mContext);
//        return db.query("sms", new String[]{"id"}, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
//    }

    public Cursor queryByRead(int read) {
        opendb(mContext);
        return db.query("sms", null, "read = ?", new String[]{String.valueOf(read)}, null, null, null);
    }

    public Cursor queryByStatus(int status) {
        opendb(mContext);
        return db.query("sms", null, "status = ?", new String[]{String.valueOf(status)}, null, null, null);
    }

    public Cursor queryByTel(String[] tel) {
        opendb(mContext);
        return db.query("sms", null, "telphone = ?", tel, null, null, null);
    }

    public Cursor queryByDate(String[] date) {
        opendb(mContext);
        return db.query("sms", null, "date = ?", date, null, null, null);
    }

    /**
     *  更具多个条件查询数据库指定数据
     * @param sdata
     * @return
     */
    public Cursor queryByCondition(smsdata sdata) {
        String[] condition = new String[]{
                String.valueOf(sdata.getRead()),sdata.getDate(),sdata.getTel(),sdata.getBody()
        };
        opendb(mContext);
        return db.query("sms",null,"read = ? and date = ? and telphone = ? and body = ?",condition,null,null,null);
    }


    /**
     *  根据指定数据设置数据库对应数据为已读，并删除数据库对应数据
     * @param sdata
     * @return  1：表示指定数据被删除  0：表示没有指定删除数据
     */
    public int setReadBySmsdata (smsdata sdata) {
        if ( sdata != null && sdata.getRead() == 0 ) {
            Cursor cursor = queryByCondition(sdata);
            while (cursor.moveToNext()) {
                setRead(cursor);    // 设置数据为已读
                deleteByRead(1);    // 删除已读数据
                return 1;
            }
            cursor.close();
        }
        return 0;
    }


    /**
     *   设置read状态
     *      read = 1    表示短信已经读过
     *      read = 0    表示短信未读
     * @param cursor
     */
    private void setRead (Cursor cursor) {
        ContentValues values = new ContentValues();
        values.put("date", cursor.getString(cursor.getColumnIndexOrThrow("date")));
        values.put("telphone", cursor.getString(cursor.getColumnIndexOrThrow("telphone")));
        values.put("body", cursor.getString(cursor.getColumnIndexOrThrow("body")));
        values.put("status", cursor.getInt(cursor.getColumnIndexOrThrow("status")));
        values.put("read", 1);
        values.put("ServiceCenterAddress", cursor.getString(cursor.getColumnIndexOrThrow("ServiceCenterAddress")));
        updatatableByCondition(values, cursor);
    }

    /**
     *  读取数据库的数据为显示作准备
     * @param cursor
     * @return
     */
    private smsdata initSmsData(Cursor cursor) {
        smsdata mdata = new smsdata();
        mdata.setTel(cursor.getString(cursor.getColumnIndexOrThrow("telphone")));
        mdata.setBody(cursor.getString(cursor.getColumnIndexOrThrow("body")));
        mdata.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        mdata.setServiceCenterAddress(cursor.getString(cursor.getColumnIndexOrThrow("ServiceCenterAddress")));
        mdata.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow("status")));
        mdata.setRead(cursor.getInt(cursor.getColumnIndexOrThrow("read")));
        mdata.setSmsCounter(mdata.getSmsCounter() + 1);
        return mdata;
    }

    /**
     * 设置短信的显示内容；
     * @param cursor
     * @param list
     * @return 返回一个list 对象
     */
    public List<smsdata> initListData(Cursor cursor, List<smsdata> list) {
        smsdata mdata = initSmsData(cursor);
        if (mdata == null) return null;
        list.add(mdata);
        return list;
    }

    /**
     *  截取电话号码，删除"+86"
     * @param tel
     * @return
     */
    public String interceptionTel(String tel) {
        String ret = tel ;
        if (tel.startsWith("+86")) {
            ret = tel.substring(3);
        }
        return ret ;
    }

    /**
     *  读取短信的具体内容存入数据库中
     * @param messages
     */
    private void getSmsData(SmsMessage[] messages) {
        String temp = null ;
        SmsMessage sms = messages[0];
        ContentValues values = new ContentValues();
        Date date = new Date(sms.getTimestampMillis());  // 获取短信日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String receiveTime = format.format(date);
        values.put("date", receiveTime.trim());

        temp = interceptionTel(sms.getOriginatingAddress());
        String name = getContactsName(temp,mContext);
        values.put("telphone", name);

        // 将拆分的短信合并成一条信息存入数据库
        if (messages.length == 1) {
            values.put("body", sms.getDisplayMessageBody());
        } else {
            StringBuilder body = new StringBuilder();
            for (int i = 0; i < messages.length; i++) {
                sms = messages[i];
                body.append(sms.getDisplayMessageBody());
            }
            values.put("body", body.toString());
        }
        values.put("status", sms.getStatus());
        values.put("read", 0);
        values.put("ServiceCenterAddress", sms.getServiceCenterAddress());
        insert(values);
    }

    /**
     * 获取联系人名字
     * @param tel
     * @param context
     * @return
     */
    public String  getContactsName (String tel,Context context) {
        String contactName = tel;
        ContentResolver cr = context.getContentResolver();
        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",new String[]{tel}, null);
        if (pCur.moveToFirst()) {
            contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            pCur.close();
        }
        return contactName;
    }


    /**
     * 通过广播来获取短信信息并更新UI界面
     *
     * @param intent
     */
    public void getBroadcastData(Intent intent) {
        if (intent.getAction().equals(SMS_Action)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];  //解析短信
                for (int i = 0; i < pdus.length; i++) {
                    byte[] pdu = (byte[]) pdus[i];
                    messages[i] = SmsMessage.createFromPdu(pdu);
                }
                //添加到数据库
                getSmsData(messages);
                //通知HomeActivity获取数据
                smsUpdateData data = new Taxi_HomeActivity();
                data.updataSMSData();
            }
        }
    }

    /**
     * 比较当前系统SDK是否大于KITKAT(4.4);
     *  这样做主要是4.4以后短信服务要获取默认权限。
     * @return
     */
    public static boolean sdkGreaterKitkat() {
        int osVersion = -1;
        int newVersion = android.os.Build.VERSION_CODES.KITKAT;
        try
        {
            osVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return (osVersion >= newVersion);
    }


    public static final String CLASS_SMS_MANAGER = "com.android.internal.telephony.SmsApplication";
    public static final String METHOD_SET_DEFAULT = "setDefaultApplication";

    /**
     * 设置成默认的短信应用
     * @param context
     */
    public void setDefaultSms(Context context) {
        //设置为默认短信应用
        String defaultSmsApp = null;
        String currentPn = context.getPackageName();//获取当前程序包名
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
        {
            defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(context);//获取手机当前设置的默认短信应用的包名
        }
        if (!defaultSmsApp.equals(currentPn))
        {
            try {
                //反射方式不弹框
                Class<?> smsClass = Class.forName(CLASS_SMS_MANAGER);
                Method method = smsClass.getMethod(METHOD_SET_DEFAULT, String.class, Context.class);
                method.invoke(null, context.getPackageName(), this);
            } catch (Exception e) {
                Log.d("MYTAG","++++++++++");
                e.printStackTrace();
                //Dialog方式会弹窗
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, currentPn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

}

