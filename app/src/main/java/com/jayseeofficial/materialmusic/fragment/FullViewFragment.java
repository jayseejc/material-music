package com.jayseeofficial.materialmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.SongManager;
import com.jayseeofficial.materialmusic.domain.Album;
import com.jayseeofficial.materialmusic.event.PlaybackEvent;
import com.jayseeofficial.materialmusic.view.SquareImageView;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by jon on 13/06/15.
 */
public class FullViewFragment extends Fragment {

    @InjectView(R.id.img_album_art)
    SquareImageView imgAlbumArt;

    @InjectView(R.id.tb_main_full)
    Toolbar tbMain;

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
        Album album = SongManager.getInstance().getAlbum(event.getSong());
        Picasso.with(getActivity())
                .load("file://" + album.getAlbumArtPath())
                .placeholder(R.drawable.ic_default_artwork)
                .into(imgAlbumArt);
    }

    public Toolbar getToolbar() {
        return tbMain;
    }
}
