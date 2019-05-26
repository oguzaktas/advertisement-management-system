package com.example.advertisementmanagement;

import android.content.Intent;
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

public class ListAdvertisement extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Reklam> results;
    private String latitude;
    private String longitude;

    private TextView btnBack;
    private ListView listView;

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
