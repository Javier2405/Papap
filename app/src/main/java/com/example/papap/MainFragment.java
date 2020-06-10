package com.example.papap;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment{
    FirebaseFirestore db;
    String userID;

    String comida = "Mañana";

    LoadingDialog loadingDialog;

    ArrayList<Baby> baby_list;
    String[] babies;
    HashMap<Baby,ArrayList<Diet>> dietPerBaby;

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
        //Obtener array de papillas -> Obtener array de bebes y set de spinner -> Calcular las dietas de lo obtenido HECHO
        //Hacer set de la información HECHO
        //Los botones hacen un set a la información HECHO
        //El spinner hace un set a la información HECHO
        //Agregar alerta de carga HECHO

        MainActivity activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        db = FirebaseFirestore.getInstance();

        this.setLoadingDialog(new LoadingDialog(getActivity()));

        loadingDialog.startLoadingDialog();

        dia = view.findViewById(R.id.spinner_dia);
        ArrayAdapter<String> adapterDia = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, DIAS_OPTIONS);
        dia.setAdapter(adapterDia);
        bebe = view.findViewById(R.id.spinner_bebe);
        this.baby_list = new ArrayList<>();
        this.paps = new ArrayList<>();
        this.dietPerBaby = new HashMap<>();
        this.userID = activity.getUserID();
        loadPaps(activity,view); //Loadpaps carga todas las paps de la base de datos, despues carga todos los bebes y finalmente hace el set de las dietas para cada dia

        hora = view.findViewById(R.id.textView_hora);
        nombre_receta = view.findViewById(R.id.textView_nombre_receta);
        calorias = view.findViewById(R.id.textView_calorias);

        instrucciones = view.findViewById(R.id.instrucciones);
        ingredientes = view.findViewById(R.id.ingredientes);

        btn_siguiente = view.findViewById(R.id.btn_siguiente);
        btn_anterior = view.findViewById(R.id.btn_anterior);

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getComida()){
                    case "Mañana":
                        setComida("Tarde");
                        break;
                    case "Tarde":
                        setComida("Noche");
                        break;
                    case "Noche":
                        setComida("Mañana");
                        break;
                }
                setData(getBaby(getBebe().getSelectedItem().toString()),Integer.parseInt(getDia().getSelectedItem().toString()));
            }
        });

        btn_anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getComida()){
                    case "Mañana":
                        setComida("Noche");
                        break;
                    case "Tarde":
                        setComida("Mañana");
                        break;
                    case "Noche":
                        setComida("Tarde");
                        break;
                }
                setData(getBaby(getBebe().getSelectedItem().toString()),Integer.parseInt(getDia().getSelectedItem().toString()));
            }
        });

        /*
        VideoView videoView = (VideoView) view.findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(getContext());
        //Uri uri = Uri.parse("https://www.youtube.com/watch?v=4NRXx6U8ABQ");
        videoView.setMediaController(mediaController);
        //videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();


        WebView videoView =(WebView) view.findViewById(R.id.videoView);
        videoView.getSettings().setJavaScriptEnabled(true);
        videoView.getSettings().setPluginState(WebSettings.PluginState.ON);
        videoView.loadUrl("http://www.youtube.com/embed/" + "4NRXx6U8ABQ" + "?autoplay=1&vq=small");
        videoView.setWebChromeClient(new WebChromeClient());

        */
        String frameVideo = "<html><body>Prcedimiento<br><iframe width=\"300\" height=\"200\" src=\"https://www.youtube.com/embed/fQs9r-NaaXg\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        WebView displayYoutubeVideo = (WebView) view.findViewById(R.id.video);
        displayYoutubeVideo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");

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
                                document.getString("Alergias"),
                                document.getString("id"));
                        addBaby_list(baby);
                        Log.d("BABY", baby.getName());
                    }
                    //Termino de obtener los documents asi que procedo a hacer el set del nombre de los bebes para mi spinner

                    String[] babiesName;
                    if(sizeBaby_list()==0){
                        babiesName = new String[1];
                        babiesName[0]="No hay ningun bebe disponible";
                    }else {
                        babiesName = new String[sizeBaby_list()];
                        for (int i = 0; babiesName.length > i; i++) {
                            babiesName[i] = getBaby_list(i).getName();

                        }
                    }

                    Spinner bebe = view.findViewById(R.id.spinner_bebe);
                    ArrayAdapter<String> adapterBebe = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_dropdown_item_1line, babiesName);
                    bebe.setAdapter(adapterBebe);
                    setBabies(babiesName);

                    if(sizeBaby_list()==0){
                        getLoadingDialog().dismissDialog();
                        return;
                    }

                    //Llamo al metodo para crear el array de dietas de cada bebe
                    setDietsPerBaby();

                    //Imprimo las dietas de los engendros
                    printDietsPerBaby();

                    //Hago el set de los datos en la view
                    setData(getBaby(getBebe().getSelectedItem().toString()),Integer.parseInt(getDia().getSelectedItem().toString()));
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void setDietsPerBaby(){
        //Pasar por el array de bebes para hacer el set de la dieta del mes para los bebes
        HashMap<Baby,ArrayList<Diet>> dietPerBaby = new HashMap<>();

        for (int i =0;i<this.baby_list.size();i++){
            //Obtengo la dieta mensual para cade bebe y la voy almacenando
            dietPerBaby.put(this.baby_list.get(i),this.setDiet(this.baby_list.get(i),this.paps));
        }

        this.setDietPerBaby(dietPerBaby);
    }

    private ArrayList<Diet> setDiet(Baby baby,ArrayList<Pap> paps){
        PapsSelector papsSelector = new PapsSelector(baby.getGender(),baby.getAge(),baby.getDislikes());
        ArrayList<Diet> month = new ArrayList<>();

        //Revisar cuales papilla se le pueden dar al bebe almacenarlas y luego usando numeros random dar tres por dia
        ArrayList<Pap> validPaps = new ArrayList<>();
        for (int i = 0;i<paps.size();i++){
            if(papsSelector.validPaps(paps.get(i).getIngredientes())){
                validPaps.add(paps.get(i));
            }
        }

        //Hacemos el set de un numero random y lo usamos para tomar papillas random de las validas
        double random;

        for (int i = 0;i<30;i++){
            random = (Math.random()*((validPaps.size())));

            //Generamos una variable dieta para ese dia y validamos que las calorias sean las correctas
            Diet diet = new Diet();
            while(papsSelector.validCalories(validPaps.get((int) random).getCalorias())){
                random = (Math.random()*((validPaps.size())));
            }
            diet.setEvening(validPaps.get((int) random));

            random = (Math.random()*((validPaps.size())));
            while(papsSelector.validCalories(validPaps.get((int) random).getCalorias())){
                random = (Math.random()*((validPaps.size())));
            }
            diet.setMorning(validPaps.get((int) random));

            random = (Math.random()*((validPaps.size())));
            while(papsSelector.validCalories(validPaps.get((int) random).getCalorias())){
                random = (Math.random()*((validPaps.size())));
            }
            diet.setNigth(validPaps.get((int) random));

            month.add(diet);
        }

        return month;
    }

    //Debugging function
    private void printDietsPerBaby(){
        for (int i = 0;i<this.baby_list.size();i++){
            ArrayList<Diet> month = this.dietPerBaby.get(this.baby_list.get(i));
            Log.d("BABY NAME", this.baby_list.get(i).getName());
            for (int j = 0; j < month.size(); j++){
                Log.d("BABY DIET PER DAY", Integer.toString(j) + ":" + month.get(j).toString());
            }
        }
    }

    private int getBaby(String name){
        int i = 0;

        for(;i<baby_list.size();i++){
            if(baby_list.get(i).getName().equals(name)){
                break;
            }
        }

        return i;
    }

    private void setData(int baby, int day){
        /*Hacer set de la hora
         * Nombre de la receta
         * Calorias
         * Ingredientes
         * Preparación
         * */
        this.getHora().setText(this.comida); //Set de hora
        Diet diet = this.dietPerBaby.get(baby_list.get(baby)).get(day-1); //Obtener la dieta del dia
        switch (getComida()){
            case "Mañana":
                this.getNombre_receta().setText(diet.getMorning().getNombre()); //Set del nombre de la receta
                this.getCalorias().setText(Integer.toString(diet.getMorning().getCalorias()) + " calorias por porción"); //Set de las calorias
                //Preparar el adapter de ingredientes
                String[] array_ingredientes = new String[diet.getMorning().getIngredientes().size()];
                for(int i = 0; i<diet.getMorning().getIngredientes().size();i++){
                    array_ingredientes[i] = diet.getMorning().getIngredientes().get(i);
                }
                ArrayAdapter<String> adapter_ingredientes = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, array_ingredientes);
                this.getIngredientes().setAdapter(adapter_ingredientes); //Set de ingredientes
                //Preparar el adapter de instrucciones
                String[] array_pasos = new String[diet.getMorning().getPasos().size()];
                for(int i = 0; i<diet.getMorning().getPasos().size();i++){
                    array_pasos[i] = diet.getMorning().getPasos().get(i);
                }
                ArrayAdapter<String> adapter_pasos = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, array_pasos);
                this.getInstrucciones().setAdapter(adapter_pasos); //Set de instrucciones
                break;
            case "Tarde":
                this.getNombre_receta().setText(diet.getEvening().getNombre()); //Set del nombre de la receta
                this.getCalorias().setText(Integer.toString(diet.getEvening().getCalorias()) + " calorias por porción"); //Set de las calorias
                //Preparar el adapter de ingredientes
                String[] array_ingredientesT = new String[diet.getEvening().getIngredientes().size()];
                for(int i = 0; i<diet.getEvening().getIngredientes().size();i++){
                    array_ingredientesT[i] = diet.getEvening().getIngredientes().get(i);
                }
                ArrayAdapter<String> adapter_ingredientesT = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, array_ingredientesT);
                this.getIngredientes().setAdapter(adapter_ingredientesT); //Set de ingredientes
                //Preparar el adapter de instrucciones
                String[] array_pasosT = new String[diet.getEvening().getPasos().size()];
                for(int i = 0; i<diet.getEvening().getPasos().size();i++){
                    array_pasosT[i] = diet.getEvening().getPasos().get(i);
                }
                ArrayAdapter<String> adapter_pasosT = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, array_pasosT);
                this.getInstrucciones().setAdapter(adapter_pasosT); //Set de instrucciones
                break;
            case "Noche":
                this.getNombre_receta().setText(diet.getNigth().getNombre()); //Set del nombre de la receta
                this.getCalorias().setText(Integer.toString(diet.getNigth().getCalorias()) + " calorias por porción"); //Set de las calorias
                //Preparar el adapter de ingredientes
                String[] array_ingredientesN = new String[diet.getNigth().getIngredientes().size()];
                for(int i = 0; i<diet.getNigth().getIngredientes().size();i++){
                    array_ingredientesN[i] = diet.getNigth().getIngredientes().get(i);
                }
                ArrayAdapter<String> adapter_ingredientesN = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, array_ingredientesN);
                this.getIngredientes().setAdapter(adapter_ingredientesN); //Set de ingredientes
                //Preparar el adapter de instrucciones
                String[] array_pasosN = new String[diet.getNigth().getPasos().size()];
                for(int i = 0; i<diet.getNigth().getPasos().size();i++){
                    array_pasosN[i] = diet.getNigth().getPasos().get(i);
                }
                ArrayAdapter<String> adapter_pasosN = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, array_pasosN);
                this.getInstrucciones().setAdapter(adapter_pasosN); //Set de instrucciones
                break;
        }

        dia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setData(getBaby(getBebe().getSelectedItem().toString()),Integer.parseInt(getDia().getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bebe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setData(getBaby(getBebe().getSelectedItem().toString()),Integer.parseInt(getDia().getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.getLoadingDialog().dismissDialog();
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

    public HashMap<Baby, ArrayList<Diet>> getDietPerBaby() {
        return dietPerBaby;
    }

    public void setDietPerBaby(HashMap<Baby, ArrayList<Diet>> dietPerBaby) {
        this.dietPerBaby = dietPerBaby;
    }

    public TextView getHora() {
        return hora;
    }

    public TextView getNombre_receta() {
        return nombre_receta;
    }

    public TextView getCalorias() {
        return calorias;
    }

    public ListView getInstrucciones() {
        return instrucciones;
    }

    public ListView getIngredientes() {
        return ingredientes;
    }

    public Spinner getDia() {
        return dia;
    }

    public Spinner getBebe() {
        return bebe;
    }

    public String getComida() {
        return comida;
    }

    public void setComida(String comida) {
        this.comida = comida;
    }

    public LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }

    public void setLoadingDialog(LoadingDialog loadingDialog) {
        this.loadingDialog = loadingDialog;
    }

    //Array para el spinner de dias
    private static final String[] DIAS_OPTIONS = new String[] {
            "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"
    };
    
}
