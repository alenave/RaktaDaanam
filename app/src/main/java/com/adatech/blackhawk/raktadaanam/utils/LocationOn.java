package com.adatech.blackhawk.raktadaanam.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.adatech.blackhawk.raktadaanam.R;

/**
 * Created by alenave on 15/02/17.
 */

public class LocationOn implements LocationListener {
    private double latitude, longitude;
    private Location location;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private LocationManager locationManager;
private static Activity activity;
    private static LocationOn instance;

    private LocationOn(){}

    public static LocationOn getInstance(Activity activity) {
        if(instance == null) {
            instance = new LocationOn();
        }
        LocationOn.activity = activity;
        return instance;
    }

    public void check() {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        try {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!isGPSEnabled && !isNetworkEnabled) {
            // notify user
            showDialog(activity);
        } else {

            this.canGetLocation = true;

            if (isNetworkEnabled) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 3, this);
                }
                if (locationManager != null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }

            if (isGPSEnabled){
                if (location == null){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 3, this);
                    if (locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            } else {
                showDialog(activity);
            }

        }

    }

    private void showDialog(final Activity activity){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage(activity.getResources().getString(R.string.gps_network_not_enabled));
        dialog.setPositiveButton(activity.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(myIntent);
                //get gps
            }
        });
        dialog.setNegativeButton(activity.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub

            }
        });
        dialog.show();
    }

    public double getLatitude() {
        check();
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude() {
        check();
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null){
            this.location = location;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
