package com.ltv.note.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltv.note.R;
import com.ltv.note.model.INote;
import com.ltv.note.model.bean.Note;
import com.ltv.note.model.bean.NoteConstant;
import com.ltv.note.model.bean.NoteFolder;
import com.ltv.note.model.bean.NoteWrapper;
import com.ltv.note.util.NoteTimeUtil;
import com.ltv.note.view.NoteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anpo on 2017/7/25.
 */
public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NoteWrapper> noteDatas=new ArrayList<>();

    private int selectedSize=0;
    private boolean selectMode;

    public List<NoteWrapper> getNoteDatas() {
        return noteDatas;
    }

    public void setNoteDatas(List<INote> noteDatas) {
        this.noteDatas.clear();
        if(noteDatas!=null)
        for (int i = 0; i < noteDatas.size(); i++) {
            this.noteDatas.add(new NoteWrapper(noteDatas.get(i),false));
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        if(viewType== NoteConstant.NOTE){
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
            holder=new NoteViewHolder(viewItem);
        }else if(viewType== NoteConstant.NOTE_FOLDER){
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_folder, parent, false);
            holder=new NoteFolderViewHolder(viewItem);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        NoteWrapper noteWrapper = noteDatas.get(position);
        INote iNote = noteWrapper.getNote();
        View sView=null;
        switch (itemViewType){
            case NoteConstant.NOTE:
                NoteViewHolder noteHolder= (NoteViewHolder) holder;
                Note note= (Note) iNote;
                noteHolder.tvContent.setText(note.getContentText());
                noteHolder.tvTime.setText(NoteTimeUtil.getCustomStyleTime(note.getEditTime()));
                noteHolder.tvWeek.setText(NoteTimeUtil.getWeek(note.getEditTime()));
                sView=noteHolder.selectView;
                break;

            case NoteConstant.NOTE_FOLDER:
                NoteFolderViewHolder folderHolder= (NoteFolderViewHolder) holder;
                NoteFolder noteFolder= (NoteFolder) iNote;
                folderHolder.tvTime.setText(NoteTimeUtil.getCustomStyleTime(noteFolder.getEditTime()));
                folderHolder.tvWeek.setText(NoteTimeUtil.getWeek(noteFolder.getEditTime()));
                folderHolder.tvFolderName.setText(noteFolder.getName());
                folderHolder.tvNoteNum.setText(noteFolder.getNoteNum()+"个");
                sView=folderHolder.selectView;
                break;
        }

            sView.setVisibility(selectMode?View.VISIBLE:View.GONE);

           if(selectMode){
                sView.setSelected(noteWrapper.isSelect());
            }


    }

    @Override
    public int getItemCount() {
        return noteDatas==null?0:noteDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return noteDatas.get(position).getNote().getNoteType();
    }

    private void onNoteLongClick(RecyclerView.ViewHolder holder){
        if(!selectMode){
            selectMode=true;
            if(mCheckStateListener!=null){
                mCheckStateListener.onNoteCheckStateChanged(CHECKED_START);
            }
            notifyDataSetChanged();
            return;
        }

    }

    private void onNoteClick(View view,RecyclerView.ViewHolder holder){

        int position = holder.getAdapterPosition();
        int itemViewType = getItemViewType(position);
        NoteWrapper noteWrapper = noteDatas.get(position);
        INote iNote = noteWrapper.getNote();
        if(selectMode){

            View sView=itemViewType==NoteConstant.NOTE?((NoteViewHolder)holder).selectView:
                    ((NoteFolderViewHolder)holder).selectView;
                boolean selected = sView.isSelected();
                checkNote(noteWrapper,sView,!selected);

        }else{
            if(itemViewType==NoteConstant.NOTE){
               Note note= (Note) iNote;
                NoteActivity.start(view.getContext(),note.getId());
            }


        }

    }


    private void checkNote(NoteWrapper wrapper,View sView,boolean choose){
        wrapper.setSelect(choose);
        sView.setSelected(choose);
        selectedSize=choose?selectedSize+1:selectedSize-1;
        if(mCheckStateListener!=null&&(selectedSize==noteDatas.size()||selectedSize==0)){
            mCheckStateListener.onNoteCheckStateChanged(selectedSize==0?CHECKED_NONE:CHECKED_ALL);
        }
    }


    public void checkAllNote(){
        for (NoteWrapper noteData : noteDatas) {
            noteData.setSelect(true);
        }
        selectedSize=noteDatas.size();
        if(mCheckStateListener!=null){
            mCheckStateListener.onNoteCheckStateChanged(CHECKED_ALL);
        }
        notifyDataSetChanged();
    }

    public void cancleCheckNote(){
        for (NoteWrapper noteData : noteDatas) {
            noteData.setSelect(false);
        }
        selectedSize=0;
        if(mCheckStateListener!=null){
            mCheckStateListener.onNoteCheckStateChanged(CHECKED_NONE);
        }
        notifyDataSetChanged();
    }



    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public  View selectView;
        public  TextView tvTime;
        public  View ivAlarm;
        public  TextView tvWeek;
        public  TextView tvContent;
        public  ImageView ivThumbnail;

        public NoteViewHolder(View itemView) {
            super(itemView);
            selectView = itemView.findViewById(R.id.notes_item_select);
            tvTime = (TextView) itemView.findViewById(R.id.notes_item_time);
            ivAlarm = itemView.findViewById(R.id.notes_item_alarm);
            tvWeek = (TextView) itemView.findViewById(R.id.notes_item_week);
            tvContent = (TextView) itemView.findViewById(R.id.notes_item_content);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.notes_item_media);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteClick(v,this);
        }

        @Override
        public boolean onLongClick(View v) {
            onNoteLongClick(this);
            return true;
        }
    }




    public  class NoteFolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public  View selectView;
        public  TextView tvTime;
        public  TextView tvWeek;
        public  TextView tvFolderName;
        public  TextView tvNoteNum;

        public NoteFolderViewHolder(View itemView) {
            super(itemView);
            selectView = itemView.findViewById(R.id.notes_item_select);
            tvTime = (TextView) itemView.findViewById(R.id.notes_item_time);
            tvWeek = (TextView) itemView.findViewById(R.id.notes_item_week);
            tvFolderName = (TextView) itemView.findViewById(R.id.notes_folder_name);
            tvNoteNum = (TextView) itemView.findViewById(R.id.folder_notes_num);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteClick(v,this);

        }

        @Override
        public boolean onLongClick(View v) {
            onNoteLongClick(this);

            return true;
        }

    }


    public boolean onBackPressed(){
        if (selectMode){
            selectMode=false;
            for (NoteWrapper noteData : noteDatas) {
                noteData.setSelect(false);
            }
            selectedSize=0;
            if(mCheckStateListener!=null){
                mCheckStateListener.onNoteCheckStateChanged(CHECKED_END);
            }
            notifyDataSetChanged();
            return true;
        }

        return false;
    }


    public static final int CHECKED_END=0;
    public static final int CHECKED_ALL=1;
    public static final int CHECKED_NONE=2;
    public static final int CHECKED_START=3;

    private OnCheckStateChangeListener mCheckStateListener;

    public void setOnCheckStateChangeListener(OnCheckStateChangeListener mCheckStateListener) {
        this.mCheckStateListener = mCheckStateListener;
    }

    public interface OnCheckStateChangeListener{

        void onNoteCheckStateChanged(int state);
    }
}
