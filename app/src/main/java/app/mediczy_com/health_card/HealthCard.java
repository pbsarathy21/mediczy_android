package app.mediczy_com.health_card;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vinaygaba.creditcardview.CreditCardView;
import app.mediczy_com.BaseActivity;
import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import app.mediczy_com.webservicemodel.response.HealthCardDetail;
import app.mediczy_com.webservicemodel.response.Health_Card_ListResponse;

/**
 * Created by Prithivi on 09-05-2017.
 */

public class HealthCard extends BaseActivity implements ResponseListener {

    private Toolbar toolBar;
    private ProgressDialog pDialog = null;
    private TextView tvCardId, tv_CardValidity;
    private int mRequestType = 0;
    private CreditCardView healthCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_card);
        setToolBar();
        init();
    }

    private void setToolBar() {
        toolBar = (Toolbar) findViewById(R.id.tool_bar_health_card);
        setSupportActionBar(toolBar);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Health Card");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void init() {
//        tvCardId = (TextView) findViewById(R.id.textView_health_app_card_num);
//        tv_CardValidity = (TextView) findViewById(R.id.textView_health_app_valid_up_value);

        healthCard = (CreditCardView) findViewById(R.id.health_card);
        healthCard.setCardName("");
        healthCard.setCardNumber("");
        healthCard.setExpiryDate("");
        healthCard.setVisibility(View.GONE);
        onRequest();
    }

    private void onRequest() {
        mRequestType = IConstant_WebService.WSR_HealthCardReq;
        if (Utility.getInstance().isConnectingToInternet(this)) {
            showLoading();
            RequestManager.HealthCardRequest(this, null, mRequestType);
        }else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        dismissDialog();
        if (requestType == IConstant_WebService.WSR_HealthCardReq) {
            CommonResponse req = (CommonResponse) responseObj;
            if (req != null) {
                String responseSuccess = req.status;
                String msg = req.msg;
                MLog.e("responseSuccess", "" + responseSuccess);
                MLog.e("msg", "" + msg);
                if (responseSuccess != null) {
                    if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        setData(req);
                    }else if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(this, msg, true);
                    }
                }
            }
        }
    }

    private void setData(CommonResponse req) {
        String card_id, validity, cardName, health_card_name;
        try {
            card_id = req.card_id;
            validity = req.card_validity;
            cardName = req.card_name;
            health_card_name = req.health_card_name;
            healthCard.setCardName(cardName);
            healthCard.setCardNumber(card_id);
            healthCard.setExpiryDate(validity);

            if (health_card_name.equalsIgnoreCase("SILVER")) {
                healthCard.setBackgroundResource(R.drawable.health_card_silver);
            }else if (health_card_name.equalsIgnoreCase("GOLD")) {
                healthCard.setBackgroundResource(R.drawable.health_card_gold);
            }else if (health_card_name.equalsIgnoreCase("PLATINUM")) {
                healthCard.setBackgroundResource(R.drawable.health_card_platinum);
            }else if (health_card_name.equalsIgnoreCase("RED")) {
                healthCard.setBackgroundResource(R.drawable.health_card_red);
            } else {
                healthCard.setBackgroundResource(R.drawable.health_card_bg);
            }
            healthCard.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(this, e.toString(), true);
        }
    }

    private void showLoading() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(Constant.dialog_msg);
        // strings in a remote file.
        pDialog.show();
    }

    private void dismissDialog() {
        pDialog.dismiss();
    }
}
