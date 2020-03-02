package com.meucampus.arthur.testez;

import android.app.Application;

import net.gotev.uploadservice.UploadService;


/**
 * Created by ARTHUR on 17/02/2018.
 */

public class Initializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

    }
}
