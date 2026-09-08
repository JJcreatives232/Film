package com.juno.movie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity{
    private EditText password;private TextView result;private FtpConfig config;
    protected void onCreate(Bundle b){
        super.onCreate(b);setContentView(R.layout.activity_setup);
        config=new FtpConfig(this);password=findViewById(R.id.passwordBox);result=findViewById(R.id.resultText);
        findViewById(R.id.testButton).setOnClickListener(v->connect(false));
        findViewById(R.id.saveButton).setOnClickListener(v->connect(true));
    }
    private void connect(boolean save){
        String pass=password.getText().toString();
        if(pass.isEmpty()){result.setText("Enter the router FTP password.");return;}
        if(save)config.savePassword(pass);
        result.setText("Connecting...");
        new Thread(()->{
            boolean ok=new FtpRepository(config).test();
            runOnUiThread(()->{
                if(ok){
                    result.setText("Connected. Password saved.");
                    if(save){
                        ProfileStore ps=new ProfileStore(this);
                        if(ps.getProfiles().isEmpty())startActivity(new Intent(this,ProfileActivity.class));
                        else startActivity(new Intent(this,MainActivity.class));
                        finish();
                    }
                }else{
                    if(save)config.clear();
                    result.setText("Connection failed. Check router FTP and password.");
                }
            });
        }).start();
    }
}
