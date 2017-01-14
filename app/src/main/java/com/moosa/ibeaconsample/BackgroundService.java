package com.moosa.ibeaconsample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import mobi.inthepocket.android.beacons.ibeaconscanner.Beacon;
import mobi.inthepocket.android.beacons.ibeaconscanner.Error;
import mobi.inthepocket.android.beacons.ibeaconscanner.IBeaconScanner;

public class BackgroundService extends Service implements IBeaconScanner.Callback {
    public BackgroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize
        IBeaconScanner.getInstance().setCallback(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void didEnterBeacon(Beacon beacon) {
        Intent enterIntent = new Intent("enter_beacon");
        enterIntent.putExtra("obj",beacon);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(enterIntent);
    }

    @Override
    public void didExitBeacon(Beacon beacon) {
        Intent enterIntent = new Intent("exit_beacon");
        enterIntent.putExtra("obj",beacon);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(enterIntent);
    }

    @Override
    public void monitoringDidFail(Error error) {
        Intent enterIntent = new Intent("error_beacon");
        enterIntent.putExtra("obj",error);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(enterIntent);
    }
}
