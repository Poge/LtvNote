package com.ltv.note.presenter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ltv.note.R;
import com.ltv.note.model.NoteImageLoader;
import com.ltv.note.model.bean.Note;
import com.ltv.note.model.bean.NoteConstant;
import com.ltv.note.model.bean.NoteMediaInfo;
import com.ltv.note.model.bean.NoteMediaWrapper;
import com.ltv.note.util.NotePicUtil;
import com.ltv.note.util.PreferenceUtil;
import com.ltv.note.view.iview.INoteLoadCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Anpo on 2017/7/26.
 */
public class NotePresenter {

    private int noteWidth;
    private long noteId;
    private Note note;
    private Context context;
    private List<Subscription> subscriptions = new ArrayList<>();
    private INoteLoadCallBack callBack;


    public NotePresenter(Context context) {
        this.context = context;
    }

    public NotePresenter(Context context, long noteId) {
        this.noteId = noteId;
        this.context = context;

    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public int getNoteWidth() {
        return noteWidth;
    }

    public void setNoteWidth(int noteWidth) {
        this.noteWidth = noteWidth;
    }

    public void setCallBack(INoteLoadCallBack callBack) {
        this.callBack = callBack;
    }

    public void loadData() {
        note=null;
        loadData(noteId, callBack);
    }

    public void loadData(long noteId, final INoteLoadCallBack callBack) {
        Log.e("poge", "loadData: "+noteId);
        if (noteId == 0) {
            Date date = new Date();
            note = new Note();
            note.setCreateTime(date);
            note.setEditTime(date);
            note.setBgColor(PreferenceUtil.getNoteBackgroundColorPref(context));
        } else {
            note = DataSupport.find(Note.class, noteId);
        }

        if (callBack != null)
            callBack.initNoteView(note);

        if (noteId != 0 && note.getContentAll() != null && !note.getContentAll().trim().isEmpty()) {

            note.getMediaInfosAsyn().listen(new FindMultiCallback() {
                @Override
                public <T> void onFinish(List<T> t) {
                    ArrayList<NoteMediaInfo> mediaInfos = (ArrayList<NoteMediaInfo>) t;
                    note.setNoteMediaInfoList(mediaInfos);
                    Log.e("poge", "getMediaInfosAsyn: "+mediaInfos.size());
                    loadNoteDate(note, callBack);
                }
            });

        }

    }


    private void loadNoteDate(final Note note, final INoteLoadCallBack callBack) {
        final Editable editable = new SpannableStringBuilder(note.getContentAll());
        if (note.getNoteMediaInfoList().isEmpty()) {
            callBack.onLoadNoteData(editable);
            return;
        }

        String loadingPicUrl = ImageDownloader.Scheme.DRAWABLE.wrap(R.drawable.default_load_pic + "");
        ImageLoader.getInstance().loadImage(loadingPicUrl, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                preLoadMediaInfo(note.getNoteMediaInfoList(),editable,loadedImage,callBack);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                callBack.onLoadNoteData(editable);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                callBack.onLoadNoteData(editable);
            }
        });

    }


    private void preLoadMediaInfo(final List<NoteMediaInfo> mediaInfos, final Editable editable, final Bitmap preLoadBitmap,
                                  final INoteLoadCallBack callBack) {
        Subscription subscribe = Observable.create(new Observable.OnSubscribe<Editable>() {
            @Override
            public void call(Subscriber<? super Editable> subscriber) {
                Bitmap loadingBitmap = NotePicUtil.getAdjustBitmap(noteWidth, preLoadBitmap);
                String textContent = editable.toString();
                //用预加载图片信息替代便签中的图片信息
                for (NoteMediaInfo mediaInfo : mediaInfos) {
                    String mediaUrl = mediaInfo.getMediaUrl();
                    int start = textContent.indexOf(mediaUrl);
                    int end = textContent.lastIndexOf(mediaUrl);
                    if (start == -1)
                        continue;
                    SpannableStringBuilder ss = new SpannableStringBuilder(mediaUrl);
                    ImageSpan imageSpan = new ImageSpan(context.getApplicationContext(), loadingBitmap);
                    ss.setSpan(imageSpan, 0, mediaUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editable.replace(start, start + mediaUrl.length(), ss);
                    //一张图片可能需要替代多次
                    while (start != end) {
                        start = textContent.indexOf(mediaUrl, start + mediaUrl.length());
                        SpannableString sss = new SpannableString(mediaUrl);
                        imageSpan = new ImageSpan(context.getApplicationContext(), loadingBitmap);
                        sss.setSpan(imageSpan, 0, mediaUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editable.replace(start, start + mediaUrl.length(), sss);
                    }

                }
                //开始进行预加载
                subscriber.onNext(editable);
                subscriber.onCompleted();

            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Editable>() {
                    @Override
                    public void onCompleted() {
                        //加载便签中的真实图片信息
                        loadMediaInfo(callBack);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Editable editable) {
                        callBack.onLoadNoteData(editable);

                    }
                });

    }


    private void loadMediaInfo(final INoteLoadCallBack callBack) {

        Subscription subscription = Observable
                .create(new Observable.OnSubscribe<Editable>() {
                    @Override
                    public void call(Subscriber<? super Editable> subscriber) {
                        String textContent = note.getContentAll();
                        Editable spannableString = new SpannableStringBuilder(textContent);
                        List<NoteMediaWrapper> datas = new ArrayList<NoteMediaWrapper>();
                        //得到便签从上至下的图片信息
                        for (NoteMediaInfo mediaInfo : note.getNoteMediaInfoList()) {
                            String url = mediaInfo.getMediaUrl();
                            int start = textContent.indexOf(url);
                            int end = textContent.lastIndexOf(url);
                            Log.e("poge", " uri: " + url + " start: " + start + " end: " + end);
                            if (start == -1)
                                continue;
                            datas.add(new NoteMediaWrapper(start, mediaInfo));
                            while (start != end) {
                                start = textContent.indexOf(url, start + url.length());
                                datas.add(new NoteMediaWrapper(start, mediaInfo));
                            }
                        }
                        //按照出现位置前后对信息排序
                        Collections.sort(datas);

                        for (int i = 0; i < datas.size(); i++) {
                            NoteMediaWrapper noteMediaWrapper = datas.get(i);
                            Bitmap noteBitmap = new NoteImageLoader(context, noteMediaWrapper.getMeidaInfo(), noteWidth).getNoteBitmap();
                            ImageSpan imageSpan = new ImageSpan(context, noteBitmap);
                            spannableString.setSpan(imageSpan,noteMediaWrapper.getStartPos(),
                                    noteMediaWrapper.getStartPos()+noteMediaWrapper.getMeidaInfo().getMediaUrl().length(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        subscriber.onNext(spannableString);
                        subscriber.onCompleted();

                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Editable>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Editable editable) {
                    callBack.onLoadNoteData(editable);
                        Log.e("poge",editable.toString());
                    }
                });

    }

    private void addMediaInfo(final Uri uri, final int mediaType) {
        Log.e("poge", "addMediaInfo " + uri.toString());
        Observable.create(new Observable.OnSubscribe<SpannableStringBuilder>() {

            @Override
            public void call(Subscriber<? super SpannableStringBuilder> subscriber) {
                NoteMediaInfo picInfo = new NoteMediaInfo();
                picInfo.setMediaType(mediaType);
                picInfo.setMediaUrl(uri.toString());
                note.addNoteMediaInfo(picInfo);
                Bitmap picBitmap = new NoteImageLoader(context, picInfo, noteWidth).getNoteBitmap();
                SpannableStringBuilder ss = NotePicUtil.getNoteMediaSpanText(context, uri.toString(), picBitmap);
                subscriber.onNext(ss);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SpannableStringBuilder>() {

                    @Override
                    public void onCompleted() {
                        Log.e("poge", "addMediaInfo onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("poge", "addMediaInfo onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(SpannableStringBuilder ss) {
                        Log.e("poge", "addMediaInfo onNext");
                        if (!callBack.isCursorFirstOfLine()) {
                            callBack.moveCursorToNextLine();
                        }
                        callBack.addNoteMediaInfo(ss);
                    }
                });

    }


    public void addPhoto(final Uri uri) {
        addMediaInfo(uri, NoteConstant.MEDIA_PIC);
    }


    public void addPicFromCamera() {
        Log.e("poge", "addPicFromCamera");
        Observable.create(new Observable.OnSubscribe<Uri>() {
            @Override
            public void call(Subscriber<? super Uri> subscriber) {
                Uri result = null;
                Cursor mCursor = context.getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media._ID},
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=?",
                        new String[]{"Camera"},
                        MediaStore.Images.Media._ID + " desc limit 1");

                if (mCursor.moveToNext()) {
                    Uri uri = MediaStore.Images.Media.getContentUri("external");
                    result = ContentUris.withAppendedId(uri, mCursor.getInt(mCursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
                }
                mCursor.close();
                mCursor = null;
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {
                        Log.e("poge", "addPicFromCamera onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("poge", "addPicFromCamera onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(Uri uri) {
                        Log.e("poge", "addPicFromCamera onNext " + uri.toString());
                        if (uri != null)
                            addPhoto(uri);
                    }
                });
    }


    public void addPicFromRecorder(Uri uri) {
        addMediaInfo(uri, NoteConstant.MEDIA_VOICE);
    }


    public void changeBackgroundColor() {
        int bgColor = note.getBgColor();
        bgColor = ++bgColor > 4 ? 1 : bgColor;
        callBack.changeBackGroundColor(PreferenceUtil.getNoteBackgroundColor(context, bgColor));
        note.setBgColor(bgColor);
    }


    public void save(final String text,final boolean toast) {
        Observable.create(new Observable.OnSubscribe<Boolean>(){

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Log.e("poge", "save ID:"+note.getId());
                boolean success=false;
                if (note.getId() == 0 && TextUtils.isEmpty(text.trim())){
                    subscriber.onNext(success);
                    return;
                }
                if (!text.equals(note.getContentAll()))
                    note.setContentAll(text);

                List<NoteMediaInfo> mediaInfos = note.getNoteMediaInfoList();
                Iterator<NoteMediaInfo> iterator = mediaInfos.iterator();
                String pureText = new String(text);
                while (iterator.hasNext()) {
                    NoteMediaInfo info = iterator.next();
                    pureText=pureText.replace(info.getMediaUrl(), "");
                    int index = text.indexOf(info.getMediaUrl());
                    if (index == -1)
                        iterator.remove();
                }

                pureText=pureText.replaceAll("['\n']{2,}", "\n");
                pureText=pureText.trim();
                note.setContentText(pureText);
//                success=note.saveOrUpdate();
                if(note.getId()==0)
                success=note.save();
                Log.e("poge", "save getNoteMediaInfoList:"+note.getNoteMediaInfoList().size());
//                success=note.saveOrUpdate();
                for (NoteMediaInfo noteMediaInfo : note.getNoteMediaInfoList()) {
                    if(!noteMediaInfo.isSaved()){
                        noteMediaInfo.save();
                    }
                }
                subscriber.onNext(success);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("poge", "save onError " + e.getMessage().toString());
                if(toast){
                    Toast.makeText(context,R.string.save_fail,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Log.e("poge", "save onNext " + aBoolean);
               if(toast){
                   Toast.makeText(context,
                           aBoolean?R.string.save_success:R.string.save_fail,
                           Toast.LENGTH_SHORT).show();
               }
            }
        });



    }


}
