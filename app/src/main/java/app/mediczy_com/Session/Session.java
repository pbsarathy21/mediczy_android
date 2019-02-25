package app.mediczy_com.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ninos on 2/20/2018.
 */

public class Session {
    private SharedPreferences prefs;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String REGID = "regid";
    public static final String ConvID = "convID";
    public static final String Chat_price = "chatprice";
    public static final String u_id = "user_id";
    public static final String ISMY_ADS_PAGE = "ismy_ads_page";
    public static final String d_email = "email";
    public static final String d_number = "number";
    public static final String d_name = "name";
    public static final String ON_OFF = "0";
    public static final String Distance = "90";

    public static final String points = "point";


    public static final String Block_Check = "Statusblock";


    public static final String PUTRDATE = "puredate";


    public static final String ISCHATPAGE = "ischatpage";


    public static final String Lat = "latitude";
    public static final String Long = "longtude";

    public static final String text_chat = "chat_in";

    public static final String Chat_Yes = "yes";

    public static final String Chat_No = "no";


    public static final String View_id = "view";

    public static final String Active = "act_doc";

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);

    }

    public String getChat_No() {

        String c_no = prefs.getString(Chat_No, "");
        return c_no;

    }

    public void setChat_No(String c_no) {

        prefs.edit().putString(Chat_No, c_no).apply();

    }


    public String getView_id() {

        String view = prefs.getString(View_id, "");
        return view;

    }

    public void setView_id(String view) {

        prefs.edit().putString(View_id, view).apply();

    }





    public String getDoc() {

        String d_active = prefs.getString(Active, "");
        return d_active;

    }

    public void setDoc(String d_active) {

        prefs.edit().putString(Active, d_active).apply();

    }


    public String getChat_Yes() {

        String c_yes = prefs.getString(Chat_Yes, "");
        return c_yes;

    }

    public void setChat_Yes(String c_yes) {

        prefs.edit().putString(Chat_Yes, c_yes).apply();

    }


    public String getText_chat() {

        String text_c = prefs.getString(text_chat, "");
        return text_c;

    }

    public void setText_chat(String tex_c) {

        prefs.edit().putString(text_chat, tex_c).apply();

    }


    public String getLat() {

        String c_lat = prefs.getString(Lat, "");
        return c_lat;

    }

    public void setLat(String c_lat) {

        prefs.edit().putString(Lat, c_lat).apply();

    }


    public String getLong() {

        String c_long = prefs.getString(Long, "");
        return c_long;

    }

    public void setLong(String c_long) {

        prefs.edit().putString(Long, c_long).apply();

    }


    public String getUser_id() {
        String id = prefs.getString(u_id, "");
        return id;
    }

    public void setUser_id(String id) {
        prefs.edit().putString(u_id, id).apply();
    }

    public void setFCMID(String id) {
        prefs.edit().putString(REGID, id).apply();
    }

    public String getFCMID() {
        String fcmid = prefs.getString(REGID, "");
        return fcmid;
    }

    public Boolean getISCHATPAGE() {
        Boolean isChatpage = prefs.getBoolean(ISCHATPAGE, false);
        return isChatpage;
    }

    public String getD_email() {

        String email = prefs.getString(d_email, "");
        return email;

    }

    public void setD_email(String email) {

        prefs.edit().putString(d_email, email).apply();

    }


    public String getD_name() {

        String name = prefs.getString(d_name, "");
        return name;

    }

    public void setD_name(String name) {

        prefs.edit().putString(d_name, name).apply();

    }

    public String getonoff() {

        String ooff = prefs.getString(ON_OFF, "");
        return ooff;

    }

    public void setonoff(String ooff) {

        prefs.edit().putString(ON_OFF, ooff).apply();

    }


    public String getdistance() {

        String distance = prefs.getString(Distance, "");
        return distance;

    }

    public void setdistance(String distance) {

        prefs.edit().putString(Distance, distance).apply();

    }


    public String getPoints() {

        String poi = prefs.getString(points, "");
        return poi;

    }

    public void setPoints(String poi) {

        prefs.edit().putString(points, poi).apply();

    }


    public String getD_number() {

        String number = prefs.getString(d_number, "");
        return number;

    }

    public void setD_number(String number) {

        prefs.edit().putString(d_number, number).apply();

    }


    public void setISCHATPAGE(Boolean ismyads_page) {
        prefs.edit().putBoolean(ISCHATPAGE, ismyads_page).apply();
    }

    public Boolean getISMY_ADS_PAGE() {
        Boolean ismyads_page = prefs.getBoolean(ISMY_ADS_PAGE, false);
        return ismyads_page;
    }

    public void setISMY_ADS_PAGE(Boolean ischatpage) {
        prefs.edit().putBoolean(ISMY_ADS_PAGE, ischatpage).apply();
    }

    public void setConvID(String id) {
        prefs.edit().putString(ConvID, id).apply();
    }

    public String getConvID() {
        String convid = prefs.getString(ConvID, "");
        return convid;
    }

    public void setChat_price(String price) {
        prefs.edit().putString(Chat_price, price).apply();
    }

    public String getChat_price() {
        String price = prefs.getString(Chat_price, "");
        return price;
    }


    public void setBlock_Check(String Block) {
        prefs.edit().putString(Block_Check, Block).apply();
    }

    public String getBlock_Check() {
        String Block = prefs.getString(Block_Check, "");
        return Block;
    }


    public void setPutrdate(String puredate) {
        prefs.edit().putString(PUTRDATE, puredate).apply();
    }

    public String getPutrdate() {
        String puredate = prefs.getString(PUTRDATE, "");
        return puredate;
    }

}
