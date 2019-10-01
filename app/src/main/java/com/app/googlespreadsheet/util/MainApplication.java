package com.app.googlespreadsheet.util;

import android.app.Application;
import android.content.Context;

/**
 * TODO: Add a class header comment!
 */
public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
