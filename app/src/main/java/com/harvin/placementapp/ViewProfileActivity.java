package com.harvin.placementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harvin.placementapp.Model.AdsDatabase;

import java.util.ArrayList;

public class ViewProfileActivity extends AppCompatActivity {
    TextView fullName,email,phone;
    Button editProf;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    DocumentReference documentReference;
    DatabaseReference databaseAds;
    ArrayList<AdsDatabase> adsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        databaseAds= (DatabaseReference) FirebaseDatabase.getInstance().getReference("users").child(userId);
        /*Query query = FirebaseDatabase.getInstance().getReference("id")
                .orderByChild("email")
                .equalTo(userId);*/
        databaseAds.addValueEventListener(valueEventListener);

        phone = findViewById(R.id.editphone);
        fullName = findViewById(R.id.editname);
        email = findViewById(R.id.editmail);
        editProf=findViewById(R.id.editprofile1);


        /*
        documentReference = fStore.collection("users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    phone.setText("  "+documentSnapshot.getString("phone"));
                    fullName.setText("  "+documentSnapshot.getString("name"));
                    email.setText("  "+documentSnapshot.getString("email"));

                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag", "onEvent: Failed to fetch Data");

                    }
                });
         */
        editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfileActivity.this,EditProfileActivity.class));
            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue(AdsDatabase.class) !=null) {
                String key=dataSnapshot.getKey();
                if(key.equals(userId))
                {
                    phone.setText("  "+dataSnapshot.child("phone").getValue().toString());
                    fullName.setText("  "+dataSnapshot.child("name").getValue().toString());
                    email.setText("  "+dataSnapshot.child("email").getValue().toString());
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}