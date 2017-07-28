package com.ltv.note.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ltv.note.R;

public class NoteListActivity extends AppCompatActivity {


    private OnActivityInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setPopupTheme(R.style.PopupMenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_list);
        if(fragment!=null&&fragment instanceof OnActivityInteractionListener)
            mListener= (OnActivityInteractionListener) fragment;
    }

    @Override
    public void onBackPressed() {

        if (mListener!=null&&mListener.onBackPressed())
            return;

        super.onBackPressed();
    }


    public interface OnActivityInteractionListener{

        boolean onBackPressed();

    }

}
