package com.example.advertisementmanagement;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.advertisementmanagement.SendNotification.CHANNEL_ID;

/**
 * Yazilim Laboratuvari II Proje 3
 * @author Oguz Aktas & Mert Var
 */
public class FindAdvertisement extends AppCompatActivity implements View.OnClickListener, LocationListener, AdapterView.OnItemSelectedListener {

    private TextView txtLatLong, btnBack, txtWelcome;
    private EditText input_new_lat, input_new_long, reklam_mesafe, reklam_firma;
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

    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_advertisement);

        txtWelcome = (TextView) findViewById(R.id.reklam_welcome);
        txtLatLong = (TextView) findViewById(R.id.textViewLocation);
        input_new_lat = (EditText) findViewById(R.id.reklam_new_lat);
        input_new_long = (EditText) findViewById(R.id.reklam_new_long);
        reklam_mesafe = (EditText) findViewById(R.id.reklam_mesafe);
        reklam_firma = (EditText) findViewById(R.id.reklam_firma);
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

        getInfo();
        getData();

        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.reklam_btn_location) {
            startActivity(new Intent(this, MapActivity.class));
            finish();
        } else if (view.getId() == R.id.reklam_btn_find) {
            getLocation();
            findAd();
            if (!results.isEmpty()) {
                getNotification();
            }
        } else if (view.getId() == R.id.reklam_btn_back) {
            startActivity(new Intent(this, DashBoard.class));
            finish();
        }
    }

    private void getNotification() {
        if (!results.isEmpty()) {
            for (Reklam reklam : results) {
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(reklam.getFirmaAdi())
                        .setContentText(reklam.getKampanyaIcerik())
                        .setSmallIcon(R.drawable.ic_adb_black_24dp)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();
                notificationManager.notify(results.indexOf(reklam), notification);
            }
        }
    }

    private void getInfo() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            latitude = b.getString("latitude");
            longitude = b.getString("longitude");
            currentLocation.setLatitude(Double.parseDouble(latitude));
            currentLocation.setLongitude(Double.parseDouble(longitude));
            txtLatLong.setText("Latitude: " + latitude + ", Longitude: " + longitude);
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
                    Reklam reklam = new Reklam((String) ds.child("firmaAdi").getValue(), (String) ds.child("latitude").getValue(),
                            (String) ds.child("longitude").getValue(), (String) ds.child("kampanyaIcerik").getValue(),
                            (String) ds.child("kampanyaSuresi").getValue(), (String) ds.child("kategori").getValue());
                    reklamlar.add(reklam);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLocation() {
        String new_lat = input_new_lat.getText().toString().trim();
        String new_long = input_new_long.getText().toString().trim();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            latitude = b.getString("latitude");
            longitude = b.getString("longitude");
            currentLocation.setLatitude(Double.parseDouble(latitude));
            currentLocation.setLongitude(Double.parseDouble(longitude));
            txtLatLong.setText("Latitude: " + latitude + ", Longitude: " + longitude);
        }
        if (!TextUtils.isEmpty(new_lat) && !TextUtils.isEmpty(new_long)) {
            latitude = new_lat;
            longitude = new_long;
            currentLocation.setLatitude(Double.parseDouble(latitude));
            currentLocation.setLongitude(Double.parseDouble(longitude));
        }
    }

    private void findAd() {
        String mesafe = reklam_mesafe.getText().toString().trim();
        String magaza = reklam_firma.getText().toString().trim();
        results.clear();
        Location location2 = new Location("");
        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
            if (!TextUtils.isEmpty(mesafe) && TextUtils.isEmpty(magaza)) {
                for (Reklam reklam : reklamlar) {
                    location2.setLatitude(Double.parseDouble(reklam.getLatitude()));
                    location2.setLongitude(Double.parseDouble(reklam.getLongitude()));
                    if (category.equals("Tum kategoriler")) {
                        if ((Double.parseDouble(mesafe) <= currentLocation.distanceTo(location2))) {
                            results.add(reklam);
                        }
                    } else {
                        if ((Double.parseDouble(mesafe) <= currentLocation.distanceTo(location2)) && reklam.getKategori().equals(category)) {
                            results.add(reklam);
                        }
                    }
                }
            } else if (TextUtils.isEmpty(mesafe) && !TextUtils.isEmpty(magaza)) {
                for (Reklam reklam : reklamlar) {
                    location2.setLatitude(Double.parseDouble(reklam.getLatitude()));
                    location2.setLongitude(Double.parseDouble(reklam.getLongitude()));
                    if (category.equals("Tum kategoriler")) {
                        if ((reklam.getFirmaAdi().contains(magaza))) {
                            results.add(reklam);
                        }
                    } else {
                        if ((reklam.getFirmaAdi().contains(magaza)) && reklam.getKategori().equals(category)) {
                            results.add(reklam);
                        }
                    }
                }
            } else if (!TextUtils.isEmpty(mesafe) && !TextUtils.isEmpty(magaza)) {
                for (Reklam reklam : reklamlar) {
                    location2.setLatitude(Double.parseDouble(reklam.getLatitude()));
                    location2.setLongitude(Double.parseDouble(reklam.getLongitude()));
                    if (category.equals("Tum kategoriler")) {
                        if ((Double.parseDouble(mesafe) <= currentLocation.distanceTo(location2)) && reklam.getFirmaAdi().equals(magaza)) {
                            results.add(reklam);
                        }
                    } else {
                        if ((Double.parseDouble(mesafe) <= currentLocation.distanceTo(location2)) && reklam.getFirmaAdi().equals(magaza) && reklam.getKategori().equals(category)) {
                            results.add(reklam);
                        }
                    }
                }
            }
            else if (TextUtils.isEmpty(mesafe) && TextUtils.isEmpty(magaza)) {
                for (Reklam reklam : reklamlar) {
                    location2.setLatitude(Double.parseDouble(reklam.getLatitude()));
                    location2.setLongitude(Double.parseDouble(reklam.getLongitude()));
                    if (!category.equals("Tum kategoriler")) {
                        if (reklam.getKategori().equals(category)) {
                            results.add(reklam);
                        }
                    } else {
                        results.add(reklam);
                    }
                }
            }
            if (results.isEmpty()) {
                Snackbar snackBar = Snackbar.make(activity_find_advertisement, "Arama sonucuna gore hicbir reklam bulunamadi.", Snackbar.LENGTH_LONG);
                snackBar.show();
            } else {
                Intent intent = new Intent(FindAdvertisement.this, ListAdvertisement.class);
                Bundle b = new Bundle();
                b.putSerializable("results", (Serializable) results);
                b.putString("latitude", latitude);
                b.putString("longitude", longitude);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        } else {
            Snackbar snackBar = Snackbar.make(activity_find_advertisement, "Lokasyon bilginiz bulunamadi.", Snackbar.LENGTH_LONG);
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
