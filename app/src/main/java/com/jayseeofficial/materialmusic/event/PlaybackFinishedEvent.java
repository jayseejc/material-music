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
}
