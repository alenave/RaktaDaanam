package com.adatech.blackhawk.raktadaanam.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.adatech.blackhawk.raktadaanam.R;
import com.adatech.blackhawk.raktadaanam.utils.LocationOn;
import com.adatech.blackhawk.raktadaanam.utils.ProgressDlg;

import org.json.JSONException;
import org.json.JSONObject;

import static com.adatech.blackhawk.raktadaanam.utils.Config.API_URL;
import static com.adatech.blackhawk.raktadaanam.utils.Request.getDonors;

public class NeedBloodGroup extends AppCompatActivity implements View.OnClickListener {

    Button b1;
    Spinner s1;
    static JSONObject jsonObjectOfDonors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needbloodgroup);
        LocationOn.getInstance(this).check();
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
                    double latitude = LocationOn.getInstance(this).getLatitude();
                    double longitude = LocationOn.getInstance(this).getLongitude();
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
                    if (jsonObject != null && jsonObject.getBoolean("success")) {
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
}
