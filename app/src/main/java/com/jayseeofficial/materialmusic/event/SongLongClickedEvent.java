package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Song;

/**
 * Created by jon on 09/06/15.
 */
public class SongLongClickedEvent {
    private Song song;
    public SongLongClickedEvent(Song song) {
        this.setSong(song);
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
