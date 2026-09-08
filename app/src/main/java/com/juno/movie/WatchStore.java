package com.juno.movie;
import android.content.Context;
import android.content.SharedPreferences;

public class WatchStore {
    private final SharedPreferences p;
    public WatchStore(Context c){p=c.getSharedPreferences("juno_watch",Context.MODE_PRIVATE);}
    private String key(String profile,String path){return profile+"::"+path;}
    public long position(String profile,String path){return p.getLong(key(profile,path),0);}
    public void save(String profile,String path,long pos,long duration){
        if(duration>0 && pos>duration*0.92)p.edit().remove(key(profile,path)).apply();
        else p.edit().putLong(key(profile,path),pos).apply();
    }
}
