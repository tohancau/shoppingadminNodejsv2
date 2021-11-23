package etn.app.danghoc.shoppingadmin.common;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import etn.app.danghoc.shoppingadmin.model.Admin;
import etn.app.danghoc.shoppingadmin.model.User;

public class Common {

    public static final String API_KEY ="1234" ;
    //public static final String API_RESTAURANT_ENDPOINT ="http://10.0.2.2:3000/";
    public static final String API_RESTAURANT_ENDPOINT = "https://nguyenxuantri.xyz/";
    public static Admin currentUser;
    public static User userSelect;
    public static int positionUserSelect;

    public static String convertStatusToString(int trangThai) {
            if(trangThai==0)
                return "đang hoạt động";
            if (trangThai==1)
                    return "đang bị khóa";

            return "chưa rõ";
        }

    public static String formatPrice(double giaSP) {
        if (giaSP != 0) {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            df.setRoundingMode(RoundingMode.UP);
            String finalPrice = new StringBuilder(df.format(giaSP)).toString();
            return finalPrice;
            //.replace(".","");
        } else
            return "0.00";
    }
}
