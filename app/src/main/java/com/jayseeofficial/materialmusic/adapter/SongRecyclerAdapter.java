package com.jayseeofficial.materialmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.SongManager;
import com.jayseeofficial.materialmusic.SongPlayer;
import com.jayseeofficial.materialmusic.domain.Album;
import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.LibraryLoadedEvent;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class SongRecyclerAdapter extends RecyclerView.Adapter<SongRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private SongManager songManager;
    private List<Song> songs;

    public SongRecyclerAdapter(Context context) {
        this.context = context.getApplicationContext();
        this.inflater = LayoutInflater.from(context);
        songManager = SongManager.getInstance(context);
        if (!songManager.isLoaded()) EventBus.getDefault().register(this);
        else dataSetChanged();
    }

    public SongRecyclerAdapter(Context context, Album album) {
        this.context = context.getApplicationContext();
        this.inflater = LayoutInflater.from(context);
        songs = album.getSongs();
        Collections.sort(songs, (lhs, rhs) -> lhs.getTrackNumber() - rhs.getTrackNumber());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_basic_info, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Song song = songs.get(i);
        viewHolder.itemView.setClickable(true);
        viewHolder.txtTitle.setText(song.getTitle());
        viewHolder.txtSubtitle.setText(song.getArtist());
        viewHolder.itemView.setOnClickListener(v -> {
            SongPlayer.playSong(context, song);
        });
        Picasso.with(context)
                .load("file://" + SongManager.getInstance(context).getAlbum(song).getAlbumArtPath())
                .placeholder(R.drawable.ic_default_artwork)
                .into(viewHolder.imgAlbumArt);
    }

    @Override
    public int getItemCount() {
        if (songs == null) return 0;
        return songs.size();
    }

    private void dataSetChanged() {
        if (songManager != null) songs = songManager.getSongs();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView txtTitle;
        TextView txtSubtitle;
        ImageView imgAlbumArt;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtSubtitle = (TextView) itemView.findViewById(R.id.txt_subtitle);
            imgAlbumArt = (ImageView) itemView.findViewById(R.id.img_album_art);
        }
    }

    public void onEventMainThread(LibraryLoadedEvent event) {
        dataSetChanged();
        EventBus.getDefault().unregister(this);
    }

}
