package com.example.epoka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText id;
    EditText mdp;
    BufferedReader br;
    HttpURLConnection connexion;
    InputStream is = null;
    JSONObject jsonData;
    JSONArray jArray;
    private String urlServiceWebUser;
    String ch ="";
    String ligne;
    TextView Rep;
    URL url;
    String ip = "192.168.1.35";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void connexion(View view){

        id = (EditText) findViewById (R.id.etId);
        mdp = (EditText) findViewById (R.id.etMdp);
        Rep = (TextView) findViewById (R.id.etRep);

        urlServiceWebUser = "http://" + ip + "/epoka/services/srvConnexion.php?id=" + id.getText() +"&mdp=" + mdp.getText();
        Rep.setText(getUserDataJson(urlServiceWebUser));
    }

    private String getUserDataJson(String urlString){

        try {
            //Echange HTTP avec le serveur
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            url = new URL(urlString);
            connexion = (HttpURLConnection) url.openConnection();
            connexion.connect();
            is = connexion.getInputStream();

            // Exploitation réponse texte brut
            br = new BufferedReader(new InputStreamReader(is));
            ch="";
            while((ligne = br.readLine()) != null){
                ch= ch + ligne + "\n";
            }
        }catch(Exception expt){
            Log.e("log_tag","Erreur pendant la récupération des données : " + expt.toString());
        };

        //Parse les données JSON
        try{
            jArray = new JSONArray(ch);
            ch="";
            for(int i = 0; i < jArray.length(); i++){
                jsonData = jArray.getJSONObject(i);
            }
            Intent intentMission = new Intent(this, Menu.class);
            intentMission.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK);
            intentMission.putExtra("id", jsonData.getInt("id"));
            intentMission.putExtra("nom", jsonData.getString("surname"));
            intentMission.putExtra("prenom", jsonData.getString("name"));
            intentMission.putExtra("agence", jsonData.getInt("idAgency"));
            intentMission.putExtra("ip", ip);
            startActivity(intentMission);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }catch (JSONException e){
            Log.e("log_tag","Erreur pendant l'analyse des données : " + e.toString());
        }
        return ch;
    }
}