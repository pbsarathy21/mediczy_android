package app.mediczy_com.service_broadcast;

import android.app.IntentService;
import android.content.Intent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;

/**
 * Created by Prithivi Raj on 02-01-2016.
 */
public class ServiceOnlineStatus extends IntentService {

    public static final String SERVICE_IN_MSG = "msg";
    ArrayList<NameValuePair> params;

    public ServiceOnlineStatus() {
        super("ServiceOnlineStatus");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
        //        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String requestString = intent.getStringExtra(SERVICE_IN_MSG);
        MLog.d("Service_requestString--->:", "" + requestString);

        String device_id = Utility.getInstance().getDeviceID(ServiceOnlineStatus.this);
        String gcm_id = LocalStore.getInstance().getGcmId(this);
        String USerId = LocalStore.getInstance().getUserID(this);
        String Type = LocalStore.getInstance().getType(this);

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("number", LocalStore.getInstance().getPhoneNumber(ServiceOnlineStatus.this)));
        params.add(new BasicNameValuePair("device_id", device_id));
        params.add(new BasicNameValuePair("gcm_id", gcm_id));
        params.add(new BasicNameValuePair("user_id", USerId));
        params.add(new BasicNameValuePair("type", Type));
        params.add(new BasicNameValuePair("status", "online"));

        post(IConstant_WebService.online_status);
    }

    private String post(String address) {
        MLog.e("OnlineStatus", "" + address);
        for (NameValuePair param : params) {
            MLog.e("", "Request_Params: " + param.getName() + " = " + param.getValue());
        }
        String response = null;
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int response_code = conn.getResponseCode();
            if (response_code == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response_sb = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response_sb.append(inputLine);
                }
                in.close();
                response = response_sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            MLog.e("HttpConnection_Res_Exe", "" + e);
        }
        MLog.e("HttpConnection_Response", "" + response);
        return response;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
