package com.example.papap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    FirebaseFirestore db;
    String userID;

    public void addBaby_list(Baby baby) {
        this.baby_list.add(baby);
    }

    public Baby getBaby_list(int i) {
        return baby_list.get(i);
    }

    public int sizeBaby_list() {
        return this.baby_list.size();
    }

    ArrayList<Baby> baby_list;

    public void setBabies(String[] babies) {
        this.babies = babies;
    }

    String[] babies;

    public void setPaps(ArrayList<Pap> paps) {
        this.paps = paps;
    }

    ArrayList<Pap> paps;

    Spinner dia;
    Spinner bebe;

    TextView hora;
    TextView nombre_receta;
    TextView calorias;

    ListView instrucciones;
    ListView ingredientes;

    Button btn_siguiente;
    Button btn_anterior;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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

        //Agregar valores a spinner HECHO
        //Obtener array de papilla almacenadas HECHO
        //Obtener array de papillas -> Obtener array de bebes y set de spinner HECHO -> Calcular las dietas de lo obtenido
        //Obtener el array de papillas para el bebe
        //Al cambiar el bebe se recalcula
        //Los botones hacen un set a la información
        //El spinner hace un set a la información

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        db = FirebaseFirestore.getInstance();

        dia = view.findViewById(R.id.spinner_dia);
        bebe = view.findViewById(R.id.spinner_bebe);

        hora = view.findViewById(R.id.textView_hora);
        nombre_receta = view.findViewById(R.id.textView_nombre_receta);
        calorias = view.findViewById(R.id.textView_calorias);

        instrucciones = view.findViewById(R.id.instrucciones);
        ingredientes = view.findViewById(R.id.ingredientes);

        btn_siguiente = view.findViewById(R.id.btn_siguiente);
        btn_anterior = view.findViewById(R.id.btn_anterior);

        this.baby_list = new ArrayList<>();
        this.paps = new ArrayList<>();

        MainActivity activity = (MainActivity) getActivity();

        ArrayAdapter<String> adapterDia = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, DIAS_OPTIONS);
        dia.setAdapter(adapterDia);

        this.userID = activity.getUserID();

        loadPaps(activity,view);

        return view;
    }

    private void loadBabies(MainActivity activity, View view){
        db.collection("users").document(userID).collection("bebe").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Baby baby = new Baby(document.getString("Nombre"),
                                document.getString("Edad"),
                                document.getString("Genero"),
                                document.getString("Disgustos"),
                                document.getString("Alergias"));
                        //babyAdd(baby,activity,view);
                        addBaby_list(baby);
                        Log.d("LIST SIZE", Integer.toString(baby_list.size()));
                    }
                    Spinner bebe;
                    String[] babiesName = new String[sizeBaby_list()];
                    for (int i = 0;babiesName.length>i;i++){
                        babiesName[i] = getBaby_list(i).getName();

                    }
                    bebe = view.findViewById(R.id.spinner_bebe);
                    ArrayAdapter<String> adapterBebe = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_dropdown_item_1line, babiesName);
                    bebe.setAdapter(adapterBebe);

                    setBabies(babiesName);

                } else {
                    Log.d("NO JALO", "Error getting documents: ", task.getException());
                }
            }
        });

        /*DocumentReference documentReference = db.collection("users").document(userID);
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
                        babyAdd(baby,activity,view);
                        Log.d("LIST SIZE", Integer.toString(baby_list.size()));
                    }
                }
            }
        });*/
    }

    private void babyAdd(Baby baby,MainActivity activity, View view){
        Spinner bebe;

        this.baby_list.add(baby);
        String[] babiesName = new String[this.baby_list.size()];
        for (int i = 0;babiesName.length>i;i++){
            babiesName[i] = this.baby_list.get(i).getName();
        }
        bebe = view.findViewById(R.id.spinner_bebe);
        ArrayAdapter<String> adapterBebe = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, babiesName);
        bebe.setAdapter(adapterBebe);

        babies = babiesName;

    }

    private void loadPaps(MainActivity activity, View view){


        db.collection("paps").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private ArrayList<Pap> paps = new ArrayList<>();

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        List<String> ingredientes = (List<String>) document.get("Ingredientes");
                        List<String> pasos = (List<String>) document.get("Pasos");
                        Pap pap = new Pap(document.getString("Nombre"),Integer.parseInt(document.getString("Calorias")),ingredientes,pasos);
                        paps.add(pap);
                        Log.d("SI JALO?", pap.getNombre());
                    }
                    setPaps(paps);
                    loadBabies(activity,view);

                } else {
                    Log.d("NO JALO", "Error getting documents: ", task.getException());
                }
            }
        });




    }

    private static final String[] DIAS_OPTIONS = new String[] {
            "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"
    };


}
