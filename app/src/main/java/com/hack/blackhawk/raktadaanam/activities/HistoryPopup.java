package com.hack.blackhawk.raktadaanam.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.hack.blackhawk.raktadaanam.MainActivity;
import com.hack.blackhawk.raktadaanam.R;
import com.hack.blackhawk.raktadaanam.models.Location;
import com.hack.blackhawk.raktadaanam.models.People;
import com.hack.blackhawk.raktadaanam.utils.CustomDate;
import com.hack.blackhawk.raktadaanam.utils.GPSTracker;
import com.hack.blackhawk.raktadaanam.utils.ProgressDlg;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.widget.Toast.LENGTH_SHORT;
import static com.hack.blackhawk.raktadaanam.utils.Config.API_URL;
import static com.hack.blackhawk.raktadaanam.utils.Request.post;



public class HistoryPopup extends AppCompatActivity implements View.OnClickListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private People people = new People();
    private double latitude;
    private double longitude;
    EditText lastDanationDate;
    Calendar calendar;
    int year, month, day;
    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog ;


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
        lastDanationDate.setOnClickListener(this);
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
                    postCall(requestBody);
                }
                break;
            case R.id.input_donationDate :
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(HistoryPopup.this, year, month, day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);

                datePickerDialog.setAccentColor(Color.parseColor("#009688"));

                datePickerDialog.setTitle("last donated");

                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
                break;
        }
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int Year, int Month, int Day) {
        setDate(Day, Month, Year);
    }

    private String createJSONBody(People people) {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String jsonInString = gson.toJson(people);
        return jsonInString;

    }


    public void postCall(String peopleObj) {

        new AsyncTask<String, Void, JSONObject>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ProgressDlg.showProgressDialog(HistoryPopup.this, "Please wait...");
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                return post(params[0], API_URL + "donors.json");
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if(jsonObject.getBoolean("success")) {
                        ProgressDlg.hideProgressDialog();
                        Intent intent = new Intent(HistoryPopup.this, MainActivity.class);
                        Toast.makeText(getApplicationContext(), "Successfully registered as donor", LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(peopleObj, null, null);
    }


    private void setDate(int day, int month, int year) {
        lastDanationDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    //code for datePicker end

}