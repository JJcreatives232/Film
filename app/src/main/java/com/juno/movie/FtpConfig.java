package com.juno.movie;
import android.content.Context;
import android.content.SharedPreferences;

public class FtpConfig {
    private final SharedPreferences p;
    public FtpConfig(Context c){p=c.getSharedPreferences("juno_ftp",Context.MODE_PRIVATE);}
    public String ip(){return "192.168.100.1";}
    public int port(){return 21;}
    public String user(){return "root";}
    public String password(){return p.getString("password","");}
    public boolean configured(){return !password().isEmpty();}
    public void savePassword(String s){p.edit().putString("password",s).apply();}
    public void clear(){p.edit().clear().apply();}
}
