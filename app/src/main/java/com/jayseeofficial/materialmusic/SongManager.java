package com.jayseeofficial.materialmusic;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

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
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.DURATION,
                            MediaStore.Audio.Media._ID},
                    MediaStore.Audio.Media.IS_MUSIC + " = 1",
                    null,
                    null
            );
            
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int lengthColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setTitle(cursor.getString(titleColumn));
                song.setArtist(cursor.getString(artistColumn));
                song.setLength(cursor.getInt(lengthColumn));
                song.setId(cursor.getInt(idColumn));
                songs.add(song);
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
