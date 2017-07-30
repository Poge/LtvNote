package com.ltv.note.model.bean;

import com.ltv.note.model.INote;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;
import org.litepal.crud.async.FindExecutor;
import org.litepal.crud.async.FindMultiExecutor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Anpo on 2017/7/25.
 */
public class Note extends DataSupport implements INote{



    private long id;

    @Column(nullable = false)
    private Date createTime;

    @Column(nullable = false)
    private Date editTime;

    private String contentAll;

    private String contentText;

    private List<NoteMediaInfo> noteMediaInfoList =new ArrayList<NoteMediaInfo>();

    private String thumbnail;

    @Column(defaultValue = "false")
    private boolean hasAlarm;

    @Column(defaultValue = "1")
    private int bgColor;

    private NoteAlarm noteAlarm;


    public Note() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContentAll() {
        return contentAll;
    }

    public void setContentAll(String contentAll) {
        this.contentAll = contentAll;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public List<NoteMediaInfo> getNoteMediaInfoList() {

        return noteMediaInfoList;
    }
    public FindMultiExecutor getMediaInfosAsyn() {
        FindMultiExecutor async = DataSupport.where("note_id = ?", String.valueOf(getId())).findAsync(NoteMediaInfo.class);
        return async;
    }
    public List<NoteMediaInfo> getMediaInfoSync() {
        noteMediaInfoList =DataSupport.where("note_id = ?",String.valueOf(getId())).find(NoteMediaInfo.class);
        return noteMediaInfoList;
    }

    public void setNoteMediaInfoList(List<NoteMediaInfo> noteMediaInfoList) {
        if(this.noteMediaInfoList.size()>0)
            this.noteMediaInfoList.clear();
        this.noteMediaInfoList.addAll(noteMediaInfoList);
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public boolean isHasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public NoteAlarm getAlarm() {
        return noteAlarm;
    }
    public FindExecutor getAlarmAsyn() {
        FindExecutor async = DataSupport.where("note_id = ?", String.valueOf(getId())).findFirstAsync(NoteAlarm.class);
        return async;
    }
    public NoteAlarm getAlarmSync() {
        noteAlarm =DataSupport.where("note_id = ?", String.valueOf(getId())).findFirst(NoteAlarm.class);
        return noteAlarm;
    }

    public void setAlarm(NoteAlarm alarm) {
        this.noteAlarm = alarm;
    }

    @Override
    public int getNoteType() {
        return NoteConstant.NOTE;
    }


    public void addNoteMediaInfo(NoteMediaInfo info){
        if(!noteMediaInfoList.contains(info))
            noteMediaInfoList.add(info);

    }

    @Override
    public int compareTo(INote o) {
        int noteType = o.getNoteType();
        if(noteType==NoteConstant.NOTE){
            Note another= (Note) o;
           return 0-getEditTime().compareTo(another.getEditTime());
        }else if(noteType==NoteConstant.NOTE_FOLDER){
            NoteFolder folder= (NoteFolder) o;
            return 0-getEditTime().compareTo(folder.getEditTime());
        }

        return 0;
    }
}
