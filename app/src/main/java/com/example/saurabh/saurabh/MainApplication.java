package com.example.saurabh.saurabh;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import pl.tajchert.nammu.Nammu;

/**
 * Created by saurabh on 9/10/16.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());
        Nammu.init(this);
    }
}
