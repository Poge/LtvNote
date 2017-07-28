package com.ltv.note.model.bean;

import com.ltv.note.model.INote;

/**
 * Created by Anpo on 2017/7/26.
 */
public class NoteWrapper {

    private INote note;

    private boolean isSelect = false;

    public NoteWrapper(INote note, boolean isSelect) {
        this.isSelect = isSelect;
        this.note = note;
    }

    public INote getNote() {
        return note;
    }

    public void setNote(INote note) {
        this.note = note;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
