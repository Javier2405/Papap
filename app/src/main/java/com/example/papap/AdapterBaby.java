package com.example.papap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class AdapterBaby extends RecyclerView.Adapter<AdapterBaby.ViewHolder> implements View.OnClickListener{

    LayoutInflater inflater;
    ArrayList<Baby> model;
    private View.OnClickListener listener;

    public AdapterBaby(Context context, ArrayList<Baby> model){
        this.inflater= LayoutInflater.from(context);
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_bebes,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String babyName = model.get(position).getName();
        String babyAge = model.get(position).getAge();
        String babyGender = model.get(position).getGender();
        String babyAllergic = model.get(position).getAllergic();
        String babyDislikes = model.get(position).getDislikes();

        holder.babyName.setText(babyName);
        holder.age.setText(babyAge);
        holder.gender.setText(babyGender);
        holder.babyAllergic.setText(babyAllergic);
        holder.babyDislikes.setText(babyDislikes);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public void onClick(View v) {
        if(this.listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView babyName, age, gender, babyDislikes, babyAllergic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            babyName = itemView.findViewById(R.id.babyName);
            age = itemView.findViewById(R.id.age);
            gender = itemView.findViewById(R.id.gender);
            babyDislikes = itemView.findViewById(R.id.babyDislikes);
            babyAllergic = itemView.findViewById(R.id.babyAllergic);
        }
    }
}
