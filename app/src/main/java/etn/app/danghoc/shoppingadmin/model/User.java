package etn.app.danghoc.shoppingadmin.model;

public class User {
    private String IdUser, PhoneUser, NameUser, AddressUser;
    int TrangThai;

    public User(String idUser, String phoneUser, String nameUser, String addressUser, int trangThai) {
        IdUser = idUser;
        PhoneUser = phoneUser;
        NameUser = nameUser;
        AddressUser = addressUser;
        TrangThai = trangThai;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public String getPhoneUser() {
        return PhoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        PhoneUser = phoneUser;
    }

    public String getNameUser() {
        return NameUser;
    }

    public void setNameUser(String nameUser) {
        NameUser = nameUser;
    }

    public String getAddressUser() {
        return AddressUser;
    }

    public void setAddressUser(String addressUser) {
        AddressUser = addressUser;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }
}
