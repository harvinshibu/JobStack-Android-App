package com.harvin.placementapp.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harvin.placementapp.Adapter.MyRecyclerViewAdapter;
import com.harvin.placementapp.Model.AdsDatabase;
import com.harvin.placementapp.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    RecyclerView mFireStoreList;
    ArrayList<AdsDatabase> adsArrayList;
    MyRecyclerViewAdapter adapter;
    DatabaseReference databaseAds;
    Spinner filter;
    EditText searchEdit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        mFireStoreList=v.findViewById(R.id.list_ads);
        searchEdit=v.findViewById(R.id.search_home);
        filter=v.findViewById(R.id.spinner);
        filter.setOnItemSelectedListener(this);
        /*filterLoc=v.findViewById(R.id.spinner2);
        filterLoc.setOnItemSelectedListener(this);*/
        mFireStoreList.setHasFixedSize(true);
        mFireStoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseAds= FirebaseDatabase.getInstance().getReference("ads");
        adsArrayList=new ArrayList<>();
        loadDataFromDatabase();
        if(searchEdit != null){
            searchEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                        search(s.toString());
                }
            });
        }
        return v;
    }

    private void search(String string){
        ArrayList<AdsDatabase> searchList=new ArrayList<>();
        for(AdsDatabase object : adsArrayList){
            if(object.getDescription().toLowerCase().contains(string.toLowerCase())){
                searchList.add(object);
            }
        }
        adapter = new MyRecyclerViewAdapter(getContext(),HomeFragment.this,searchList);
        mFireStoreList.setAdapter(adapter);
    }

    private void searchCategory(String string){
        ArrayList<AdsDatabase> searchList=new ArrayList<>();
        for(AdsDatabase object : adsArrayList){
            if(object.getJobCategory().toLowerCase().contains(string.toLowerCase())){
                searchList.add(object);
            }
        }
        adapter = new MyRecyclerViewAdapter(getContext(),HomeFragment.this,searchList);
        mFireStoreList.setAdapter(adapter);
    }

    private void loadDataFromDatabase(){
        if(adsArrayList.size()>0)
            adsArrayList.clear();
        databaseAds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot :snapshot.getChildren()) {
                    AdsDatabase adsDatabase = postSnapshot.getValue(AdsDatabase.class);
                    adsArrayList.add(adsDatabase);

                    adapter = new MyRecyclerViewAdapter(getContext(),HomeFragment.this,adsArrayList);
                    mFireStoreList.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner filter = (Spinner)parent;
        Spinner filterLoc = (Spinner)parent;
        if(filter.getId() == R.id.spinner)
        {
            if(position==0)
            {
                search("");
            }else{
                String p=parent.getSelectedItem().toString().trim().toLowerCase();
                //Toast.makeText(getContext(),""+p,Toast.LENGTH_SHORT).show();
                searchCategory(p);
            }
        }
        /*if(filterLoc.getId() == R.id.spinner2)
        {
            if(position==0)
            {
                search("");
            }else{
                String p=parent.getSelectedItem().toString().trim().toLowerCase();
                Toast.makeText(getContext(),""+p,Toast.LENGTH_SHORT).show();
                searchLoc(p);
            }
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)  {
        inflater.inflate(R.menu.search_taskbar, menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }
    }
    /*
    private void loadDataFromFireBase() {
        if(adsArrayList.size()>0)
            adsArrayList.clear();
        db.collection("ads")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot querySnapshot: task.getResult()){
                            Ads ads;
                            ads = new Ads(querySnapshot.getString("email"),querySnapshot.getString("title"),querySnapshot.getString("description"),
                                    querySnapshot.getString("position"),querySnapshot.getString("period"),querySnapshot.getString("location"),
                                    querySnapshot.getString("phone"),querySnapshot.getString("salary"),querySnapshot.getString("url"));
                            adsArrayList.add(ads);
                        }
                        adapter = new MyRecyclerViewAdapter(getContext(),HomeFragment.this,adsArrayList);
                        mFireStoreList.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("Error",e.getMessage());
                    }
                });
    }

    private void setUpFireBase() {
        db=FirebaseFirestore.getInstance();
    }
    */
}
