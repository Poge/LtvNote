package com.ltv.note.view.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ltv.note.R;

/**
 * Created by Anpo on 2017/7/28.
 */
public class NoteSettingFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_note);
    }
}
