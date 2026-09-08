package com.juno.movie;

import android.content.Intent;
import android.os.Bundle;
import android.text.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import java.util.*;

public class MainActivity extends AppCompatActivity{
    private final List<Movie> all=new ArrayList<>();
    private MovieAdapter adapter;private TextView status,welcome;private EditText search;private FtpConfig config;private String profile;
    private FavoriteStore fav;
    protected void onCreate(Bundle b){
        super.onCreate(b);setContentView(R.layout.activity_main);
        config=new FtpConfig(this);fav=new FavoriteStore(this);
        if(!config.configured()){startActivity(new Intent(this,SetupActivity.class));finish();return;}
        ProfileStore ps=new ProfileStore(this);profile=ps.active();
        if(profile.isEmpty()){startActivity(new Intent(this,ProfileActivity.class));finish();return;}
        status=findViewById(R.id.statusText);welcome=findViewById(R.id.welcomeText);search=findViewById(R.id.searchBox);
        welcome.setText("Watching as "+profile);
        RecyclerView list=findViewById(R.id.movieList);list.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MovieAdapter(new ArrayList<>(),profile,new MovieAdapter.Listener(){
            public void play(Movie m){Intent i=new Intent(MainActivity.this,PlayerActivity.class);i.putExtra("moviePath",m.path);startActivity(i);}
            public void favorite(Movie m){fav.toggle(profile,m.path);adapter.notifyDataSetChanged();}
        },fav);list.setAdapter(adapter);
        findViewById(R.id.profileButton).setOnClickListener(v->startActivity(new Intent(this,ProfileActivity.class)));
        findViewById(R.id.settingsButton).setOnClickListener(v->startActivity(new Intent(this,SettingsActivity.class)));
        search.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int a,int c,int d){}public void onTextChanged(CharSequence s,int a,int b,int c){filter(s.toString());}public void afterTextChanged(Editable e){}});
    }
    protected void onResume(){super.onResume();if(config!=null&&config.configured()&&adapter!=null)load();}
    private void load(){
        status.setText("Scanning /Movies/...");
        new Thread(()->{try{List<Movie> r=new FtpRepository(config).scanMovies();runOnUiThread(()->{all.clear();all.addAll(r);adapter.setMovies(r);status.setText(r.size()+" movie(s) • streamed from router");});}
        catch(Exception e){runOnUiThread(()->status.setText("Could not load /Movies/: "+e.getMessage()));}}).start();
    }
    private void filter(String q){q=q.toLowerCase(Locale.ROOT).trim();List<Movie> r=new ArrayList<>();for(Movie m:all)if(m.name.toLowerCase(Locale.ROOT).contains(q))r.add(m);adapter.setMovies(r);}
}
