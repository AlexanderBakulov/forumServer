package net.thumbtack.forums.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ErrorsDtoResponse {

    private List<Error> errors;

    public ErrorsDtoResponse() {
        this.errors = new ArrayList<>();
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public void addError(Error error) {
        errors.add(error);
    }


}
