package com.jayseeofficial.materialmusic.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jayseeofficial.materialmusic.BuildConfig;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class BaseActivity extends AppCompatActivity {

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

}
