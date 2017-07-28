package com.ltv.note.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;

import com.ltv.note.R;


public class NoteAppInfoDialog extends Dialog {

	private Context context;
	
	
	public NoteAppInfoDialog(Context context) {
		super(context);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCustomDialog();

	}
	
 
	private void setCustomDialog() {
		View view = View.inflate(context, R.layout.dialog_about_us, null);
		view.findViewById(R.id.about_net).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   Intent intent = new Intent();       
		            intent.setAction("android.intent.action.VIEW");   
		            Uri content_url = Uri.parse("http://www.qualper.com");  
		            intent.setData(content_url); 
		            context.startActivity(intent);
		            dismiss();
				
			}
		});
		super.setContentView(view);
	}

	
}
