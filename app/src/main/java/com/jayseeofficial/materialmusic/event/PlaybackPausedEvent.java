package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Song;

/**
 * Created by jon on 30/05/15.
 */
public class PlaybackPausedEvent extends PlaybackEvent {

    public static final String EVENT_TYPE = "paused";

    public PlaybackPausedEvent(Song song) {
        super(song);
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }
}
