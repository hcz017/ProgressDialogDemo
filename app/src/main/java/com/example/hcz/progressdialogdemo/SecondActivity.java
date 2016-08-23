package com.example.hcz.progressdialogdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    public String INTENT_EXTRA = "intentExtra";
    private static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        TextView textView = (TextView) findViewById(R.id.textView2);
        //取出intent里面的Extra，看里面的值有没有更新
        int intentExtra = getIntent().getIntExtra(INTENT_EXTRA, -1);
        Log.i(TAG, "sendNotification: putExtra: " + intentExtra);

        textView.setText("intentExtra: " + intentExtra);
    }
}
