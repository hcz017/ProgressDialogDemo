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
    String TAG = "ckt";
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

        Log.i(TAG, "ckt sProgressDialog: " + sProgressDialog);
        if (savedInstanceState != null) {
            progressComplete = savedInstanceState.getBoolean("progressComplete");
        }
        Log.i(TAG, "ckt progressComplete: " + progressComplete);

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

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(NOT_TASK_COMPLETE);
                }
            }).start();

//            mHandler.sendEmptyMessage(NOT_TASK_COMPLETE);
//            showProgressDialog();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("progressComplete", progressComplete);
        Log.i(TAG, "ckt saved progressComplete: " + progressComplete);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == NOT_TASK_COMPLETE) {
                sProgressDialog.show();
            }
            if (msg.what == TASK_COMPLETE) {
                progressComplete = true;
                Log.i(TAG, "ckt handler progressComplete: " + progressComplete);
                Log.i(TAG, "ckt handler sProgressDialog: " + sProgressDialog);
                dismissDialog();
            }
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(TASK_COMPLETE);
        }
    };
    Thread thread = new Thread(runnable);

    public void startProgress() {
        showProgressDialog();
        thread.start();
    }

    public void showProgressDialog() {
        sProgressDialog = new ProgressDialog(this);
        sProgressDialog.setIndeterminate(true);
        sProgressDialog.setMessage("please_wait");
        sProgressDialog.setCancelable(false);
        sProgressDialog.show();
        Log.i(TAG, "ckt showProgressDialog:" + sProgressDialog);
        progressComplete = false;
    }

    private void dismissDialog() {
        Log.i(TAG, "ckt dismissDialog:" + sProgressDialog);
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
