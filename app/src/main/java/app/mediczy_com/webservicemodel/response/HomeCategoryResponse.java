package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class HomeCategoryResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    public String id;
    @SerializedName("category_id")
    public String category_id;
    @SerializedName("hospital_id")
    public String hospital_id;
    @SerializedName("online_count")
    public String online_count;
    @SerializedName("doctors_online_count")
    public String doctors_online_count;
    @SerializedName("name")
    public String name;
    @SerializedName("image")
    public String image;
}
