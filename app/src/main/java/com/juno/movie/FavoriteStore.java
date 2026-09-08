package com.juno.movie;
import android.content.Context;
import android.content.SharedPreferences;

public class FavoriteStore {
    private final SharedPreferences p;
    public FavoriteStore(Context c){p=c.getSharedPreferences("juno_favorites",Context.MODE_PRIVATE);}
    private String key(String profile,String path){return profile+"::"+path;}
    public boolean isFavorite(String profile,String path){return p.getBoolean(key(profile,path),false);}
    public void toggle(String profile,String path){String k=key(profile,path);p.edit().putBoolean(k,!isFavorite(profile,path)).apply();}
}
