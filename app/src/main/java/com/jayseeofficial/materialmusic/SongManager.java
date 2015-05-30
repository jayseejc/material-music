package com.jayseeofficial.materialmusic;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.ArrayMap;

import com.jayseeofficial.materialmusic.domain.Album;
import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.AlbumsLoadedEvent;
import com.jayseeofficial.materialmusic.event.LibraryLoadedEvent;
import com.jayseeofficial.materialmusic.event.SongsLoadedEvent;

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

    public static SongManager getInstance() {
        if (instance == null)
            throw new IllegalStateException("Context not provided! Cannot initialize!");
        return instance;
    }

    private Context context;
    private List<Song> songs;
    private ArrayMap<String, Album> albums;
    private boolean isLoaded = false;

    private SongManager(Context context) {
        this.context = context;
        loadLibraryInBackground();
    }

    private void loadLibraryInBackground() {
        new Thread(() -> {
            Cursor cursor;
            // Load albums
            cursor = context.getContentResolver().query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Audio.Albums.ALBUM,
                            MediaStore.Audio.Albums.ALBUM_ART,
                            MediaStore.Audio.Albums.ARTIST,
                            MediaStore.Audio.Albums.ALBUM_KEY
                    },
                    null,
                    null,
                    null
            );
            int albumTitleColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int albumArtColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int albumArtistColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int albumKeyColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY);

            albums = new ArrayMap<>(cursor.getCount());

            while (cursor.moveToNext()) {
                Album album = new Album();
                album.setTitle(cursor.getString(albumTitleColumn));
                album.setArtist(cursor.getString(albumArtistColumn));
                album.setAlbumArtPath(cursor.getString(albumArtColumn));
                album.setKey(cursor.getString(albumKeyColumn));
                albums.put(cursor.getString(albumKeyColumn), album);
            }
            cursor.close();
            EventBus.getDefault().post(new AlbumsLoadedEvent());
            // Load songs
            cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.DURATION,
                            MediaStore.Audio.Media.ALBUM_KEY,
                            MediaStore.Audio.Media._ID},
                    MediaStore.Audio.Media.IS_MUSIC + " = 1",
                    null,
                    null
            );
            int songTitleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLengthColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songAlbumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY);
            int songIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            songs = new ArrayList<Song>(cursor.getCount());
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setTitle(cursor.getString(songTitleColumn));
                song.setArtist(cursor.getString(songArtistColumn));
                song.setLength(cursor.getInt(songLengthColumn));
                song.setId(cursor.getInt(songIdColumn));
                song.setAlbumKey(cursor.getString(songAlbumIdColumn));
                songs.add(song);
                if (albums.get(song.getAlbumKey()) != null) {
                    albums.get(song.getAlbumKey()).addSong(song);
                }
            }
            isLoaded = true;
            EventBus.getDefault().post(new SongsLoadedEvent());
            EventBus.getDefault().post(new LibraryLoadedEvent());
        }).start();
    }

    public List<Song> getSongs() {
        return songs;
    }

    public Uri getSongUri(Song song) {
        return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Integer.toString(song.getId()));
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public Album getAlbum(Song song) {
        return albums.get(song.getAlbumKey());
    }
}
