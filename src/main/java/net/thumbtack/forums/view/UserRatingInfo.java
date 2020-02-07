package net.thumbtack.forums.view;

public class UserRatingInfo {

    private String name;
    private float rating;
    private int rated;

    public UserRatingInfo() {
    }

    public UserRatingInfo(String name, float rating, int rated) {
        this.name = name;
        this.rating = rating;
        this.rated = rated;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public int getRated() {
        return rated;
    }
}
