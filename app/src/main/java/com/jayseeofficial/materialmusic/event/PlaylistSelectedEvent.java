package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Playlist;

/**
 * Created by jon on 06/06/15.
 */
public class PlaylistSelectedEvent {
    private Playlist playlist;

    public PlaylistSelectedEvent(Playlist playlist) {
        this.playlist = playlist;
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
