package com.jayseeofficial.materialmusic;

import android.content.Context;

import com.google.gson.Gson;
import com.jayseeofficial.materialmusic.domain.Playlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 06/06/15.
 */
public class FilePlaylistManager implements PlaylistManager {
    private static FilePlaylistManager instance;

    public static PlaylistManager getInstance(Context context) {
        if (instance == null) instance = new FilePlaylistManager(context);
        return instance;
    }

    private Context context;
    private Gson gson;

    private FilePlaylistManager(Context context) {
        this.context = context.getApplicationContext();
        gson = new Gson();
    }

    @Override
    public List<Playlist> loadPlaylists() {
        File playlistDir = new File(context.getFilesDir(), "playlists");
        if (!playlistDir.exists()) playlistDir.mkdir();
        File[] playlistFiles = playlistDir.listFiles();
        List<Playlist> playlistList = new ArrayList<>();
        for (File file : playlistFiles) {
            if (file.getName().endsWith(".playlist")) {
                try {
                    playlistList.add(gson.fromJson(new FileReader(file), Playlist.class));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return playlistList;
    }

    public boolean savePlaylists(List<Playlist> playlists) {
        File playlistDir = new File(context.getFilesDir(), "playlists");
        if (!playlistDir.exists()) playlistDir.mkdir();
        for (Playlist playlist : playlists) {
            File file = new File(playlistDir, playlist.getTitle() + ".playlist");
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(gson.toJson(playlist));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
