package com.hack.blackhawk.raktadaanam;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.vision.text.Line;
import com.hack.blackhawk.raktadaanam.activities.DonorActivity;
import com.hack.blackhawk.raktadaanam.activities.MapActivity;
import com.hack.blackhawk.raktadaanam.activities.NeedBloodGroup;
import com.hack.blackhawk.raktadaanam.utils.InternetConnection;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button b1, b2;
    private LocationManager lm;
    private LinearLayout retry;
    private ScrollView fullView;
    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initViews();
        permission();
        if (!InternetConnection.isInternetConnected(getApplicationContext())) {
            fullView.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        } else {
            checkLocationOn();
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DonorActivity.class);
                    startActivity(intent, null);
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, NeedBloodGroup.class);
                    startActivity(intent, null);
                }
            });

        }

    }

    private void initViews() {
        fullView = (ScrollView) findViewById(R.id.full_view);
        retryButton = (Button) findViewById(R.id.retry_button);
        retry = (LinearLayout) findViewById(R.id.retry);
        b1 = (Button) findViewById(R.id.doner);
        b2 = (Button) findViewById(R.id.reciver);
        retryButton.setOnClickListener(this);
    }


    private void checkLocationOn() {
        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getApplicationContext().getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getApplicationContext().getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(getApplicationContext().getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retry_button:
                if (InternetConnection.isInternetConnected(getApplicationContext())) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                break;
        }
    }


    private void permission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.INTERNET},
                    1);
        }

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE}, 1);
        }
    }
}
