# SMSMessage

适用于Android 4.4之后需要设置成默认短信应用
Manifest中需要声明的:

<receiver
android:name=".Utils.SMS.SmsReceiver"
android:permission="android.permission.BROADCAST_SMS">
<intent-filter>
<action android:name="android.provider.Telephony.SMS_DELIVER"/>
<action android:name="android.provider.Telephony.SMS_RECEIVED"/>
</intent-filter>
</receiver>
<receiver
android:name=".Utils.SMS.MmsReceiver"
android:permission="android.permission.BROADCAST_WAP_PUSH">
<intent-filter>
<action android:name="android.provider.Telephony.WAP_PUSH_DELIVER"/>

<data android:mimeType="application/vnd.wap.mms-message"/>
</intent-filter>
</receiver>

<service android:name=".Utils.SMS.HeadlessSmsSendService"
android:permission="android.permission.SEND_RESPOND_VIA_MESSAGE"
android:exported="true" >
<intent-filter>
<action android:name="android.intent.action.RESPOND_VIA_MESSAGE" />
<category android:name="android.intent.category.DEFAULT" />
<data android:scheme="sms" />
<data android:scheme="smsto" />
<data android:scheme="mms" />
<data android:scheme="mmsto" />
</intent-filter>
</service>


主Activity中需要加上:
<action android:name="android.intent.action.SEND"/>
<action android:name="android.intent.action.SENDTO"/>

<category android:name="android.intent.category.DEFAULT"/>
<category android:name="android.intent.category.BROWSABLE"/>

<data android:scheme="sms"/>
<data android:scheme="smsto"/>
<data android:scheme="mms"/>
<data android:scheme="mmsto"/>

使用：
//设置成默认短信应用
SmsTools.getInstance(this).setDefaultSms(this);
//在SMSReciver中就可以收到系统发来的短信广播


注：

4.4 之前：

	* 新接收短信广播 SMS_RECEIVED_ACTION 为有序广播。任意应用可接到该广播并中止其继续传播。中止后优先级低的短信应用和系统短信服务将不知道新短信到达，从而不写进数据库。这样就做到了拦截（其实很多恶意应用也这么干）。
	* 任意应用都可以操作短信数据库，包括新建（含伪造收件箱和发件箱短信）、修改（含篡改历史短信）、删除。
	* 任意应用都可以发送短信和彩信，但默认不写进短信数据库，除非应用手动存入，否则用户是看不到的（配合拦截就可以安静地吸费了）。

4.4 及之后：

	* 设立默认短信应用机制，成为默认短信后的应用将全面接管（替代）系统短信服务。与设置默认浏览器类似，成为默认短信应用需要向用户申请。
	* 新接收短信广播 SMS_RECEIVED_ACTION 更改为无序广播，增加只有默认短信应用能够接收的广播 SMS_DELIVER_ACTION。二者的不同在于，当默认短信应用收到 SMS_DELIVER_ACTION 时它要负责将其存入数据库。任意应用仍然可以接收到 SMS_RECEIVED_ACTION 广播但不能将其中止。因此所有的应用和系统短信服务都可以接收到新短信，没有应用能够再用中止广播的方式拦截短信。
	* 只有默认短信应用可以操作短信数据库，包括新建（含伪造收件箱和发件箱短信）、修改（含篡改历史短信）和删除。其它应用只能读取短信数据库。默认短信应用需要在发送短信、收到新短信之后手动写入系统短信数据库，否则其它应用将读取不到该条短信。默认短信应用可以通过控制不写入数据库的方式拦截短信。
	* 任意应用仍然都可以发送短信，但默认短信应用以外的应用发短信的接口底层改为调用系统短信服务，而不再直接调用驱动通信，因此其所发短信会被系统短信服务自动转存数据库。此外，只有默认短信应用可以发送彩信。能不能发短信、发短信会不会被存入数据库，这一点安卓开发团队的博客发生过改动，导致网上出现了很多矛盾的说法。以上结论是自行验证所得。

共同点：

	* 任意应用都可以接收到短信数据库发生变化的广播，并可以读取全部短信数据库。
	* 任意应用都可以发送短信并接收短信回执。

