package HelperClasses;

public class ReturnHelper {

    private Boolean success;
    private String message;

    public ReturnHelper() {
    }

    public ReturnHelper(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getIsSuccess() {
        return success;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.success = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
