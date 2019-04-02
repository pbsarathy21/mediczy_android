package app.mediczy_com.webservice.request;

import com.google.gson.Gson;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.webservicemodel.response.ChatResponseModel;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import app.mediczy_com.webservicemodel.response.FilterResponse;
import app.mediczy_com.webservicemodel.response.NetworkPartnerDetailResponse;
import app.mediczy_com.webservicemodel.response.ViewDetailResponse;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class Parse {

    public static synchronized ViewDetailResponse ViewDetailResponse(String response) {
        try {
            Gson gson = new Gson();
            String res = response.toString();
            MLog.d("", res);
            return gson.fromJson(res, ViewDetailResponse.class);
        } catch (Exception e) {
            MLog.e("Parser_Exception", ""+e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized CommonResponse CommonParse(String response) {
        try {
            Gson gson = new Gson();
            String res = response.toString();
            MLog.d("", res);
            return gson.fromJson(res, CommonResponse.class);
        } catch (Exception e) {
            MLog.e("Parser_Exception", ""+e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized FilterResponse FilterResponse(String response) {
        try {
            Gson gson = new Gson();
            String res = response.toString();
            MLog.d("", res);
            return gson.fromJson(res, FilterResponse.class);
        } catch (Exception e) {
            MLog.e("Parser_Exception", ""+e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized ChatResponseModel ChatResponse(String response) {
        try {
            Gson gson = new Gson();
            String res = response.toString();
            MLog.d("", res);
            return gson.fromJson(res, ChatResponseModel.class);
        } catch (Exception e) {
            MLog.e("Parser_Exception", ""+e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized NetworkPartnerDetailResponse NetworkPartnerDetailResponse(String response) {
        try {
            Gson gson = new Gson();
            String res = response.toString();
            MLog.d("", res);
            return gson.fromJson(res, NetworkPartnerDetailResponse.class);
        } catch (Exception e) {
            MLog.e("Parser_Exception", ""+e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
