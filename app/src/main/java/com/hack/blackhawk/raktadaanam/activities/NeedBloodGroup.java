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

import com.hack.blackhawk.raktadaanam.MainActivity;
import com.hack.blackhawk.raktadaanam.R;
import com.hack.blackhawk.raktadaanam.utils.GPSTracker;

import static com.hack.blackhawk.raktadaanam.utils.Config.API_URL;
import static com.hack.blackhawk.raktadaanam.utils.Request.post;

/**
 * Created by dharmendra on 18/12/16.
 */

public class NeedBloodGroup extends AppCompatActivity implements View.OnClickListener {

    Button b1;
    Spinner s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_blood_group);
        Spinner dropdown = (Spinner) findViewById(R.id.needbloodGroup);
        String[] items = new String[]{"--Select--", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setSelection(0);
        dropdown.setAdapter(adapter);
        b1 = (Button) findViewById(R.id.needContinue);
        s1 = (Spinner) findViewById(R.id.needbloodGroup);
        b1.setOnClickListener(this);
    }


    public void postCall(String peopleObj) {

        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                ProgressDlg.showProgressDialog(getContext(), "प्रतीक्षा करें...");
            }

            @Override
            protected Void doInBackground(String... params) {
//                try {
//                    JsonParser jsonParser = new JsonParser();
//                    JSONObject feedObject = jsonParser.parsing(Config.API_URL + "pics.json");
//                    return feedObject;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                post(params[0], "https://blooming-plateau-54995.herokuapp.com/donors.json");
                post(params[0], API_URL + "get_donors.json");

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
                    //add code here
//                    intent.putExtra("blood_group", bg);
                    GPSTracker gps = new GPSTracker(this);
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    String jsonString = "{\"lat\":" + latitude + ",\"lng\":" + longitude + ",\"blood_group\":" + bg + "}";
//                    Toast.makeText(getApplicationContext(), latitude + " " + longitude, Toast.LENGTH_SHORT).show();

                    postCall(jsonString);
                    startActivity(intent);
                }
                break;
        }
    }
}
