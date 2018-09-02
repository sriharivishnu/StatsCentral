package com.magnitudestudios.sriharivishnu.statscentral;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sriharivishnu on 2018-08-01.
 */

public class Utils extends Application{
    private static FirebaseDatabase mDatabase;
    @Override
    public void onCreate() {
        super.onCreate();
        if (!FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }


}
