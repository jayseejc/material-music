package com.jayseeofficial.materialmusic.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.adapter.SongRecyclerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LibraryViewActivity extends AppCompatActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.nav_view)
    NavigationView navigationView;

    @InjectView(R.id.rv_song_list)
    RecyclerView rvSongList;

    @InjectView(R.id.tb_main)
    Toolbar tbMain;

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
}
