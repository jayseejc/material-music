package com.jayseeofficial.materialmusic.event;

/**
 * Created by jon on 30/05/15.
 */
public class PlaybackResumedEvent implements PlaybackEvent {

    public static final String EVENT_TYPE = "resume";

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }
}
