package app.mediczy_com.webservicemodel.request;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class FilterSearchRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String id;
    public String location;
    public String language;
    public String education;
    public String docFees;
    public String speciality;
    public String appointment;
    public String experience;
    public String latitude;
    public String longitude;
}
