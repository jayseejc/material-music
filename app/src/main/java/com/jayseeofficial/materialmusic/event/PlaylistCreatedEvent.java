package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Playlist;

/**
 * Created by jon on 09/06/15.
 */
public class PlaylistCreatedEvent {
    private Playlist playlist;

    public PlaylistCreatedEvent(Playlist playlist) {
        this.playlist = playlist;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

}
