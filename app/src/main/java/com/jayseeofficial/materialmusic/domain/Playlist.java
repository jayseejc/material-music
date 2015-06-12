package com.jayseeofficial.materialmusic.domain;

import com.jayseeofficial.materialmusic.SongManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by jon on 06/06/15.
 */
public class Playlist implements Serializable {

    private static Random random = new Random();

    public static int generateNewId() {
        SongManager manager = SongManager.getInstance();
        if (manager == null) return random.nextInt();
        int id;
        do {
            id = random.nextInt();
        } while (!validId(id));
        return id;
    }

    private static boolean validId(int id) {
        List<Playlist> playlists = SongManager.getInstance().getPlaylists();
        if (playlists != null)
            for (Playlist playlist : playlists) if (playlist.getId() == id) return false;
        return true;
    }

    //private MultiKeyMap songs = MultiKeyMap.multiKeyMap(new HashedMap<>());
    private HashMap<Integer, Song> songs = new HashMap<>();
    private int id;
    private String title;

    public Playlist() {
        this.id = generateNewId();
    }

    public synchronized void addSong(Song song) {
        //songs.put(song.getId(), song.getTitle(), song);
        songs.put(song.getId(), song);
    }

    public void addSongs(List<Song> songs) {
        for (Song song : songs) addSong(song);
    }

    public Song getSong(int id) {
        return (Song) songs.get(id);
    }

    public Song getSong(String title) {
        //return (Song) songs.get(title);
        return null;
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs.values());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
