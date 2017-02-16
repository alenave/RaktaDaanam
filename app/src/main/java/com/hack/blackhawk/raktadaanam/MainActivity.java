package com.hack.blackhawk.raktadaanam;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hack.blackhawk.raktadaanam.activities.DonorActivity;
import com.hack.blackhawk.raktadaanam.activities.NeedBloodGroup;
import com.hack.blackhawk.raktadaanam.utils.LocationOn;
import com.hack.blackhawk.raktadaanam.utils.InternetConnection;
import com.hack.blackhawk.raktadaanam.utils.Permission;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button b1, b2;
    private LocationManager lm;
    private LinearLayout retry;
    private ScrollView fullView;
    private Button retryButton;
    private final Activity activity = (Activity) this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initViews();
        Permission.check(this);
        if (!InternetConnection.isInternetConnected(getApplicationContext())) {
            fullView.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        } else {
            LocationOn.getInstance(this).check();
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DonorActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, null);
                    }
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, NeedBloodGroup.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, null);
                    }
                }
            });
        }

    }

    private void initViews() {
        fullView = (ScrollView) findViewById(R.id.full_view);
        retryButton = (Button) findViewById(R.id.retry_button);
        retry = (LinearLayout) findViewById(R.id.retry);
        b1 = (Button) findViewById(R.id.doner);
        b2 = (Button) findViewById(R.id.reciver);
        retryButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retry_button:
                if (InternetConnection.isInternetConnected(getApplicationContext())) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                break;
        }
    }
}
