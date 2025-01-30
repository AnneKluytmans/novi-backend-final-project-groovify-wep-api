package com.groovify.vinylshopapi.enums;

import java.util.Arrays;

public enum Genre {
    ROCK,
    POP,
    JAZZ,
    BLUES,
    CLASSICAL,
    HIP_HOP,
    RAP,
    ELECTRONIC,
    METAL,
    REGGAE,
    COUNTRY,
    FOLK,
    FUNK,
    SOUL,
    RNB,
    LATIN,
    PUNK,
    DISCO,
    HOUSE,
    TECHNO,
    OTHER;

    public static Genre stringToGenre(String genre) {
        try {
            return Genre.valueOf(genre.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid genre: " + genre + "'. Please use one of the valid genres: " + Arrays.toString(Genre.values()) + " or 'OTHER'.");
        }
    }
}
