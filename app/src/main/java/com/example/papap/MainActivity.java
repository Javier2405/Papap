package com.example.papap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    TextView name, email, number;
    Button logoutButton, dietButton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.nameView);
        email = findViewById(R.id.emailView);
        number = findViewById(R.id.numberView);
        logoutButton = findViewById(R.id.logoutButton);
        dietButton = findViewById(R.id.dietButton);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        //getting the user
        final DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(MainActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d("ERROR","Error:"+e.getMessage());
                }else{
                    name.setText("Nombre: "+ documentSnapshot.getString("Nombre"));
                    email.setText("E-mail: "+ documentSnapshot.getString("Correo Electronico"));
                    number.setText("Telefono: "+ documentSnapshot.getString("Telefono"));
                }

            }

        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        //Dieta
        dietButton.setOnClickListener((v) -> {
            documentReference.addSnapshotListener(MainActivity.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e!=null){
                        Log.d("ERROR","Error:"+e.getMessage());
                    }else{
                        if(documentSnapshot.getBoolean("tarjeta")){
                            startActivity(new Intent(getApplicationContext(), Information.class));
                        }else{
                            Intent changeActivity = new Intent(getApplicationContext(), CreditCard.class);
                            changeActivity.putExtra("userid",userID);
                            startActivity(changeActivity);
                        }
                    }
                }
            });
        });

    }
}
