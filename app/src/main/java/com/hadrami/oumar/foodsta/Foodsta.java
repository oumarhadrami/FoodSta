package com.hadrami.oumar.foodsta;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by oumar on 26/12/2016.
 */

public class Foodsta extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}
