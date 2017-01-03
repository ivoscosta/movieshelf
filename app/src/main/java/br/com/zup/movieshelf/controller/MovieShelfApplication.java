package br.com.zup.movieshelf.controller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ivo on 10/4/16.
 */
public class MovieShelfApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MovieShelfApplication.context = getApplicationContext();
    }

    //pega o contexto da aplicação
    public static Context getAppContext() {
        return MovieShelfApplication.context;
    }

    //mata a aplicação
    public static void closeApplication() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        return;
    }
}
