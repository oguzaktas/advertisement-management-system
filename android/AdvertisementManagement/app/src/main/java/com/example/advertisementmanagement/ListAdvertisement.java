package com.example.advertisementmanagement;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;
import java.util.ArrayList;

/**
 * Yazilim Laboratuvari II Proje 3
 * @author Oguz Aktas & Mert Var
 */
public class ListAdvertisement extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Reklam> results;
    private String latitude;
    private String longitude;

    private TextView btnBack;
    private ListView listView;

    private static final String CHANNEL_ID = "channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_advertisement);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            results = (ArrayList<Reklam>) b.getSerializable("results");
            latitude = b.getString("latitude");
            longitude = b.getString("longitude");
            Log.e("latitude", latitude);
        }

        btnBack = (TextView) findViewById(R.id.list_btn_back);
        listView = (ListView) findViewById(R.id.listView);

        ReklamListAdapter adapter = new ReklamListAdapter(this, R.layout.reklam, results);
        listView.setAdapter(adapter);

        btnBack.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notification Channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is notification channel");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.list_btn_back) {
            Intent intent = new Intent(ListAdvertisement.this, FindAdvertisement.class);
            Bundle b = new Bundle();
            b.putString("latitude", latitude);
            b.putString("longitude", longitude);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
