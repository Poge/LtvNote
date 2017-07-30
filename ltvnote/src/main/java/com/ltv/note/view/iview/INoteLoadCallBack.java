package com.ltv.note.view.iview;

import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

import com.ltv.note.model.bean.Note;

/**
 * Created by Anpo on 2017/7/26.
 */
public interface INoteLoadCallBack {

    void initNoteView(Note note);

    void onLoadNoteData(Editable edit);

    Editable  getNoteText();

    void moveCursorToPos(int position);

    void moveCursorToNextLine();

    boolean isCursorFirstOfLine();

    void addNoteMediaInfo(SpannableStringBuilder ss);

    void changeBackGroundColor(int bgColor);

}
