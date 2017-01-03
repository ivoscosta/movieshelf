package br.com.zup.movieshelf.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.zup.movieshelf.R;
import br.com.zup.movieshelf.model.Movie;
import br.com.zup.movieshelf.util.CustomFont;
import br.com.zup.movieshelf.util.Util;

/**
 * Created by ivo on 28/12/16.
 */

public class MovieGridAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Movie> movies;
    
    public MovieGridAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.movies = movies;
    }
    
    @Override
    public int getCount() {
        return movies.size();
    }
    
    @Override
    public long getItemId(int position) {
        return 0;
    }
    
    @Override
    public Object getItem(int position) {
        return null;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movie = movies.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_grid_movie, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.poster);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.title);

        Util.setPicture(imageView, movie.getPoster());
        nameTextView.setText(movie.getTitle());
        nameTextView.setTypeface(CustomFont.getTypeface(Typeface.NORMAL));

        return convertView;
    }

}
