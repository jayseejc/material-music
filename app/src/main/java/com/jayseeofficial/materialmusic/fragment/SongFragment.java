package com.jayseeofficial.materialmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jayseeofficial.materialmusic.adapter.SongRecyclerAdapter;
import com.jayseeofficial.materialmusic.domain.Album;

/**
 * Created by jon on 30/05/15.
 */
public class SongFragment extends Fragment {

    private static final String ARG_ALBUM = "album";

    private RecyclerView recyclerView;

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
        if (getArguments().getSerializable(ARG_ALBUM) != null) {
            recyclerView.setAdapter(new SongRecyclerAdapter(getActivity(),
                    (Album) getArguments().getSerializable(ARG_ALBUM)));
        } else {
            recyclerView.setAdapter(new SongRecyclerAdapter(getActivity()));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(params);

        return recyclerView;
    }
}
