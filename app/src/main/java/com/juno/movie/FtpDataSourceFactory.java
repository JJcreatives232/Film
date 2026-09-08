package com.juno.movie;
import androidx.media3.datasource.DataSource;

public class FtpDataSourceFactory implements DataSource.Factory {
    private final FtpConfig config;
    public FtpDataSourceFactory(FtpConfig config){this.config=config;}
    @Override public DataSource createDataSource(){return new FtpDataSource(config);}
}
