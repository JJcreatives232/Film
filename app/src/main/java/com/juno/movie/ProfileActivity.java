package com.juno.movie;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import java.util.*;

public class ProfileActivity extends AppCompatActivity{
    private ProfileStore store;private ProfileAdapter adapter;
    protected void onCreate(Bundle b){
        super.onCreate(b);setContentView(R.layout.activity_profiles);store=new ProfileStore(this);
        RecyclerView list=findViewById(R.id.profileList);list.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ProfileAdapter(store.getProfiles(),n->{store.setActive(n);startActivity(new Intent(this,MainActivity.class));finish();});
        list.setAdapter(adapter);
        findViewById(R.id.addProfileButton).setOnClickListener(v->addProfile());
    }
    private void addProfile(){
        EditText input=new EditText(this);input.setHint("Name e.g. JJ");
        new AlertDialog.Builder(this).setTitle("Create profile").setView(input)
            .setPositiveButton("CREATE",(d,w)->{String n=input.getText().toString().trim();if(!n.isEmpty()){store.add(n);adapter.set(store.getProfiles());}})
            .setNegativeButton("CANCEL",null).show();
    }
    static class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.H>{
        interface L{void click(String n);}List<String> data;L l;
        ProfileAdapter(List<String>d,L l){data=d;this.l=l;}void set(List<String>d){data=d;notifyDataSetChanged();}
        @NonNull public H onCreateViewHolder(@NonNull ViewGroup p,int t){return new H(LayoutInflater.from(p.getContext()).inflate(R.layout.item_profile,p,false));}
        public void onBindViewHolder(@NonNull H h,int i){String n=data.get(i);h.name.setText(n);h.avatar.setText(n.substring(0,1).toUpperCase());h.itemView.setOnClickListener(v->l.click(n));}
        public int getItemCount(){return data.size();}
        static class H extends RecyclerView.ViewHolder{TextView name,avatar;H(View v){super(v);name=v.findViewById(R.id.profileName);avatar=v.findViewById(R.id.avatar);}}
    }
}
