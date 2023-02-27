package com.harvin.placementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditMyAdActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String pos,per,loc1,phone1,mail1,url1,id1;
    Button next15;
    EditText jTitle1,jDes1,salary1;
    Spinner sPeriod1;
    Spinner pType1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_ad);

        sPeriod1 =findViewById(R.id.sPeriod1);
        sPeriod1.setOnItemSelectedListener(this);
        jTitle1=findViewById(R.id.jobTitle1);
        jDes1=findViewById(R.id.jobDes1);
        salary1=findViewById(R.id.salary1);
        pType1 = findViewById(R.id.pType1);
        ArrayAdapter<CharSequence> adapter21 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.Position_type, android.R.layout.simple_spinner_dropdown_item);
        pType1.setAdapter(adapter21);
        pType1.setOnItemSelectedListener(this);
        next15=findViewById(R.id.next15);

        Intent i=getIntent();
        id1=i.getStringExtra("idAd1");
        jTitle1.setText(""+i.getStringExtra("title1"));
        jDes1.setText(""+i.getStringExtra("des1"));
        salary1.setText(""+i.getStringExtra("sal1"));
        loc1=i.getStringExtra("loc1");
        phone1=i.getStringExtra("phone1");
        mail1=i.getStringExtra("mail1");
        url1=i.getStringExtra("url1");

        next15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean stat = true;
                stat = validate();
                if(stat){
                    Intent intent=new Intent(getApplicationContext(),EditMyAdActivity2.class);
                    intent.putExtra("title2",jTitle1.getText().toString());
                    intent.putExtra("des2",jDes1.getText().toString());
                    intent.putExtra("salary2",salary1.getText().toString());
                    intent.putExtra("period2",per);
                    intent.putExtra("position2",pos);
                    intent.putExtra("loc2",loc1);
                    intent.putExtra("phone2",phone1);
                    intent.putExtra("mail2",mail1);
                    intent.putExtra("url2",url1);
                    intent.putExtra("idAd2",id1);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Check fields and Try again !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate() {
        boolean stat = true;
        if (jTitle1.getText().toString().trim().equals("")){
            jTitle1.setError("Field required");
            stat = false;
        }

        if (jDes1.getText().toString().trim().equals("")){
            jDes1.setError("Field required");
            stat = false;
        }

        if (salary1.getText().toString().trim().equals("")){
            salary1.setError("Field required");
            stat = false;
        }
        return  stat;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner sPeriod1 = (Spinner)parent;
        Spinner pType1 = (Spinner)parent;
        if(sPeriod1.getId() == R.id.sPeriod1)
        {
            per=parent.getSelectedItem().toString();
        }
        if(pType1.getId() == R.id.pType1)
        {
            pos=parent.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}