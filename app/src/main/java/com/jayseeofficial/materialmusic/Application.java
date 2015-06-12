package com.jayseeofficial.materialmusic;

import android.util.Log;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class Application extends android.app.Application {

    private static PlaylistManager playlistManager;

    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);

        SongManager.init(this);
        SongPlayer.init(this);
        NotificationManager.init(this);
        playlistManager = FilePlaylistManager.getInstance(this);
    }

    public static PlaylistManager getPlaylistManager() {
        return playlistManager;
    }

    public void onEvent(Object object) {
        Log.d("Event at " + System.currentTimeMillis() + "!", object.toString());
    }

}
