package com.harvin.placementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harvin.placementapp.Model.Users;

public class RegisterActivity extends AppCompatActivity {
    EditText email,name,pno,pass;
    Button reg;
    private FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    FirebaseFirestore fStore;
    DatabaseReference databaseAds;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        email=findViewById(R.id.email11);
        name=findViewById(R.id.name1);
        pno=findViewById(R.id.pno1);
        pass=findViewById(R.id.pass2);
        reg=findViewById(R.id.reg1);
        LoadingDialog loadingDialog=new LoadingDialog(RegisterActivity.this);
        databaseAds= FirebaseDatabase.getInstance().getReference("users");

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1 = email.getText().toString().trim();
                String name1 = name.getText().toString();
                String phone = pno.getText().toString().trim();
                String password = pass.getText().toString();
                loadingDialog.startLoadingDialog();
                boolean stat = true;
                stat = validate();
                if (stat) {
                    mAuth.createUserWithEmailAndPassword(email1, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userID = mAuth.getCurrentUser().getUid();
                                Users users=new Users(userID,email1,name1,phone,"online");
                                databaseAds.child(userID).setValue(users);
                                loadingDialog.dismissDialog();
                                Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                                /*
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name1);
                                user.put("email", email1);
                                user.put("phone", phone);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });
                                 */
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                            } else {
                                loadingDialog.dismissDialog();
                                Toast.makeText(RegisterActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    loadingDialog.dismissDialog();
                    Toast.makeText(RegisterActivity.this, "Check fields and Try again !", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean validate() {
        boolean stat = true;
        if (email.getText().toString().trim().equals("")){
            email.setError("Field required");
            stat = false;
        }

        if (pass.getText().toString().trim().equals("")){
            pass.setError("Field required");
            stat = false;
        }
        if (pno.getText().toString().trim().equals("")){
            pno.setError("Field required");
            stat = false;
        }
        if (name.getText().toString().trim().equals("")){
            name.setError("Field required");
            stat = false;
        }
        return  stat;
    }
}