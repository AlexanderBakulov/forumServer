package net.thumbtack.forums.view;

public class PostRatingInfo {

    private int id;
    private float rating;
    private int rated;

    public PostRatingInfo() {
    }

    public PostRatingInfo(int id, float rating, int rated) {
        this.id = id;
        this.rating = rating;
        this.rated = rated;
    }

    public int getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }

    public int getRated() {
        return rated;
    }
}
