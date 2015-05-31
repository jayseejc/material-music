package com.jayseeofficial.materialmusic.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.SongManager;
import com.jayseeofficial.materialmusic.SongPlayer;
import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.AlbumSelectedEvent;
import com.jayseeofficial.materialmusic.event.ArtistSelectedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackEvent;
import com.jayseeofficial.materialmusic.fragment.AlbumFragment;
import com.jayseeofficial.materialmusic.fragment.ArtistFragment;
import com.jayseeofficial.materialmusic.fragment.SongFragment;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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

    @OnClick(R.id.btn_next)
    public void nextTrack() {
    }

    @OnClick(R.id.btn_prev)
    public void previousTrack() {
    }

    @InjectView(R.id.btn_play)
    ImageButton btnPlay;
    ImageButton navBtnPlay;
    ImageButton navBtnPrev;
    ImageButton navBtnNext;
    TextView navTxtTitle;
    TextView navTxtSubtitle;

    @OnClick(R.id.btn_play)
    public void toggleTrack() {
        SongPlayer.toggleSong();
    }

    private Mode mode;

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

        setSupportActionBar(tbMain);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.action_songs) {
                setMode(Mode.SONGS);
            } else if (id == R.id.action_albums) {
                setMode(Mode.ALBUMS);
            } else if (id == R.id.action_artisis) {
                setMode(Mode.ARTISTS);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        if (savedInstanceState != null) {
            setMode((Mode) savedInstanceState.getSerializable(SAVED_MODE));
        } else setMode(Mode.SONGS);

        refreshPlayIcon();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(SAVED_MODE, mode);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onEventMainThread(PlaybackEvent event) {
        refreshPlayIcon();
        Song currentSong = SongPlayer.getCurrentSong();
        if (currentSong != null) {
            navTxtTitle.setText(currentSong.getTitle());
            navTxtSubtitle.setText(currentSong.getArtist());
            Picasso.with(this)
                    .load("file://" + SongManager.getInstance(this).getAlbum(currentSong).getAlbumArtPath())
                    .error(R.drawable.nav_header_image)
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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void refreshPlayIcon() {
        if (SongPlayer.isPlaying()) {
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
