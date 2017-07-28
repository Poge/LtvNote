package com.ltv.note.view.pref;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltv.note.R;
import com.ltv.note.view.dialog.FontSizeSelectDialog;
import com.ltv.note.view.dialog.NoteAppInfoDialog;
import com.ltv.note.view.dialog.NoteColorSelectDialog;
import com.ltv.note.view.dialog.NoteShareSelectDialog;

/**
 * Created by Anpo on 2017/7/28.
 */
public class NotePref extends Preference implements DialogInterface.OnDismissListener {

    private TextView tvTitle;
    private TextView tvSummary;
    private ImageView ivNavi;
    private String key;

    public NotePref(Context context) {
        this(context, null);
    }

    public NotePref(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotePref(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NotePref(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayoutResource(R.layout.pref_note);
    }


    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        tvTitle = (TextView) view.findViewById(R.id.setting_title);
        tvSummary = (TextView) view.findViewById(R.id.setting_summary);
        ivNavi = (ImageView) view.findViewById(R.id.setting_navi);
        tvTitle.setText(getTitle());
        tvTitle.setText(getTitleRes());

        key = getKey();
        if(!key.equals("notecolor"))
        ivNavi.setImageResource(R.drawable.set_go);

        if(key.equals("appinfo")||key.equals("notecolor"))
            tvSummary.setVisibility(View.GONE);

        initPrefData();

    }

    private void initPrefData() {
        switch (key) {
            case "fontsize":
                int fontSize = getPersistedInt(3);
                tvSummary.setText(fontSize==1? R.string.font_bigger:fontSize==2?R.string.font_big:
                        fontSize==3?R.string.font_normal:R.string.font_small);
                break;
            case "notecolor":
                int bgColor = getPersistedInt(1);
                ivNavi.setImageResource(bgColor==1?R.drawable.blue:bgColor==2?R.drawable.green:
                        bgColor==3?R.drawable.yellow:R.drawable.pink);
                break;
            case "noteshare":
                int shareType = getPersistedInt(1);
                tvSummary.setText(shareType==1?R.string.share_text:R.string.share_file);
                break;
        }
    }


    @Override
    protected void onClick() {
        Dialog dialog=null;
        switch (key) {
            case "fontsize":
                dialog=new FontSizeSelectDialog(getContext());
                break;
            case "notecolor":
                dialog=new NoteColorSelectDialog(getContext());
                break;
            case "noteshare":
                dialog=new NoteShareSelectDialog(getContext());
                break;
            case "appinfo":
                dialog=new NoteAppInfoDialog(getContext());
                break;
        }
        dialog.setOnDismissListener(this);
        dialog.show();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        initPrefData();
    }
}
