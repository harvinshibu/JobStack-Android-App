package com.harvin.placementapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyAdsSelectActivity extends AppCompatActivity {
    TextView MyselTitle,MyselDes,MyselSalary,MyselPeriod,MyselPosition,MyselLoc,MyselPhone,MyselMail,MyselUrl;
    String  MyselTitle1,MyselDes1,MyselSalary1,MyselLoc1,MyselPhone1,MyselMail1,MyselUrl1,MyIdAd;
    Button editAd,deleteAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads_select);

        MyselDes=findViewById(R.id.MyselDes);
        MyselTitle=findViewById(R.id.mytitleJob);
        MyselSalary=findViewById(R.id.MyselSal);
        MyselPeriod=findViewById(R.id.MyselPeriod1);
        MyselPosition=findViewById(R.id.MyselPos1);
        MyselLoc=findViewById(R.id.MyselLoc);
        MyselPhone=findViewById(R.id.MyselPhone1);
        MyselMail=findViewById(R.id.MyselMail);
        MyselUrl=findViewById(R.id.MyselUrl);
        editAd=findViewById(R.id.editAds);
        deleteAd=findViewById(R.id.deleteAds2);


        Intent intent=getIntent();
        MyIdAd=intent.getStringExtra("idAd");
        MyselTitle1=intent.getStringExtra("title");
        MyselDes1=intent.getStringExtra("des");
        MyselSalary1=intent.getStringExtra("sal");
        MyselLoc1=intent.getStringExtra("loc");
        MyselPhone1=intent.getStringExtra("phone");
        MyselMail1=intent.getStringExtra("email");
        MyselUrl1=intent.getStringExtra("url");
        MyselTitle.setText("  "+intent.getStringExtra("title"));
        MyselDes.setText(""+intent.getStringExtra("des"));
        MyselSalary.setText(" "+intent.getStringExtra("sal"));
        MyselPeriod.setText(" "+intent.getStringExtra("period"));
        MyselPosition.setText(" "+intent.getStringExtra("pos"));
        MyselLoc.setText(" "+intent.getStringExtra("loc"));
        MyselPhone.setText(" "+intent.getStringExtra("phone"));
        MyselMail.setText(" "+intent.getStringExtra("email"));
        MyselUrl.setText(" "+intent.getStringExtra("url"));

        editAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyAdsSelectActivity.this,EditMyAdActivity.class);
                i.putExtra("title1",MyselTitle1);
                i.putExtra("des1",MyselDes1);
                i.putExtra("sal1",MyselSalary1);
                i.putExtra("loc1",MyselLoc1);
                i.putExtra("phone1",MyselPhone1);
                i.putExtra("mail1",MyselMail1);
                i.putExtra("url1",MyselUrl1);
                i.putExtra("idAd1",MyIdAd);
                startActivity(i);
            }
        });
        deleteAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder deleteAdsDialog = new AlertDialog.Builder(MyAdsSelectActivity.this);
                deleteAdsDialog.setTitle("Delete Ad");
                deleteAdsDialog.setMessage("You are about to delete your ad \n You won't be able to undo this.");

                deleteAdsDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference dAds=FirebaseDatabase.getInstance().getReference("ads").child(MyIdAd);
                        dAds.removeValue();
                        Toast.makeText(MyAdsSelectActivity.this,"Ad Deleted",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MyAdsSelectActivity.this,HomeActivity.class));
                    }
                });

                deleteAdsDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                deleteAdsDialog.create().show();
            }
        });
    }

}