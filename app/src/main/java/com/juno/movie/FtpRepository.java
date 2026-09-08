package com.juno.movie;

import org.apache.commons.net.ftp.*;
import java.util.*;

public class FtpRepository {
    private final FtpConfig c;
    public FtpRepository(FtpConfig c){this.c=c;}
    private FTPClient connect() throws Exception{
        FTPClient ftp=new FTPClient();
        ftp.setConnectTimeout(8000); ftp.setDataTimeout(15000);
        ftp.connect(c.ip(),c.port());
        if(!ftp.login(c.user(),c.password()))throw new Exception("FTP login failed");
        ftp.enterLocalPassiveMode(); ftp.setFileType(FTP.BINARY_FILE_TYPE);
        return ftp;
    }
    public boolean test(){
        FTPClient ftp=null;
        try{ftp=connect();return ftp.isConnected();}
        catch(Exception e){return false;}
        finally{if(ftp!=null){try{ftp.logout();}catch(Exception ignored){}try{ftp.disconnect();}catch(Exception ignored){}}}
    }
    public List<Movie> scanMovies() throws Exception{
        FTPClient ftp=connect();List<Movie> out=new ArrayList<>();
        try{
            FTPFile[] files=ftp.listFiles("/Movies");
            for(FTPFile f:files){
                if(!f.isFile())continue;
                String n=f.getName(),x=n.toLowerCase(Locale.ROOT);
                if(x.endsWith(".mp4")||x.endsWith(".mkv")||x.endsWith(".avi")||x.endsWith(".mov")||x.endsWith(".webm")||x.endsWith(".m4v"))
                    out.add(new Movie(n,"/Movies/"+n));
            }
            out.sort(Comparator.comparing(m->m.name.toLowerCase(Locale.ROOT)));
            return out;
        }finally{try{ftp.logout();}catch(Exception ignored){}try{ftp.disconnect();}catch(Exception ignored){}}
    }
}
