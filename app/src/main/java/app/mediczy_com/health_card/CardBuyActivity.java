package app.mediczy_com.health_card;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import app.mediczy_com.BaseActivity;
import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.settings_help.Help_Activity;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.CountryDetails;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.CardBuy_Req;
import app.mediczy_com.webservicemodel.response.CommonResponse;
import app.mediczy_com.webservicemodel.response.Health_Card_ListResponse;

/**
 * Created by Prithivi on 26-03-2017.
 */

public class CardBuyActivity extends BaseActivity implements ResponseListener,
        PaymentCardBuyObserver {

    private Toolbar toolBar;
    private Animation animationShake;
    private EditText edFirstName, edLastName, edEmail, edStreet, edStreet1, edCity,
            edState, edZipCode, edPhone;
    private Spinner Sp_Country;
    private Button btnBuy, btnFQA, btnHealthCard;
    private TextView tvSelectedHealthCard;
    private ProgressDialog pDialog = null;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static CardBuyActivity cardBuyActivity;
    private CardBuy_Req cardBuyReq;
    private int mRequestType = 0;
    private ArrayList<Health_Card_ListResponse> arrayList = new ArrayList<Health_Card_ListResponse>();
    private String strCountry, selectedCardName="a", membership_card_id, selectedCardAmount="", membership_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_buy_activity);
        cardBuyActivity = this;
        setToolBar();
        init();

        btnHealthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardBuyActivity.this, HealthCardListActivity.class);
                intent.putExtra("isFrom", "CardBuyActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        btnFQA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardBuyActivity.this, Help_Activity.class);
                intent.putExtra("isFrom", "CardBuyActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        Sp_Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCountry = CountryDetails.code[position].toString();
                MLog.e("str_CountryName:", "" + strCountry);
//                ShowToast(str_country);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Gson gson = new Gson();
        String json = LocalStore.getInstance().getSelectedCard(this);
        if (json!=null && !json.equals("0")) {
            Health_Card_ListResponse object = gson.fromJson(json, Health_Card_ListResponse.class);
            selectedCard = object.membership_card_id;
            selectedCardAmount = object.amount;
            tvSelectedHealthCard.setText(""+object.name);
        }*/
    }

    private void validate() {
        boolean isEmailValid = false;
        String strFirstName = edFirstName.getText().toString();
        String strLastName =  edLastName.getText().toString();
        String strEmail = edEmail.getText().toString();
        String strStreet = edStreet.getText().toString();
        String strStreet1 = edStreet1.getText().toString();
        String strCity = edCity.getText().toString();
        String strState = edState.getText().toString();
        String strZip = edZipCode.getText().toString();
        String strPhone = edPhone.getText().toString();
        isEmailValid = Utility.getInstance().eMailValidation(strEmail);

        if (strFirstName.equals("")) {
            edFirstName.startAnimation(animationShake);
            edFirstName.setError("Enter FirstName");
        }else if(strLastName.equals("")) {
            edLastName.startAnimation(animationShake);
            edLastName.setError("Enter LastName");
        }else if(strEmail.equals("")) {
            edEmail.startAnimation(animationShake);
            edEmail.setError("Enter Email");
        }else if(!isEmailValid) {
            edEmail.startAnimation(animationShake);
            edEmail.setError("Invalid Email Address");
        }else if(strStreet.equals("")) {
            edStreet.startAnimation(animationShake);
            edStreet.setError("Enter Address");
        }else if(strCity.equals("")) {
            edCity.startAnimation(animationShake);
            edCity.setError("Enter City");
        }else if(strState.equals("")) {
            edState.startAnimation(animationShake);
            edState.setError("Enter State");
        }else if(strZip.equals("")) {
            edZipCode.startAnimation(animationShake);
            edZipCode.setError("Enter ZipCode");
        }else if(strPhone.equals("")) {
            edPhone.startAnimation(animationShake);
            edPhone.setError("Enter Phone Number");
        }else if(selectedCardName.equals("a")) {
            Utility.getInstance().showToast("No card selected !!!", this);
        }else {
            mRequestType = IConstant_WebService.WSR_CardBuyReq;
            onRequest();
        }
    }

    private void setToolBar() {
        toolBar = (Toolbar) findViewById(R.id.tool_bar_cardbuy);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Health Card");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void init() {
        animationShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        edFirstName = (EditText) findViewById(R.id.editText_first_name_card_buy);
        edLastName = (EditText) findViewById(R.id.editText_last_name_card_buy);
        edEmail = (EditText) findViewById(R.id.editText_email_card_buy);
        edStreet = (EditText) findViewById(R.id.editText_street_card_buy);
        edStreet1 = (EditText) findViewById(R.id.editText_street1_card_buy);
        edCity = (EditText) findViewById(R.id.editText_city_card_buy);
        edState = (EditText) findViewById(R.id.editText_state_card_buy);
        edZipCode = (EditText) findViewById(R.id.editText_zip_code_card_buy);
        edPhone = (EditText) findViewById(R.id.editText_phone_card_buy);
        Sp_Country = (Spinner) findViewById(R.id.spinner_country_card_buy);
        btnBuy = (Button) findViewById(R.id.button_card_buy_buy);
        btnFQA = (Button) findViewById(R.id.button_card_buy_faq);
        btnHealthCard = (Button) findViewById(R.id.button_card_buy_select_card);
        tvSelectedHealthCard = (TextView) findViewById(R.id.tv_card_buy_selected_card_value);
        btnHealthCard.setVisibility(View.GONE);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_card_list);
        RecyclerView.LayoutManager mLayoutManager;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayAdapter<String> dataAdapter_country = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, CountryDetails.country);
        dataAdapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_Country.setAdapter(dataAdapter_country);

        String email = Utility.getInstance().getEmail(CardBuyActivity.this);
        if (email!=null)
            edEmail.setText(email);
        edFirstName.setText(LocalStore.getInstance().getFirstName(this));
        String number = LocalStore.getInstance().getPhoneNumber(this);
 //       number = number.replaceAll("+91","");
        number = number.substring(3);
        edPhone.setText(number);
        LocalStore.getInstance().ClearSelectedCard(this);

        Bundle bundle = getIntent().getExtras();
        membership_card_id = bundle.getString("membership_card_id");
        selectedCardAmount = bundle.getString("selectedCardAmount");
        selectedCardName = bundle.getString("selectedCardName");

        tvSelectedHealthCard.setText(""+selectedCardName);
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            if (mRequestType == IConstant_WebService.WSR_CardBuyReq) {
                cardBuyReq = new CardBuy_Req();
                cardBuyReq.first_name = edFirstName.getText().toString();
                cardBuyReq.last_name = edLastName.getText().toString();
                cardBuyReq.email = edEmail.getText().toString();
                cardBuyReq.street_address = edStreet.getText().toString();
                cardBuyReq.street_address1 = edStreet1.getText().toString();
                cardBuyReq.city = edCity.getText().toString();
                cardBuyReq.state = edState.getText().toString();
                cardBuyReq.zip_code = edZipCode.getText().toString();
                cardBuyReq.phone = edPhone.getText().toString();
                cardBuyReq.country = strCountry;
                cardBuyReq.selectedCard = membership_card_id;
                cardBuyReq.selectedCardAmount = selectedCardAmount;
                RequestManager.CardBuyRequest(this, null, cardBuyReq, mRequestType);
            }else if (mRequestType == IConstant_WebService.WSR_NP_health_card_list_Req) {
                RequestManager.CardBuyListRequest(this, null, null, mRequestType);
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
                if (requestType == IConstant_WebService.WSR_CardBuyReq) {
                    String responseSuccess = res.status;
                    String msg = res.msg;
                    MLog.e("responseSuccess", "" + responseSuccess);
                    MLog.e("msg", "" + msg);
                    if (responseSuccess != null) {
                        if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Success)) {
                            setData(res);
                        }
                        if (responseSuccess.equalsIgnoreCase(Constant.Response_Status_Error)) {
                            AlertDialogFinish.Show(this, msg, true);
                        }
                    }
                }
                if (requestType == IConstant_WebService.WSR_NP_health_card_list_Req) {
                    if (res.health_card_list != null) {
                        if (res.health_card_list.size() > 0) {
                            for (int i = 0; i < res.health_card_list.size(); i++) {
                                Health_Card_ListResponse response = new Health_Card_ListResponse();
                                response.membership_card_id = res.health_card_list.get(i).membership_card_id;
                                response.name = res.health_card_list.get(i).name;
                                response.amount = res.health_card_list.get(i).amount;
                                response.benefits = res.health_card_list.get(i).benefits;
                                response.image_path = res.health_card_list.get(i).image_path;
                                if (res.health_card_list.get(i).name.equals("SILVER")) {
                                    response.cardImage = R.drawable.health_card_silver;
                                }else if (res.health_card_list.get(i).name.equals("GOLD")) {
                                    response.cardImage = R.drawable.health_card_gold;
                                }else if (res.health_card_list.get(i).name.equals("PLATINUM")) {
                                    response.cardImage = R.drawable.health_card_platinum;
                                }else if (res.health_card_list.get(i).name.equals("RED")) {
                                    response.cardImage = R.drawable.health_card_red;
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utility.getInstance().showToast(e.toString(), this);
        }
    }

    private void setData(CommonResponse req) {
        try {
            membership_id = req.membership_id;
            movePaymentActivity(req);
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(this, e.toString(), true);
        }
    }

    private void movePaymentActivity(CommonResponse req) {
        MLog.e("membership_id",""+membership_id);
        String id = req.membership_id;
        Intent intent = new Intent(CardBuyActivity.this, PaymentCardBuy.class);
        intent.putExtra("Id", id);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onPaymentReceived(boolean paymentStatus) {
 //       showMessage();
        try {
            LocalStore.getInstance().setHealthCard_Available(this, "yes");
            Intent i = new Intent(this, HealthCard.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
  //          HealthCardListActivity.healthCardListActivity.finish();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.getInstance().showToast( e.toString(), this);
        }
    }

    @Override
    public void onCardSelected(Health_Card_ListResponse object) {
  //      selectedCard = object.membership_card_id;
        selectedCardAmount = object.amount;
       tvSelectedHealthCard.setText(""+object.name);
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
}
