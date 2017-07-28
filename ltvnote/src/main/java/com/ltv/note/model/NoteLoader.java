package com.ltv.note.model;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.ltv.note.model.bean.Note;
import com.ltv.note.model.bean.NoteFolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anpo on 2017/7/25.
 */
public class NoteLoader extends AsyncTaskLoader<List<INote>> {

    public NoteLoader(Context context) {
        super(context);
    }

    @Override
    public List<INote> loadInBackground() {
        List<Note> notes = DataSupport.where("notefolder_id is null").find(Note.class);
        List<NoteFolder> folders = DataSupport.findAll(NoteFolder.class);
        List<INote> datas=new ArrayList<>();
        datas.addAll(notes);
        datas.addAll(folders);
        Collections.sort(datas);
        return datas;
    }

    @Override
    protected void onStartLoading() {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(Boolean event) {
        if(event){
            onContentChanged();
        }
    }
}
