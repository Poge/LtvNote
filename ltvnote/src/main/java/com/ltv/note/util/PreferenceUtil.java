package com.ltv.note.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ltv.note.R;

/**
 * Created by Anpo on 2017/7/28.
 */
public class PreferenceUtil {

    public static int getNoteFontSize(Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int defaultSize = preferences.getInt("fontsize", 3);

        return defaultSize == 1 ? 24 : defaultSize == 2 ? 20
                : defaultSize == 3 ? 16 : 14;
    }


     public static int getNoteBackgroundColorPref(Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int defaultColor = preferences.getInt("notecolor", 1);
        return defaultColor;
    }


    public static int getNoteBackgroundColor(Context context,int pref) {
        if(pref>4||pref<0)
            pref=getNoteBackgroundColorPref(context);
        return context.getResources().getColor(pref == 1 ? R.color.note_bg_blue :
                pref == 2 ? R.color.note_bg_green : pref == 3 ? R.color.note_bg_yellow : R.color.note_bg_pink);
    }

}
