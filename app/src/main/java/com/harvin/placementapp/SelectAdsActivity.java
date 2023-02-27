package com.harvin.placementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SelectAdsActivity extends AppCompatActivity {
    TextView selTitle,selDes,selSalary,selPeriod,selPosition,selLoc,selPhone,selMail,selUrl;
    Button chats,call;
    String usersId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ads);
        selDes=findViewById(R.id.selDes);
        selTitle=findViewById(R.id.titleJob);
        selSalary=findViewById(R.id.selLink);
        selPeriod=findViewById(R.id.selPeriod1);
        selPosition=findViewById(R.id.selPos1);
        selLoc=findViewById(R.id.selLoc);
        selPhone=findViewById(R.id.selPhone1);
        selMail=findViewById(R.id.selMail);
        selUrl=findViewById(R.id.selUrl);
        call=findViewById(R.id.selCall);
        chats=findViewById(R.id.selChat);

        Intent intent=getIntent();
        usersId=intent.getStringExtra("usersId");
        selTitle.setText("  "+intent.getStringExtra("title"));
        selDes.setText(""+intent.getStringExtra("des"));
        selSalary.setText(" "+intent.getStringExtra("sal"));
        selPeriod.setText(" "+intent.getStringExtra("period"));
        selPosition.setText(" "+intent.getStringExtra("pos"));
        selLoc.setText(" "+intent.getStringExtra("loc"));
        selPhone.setText(" "+intent.getStringExtra("phone"));
        selMail.setText(" "+intent.getStringExtra("email"));
        selUrl.setText(" "+intent.getStringExtra("url"));

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+intent.getStringExtra("phone").trim()));
                startActivity(i);
            }
        });

        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
                if(fuser.getUid().equals(usersId))
                {
                    Toast.makeText(SelectAdsActivity.this,"You can't send message to yourself",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(SelectAdsActivity.this,MessagingActivity.class);
                    intent.putExtra("userId",usersId);
                    startActivity(intent);
                }


            }
        });

    }
}