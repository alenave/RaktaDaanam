package com.hack.blackhawk.raktadaanam.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hack.blackhawk.raktadaanam.R;

/**
 * Created by dharmendra on 17/12/16.
 */

public class HistoryPopup extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historypopup);
        Intent i = getIntent();
        //to get bean object back
        //Deneme dene = (Deneme)i.getSerializableExtra("sampleObject");
        Button b1 = (Button)findViewById(R.id.donorContinue);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e1 = (EditText)findViewById(R.id.input_donationDate);
                String lastDonate = e1.getText().toString();
                if("".equalsIgnoreCase(lastDonate) && checkDob(lastDonate)) {
                    //add this to request
                }
                //success!
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryPopup.this);
                builder.setMessage("Doner request send")
                        .setTitle("Doner request send")
                        .setPositiveButton("Ok", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private boolean checkDob(String date) {
        return date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})") ? true : false;
    }
}
