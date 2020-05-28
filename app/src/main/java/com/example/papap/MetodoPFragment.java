package com.example.papap;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
 * Use the {@link MetodoPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MetodoPFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AdapterCard adapterCard;
    private RecyclerView recyclerView;
    ArrayList<Card> card_list;
    FirebaseFirestore fStore;
    Button add_card;
    String userID;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MetodoPFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MetodoPFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MetodoPFragment newInstance(String param1, String param2) {
        MetodoPFragment fragment = new MetodoPFragment();
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
        View view = inflater.inflate(R.layout.fragment_metodo_p, container, false);
        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.card_list = new ArrayList<>();
        fStore = FirebaseFirestore.getInstance();
        MainActivity activity = (MainActivity) getActivity();
        this.userID = activity.getUserID();
        loadData();

        this.add_card = view.findViewById(R.id.add_card);
        this.add_card.setOnClickListener((v) -> {
            Intent changeActivity = new Intent(getActivity().getApplicationContext(), CreditCard.class);
            changeActivity.putExtra("userid",userID);
            changeActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(changeActivity);
        });
        return view;
    }

    public void loadData(){
        DocumentReference documentReference = fStore.collection("users").document(userID);

        //QUERY DE TODOS LOS DOCUMENTOS QUE TIENE UNA COLECCION DE DATOS EN FIREBASE
        documentReference.collection("tarjeta").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d("Error citas","Error:"+e.getMessage());
                }else if(documentSnapshot.isEmpty()) {
                    Log.d("Empty list","The list is empty");
                }else{
                    for (DocumentChange documentChange : documentSnapshot.getDocumentChanges()) {
                        Card card = new Card(documentChange.getDocument().getString("Nombre del titular"),
                                documentChange.getDocument().getString("Numero de tarjeta"),
                                documentChange.getDocument().getString("Vencimiento"),
                                documentChange.getDocument().getBoolean("selected"));
                        card_list.add(card);
                        Log.d("LIST SIZE", Integer.toString(card_list.size()));
                    }
                    showData();
                }
            }
        });
    }

    public void showData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("LIST SIZE SHOW", Integer.toString(this.card_list.size()));
        this.adapterCard= new AdapterCard(getContext(), this.card_list);
        recyclerView.setAdapter(this.adapterCard);

    }
}
