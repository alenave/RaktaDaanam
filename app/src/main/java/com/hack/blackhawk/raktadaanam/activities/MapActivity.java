package com.hack.blackhawk.raktadaanam.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.hack.blackhawk.raktadaanam.R;

import static android.support.v7.app.AlertDialog.Builder;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener {

    // Google Map
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private TextView textView;
    private LocationManager lm;
//    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
//        imageView = (ImageView) findViewById(R.id.marker);
        permission();

//        textView = (TextView) findViewById(R.id.text);

        try {
            // Loading map
            initializeMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } catch (Exception e) {
            e.printStackTrace();
        }


        checkLocationOn();
    }

    private void checkLocationOn() {
        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            Builder dialog = new Builder(this);
            dialog.setMessage(getApplicationContext().getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getApplicationContext().getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(getApplicationContext().getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }

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
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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
//            LatLng center = googleMap.getCameraPosition().target;
//            textView.setText(String.valueOf(mLastLocation.getLatitude()));
            googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .radius(10000)
                    .strokeColor(Color.RED));
            LatLng pinPoint = null;
            pinPoint = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(pinPoint).title("Donor").snippet("Population: 4,137,400").icon(BitmapDescriptorFactory.fromResource(R.mipmap.green))).setTag(0);
//            setMarkerForDonor();
            // Set a listener for marker click.
            googleMap.setOnMarkerClickListener(this);

        }
    }

    private void setMarkerForDonor() {
        LatLng pinPoint = null;
        pinPoint = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(pinPoint).title("Donor").snippet("Population: 4,137,400").icon(BitmapDescriptorFactory.fromResource(R.mipmap.red))).setTag(0);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
//            clickCount = clickCount + 1;
//            marker.setTag(clickCount);
            marker.showInfoWindow();
//            Toast.makeText(this,
//                    marker.getTitle() +
//                            " has been clicked " + clickCount + " times.",

        }
        return false;
    }
//    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {
//
//        View customMarkerView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
//        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
//        markerImageView.setImageResource(resId);
//        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
//        customMarkerView.buildDrawingCache();
//        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(returnedBitmap);
//        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
//        Drawable drawable = customMarkerView.getBackground();
//        if (drawable != null)
//            drawable.draw(canvas);
//        customMarkerView.draw(canvas);
//        return returnedBitmap;
//    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
