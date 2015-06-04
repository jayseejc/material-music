package com.jayseeofficial.materialmusic;

import android.util.Log;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SongManager.init(this);
        SongPlayer.init(this);
        NotificationManager.init(this);

        EventBus.getDefault().register(this);
    }

    public void onEvent(Object object) {
        Log.d("Event at " + System.currentTimeMillis() + "!", object.toString());
    }
}
