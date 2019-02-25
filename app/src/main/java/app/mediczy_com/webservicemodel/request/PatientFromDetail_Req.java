package app.mediczy_com.webservicemodel.request;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class PatientFromDetail_Req implements Serializable {
    private static final long serialVersionUID = 1L;
    public String doctor_id;
    public String appointment_id;
    public String doctor_phone_number;
}
