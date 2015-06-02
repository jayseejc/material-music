package com.jayseeofficial.materialmusic.event;

/**
 * Created by jon on 01/06/15.
 */
public class ShuffleEvent {
    private boolean shuffleOn;

    public ShuffleEvent(boolean shuffleOn) {
        this.shuffleOn = shuffleOn;
    }

    public boolean isShuffleOn() {
        return shuffleOn;
    }
}
