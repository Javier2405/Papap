package com.example.papap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText fullNameRF, emailRF, passwordRF;
    Button registerButton;
    TextView login;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize components
        fullNameRF = findViewById(R.id.fullNameFieldR);
        emailRF = findViewById(R.id.emailFieldR);
        passwordRF = findViewById(R.id.passRegister);
        registerButton = findViewById(R.id.registerButton);
        login = findViewById(R.id.loginR);
        progressBar = findViewById(R.id.progressBarR);
        //initialize firebase
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailRF.getText().toString();
                String password = passwordRF.getText().toString();

                if(TextUtils.isEmpty(email)){
                    emailRF.setError("Correo es requerido");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    passwordRF.setError("Contraseña es requerida");
                    return;
                }

                if (password.length() < 6){
                    passwordRF.setError("Contraseña deber ser al menos de 6 caracteres");
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
