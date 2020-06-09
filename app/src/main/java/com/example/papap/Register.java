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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText fullNameRF, emailRF, passwordRF, phoneRF;
    Button registerButton;
    TextView login;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize components
        fullNameRF = findViewById(R.id.nameF);
        emailRF = findViewById(R.id.creditCardNumberFieldR);
        passwordRF = findViewById(R.id.dueDateFieldR);
        phoneRF = findViewById(R.id.phoneField);
        registerButton = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginR);
        progressBar = findViewById(R.id.progressBarR);

        //initialize firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = fullNameRF.getText().toString();
                final String email = emailRF.getText().toString();
                String password = passwordRF.getText().toString();
                final String phone = phoneRF.getText().toString();

                if(TextUtils.isEmpty(fullName)){
                    fullNameRF.setError("Nombre completo es requerido");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    emailRF.setError("Correo es requerido");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailRF.setError("Correo no es valido");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    passwordRF.setError("Contraseña es requerida");
                    return;
                }

                if (password.length() <= 8){
                    passwordRF.setError("Contraseña deber ser al menos de 8 caracteres");
                    return;
                }

                //start to process data
                //enable progress bar
                progressBar.setVisibility(View.VISIBLE);

                //register user
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
                            //create collection for storing
                            //if collection doesnt exist it will create
                            //we are going to insert the user info in a document by its id
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            //store the data
                            Map<String, Object> user = new HashMap<>();
                            user.put("Nombre", fullName);
                            user.put("Correo Electronico", email);
                            user.put("Telefono", phone);
                            user.put("bebe", false);
                            //insert to data base
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("INSERTION", "onSuccess: user profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("INSERTION", "onFailure: "+ e.toString());
                                }
                            });


                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(Register.this, "Hubo un error al crear al usuario. Intente de nuevo", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}
