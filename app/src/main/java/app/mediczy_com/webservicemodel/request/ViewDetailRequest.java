package app.mediczy_com.webservicemodel.request;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class ViewDetailRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String id;
    public String free_code;
    public String mobile_number;
    public String doctor_id;
}
