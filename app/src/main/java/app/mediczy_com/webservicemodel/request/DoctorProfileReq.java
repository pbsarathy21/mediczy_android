package app.mediczy_com.webservicemodel.request;

import java.io.Serializable;

/**
 * Created by Prithivi on 08-10-2017.
 */

public class DoctorProfileReq implements Serializable {
    private static final long serialVersionUID = 1L;

    public String doctor_banner_id;
    public String doctor_id;
    public String firstname;
    public String lastname;
    public String degree;
    public String experience;
    public String address;
    public String price;
    public String services;
    public String languages;
    public String licence;
    public String image;
    public String adsId;
    public String imagePath;
}
