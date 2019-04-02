package app.mediczy_com.webservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
public class AsyncImageRequest extends AsyncTask<String, Integer, String> {

    OnAsyncImageRequestComplete caller;
    Context context;
    String method = "GET";
    String dialog_msg, device_id;
    MultipartEntity parameters = null;
    ProgressDialog pDialog = null;
    Bitmap bitmap = null;

    // Three Constructors
    public AsyncImageRequest(Context a, String m, MultipartEntity p, String dialog_str, Bitmap bitmap1) {
        caller = (OnAsyncImageRequestComplete) a;
        context = a;
        method = m;
        parameters = p;
        dialog_msg = dialog_str;
        bitmap = bitmap1;

        for(int i=0; i<parameters.getContentLength(); i++){
            MLog.e("getName", "" + parameters.getContentType().getName());
            MLog.e("getValue", "" + parameters.getContentType().getValue());
        }
    }

    public AsyncImageRequest(Activity a, String m, String dialog_str) {
        caller = (OnAsyncImageRequestComplete) a;
        context = a;
        method = m;
        dialog_msg = dialog_str;

//        device_id = DeviceId.getDeviceID(a);
    }

    public AsyncImageRequest(Activity a) {
        caller = (OnAsyncImageRequestComplete) a;
        context = a;
    }

    // Interface to be implemented by calling activity
    public interface OnAsyncImageRequestComplete {
        public void asyncImageResponse(String response, Bitmap bitmap);
    }

    public String doInBackground(String... urls) {
        // get url pointing to entry point of API
        String address = urls[0].toString();
        MLog.e("API", "" + address);
        if (method == "POST") {
            return post(address);
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
        caller.asyncImageResponse(response, bitmap);
    }

    protected void onCancelled(String response) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.asyncImageResponse(response, bitmap);
    }

    private String post(String address) {
        String result = null;
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
//			conn.setRequestProperty("image", fileName);
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Content-length", reqEntity.getContentLength() + "");
            conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());
            OutputStream os = conn.getOutputStream();
            reqEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                result = response.toString();
            } else {
                result = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MLog.e("HttpConnection_Res_Exe", "" + e);
        }
        MLog.e("HttpConnection_Response", "" + result);
        return result;
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