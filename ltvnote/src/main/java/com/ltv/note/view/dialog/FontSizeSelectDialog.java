package com.ltv.note.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.ltv.note.R;
import com.ltv.note.util.DensityUtil;

public class FontSizeSelectDialog extends Dialog {

	
	private Context context;
	private View t1,t2,t3,t4;
	private View t1_choice,t2_choice,t3_choice,t4_choice;
	
	public FontSizeSelectDialog(Context context) {
		super(context);
		this.context=context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCustomDialog();
	}


	
	private void setCustomDialog() {
		View view = View.inflate(context, R.layout.dialog_font_size, null);
		t1 = view.findViewById(R.id.t1);
		t2 = view.findViewById(R.id.t2);
		t3 = view.findViewById(R.id.t3);
		t4 = view.findViewById(R.id.t4);
		t1.setOnClickListener(listener);
		t2.setOnClickListener(listener);
		t3.setOnClickListener(listener);
		t4.setOnClickListener(listener);
		
		
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
		
		diplayDefaultSize();
		int width = DensityUtil.dp2px(getContext(),362);
		int height = DensityUtil.dp2px(getContext(),237);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

		super.setContentView(view);
	}
	
	private void chooseTextFont(int choose){
		t1_choice.setVisibility(choose==R.id.t1?View.VISIBLE:View.INVISIBLE);
		t2_choice.setVisibility(choose==R.id.t2?View.VISIBLE:View.INVISIBLE);
		t3_choice.setVisibility(choose==R.id.t3?View.VISIBLE:View.INVISIBLE);
		t4_choice.setVisibility(choose==R.id.t4?View.VISIBLE:View.INVISIBLE);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		preferences.edit().putInt("fontsize",choose==R.id.t1?1: choose==R.id.t2?2:choose==R.id.t3?3:4).commit();
	}
	
	private void diplayDefaultSize(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int defaultSize = preferences.getInt("fontsize", 3);
		int choose=defaultSize==1?R.id.t1:defaultSize==2?R.id.t2:defaultSize==3?R.id.t3:R.id.t4;
		chooseTextFont(choose);
	}
	
	private View.OnClickListener listener=new View.OnClickListener(){
		
		@Override
		public void onClick(View v) {
			chooseTextFont(v.getId());
		}
	};


}
