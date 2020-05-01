package com.example.papap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Information extends AppCompatActivity {
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        fStore = FirebaseFirestore.getInstance();

        ArrayAdapter<String> adapterEdad = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, EDAD_OPTIONS);
        ArrayAdapter<String> adapterGenero = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, GENERO_OPTIONS);
        ArrayAdapter<String> adapterAlergias = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ALERGIAS_OPTIONS);
        ArrayAdapter<String> adapterDisgustos = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DISGUSTOS_OPTIONS);

        Spinner edad = findViewById(R.id.spinnerEdad);
        edad.setAdapter(adapterEdad);

        Spinner genero = findViewById(R.id.spinnerGenero);
        genero.setAdapter(adapterGenero);

        Spinner alergias = findViewById(R.id.spinnerAlergias);
        alergias.setAdapter(adapterAlergias);

        Spinner disgustos = findViewById(R.id.spinnerDisgustos);
        disgustos.setAdapter(adapterDisgustos);

        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener((v)->{
            String strEdad = String.valueOf(edad.getSelectedItem());
            String strGenero = String.valueOf(genero.getSelectedItem());
            String strAlergias = String.valueOf(alergias.getSelectedItem());
            String strDisgustos = String.valueOf(disgustos.getSelectedItem());

            if(strEdad.equals("Selecciona la edad")){
                Toast.makeText(this, "Seleccionar todos los campos es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }
            if(strGenero.equals("Selecciona el genero")){
                Toast.makeText(this, "Seleccionar todos los campos es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }
            if(strAlergias.equals("Selecciona las alergias")){
                Toast.makeText(this, "Seleccionar todos los campos es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }
            if(strDisgustos.equals("Selecciona los disgustos")){
                Toast.makeText(this, "Seleccionar todos los campos es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String userID = extras.getString("userid");

            //getting the user
            final DocumentReference documentReference = fStore.collection("users").document(userID);

            //set data into HashMap
            Map<String, Object> bebe = new HashMap<>();
            bebe.put("Edad", strEdad);
            bebe.put("Genero", strGenero);
            bebe.put("Alergias", strAlergias);
            bebe.put("Disgustos", strDisgustos);
            //insert to data base
            documentReference.collection("bebe").document().set(bebe).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("INSERTION", "Info was added satisfactory");
                    Intent changeActivity = new Intent(getApplicationContext(), Paps.class);
                    changeActivity.putExtra("userid",userID);
                    startActivity(changeActivity);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("INSERTION", "onFailure: "+ e.toString());
                }
            });

            Map<String, Object> user = new HashMap<>();
            user.put("bebe", true);

            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("INSERTION", "Turned baby to true");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("INSERTION", "onFailure: "+ e.toString());
                }
            });

            Toast.makeText(this, "El bebe fue agregado con exito", Toast.LENGTH_SHORT).show();

        });
    }

    private static final String[] EDAD_OPTIONS = new String[] {
            "Selecciona la edad","6 meses", "7 meses", "8 meses", "9 meses", "10 meses", "11 meses", "12 meses"
    };

    private static final String[] GENERO_OPTIONS = new String[] {
            "Selecciona el genero","Femenino","Masculino"
    };
    private static final String[] ALERGIAS_OPTIONS = new String[] {
            "Selecciona las alergias","Platano", "Mango", "Manzana", "Nuez", "Vainilla", "Huevo", "Leche","Ninguno"
    };
    private static final String[] DISGUSTOS_OPTIONS = new String[] {
            "Selecciona los disgustos","Platano", "Mango", "Manzana", "Nuez", "Vainilla", "Huevo", "Leche","Ninguno"
    };
}
