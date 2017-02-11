package com.hack.blackhawk.raktadaanam.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hack.blackhawk.raktadaanam.R;
import com.hack.blackhawk.raktadaanam.models.Location;
import com.hack.blackhawk.raktadaanam.models.People;
import com.hack.blackhawk.raktadaanam.utils.CustomDate;
import com.hack.blackhawk.raktadaanam.utils.GPSTracker;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.widget.Toast.LENGTH_SHORT;
import static com.hack.blackhawk.raktadaanam.utils.Config.API_URL;
import static com.hack.blackhawk.raktadaanam.utils.Request.post;


public class HistoryPopup extends AppCompatActivity implements View.OnClickListener {
    private People people = new People();
    private double latitude;
    private double longitude;
    EditText lastDanationDate;
    Calendar calendar;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historypopup);
        Intent i = getIntent();
        people = (People) getIntent().getSerializableExtra("People");
        Button b1 = (Button) findViewById(R.id.donorContinue);
        b1.setOnClickListener(this);
        lastDanationDate = (EditText) findViewById(R.id.input_donationDate);
        //Date picker code start
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //Date picker code end
    }

    private boolean checkDob(String date) {
        return CustomDate.checkDateDDmmYYYY(date) ? true : false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.donorContinue:
                GPSTracker gps = new GPSTracker(this);
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                if (latitude > 1 && longitude > 1) {

//                    EditText e1 = (EditText) findViewById(R.id.input_donationDate);
//                    String lastDonate = e1.getText().toString();
//                    Toast.makeText(getApplicationContext(), latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                    String lastDonate = lastDanationDate.getText().toString();
                    Date last_donation_date = new Date();
                    if ("".equalsIgnoreCase(lastDonate) || !checkDob(lastDonate)) {
                        Date today = new Date();
                        Calendar cal = new GregorianCalendar();
                        cal.setTime(today);
                        cal.add(Calendar.DAY_OF_MONTH, -56);
                        last_donation_date = cal.getTime();

                    } else {
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            last_donation_date = df.parse(lastDonate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    people.setLastDonationDate(last_donation_date);
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
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String jsonInString = gson.toJson(people);
        return jsonInString;

    }


    public void postCall(String peopleObj) {

        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(String... params) {
                post(params[0], API_URL + "donors.json");

                return null;
            }

        }.execute(peopleObj, null, null);
    }

    //Code for datePicker start
    @SuppressWarnings("deprecation")
    public void setLastDonationDate(View view) {
        showDialog(999);
//        Toast.makeText(getApplicationContext(), "ca", LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        lastDanationDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    //code for datePicker end

}