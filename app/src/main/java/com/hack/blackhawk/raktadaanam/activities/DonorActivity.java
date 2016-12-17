package com.hack.blackhawk.raktadaanam.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.hack.blackhawk.raktadaanam.R;
import com.hack.blackhawk.raktadaanam.models.People;
import com.hack.blackhawk.raktadaanam.utils.GPSTracker;
import com.hack.blackhawk.raktadaanam.utils.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.hack.blackhawk.raktadaanam.utils.Request.post;


public class DonorActivity extends AppCompatActivity {

    Button b1;
    EditText e1, e2, e3, e4;
    RadioGroup r1;
    Spinner s1;
    String name, pass, dob, gender, bg;
    long mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        Spinner dropdown = (Spinner) findViewById(R.id.bloodGroup);
        String[] items = new String[]{"--Select--", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setSelection(0);
        dropdown.setAdapter(adapter);
//        GPSTracker gps = new GPSTracker(getApplicationContext());
//        double latitude = gps.getLatitude();
//        double longitude= gps.getLongitude();
//
//        Toast.makeText(getApplicationContext(), latitude + " " + longitude, Toast.LENGTH_SHORT).show();

        e1 = (EditText) findViewById(R.id.input_name);
        e2 = (EditText) findViewById(R.id.input_password);
        e3 = (EditText) findViewById(R.id.mobile_no);
        e4 = (EditText) findViewById(R.id.input_dob);
        r1 = (RadioGroup) findViewById(R.id.radioGrp);
        s1 = (Spinner) findViewById(R.id.bloodGroup);

        b1 = (Button) findViewById(R.id.donorRegistration);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean sendRequest = false;
                //check any empty entry
                if ("".equalsIgnoreCase(e1.getText().toString()) || "".equalsIgnoreCase(e2.getText().toString())
                        || "".equalsIgnoreCase(e3.getText().toString()) || "".equalsIgnoreCase(e4.getText().toString()) || !checkMobile(e3.getText().toString())) {
                    sendRequest = false;
                } else {
                    name = e1.getText().toString();
                    pass = e2.getText().toString();
                    dob = e4.getText().toString();
                    int selectedGenderId = r1.getCheckedRadioButtonId();
                    RadioButton genderButton = (RadioButton) findViewById(selectedGenderId);
                    gender = genderButton.getText().toString();
                    bg = s1.getSelectedItem().toString();
                    mobile = Long.parseLong(e3.getText().toString());
                    sendRequest = true;
                }
                //check present blood group
                if (sendRequest && bg.equalsIgnoreCase("--Select--")) {
                    sendRequest = false;
                }
                //check if date is correct
                if (sendRequest && !checkDob(dob)) {
                    sendRequest = false;
                }
                if (sendRequest) {
                    Intent intent = new Intent(DonorActivity.this, HistoryPopup.class);
                    //To pass: obj to be bean class
                    //intent.putExtra("MyClass", obj);
                    startActivity(intent, null);


                } else {
                    //fail!
                    AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
                    builder.setMessage("Error")
                            .setTitle("Please fill all the entries correctly")
                            .setPositiveButton("Ok", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

        TextView t1 = (TextView)findViewById(R.id.alredyDonor);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonorActivity.this, LoginActivity.class);
                startActivity(intent, null);
            }
        });
    }

    private boolean checkDob(String date) {
//        return date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})") ? true : false;
        return true;
    }

    private boolean checkMobile(String mobile) {
//        return mobile.matches("^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$") ? true : false;
        return true;
    }


    public void postCall(JSONObject peopleObj) {

        new AsyncTask<JSONObject, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                ProgressDlg.showProgressDialog(getContext(), "प्रतीक्षा करें...");
            }

            @Override
            protected Void doInBackground(JSONObject... params) {
//                try {
//                    JsonParser jsonParser = new JsonParser();
//                    JSONObject feedObject = jsonParser.parsing(Config.API_URL + "pics.json");
//                    return feedObject;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                post(params[0]);
                return null;
            }

//            @Override
//            protected void onPostExecute(Void) {
//                super.onPostExecute(Void);
////                try {
////                    if (feedObject != null) {
//////                        firstLoading.setVisibility(View.GONE);
////                        feedList.clear();
////                        JsonParser jsonParser = new JsonParser();
////                        feedList.addAll(jsonParser.fetchPicDetails(feedObject));
////                        Collections.reverse(feedList);
////
////                        firstLoading.setVisibility(View.GONE);
////                        adapter.notifyDataSetChanged();
////                    }
//////                        ProgressDlg.hideProgressDialog();
////                } catch (Exception e)
////
////                {
////                    e.printStackTrace();
////                }
//                return null;
//            }


        }.execute(peopleObj, null, null);
    }


}
