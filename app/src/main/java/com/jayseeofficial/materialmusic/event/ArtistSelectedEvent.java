package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Artist;

/**
 * Created by jon on 30/05/15.
 */
public class ArtistSelectedEvent {
    private Artist artist;

    public ArtistSelectedEvent(Artist artist) {
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
    }
}
