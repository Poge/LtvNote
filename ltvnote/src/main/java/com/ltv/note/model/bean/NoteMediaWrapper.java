package com.ltv.note.model.bean;

import android.graphics.Bitmap;

/**
 * Created by Anpo on 2017/7/27.
 */
public class NoteMediaWrapper implements Comparable<NoteMediaWrapper> {

    private NoteMediaInfo meidaInfo;

    private int startPos=-1;

    private Bitmap  bitmap;

    public NoteMediaWrapper() {

    }

    public NoteMediaWrapper(int startPos, NoteMediaInfo meidaInfo) {
        this.startPos = startPos;
        this.meidaInfo = meidaInfo;
    }


    public NoteMediaWrapper(int startPos, Bitmap bitmap, NoteMediaInfo meidaInfo) {
        this.startPos = startPos;
        this.bitmap = bitmap;
        this.meidaInfo = meidaInfo;
    }


    public NoteMediaInfo getMeidaInfo() {
        return meidaInfo;
    }

    public void setMeidaInfo(NoteMediaInfo meidaInfo) {
        this.meidaInfo = meidaInfo;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int compareTo(NoteMediaWrapper o) {
        return startPos-o.getStartPos();
    }
}
