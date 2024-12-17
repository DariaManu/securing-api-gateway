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
            new Song(1, "title1", "artist1", "album1"),
            new Song(2, "title2", "artist2", "album2"),
            new Song(3, "title3", "artist3", "album3")
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
