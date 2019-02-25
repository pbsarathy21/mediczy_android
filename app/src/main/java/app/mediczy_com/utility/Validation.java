
package app.mediczy_com.utility;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

     //Context mContect;



    // validating email id
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /*// validating email id
    public static boolean isSpl_Password(String spl_pass) {
        String SPL_PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$";

        Pattern pattern = Pattern.compile(SPL_PASS_PATTERN);
        Matcher matcher = pattern.matcher(spl_pass);
        return matcher.matches();
    }*/


    public boolean isSpl_Password(final String spl_pass) {

        Pattern pattern;
        Matcher matcher;

        if (spl_pass.length() >= 8) {
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(spl_pass);

            return matcher.matches();

        } else {

           // Toast.makeText(mContect, "Minimum 8 Character", Toast.LENGTH_SHORT).show();

        }
        return false;


    }


    public static boolean isValidName(String name) {
        String Name_Pattern = "^[a-zA-Z ]+$";
        Pattern pattern = Pattern.compile(Name_Pattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public boolean isValidPhone(String name) {
        if (name.length() == 10) {
            return true;
        }
        return false;
    }


    public boolean isValidPincode(String name) {
        if (name.length() == 6) {
            return true;
        }
        return false;
    }

    public boolean isZip(String zip) {
        if (zip.length() == 5) {
            return true;
        }
        return false;
    }

    // validating password with retype password
    public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 5) {
            return true;
        }
        return false;
    }

    //
    public boolean isEmpty(String val) {
        if (val == null || val.length() == 0) {
            return true;
        }
        return false;
    }


    public boolean isChecked(boolean check) {

        if (check = true) {

            return true;
        }

        return false;

    }

    // validating password with retype password
    public boolean isValidConfirmPassword(String spl_pass, String Cpass) {
        if (spl_pass.equals(Cpass)) {
            return true;
        }

        return false;
    }

    public boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url);

        if (m.matches())
            return true;
        else
            return false;
    }

    public boolean isConfirmAccountNumber(String accno, String Caccno) {
        if (accno.equals(Caccno)) {
            return true;
        }
        return false;
    }

    public boolean isAccountNumber(String accno) {
        if (accno != null && accno.length() > 5) {
            return true;
        }
        return false;

    }


    public boolean isIfsc(String ifsc) {
        if (ifsc != null && ifsc.length() > 10) {
            return true;
        }
        return false;

    }


}