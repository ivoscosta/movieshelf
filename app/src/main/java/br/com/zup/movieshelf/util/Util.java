package br.com.zup.movieshelf.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import br.com.zup.movieshelf.R;
import br.com.zup.movieshelf.controller.MovieShelfApplication;

/**
 * Created by ivo on 28/12/16.
 */

public class Util {

    public static Boolean isConnectInternet(Activity a) {
        ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void setPicture(ImageView imageView, String image) {
        Glide.with(MovieShelfApplication.getAppContext())
                .load(image)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView);
    }

    public static void crossfade(final View out, View in) {
        int mShortAnimationDuration = 400; //duration of animation
        if (in != null) {
            in.setAlpha(0f);
            in.setVisibility(View.VISIBLE);
            in.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
        }

        if (out != null) {
            out.animate()
                    .alpha(0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            out.setVisibility(View.GONE);
                            out.setAlpha(1f);
                        }
                    });
        }
    }
}
