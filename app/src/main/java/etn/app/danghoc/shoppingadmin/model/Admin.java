package etn.app.danghoc.shoppingadmin.model;

public class Admin {
   String IdAdmin,PhoneAdmin, NameAdmin;

    public Admin(String idAdmin, String phoneAdmin, String nameAdmin) {
        IdAdmin = idAdmin;
        PhoneAdmin = phoneAdmin;
        NameAdmin = nameAdmin;
    }

    public String getIdAdmin() {
        return IdAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        IdAdmin = idAdmin;
    }

    public String getPhoneAdmin() {
        return PhoneAdmin;
    }

    public void setPhoneAdmin(String phoneAdmin) {
        PhoneAdmin = phoneAdmin;
    }

    public String getNameAdmin() {
        return NameAdmin;
    }

    public void setNameAdmin(String nameAdmin) {
        NameAdmin = nameAdmin;
    }
}
