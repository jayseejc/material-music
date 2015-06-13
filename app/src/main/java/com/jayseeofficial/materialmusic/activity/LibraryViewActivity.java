package com.jayseeofficial.materialmusic.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jayseeofficial.materialmusic.BuildConfig;
import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.SongManager;
import com.jayseeofficial.materialmusic.SongPlayer;
import com.jayseeofficial.materialmusic.domain.Album;
import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.AlbumSelectedEvent;
import com.jayseeofficial.materialmusic.event.ArtistSelectedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackEvent;
import com.jayseeofficial.materialmusic.event.SeekEvent;
import com.jayseeofficial.materialmusic.event.ShuffleEvent;
import com.jayseeofficial.materialmusic.event.SkipEvent;
import com.jayseeofficial.materialmusic.fragment.AlbumFragment;
import com.jayseeofficial.materialmusic.fragment.ArtistFragment;
import com.jayseeofficial.materialmusic.fragment.SongFragment;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class LibraryViewActivity extends BaseActivity {

    enum Mode {
        SONGS, ALBUMS, ARTISTS, PLAYLISTS
    }

    private static final String SAVED_MODE = "mode";
    private static final String SAVED_POSITION = "position";

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.nav_view)
    NavigationView navigationView;

    @InjectView(R.id.tb_main)
    Toolbar tbMain;

    @InjectView(R.id.img_nav_header)
    ImageView imgNavHeader;

    @InjectView(R.id.txt_title)
    TextView txtTitle;

    @InjectView(R.id.txt_subtitle)
    TextView txtSubTitle;

    @InjectView(R.id.sb_position)
    SeekBar sbPosition;

    @OnClick(R.id.btn_next)
    public void nextTrack() {
        EventBus.getDefault().post(new SkipEvent(SkipEvent.Direction.NEXT));
    }

    @OnClick(R.id.btn_prev)
    public void previousTrack() {
        EventBus.getDefault().post(new SkipEvent(SkipEvent.Direction.PREVIOUS));
    }

    @InjectView(R.id.btn_play)
    ImageButton btnPlay;
    ImageButton navBtnPlay;
    ImageButton navBtnPrev;
    ImageButton navBtnNext;
    TextView navTxtTitle;
    TextView navTxtSubtitle;

    ActionBarDrawerToggle toggle;

    @OnClick(R.id.btn_play)
    public void toggleTrack() {
        SongPlayer.getInstance(this).toggleSong();
    }

    private Mode mode;

    private boolean pauseUpdate = false;
    private Thread seekBarThread = new Thread(() -> {
        while (true) {
            try {
                if (!pauseUpdate)
                    sbPosition.setProgress(
                            SongPlayer.getInstance(LibraryViewActivity.this).getCurrentPosition());
                if (sbPosition.getVisibility() != View.VISIBLE)
                    runOnUiThread(() -> sbPosition.setVisibility(View.VISIBLE));
            } catch (NullPointerException ex) {
                if (!pauseUpdate)
                    sbPosition.setProgress(0);
                if (sbPosition.getVisibility() != View.INVISIBLE)
                    runOnUiThread(() -> sbPosition.setVisibility(View.INVISIBLE));
            }
            try {
                // Only update 4 times a second.
                Thread.sleep(250);
            } catch (InterruptedException e) {
            }
        }
    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_library_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                item.setChecked(!item.isChecked());
                setShuffle(item.isChecked());
                return true;
        }
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShuffle(boolean shuffle) {
        EventBus.getDefault().post(new ShuffleEvent(shuffle));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_view);

        ButterKnife.inject(this);
        // Butterknife is having trouble finding the media controls in the nav drawer, so we define
        // their actions here manually.
        navBtnPlay = (ImageButton) navigationView.findViewById(R.id.btn_play);
        navBtnNext = (ImageButton) navigationView.findViewById(R.id.btn_next);
        navBtnPrev = (ImageButton) navigationView.findViewById(R.id.btn_prev);
        navTxtTitle = (TextView) navigationView.findViewById(R.id.txt_title);
        navTxtSubtitle = (TextView) navigationView.findViewById(R.id.txt_subtitle);
        navBtnPlay.setOnClickListener(v -> toggleTrack());
        navBtnPrev.setOnClickListener(v -> previousTrack());
        navBtnNext.setOnClickListener(v -> nextTrack());
        sbPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    EventBus.getDefault().post(new SeekEvent(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseUpdate = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pauseUpdate = false;
            }
        });

        setSupportActionBar(tbMain);
        if (BuildConfig.DEBUG) navigationView.getMenu().add("Debug");
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.action_songs) {
                setMode(Mode.SONGS);
            } else if (id == R.id.action_albums) {
                setMode(Mode.ALBUMS);
            } else if (id == R.id.action_artisis) {
                setMode(Mode.ARTISTS);
            } else if (id == R.id.action_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else if (menuItem.getTitle().equals("Debug")) {
                startActivity(new Intent(this, DebugActivity.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        toggle.setToolbarNavigationClickListener(v -> {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                drawerLayout.closeDrawer(Gravity.LEFT);
            else
                drawerLayout.openDrawer(Gravity.LEFT);
        });
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState != null) {
            setMode((Mode) savedInstanceState.getSerializable(SAVED_MODE));
        } else setMode(Mode.SONGS);

        refreshPlayIcon();
        seekBarThread.start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(SAVED_MODE, mode);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onEventMainThread(PlaybackEvent event) {
        refreshPlayIcon();
        Song currentSong = SongPlayer.getInstance(this).getCurrentSong();
        if (currentSong != null) {
            navTxtTitle.setText(currentSong.getTitle());
            txtTitle.setText(currentSong.getTitle());
            navTxtSubtitle.setText(currentSong.getArtist());
            txtSubTitle.setText(currentSong.getArtist());
            sbPosition.setMax(currentSong.getLength());

            Album album = SongManager.getInstance(this).getAlbum(currentSong);
            if (album.getAlbumArtPath() != null) Picasso.with(this)
                    .load("file://" + album.getAlbumArtPath())
                    .error(R.drawable.nav_header_image)
                    .resize(imgNavHeader.getWidth(), imgNavHeader.getHeight())
                    .centerCrop()
                    .into(imgNavHeader);
            else Picasso.with(this)
                    .load(R.drawable.nav_header_image)
                    .resize(imgNavHeader.getWidth(), imgNavHeader.getHeight())
                    .centerCrop()
                    .into(imgNavHeader);
        } else {
            imgNavHeader.setImageResource(R.drawable.nav_header_image);
            navTxtTitle.setText("");
            navTxtSubtitle.setText("");
        }
    }

    public void onEventMainThread(AlbumSelectedEvent event) {
        this.mode = Mode.SONGS;
        navigationView.getMenu().findItem(R.id.action_songs).setChecked(true);
        setFragment(SongFragment.createInstance(event.getAlbum()));
    }

    public void onEventMainThread(ArtistSelectedEvent event) {
        this.mode = Mode.ALBUMS;
        navigationView.getMenu().findItem(R.id.action_albums).setChecked(true);
        setFragment(AlbumFragment.createInstance(event.getArtist()));
    }

    private void setFragment(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fragment.setEnterTransition(new Fade());
            fragment.setExitTransition(new Fade());
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void refreshPlayIcon() {
        if (SongPlayer.getInstance(this).isPlaying()) {
            btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
            navBtnPlay.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
        } else {
            btnPlay.setImageResource(R.drawable.ic_play_circle_outline_black_48dp);
            navBtnPlay.setImageResource(R.drawable.ic_play_circle_outline_black_48dp);
        }
    }

    private void setMode(Mode mode) {
        this.mode = mode;
        switch (mode) {
            case SONGS:
                navigationView.getMenu().findItem(R.id.action_songs).setChecked(true);
                setFragment(SongFragment.createInstance());
                break;
            case ALBUMS:
                navigationView.getMenu().findItem(R.id.action_albums).setChecked(true);
                setFragment(AlbumFragment.createInstance());
                break;
            case ARTISTS:
                navigationView.getMenu().findItem(R.id.action_artisis).setChecked(true);
                setFragment(ArtistFragment.createInstance());
                break;
        }
    }
}
