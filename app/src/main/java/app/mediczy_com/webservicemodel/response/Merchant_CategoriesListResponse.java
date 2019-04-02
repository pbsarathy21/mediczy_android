package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Prithivi on 26-12-2017.
 */

public class Merchant_CategoriesListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("merchant_category_id")
    public String merchant_category_id;
    @SerializedName("name")
    public String name;
    @SerializedName("icon")
    public String icon;


    @SerializedName("location")
    public String location;
    @SerializedName("rating")
    public String rating;
    @SerializedName("company_notes")
    public String company_notes;
    @SerializedName("offers")
    public String offers;
}
