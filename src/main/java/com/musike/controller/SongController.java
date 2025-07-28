package com.musike.controller;

import com.musike.model.Song;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    @GetMapping
    public List<Song> getAllSongs() {
        // Placeholder - return empty list for now
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public Song getSongById(@PathVariable Long id) {
        // Placeholder - return null for now
        return null;
    }

    @GetMapping("/search")
    public List<Song> searchSongs(@RequestParam String q) {
        // Placeholder - return empty list for now
        return new ArrayList<>();
    }

    @GetMapping("/liked")
    public List<Song> getLikedSongs() {
        // Placeholder - return empty list for now
        return new ArrayList<>();
    }

    @PostMapping("/{id}/like")
    public Map<String, String> toggleLike(@PathVariable Long id) {
        // Placeholder - return success message
        return Map.of("message", "Like toggled successfully");
    }
} 