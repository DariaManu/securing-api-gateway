package com.example.musicservice.controller;

import com.example.musicservice.domain.Song;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {
    private final List<Song> songs = List.of(
            new Song(1, "Dynamite", "BTS", "BE"),
            new Song(2, "Butter", "BTS", "Permission To Dance"),
            new Song(3, "Permission To Dance", "BTS", "Permission To Dance")
    );

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable final Integer id) {
        return ResponseEntity.ok(songs.stream().filter(song -> song.getId().equals(id)).toList().get(0));
    }
}
