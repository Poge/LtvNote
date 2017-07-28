package com.ltv.note.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

/**
 * Created by Anpo on 2017/7/27.
 */
public class NotePicUtil {


    public static Bitmap getAdjustBitmap(int desBitmapWidth,Bitmap src){
        Bitmap des= Bitmap.createBitmap(desBitmapWidth,
                src.getHeight() + 10 * 2, Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(des);
        int left = (desBitmapWidth - src.getWidth()) / 2;
        canvas.drawBitmap(src, left, 10, null);
        canvas=null;
        return des;
    }


    public static SpannableString getNoteMediaSpanText(Context context,String url,Bitmap bitmap){
        final SpannableString ss = new SpannableString(url);
        final ImageSpan span = new ImageSpan(context, bitmap);
        ss.setSpan(span, 0, url.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

}
