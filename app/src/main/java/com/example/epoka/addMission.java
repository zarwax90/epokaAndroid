package com.example.epoka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class addMission extends AppCompatActivity {
    private String urlServiceWebVille;
    private String urlServiceWebAddMission;
    String ch = "";
    String ip;
    int idUser;
    public List<cpVille> list = new ArrayList<cpVille>();
    Spinner ville;
    String cpNom;


    EditText debut;
    EditText fin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mission);
        Bundle extras=getIntent().getExtras();
        if(extras!=null) {
            idUser = extras.getInt("id");
            ip = extras.getString("ip");
        }

        urlServiceWebVille = "http://" + ip + "/epoka/services/srvVilles";
        getVilleDataJson(urlServiceWebVille);

    }

    public class cpVille {
        public int id;
        public int cp;
        public String nom;

        public cpVille(int unId, String unNom, int unCp) {
            id = unId;
            nom = unNom;
            cp = unCp;
        }

        @Override
        public String toString() {
            cpNom = nom + " (" + cp +")";
            return cpNom;
        }
    }

    private String getVilleDataJson(String urlString) {

        try {
            //Echange HTTP avec le serveur
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = new URL(urlString);
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            InputStream is = connexion.getInputStream();
            ch = new Scanner(is,"UTF-8").useDelimiter("\\A").next(); // Convert the InputStream into a string
            connexion.disconnect();


        } catch (Exception expt) {
            Log.e("log_tag", "Erreur pendant la récupération des données : " + expt.toString());
        };

        //Parse les données JSON
        try {
            JSONArray jArray = new JSONArray(ch);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonData = jArray.getJSONObject(i);
                int id= jsonData.getInt("id");
                String nom = jsonData.getString("name");
                int cp= jsonData.getInt("cp");
                list.add(new cpVille ( id, nom,cp));
            }
            ArrayAdapter<cpVille> adapter = new ArrayAdapter<cpVille>(this, android.R.layout.simple_spinner_item,list);
            ville = (Spinner) findViewById(R.id.spinnerVille);
            ville.setAdapter(adapter);
        } catch (JSONException e) {
            Log.e("log_tag", "Erreur pendant l'analyse des données : " + e.toString());
        }
        return ch;
    }

    public void addMission(View view){
        debut = (EditText) findViewById (R.id.etDebut);
        fin = (EditText) findViewById (R.id.etFin);
        cpVille cpVille = (cpVille) ville.getSelectedItem();
        int idVille = cpVille.id;
        urlServiceWebAddMission ="http://" + ip + "/epoka/services/srvAddMission.php?idUser="+ idUser +"&idVille="+ idVille +"&debut="+ debut.getText() +"&fin=" + fin.getText();
        addMissionBdd(urlServiceWebAddMission);
    }

    private String addMissionBdd(String urlString) {

        try {
            //Echange HTTP avec le serveur
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = new URL(urlString);
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            InputStream is = connexion.getInputStream();
            ch = "";
            ch = new Scanner(is, "UTF-8").useDelimiter("\\A").next(); // Convert the InputStream into a string
            connexion.disconnect();

            Context context = getApplicationContext();
            CharSequence text = ch;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } catch (Exception expt) {
            Log.e("log_tag", "Erreur pendant la récupération des données : " + expt.toString());
        };
        return ch;
    }

}