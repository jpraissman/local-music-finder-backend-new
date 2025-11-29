ALTER TABLE band_genres
DROP
COLUMN genre;

ALTER TABLE band_genres
    ADD genre VARCHAR(255);