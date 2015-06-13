package com.jayseeofficial.materialmusic.activity;

import android.preference.PreferenceActivity;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.jayseeofficial.materialmusic.BuildConfig;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 12/06/15.
 */
public class BasePreferenceActivity extends PreferenceActivity {
    private View contentView;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        contentView = findViewById(android.R.id.content);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(Object object) {
        if (BuildConfig.DEBUG) {
            Snackbar.make(contentView, "Event: " + object.getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        Log.d(getClass().getSimpleName(), "Fragment validation deferred for " + fragmentName);
        return true;
    }
}
