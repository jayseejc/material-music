package com.jayseeofficial.materialmusic.activity;

import android.content.Intent;
import android.os.Bundle;

import com.jayseeofficial.materialmusic.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DebugActivity extends BaseActivity {

    @OnClick(R.id.btn_debug_library_view_activity)
    void onLibraryViewActivityClicked() {
        startActivity(new Intent(this, LibraryViewActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        ButterKnife.inject(this);
    }
}
