package com.juno.movie;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Holder>{
    public interface Listener{void play(Movie m);void favorite(Movie m);}
    private List<Movie> movies;private final Listener listener;private final String profile;private final FavoriteStore fav;
    public MovieAdapter(List<Movie> m,String p,Listener l,FavoriteStore f){movies=m;profile=p;listener=l;fav=f;}
    public void setMovies(List<Movie> m){movies=m;notifyDataSetChanged();}
    @NonNull public Holder onCreateViewHolder(@NonNull ViewGroup p,int t){
        return new Holder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_movie,p,false));
    }
    public void onBindViewHolder(@NonNull Holder h,int i){
        Movie m=movies.get(i);h.title.setText(m.name);h.path.setText(m.path);
        h.favorite.setText(fav.isFavorite(profile,m.path)?"♥":"♡");
        h.itemView.setOnClickListener(v->listener.play(m));
        h.favorite.setOnClickListener(v->listener.favorite(m));
    }
    public int getItemCount(){return movies.size();}
    static class Holder extends RecyclerView.ViewHolder{
        TextView title,path,favorite;
        Holder(View v){super(v);title=v.findViewById(R.id.movieTitle);path=v.findViewById(R.id.moviePath);favorite=v.findViewById(R.id.favoriteIcon);}
    }
}
