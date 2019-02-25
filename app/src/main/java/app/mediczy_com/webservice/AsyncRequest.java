package app.mediczy_com.webservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 07-12-2015.
 */
public class AsyncRequest extends AsyncTask<String, Integer, String> {

    OnAsyncRequestComplete caller;
    Context context;
    Fragment fragment;
    String method = "GET";
    String dialog_msg, device_id;
    List<NameValuePair> parameters = null;
    ProgressDialog pDialog = null;

    // Three Constructors
    public AsyncRequest(Context a, Fragment fragment1, String m, List<NameValuePair> p, String dialog_str) {
        if (fragment1!=null)
            caller = (OnAsyncRequestComplete) fragment1;
        else
            caller = (OnAsyncRequestComplete) a;
        context = a;
        fragment = fragment1;
        method = m;
        parameters = p;
        dialog_msg = dialog_str;

/*        device_id = DeviceId.getDeviceID(a);
        p.add(new BasicNameValuePair("device_id", device_id));*/
        for (NameValuePair param : parameters) {
            MLog.e("", "Request_Params: " + param.getName() + " = " + param.getValue());
        }
    }

    public AsyncRequest(Activity a, Fragment fragment1, String m, String dialog_str) {
        if (fragment1!=null)
            caller = (OnAsyncRequestComplete) fragment1;
        else
            caller = (OnAsyncRequestComplete) a;
        context = a;
        method = m;
        dialog_msg = dialog_str;

//        device_id = DeviceId.getDeviceID(a);
    }

    public AsyncRequest(Activity a) {
        caller = (OnAsyncRequestComplete) a;
        context = a;
    }

    // Interface to be implemented by calling activity
    public interface OnAsyncRequestComplete {
        public void asyncResponse(String response);
    }

    public String doInBackground(String... urls) {
        // get url pointing to entry point of API
        String address = urls[0].toString();
        MLog.e("API", "" + address);
        if (method == "POST") {
            return post(address);
        }
        if (method == "GET") {
            return get(address);
        }
        return null;
    }

    public void onPreExecute() {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage(dialog_msg); // typically you will define such
        // strings in a remote file.
        pDialog.show();
    }

    public void onProgressUpdate(Integer... progress) {
        // you can implement some progressBar and update it in this record
        // setProgressPercent(progress[0]);
    }

    public void onPostExecute(String response) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.asyncResponse(response);
    }

    protected void onCancelled(String response) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.asyncResponse(response);
    }

    private String get(String address) {
        String response = null;
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setDoInput(true); // true if we want to read server's response
            conn.setDoOutput(false);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response1 = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response1.append(inputLine);
                }
                in.close();
                response = response1.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
            MLog.e("HttpConnection_Res_Exe", "" + e);
        }
        MLog.e("HttpConnection_Response", "" + response);
        return response;
    }

    private String post(String address) {
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
            writer.write(getQuery(parameters));
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

    private String stringifyResponse(HttpResponse response) {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            return sb.toString();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}