package com.ltv.note.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ltv.note.R;
import com.ltv.note.model.INote;
import com.ltv.note.model.NoteLoader;
import com.ltv.note.view.NoteActivity;
import com.ltv.note.view.NoteListActivity;
import com.ltv.note.view.NoteSettingActivity;
import com.ltv.note.view.adapter.NoteItemDecoration;
import com.ltv.note.view.adapter.NoteListAdapter;

import java.util.List;

public class NoteListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<INote>>,
        NoteListActivity.OnActivityInteractionListener, NoteListAdapter.OnCheckStateChangeListener {

    private RecyclerView mRecyclerView;
    private FloatingActionButton fabAdd;
    private NoteListAdapter noteListAdapter;
    private int stateOfNoteChecked = 0;

    public NoteListFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.note_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new NoteItemDecoration());
        mRecyclerView.setHasFixedSize(true);
        noteListAdapter = new NoteListAdapter();
        noteListAdapter.setOnCheckStateChangeListener(this);
        mRecyclerView.setAdapter(noteListAdapter);
        fabAdd = (FloatingActionButton) view.findViewById(R.id.add_fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteActivity.start(v.getContext(),0);
            }
        });

        getLoaderManager().initLoader(0, null, this);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("poge","onCreateOptionsMenu: ");
        inflater.inflate(R.menu.menu_note_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.e("poge","onPrepareOptionsMenu: ");
        MenuItem selectNone = menu.findItem(R.id.menu_none_select);
        MenuItem selectAll = menu.findItem(R.id.menu_all_select);
        switch (stateOfNoteChecked) {
            case NoteListAdapter.CHECKED_START:
            case NoteListAdapter.CHECKED_NONE:
                selectNone.setVisible(false);
                selectAll.setVisible(true);
                break;
            case NoteListAdapter.CHECKED_ALL:
                selectNone.setVisible(true);
                selectAll.setVisible(false);
                break;
            case NoteListAdapter.CHECKED_END:
                selectNone.setVisible(false);
                selectAll.setVisible(false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("poge","onOptionsItemSelected: ");
        switch (item.getItemId()){
            case R.id.menu_setting:
               startActivity(new Intent(getContext(),NoteSettingActivity.class)) ;
                break;
            case R.id.menu_all_select:
                noteListAdapter.checkAllNote();
                break;
            case R.id.menu_none_select:
                noteListAdapter.cancleCheckNote();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<INote>> onCreateLoader(int id, Bundle args) {

        return new NoteLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<INote>> loader, List<INote> data) {
        noteListAdapter.setNoteDatas(data);
    }

    @Override
    public void onLoaderReset(Loader<List<INote>> loader) {
        noteListAdapter.setNoteDatas(null);
    }


    @Override
    public boolean onBackPressed() {

        return noteListAdapter.onBackPressed();
    }

    @Override
    public void onNoteCheckStateChanged(int state) {
        stateOfNoteChecked = state;
        Log.e("poge","onNoteCheckStateChanged: "+state);
        getActivity().invalidateOptionsMenu();
    }
}
