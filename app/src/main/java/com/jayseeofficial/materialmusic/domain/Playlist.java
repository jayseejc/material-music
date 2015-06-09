package com.jayseeofficial.materialmusic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jon on 06/06/15.
 */
public class Playlist implements Serializable {
    //private MultiKeyMap songs = MultiKeyMap.multiKeyMap(new HashedMap<>());
    private HashMap<Integer, Song> songs = new HashMap<>();

    private String title;

    public Playlist() {
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
}
