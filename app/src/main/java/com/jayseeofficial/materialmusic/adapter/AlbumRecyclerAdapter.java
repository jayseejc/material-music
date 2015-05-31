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
import com.jayseeofficial.materialmusic.domain.Album;
import com.jayseeofficial.materialmusic.domain.Artist;
import com.jayseeofficial.materialmusic.event.AlbumSelectedEvent;
import com.jayseeofficial.materialmusic.event.LibraryLoadedEvent;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class AlbumRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private SongManager songManager;
    private List<Album> albums;

    public AlbumRecyclerAdapter(Context context) {
        this.context = context.getApplicationContext();
        this.inflater = LayoutInflater.from(context);
        songManager = SongManager.getInstance(context);
        if (!songManager.isLoaded()) EventBus.getDefault().register(this);
        else dataSetChanged();
    }

    public AlbumRecyclerAdapter(Context context, Artist artist) {
        this.context = context.getApplicationContext();
        this.inflater = LayoutInflater.from(context);
        songManager = SongManager.getInstance(context);
        albums = artist.getAlbums();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_big_info, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Album album = albums.get(i);
        viewHolder.itemView.setClickable(true);
        viewHolder.txtTitle.setText(album.getTitle());
        viewHolder.txtSubtitle.setText(album.getArtist());
        viewHolder.itemView.setOnClickListener(v -> {
            EventBus.getDefault().post(new AlbumSelectedEvent(album));
        });
        Picasso.with(context)
                .load("file://" + album.getAlbumArtPath())
                .placeholder(R.drawable.ic_default_artwork)
                .into(viewHolder.imgAlbumArt);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    private void dataSetChanged() {
        albums = songManager.getAlbums();
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
        dataSetChanged();
        EventBus.getDefault().unregister(this);
    }

}
