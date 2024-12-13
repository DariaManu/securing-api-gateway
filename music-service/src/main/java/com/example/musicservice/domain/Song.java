package com.example.musicservice.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class Song implements Serializable {
    private Integer id;
    private String title;
    private String artist;
    private String album;
}
