package com.example.papap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView name, email, number;
    Button logoutButton, dietButton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //Drawer, navigation view & toolbar
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;

    //fragments
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        //establecer evento on click al navView
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //cargar fragment principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new MainFragment());
        fragmentTransaction.commit();


        /*
        name = findViewById(R.id.nameView);
        email = findViewById(R.id.emailView);
        number = findViewById(R.id.numberView);
        logoutButton = findViewById(R.id.logoutButton);
        dietButton = findViewById(R.id.dietButton);

         */
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        /*
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
                        if(documentSnapshot.getBoolean("tarjeta") && documentSnapshot.getBoolean("bebe")){
                            Intent changeActivity = new Intent(getApplicationContext(), Paps.class);
                            changeActivity.putExtra("userid",userID);
                            startActivity(changeActivity);
                        }else if(!documentSnapshot.getBoolean("tarjeta")){
                            Intent changeActivity = new Intent(getApplicationContext(), CreditCard.class);
                            changeActivity.putExtra("userid",userID);
                            startActivity(changeActivity);
                        }else if(!documentSnapshot.getBoolean("bebe")){
                            Intent changeActivity = new Intent(getApplicationContext(), Information.class);
                            changeActivity.putExtra("userid",userID);
                            startActivity(changeActivity);
                        }
                    }
                }
            });
        });

         */
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new MainFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.bebes){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new BebesFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.profile){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new PerfilFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.payment){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new MetodoPFragment());
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.exit){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        return false;
    }

    public String getUserID(){
        return this.userID;
    }
}
