package app.mediczy_com.health_card;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

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
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import app.mediczy_com.webservicemodel.response.Health_Card_ListResponse;

public class HealthCardListActivity extends BaseActivity implements ResponseListener,
        PaymentCardBuyObserver {

    private Toolbar toolBar;
    private Animation animationShake;
    private ProgressDialog pDialog = null;
    private RecyclerView mRecyclerView;
    public static HealthCardListActivity healthCardListActivity;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Health_Card_ListResponse> arrayList = new ArrayList<Health_Card_ListResponse>();
    private int mRequestType = 0;
    private RequestCommon requestCommon;
    private Dialog dialogCheckHealthCardAvailable;
    private String selectedCard="a", selectedCardAmount="", health_card_number="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_card_list_activity);
        healthCardListActivity = this;
        setToolBar();
        init();
        custom_dialog_checkHealthCardAvailable();
    }

    private void setToolBar() {
        toolBar = (Toolbar) findViewById(R.id.tool_bar_cardbuy_list);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Health Card List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void init() {
        animationShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_card_list1);
        RecyclerView.LayoutManager mLayoutManager;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRequestType = IConstant_WebService.WSR_NP_health_card_list_Req;
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            if (mRequestType == IConstant_WebService.WSR_NP_health_card_list_Req) {
                RequestManager.CardBuyListRequest(this, null, null, mRequestType);
            }if (mRequestType == IConstant_WebService.WSR_HealthCardCheckAvailable) {
                requestCommon = new RequestCommon();
                requestCommon.health_card_number = health_card_number;
                RequestManager.CheckHealthCardAvailable(this, null, requestCommon, mRequestType);
            }
        }else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        CommonResponse res = (CommonResponse) responseObj;
        MLog.e("res", "" + res);
        try {
            if (res != null) {
                String responseSuccess = res.status;
                String msg = res.msg;
                MLog.e("responseSuccess", "" + responseSuccess);
                MLog.e("msg", "" + msg);
                if (requestType == IConstant_WebService.WSR_NP_health_card_list_Req) {
                    if (res.health_card_list != null) {
                        if (res.health_card_list.size() > 0) {
                            for (int i = 0; i < res.health_card_list.size(); i++) {
                                Health_Card_ListResponse response = new Health_Card_ListResponse();
                                response.membership_card_id = res.health_card_list.get(i).membership_card_id;
                                response.amount = res.health_card_list.get(i).amount;
                                response.benefits = res.health_card_list.get(i).benefits;
                                response.image_path = res.health_card_list.get(i).image_path;
                                if (res.health_card_list.get(i).name.equalsIgnoreCase("SILVER")) {
                                    response.name = "SILVER";
                                    response.cardImage = R.drawable.health_card_silver;
                                }else if (res.health_card_list.get(i).name.equalsIgnoreCase("GOLD")) {
                                    response.cardImage = R.drawable.health_card_gold;
                                    response.name = "GOLD";
                                }else if (res.health_card_list.get(i).name.equalsIgnoreCase("PLATINUM")) {
                                    response.cardImage = R.drawable.health_card_platinum;
                                    response.name = "PLATINUM";
                                }else if (res.health_card_list.get(i).name.equalsIgnoreCase("RED")) {
                                    response.cardImage = R.drawable.health_card_red;
                                    response.name = "RED";
                                } else {
                                    response.cardImage = R.drawable.health_card_bg;
                                }
                                arrayList.add(response);
                            }
                            HealthCard_Adapter adapter = new HealthCard_Adapter(this, arrayList);
                            mRecyclerView.setAdapter(adapter);
                            Utility.getInstance().runLayoutAnimation(mRecyclerView, this);
                        } else {
                            Utility.getInstance().showToast("Card List is Empty", this);
                        }
                    }
                } else if (requestType == IConstant_WebService.WSR_HealthCardCheckAvailable) {
                    if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        dialogCheckHealthCardAvailable.dismiss();
                        LocalStore.getInstance().setHealthCard_Available(this, "yes");
                        Intent i = new Intent(HealthCardListActivity.this, HealthCard.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                    } if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(this, msg, true);
                    }
                }
            } else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utility.getInstance().showToast(e.toString(), this);
        }
    }

    @Override
    public void onPaymentReceived(boolean paymentStatus) {
        //       showMessage();
    }

    @Override
    public void onCardSelected(Health_Card_ListResponse object) {
        selectedCard = object.name;
        selectedCardAmount = object.amount;
 //       tvSelectedHealthCard.setText(""+object.name);

        Intent intent = new Intent(this, CardBuyActivity.class);
        intent.putExtra("membership_card_id", object.membership_card_id);
        intent.putExtra("selectedCardAmount", object.amount);
        intent.putExtra("selectedCardName", object.name);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }

    @Override
    public void onCardBenefitsSelected(Health_Card_ListResponse object) {
        viewBenefits(object);
    }

    private void viewBenefits(Health_Card_ListResponse object) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_terms_condition);
        TextView tvHeading = (TextView) dialog.findViewById(R.id.textView_terms_heading);
        TextView tvValue = (TextView) dialog.findViewById(R.id.textView_terms_value);
        tvHeading.setText(""+object.name+" BENEFITS");
        tvValue.setText(object.benefits);
        dialog.show();
    }

    private void custom_dialog_checkHealthCardAvailable() {
        final Animation animationShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        dialogCheckHealthCardAvailable = new Dialog(this);
        dialogCheckHealthCardAvailable.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCheckHealthCardAvailable.setContentView(R.layout.popup_add_prescription);
        dialogCheckHealthCardAvailable.setCancelable(false);
        final EditText Ed_Subject = (EditText) dialogCheckHealthCardAvailable.findViewById(R.id.editText_popup__add_prescription_subject);
        final EditText Ed_PopUp = (EditText) dialogCheckHealthCardAvailable.findViewById(R.id.editText_popup__add_prescription);
        final CheckBox toggleButton = (CheckBox) dialogCheckHealthCardAvailable.findViewById(R.id.toggleButton_send_prescription);
        final TextView tvTitle = (TextView) dialogCheckHealthCardAvailable.findViewById(R.id.textView_popup_add_prescription);
        final TextView tvSend = (TextView) dialogCheckHealthCardAvailable.findViewById(R.id.textView_pres_send);
        final TextView tvCancel = (TextView) dialogCheckHealthCardAvailable.findViewById(R.id.textView_pres_cancel);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Ed_PopUp, InputMethodManager.SHOW_IMPLICIT);
        tvTitle.setText("Do You have Health card?");
        Ed_Subject.setHint("Enter Health Card Number");
        toggleButton.setVisibility(View.GONE);
        Ed_PopUp.setVisibility(View.GONE);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String str_health_card_number = Ed_Subject.getText().toString();
                if (str_health_card_number.equals("")) {
                    Ed_Subject.startAnimation(animationShake);
                } else {
 //                   dialogCheckHealthCardAvailable.dismiss();
                    health_card_number = str_health_card_number;
                    mRequestType = IConstant_WebService.WSR_HealthCardCheckAvailable;
                    onRequest();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogCheckHealthCardAvailable.dismiss();
            }
        });
        dialogCheckHealthCardAvailable.show();
    }
}
