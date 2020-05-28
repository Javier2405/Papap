package com.example.papap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.ViewHolder> implements View.OnClickListener{

    LayoutInflater inflater;
    ArrayList<Card> model;
    private View.OnClickListener listener;

    public AdapterCard(Context context, ArrayList<Card> model){
        this.inflater= LayoutInflater.from(context);
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_credit_cards,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String titular = model.get(position).getTitular();
        String fVencimiento = model.get(position).getVencimiento();
        String numTarjeta = model.get(position).getCardnum();
        boolean selected = model.get(position).isSelected();

        if(selected){
           holder.select.setVisibility(View.INVISIBLE);
           holder.recycler.setBackgroundColor(holder.color);
        }

        holder.titular.setText(titular);
        holder.fVencimiento.setText(fVencimiento);
        holder.numTarjeta.setText(numTarjeta);
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
        TextView titular, fVencimiento, numTarjeta;
        Button select;
        CardView recycler;
        int color;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titular = itemView.findViewById(R.id.titular);
            fVencimiento = itemView.findViewById(R.id.fechaVencimiento);
            numTarjeta = itemView.findViewById(R.id.numeroTarjeta);
            select = itemView.findViewById(R.id.select_card);
            recycler = itemView.findViewById(R.id.cardView);
            color = itemView.getResources().getColor(R.color.colorSelected);
        }
    }
}