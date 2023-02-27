package com.harvin.placementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harvin.placementapp.Model.AdsDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditMyAdActivity2 extends AppCompatActivity {
    private EditText locAd, pnumber,url,email;
    private Button postEdit, locate;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Spinner jobCat;
    String jobTitle,jobDes,sal,position,period,mail1,id2,jobC;
    String userId;
    FirebaseUser user;
    DatabaseReference databaseAds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_ad3);

        locAd = findViewById(R.id.loc_addres1);
        pnumber = findViewById(R.id.pno151);
        postEdit = findViewById(R.id.postEdit);
        locate = findViewById(R.id.locate1);
        url=findViewById(R.id.url02);
        email=findViewById(R.id.email151);
        jobCat=findViewById(R.id.jobCat);
        jobCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jobC= parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Intent intent=getIntent();
        id2=intent.getStringExtra("idAd2");
        jobTitle=intent.getStringExtra("title2");
        jobDes=intent.getStringExtra("des2");
        sal=intent.getStringExtra("salary2");
        position=intent.getStringExtra("position2");
        period=intent.getStringExtra("period2");
        locAd.setText(""+intent.getStringExtra("loc2"));
        pnumber.setText(""+intent.getStringExtra("phone2"));
        email.setText(""+intent.getStringExtra("mail2"));
        mail1=intent.getStringExtra("mail2");
        url.setText(""+intent.getStringExtra("url2"));
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        databaseAds= FirebaseDatabase.getInstance().getReference("ads").child(id2);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EditMyAdActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location=task.getResult();
                            if(location!=null){
                                Geocoder geocoder= new Geocoder(EditMyAdActivity2.this, Locale.getDefault());
                                try {
                                    List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    locAd.setText(""+addresses.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(EditMyAdActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        postEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });
    }

    private void updateProduct() {
        String loc=locAd.getText().toString();
        String phon=pnumber.getText().toString();
        String url02=url.getText().toString();
        String email03=email.getText().toString().trim();
        boolean stat = true;
        stat = validate();
        if (stat) {

            AdsDatabase ads3 = new AdsDatabase(id2,email03,jobTitle,jobDes,position,period,loc,phon,sal,url02,jobC,userId);
            databaseAds.setValue(ads3);
            Toast.makeText(getApplicationContext(), "Ad Updated", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            /*
            Ads a = new Ads(mail1,jobTitle,jobDes,position,period,loc,phon,sal,url02);

            fStore.collection("ads").document(ads.getId())
                    .set(a)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Ad Updated", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),MyAdsSelectActivity.class));
                        }
                    });

             */
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check fields and Try again !", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validate() {
        boolean stat = true;
        if (pnumber.getText().toString().trim().equals("")){
            pnumber.setError("Field required");
            stat = false;
        }

        if (locAd.getText().toString().trim().equals("")){
            locAd.setError("Field required");
            stat = false;
        }
        return  stat;
    }
}