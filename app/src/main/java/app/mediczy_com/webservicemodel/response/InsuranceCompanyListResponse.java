package app.mediczy_com.webservicemodel.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class InsuranceCompanyListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("name")
    public String name;
    @SerializedName("policy_number")
    public String policy_number;
    @SerializedName("sum_insured")
    public String sum_insured;

    @SerializedName("tpa_id")
    public String tpa_id;
}
