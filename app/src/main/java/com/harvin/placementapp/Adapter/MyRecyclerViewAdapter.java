package com.harvin.placementapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.harvin.placementapp.Model.AdsDatabase;
import com.harvin.placementapp.R;
import com.harvin.placementapp.SelectAdsActivity;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyRecyclerViewHolder> {

    Fragment homeFragment;
    ArrayList<AdsDatabase> adsArrayList;
    Context mContext;

    public MyRecyclerViewAdapter(Context mContext,Fragment homeFragment, ArrayList<AdsDatabase> userArrayList) {
        this.mContext=mContext;
        this.homeFragment=homeFragment;
        this.adsArrayList=userArrayList;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(homeFragment.getContext());
        View view = layoutInflater.inflate(R.layout.home_row, parent,false);
        return new MyRecyclerViewHolder(view);

    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        holder.titleTV.setText(adsArrayList.get(position).getTitle());
        holder.worktypeTV.setText(adsArrayList.get(position).getDescription());
        holder.addressTV.setText(adsArrayList.get(position).getLocation());
        holder.emailTV.setText(adsArrayList.get(position).getEmail());
        holder.phoneiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+adsArrayList.get(position).getPhone()));
                homeFragment.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return adsArrayList.size();
    }

    class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTV;
        public TextView worktypeTV;
        public TextView addressTV;
        public TextView emailTV;
        public ImageButton phoneiv;
        public TextView btnTv;
        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV=itemView.findViewById(R.id.titleTV);
            worktypeTV=itemView.findViewById(R.id.worktypeTV);
            addressTV=itemView.findViewById(R.id.postdateTV);
            emailTV=itemView.findViewById(R.id.expirydateTV);
            phoneiv=itemView.findViewById(R.id.phoneiv);
            btnTv=itemView.findViewById(R.id.btnTV);
            btnTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, SelectAdsActivity.class);
                    intent.putExtra("title", adsArrayList.get(getAdapterPosition()).getTitle());
                    intent.putExtra("email",adsArrayList.get(getAdapterPosition()).getEmail());
                    intent.putExtra("des",adsArrayList.get(getAdapterPosition()).getDescription());
                    intent.putExtra("sal",adsArrayList.get(getAdapterPosition()).getSalary());
                    intent.putExtra("period",adsArrayList.get(getAdapterPosition()).getPeriod());
                    intent.putExtra("pos",adsArrayList.get(getAdapterPosition()).getPosition());
                    intent.putExtra("loc",adsArrayList.get(getAdapterPosition()).getLocation());
                    intent.putExtra("url",adsArrayList.get(getAdapterPosition()).getUrl());
                    intent.putExtra("phone",adsArrayList.get(getAdapterPosition()).getPhone());
                    intent.putExtra("usersId",adsArrayList.get(getAdapterPosition()).getIdUser());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}




