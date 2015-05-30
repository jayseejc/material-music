package com.jayseeofficial.materialmusic.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 30/05/15.
 */
public class Album {
    private List<Song> songs = new ArrayList<>();
    private String title;
    private String artist;
    private String albumArtPath;
    private String key;

    public void addSong(Song... songs) {
        for (Song song : songs) this.songs.add(song);
    }

    public List<Song> getSongs() {
        return songs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumArtPath() {
        return albumArtPath;
    }

    public void setAlbumArtPath(String albumArtPath) {
        this.albumArtPath = albumArtPath;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
