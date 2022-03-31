package etn.app.danghoc.shoppingadmin.model;

import java.util.List;

public class SanPhamBaoCaoModel {
    String message;
    boolean success;
    List<SanPhamBaoCao> result;

    public SanPhamBaoCaoModel(String message, boolean success, List<SanPhamBaoCao> result) {
        this.message = message;
        this.success = success;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<SanPhamBaoCao> getResult() {
        return result;
    }

    public void setResult(List<SanPhamBaoCao> result) {
        this.result = result;
    }
}
