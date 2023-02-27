package com.harvin.placementapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harvin.placementapp.Model.AdsDatabase;
import com.harvin.placementapp.Model.Users;

import java.util.ArrayList;


public class EditProfileActivity extends AppCompatActivity {
    EditText saveName,saveEmail,savePhone;
    Button saveChange;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DatabaseReference databaseAds;
    ArrayList<AdsDatabase> adsArrayList;
    String userId;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        savePhone = findViewById(R.id.savepno);
        saveName = findViewById(R.id.savename1);
        saveEmail = findViewById(R.id.savemail1);
        saveChange=findViewById(R.id.savechange);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        adsArrayList=new ArrayList<>();
        databaseAds= FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseAds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(AdsDatabase.class) !=null) {
                    String key=dataSnapshot.getKey();
                    if(key.equals(userId))
                    {
                        savePhone.setText("  "+dataSnapshot.child("phone").getValue().toString());
                        saveName.setText("  "+dataSnapshot.child("name").getValue().toString());
                        saveEmail.setText("  "+dataSnapshot.child("email").getValue().toString());
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
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    savePhone.setText(""+documentSnapshot.getString("phone"));
                    saveName.setText(""+documentSnapshot.getString("name"));
                    saveEmail.setText(""+documentSnapshot.getString("email"));
                }
                else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

         */

        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changemail=saveEmail.getText().toString().trim();
                String changepno=savePhone.getText().toString().trim();
                String changename=saveName.getText().toString().trim();

                boolean stat=true;
                stat=validate();
                if(stat)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.layout_dialog_profile, null);
                    builder.setView(view)
                            .setTitle("Enter Password :")
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    EditText oldPasswrd=view.findViewById(R.id.enPass1);
                                    String oldP = oldPasswrd.getText().toString();
                                    AuthCredential authCredential= EmailAuthProvider.getCredential(user.getEmail(),oldP);

                                    user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            user.updateEmail(changemail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Users users = new Users(userId,changemail,changename,changepno,"online");
                                                    databaseAds.setValue(users);
                                                    Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(),ViewProfileActivity.class));
                                                    finish();
                                                    // Firestore Code
                                                    /*
                                                    DocumentReference docRef = fStore.collection("users").document(user.getUid());
                                                    Map<String,Object> edited = new HashMap<>();
                                                    edited.put("email",changemail);
                                                    edited.put("name",changename);
                                                    edited.put("phone",changepno);
                                                    docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                            finish();
                                                        }
                                                    });

                                                     */
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(EditProfileActivity.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    Log.d("message",e.getMessage());
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"Incorrect Old Password !",Toast.LENGTH_SHORT).show();
                                            Log.d("message",e.getMessage());
                                        }
                                    });
                                }
                            });
                    builder.create().show();
                }
            }
        });
    }

    private boolean validate() {
        boolean stat = true;
        if (saveEmail.getText().toString().trim().equals("")){
            saveEmail.setError("Field required");
            stat = false;
        }

        if (savePhone.getText().toString().trim().equals("")){
            savePhone.setError("Field required");
            stat = false;
        }
        if (saveName.getText().toString().trim().equals("")){
            saveName.setError("Field required");
            stat = false;
        }
        return  stat;
    }
}