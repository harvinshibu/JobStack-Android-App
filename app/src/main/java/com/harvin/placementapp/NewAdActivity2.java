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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harvin.placementapp.Model.AdsDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class NewAdActivity2 extends AppCompatActivity {
    EditText locAd, pnumber, url, emailId;
    Button post, locate;
    Spinner jobCat;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String jobTitle, jobDes, sal, position, period;
    public static String PrimEmail;
    String userId,jobC;
    FirebaseUser user;
    DatabaseReference databaseAds,databaseAds2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad3);
        locAd = findViewById(R.id.loc_addres);
        pnumber = findViewById(R.id.pno15);
        post = findViewById(R.id.post);
        locate = findViewById(R.id.locate);
        url = findViewById(R.id.url01);
        emailId=findViewById(R.id.email15);
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
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        databaseAds = FirebaseDatabase.getInstance().getReference("ads");
        databaseAds2 = FirebaseDatabase.getInstance().getReference("users").child(userId);
        Intent intent = getIntent();
        jobTitle = intent.getStringExtra("title");
        jobDes = intent.getStringExtra("des");
        sal = intent.getStringExtra("salary");
        position = intent.getStringExtra("position");
        period = intent.getStringExtra("period");

        databaseAds2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(AdsDatabase.class) != null) {
                    String key = dataSnapshot.getKey();
                    if (key.equals(userId)) {
                        pnumber.setText("  " + dataSnapshot.child("phone").getValue().toString());
                        emailId.setText(""+dataSnapshot.child("email").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    pnumber.setText("  " + documentSnapshot.getString("phone"));
                    PrimEmail = documentSnapshot.getString("email");
                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });
        */
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(NewAdActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(NewAdActivity2.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    locAd.setText("" + addresses.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(NewAdActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = locAd.getText().toString();
                String phon = pnumber.getText().toString();
                String url02 = url.getText().toString();
                PrimEmail=emailId.getText().toString().trim();
                boolean stat = true;
                stat = validate();
                if (stat) {
                    //RealTime Database
                    String idAd = databaseAds.push().getKey();
                    AdsDatabase ads2 = new AdsDatabase(idAd, PrimEmail, jobTitle, jobDes, position, period, loc, phon, sal, url02,jobC,userId);
                    databaseAds.child(idAd).setValue(ads2);
                    Toast.makeText(getApplicationContext(), "Ad Posted", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                    /*
                    // FireStore
                    CollectionReference collectionReference = fStore.collection("ads");
                    Ads ads=new Ads(PrimEmail,jobTitle,jobDes,position,period,loc,phon,sal,url02);
                    collectionReference.add(ads).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Ad Posted", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                     */
                } else {
                    Toast.makeText(getApplicationContext(), "Check fields and Try again !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate() {
        boolean stat = true;
        if (pnumber.getText().toString().trim().equals("")) {
            pnumber.setError("Field required");
            stat = false;
        }

        if (locAd.getText().toString().trim().equals("")) {
            locAd.setError("Field required");
            stat = false;
        }
        return stat;
    }

}