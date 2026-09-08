package com.juno.movie;

import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.common.DataSpec;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.datasource.DataSourceException;
import java.io.*;
import org.apache.commons.net.ftp.*;

public class FtpDataSource implements DataSource {
    private final FtpConfig config;
    private FTPClient ftp;
    private InputStream input;
    private long remaining=C.LENGTH_UNSET;
    private Uri uri;

    public FtpDataSource(FtpConfig config){this.config=config;}

    @Override public void addTransferListener(TransferListener listener) { }

    @Override public long open(DataSpec spec) throws IOException{
        uri=spec.uri;
        try{
            ftp=new FTPClient();
            ftp.setConnectTimeout(10000); ftp.setDataTimeout(20000);
            ftp.connect(config.ip(),config.port());
            if(!ftp.login(config.user(),config.password()))throw new IOException("FTP login failed");
            ftp.enterLocalPassiveMode(); ftp.setFileType(FTP.BINARY_FILE_TYPE);

            String path=uri.getPath();
            FTPFile[] info=ftp.listFiles(path);
            long size=(info.length>0 && info[0].isFile())?info[0].getSize():C.LENGTH_UNSET;

            if(spec.position>0)ftp.setRestartOffset(spec.position);
            input=ftp.retrieveFileStream(path);
            if(input==null)throw new IOException("FTP stream failed: "+ftp.getReplyString());

            if(size!=C.LENGTH_UNSET)remaining=Math.max(0,size-spec.position);
            if(spec.length!=C.LENGTH_UNSET)remaining=Math.min(remaining,spec.length);
            return remaining;
        }catch(Exception e){
            try{close();}catch(Exception ignored){}
            throw new DataSourceException("Unable to stream "+uri+": "+e.getMessage());
        }
    }

    @Override public int read(byte[] b,int off,int len)throws IOException{
        if(len==0)return 0;
        if(remaining==0)return -1;
        int want=remaining==C.LENGTH_UNSET?len:(int)Math.min(len,remaining);
        int r=input.read(b,off,want);
        if(r<0){remaining=0;return -1;}
        if(remaining!=C.LENGTH_UNSET)remaining-=r;
        return r;
    }
    @Override public Uri getUri(){return uri;}
    @Override public void close() throws IOException{
        try{if(input!=null)input.close();}finally{
            input=null;
            if(ftp!=null){
                try{ftp.completePendingCommand();}catch(Exception ignored){}
                try{ftp.logout();}catch(Exception ignored){}
                try{ftp.disconnect();}catch(Exception ignored){}
                ftp=null;
            }
        }
    }
}
