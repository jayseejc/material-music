package com.jayseeofficial.materialmusic;

import com.jayseeofficial.materialmusic.domain.Playlist;

import java.util.List;

/**
 * Created by jon on 06/06/15.
 */
public interface PlaylistManager {
    List<Playlist> loadPlaylists();

    boolean savePlaylists(List<Playlist> playlists);
}
