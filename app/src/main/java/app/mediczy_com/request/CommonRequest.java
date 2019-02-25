package app.mediczy_com.request;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.mediczy_com.ChatBot.Diagonose_Value;
import app.mediczy_com.ChatBot.Value_men;

public class CommonRequest {


    //SERVICE
    public String user_query;
    public String is_android;
    public ArrayList<Value_men> inital_symptoms;
    public String user_id;
    public String name;
    public String age;
    public String gender;
    public String user_bot_report_id;
    public String type;
    public String lat;
    public String id;

    public ArrayList<Diagonose_Value> diagnosis_symptoms;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @SerializedName("long")

    public String longitude;

}
