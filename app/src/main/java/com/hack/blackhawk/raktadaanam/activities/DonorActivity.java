package com.hack.blackhawk.raktadaanam.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.hack.blackhawk.raktadaanam.R;


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
        Spinner dropdown = (Spinner)findViewById(R.id.bloodGroup);
        String[] items = new String[]{"--Select--","A+", "A-", "B+","B-", "O+", "O-","AB+","AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setSelection(0);
        dropdown.setAdapter(adapter);

        e1 = (EditText)findViewById(R.id.input_name);
        e2 = (EditText)findViewById(R.id.input_password);
        e3 = (EditText)findViewById(R.id.mobile_no);
        e4 = (EditText)findViewById(R.id.input_dob);
        r1 = (RadioGroup)findViewById(R.id.radioGrp);
        s1 = (Spinner)findViewById(R.id.bloodGroup);

        b1 = (Button)findViewById(R.id.donorRegistration);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean sendRequest = false;
                //check any empty entry
                if ("".equalsIgnoreCase(e1.getText().toString())|| "".equalsIgnoreCase(e2.getText().toString())
                        || "".equalsIgnoreCase(e3.getText().toString()) || "".equalsIgnoreCase(e4.getText().toString()) || !checkMobile(e3.getText().toString())) {
                    sendRequest = false;
                } else {
                    name = e1.getText().toString();
                    pass = e2.getText().toString();
                    dob = e4.getText().toString();
                    int selectedGenderId = r1.getCheckedRadioButtonId();
                    RadioButton genderButton = (RadioButton)findViewById(selectedGenderId);
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
                /*Log.d("name", name);
                Log.d("pass", pass);
                Log.d("mobile", ""+mobile);
                Log.d("dob", dob);
                Log.d("gender", gender);
                Log.d("bg", bg);*/

                if (sendRequest) {
                    //success!
                    AlertDialog.Builder builder = new AlertDialog.Builder(DonorActivity.this);
                    builder.setMessage("Doner request send")
                            .setTitle("Doner request send")
                            .setPositiveButton("Ok", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
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
    }

    private boolean checkDob(String date) {
        return date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})") ? true : false;
    }
    private boolean checkMobile(String mobile) {
        return mobile.matches("^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$") ? true : false;
    }
}
