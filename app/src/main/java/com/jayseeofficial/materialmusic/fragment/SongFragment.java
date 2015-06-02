package com.jayseeofficial.materialmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.adapter.SongRecyclerAdapter;
import com.jayseeofficial.materialmusic.domain.Album;
import com.squareup.picasso.Picasso;

/**
 * Created by jon on 30/05/15.
 */
public class SongFragment extends Fragment {

    private static final String ARG_ALBUM = "album";

    private RecyclerView recyclerView;
    private ImageView imageView = null;

    public static SongFragment createInstance() {
        SongFragment fragment = new SongFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static SongFragment createInstance(Album album) {
        SongFragment fragment = new SongFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_ALBUM, album);
        fragment.setArguments(arguments);
        return fragment;
    }

    public SongFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String title;
        if (getArguments().getSerializable(ARG_ALBUM) != null) {
            title = ((Album) getArguments().getSerializable(ARG_ALBUM)).getTitle();
        } else {
            title = "Songs";
        }
        getActivity().setTitle(title);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean albumMode = getArguments().getSerializable(ARG_ALBUM) != null;
        View view;
        if (albumMode) {
            view = inflater.inflate(R.layout.fragment_song_album_mode, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_song, container, false);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_songs);

        if (albumMode) {
            imageView = (ImageView) view.findViewById(R.id.img_album_art);
            imageView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                String path = ((Album) getArguments().getSerializable(ARG_ALBUM)).getAlbumArtPath();
                if (path != null) {
                    Picasso.with(getActivity())
                            .load("file://" + ((Album) getArguments().getSerializable(ARG_ALBUM)).getAlbumArtPath())
                            .error(R.drawable.ic_default_artwork)
                            .into(imageView);
                } else {
                    Picasso.with(getActivity())
                            .load(R.drawable.ic_default_artwork)
                            .into(imageView);
                }
            });
            recyclerView.setAdapter(new SongRecyclerAdapter(getActivity(), (Album) getArguments().getSerializable(ARG_ALBUM)));
        } else {
            recyclerView.setAdapter(new SongRecyclerAdapter(getActivity()));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}
