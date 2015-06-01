package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 01/06/15.
 */
public class SongSelectedEvent {
    private Song song;
    private List<Song> songs;

    public SongSelectedEvent(Song song) {
        ArrayList<Song> songs = new ArrayList<>(1);
        songs.add(song);
        this.songs = songs;
        this.song = song;
    }

    public SongSelectedEvent(Song song, List<Song> songs) {
        if (!songs.contains(song)) throw new IllegalArgumentException("List must contain song");
        this.song = song;
        this.songs = songs;
    }

    public Song getSong() {
        return song;
    }

    public List<Song> getSongList() {
        return songs;
    }
}
