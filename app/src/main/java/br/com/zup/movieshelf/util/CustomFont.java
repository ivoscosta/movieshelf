package br.com.zup.movieshelf.util;

import android.graphics.Typeface;

import br.com.zup.movieshelf.controller.MovieShelfApplication;

/**
 * Created by ivo on 10/4/16.
 */
public class CustomFont {

    public static Typeface getTypeface(int textStyle) {
        String fontPath;
        switch (textStyle) {
            case Typeface.BOLD: // bold
                fontPath = "fonts/OpenSans-Bold.ttf";
                break;
            case Typeface.ITALIC: // italic
                fontPath = "fonts/OpenSans-Italic.ttf";
                break;
            case Typeface.BOLD_ITALIC: // bold italic
                fontPath = "fonts/OpenSans-BoldItalic.ttf";
                break;
            case Typeface.NORMAL: // regular
                fontPath = "fonts/OpenSans-Regular.ttf";
                break;
            case 4: // HelvenueThin
                fontPath = "fonts/HelveticaThin.ttf";
                break;
            default:
                fontPath = "fonts/HelveticaThin.ttf";
        }
        return Typeface.createFromAsset(MovieShelfApplication.getAppContext().getAssets(), fontPath);
    }
}