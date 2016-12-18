package com.hack.blackhawk.raktadaanam.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hack.blackhawk.raktadaanam.R;
import com.hack.blackhawk.raktadaanam.models.Location;
import com.hack.blackhawk.raktadaanam.models.People;
import com.hack.blackhawk.raktadaanam.utils.GPSTracker;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.hack.blackhawk.raktadaanam.utils.Request.post;


public class HistoryPopup extends AppCompatActivity implements View.OnClickListener {
    private People people =  new People();
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historypopup);
        Intent i = getIntent();
        people = (People) getIntent().getSerializableExtra("People");
        Button b1 = (Button) findViewById(R.id.donorContinue);
        b1.setOnClickListener(this);
    }

    private boolean checkDob(String date) {
        return date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})") ? true : false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.donorContinue:
                GPSTracker gps = new GPSTracker(this);
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                if (latitude > 1 && longitude > 1) {
                    Toast.makeText(getApplicationContext(), latitude + " " + longitude, Toast.LENGTH_SHORT).show();

                    EditText e1 = (EditText) findViewById(R.id.input_donationDate);
                    String lastDonate = e1.getText().toString();
                    Date lastDonationDate = new Date();
                    if ("".equalsIgnoreCase(lastDonate) || !checkDob(lastDonate)) {
                        Date today = new Date();
                        Calendar cal = new GregorianCalendar();
                        cal.setTime(today);
                        cal.add(Calendar.DAY_OF_MONTH, -56);
                        lastDonationDate = cal.getTime();

                    } else {
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            lastDonationDate = df.parse(lastDonate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    people.setLastDonationDate(lastDonationDate);
                    Location location = new Location(latitude, longitude);
                    people.setLocation(location);
                    String requestBody = createJSONBody(people);
//get location
//success!
                    AlertDialog.Builder builder = new AlertDialog.Builder(HistoryPopup.this);
                    builder.setMessage("Doner request send")
                            .setTitle("Doner request send")
                            .setPositiveButton("Ok", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    postCall(requestBody);
                }
                break;
        }
    }

    private String createJSONBody(People people) {
        Gson gson = new Gson();
        String jsonInString = gson.toJson(people);
        return jsonInString;

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