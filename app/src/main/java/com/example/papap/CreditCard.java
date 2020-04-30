package com.example.papap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreditCard extends AppCompatActivity {
    EditText creditCardName, creditCardNumber, dueDate;
    Button registerButton;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);

        creditCardName = findViewById(R.id.nameFieldR);
        creditCardNumber = findViewById(R.id.creditCardNumberFieldR);
        dueDate = findViewById(R.id.dueDateFieldR);
        registerButton = findViewById(R.id.registerButton);

        fStore = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener((v) -> {

            if(TextUtils.isEmpty(creditCardName.getText().toString())){
                creditCardName.setError("Nombre del titular es requerido");
                return;
            }
            if(TextUtils.isEmpty(creditCardNumber.getText().toString())){
                creditCardNumber.setError("Numero de tarjeta es requerido");
                return;
            }
            if(TextUtils.isEmpty(dueDate.getText().toString())){
                dueDate.setError("Fecha de vencimiento es requerida");
                return;
            }

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String userID = extras.getString("userid");

            //getting the user
            final DocumentReference documentReference = fStore.collection("users").document(userID);

            //set data into HashMap
            Map<String, Object> tarjeta = new HashMap<>();
            tarjeta.put("Nombre del titular", creditCardName.getText().toString());
            tarjeta.put("Numero de tarjeta", creditCardNumber.getText().toString());
            tarjeta.put("Vencimiento", dueDate.getText().toString());
            //insert to data base
            documentReference.collection("tarjeta").document().set(tarjeta).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("INSERTION", "Info was added satisfactory");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("INSERTION", "onFailure: "+ e.toString());
                }
            });
            Map<String, Object> user = new HashMap<>();
            user.put("tarjeta", true);

            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("INSERTION", "Turned credit card to true");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("INSERTION", "onFailure: "+ e.toString());
                }
            });

            Toast.makeText(this, "La tarjeta de credito fue crada con éxito", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Information.class));
        });

    }
}
