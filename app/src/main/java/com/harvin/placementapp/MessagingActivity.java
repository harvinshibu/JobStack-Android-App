package com.harvin.placementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harvin.placementapp.Adapter.MessageAdapter;
import com.harvin.placementapp.Model.Chats;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingActivity extends AppCompatActivity {
    CircleImageView prof_img;
    TextView username;
    EditText send_txt;
    ImageButton send_btn;
    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;
    String usersId;
    MessageAdapter messageAdapter;
    ArrayList<Chats> chatsArrayList;
    RecyclerView recyclerView;
    ValueEventListener seenListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        prof_img=findViewById(R.id.prof_img);
        username=findViewById(R.id.username);
        send_txt=findViewById(R.id.text_send);
        send_btn=findViewById(R.id.btn_send);

        recyclerView=findViewById(R.id.msg_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent=getIntent();
        usersId=intent.getStringExtra("userId");
        fuser= FirebaseAuth.getInstance().getCurrentUser();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=send_txt.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(),usersId,msg);
                }
                else{
                    Toast.makeText(MessagingActivity.this,"You can't send empty message",Toast.LENGTH_SHORT).show();
                }
                send_txt.setText("");
            }
        });


        reference= (DatabaseReference) FirebaseDatabase.getInstance().getReference("users").child(usersId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                username.setText(""+snapshot.child("name").getValue().toString());
                prof_img.setImageResource(R.mipmap.ic_launcher);
                readMessages(fuser.getUid(),usersId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seenMessage(usersId);

    }

    private void seenMessage(final String usersId){
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chats chats=dataSnapshot.getValue(Chats.class);
                    if(chats.getReceiver().equals(fuser.getUid()) && chats.getSender().equals(usersId)){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen", true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender,String receiver,String message)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);
        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(receiver);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(receiver);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Multiple same users in Chats
        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(receiver)
                .child(fuser.getUid());
        chatRefReceiver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatRefReceiver.child("id").setValue(fuser.getUid());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessages(final String myid, String userid){
        chatsArrayList=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Chats chats=dataSnapshot.getValue(Chats.class);
                    if(chats.getReceiver().equals(myid) && chats.getSender().equals(userid) || chats.getReceiver().equals(userid) && chats.getSender().equals(myid)){
                        chatsArrayList.add(chats);
                    }
                }
                messageAdapter=new MessageAdapter(MessagingActivity.this,chatsArrayList);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void status(String status){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        status("offline");
    }
}