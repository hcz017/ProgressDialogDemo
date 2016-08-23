package com.example.hcz.progressdialogdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MyNotification extends AppCompatActivity {

    private EditText mEdtTargetAC;
    public String INTENT_EXTRA = "intentExtra";
    int mNotificationId = 001;
    // Gets an instance of the NotificationManager service
    public NotificationManager mNotifyMgr;
    private static final String TAG = "MyNotification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_noti);
        mEdtTargetAC = (EditText) findViewById(R.id.edt_target_AC);
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public void sendNotification(View view) {
        //1.Create a Notification Builder
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setAutoCancel(true)
                        .setContentText("Hello World!");
        //2.Define the Notification's Action
        Intent resultIntent = new Intent(this, SecondActivity.class);
        //如果TextView里面有内容，那么就往intent里面放一个int值1。
        // 多次操作，把不同的值put进去，之后检查有没有更新
        int intentExtra = TextUtils.isEmpty(mEdtTargetAC.getText()) ? 0 : 1;
        resultIntent.putExtra(INTENT_EXTRA, intentExtra);
        Log.i(TAG, "sendNotification: putExtra: " + intentExtra);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT//这个flag保证了intent里的Extra的值可以更新
                );

        //3.Set the Notification's Click Behavior
        mBuilder.setContentIntent(resultPendingIntent);
        //4.Issue the Notification
        // Sets an ID for the notification
        /*int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);*/
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public void cancelNotification() {
        mNotifyMgr.cancel(mNotificationId);
    }

    //分别用Notification和startActivity的方式检验Intent里Extra值的传递。
    public void StartTargetActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        int intentExtra = TextUtils.isEmpty(mEdtTargetAC.getText()) ? 0 : 1;
        intent.putExtra(INTENT_EXTRA, intentExtra);
        startActivity(intent);
    }
}
