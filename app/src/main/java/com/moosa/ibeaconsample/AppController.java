package com.moosa.ibeaconsample;

import android.app.Application;

import mobi.inthepocket.android.beacons.ibeaconscanner.IBeaconScanner;

/**
 * Created by Moosa on 15/01/2017.
 * moosa.bh@gmail.com
 */

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IBeaconScanner.initialize(IBeaconScanner.newInitializer(this).build());
    }
}
