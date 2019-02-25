package app.mediczy_com.ChatBot;

/**
 * Created by l on 15-09-2018.
 */

public class Game {

    private String name;
    private int imageSource;

    public Game(int imageSource, String name) {
        this.name = name;
        this.imageSource = imageSource;
    }

    public String getName() {
        return name;
    }

    public int getImageSource() {
        return imageSource;
    }
}
