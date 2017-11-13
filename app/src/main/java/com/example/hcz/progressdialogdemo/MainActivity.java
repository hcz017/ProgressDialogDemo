package com.example.hcz.progressdialogdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hcz.progressdialogdemo.notification.MyNotification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    boolean progressComplete = true;
    private ProgressDialog sProgressDialog;
    private TextView tvOpName;
    private EditText edtPhoneNumber;
    private RadioGroup mRadioGroup;
    private static final String TAG = "MainActivity";
    private static final int TASK_COMPLETE = 0;
    private static final int NOT_TASK_COMPLETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvOpName = (TextView) findViewById(R.id.operator_name);
        Button btn = (Button) findViewById(R.id.button);
        edtPhoneNumber = (EditText) findViewById(R.id.edt_phone_number);

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
        Button rbBtn = (Button) findViewById(R.id.btn_show_radio);
        rbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRadioButtons();
            }
        });

        findViewById(R.id.btn_hide_radio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioGroup != null) {
                    mRadioGroup.removeAllViews();
                }
            }
        });
    }

    private void showRadioButtons() {
        // prepare view
        mRadioGroup = (RadioGroup) findViewById(R.id.sim_cards);
        mRadioGroup.removeAllViews();
        int activeSimCount = 2;
        if (activeSimCount > 1) {
            mRadioGroup.setVisibility(View.VISIBLE);
            mRadioGroup.setOnCheckedChangeListener(null);
            for (int slotId = 0; slotId < activeSimCount; slotId++) {
                String carrierName = getCarrierName(slotId);
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(getString(R.string.use_data, carrierName));
                mRadioGroup.addView(radioButton, RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT);
            }

            // check default data slot
//            int defaultDataSubId = SubscriptionManager.getDefaultDataSubId();
//            int defaultDataSlotId = SubscriptionManager.getSlotId(defaultDataSubId);
            RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(1);
            radioButton.setChecked(true);

            // set checkListener
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for (int slotId = 0; slotId < 2; slotId++) {
                        if ((mRadioGroup.getChildAt(slotId).getId() == checkedId)) {
                            Log.d(TAG, "onCheckedChanged: slotId: " + slotId);
                            Toast.makeText(MainActivity.this, "check " + slotId, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private String getCarrierName(int slotId) {
        String[] carrierNames= {"CMCC","CU"};
        return carrierNames[slotId];
    }

    /**
     * play sound
     */
    private SoundPool mSoundPool;
    private int mRefocusSound;
    private static final String NOTI_MUSIC_PATH = "/system/media/audio/notifications/pixiedust.ogg";

    public void playSound(View view) {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);
        mRefocusSound = mSoundPool.load(NOTI_MUSIC_PATH, 1);
        mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
                mSoundPool.play(mRefocusSound, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        });
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

    /**
     * show ProgressDialog
     */
    public void startProgress() {
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run sProgressDialog: " + sProgressDialog);
                try {
                    Thread.sleep(3000);
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
        Log.d(TAG, "showProgressDialog: " + getSupportActionBar());
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

    public void TestNotificationExtra(View view) {
        Intent intent = new Intent(this, MyNotification.class);
        startActivity(intent);
    }

    //java reflect to access hide methods.
    public void getNetworkOperatorName(View view) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // get defaultDataSubId  @hide
        Class subscriptionManager;
        subscriptionManager = Class.forName("android.telephony.SubscriptionManager");
        Method getDefaultDataSubId = subscriptionManager.getMethod("getDefaultDataSubId");
        Object defaultDataSubId = getDefaultDataSubId.invoke(null);
        // to get getNetworkOperatorName(subId), @hide
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        Class telephonyManager;
        telephonyManager = Class.forName("android.telephony.TelephonyManager");
        Method getNetworkOperatorName = telephonyManager.getMethod("getNetworkOperatorName", int.class);
        getNetworkOperatorName.setAccessible(true);
        Object operatorName = getNetworkOperatorName.invoke(tm, defaultDataSubId);
        tvOpName.setText((String) operatorName);
        Log.i(TAG, "default data sub network operator name : " + operatorName);
    }

    public void dial(View view) {
        if (!TextUtils.isEmpty(edtPhoneNumber.getText())) {
            String phoneNumber = edtPhoneNumber.getText().toString();
            TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
            Uri uri = Uri.fromParts("tel", phoneNumber, null);//Uri 主要是号码
            Bundle extras = new Bundle();
            extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true);//默认开扬声器

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, 123);
            } else {
                telecomManager.placeCall(uri, extras);
            }
        }
    }
    public void fragmentTest(View view){
        Intent intent = new Intent(this, MyFragmentActivityB.class);
        startActivity(intent);
    }
}
