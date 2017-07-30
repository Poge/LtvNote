package com.ltv.note.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ltv.note.R;
import com.ltv.note.model.bean.Note;
import com.ltv.note.presenter.NotePresenter;
import com.ltv.note.util.NoteTimeUtil;
import com.ltv.note.util.PreferenceUtil;
import com.ltv.note.view.iview.INoteLoadCallBack;

public class NoteActivity extends AppCompatActivity implements INoteLoadCallBack, View.OnClickListener {

    private final int ADD_VOICE = 1;
    private final int ADD_PICTURE = 2;
    private final int ADD_CAMERA = 3;

    private EditText etNote;
    private ImageView ivClock, ivRecord, ivPhoto, ivCamera,
            ivShare;
    private NotePresenter notePresenter;
    private ScrollView scrollView;

    public static void start(Context context, long noteId) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("NoteId", noteId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_note);
        toolbar.setNavigationIcon(R.drawable.add_back);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        long noteId = getIntent().getLongExtra("NoteId", 0);
        scrollView = (ScrollView) findViewById(R.id.scrollview_note);
        etNote = (EditText) findViewById(R.id.note_edit_view);
        ivClock = (ImageView) findViewById(R.id.add_remind);
        ivClock.setOnClickListener(this);
        ivRecord = (ImageView) findViewById(R.id.add_record);
        ivRecord.setOnClickListener(this);
        ivPhoto = (ImageView) findViewById(R.id.add_photo);
        ivPhoto.setOnClickListener(this);
        ivCamera = (ImageView) findViewById(R.id.add_camera);
        ivCamera.setOnClickListener(this);
        ivShare = (ImageView) findViewById(R.id.add_send);
        ivShare.setOnClickListener(this);
        notePresenter = new NotePresenter(this, noteId);
        notePresenter.setCallBack(this);
        etNote.post(new Runnable() {
            @Override
            public void run() {
                int width = etNote.getLayout().getWidth();
                Log.e("poge", "width :" + width);
                notePresenter.setNoteWidth(width);
                notePresenter.loadData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_color:
                notePresenter.changeBackgroundColor();
                break;
            case R.id.menu_save:
                notePresenter.save(etNote.getText().toString(),true);
                break;
            case android.R.id.home:
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initNoteView(Note note) {
        TextView tvTitle = (TextView) findViewById(R.id.title_note);
        tvTitle.setText(NoteTimeUtil.getNoteTitle(note.getEditTime()));
        etNote.setTextSize(PreferenceUtil.getNoteFontSize(this));
        scrollView.setBackgroundColor(PreferenceUtil.getNoteBackgroundColor(this,note.getBgColor()));
    }

    @Override
    public void onLoadNoteData(Editable edit) {
        etNote.setText(edit);
    }

    @Override
    public Editable getNoteText() {
        return etNote.getText();
    }

    @Override
    public void moveCursorToPos(int pos) {
        if (pos >= 0)
            etNote.setSelection(pos);
    }

    @Override
    public void moveCursorToNextLine() {
        etNote.append("\n");
    }

    @Override
    public boolean isCursorFirstOfLine() {
        int index = etNote.getSelectionStart();
        if (index <= 0) {
            return true;
        }
        Layout layout = etNote.getLayout();
        if (layout != null) {
            int line = layout.getLineForOffset(index);
            int lineStart = layout.getLineStart(line);
            if (lineStart == index) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addNoteMediaInfo(SpannableStringBuilder ss) {
        Log.e("poge", "addNoteMediaInfo");
        int position = etNote.getSelectionStart();
        if (position >= 0) {
            etNote.getText().insert(position, ss);
        } else {
            etNote.append(ss);
        }

        etNote.setSelection(position + ss.length());
        etNote.append("\n");
    }

    @Override
    public void changeBackGroundColor(int bgColor) {
        scrollView.setBackgroundColor(bgColor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_remind:
                break;
            case R.id.add_record:
                openRecorder();
                break;
            case R.id.add_photo:
                Intent pIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pIntent, ADD_PICTURE);
                break;
            case R.id.add_camera:
                openCamera();
                break;
            case R.id.add_send:
                break;
        }
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ADD_CAMERA);
        } else {
            Intent cIntent = new Intent();
            cIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cIntent, ADD_CAMERA);
        }

    }

    /**
     * 打开录音
     */
    private void openRecorder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ADD_VOICE);
        } else {
            Intent rIntent = new Intent(
                    MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivityForResult(rIntent, ADD_VOICE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_VOICE:
                    Uri vUri = data.getData();
                    notePresenter.addPicFromRecorder(vUri);
                    break;
                case ADD_PICTURE:
                    Uri pUri = data.getData();
                    notePresenter.addPhoto(pUri);
                    break;
                case ADD_CAMERA:
                    notePresenter.addPicFromCamera();
                    break;

                default:
                    break;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ADD_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Open Camera Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case ADD_VOICE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openRecorder();
                } else {
                    Toast.makeText(this, "Open Recorder Denied", Toast.LENGTH_SHORT).show();
                }
                break;

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        notePresenter.save(etNote.getText().toString(),false);
        super.onDestroy();
    }

}
