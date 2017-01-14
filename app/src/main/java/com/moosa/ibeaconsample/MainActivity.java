package com.moosa.ibeaconsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mobi.inthepocket.android.beacons.ibeaconscanner.Beacon;
import mobi.inthepocket.android.beacons.ibeaconscanner.Error;
import mobi.inthepocket.android.beacons.ibeaconscanner.IBeaconScanner;

public class MainActivity extends AppCompatActivity {
    private static final String SIR_SYED_CLASS = "e2c56db5-dffb-48d2-b060-d0f5a71096e0";
    private BroadcastReceiver onEnter, onExit, onError;
    private TextView beaconState, currentBeacon;
    private ArrayList<String> historyStrings;
    private ArrayAdapter<String> historyAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView historyList = (ListView) findViewById(R.id.history_list);
        historyStrings = new ArrayList<>();
        historyAdaptor = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,historyStrings);
        historyList.setAdapter(historyAdaptor);
        beaconState = (TextView) findViewById(R.id.display_beacon_state);
        currentBeacon = (TextView) findViewById(R.id.connectedBeacon);
        initBR();
        startService(new Intent(this, BackgroundService.class));
        final Beacon beacon = Beacon.newBuilder()
                .setUUID(SIR_SYED_CLASS)
                .setMajor(0)
                .setMinor(0)
                .build();

        IBeaconScanner.getInstance().startMonitoring(beacon);

    }

    private void initBR() {
        onEnter = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    Beacon beacon = intent.getParcelableExtra("obj");
                    String curBeacon = "Beacon in Range\n"+"UUID: " + beacon.getUUID() + "\n"
                            + "Major: " + beacon.getMajor() + "\n" +
                            "Minor: " + beacon.getMinor() + "";

                    historyStrings.add("Entry Time : "+millisToTime(System.currentTimeMillis()));
                    historyAdaptor.notifyDataSetChanged();
                    beaconState.setText("Welcome To the Mobile Development Class");
                    currentBeacon.setText(curBeacon);
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Something went wrong (onEnter)", Toast.LENGTH_LONG).show();
                }

            }
        };
        onExit = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    Beacon beacon = intent.getParcelableExtra("obj");
                    String curBeacon = "Last Beacon in Range\n"+"UUID: " + beacon.getUUID() + "\n"
                            + "Major: " + beacon.getMajor() + "\n" +
                            "Minor: " + beacon.getMinor() + "";

                    historyStrings.add("Leave Time : "+millisToTime(System.currentTimeMillis()));
                    historyAdaptor.notifyDataSetChanged();
                    beaconState.setText("Good Bye");
                    currentBeacon.setText(curBeacon);
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Something went wrong (onExit)", Toast.LENGTH_LONG).show();
                }
            }
        };
        onError = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    Error error = intent.getParcelableExtra("obj");
                    String err = error.name() + " ";
                    beaconState.setText(err);
                    currentBeacon.setText(" ");
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Something went wrong (onError)", Toast.LENGTH_LONG).show();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(onEnter, new IntentFilter("enter_beacon"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onError, new IntentFilter("error_beacon"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onExit, new IntentFilter("exit_beacon"));

    }
    private String millisToTime(long timeMillis){
        Date date = new Date(timeMillis);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        return formatter.format(date);

    }

}
