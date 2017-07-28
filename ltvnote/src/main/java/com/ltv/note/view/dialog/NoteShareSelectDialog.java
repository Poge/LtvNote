package com.ltv.note.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;

import com.ltv.note.R;

public class NoteShareSelectDialog extends Dialog {

	
	private Context context;
	private View shareText,shareFile;
	private View text_choice,file_choice;
	
	public NoteShareSelectDialog(Context context) {
		super(context);
		this.context=context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCustomDialog();
		
	}
	
	private void setCustomDialog() {
		View view = View.inflate(context, R.layout.dialog_note_share, null);
		shareText = view.findViewById(R.id.share_text);
		shareFile = view.findViewById(R.id.share_file);
		shareText.setOnClickListener(listener);
		shareFile.setOnClickListener(listener);
		
		
		text_choice = view.findViewById(R.id.text_choice);
		file_choice = view.findViewById(R.id.file_choice);
		
		view.findViewById(R.id.share_close).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				
			}
		});
		
		
		diplayDefaultShare();
		super.setContentView(view);
	}
	
	private void chooseNoteShare(int choose){
		file_choice.setVisibility(choose==R.id.share_file?View.VISIBLE:View.INVISIBLE);
		text_choice.setVisibility(choose==R.id.share_text?View.VISIBLE:View.INVISIBLE);
		
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putInt("noteshare",choose==R.id.share_text?1:2).commit();
		
	}
	
	private void diplayDefaultShare(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int defaultColor = preferences.getInt("noteshare", 1);
		int choose=defaultColor==1?R.id.share_text:R.id.share_file;
		chooseNoteShare(choose);
	}
	
	
	
	private View.OnClickListener listener=new View.OnClickListener(){
		
		@Override
		public void onClick(View v) {
			chooseNoteShare(v.getId());
		}
	};

	

}
