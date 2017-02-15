package com.adatech.blackhawk.raktadaanam.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.adatech.blackhawk.raktadaanam.utils.LocationOn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.adatech.blackhawk.raktadaanam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener {

    // Google Map
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private TextView textView;
    private LocationManager lm;
    //    private Intent intent;
    String blood_group;
//    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        blood_group = getIntent().getStringExtra("blood_group");
        permission();


        try {
            // Loading map
            initializeMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } catch (Exception e) {
            e.printStackTrace();
        }


        LocationOn.getInstance(this).check();
    }


    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initializeMap() {
        if (googleMap == null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);

        }
    }


    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;

        setUpMap();

    }

    public void setUpMap() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
        }

        googleMap.setTrafficEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }
        if (mLastLocation != null) {
            googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .radius(10000)
                    .strokeColor(Color.RED));

            LatLng pinPoint = null;
            pinPoint = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(pinPoint).title("Receiver").icon(BitmapDescriptorFactory.fromResource(R.mipmap.green))).setTag(0);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pinPoint, 12);
            googleMap.animateCamera(cameraUpdate);
            try {
                setMarkerForDonor();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Set a listener for marker click.
            googleMap.setOnMarkerClickListener(this);

        }
    }

    private void setMarkerForDonor() throws JSONException {
        JSONObject jsonObject = NeedBloodGroup.jsonObjectOfDonors;
        if (jsonObject != null) {
            try {
                if (jsonObject.has("donors")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("donors");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = (JSONObject) jsonArray.get(i);
                        LatLng pinPoint1 = new LatLng(jo.getDouble("latitude"), jo.getDouble("longitude"));
                        googleMap.addMarker(new MarkerOptions().position(pinPoint1).title("Donor: " + jo.getString("name")).snippet("Mobile: " + jo.getString("mobile_number")).icon(BitmapDescriptorFactory.fromResource(R.mipmap.red))).setTag(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static final int REQUEST_PERMISSION = 99;

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            marker.showInfoWindow();
            if(marker.isInfoWindowShown()) {
                callDonor(marker);
            }

        }

        return false;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void callDonor(final Marker marker) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Call " + marker.getTitle().substring(7));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + marker.getSnippet().substring(8)));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_PERMISSION);
                } else {
                    startActivity(callIntent);

                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
