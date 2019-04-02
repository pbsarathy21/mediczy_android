package app.mediczy_com.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
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
import java.util.Locale;
import javax.net.ssl.HttpsURLConnection;
import app.mediczy_com.DoctorsList;
import app.mediczy_com.R;
import app.mediczy_com.bean.Bean_Doctor_List;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.CircularImageView;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.viewdetail.ViewDetail;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class DoctorsList_Adapter extends RecyclerView.Adapter<DoctorsList_Adapter.ViewHolder>
{
    Activity context;
    private ImageLoader imageLoader;
    private ArrayList<Bean_Doctor_List> arrayList;
    private List<Bean_Doctor_List> items;
    private String strTitle, ReqType, device_id, redId, Number, responseStatus, responseMsg, user_id;
    int position1;
    ViewHolder viewHolder1;

    public DoctorsList_Adapter(DoctorsList doctorsList, ArrayList<Bean_Doctor_List> itemsData, String strTitle) {
        this.arrayList = itemsData;
        this.context = doctorsList;
        imageLoader=new ImageLoader(doctorsList);
        this.items = new ArrayList<Bean_Doctor_List>();
        this.items.addAll(itemsData);
        this.strTitle = strTitle;
        Number= LocalStore.getInstance().getPhoneNumber(context);
        user_id = LocalStore.getInstance().getUserID(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DoctorsList_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_doctor_list, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final String Name = arrayList.get(position).getName();
        viewHolder.Tv_Name.setText(arrayList.get(position).getName());
        viewHolder.Tv_Study.setText(arrayList.get(position).getStudy());
        viewHolder.Tv_Experience.setText(arrayList.get(position).getExperience()+" yrs exp.");
        viewHolder.Tv_Type.setText(arrayList.get(position).getDoctor_Type());
        viewHolder.Tv_Ava_Date.setText(arrayList.get(position).getNextAvailable());

        final String OnlineStatus = arrayList.get(position).getAvailable();
        String price = arrayList.get(position).getRate();
        String NextAvailable = arrayList.get(position).getNextAvailable();
        final String Number = arrayList.get(position).getNumber();
        MLog.e("Number", "" + Number);

        if (price.equalsIgnoreCase("Free")  || price.equalsIgnoreCase("0")){
            viewHolder.Tv_Rate.setText("Free");
        }else{
            viewHolder.Tv_Rate.setText("\u20B9" + arrayList.get(position).getRate());
        }

        if (NextAvailable.equalsIgnoreCase("Appointment unavailable")){
            viewHolder.Tv_Ava_Date.setTextColor(context.getResources().getColor(R.color.gray));
        }else {
            viewHolder.Tv_Ava_Date.setTextColor(context.getResources().getColor(R.color.color_3));
        }

        if (OnlineStatus.equalsIgnoreCase("NO")){
            viewHolder.Iv_ImageStatus.setBackgroundResource(R.drawable.plus);
            viewHolder.Tv_Available.setText("Not in online");
        }else if(OnlineStatus.equalsIgnoreCase("YES")){
            viewHolder.Tv_Available.setText("Available Now");
            viewHolder.Iv_ImageStatus.setBackgroundResource(R.drawable.video_calling);
        }else if(OnlineStatus.equalsIgnoreCase("Requested")){
            viewHolder.Tv_Available.setText("Not in online");
            viewHolder.Iv_ImageStatus.setBackgroundResource(R.drawable.fab_tick);
        }

        String imagePath = arrayList.get(position).getImage();
        MLog.e("imagePath", "" + imagePath);
        imageLoader.DisplayImage(imagePath, viewHolder.Iv_Image, 4);

        viewHolder.Iv_Image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    Utility.getInstance().showImage(viewHolder.Iv_Image, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        viewHolder.Iv_ImageStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OnlineStatus.equalsIgnoreCase("NO")){
/*                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + Number));
                    context.startActivity(callIntent);*/
                    custom_dialog_App_conform(Name, position, viewHolder);
                }
            }
        });

        viewHolder.Rl_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = arrayList.get(position).getId();
                String Name = arrayList.get(position).getName();
                String date = arrayList.get(position).getAvailable_Date();
                Intent i = new Intent(context, ViewDetail.class);
                i.putExtra("id", id);
                i.putExtra("name", Name);
                i.putExtra("categoryName", strTitle);
                i.putExtra("date", date);
                i.putExtra("isFrom", "DoctorList");
                context.startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    private void custom_dialog_App_conform(String Name, final int position, final ViewHolder viewHolder) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_appoinment_conform);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.textView_app_conform_cancel);
        TextView tv_send = (TextView) dialog.findViewById(R.id.textView_app_conform_continue);
        TextView tv_title = (TextView) dialog.findViewById(R.id.textView1_app_conform_title);
        tv_title.setText("You will get SMS When " + Name + " is available");
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                viewHolder1= viewHolder;
                position1 = position;
                String id = arrayList.get(position).getId();
                if (Utility.getInstance().isConnectingToInternet(context)) {
                    ReqType="HealthTube_List";
                    RequestWebService(ReqType, id);
                } else {
                    AlertDialogFinish.Show(context, Constant.Alart_Internet, true);
                }
            }
        });
        dialog.show();
    }

    private void RequestWebService(String reqType1, String id) {
        if (reqType1.equals("HealthTube_List")) {
            ArrayList<NameValuePair> params = getParams(reqType1, id);
            String requestURL = IConstant_WebService.doctor_notification;
            AsyncRequest getPosts = new AsyncRequest(context, Constant.POST, params, "Updating data...");
            getPosts.execute(requestURL);
        }
    }

    private ArrayList<NameValuePair> getParams(String reqType, String id) {
        device_id = Utility.getInstance().getDeviceID(context);
        redId = LocalStore.getInstance().getGcmId(context);

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("fcm_id", redId));
        params.add(new BasicNameValuePair("device_id", device_id));
        params.add(new BasicNameValuePair("mobile_number", Number));
        params.add(new BasicNameValuePair("doctor_id", id));
        params.add(new BasicNameValuePair("user_id", user_id));
        return params;
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Tv_Name;
        public TextView Tv_Study;
        public TextView Tv_Experience;
        public TextView Tv_Available;
        public TextView Tv_Ava_Date;
        public TextView Tv_Rate;
        public TextView Tv_Type;
        public CircularImageView Iv_Image;
        public ImageView Iv_ImageStatus;
        public RelativeLayout Rl_Click;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Tv_Name = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_name);
            Tv_Study = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_study);
            Tv_Experience = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_exp);
            Tv_Available = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_available);
            Tv_Rate = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_rate);
            Tv_Type = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_type);
            Tv_Ava_Date = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_available_date);
            Iv_Image = (CircularImageView) itemLayoutView.findViewById(R.id.imageView_adapter_doctor_photo);
            Iv_ImageStatus = (ImageView) itemLayoutView.findViewById(R.id.imageView_adapter_status);
            Rl_Click = (RelativeLayout) itemLayoutView.findViewById(R.id.relative_view_doctor_adapter);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        if (charText.length() == 0) {
            arrayList.clear();
            arrayList.addAll(items);
        }
        else {
            for (Bean_Doctor_List wp : items) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayList.clear();
                    arrayList.add(wp);
                }else{
//                    arrayList.addAll(items);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class AsyncRequest extends AsyncTask<String, Integer, String> {
        Context context;
        String method = "GET";
        String dialog_msg, device_id;
        List<NameValuePair> parameters = null;
        ProgressDialog pDialog = null;

        // Three Constructors
        public AsyncRequest(Context a, String m, List<NameValuePair> p, String dialog_str) {
            context = a;
            method = m;
            parameters = p;
            dialog_msg = dialog_str;

            device_id = Utility.getInstance().getDeviceID(a);
            p.add(new BasicNameValuePair("device_id", device_id));
            for (NameValuePair param : parameters) {
                MLog.e("", "Request_Params: " + param.getName() + " = " + param.getValue());
            }
        }

        public AsyncRequest(Activity a, String m, String dialog_str) {
            context = a;
            method = m;
            dialog_msg = dialog_str;

            device_id = Utility.getInstance().getDeviceID(a);
        }

        public AsyncRequest(Activity a) {
            context = a;
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
            MLog.e("response_doctor_notification", "" + response);
            if (response!=null && !response.isEmpty()) {
                try {
                    JSONObject object1 = new JSONObject(response);
                    responseStatus = object1.getString("status");
                    if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)){
                        if (ReqType.equals("HealthTube_List")) {
                            arrayList.get(position1).setAvailable("Requested");
                            viewHolder1.Iv_ImageStatus.setBackgroundResource(R.drawable.fab_tick);
                            notifyDataSetChanged();
                        }
                    }else if(responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)){
                        responseMsg = object1.getString("msg");
                        AlertDialogFinish.Show((Activity) context, responseMsg, true);
                    } else {
                        AlertDialogFinish.Show((Activity) context, Constant.Alart_Status500, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogFinish.Show((Activity) context, e.toString(), true);
                }
            } else {
                AlertDialogFinish.Show((Activity) context, Constant.Alart_Status500, true);
            }
        }

        protected void onCancelled(String response) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

        private String get(String address) {
            String response = null;
            try {
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn = (HttpURLConnection) url.openConnection();
                conn.setUseCaches(false);
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
            for (NameValuePair pair : params) {
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
}