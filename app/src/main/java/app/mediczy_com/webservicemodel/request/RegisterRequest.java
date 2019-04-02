package app.mediczy_com.webservicemodel.request;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-2-2018.
 */

public class RegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    public String firstname;
    public String lastname;
    public String number;
    public String country_code;
    public String gender;
    public String dob;
    public String ref_id;
    public String type;
    public String fcm_id;
    public String device_id;
    public String username;
    public String password;
    public String free_code;
}
