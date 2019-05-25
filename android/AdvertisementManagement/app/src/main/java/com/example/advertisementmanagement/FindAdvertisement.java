package com.example.advertisementmanagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FindAdvertisement extends AppCompatActivity implements View.OnClickListener, LocationListener, AdapterView.OnItemSelectedListener {

    private TextView txtLatLong, btnBack, txtWelcome;
    private EditText input_new_lat, input_new_long, reklam_mesafe;
    private Button btnFind, btnGetGPSLocation;
    private RelativeLayout activity_find_advertisement;
    private Spinner spnCategory;
    private String category;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private List<Reklam> reklamlar = new ArrayList<>();
    private List<Reklam> results = new ArrayList<>();

    private LocationManager locationManager;
    private Location currentLocation = new Location("");

    private String latitude;
    private String longitude;
    private String denemefirmaadi;

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

        spnCategory = (Spinner) findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category2, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(this);

        btnGetGPSLocation.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //Init Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference().child("reklam");

        //Session check
        if (mAuth.getCurrentUser() != null)
            txtWelcome.setText("Hosgeldiniz, " + mAuth.getCurrentUser().getEmail());

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
        if (latitude != null && longitude != null) {
            txtLatLong.setText("Latitude: " + latitude + ", Longitude: " + longitude);
        }

        getLocation();
        getData();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.reklam_btn_location) {
            startActivity(new Intent(this, MapActivity.class));
            finish();
        } else if (view.getId() == R.id.reklam_btn_find) {
            getLocation();
            findAd();
        } else if (view.getId() == R.id.reklam_btn_back) {
            startActivity(new Intent(this, DashBoard.class));
            finish();
        }
    }

    private void getData() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reklamlar.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    keys.add(ds.getKey());
                    Reklam reklam = new Reklam((String) ds.child("firmaAdi").getValue(), (String) ds.child("kampanyaIcerik").getValue(),
                            (String) ds.child("kampanyaSuresi").getValue(), (String) ds.child("kategori").getValue(),
                            (String) ds.child("latitude").getValue(), (String) ds.child("longitude").getValue());
                    reklamlar.add(reklam);
                    denemefirmaadi = (String) ds.child("firmaAdi").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLocation() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            latitude = String.valueOf(b.getDouble("latitude"));
            longitude = String.valueOf(b.getDouble("longitude"));
            currentLocation.setLatitude(Double.parseDouble(latitude));
            currentLocation.setLongitude(Double.parseDouble(longitude));
            txtLatLong.setText("Latitude: " + latitude + ", Longitude: " + longitude);
        }
        if (!TextUtils.isEmpty((CharSequence) input_new_lat) && !TextUtils.isEmpty((CharSequence) input_new_long)) {
            latitude = String.valueOf(input_new_lat);
            longitude = String.valueOf(input_new_long);
            System.out.println(latitude);
            currentLocation.setLatitude(Double.parseDouble(latitude));
            currentLocation.setLongitude(Double.parseDouble(longitude));
        }
    }

    private void findAd() {
        String mesafe = reklam_mesafe.getText().toString().trim();
        results.clear();
        Location location2 = new Location("");
        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
            if (!TextUtils.isEmpty(mesafe)) {
                for (Reklam reklam : reklamlar) {
                    location2.setLatitude(Double.parseDouble(reklam.getLatitude()));
                    location2.setLongitude(Double.parseDouble(reklam.getLongitude()));
                    if (Double.parseDouble(mesafe) >= currentLocation.distanceTo(location2)) {
                        results.add(reklam);
                    }
                }
            } else {
                Snackbar snackBar = Snackbar.make(activity_find_advertisement, "Threshold (mesafe) bilgisini girdikten sonra tekrar deneyiniz.", Snackbar.LENGTH_LONG);
                snackBar.show();
            }
        } else {
            Snackbar snackBar = Snackbar.make(activity_find_advertisement, "Lokasyon bilgisi bulunamadi.", Snackbar.LENGTH_LONG);
            snackBar.show();
        }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
