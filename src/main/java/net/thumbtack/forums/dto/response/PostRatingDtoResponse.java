package net.thumbtack.forums.dto.response;

public class PostRatingDtoResponse {

    private int id;
    private float rating;
    private int rated;

    public PostRatingDtoResponse() {
    }

    public PostRatingDtoResponse(int id, float rating, int rated) {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setRated(int rated) {
        this.rated = rated;
    }
}
