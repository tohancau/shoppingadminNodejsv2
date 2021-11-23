package etn.app.danghoc.shoppingadmin.model;

import java.util.List;

public class AdminModel {
    private boolean success;
    private String message;
    private List<Admin>result;

    public AdminModel(boolean success, String message, List<Admin> result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Admin> getResult() {
        return result;
    }

    public void setResult(List<Admin> result) {
        this.result = result;
    }
}
