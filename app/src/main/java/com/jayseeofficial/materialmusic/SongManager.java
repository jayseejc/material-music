package com.jayseeofficial.materialmusic;

import android.content.Context;

import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.LibraryLoadedEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class SongManager {

    static SongManager instance;

    public static SongManager getInstance(Context context) {
        if (instance == null) instance = new SongManager(context.getApplicationContext());
        return instance;
    }

    private Context context;
    private List<Song> songs;
    private boolean isLoaded = false;

    private SongManager(Context context) {
        this.context = context;
        loadLibraryInBackground();
        songs = new ArrayList<>(100 * 100);
    }

    private void loadLibraryInBackground() {
        new Thread(() -> {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    Song song = new Song();
                    song.setTitle("Song " + (j + 1));
                    song.setLength((j + 1) * 7);
                    song.setArtist("Artist " + (i + 1));
                    songs.add(song);
                }
            }
            isLoaded = true;
            EventBus.getDefault().post(new LibraryLoadedEvent());
        }).start();
    }

    public List<Song> getSongs() {
        return songs;
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}
