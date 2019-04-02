package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Prithivi on 05-03-2017.
 */

public class FilterDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("msg")
    public String msg;

    @SerializedName("status")
    public String status;

    @SerializedName("filter_id")
    public String filter_id;

    @SerializedName("locations")
    public List<Filter> location;

    @SerializedName("languages")
    public List<Filter> language;

    @SerializedName("educations")
    public List<Filter> education;

    @SerializedName("fees")
    public List<Filter> doctorfees;

    @SerializedName("categories")
    public List<Filter> categories;

    @SerializedName("appointment")
    public List<Filter> appointment;

    @SerializedName("experiences")
    public List<Filter> experience;
}
