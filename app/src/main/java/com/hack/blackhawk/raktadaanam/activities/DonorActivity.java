package com.hack.blackhawk.raktadaanam.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hack.blackhawk.raktadaanam.R;
import com.hack.blackhawk.raktadaanam.models.People;
import com.hack.blackhawk.raktadaanam.utils.GPSTracker;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.hack.blackhawk.raktadaanam.utils.Request.post;


public class DonorActivity extends AppCompatActivity implements View.OnClickListener {

    Button b1;
    EditText userName, password, mobile_number, dateOfBirth;
    RadioGroup r1;
    Spinner s1;
    String name, pass, dob, gender, bg;
    long mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        Spinner dropdown = (Spinner) findViewById(R.id.blood_group);
        String[] items = new String[]{"--Select--", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setSelection(0);
        dropdown.setAdapter(adapter);


        userName = (EditText) findViewById(R.id.input_name);
        password = (EditText) findViewById(R.id.input_password);
        mobile_number = (EditText) findViewById(R.id.mobile_no);
        dateOfBirth = (EditText) findViewById(R.id.input_dob);
        r1 = (RadioGroup) findViewById(R.id.radioGrp);
        s1 = (Spinner) findViewById(R.id.blood_group);

        b1 = (Button) findViewById(R.id.donorRegistration);
        b1.setOnClickListener(this);

        TextView t1 = (TextView) findViewById(R.id.alredyDonor);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonorActivity.this, LoginActivity.class);
                startActivity(intent, null);
            }
        });
    }



    private boolean checkDob(String date) {
        return date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})") ? true : false;
    }

    private boolean checkMobile(String mobile) {
        return mobile.matches("^[7-9][0-9]{9}$") ? true : false;
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.donorRegistration:
                boolean sendRequest = false;
                //check any empty entry
                if ("".equalsIgnoreCase(userName.getText().toString()) || "".equalsIgnoreCase(password.getText().toString())
                        || "".equalsIgnoreCase(mobile_number.getText().toString()) || "".equalsIgnoreCase(dateOfBirth.getText().toString()) || !checkMobile(mobile_number.getText().toString())) {
                    sendRequest = false;
                } else {
                    name = userName.getText().toString();
                    pass = password.getText().toString();
                    dob = dateOfBirth.getText().toString();
                    int selectedGenderId = r1.getCheckedRadioButtonId();
                    RadioButton genderButton = (RadioButton) findViewById(selectedGenderId);
                    gender = genderButton.getText().toString();
                    bg = s1.getSelectedItem().toString();
                    mobile = Long.parseLong(mobile_number.getText().toString());
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
                    People people = new People(name, mobile, bg, dob, gender, pass);

                    intent.putExtra("People", people);
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
                break;
        }


    }
}
