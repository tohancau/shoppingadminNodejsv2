package etn.app.danghoc.shoppingadmin.model;

import java.util.List;

public class UserModel {
    private boolean success;
    private List<User> result;
    private String message;

    public UserModel(boolean success, List<User> result, String message) {
        this.success = success;
        this.result = result;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<User> getResult() {
        return result;
    }

    public void setResult(List<User> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
