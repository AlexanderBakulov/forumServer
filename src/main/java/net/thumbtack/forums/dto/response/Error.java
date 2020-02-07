package net.thumbtack.forums.dto.response;

public class Error {

    private String errorCode;
    private String field;
    private String errorMessage;

    public Error(String errorCode, String field, String errorMessage) {
        this.errorCode = errorCode;
        this.field = field;
        this.errorMessage = errorMessage;
    }

    public Error(String errorCode, String errorMessage) {
        this(errorCode, null , errorMessage);
    }

    public Error(String errorMessage) {
        this(null, null, errorMessage);
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
