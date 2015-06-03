package com.jayseeofficial.materialmusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jayseeofficial.materialmusic.event.PlaybackToggleEvent;
import com.jayseeofficial.materialmusic.event.SkipEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 03/06/15.
 */
public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_KEY = "action";

    public enum Action {
        NEXT,
        PREV,
        TOGGLE
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Action action = (Action) intent.getExtras().get(ACTION_KEY);
        if (action == null)
            throw new IllegalArgumentException("Must provide an Action to perform.");
        switch (action){
            case NEXT:
                EventBus.getDefault().post(new SkipEvent(SkipEvent.Direction.NEXT));
                break;
            case PREV:
                EventBus.getDefault().post(new SkipEvent(SkipEvent.Direction.PREVIOUS));
                break;
            case TOGGLE:
                EventBus.getDefault().post(new PlaybackToggleEvent());
                break;
            default:
                throw new IllegalArgumentException("Must provide an Action to perform.");
        }
    }
}
