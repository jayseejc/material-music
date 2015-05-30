package com.jayseeofficial.materialmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.SongManager;
import com.jayseeofficial.materialmusic.SongPlayer;
import com.jayseeofficial.materialmusic.domain.Song;
import com.jayseeofficial.materialmusic.event.LibraryLoadedEvent;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class SongRecyclerAdapter extends RecyclerView.Adapter<SongRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private SongManager songManager;

    public SongRecyclerAdapter(Context context) {
        this.context = context.getApplicationContext();
        this.inflater = LayoutInflater.from(context);
        songManager = SongManager.getInstance(context);
        if (!songManager.isLoaded()) EventBus.getDefault().register(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_basic_info, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Song song = songManager.getSongs().get(i);
        viewHolder.itemView.setClickable(true);
        viewHolder.txtTitle.setText(song.getTitle());
        viewHolder.txtSubtitle.setText(song.getArtist());
        viewHolder.itemView.setOnClickListener(v -> {
            SongPlayer.playSong(context, song);
        });
        Picasso.with(context)
                .load("file://" + songManager.getAlbum(song).getAlbumArtPath())
                .placeholder(R.drawable.ic_default_artwork)
                .into(viewHolder.imgAlbumArt, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(getClass().getSimpleName(), "Successfully loaded " + songManager.getAlbum(song).getAlbumArtPath());
                    }
                    @Override
                    public void onError() {
                        Log.d(getClass().getSimpleName(), "Error loading " + songManager.getAlbum(song).getAlbumArtPath());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return songManager.getSongs().size();
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
        notifyDataSetChanged();
        EventBus.getDefault().unregister(this);
    }

}
