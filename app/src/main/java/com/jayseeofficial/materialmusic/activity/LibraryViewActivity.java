package com.jayseeofficial.materialmusic.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.SongManager;
import com.jayseeofficial.materialmusic.SongPlayer;
import com.jayseeofficial.materialmusic.adapter.AlbumRecyclerAdapter;
import com.jayseeofficial.materialmusic.adapter.ArtistRecyclerAdapter;
import com.jayseeofficial.materialmusic.adapter.SongRecyclerAdapter;
import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.AlbumSelectedEvent;
import com.jayseeofficial.materialmusic.event.ArtistSelectedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackEvent;
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

    @InjectView(R.id.rv_song_list)
    RecyclerView rvSongList;

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
            rvSongList.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(SAVED_POSITION));
        } else setMode(Mode.SONGS);

        refreshPlayIcon();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(SAVED_MODE, mode);
        savedInstanceState.putParcelable(SAVED_POSITION, rvSongList.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onEventMainThread(PlaybackEvent event) {
        refreshPlayIcon();
        Song currentSong = SongPlayer.getCurrentSong();
        if (currentSong != null) {
            Picasso.with(this)
                    .load("file://" + SongManager.getInstance(this).getAlbum(currentSong).getAlbumArtPath())
                    .error(R.drawable.nav_header_image)
                    .resize(imgNavHeader.getWidth(), imgNavHeader.getHeight())
                    .centerCrop()
                    .into(imgNavHeader);
        } else {
            imgNavHeader.setImageResource(R.drawable.nav_header_image);
        }
    }

    public void onEventMainThread(ArtistSelectedEvent event) {
        setMode(Mode.ALBUMS);
        rvSongList.setAdapter(new AlbumRecyclerAdapter(this, event.getArtist()));
    }

    public void onEventMainThread(AlbumSelectedEvent event) {
        setMode(Mode.SONGS);
        rvSongList.setAdapter(new SongRecyclerAdapter(this, event.getAlbum()));
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
                rvSongList.setAdapter(new SongRecyclerAdapter(this));
                rvSongList.setLayoutManager(new LinearLayoutManager(this));
                break;
            case ALBUMS:
                navigationView.getMenu().findItem(R.id.action_albums).setChecked(true);
                rvSongList.setAdapter(new AlbumRecyclerAdapter(this));
                rvSongList.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.max_columns)));
                break;
            case ARTISTS:
                navigationView.getMenu().findItem(R.id.action_artisis).setChecked(true);
                rvSongList.setAdapter(new ArtistRecyclerAdapter(this));
                rvSongList.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
    }

}
