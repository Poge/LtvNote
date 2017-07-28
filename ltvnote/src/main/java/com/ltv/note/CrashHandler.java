package com.ltv.note;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {
	
	
	
	private static CrashHandler sInstance=new CrashHandler();
	@SuppressWarnings("unused")
	private Context mContext;
	private UncaughtExceptionHandler mDefaultCrashHandler;
	
	
	private static final String  PATH=Environment.getExternalStorageDirectory().getPath()+"/CrashTest/log/";
    private static final String FILE_NAME="crash_poge";
    private static final String FILE_SUFFIX=".trace";
	private CrashHandler() {
	}
	
	public static CrashHandler getInstace(){
		
		return sInstance;
		
	}
	
	public void init(Context context){
		mContext=context.getApplicationContext();
		mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
//		try {
//			dumpExceptionToSDCard(ex);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		ex.printStackTrace();
		Log.e("poge error",ex.getMessage().toString());

		if(mDefaultCrashHandler!=null)
         mDefaultCrashHandler.uncaughtException(thread, ex);
		
	}
	
	
	private void dumpExceptionToSDCard(Throwable ex) throws IOException{
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return;
		}
		
		File dir=new File(PATH);
		if(!dir.exists())
			dir.mkdirs();
		long current = System.currentTimeMillis();
		String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date(current));
		File file = new File(PATH+FILE_NAME+time+FILE_SUFFIX);
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(time);
			pw.println();
			ex.printStackTrace(pw);
			pw.close();
		} catch (Exception e) {
		}
		
		
		
		
	}

}
