package com.teamtreehouse.model;

import java.io.*;
import java.util.*;

public class SongBook {
    private List<Song> mSongs;

    public SongBook() {
        mSongs = new ArrayList<Song>();
    }

    public void exportTo(String filename) {
        try (
                FileOutputStream fos = new FileOutputStream(filename);
                PrintWriter writer = new PrintWriter(fos);
        ) {
            for (Song song : mSongs) {
                writer.printf("%s|%s|%s%n",
                        song.getArtist(),
                        song.getTitle(),
                        song.getVideoUrl()
                );
            }

        } catch (IOException ioe) {
            System.out.printf("Problem saving %s %n", filename);
            ioe.printStackTrace();
        }
    }

    public void importFrom(String filename) {
        try (
                FileInputStream fis = new FileInputStream(filename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split("\\|");
                addSong(new Song(args[0], args[1], args[2]));

            }

        } catch (IOException ioe) {
            System.out.println("Problems loadign file");
            ioe.printStackTrace();
        }
    }

    public void addSong(Song song) {
        mSongs.add(song);
    }

    public int getSongCount() {
        return mSongs.size();
    }

    private Map<String, List<Song>> byArtist() {
        Map<String, List<Song>> byArtist = new TreeMap<>();

        for (Song song : mSongs) {
            List<Song> artistSong = byArtist.get(song.getArtist());
            if (artistSong == null) {
                artistSong = new ArrayList<>();
                byArtist.put(song.getArtist(), artistSong);
            }
            artistSong.add(song);
        }
        return byArtist;
    }

    public Set<String> getArtist() {
        return byArtist().keySet();
    }

    public List<Song> getSongForArtist(String artistName) {

        List<Song> songs = byArtist().get(artistName);

        songs.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                if (song1.equals(song2)) {
                    return 0;
                }

                return song1.mTitle.compareTo(song2.mTitle);
            }

        });

        return songs;
    }
}