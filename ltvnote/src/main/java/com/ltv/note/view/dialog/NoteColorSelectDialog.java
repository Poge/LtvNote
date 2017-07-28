package com.ltv.note.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;

import com.ltv.note.R;

public class NoteColorSelectDialog extends Dialog {

	
	private Context context;
	private View blue,green,yellow,pink;
	private View t1_choice,t2_choice,t3_choice,t4_choice;
	
	public NoteColorSelectDialog(Context context) {
		super(context);
		this.context=context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCustomDialog();
		
	}
	
	private void setCustomDialog() {
		View view = View.inflate(context, R.layout.dialog_note_color, null);
		blue = view.findViewById(R.id.blue);
		green = view.findViewById(R.id.green);
		yellow = view.findViewById(R.id.yellow);
		pink = view.findViewById(R.id.pink);
		blue.setOnClickListener(listener);
		green.setOnClickListener(listener);
		yellow.setOnClickListener(listener);
		pink.setOnClickListener(listener);
		
		
		t1_choice = view.findViewById(R.id.t1_choice);
		t2_choice = view.findViewById(R.id.t2_choice);
		t3_choice = view.findViewById(R.id.t3_choice);
		t4_choice = view.findViewById(R.id.t4_choice);
		
		view.findViewById(R.id.font_cancle).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				
			}
		});
		
		
		
		diplayDefaultColor();
		super.setContentView(view);
	}
	
	private void chooseNoteColor(int choose){
		t4_choice.setVisibility(choose==R.id.blue?View.VISIBLE:View.INVISIBLE);
		t3_choice.setVisibility(choose==R.id.green?View.VISIBLE:View.INVISIBLE);
		t2_choice.setVisibility(choose==R.id.yellow?View.VISIBLE:View.INVISIBLE);
		t1_choice.setVisibility(choose==R.id.pink?View.VISIBLE:View.INVISIBLE);
		
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putInt("notecolor",choose==R.id.blue?1: choose==R.id.green?2:choose==R.id.yellow?3:4).commit();
		
	}
	
	private void diplayDefaultColor(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int defaultColor = preferences.getInt("notecolor", 1);
		int choose=defaultColor==1?R.id.blue:defaultColor==2?R.id.green:defaultColor==3?R.id.yellow:R.id.pink;
		chooseNoteColor(choose);
	}
	
	
	
	private View.OnClickListener listener=new View.OnClickListener(){
		
		@Override
		public void onClick(View v) {
			chooseNoteColor(v.getId());
		}
	};

	

}
