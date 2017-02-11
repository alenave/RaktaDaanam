package com.hack.blackhawk.raktadaanam.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.hack.blackhawk.raktadaanam.R;

import com.hack.blackhawk.raktadaanam.utils.GPSTracker;

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

        Spinner dropdown = (Spinner) findViewById(R.id.needbloodGroup);
        String[] items = new String[]{"--Select--", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setSelection(0);
        dropdown.setAdapter(adapter);
        dropdown.setDropDownWidth(getWindowManager().getDefaultDisplay().getWidth());
        b1 = (Button) findViewById(R.id.needContinue);
        s1 = (Spinner) findViewById(R.id.needbloodGroup);
        b1.setOnClickListener(this);
    }


    public void postCall(JSONObject peopleObj) {

        new AsyncTask<JSONObject, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(JSONObject... params) {
                jsonObjectOfDonors = getDonors(params[0], API_URL + "get_donors.json");

                return null;
            }

        }.execute(peopleObj, null, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.needContinue:
                Intent intent = new Intent(NeedBloodGroup.this, MapActivity.class);
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
                    startActivity(intent);
                }
                break;
        }
    }
}
