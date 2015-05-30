package com.jayseeofficial.materialmusic;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.jayseeofficial.materialmusic.domain.Song;

/**
 * Created by jon on 30/05/15.
 */
public class SongPlayer {

    private static MediaPlayer mediaPlayer;

    public static void playSong(Context context, Song song) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.stop();
        Uri playbackUrl = SongManager.getInstance().getSongUri(song);
        mediaPlayer = MediaPlayer.create(context, playbackUrl);
        mediaPlayer.start();
    }
}
