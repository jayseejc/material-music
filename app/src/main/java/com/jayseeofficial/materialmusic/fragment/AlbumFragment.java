package com.jayseeofficial.materialmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.adapter.AlbumRecyclerAdapter;
import com.jayseeofficial.materialmusic.domain.Artist;

/**
 * Created by jon on 30/05/15.
 */
public class AlbumFragment extends Fragment {

    private static final String ARG_ARTIST = "artist";

    private RecyclerView recyclerView;

    public static AlbumFragment createInstance() {
        AlbumFragment fragment = new AlbumFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static AlbumFragment createInstance(Artist artist) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_ARTIST, artist);
        fragment.setArguments(arguments);
        return fragment;
    }

    public AlbumFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().getSerializable(ARG_ARTIST) != null) {
            recyclerView.setAdapter(new AlbumRecyclerAdapter(getActivity(),
                    (Artist) getArguments().getSerializable(ARG_ARTIST)));
        } else {
            recyclerView.setAdapter(new AlbumRecyclerAdapter(getActivity()));
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                getActivity().getResources().getInteger(R.integer.max_columns)));
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
