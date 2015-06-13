package com.jayseeofficial.materialmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.adapter.PlaylistRecyclerAdapter;
import com.jayseeofficial.materialmusic.event.NewPlaylistRequestedEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 06/06/15.
 */
public class PlaylistFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    public static PlaylistFragment createInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    public PlaylistFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(new PlaylistRecyclerAdapter(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().setTitle("Playlists");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_main);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_add_playlist);
        floatingActionButton.setOnClickListener(v -> EventBus.getDefault().post(new NewPlaylistRequestedEvent()));

        return view;
    }
}
