package com.harvin.placementapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harvin.placementapp.MessagingActivity;
import com.harvin.placementapp.Model.Users;
import com.harvin.placementapp.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    ArrayList<Users> userArrayList;
    Context mContext;
    private boolean ischat;

    public UserAdapter(Context mContext, ArrayList<Users> userArrayList,boolean ischat) {
        this.mContext=mContext;
        this.userArrayList=userArrayList;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent,false);
        return new UserAdapter.UserViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        Users users=userArrayList.get(position);
        holder.username.setText(users.getName());
        holder.profile_img.setImageResource(R.mipmap.ic_launcher);
        if(ischat){
            if(users.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }
            else{
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MessagingActivity.class);
                intent.putExtra("userId",users.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_img;
        public ImageView img_on;
        public ImageView img_off;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username1);
            profile_img=itemView.findViewById(R.id.prof_img1);
            img_off=itemView.findViewById(R.id.img_off);
            img_on=itemView.findViewById(R.id.img_on);
        }
    }

}

