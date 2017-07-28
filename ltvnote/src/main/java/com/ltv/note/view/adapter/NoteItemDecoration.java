package com.ltv.note.view.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ltv.note.R;

/**
 * Created by Anpo on 2017/7/25.
 */
public class NoteItemDecoration extends RecyclerView.ItemDecoration {


    public NoteItemDecoration() {
        super();

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int divideHeight = (int) parent.getContext().getResources().getDimension(R.dimen.list_item_divide_height);
        int left = parent.getPaddingLeft();
        int right=parent.getWidth()-parent.getPaddingRight();
        int childCount = parent.getChildCount();
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#03a9f4"));
        for (int i=0;i<childCount;i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top=child.getBottom()+params.bottomMargin;
            int bottom=top+divideHeight;
            c.drawRect(left,top,right,bottom,paint);
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int divideHeight = (int) parent.getContext().getResources().getDimension(R.dimen.list_item_divide_height);
        outRect.set(0,0,0,divideHeight);
    }

}
