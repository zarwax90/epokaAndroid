package com.example.epoka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Menu extends AppCompatActivity {
    String nom;
    String prenom;
    String ip;
    Integer id;
    TextView user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        user = (TextView) findViewById (R.id.textViewUser);
        Bundle extras=getIntent().getExtras();
        if(extras!=null) {
            id = extras.getInt("id");
            nom = extras.getString("nom");
            prenom = extras.getString("prenom");
            user.setText("Bonjour " + prenom + " " + nom + "(" + id + ")");
            ip = extras.getString("ip");
        }
    }
    public void addMission(View view){
        Intent intentAddMission = new Intent(this, addMission.class);
        intentAddMission.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK);
        intentAddMission.putExtra("id", id);
        intentAddMission.putExtra("ip", ip);
        startActivity(intentAddMission);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }
    public void leave(View view){
        System.exit(0);

    }
}