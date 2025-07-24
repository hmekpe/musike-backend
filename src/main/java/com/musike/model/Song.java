package com.musike.model;

public class Song {
    private Long id;
    private String title;
    private String artist;

    public Song(Long id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
} 