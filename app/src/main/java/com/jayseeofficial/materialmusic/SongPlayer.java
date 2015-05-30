package com.jayseeofficial.materialmusic;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.PlaybackFinishedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackPausedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackResumedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackStartedEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class SongPlayer {

    private static MediaPlayer mediaPlayer;
    private static Song currentSong = null;

    public static void playSong(Context context, Song song) {
        stopSong();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.stop();
        Uri playbackUrl = SongManager.getInstance().getSongUri(song);
        mediaPlayer = MediaPlayer.create(context, playbackUrl);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
                    mediaPlayer = null;
                    currentSong = null;
                    EventBus.getDefault().post(new PlaybackFinishedEvent());
                }
        );
        currentSong = song;
        EventBus.getDefault().post(new PlaybackStartedEvent());
    }

    public static void pauseSong() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            EventBus.getDefault().post(new PlaybackPausedEvent());
        }
    }

    public static void resumeSong() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            EventBus.getDefault().post(new PlaybackResumedEvent());
        }
    }

    public static void stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            EventBus.getDefault().post(new PlaybackFinishedEvent());
        }
    }

    public static void toggleSong() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) pauseSong();
            else resumeSong();
        }
    }

    public static boolean isPlaying() {
        if (mediaPlayer != null) return mediaPlayer.isPlaying();
        return false;
    }

    public static Song getCurrentSong() {
        return currentSong;
    }

}
