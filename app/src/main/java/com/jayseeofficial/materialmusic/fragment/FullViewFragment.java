package com.jayseeofficial.materialmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.SongManager;
import com.jayseeofficial.materialmusic.domain.Album;
import com.jayseeofficial.materialmusic.event.PlaybackEvent;
import com.jayseeofficial.materialmusic.event.PlaybackFinishedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackPausedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackResumedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackStartedEvent;
import com.jayseeofficial.materialmusic.event.PlaybackToggleEvent;
import com.jayseeofficial.materialmusic.event.SkipEvent;
import com.jayseeofficial.materialmusic.view.SquareImageView;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by jon on 13/06/15.
 */
public class FullViewFragment extends Fragment {

    private boolean pauseUpdate = false;

    @InjectView(R.id.img_album_art)
    SquareImageView imgAlbumArt;

    @InjectView(R.id.tb_main_full)
    Toolbar tbMain;

    @InjectView(R.id.btn_next)
    ImageButton btnNext;

    @InjectView(R.id.btn_play)
    ImageButton btnPlay;

    @InjectView(R.id.btn_prev)
    ImageButton btnPrev;

    @InjectView(R.id.sb_position)
    SeekBar sbPosition;

    @InjectView(R.id.txt_title)
    TextView txtTitle;

    @InjectView(R.id.txt_subtitle)
    TextView txtSubtitle;

    @OnClick(R.id.btn_next)
    public void next() {
        EventBus.getDefault().post(new SkipEvent(SkipEvent.Direction.NEXT));
    }

    @OnClick(R.id.btn_play)
    public void toggle() {
        EventBus.getDefault().post(new PlaybackToggleEvent());
    }

    @OnClick(R.id.btn_prev)
    public void nrevious() {
        EventBus.getDefault().post(new SkipEvent(SkipEvent.Direction.PREVIOUS));
    }

    public FullViewFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_view, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    public void onEventMainThread(PlaybackEvent event) {
        sbPosition.setMax(event.getSong().getLength());
        Album album = SongManager.getInstance().getAlbum(event.getSong());

        txtTitle.setText(event.getSong().getTitle());
        txtSubtitle.setText(event.getSong().getArtist() + " - " + album.getTitle());
        Picasso.with(getActivity())
                .load("file://" + album.getAlbumArtPath())
                .placeholder(R.drawable.ic_default_artwork)
                .into(imgAlbumArt);
        if (event.getEventType().equals(PlaybackStartedEvent.EVENT_TYPE) ||
                event.getEventType().equals(PlaybackResumedEvent.EVENT_TYPE)) {
            Picasso.with(getActivity())
                    .load(R.drawable.ic_pause_circle_outline_black_48dp)
                    .into(btnPlay);
        } else if (event.getEventType().equals(PlaybackFinishedEvent.EVENT_TYPE) ||
                event.getEventType().equals(PlaybackPausedEvent.EVENT_TYPE)) {
            Picasso.with(getActivity())
                    .load(R.drawable.ic_play_circle_outline_black_48dp)
                    .into(btnPlay);
        }
    }

    public Toolbar getToolbar() {
        return tbMain;
    }
}
