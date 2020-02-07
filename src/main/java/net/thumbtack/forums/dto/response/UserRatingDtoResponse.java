package net.thumbtack.forums.dto.response;

public class UserRatingDtoResponse {

    private String name;
    private float rating;
    private int rated;

    public UserRatingDtoResponse() {
    }

    public UserRatingDtoResponse(String name, float rating, int rated) {
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
