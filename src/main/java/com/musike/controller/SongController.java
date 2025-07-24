package com.musike.controller;

import com.musike.model.Song;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class SongController {
    @GetMapping("/songs")
    public List<Song> getSongs() {
        return Arrays.asList(
            new Song(1L, "Canopy", "Kuami Eugene"),
            new Song(2L, "All My Love", "Amaarae"),
            new Song(3L, "2002", "Anne-Marie")
        );
    }
} 