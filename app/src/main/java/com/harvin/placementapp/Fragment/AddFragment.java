package com.harvin.placementapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.libraries.places.api.Places;
import com.harvin.placementapp.NewAdActivity2;
import com.harvin.placementapp.R;

public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener
{
    String s,p;
    Button next;
    EditText jTitle,jDes,salary;
    Spinner sPeriod;
    Spinner pType;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);
        sPeriod = v.findViewById(R.id.sPeriod);
        sPeriod.setOnItemSelectedListener(this);
        jTitle=v.findViewById(R.id.jobTitle);
        jDes=v.findViewById(R.id.jobDes);
        salary=v.findViewById(R.id.salary);
        pType = v.findViewById(R.id.pType);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.Position_type, android.R.layout.simple_spinner_dropdown_item);
        pType.setAdapter(adapter2);
        pType.setOnItemSelectedListener(this);
        next=v.findViewById(R.id.next1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean stat = true;
                stat = validate();
                if(stat){
                    Intent intent=new Intent(getActivity(), NewAdActivity2.class);
                    intent.putExtra("title",jTitle.getText().toString());
                    intent.putExtra("des",jDes.getText().toString());
                    intent.putExtra("salary",salary.getText().toString());
                    intent.putExtra("period",p);
                    intent.putExtra("position",s);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(), "Check fields and Try again !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    private boolean validate() {
        boolean stat = true;
        if (jTitle.getText().toString().trim().equals("")){
            jTitle.setError("Field required");
            stat = false;
        }

        if (jDes.getText().toString().trim().equals("")){
            jDes.setError("Field required");
            stat = false;
        }

        if (salary.getText().toString().trim().equals("")){
            salary.setError("Field required");
            stat = false;
        }
        return  stat;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner sPeriod = (Spinner)parent;
        Spinner pType = (Spinner)parent;
        if(sPeriod.getId() == R.id.sPeriod)
        {
            p=parent.getSelectedItem().toString();
        }
        if(pType.getId() == R.id.pType)
        {
            s=parent.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
