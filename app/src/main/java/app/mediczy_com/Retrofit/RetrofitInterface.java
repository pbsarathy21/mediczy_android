package app.mediczy_com.Retrofit;

import app.mediczy_com.request.CommonRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    // live

    String url = "https://mediczy.com/mediczy-api/api/mobile/";


/*    @FormUrlEncoded
    @POST("/phrase_to_symptoms")
    void chat_view(@Field("user_query") String u_qurry,
                   @Body("inital_symptoms") String lostProjectReasons,
                   Callback<ListDetails> chat_view);*/

    //@Headers("Content-Type: application/json")


 /*   @POST("/phrase_to_symptoms")
    abstract void chat_view(@Body TypedInput body, Callback<Object> callback);*/


    //   String url = "http://mediczy.com/mediczy-api/api/mobile/";

    @POST("phrase_to_symptoms")
    Call<ListDetails> chat_view(@Body CommonRequest commonRequest);


    @POST("symptoms_diagnosis")
    Call<ListDetails> Send_Chat(@Body CommonRequest commonRequest);


    @POST("medibot_report")
    Call<ListDetails> Report_Chat(@Body CommonRequest commonRequest);

    @POST("user_bot_report")
    Call<ListDetails> Report_Detail(@Body CommonRequest commonRequest);

    @POST("user_bot_reports")
    Call<ListDetails> Report_List(@Body CommonRequest commonRequest);

    @POST("active_doctors")
    Call<ListDetails> Act_Doctor(@Body CommonRequest commonRequest);

    @POST("user_symptoms_count")
    Call<ListDetails> Sys_count(@Body CommonRequest commonRequest);

    @GET("symptom_packages")
    Call<ListDetails> pack();

    @GET("splash-screens")
    Call<ListDetails> slide();

    @FormUrlEncoded
    @POST("android_message_key")
    Call<ListDetails> hashKeyApi(@Field("hash_key") String hash_key);


}