package com.example.advertisementmanagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;

public class FindAdvertisement extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private TextView txtLatLong, btnBack, txtWelcome;
    private EditText input_new_lat, input_new_long, reklam_mesafe;
    private Button btnFind, btnGetGPSLocation;
    private RelativeLayout activity_find_advertisement;

    private FirebaseAuth auth;

    private LocationManager locationManager;

    private FusedLocationProviderClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_advertisement);

        txtWelcome = (TextView) findViewById(R.id.reklam_welcome);
        txtLatLong = (TextView) findViewById(R.id.textViewLocation);
        input_new_lat = (EditText) findViewById(R.id.editTextLatitude);
        input_new_long = (EditText) findViewById(R.id.editTextLongitude);
        reklam_mesafe = (EditText) findViewById(R.id.reklam_mesafe);
        btnGetGPSLocation = (Button) findViewById(R.id.reklam_btn_location);
        btnFind = (Button) findViewById(R.id.reklam_btn_find);
        btnBack = (TextView) findViewById(R.id.reklam_btn_back);
        activity_find_advertisement = (RelativeLayout) findViewById(R.id.activity_find_advertisement);

        btnGetGPSLocation.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //Init Firebase
        auth = FirebaseAuth.getInstance();

        //Session check
        if (auth.getCurrentUser() != null)
            txtWelcome.setText("Hosgeldiniz, " + auth.getCurrentUser().getEmail());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.reklam_btn_location) {
            startActivity(new Intent(this, MapActivity.class));
            finish();
        } else if (view.getId() == R.id.reklam_btn_find) {
            findAd();
        } else if (view.getId() == R.id.reklam_btn_back) {
            startActivity(new Intent(this, DashBoard.class));
            finish();
        }
    }

    private void findLocation() {

    }

    private void findLocation(String latlong) {
        String latitude = input_new_lat.getText().toString().trim();
        String longitude = input_new_long.getText().toString().trim();

        if (!TextUtils.isEmpty(latitude) && TextUtils.isEmpty(longitude)) {

        }
    }

    private void findLocation(String latitude, String longitude) {

    }

    private void findAd() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLocationChanged(Location location) {
        /**
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        txtLatLong.setText("Latitude: " + latitude + ", Longitude: " + longitude);
         */
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
