package com.juno.movie;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.*;

public class ProfileStore {
    private final SharedPreferences p;
    public ProfileStore(Context c){p=c.getSharedPreferences("juno_profiles",Context.MODE_PRIVATE);}
    public List<String> getProfiles(){
        String raw=p.getString("profiles","");
        List<String> out=new ArrayList<>();
        if(!raw.isEmpty()) for(String s:raw.split("\\|")) if(!s.trim().isEmpty()) out.add(s);
        return out;
    }
    public void add(String name){
        name=name.trim().replace("|","");
        if(name.isEmpty())return;
        List<String> list=getProfiles();
        for(String x:list) if(x.equalsIgnoreCase(name))return;
        list.add(name); save(list);
    }
    private void save(List<String> list){p.edit().putString("profiles",String.join("|",list)).apply();}
    public String active(){return p.getString("active","");}
    public void setActive(String name){p.edit().putString("active",name).apply();}
}
