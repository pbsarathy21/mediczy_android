package app.mediczy_com.service_broadcast;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Date;

import app.mediczy_com.doctor_prescribed.ADD_Doctor_Prescribed;
import app.mediczy_com.storage.LocalStore;

/**
 * Created by Prithivi on 04-12-2016.
 */

public class CallReceiver extends PhoneCallReceiver_1 {

//    String ReqType, strPrescriptionMsg, doctorId;

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start)
    {
        //
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start)
    {
        //
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
 //       showToast(number+" onIncomingCallEnded", ctx);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
        //
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
    {
        if (LocalStore.getInstance().getDoctorDetailNumber(ctx).equalsIgnoreCase(number)) {
 //           doctorId = LocalStore.getDoctorDetailId(ctx);
  //          custom_dialog_prescription(ctx);
            String Type = LocalStore.getInstance().getType(ctx);
            if (Type.equalsIgnoreCase("Doctor")){
                doctorPrescriptionActivity(ctx);
            }
        }else {
//            showToast(number+" onOutgoingCallEnded", ctx);
        }

    }

    private void doctorPrescriptionActivity(Context ctx) {
        Intent myIntent = new Intent(ctx, ADD_Doctor_Prescribed.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(myIntent);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        //
    }

    private void showToast(String msg, Context ctx){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

/*
    private void custom_dialog_prescription(final Context ctx) {
        final Animation animationShake = AnimationUtils.loadAnimation(ctx, R.anim.shake);
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_add_prescription);
        dialog.setCancelable(false);
        final EditText Ed_PopUp = (EditText) dialog.findViewById(R.id.editText_popup__add_prescription);
        final TextView tvSend = (TextView) dialog.findViewById(R.id.textView_pres_send);
        final TextView tvCancel = (TextView) dialog.findViewById(R.id.textView_pres_cancel);
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Ed_PopUp, InputMethodManager.SHOW_IMPLICIT);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String str_ed = Ed_PopUp.getText().toString();
                if (!str_ed.equals("")) {
                    strPrescriptionMsg = str_ed;
                    dialog.dismiss();
  //                  LocalStore.Clear(ctx);

                    ReqType = "prescription";
                    onRequest(ReqType, ctx);
                } else {
                    Ed_PopUp.startAnimation(animationShake);
                    showToast("Enter prescription !!!", ctx);
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onRequest(String ReqType, Context ctx) {
        if (InternetConnection.isConnectingToInternet(ctx)) {
            String requestURL = IConstant_WebService.prescription;
            Async_Uploading getPosts = new Async_Uploading(requestURL , ctx);
            getPosts.execute();
        } else {
            showToast(Constant.Alart_Internet, ctx);
        }
    }

    class Async_Uploading extends AsyncTask<Void, Void, String> {
        private ProgressDialog chat_bot_progress = null;
        String requestURL, result;
        Context ctx;

        public Async_Uploading(String reqType1, Context ctx1) {
            requestURL = reqType1;
            ctx = ctx1;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            chat_bot_progress = ProgressDialog.show(ctx, null, "Updating data...");
            chat_bot_progress.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                MLog.e("requestURL--------->", "" + requestURL);
                URL url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
//			conn.setRequestProperty("image", fileName);

                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (ReqType.equals("prescription")) {

                    String device_id = DeviceId.getDeviceID(ctx);
                    String gcm_id = LocalStore.getGcmId(ctx);
                    MLog.e("device_id", "" + device_id);
                    MLog.e("gcm_id", "" + gcm_id);
                    MLog.e("doctor_id", "" + doctorId);
                    MLog.e("prescription", "" + strPrescriptionMsg);

                    reqEntity.addPart("gcm_id", new StringBody(gcm_id));
                    reqEntity.addPart("device_id", new StringBody(device_id));
                    reqEntity.addPart("doctor_id", new StringBody(doctorId));
                    reqEntity.addPart("prescription", new StringBody(strPrescriptionMsg));
                }
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
            }
            return result;
        }

        @Override
        protected void onPostExecute(String response) {
            MLog.e("response", "" + response);
            if (response != null && !response.isEmpty()) {
                LocalStore.Clear(ctx);
            }
            chat_bot_progress.dismiss();
            super.onPostExecute(result);
        }
    }

 */
}
