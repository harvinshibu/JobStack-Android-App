package com.harvin.placementapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harvin.placementapp.Adapter.MyAdsRecyclerViewAdapter;
import com.harvin.placementapp.Model.AdsDatabase;
import com.harvin.placementapp.R;

import java.util.ArrayList;


public class MyAdsFragment extends Fragment {
    RecyclerView mFireStoreList;
    FirebaseFirestore db;
    ArrayList<AdsDatabase> adsArrayList;
    MyAdsRecyclerViewAdapter adapter;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    String TempMail;
    DatabaseReference databaseAds,databaseAds2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myads, container, false);

        mFireStoreList=v.findViewById(R.id.list_myads);
        mFireStoreList.setHasFixedSize(true);
        mFireStoreList.setLayoutManager(new LinearLayoutManager(getContext()));

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        adsArrayList=new ArrayList<>();
        setUpFireBase();
        databaseAds2= (DatabaseReference) FirebaseDatabase.getInstance().getReference("users").child(userId);

        databaseAds2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(AdsDatabase.class) != null) {
                    String key = dataSnapshot.getKey();
                    if (key.equals(userId)) {
                        TempMail = dataSnapshot.child("email").getValue().toString().trim();
                        databaseAds= (DatabaseReference) FirebaseDatabase.getInstance().getReference("ads");
                        Query query = FirebaseDatabase.getInstance().getReference("ads")
                                .orderByChild("idUser")
                                .equalTo(userId);
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        documentReference = fStore.collection("users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    TempMail=documentSnapshot.getString("email");
                    databaseAds= (DatabaseReference) FirebaseDatabase.getInstance().getReference("ads");
                    Query query = FirebaseDatabase.getInstance().getReference("ads")
                            .orderByChild("email")
                            .equalTo(TempMail);
                    query.addListenerForSingleValueEvent(valueEventListener);
                }
                else {
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
        return v;
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                adsArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdsDatabase adsDatabase = snapshot.getValue(AdsDatabase.class);
                    adsArrayList.add(adsDatabase);
                }
                adapter = new MyAdsRecyclerViewAdapter(getContext(),MyAdsFragment.this,adsArrayList);
                mFireStoreList.setAdapter(adapter);

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    /*
    private void loadDataFromFireBase(String ss) {
        if(adsArrayList.size()>0)
            adsArrayList.clear();
        db.collection("ads")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot querySnapshot: task.getResult()){
                            Ads ads1=querySnapshot.toObject(Ads.class);
                            ads1.setId(querySnapshot.getId());
                            adsArrayList.add(ads1);
                        }

                        for(int i=0;i<adsArrayList.size();i++){
                            if(ss.equals(adsArrayList.get(i).getEmail())){
                                Ads ads2=new Ads((adsArrayList.get(i).getEmail()),(adsArrayList.get(i).getTitle()),(adsArrayList.get(i).getDescription()),
                                        (adsArrayList.get(i).getPosition()),(adsArrayList.get(i).getPeriod()),(adsArrayList.get(i).getLocation()),
                                        (adsArrayList.get(i).getPhone()),(adsArrayList.get(i).getSalary()),(adsArrayList.get(i).getUrl()));
                                MyadsArrayList.add(ads2);
                            }
                        }
                        adapter = new MyAdsRecyclerViewAdapter(getContext(),MyAdsFragment.this,MyadsArrayList);
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

     */
    private void setUpFireBase() {
        db=FirebaseFirestore.getInstance();
    }

}