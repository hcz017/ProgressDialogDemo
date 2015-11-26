package com.example.hcz.progressdialogdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    boolean progressComplete = true;
    ProgressDialog sProgressDialog;
    String TAG = "hcz";
    private static final int TASK_COMPLETE = 0;
    private static final int NOT_TASK_COMPLETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Log.i(TAG, "onCreate sProgressDialog: " + sProgressDialog);
        if (savedInstanceState != null) {
            progressComplete = savedInstanceState.getBoolean("progressComplete");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    @Override
    public void onPostResume() {
        super.onPostResume();
        if (!progressComplete) {
            showProgressDialog();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("progressComplete", progressComplete);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == TASK_COMPLETE) {
                progressComplete = true;
                Log.i(TAG, "handler sProgressDialog: " + sProgressDialog);
                dismissDialog();
            }
        }
    };

    public void startProgress() {
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run sProgressDialog: " + sProgressDialog);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "sendEmptyMessage: " + sProgressDialog);
                mHandler.sendEmptyMessage(TASK_COMPLETE);
            }
        }).start();
    }

    public void showProgressDialog() {
        sProgressDialog = new ProgressDialog(this);
        sProgressDialog.setIndeterminate(true);
        sProgressDialog.setMessage("please_wait");
        sProgressDialog.setCancelable(true);
        sProgressDialog.show();
        Log.i(TAG, "showProgressDialog:" + sProgressDialog);
        progressComplete = false;
    }

    private void dismissDialog() {
        Log.i(TAG, "dismissDialog:" + sProgressDialog);
        if (sProgressDialog != null) {
            sProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
