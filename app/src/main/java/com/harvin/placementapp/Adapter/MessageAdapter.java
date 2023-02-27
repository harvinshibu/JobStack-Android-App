package com.harvin.placementapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.harvin.placementapp.Model.Chats;
import com.harvin.placementapp.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    Activity MessageActivity;
    ArrayList<Chats> chatsArrayList;
    Context mContext;
    FirebaseUser fuser;
    public MessageAdapter(Context mContext, ArrayList<Chats> chatsArrayList) {
        this.mContext=mContext;
        this.chatsArrayList=chatsArrayList;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent,false);
            return new MessageAdapter.MessageViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent,false);
            return new MessageAdapter.MessageViewHolder(view);
        }
        /*LayoutInflater layoutInflater=LayoutInflater.from(MessageActivity.getApplication());
        View view = layoutInflater.inflate(R.layout.activity_messaging, parent,false);
        return new MessageAdapter.MessageViewHolder(view);
         */

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Chats chats=chatsArrayList.get(position);
        holder.show_message.setText(chats.getMessage());
        holder.profile_img.setImageResource(R.mipmap.ic_launcher);
        if (position == chatsArrayList.size()-1){
            if(chats.isIsseen()){
                holder.txt_seen.setText("Seen");
            }
            else{
                holder.txt_seen.setText("Delivered");
            }
        }
        else{
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatsArrayList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_img;
        public TextView txt_seen;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_img=itemView.findViewById(R.id.profile_image);
            txt_seen=itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(chatsArrayList.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}
