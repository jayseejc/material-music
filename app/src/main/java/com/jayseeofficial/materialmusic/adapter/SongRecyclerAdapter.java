package com.jayseeofficial.materialmusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jayseeofficial.materialmusic.R;
import com.jayseeofficial.materialmusic.SongManager;
import com.jayseeofficial.materialmusic.event.LibraryLoadedEvent;

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
        viewHolder.txtTitle.setText(songManager.getSongs().get(i).getTitle());
        viewHolder.txtSubtitle.setText(songManager.getSongs().get(i).getArtist());
    }

    @Override
    public int getItemCount() {
        return songManager.getSongs().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView txtTitle;
        TextView txtSubtitle;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtSubtitle = (TextView) itemView.findViewById(R.id.txt_subtitle);
        }
    }

    public void onEventMainThread(LibraryLoadedEvent event) {
        notifyDataSetChanged();
        EventBus.getDefault().unregister(this);
    }

}
