package com.hack.blackhawk.raktadaanam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hack.blackhawk.raktadaanam.activities.DonorActivity;
import com.hack.blackhawk.raktadaanam.activities.MapActivity;

public class MainActivity extends AppCompatActivity{
    Button b1,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button)findViewById(R.id.doner);
        b2 = (Button)findViewById(R.id.reciver);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DonorActivity.class);
                startActivity(intent, null);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent, null);
            }
        });

    }
}
