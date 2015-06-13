package com.jayseeofficial.materialmusic.activity;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;

import com.jayseeofficial.materialmusic.BuildConfig;
import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.SongManager;
import com.jayseeofficial.materialmusic.domain.Playlist;
import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.NewPlaylistRequestedEvent;
import com.jayseeofficial.materialmusic.event.PlaylistCreatedEvent;
import com.jayseeofficial.materialmusic.event.SongAddedToPlaylistEvent;
import com.jayseeofficial.materialmusic.event.SongLongClickedEvent;

import java.util.List;

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

    private void showAddSongToPlaylistDialog(Song song) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        NavigationView navigationView = new NavigationView(this);
        List<Playlist> playlists = SongManager.getInstance(this).getPlaylists();
        for (Playlist playlist : playlists) {
            navigationView.getMenu().add(1, playlist.getId(), Menu.NONE, playlist.getTitle());
        }
        builder.setTitle("Add to playlist...");
        builder.setView(navigationView);
        AlertDialog dialog = builder.create();
        dialog.show();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            for (Playlist playlist : playlists) {
                if (playlist.getId() == id) {
                    EventBus.getDefault().post(new SongAddedToPlaylistEvent(song, playlist));
                    dialog.dismiss();
                    break;
                }
            }
            return false;
        });
    }

    public void onEvent(Object object) {
        if (BuildConfig.DEBUG) {
            Snackbar.make(contentView, "Event: " + object.getClass().getSimpleName(), Snackbar.LENGTH_SHORT).show();
        }
    }

    public void onEventMainThread(NewPlaylistRequestedEvent event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_playlist_add_white_24dp);
        builder.setTitle("New Playlist");

        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editText);

        builder.setPositiveButton("OK", (dialog, which) -> {
            Playlist playlist = new Playlist();
            playlist.setTitle(editText.getText().toString());
            EventBus.getDefault().post(new PlaylistCreatedEvent(playlist));
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.create().show();
    }

    public void onEventMainThread(SongLongClickedEvent event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(event.getSong().getTitle());
        NavigationView navigationView = new NavigationView(this);
        new MenuInflater(this).inflate(R.menu.long_click, navigationView.getMenu());

        builder.setView(navigationView);
        AlertDialog dialog = builder.create();
        dialog.show();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.action_add_to_playlist:
                    showAddSongToPlaylistDialog(event.getSong());
                    dialog.dismiss();
                    return true;
            }
            return false;
        });
    }

}
