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
import com.google.firebase.database.ValueEventListener;
import com.harvin.placementapp.Adapter.UserAdapter;
import com.harvin.placementapp.Model.Chatlist;
import com.harvin.placementapp.Model.Users;
import com.harvin.placementapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<Users> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private ArrayList<Chatlist> userList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView=v.findViewById(R.id.recycler_chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        userList=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot snapshot1 :snapshot.getChildren()){
                    Chatlist chatlist=snapshot1.getValue(Chatlist.class);
                    userList.add(chatlist);
                }
                chatList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chats chats=dataSnapshot.getValue(Chats.class);
                    if(chats.getSender().equals(fuser.getUid())){
                        userList.add(chats.getReceiver());
                    }
                    if(chats.getReceiver().equals(fuser.getUid())){
                        userList.add(chats.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
         */
        return v;
    }



    private  void chatList(){
        mUsers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot1 :snapshot.getChildren()){
                    Users users=snapshot1.getValue(Users.class);
                    for(Chatlist chatlist : userList){
                        if(users.getId().equals(chatlist.getId())){
                            mUsers.add(users);
                        }
                    }
                }
                userAdapter=new UserAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*
    private void readChats(){
        mUsers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users=dataSnapshot.getValue(Users.class);
                    try{
                        for (String id : userList) {
                            if (users.getId().equals(id)) {
                                if (mUsers.size() != 0) {
                                    for (Users users12 : mUsers) {
                                        if (!users.getId().equals(users12.getId())) {
                                            mUsers.add(users);
                                        }
                                    }
                                }
                                else
                                {
                                    mUsers.add(users);
                                }
                            }
                        }
                    }
                    catch (Exception e){
                    }
                }
                userAdapter=new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
     */
    private  void status(String status){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            status("online");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            status("offline");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            status("offline");
        }
    }
}

