package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Song;

/**
 * Created by jon on 30/05/15.
 */
public class PlaybackStartedEvent extends PlaybackEvent {

    public static final String EVENT_TYPE = "start";

    public PlaybackStartedEvent(Song song) {
        super(song);
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }
}
