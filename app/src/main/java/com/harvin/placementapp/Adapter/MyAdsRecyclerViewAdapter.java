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
import com.harvin.placementapp.MyAdsSelectActivity;
import com.harvin.placementapp.R;

import java.util.ArrayList;

public class MyAdsRecyclerViewAdapter extends RecyclerView.Adapter<MyAdsRecyclerViewAdapter.MyAdsRecyclerViewHolder> {

    Fragment homeFragment;
    private ArrayList<AdsDatabase> MyadsArrayList;
    Context mContext;


    public MyAdsRecyclerViewAdapter(Context mContext,Fragment homeFragment, ArrayList<AdsDatabase> userArrayList) {
        this.mContext=mContext;
        this.homeFragment=homeFragment;
        this.MyadsArrayList=userArrayList;
    }

    @NonNull
    @Override
    public MyAdsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(homeFragment.getContext());
        View view = layoutInflater.inflate(R.layout.myads_row, parent,false);
        return new MyAdsRecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyAdsRecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tileTv.setText(MyadsArrayList.get(position).getTitle());
        holder.desTv.setText(MyadsArrayList.get(position).getDescription());
        holder.locTv.setText(MyadsArrayList.get(position).getLocation());
        holder.mailTv.setText(MyadsArrayList.get(position).getEmail());
        holder.phoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+MyadsArrayList.get(position).getPhone()));
                homeFragment.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MyadsArrayList.size();
    }

    class MyAdsRecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView tileTv;
        public TextView locTv;
        public TextView desTv;
        public TextView mailTv;
        public TextView viewBtn;
        public ImageButton phoneTv;
        public MyAdsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tileTv=itemView.findViewById(R.id.tileTv);
            locTv=itemView.findViewById(R.id.locTv);
            desTv=itemView.findViewById(R.id.desTv);
            mailTv=itemView.findViewById(R.id.mailTv);
            viewBtn=itemView.findViewById(R.id.viewBtn);
            phoneTv=itemView.findViewById(R.id.phoneTv);
            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdsDatabase ads= MyadsArrayList.get(getAdapterPosition());
                    Intent intent = new Intent(mContext, MyAdsSelectActivity.class);
                    intent.putExtra("title", MyadsArrayList.get(getAdapterPosition()).getTitle());
                    intent.putExtra("email",MyadsArrayList.get(getAdapterPosition()).getEmail());
                    intent.putExtra("des",MyadsArrayList.get(getAdapterPosition()).getDescription());
                    intent.putExtra("sal",MyadsArrayList.get(getAdapterPosition()).getSalary());
                    intent.putExtra("period",MyadsArrayList.get(getAdapterPosition()).getPeriod());
                    intent.putExtra("pos",MyadsArrayList.get(getAdapterPosition()).getPosition());
                    intent.putExtra("loc",MyadsArrayList.get(getAdapterPosition()).getLocation());
                    intent.putExtra("url",MyadsArrayList.get(getAdapterPosition()).getUrl());
                    intent.putExtra("phone",MyadsArrayList.get(getAdapterPosition()).getPhone());
                    intent.putExtra("idAd",MyadsArrayList.get(getAdapterPosition()).getId());
                    intent.putExtra("ads1", ads);
                    mContext.startActivity(intent);
                }
            });
        }
    }


}

