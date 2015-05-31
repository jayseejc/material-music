package com.jayseeofficial.materialmusic.domain;

import android.os.Build;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jon on 30/05/15.
 */
public class Artist {
    private String name;
    private String key;
    private Map<String, Album> albums;

    public Artist() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            albums = new ArrayMap<>();
        } else {
            albums = new HashMap<>();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void addAlbum(Album album) {
        albums.put(album.getKey(), album);
    }

    public boolean removeAlbum(Album album) {
        return albums.remove(album.getKey()) != null;
    }

    public List<Album> getAlbums() {
        return new ArrayList<>(albums.values());
    }

}
