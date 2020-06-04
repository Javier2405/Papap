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

    ArrayList<Baby> baby_list;
    String[] babies;

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

        MainActivity activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        db = FirebaseFirestore.getInstance();

        dia = view.findViewById(R.id.spinner_dia);
        ArrayAdapter<String> adapterDia = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, DIAS_OPTIONS);
        dia.setAdapter(adapterDia);
        bebe = view.findViewById(R.id.spinner_bebe);
        this.baby_list = new ArrayList<>();
        this.paps = new ArrayList<>();
        this.userID = activity.getUserID();
        loadPaps(activity,view); //Loadpaps carga todas las paps de la base de datos, despues carga todos los bebes y finalmente hace el set de las dietas para cada dia

        hora = view.findViewById(R.id.textView_hora);
        nombre_receta = view.findViewById(R.id.textView_nombre_receta);
        calorias = view.findViewById(R.id.textView_calorias);

        instrucciones = view.findViewById(R.id.instrucciones);
        ingredientes = view.findViewById(R.id.ingredientes);

        btn_siguiente = view.findViewById(R.id.btn_siguiente);
        btn_anterior = view.findViewById(R.id.btn_anterior);

        return view;
    }

    private void loadPaps(MainActivity activity, View view){
        //Obtengo todos los documentos de la collection paps de la base de datos
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
                        Log.d("PAP", pap.getNombre());
                    }
                    //Termino de obtener los documents asi que procedo a hacer el set de las papillas
                    setPaps(paps);

                    //Llamo al metodo para obtener los bebes una vez hago el set de las papillas
                    loadBabies(activity,view);

                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void loadBabies(MainActivity activity, View view){
        //Obteniendo todos los documents de la collection bebe del usuario
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
                        addBaby_list(baby);
                        Log.d("BABY", baby.getName());
                    }
                    //Termino de obtener los documents asi que procedo a hacer el set del nombre de los bebes para mi spinner

                    String[] babiesName = new String[sizeBaby_list()];
                    for (int i = 0;babiesName.length>i;i++){
                        babiesName[i] = getBaby_list(i).getName();

                    }

                    Spinner bebe = view.findViewById(R.id.spinner_bebe);
                    ArrayAdapter<String> adapterBebe = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_dropdown_item_1line, babiesName);
                    bebe.setAdapter(adapterBebe);
                    setBabies(babiesName);

                    //Llamo al metodo para crear el array de dietas de cada bebe

                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void addBaby_list(Baby baby) {
        this.baby_list.add(baby);
    }

    public Baby getBaby_list(int i) {
        return baby_list.get(i);
    }

    public int sizeBaby_list() {
        return this.baby_list.size();
    }

    public void setBabies(String[] babies) {
        this.babies = babies;
    }

    public void setPaps(ArrayList<Pap> paps) {
        this.paps = paps;
    }

    //Array para el spinner de dias
    private static final String[] DIAS_OPTIONS = new String[] {
            "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"
    };


}
