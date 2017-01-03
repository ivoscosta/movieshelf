package br.com.zup.movieshelf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.zup.movieshelf.R;
import br.com.zup.movieshelf.model.Movie;
import br.com.zup.movieshelf.util.Util;

/**
 * Created by ivo on 30/12/16.
 */

public class MovieListAdapter extends BaseAdapter {

    private Context context;
    private List<Movie> arraylist;

    public MovieListAdapter(List<Movie> apps, Context context) {
        this.arraylist = apps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null) {
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movie, null);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.imgPoster = (ImageView) rowView.findViewById(R.id.poster);
            viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.title);
            viewHolder.txtYear = (TextView) rowView.findViewById(R.id.year);
            viewHolder.txtType = (TextView) rowView.findViewById(R.id.type);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Util.setPicture(viewHolder.imgPoster, arraylist.get(position).getPoster());
        viewHolder.txtTitle.setText(arraylist.get(position).getTitle());
        viewHolder.txtYear.setText(arraylist.get(position).getYear());
        viewHolder.txtType.setText(arraylist.get(position).getType());
        return rowView;
    }

    public class ViewHolder {
        TextView txtTitle, txtYear, txtType;
        ImageView imgPoster;
    }
}
