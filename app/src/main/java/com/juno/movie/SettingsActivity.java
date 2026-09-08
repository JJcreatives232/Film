package com.juno.movie;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity{
    protected void onCreate(Bundle b){
        super.onCreate(b);setContentView(R.layout.activity_settings);
        findViewById(R.id.reconfigureButton).setOnClickListener(v->startActivity(new Intent(this,SetupActivity.class)));
        findViewById(R.id.profilesButton).setOnClickListener(v->startActivity(new Intent(this,ProfileActivity.class)));
    }
}
