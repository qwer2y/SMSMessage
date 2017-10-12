package com.example.TestTaxi.Utils.SMS;

/**
 * Created by Chris on 2015/6/8.
 */
public class smsdata {
    private String Tel ;     // 电话号码
    private String body ;    // 短息内容
    private String date ;    // 接收日期
    private String ServiceCenterAddress;    // 短信服务中心
    private int read ;      // 标志是否已读
    private int status ;    // 短信发送还是接收
    private static int smsCounter  = 0 ;   // 统计未读信息数量

    public smsdata ( String Tel, String body ,String date ){
        this.Tel  = Tel ;
        this.body = body ;
        this.date = date ;
    }

    public smsdata ( String Tel, String body ){
        this(Tel,body,null);
    }

    public smsdata ( String Tel ){
        this(Tel,null);
    }

    public smsdata () {
        this(null);
    }

    public String getTel() {
        return Tel;
    }

    public String setTel(String tel) {
        return this.Tel = tel ;
    }

    public String getBody() {
        return body;
    }

    public String setBody(String body) {
        return this.body = body;
    }

    public String getDate() {
        return date;
    }

    public String setDate(String date) {
        return this.date = date ;
    }

    public void setServiceCenterAddress(String serviceCenterAddress) {
        ServiceCenterAddress = serviceCenterAddress;
    }

    public String getServiceCenterAddress() {
        return ServiceCenterAddress;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSmsCounter() {
        return smsCounter;
    }

    public void setSmsCounter(int smsCounter) {
        this.smsCounter = smsCounter;
    }
}
