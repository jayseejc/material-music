package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Playlist;
import com.jayseeofficial.materialmusic.domain.Song;

/**
 * Created by jon on 09/06/15.
 */
public class SongAddedToPlaylistEvent {
    private final Playlist playlist;
    private final Song song;

    public SongAddedToPlaylistEvent(Song song, Playlist playlist) {
        this.song = song;
        this.playlist = playlist;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Song getSong() {
        return song;
    }
}
