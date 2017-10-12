package com.example.TestTaxi.Utils.SMS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/6/5.
 */
public class SmsDatabase extends SQLiteOpenHelper {
    public SmsDatabase(Context context) {
        super(context, "SmsSql.db", null, 1);
    }

    /**
     * sms table :      短信数据库表项；
     *      telphone ：电话号码：
     *      body : 短信内容：
     *      date ：短信日期；
     *      status 0:接收 1:发送；
     *      read  0:未读  1:已读；
     *      ServiceCenterAddress
     * @param db  数据库
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
         final String sql = "create table sms(" +
                 "id integer primary Key autoincrement ," +
                 "telphone verchar(14)," +
                 "body verchar(1024)," +
                 "date verchar(14)," +
                 "status integer defualt null ," +
                 "read integer defualt null ," +
                 "ServiceCenterAddress verchar(14))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String sql = "drop table if exists sms";
        db.execSQL(sql);
        this.onCreate(db);
    }
}
