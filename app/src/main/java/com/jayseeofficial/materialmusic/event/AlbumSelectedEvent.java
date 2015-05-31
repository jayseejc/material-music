package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Album;

/**
 * Created by jon on 30/05/15.
 */
public class AlbumSelectedEvent {
    private Album album;
    public AlbumSelectedEvent(Album album) {
        this.album=album;
    }

    public Album getAlbum() {
        return album;
    }
}
