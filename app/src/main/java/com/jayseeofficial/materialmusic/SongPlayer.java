package com.jayseeofficial.materialmusic;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.AudioFocusChangedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackFinishedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackPausedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackResumedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackStartedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackToggleEvent;
import com.jayseeofficial.materialmusic.event.PlaylistUpdatedEvent;
import com.jayseeofficial.materialmusic.event.SeekEvent;
import com.jayseeofficial.materialmusic.event.ShuffleEvent;
import com.jayseeofficial.materialmusic.event.SkipEvent;
import com.jayseeofficial.materialmusic.event.SongSelectedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class SongPlayer {

    private static SongPlayer songPlayer = null;

    public static void init(Context context) {
        songPlayer = new SongPlayer(context.getApplicationContext());
    }

    public static SongPlayer getInstance() {
        if (songPlayer == null)
            throw new IllegalStateException("SongPlayer not initialized!");
        return songPlayer;
    }

    public static SongPlayer getInstance(Context context) {
        if (songPlayer == null) init(context);
        return songPlayer;
    }

    private MediaPlayer mediaPlayer;
    private Song currentSong = null;
    private List<Song> playlist = null;
    private List<Song> originalPlaylist = null;
    private boolean shuffleMode = false;
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    private Context context;

    private SongPlayer(Context context) {
        this.context = context.getApplicationContext();
        EventBus.getDefault().register(this);
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        onAudioFocusChangeListener =
                focusChange -> EventBus.getDefault().post(new AudioFocusChangedEvent(focusChange));

        int result = manager.requestAudioFocus(
                onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            throw new RuntimeException("Could not obtain audio focus!");
        }
    }

    public void playSong(Context context, Song song) {
        stopSong();
        Uri playbackUrl = SongManager.getInstance().getSongUri(song);
        mediaPlayer = MediaPlayer.create(context, playbackUrl);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
                    mediaPlayer = null;
                    EventBus.getDefault().post(new PlaybackFinishedEvent(PlaybackFinishedEvent.Reason.END_OF_TRACK));
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
            EventBus.getDefault().post(new PlaybackFinishedEvent(PlaybackFinishedEvent.Reason.UNKNOWN));
        }
    }

    public void skipNext() {
        int nextSong = playlist.indexOf(currentSong) + 1;
        if (nextSong != playlist.size()) {
            currentSong = playlist.get(nextSong);
            playSong(context, currentSong);
        } else {
            stopSong();
        }
    }

    public void skipPrevious() {
        int nextSong = playlist.indexOf(currentSong) - 1;
        if (!(nextSong < 0)) {
            currentSong = playlist.get(nextSong);
            playSong(context, currentSong);
        } else stopSong();
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

    public List<Song> getCurrentPlaylist() {
        return playlist;
    }

    public void setShuffle(boolean shuffleOn) {
        this.shuffleMode = shuffleOn;
        if (playlist != null) {
            if (shuffleOn) {
                ArrayList<Song> shuffledList = new ArrayList<>(playlist.size());
                Random random = new Random();
                shuffledList.add(playlist.remove(playlist.indexOf(currentSong)));
                while (!playlist.isEmpty()) {
                    shuffledList.add(playlist.remove(random.nextInt(playlist.size())));
                }
                playlist = shuffledList;
            } else playlist = new ArrayList<>(originalPlaylist);
        }
        EventBus.getDefault().post(new PlaylistUpdatedEvent());
    }

    public void onEventBackgroundThread(SongSelectedEvent event) {
        playlist = new ArrayList<>(event.getSongList());
        originalPlaylist = new ArrayList<>(event.getSongList());
        currentSong = event.getSong();
        playSong(context, currentSong);
        setShuffle(shuffleMode);
    }

    public void onEvent(PlaybackFinishedEvent event) {
        // The +1s are to offset base 0 vs base 1
        if (event.getReason() == PlaybackFinishedEvent.Reason.END_OF_TRACK) {
            if (playlist.indexOf(currentSong) + 1 != playlist.size()) {
                skipNext();
            }
        }
    }

    public void onEvent(SeekEvent event) {
        mediaPlayer.seekTo(event.getPosition());
    }

    public void onEvent(SkipEvent event) {
        if (event.getDirection() == SkipEvent.Direction.NEXT) skipNext();
        else if (event.getDirection() == SkipEvent.Direction.PREVIOUS) skipPrevious();
    }

    public void onEvent(PlaybackToggleEvent event) {
        toggleSong();
    }

    public void onEvent(ShuffleEvent event) {
        setShuffle(event.isShuffleOn());
    }

    public void onEvent(AudioFocusChangedEvent event) {
        if (event.getChangeType() == AudioFocusChangedEvent.FocusEvent.LOSS) {
            stopSong();
            AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            manager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    public int getCurrentSongLength() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

}
