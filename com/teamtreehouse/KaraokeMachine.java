package com.teamtreehouse;

import com.teamtreehouse.model.Song;
import com.teamtreehouse.model.SongBook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class KaraokeMachine {
    private SongBook mSongBook;
    private BufferedReader mReader;
    private Map<String, String> mMenu;
    private Queue<Song> mSongQueue;

    public KaraokeMachine(SongBook songBook) {
        mSongBook = songBook;
        mReader = new BufferedReader(new InputStreamReader(System.in));
        mMenu = new HashMap<String, String>();
        mSongQueue = new ArrayDeque<Song>();
        mMenu.put("add", "Add a new song to the song book");
        mMenu.put("play", "Play next song in the queue");
        mMenu.put("choose", "choose a song to sing");
        mMenu.put("quit", "Give up. Exit the program");
    }

    private String promtAction() throws IOException {
        System.out.printf("There are %d available and there are %d songs in the queue. Your options are: %n",
                mSongBook.getSongCount(),
                mSongQueue.size());

        for (Map.Entry<String, String> option : mMenu.entrySet()) {

            System.out.printf("%s - %s %n",
                    option.getKey(),
                    option.getValue());

        }

        System.out.print("What do you want to do? ");
        String choice = mReader.readLine();

        return choice.trim().toLowerCase();

    }

    public void run() {

        String choice = "";

        do {
            try {
                choice = promtAction();
                switch (choice) {
                    case "add":
                        Song song = promtSong();
                        mSongBook.addSong(song);
                        System.out.printf("%s was added %n", song);
                        break;
                    case "choose":
                        String artist = promptArtist();
                        Song artistSong = promtSongForArtist(artist);
                        mSongQueue.add(artistSong);
                        break;
                    case "play":
                        playNext();
                        break;
                    case "quit":
                        System.out.println("Thank you for playing");
                        break;
                    default:
                        System.out.printf("Unkown choice: '%s'. Try again",
                                choice);
                }
            } catch (IOException ioe) {
                System.out.println("Problem with input");
                ioe.printStackTrace();
            }

        } while (!choice.equals("quit"));
    }


    private Song promtSong() throws IOException {
        System.out.println("Enter the artist's name");
        String artist = mReader.readLine();
        System.out.println("Enter the name of the song");
        String song = mReader.readLine();
        System.out.println("Enter the video url");
        String videoUrl = mReader.readLine();
        return new Song(artist, song, videoUrl);
    }

/*    private int promptForIndex(List<String> options) {
        int choice = 0;
        boolean success = false;

        //loop through message until it is a success
        while (success == false) {
            int counter = 0; //<--------started at 0 to account for moving of counter
            for (String option : options) {
                counter++; //<---------------moved in front of display
                System.out.printf("%d.)  %s %n", counter, option);
            }
            System.out.print("Your choice:   ");

            // try catch to see if answer is an int. If it is then it goes through if-statement to confirm it is within range
            try {
                String optionAsString = mReader.readLine();
                choice = Integer.parseInt(optionAsString.trim());

                // if choice is larger than selesction then spit out error message
                if (choice > counter || choice < 0) {
                    System.out.println("Invalid selection. Try again.");
                } else {
                    success = true;
                }
            } catch (Exception k) {
                System.out.println("Invalid selection. Try again.");
            }
        }
        return choice - 1;
    }*/

    private int promtForIndex(List<String> options) throws IOException {
        int counter = 1;
        for (String option : options) {
            System.out.printf("%d.) %s %n", counter, option);
            counter++;
        }
        System.out.print("Your choice ");
        String optionAsString = mReader.readLine();
        int choice = Integer.parseInt(optionAsString.trim());

        return choice - 1;
    }

    private String promptArtist() throws IOException {
        System.out.println("Available artist");
        List<String> artists = new ArrayList<>(mSongBook.getArtist());
        int index = promtForIndex(artists);

        return artists.get(index);
    }

    private Song promtSongForArtist(String artist) throws IOException {
        List<Song> songs = mSongBook.getSongForArtist(artist);
        List<String> songTitles = new ArrayList<>();

        for (Song song : songs) {
            songTitles.add(song.getTitle());
        }
        System.out.printf("Available songs for %s: %n", artist);
        int index = promtForIndex(songTitles);
        return songs.get(index);
    }

    public void playNext() {
        Song song = mSongQueue.poll();

        if (song == null) {
            System.out.println("Sorry, there are no songs availalbe. " +
                    "Use choose from the menu to add more");
        } else {
            System.out.printf("%n%n%n Open %s to hear %s by %s %n%n%n",
                    song.getVideoUrl(),
                    song.getTitle(),
                    song.getArtist());
        }
    }
}

