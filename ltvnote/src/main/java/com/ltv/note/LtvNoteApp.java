package com.ltv.note;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.litepal.LitePal;

/**
 * Created by Anpo on 2017/7/24.
 */
public class LtvNoteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        CrashHandler.getInstace().init(this);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
    }
}
