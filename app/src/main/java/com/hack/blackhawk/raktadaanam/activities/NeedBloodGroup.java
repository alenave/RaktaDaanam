package com.hack.blackhawk.raktadaanam.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.hack.blackhawk.raktadaanam.R;

import com.hack.blackhawk.raktadaanam.utils.GPSTracker;
import com.hack.blackhawk.raktadaanam.utils.ProgressDlg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.hack.blackhawk.raktadaanam.utils.Config.API_URL;
import static com.hack.blackhawk.raktadaanam.utils.Request.getDonors;
import static com.hack.blackhawk.raktadaanam.utils.Request.post;

public class NeedBloodGroup extends AppCompatActivity implements View.OnClickListener {

    Button b1;
    Spinner s1;
    static JSONObject jsonObjectOfDonors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needbloodgroup);
        checkLocationOn();
        Spinner dropdown = (Spinner) findViewById(R.id.needbloodGroup);
        String[] items = new String[]{"--Blood Group--", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setSelection(0);
        dropdown.setAdapter(adapter);
        dropdown.setDropDownWidth(getWindowManager().getDefaultDisplay().getWidth());
        b1 = (Button) findViewById(R.id.needContinue);
        s1 = (Spinner) findViewById(R.id.needbloodGroup);
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.needContinue:
                String bg = s1.getSelectedItem().toString();
                if (!bg.equalsIgnoreCase("--Select--")) {
                    GPSTracker gps = new GPSTracker(this);
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
//                    Toast.makeText(getApplicationContext(), latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                    JSONObject reqBody = new JSONObject();
                    try {
                        reqBody.put("lat", latitude);
                        reqBody.put("lng", longitude);
                        reqBody.put("blood_group", bg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    postCall(reqBody);
                }
                break;
        }
    }

    public void postCall(JSONObject peopleObj) {

        new AsyncTask<JSONObject, Void, JSONObject>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ProgressDlg.showProgressDialog(NeedBloodGroup.this, "Please wait...");
            }

            @Override
            protected JSONObject doInBackground(JSONObject... params) {
                return getDonors(params[0], API_URL + "get_donors.json");
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if (jsonObject.getBoolean("success")) {
                        ProgressDlg.hideProgressDialog();
                        jsonObjectOfDonors = jsonObject;
                        Intent intent = new Intent(NeedBloodGroup.this, MapActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(peopleObj, null, null);
    }


    private void checkLocationOn() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
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
            android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
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
}
