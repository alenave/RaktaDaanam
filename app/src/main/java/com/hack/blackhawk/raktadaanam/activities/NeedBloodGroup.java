package com.hack.blackhawk.raktadaanam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.hack.blackhawk.raktadaanam.MainActivity;
import com.hack.blackhawk.raktadaanam.R;

/**
 * Created by dharmendra on 18/12/16.
 */

public class NeedBloodGroup extends AppCompatActivity {

    Button b1;
    Spinner s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needbloodgroup);
        Spinner dropdown = (Spinner) findViewById(R.id.needbloodGroup);
        String[] items = new String[]{"--Select--", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setSelection(0);
        dropdown.setAdapter(adapter);
        b1 = (Button)findViewById(R.id.needContinue);
        s1 = (Spinner) findViewById(R.id.bloodGroup);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NeedBloodGroup.this, MapActivity.class);
                String bg = s1.getSelectedItem().toString();
                if (!bg.equalsIgnoreCase("--Select--")) {
                    //add code here
                }
                startActivity(intent, null);
            }
        });
    }
}
