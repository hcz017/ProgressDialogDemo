package com.example.hcz.progressdialogdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.v4.app.ActivityCompat;
import android.telecom.TelecomManager;
import android.util.Log;

public class QuickSettingService extends TileService {

    private static final String TAG = "QuickSettingService";

    @Override
    public void onTileAdded() {
        Log.d(TAG, "onTileAdded");
    }

    @Override
    public void onTileRemoved() {
        Log.d(TAG, "onTileRemoved");
    }

    @Override
    public void onClick() {
        int state = getQsTile().getState();
        Log.d(TAG, "onClick state = " + Integer.toString(getQsTile().getState()));
        Icon icon;
        if (state == Tile.STATE_INACTIVE) {
            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_phone);
            getQsTile().setState(Tile.STATE_ACTIVE);
        } else {
            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_phone);
            getQsTile().setState(Tile.STATE_INACTIVE);
        }
        dial();
        getQsTile().setIcon(icon);
        getQsTile().updateTile();
    }

    @Override
    public void onStartListening() {
        Log.d(TAG, "onStartListening");
    }

    @Override
    public void onStopListening() {
        Log.d(TAG, "onStopListening");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    public void dial() {
        if (true) {
            String phoneNumber = "10086";
            TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
            Uri uri = Uri.fromParts("tel", phoneNumber, null);//Uri 主要是号码
            Bundle extras = new Bundle();
            extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true);//默认开扬声器

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.CALL_PHONE}, 123);
            } else {
                telecomManager.placeCall(uri, extras);
            }
        }
    }
}