package com.jayseeofficial.materialmusic.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.SongPlayer;
import com.jayseeofficial.materialmusic.adapter.SongRecyclerAdapter;
import com.jayseeofficial.materialmusic.event.PlaybackEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LibraryViewActivity extends BaseActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.nav_view)
    NavigationView navigationView;

    @InjectView(R.id.rv_song_list)
    RecyclerView rvSongList;

    @InjectView(R.id.tb_main)
    Toolbar tbMain;

    @OnClick(R.id.btn_next)
    public void nextTrack(){
    }

    @OnClick(R.id.btn_prev)
    public void previousTrack(){
    }

    @InjectView(R.id.btn_play)
    ImageButton btnPlay;

    @OnClick(R.id.btn_play)
    public void toggleTrack(){
        SongPlayer.toggleSong();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_view);

        ButterKnife.inject(this);

        setSupportActionBar(tbMain);

        navigationView.getMenu().findItem(R.id.action_songs).setChecked(true);
        drawerLayout.openDrawer(GravityCompat.START);

        rvSongList.setLayoutManager(new LinearLayoutManager(this));
        rvSongList.setAdapter(new SongRecyclerAdapter(this));

        refreshPlayIcon();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_library_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(PlaybackEvent event){
        refreshPlayIcon();
    }

    private void refreshPlayIcon(){
        if(SongPlayer.isPlaying()){
            btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
        } else {
            btnPlay.setImageResource(R.drawable.ic_play_circle_outline_black_48dp);
        }
    }

}
