package com.jayseeofficial.materialmusic.event;

import com.jayseeofficial.materialmusic.domain.Song;

/**
 * Created by jon on 30/05/15.
 */
public class PlaybackFinishedEvent extends PlaybackEvent {

    public static final String EVENT_TYPE = "finished";

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public enum Reason {
        END_OF_TRACK,
        USER,
        UNKNOWN
    }

    private Reason reason;

    public PlaybackFinishedEvent(Song song, Reason reason) {
        super(song);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
