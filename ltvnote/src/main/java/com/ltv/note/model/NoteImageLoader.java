package com.ltv.note.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.ltv.note.R;
import com.ltv.note.model.bean.NoteConstant;
import com.ltv.note.model.bean.NoteMediaInfo;
import com.ltv.note.util.NotePicUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;

/**
 * Created by Anpo on 2017/7/27.
 */
public class NoteImageLoader {

    private Context context;
    private NoteMediaInfo mediaInfo;
    private int desPicWidth;

    public NoteImageLoader(Context context, NoteMediaInfo mediaInfo, int desPicWidth) {
        this.context = context;
        this.mediaInfo = mediaInfo;
        this.desPicWidth = desPicWidth;
    }


    public Bitmap getNoteBitmap() {
        Bitmap bitmap=null;
        switch (mediaInfo.getMediaType()) {
            case NoteConstant.MEDIA_PIC:
                bitmap=getPicBitmap();
                break;
            case NoteConstant.MEDIA_VOICE:
                bitmap=getRecordBitmap(false);
                break;
        }

        if (bitmap ==null){
            bitmap=getFailBitmap();
        }

        return bitmap;
    }


    public Bitmap getPicBitmap(){
        Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().get(mediaInfo.getMediaUrl());
        if(bitmap==null)
            bitmap=loadPicBitmap();
        return bitmap;

    }

    public Bitmap getRecordBitmap(boolean playing){
        Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().get(mediaInfo.getMediaUrl()+playing);
        if(bitmap==null)
            bitmap=loadRecordBitmap(playing);
        return bitmap;
    }


    private Bitmap loadPicBitmap() {
        BitmapFactory.Options sampleValue = getBitmapInSampleValue(
                mediaInfo.getMediaUrl(), desPicWidth);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .decodingOptions(sampleValue)
                .imageScaleType(
                        ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
        ImageSize imageSize = new ImageSize(500,
                500);

        Bitmap imageSync = ImageLoader
                .getInstance().loadImageSync(mediaInfo.getMediaUrl(),
                        imageSize, options);

        if(imageSync!=null){
            Bitmap adjustBitmap = NotePicUtil.getAdjustBitmap(desPicWidth, imageSync);
            ImageLoader.getInstance().getMemoryCache().put(mediaInfo.getMediaUrl(),adjustBitmap);
            return  adjustBitmap;
        }

        return null;
    }


    private Bitmap loadRecordBitmap(boolean playing) {
        Bitmap bitmap = null;
        Cursor cursor = context.getContentResolver().query(Uri.parse(mediaInfo.getMediaUrl()),
                new String[] { MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.SIZE },
                null, null, null);

        if (cursor!=null&&cursor.moveToNext()) {
            String name = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio
                    .Media.SIZE));

            bitmap = getRecordBitmap(
                    TextUtils.isEmpty(name) ? context.getString(R.string.unnamed)
                            : name, makeSizeString(size), playing);
            cursor.close();
        }

        if(bitmap!=null){
            Bitmap adjustBitmap = NotePicUtil.getAdjustBitmap(desPicWidth, bitmap);
            ImageLoader.getInstance().getMemoryCache().put(mediaInfo.getMediaUrl()+playing,adjustBitmap);
            return  NotePicUtil.getAdjustBitmap(desPicWidth,bitmap);
        }

        return bitmap;
    }

    private Bitmap getRecordBitmap(String name, String size, boolean playing) {
        if (name.length() > 35) {
            String[] split = name.split(".");
            String name1 = split[0].substring(0, 25);
            String suffix = split[1];
            name = name1 + "..." + suffix;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                playing ? R.drawable.record_stop : R.drawable.record_play);

        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#3c3c3c"));
        paint.setAntiAlias(true);
        paint.setTextSize(30);
        canvas.drawText(name, 150, 50, paint);
        canvas.drawText(size, 150, 100, paint);
        bitmap = null;
        return createBitmap;
    }


    private Bitmap getFailBitmap(){
        Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().get("default_load_fail");
        if (bitmap==null){
            Bitmap failBitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.default_load_fail);
            Bitmap adjustBitmap = NotePicUtil.getAdjustBitmap(desPicWidth, failBitmap);
            ImageLoader.getInstance().getMemoryCache().put("default_load_fail",adjustBitmap);
            if(mediaInfo.getMediaType()== NoteConstant.MEDIA_PIC){
                ImageLoader.getInstance().getMemoryCache().put(mediaInfo.getMediaUrl(),adjustBitmap);
            }else{
                ImageLoader.getInstance().getMemoryCache().put(mediaInfo.getMediaUrl()+true,adjustBitmap);
                ImageLoader.getInstance().getMemoryCache().put(mediaInfo.getMediaUrl()+false,adjustBitmap);
            }
        }
        return bitmap;
    }

    private String makeSizeString(long size) {
        StringBuilder sizeBuilder = new StringBuilder();
        if (size <= 0) {
            sizeBuilder.append("0 kb");
            return sizeBuilder.toString();
        }
        long sizeK = size / 1024;
        long sizeM = sizeK / 1024;
        float sizeG = (float) sizeM / 1024;
        if (0 < sizeK && sizeK < 1024) {
            sizeBuilder.append(sizeK);
            sizeBuilder.append(" kb");
            return sizeBuilder.toString();
        } else if (0 < sizeM && sizeM < 1024) {
            sizeBuilder.append(sizeM);
            sizeBuilder.append(" M");
            return sizeBuilder.toString();
        } else {
            sizeBuilder.append(new DecimalFormat("#.##").format(sizeG));
            sizeBuilder.append(" G");
            return sizeBuilder.toString();
        }
    }



    private BitmapFactory.Options getBitmapInSampleValue(String uri, int width) {
        Bitmap pic = null;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        op.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            pic = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, op);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        op.inSampleSize = calculateInSampleSize(op, width);

        op.inJustDecodeBounds = false;
        op.inPurgeable = true;

        return op;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth) {
        // 原始图片的宽高
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (width > reqWidth) {
            inSampleSize = 2;
            int halfWidth = width / 2;
            while ((halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


}
