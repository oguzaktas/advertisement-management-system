package com.example.advertisementmanagement;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Yazilim Laboratuvari II Proje 3
 * @author Oguz Aktas & Mert Var
 */
public class Database extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText editTextFirmaAdi, editTextLatitude, editTextLongitude, editTextKampanyaIcerik, editTextKampanyaSuresi;
    private Button btnReklam;
    private RelativeLayout activity_database;
    private TextView btnBack;
    private Spinner spnCategory;
    private String category;

    private DatabaseReference databaseReklam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        databaseReklam = FirebaseDatabase.getInstance().getReference("reklam");

        editTextFirmaAdi = (EditText) findViewById(R.id.editTextFirmaAdi);
        editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);
        editTextKampanyaIcerik = (EditText) findViewById(R.id.editTextKampanyaIcerik);
        editTextKampanyaSuresi = (EditText) findViewById(R.id.editTextKampanyaSuresi);
        btnReklam = (Button) findViewById(R.id.database_btn_reklam);
        activity_database = (RelativeLayout)findViewById(R.id.activity_database);
        btnBack = (TextView) findViewById(R.id.database_btn_back);

        spnCategory = (Spinner) findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(this);

        btnReklam.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.database_btn_reklam) {
            addAdvertisement();
        } else if (view.getId() == R.id.database_btn_back) {
            startActivity(new Intent(Database.this, DashBoard.class));
            finish();
        }
    }

    private void addAdvertisement() {
        String firmaAdi = editTextFirmaAdi.getText().toString().trim();
        String latitude = editTextLatitude.getText().toString().trim();
        String longitude = editTextLongitude.getText().toString().trim();
        String kampanyaIcerik = editTextKampanyaIcerik.getText().toString().trim();
        String kampanyaSuresi = editTextKampanyaSuresi.getText().toString().trim();

        if (!TextUtils.isEmpty(firmaAdi) && !TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude) &&
                !TextUtils.isEmpty(kampanyaIcerik) && !TextUtils.isEmpty(kampanyaSuresi)) {

            String reklamID = databaseReklam.push().getKey(); // Unique ID
            Reklam reklam = new Reklam(reklamID, firmaAdi, latitude, longitude, kampanyaIcerik, kampanyaSuresi, category);
            databaseReklam.child(reklamID).setValue(reklam);

            Snackbar snackBar = Snackbar.make(activity_database, "Reklam basariyla eklendi.", Snackbar.LENGTH_LONG);
            snackBar.show();
            editTextLatitude.getText().clear();
            editTextLongitude.getText().clear();
            editTextKampanyaIcerik.getText().clear();
            editTextKampanyaSuresi.getText().clear();
            editTextFirmaAdi.getText().clear();
        } else {
            Snackbar snackBar = Snackbar.make(activity_database, "Tum alanlari doldurduktan sonra tekrar deneyiniz.", Snackbar.LENGTH_LONG);
            snackBar.show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
