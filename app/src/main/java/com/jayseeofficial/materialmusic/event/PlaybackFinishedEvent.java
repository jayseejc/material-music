package com.jayseeofficial.materialmusic.event;

/**
 * Created by jon on 30/05/15.
 */
public class PlaybackFinishedEvent implements PlaybackEvent {
    public enum Reason {
        END_OF_TRACK,
        USER,
        UNKNOWN
    }

    private Reason reason;

    public PlaybackFinishedEvent(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
