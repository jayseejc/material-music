package com.jayseeofficial.materialmusic.event;

/**
 * Created by jon on 01/06/15.
 */
public class SeekEvent {
    private int seekTo = 0;

    public SeekEvent(int seekTo) {
        this.seekTo = seekTo;
    }

    public int getPosition() {
        return seekTo;
    }
}
