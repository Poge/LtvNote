package com.ltv.note.model.bean;

import org.litepal.annotation.Column;

/**
 * Created by Anpo on 2017/7/25.
 */
public class NoteMediaInfo {

    private int id;

    @Column(defaultValue = "0")
    private int mediaType;

    private String mediaUrl;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NoteMediaInfo){
            NoteMediaInfo info= (NoteMediaInfo) obj;
            String url = info.getMediaUrl();
            return url.equals(mediaUrl);
        }

        return super.equals(obj);
    }
}
