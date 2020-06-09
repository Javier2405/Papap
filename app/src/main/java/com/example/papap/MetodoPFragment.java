package com.example.papap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    ArrayList<Payment> payment_list;
    FirebaseFirestore fStore;
    Button add_card;
    String userID;

    //Paypal
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static final String PAYPAL_CLIENT_ID = "Ac7NYWPGy08F34WxYqTh9MaqElwwEJrCVdEN4Qq7meOZCLkFszbMHkpbD_Y2NKYLURXeAIK2T8F6IGqG";
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PAYPAL_CLIENT_ID);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MetodoPFragment() {
        // Required empty public constructor
    }

    public MetodoPFragment(String details, String user) throws JSONException {
        fStore = FirebaseFirestore.getInstance();

        Log.d("INSERTION", "Entering constructor");
        JSONObject json = new JSONObject(details).getJSONObject("response");
        final DocumentReference documentReference = fStore.collection("users").document(user);

        //set data into HashMap
        Map<String, Object> payment = new HashMap<>();
        payment.put("id", json.getString("id"));
        payment.put("state", json.getString("state"));
        payment.put("amount", "200");
        //insert to data base
        documentReference.collection("payments").document().set(payment).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        this.payment_list = new ArrayList<>();
        fStore = FirebaseFirestore.getInstance();
        MainActivity activity = (MainActivity) getActivity();
        this.userID = activity.getUserID();

        //Iniciar servicio de Paypal
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        getActivity().startService(intent);

        loadData();

        this.add_card = view.findViewById(R.id.add_card);
        this.add_card.setOnClickListener((v) -> {
            /*Intent changeActivity = new Intent(getActivity().getApplicationContext(), CreditCard.class);
            changeActivity.putExtra("userid",userID);
            changeActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(changeActivity);*/
            processPayment();
        });
        return view;
    }

    public void loadData(){
        DocumentReference documentReference = fStore.collection("users").document(userID);

        //QUERY DE TODOS LOS DOCUMENTOS QUE TIENE UNA COLECCION DE DATOS EN FIREBASE
        documentReference.collection("payments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d("Error citas","Error:"+e.getMessage());
                }else if(documentSnapshot.isEmpty()) {
                    Log.d("Empty list","The list is empty");
                }else{
                    for (DocumentChange documentChange : documentSnapshot.getDocumentChanges()) {
                        Payment payment = new Payment(documentChange.getDocument().getString("id"),
                                documentChange.getDocument().getString("state"),
                                Integer.parseInt(documentChange.getDocument().getString("amount")));
                        payment_list.add(payment);
                        Log.d("LIST SIZE", Integer.toString(payment_list.size()));
                    }
                    showData();
                }
            }
        });
    }

    public void showData(){
        if(getActivity()!=null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            Log.d("LIST SIZE SHOW", Integer.toString(this.payment_list.size()));
            this.adapterCard = new AdapterCard(getContext(), this.payment_list);
            recyclerView.setAdapter(this.adapterCard);
        }

    }


    private void processPayment() {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(200),"MXN","Payment by "+this.userID,PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PAYPAL_REQUEST_CODE){
            if(resultCode==Activity.RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation!= null){
                    try{
                        String details = confirmation.toJSONObject().toString(4);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, new MetodoPFragment(details,this.userID));
                        fragmentTransaction.commit();
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(resultCode== Activity.RESULT_CANCELED){
                Toast.makeText(getActivity(),"Transacción cancelada",Toast.LENGTH_LONG).show();
            }else if(resultCode== PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(getActivity(),"Transacción invalida",Toast.LENGTH_LONG).show();
            }

        }

    }
}
