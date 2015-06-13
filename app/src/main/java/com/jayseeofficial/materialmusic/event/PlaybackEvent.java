package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Song;

/**
 * Created by jon on 30/05/15.
 */
public abstract class PlaybackEvent {

    private Song song;

    public PlaybackEvent(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }

    public abstract String getEventType();
}
