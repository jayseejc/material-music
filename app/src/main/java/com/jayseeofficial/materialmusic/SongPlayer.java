package com.jayseeofficial.materialmusic;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.PlaybackFinishedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackPausedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackResumedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackStartedEvent;
import com.jayseeofficial.materialmusic.event.SongSelectedEvent;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class SongPlayer {

    private static SongPlayer songPlayer = null;

    public static SongPlayer getInstance() {
        if (songPlayer == null)
            throw new IllegalStateException("Must set context with getInstance(Context) first");
        return songPlayer;
    }

    public static SongPlayer getInstance(Context context) {
        if (songPlayer == null) songPlayer = new SongPlayer(context.getApplicationContext());
        return songPlayer;
    }

    private MediaPlayer mediaPlayer;
    private Song currentSong = null;
    private List<Song> playlist = null;

    private Context context;

    private SongPlayer(Context context) {
        this.context = context.getApplicationContext();
        EventBus.getDefault().register(this);
    }

    public void playSong(Context context, Song song) {
        stopSong();
        Uri playbackUrl = SongManager.getInstance().getSongUri(song);
        mediaPlayer = MediaPlayer.create(context, playbackUrl);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
                    mediaPlayer = null;
                    EventBus.getDefault().post(new PlaybackFinishedEvent());
                }
        );
        currentSong = song;
        EventBus.getDefault().post(new PlaybackStartedEvent());
    }

    public void pauseSong() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            EventBus.getDefault().post(new PlaybackPausedEvent());
        }
    }

    public void resumeSong() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            EventBus.getDefault().post(new PlaybackResumedEvent());
        }
    }

    public void stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            EventBus.getDefault().post(new PlaybackFinishedEvent());
        }
    }

    public void toggleSong() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) pauseSong();
            else resumeSong();
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer != null) return mediaPlayer.isPlaying();
        return false;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void onEvent(SongSelectedEvent event) {
        playlist = event.getSongList();
        currentSong = event.getSong();
        playSong(context, currentSong);
    }

    public void onEvent(PlaybackFinishedEvent event) {
        if (playlist.indexOf(currentSong) + 1 != playlist.size()) {
            int nextSong = playlist.indexOf(currentSong) + 1;
            currentSong = playlist.get(nextSong);
            playSong(context, currentSong);
        }
    }

}
