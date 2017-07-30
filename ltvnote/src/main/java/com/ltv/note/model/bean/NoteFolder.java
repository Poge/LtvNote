package com.ltv.note.model.bean;

import com.ltv.note.model.INote;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Anpo on 2017/7/25.
 */
public class NoteFolder extends DataSupport implements INote{

    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    private Date createTime;

    private Date editTime;

    @Column(defaultValue = "0")
    private int noteNum;

    @Column(defaultValue = "0")
    private List<Note> noteList =new ArrayList<>();


    public NoteFolder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public int getNoteNum() {
        return noteNum;
    }

    public void setNoteNum(int noteNum) {
        this.noteNum = noteNum;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public int getNoteType() {
        return NoteConstant.NOTE_FOLDER;
    }


    @Override
    public int compareTo(INote o) {
        int noteType = o.getNoteType();
        if(noteType==NoteConstant.NOTE){
            Note another= (Note) o;
            return 0-getCreateTime().compareTo(another.getEditTime());
        }else if(noteType==NoteConstant.NOTE_FOLDER){
            NoteFolder folder= (NoteFolder) o;
            return 0-getCreateTime().compareTo(folder.getCreateTime());
        }

        return 0;
    }
}
