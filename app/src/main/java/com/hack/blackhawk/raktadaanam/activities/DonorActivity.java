package com.hack.blackhawk.raktadaanam.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.hack.blackhawk.raktadaanam.R;
import com.hack.blackhawk.raktadaanam.models.People;
import com.hack.blackhawk.raktadaanam.utils.LocationOn;
import com.hack.blackhawk.raktadaanam.utils.CustomDate;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DonorActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Button b1;
    EditText userName, password, mobile_number;
    EditText dateOfBirth;
    RadioGroup r1;
    Spinner s1;
    String name, pass, dob, gender, bg;
    long mobile;
    Calendar calendar;
    int year, month, day;
    DatePickerDialog datePickerDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        LocationOn.getInstance(this).check();
        Spinner dropdown = (Spinner) findViewById(R.id.blood_group);
        String[] items = new String[]{"--Blood Group--", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setSelection(0);
        dropdown.setAdapter(adapter);
        dropdown.setDropDownWidth(getWindowManager().getDefaultDisplay().getWidth());

        //Date picker code start
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR) - 18;

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //Date picker code end



        userName = (EditText) findViewById(R.id.input_name);
        password = (EditText) findViewById(R.id.input_password);
        mobile_number = (EditText) findViewById(R.id.mobile_no);
        dateOfBirth = (EditText) findViewById(R.id.dob);
        r1 = (RadioGroup) findViewById(R.id.radioGrp);
        s1 = (Spinner) findViewById(R.id.blood_group);

        b1 = (Button) findViewById(R.id.donorRegistration);
        b1.setOnClickListener(this);
        dateOfBirth.setOnClickListener(this);
        mobile_number.setImeOptions(EditorInfo.IME_ACTION_DONE);

//        TextView t1 = (TextView) findViewById(R.id.alredyDonor);
//        t1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DonorActivity.this, LoginActivity.class);
//                startActivity(intent, null);
//            }
//        });
    }

    private void setDate(int day, int month, int year) {
        Date dob = new GregorianCalendar(year, month, day).getTime();
        Date today = new Date();
        int old = (int)((today.getTime() - dob.getTime()) / (1000 * 60 * 60 * 24) - 4)/365;
        if(old > 17) {
            dateOfBirth.setText(new StringBuilder().append(day).append("/")
                    .append(month+1).append("/").append(year));
        } else {
            dateOfBirth.setText("");
            Toast.makeText(getApplicationContext(), "Too young to donate", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean checkDob(String date) {
        return CustomDate.checkDateDDmmYYYY(date) ? true : false;
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
                if (sendRequest && bg.equalsIgnoreCase("--Blood Group--")) {
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
                    builder.setTitle("Please fill your the entries correctly")
                            .setPositiveButton("Ok", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;

            case R.id.dob :
                mobile_number.clearFocus();
                datePickerDialog = DatePickerDialog.newInstance(DonorActivity.this, year, month, day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);

                datePickerDialog.setAccentColor(Color.parseColor("#009688"));

                datePickerDialog.setTitle("date of birth");

                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
                break;
        }


    }

    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {
        setDate(Day, Month, Year);
    }
}
