package com.jayseeofficial.materialmusic.event;

import android.media.AudioManager;

/**
 * Created by jon on 03/06/15.
 */
public class AudioFocusChangedEvent {

    public enum FocusEvent {
        GAIN,
        LOSS,
        LOSS_TRANSIENT,
        LOSS_TRANSIENT_CAN_DUCK
    }

    private FocusEvent event;

    public AudioFocusChangedEvent(int changeType) {
        switch (changeType) {
            case AudioManager.AUDIOFOCUS_GAIN:
                event = FocusEvent.GAIN;
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                event = FocusEvent.LOSS;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                event = FocusEvent.LOSS_TRANSIENT;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                event = FocusEvent.LOSS_TRANSIENT_CAN_DUCK;
                break;
            default:
                throw new IllegalArgumentException("Cannot parse argument to AudioFocusChangedEvent");
        }
    }

    public AudioFocusChangedEvent(FocusEvent event) {
        this.event = event;
    }

    public FocusEvent getChangeType() {
        return event;
    }

}
