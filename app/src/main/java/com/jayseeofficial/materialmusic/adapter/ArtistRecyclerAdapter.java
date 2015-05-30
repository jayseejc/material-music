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
import com.jayseeofficial.materialmusic.domain.Artist;
import com.jayseeofficial.materialmusic.event.LibraryLoadedEvent;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 30/05/15.
 */
public class ArtistRecyclerAdapter extends RecyclerView.Adapter<ArtistRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private SongManager songManager;
    private List<Artist> artists;

    public ArtistRecyclerAdapter(Context context) {
        this.context = context.getApplicationContext();
        this.inflater = LayoutInflater.from(context);
        songManager = SongManager.getInstance(context);
        if (!songManager.isLoaded()) EventBus.getDefault().register(this);
        dataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_basic_info, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Artist artist = artists.get(i);
        viewHolder.itemView.setClickable(true);
        viewHolder.txtTitle.setText(artist.getName());
        viewHolder.itemView.setOnClickListener(v -> {
            // TODO proceed to listing about artist
        });
        Picasso.with(context)
                .load(R.drawable.ic_default_artwork)
                .into(viewHolder.imgAlbumArt);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    private void dataSetChanged() {
        artists = SongManager.getInstance(context).getArtists();
        notifyDataSetChanged();
    }

    public void onEventMainThread(LibraryLoadedEvent event) {
        dataSetChanged();
        EventBus.getDefault().unregister(this);
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
}
