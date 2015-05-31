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

import com.jayseeofficial.materialmusic.adapter.ArtistRecyclerAdapter;

/**
 * Created by jon on 30/05/15.
 */
public class ArtistFragment extends Fragment {

    private static final String ARG_ARTIST = "artist";

    private RecyclerView recyclerView;

    public static ArtistFragment createInstance() {
        ArtistFragment fragment = new ArtistFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    public ArtistFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(new ArtistRecyclerAdapter(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
