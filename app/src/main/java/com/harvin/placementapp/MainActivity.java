package com.harvin.placementapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText uname,pass;
    TextView forgotTextLink;
    Button log,sign;
    FirebaseAuth fAuth;

    @Override
    protected void onStart() {
        super.onStart();
        fAuth = FirebaseAuth.getInstance();
        LoadingDialog loadingDialog=new LoadingDialog(MainActivity.this);
        if(fAuth.getCurrentUser() != null){
            if(!checkInternetConnection()){
                checkInternetConnection();
            }
            else{
                status("online");
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uname= findViewById(R.id.uname1);
        pass= findViewById(R.id.pass1);
        log=findViewById(R.id.log);
        sign=findViewById(R.id.sign);
        forgotTextLink = findViewById(R.id.forgotPassword);
        LoadingDialog loadingDialog=new LoadingDialog(MainActivity.this);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                if(!checkInternetConnection()){
                    loadingDialog.dismissDialog();
                    checkInternetConnection();
                }

                else{
                String usnme = uname.getText().toString().trim();
                String password = pass.getText().toString();
                boolean stat = true;
                stat = validate();
                if (stat) {
                    fAuth.signInWithEmailAndPassword(usnme, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                status("online");
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else {
                                loadingDialog.dismissDialog();
                                Toast.makeText(MainActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
                else
                    loadingDialog.dismissDialog();
                    /*Toast.makeText(MainActivity.this, "Check fields and try again", Toast.LENGTH_SHORT).show();*/
                }
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternetConnection() == false){
                    checkInternetConnection();
                }
                else{
                    Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternetConnection() == false){
                    checkInternetConnection();
                }
                else{
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Sent", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        loadingDialog.startLoadingDialog();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(MainActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(MainActivity.this, "Error ! Reset Link Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });
                passwordResetDialog.create().show();
                }
            }
        });
    }

    private boolean checkInternetConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            Dialog dialog =new Dialog(this);
            dialog.setContentView(R.layout.internet_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations= android.R.style.Animation_Dialog;
            Button btnTry=dialog.findViewById(R.id.button_try);
            btnTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            dialog.show();
            return false;
        }else
            {
            return true;
        }
    }

    private boolean validate() {
        boolean stat = true;
        if (uname.getText().toString().trim().equals("")){
            uname.setError("Field required");
            stat = false;
        }
        if (pass.getText().toString().trim().equals("")){
            pass.setError("Field required");
            stat = false;
        }
        return  stat;
    }
    private  void status(String status){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

}