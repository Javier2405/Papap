package com.example.papap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdapterBaby extends RecyclerView.Adapter<AdapterBaby.ViewHolder> implements View.OnClickListener{

    LayoutInflater inflater;
    ArrayList<Baby> model;
    private View.OnClickListener listener;
    FirebaseFirestore fStore;
    String userID;

    public AdapterBaby(Context context, ArrayList<Baby> model, String userID){
        this.inflater= LayoutInflater.from(context);
        this.model = model;
        this.fStore = FirebaseFirestore.getInstance();
        this.userID = userID;
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
        holder.deleteBaby.setOnClickListener((v) -> {
            CollectionReference collectionReference = fStore.collection("users").document(this.userID).collection("bebe");
            Query query = collectionReference.whereEqualTo("id", model.get(position).getId());
            Log.d("pruebaDelete","Me presionaron: "+model.get(position).getId());
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("pruebaDelete", document.getId() + " => " + document.getData());
                        DocumentReference doc = collectionReference.document(document.getId());
                        doc.delete();
                        Toast.makeText(v.getContext(), "El bebe ha sido eliminado", Toast.LENGTH_LONG).show();
                        Fragment newFragment = new MainFragment();
                        FragmentTransaction transaction = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                } else {
                    Log.d("pruebaDelete", "Error getting documents: ", task.getException());
                }
            });

        });
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
        Button deleteBaby;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            babyName = itemView.findViewById(R.id.babyName);
            age = itemView.findViewById(R.id.age);
            gender = itemView.findViewById(R.id.gender);
            babyDislikes = itemView.findViewById(R.id.babyDislikes);
            babyAllergic = itemView.findViewById(R.id.babyAllergic);
            deleteBaby = itemView.findViewById(R.id.delete_baby);
        }
    }
}
