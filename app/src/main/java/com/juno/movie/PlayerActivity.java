package com.juno.movie;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.datasource.DataSource;

public class PlayerActivity extends AppCompatActivity{
    private ExoPlayer player;private String path,profile;private WatchStore watch;
    protected void onCreate(Bundle b){
        super.onCreate(b);setContentView(R.layout.activity_player);
        path=getIntent().getStringExtra("moviePath");profile=new ProfileStore(this).active();
        watch=new WatchStore(this);FtpConfig config=new FtpConfig(this);
        player=new ExoPlayer.Builder(this).build();
        androidx.media3.ui.PlayerView view=findViewById(R.id.playerView);view.setPlayer(player);
        DataSource.Factory factory=new FtpDataSourceFactory(config);
        ProgressiveMediaSource source=new ProgressiveMediaSource.Factory(factory)
            .createMediaSource(MediaItem.fromUri("ftp://"+config.ip()+path));
        player.setMediaSource(source);player.prepare();
        long saved=watch.position(profile,path);if(saved>0)player.seekTo(saved);player.play();
    }
    protected void onStop(){
        super.onStop();
        if(player!=null){watch.save(profile,path,player.getCurrentPosition(),player.getDuration());player.release();player=null;}
    }
}
