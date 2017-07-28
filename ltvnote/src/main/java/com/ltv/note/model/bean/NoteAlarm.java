package com.ltv.note.model.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by Anpo on 2017/7/25.
 */
public class NoteAlarm extends DataSupport {

    private int id;

    @Column(defaultValue = "true")
    private boolean enable;

    @Column(defaultValue = "false")
    private boolean hasAlarm;

    @Column(defaultValue = "0")
    private int alarmStyle;

    private Date alarmTime;

    public NoteAlarm() {
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getAlarmStyle() {
        return alarmStyle;
    }

    public void setAlarmStyle(int alarmStyle) {
        this.alarmStyle = alarmStyle;
    }

    public boolean isHasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
