package com.example.papap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BebesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BebesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AdapterBaby adapterBaby;
    private RecyclerView recyclerView;
    ArrayList<Baby> baby_list;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    Button add_baby;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BebesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BebesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BebesFragment newInstance(String param1, String param2) {
        BebesFragment fragment = new BebesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bebes, container, false);
        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.baby_list = new ArrayList<>();
        fStore = FirebaseFirestore.getInstance();
        MainActivity activity = (MainActivity) getActivity();
        this.userID = activity.getUserID();
        loadData();

        this.add_baby = view.findViewById(R.id.add_baby);
        this.add_baby.setOnClickListener((v) -> {
            Intent changeActivity = new Intent(getActivity().getApplicationContext(), Information.class);
            changeActivity.putExtra("userid",userID);
            startActivity(changeActivity);
        });
        return view;
    }

    public void loadData(){
        DocumentReference documentReference = fStore.collection("users").document(userID);

        //QUERY DE TODOS LOS DOCUMENTOS QUE TIENE UNA COLECCION DE DATOS EN FIREBASE
        documentReference.collection("bebe").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d("Error citas","Error:"+e.getMessage());
                }else{
                        for (DocumentChange documentChange : documentSnapshot.getDocumentChanges()) {
                            //Log.d("citas", documentChange.getDocument().getString("Nombre"));
                            Baby baby = new Baby(documentChange.getDocument().getString("Nombre"),
                                    documentChange.getDocument().getString("Edad"),
                                    documentChange.getDocument().getString("Genero"),
                                    documentChange.getDocument().getString("Disgustos"),
                                    documentChange.getDocument().getString("Alergias"));
                            baby_list.add(baby);
                            Log.d("LIST SIZE", Integer.toString(baby_list.size()));
                        }
                    showData();
                }
            }
        });
        /*documentReference.collection("bebe").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("empty", "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<Baby> types = documentSnapshots.toObjects(Baby.class);

                            // Add all to your list
                            baby_list.addAll(types);
                            Log.d("lista", "onSuccess: " + baby_list);
                            showData();
                        }
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("error citas", e.getMessage());
                    }
                });*/
    }

    public void showData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("LIST SIZE SHOW", Integer.toString(this.baby_list.size()));
        this.adapterBaby = new AdapterBaby(getContext(), this.baby_list);
        recyclerView.setAdapter(this.adapterBaby);
        this.adapterBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Nombre: " + baby_list.get(recyclerView.getChildAdapterPosition(v)).getName() +
                        "\nEdad: " + baby_list.get(recyclerView.getChildAdapterPosition(v)).getAge() +
                        "\nGenero: " + baby_list.get(recyclerView.getChildAdapterPosition(v)).getGender() +
                        "\nAlergias: " + baby_list.get(recyclerView.getChildAdapterPosition(v)).getAllergic() +
                        "\nDisgustos: " + baby_list.get(recyclerView.getChildAdapterPosition(v)).getDislikes());
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle("Detalles");
                alert.show();
            }
        });

    }
}
