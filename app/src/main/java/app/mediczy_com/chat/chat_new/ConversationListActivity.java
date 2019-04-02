package app.mediczy_com.chat.chat_new;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import app.mediczy_com.BaseActivity;
import app.mediczy_com.R;
import app.mediczy_com.adapter.ChatConversation_Adapter;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.ChatDetailModel;
import app.mediczy_com.webservicemodel.response.ChatResponseModel;

/**
 * Created by Prithivi on 08-06-2017.
 */

public class ConversationListActivity extends BaseActivity implements ResponseListener,
        ConversationDeleteObserver, ConversationUpdateObserver {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RelativeLayout rlConv_Empty;
    private ChatConversation_Adapter detailAdapter;
    private ArrayList<ChatDetailModel> arrayList = new ArrayList<>();
    public static ConversationListActivity conversationListActivity;
    private String Id, USerId, MobileNumber, Type, ConversationId="";
    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0, ConversationDeletePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_conversation_list);
        conversationListActivity = this;
        setToolbar();
        init();
    }

    private void setToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_chat_conversation_list);
        rlConv_Empty = (RelativeLayout) findViewById(R.id.rl_conversation_list_empty);
        rlConv_Empty.setVisibility(View.GONE);
//        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ConversationListActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Bundle bundle = getIntent().getExtras();
        Id = bundle.getString("Id");

        LocalStore.getInstance().setDoctorDetailId(this, Id);
        MobileNumber = LocalStore.getInstance().getPhoneNumber(this);
        USerId = LocalStore.getInstance().getUserID(this);
        Type = LocalStore.getInstance().getType(this);

//        setLocalData();
        mRequestType = IConstant_WebService.WSR_ConversationListReq;
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(ConversationListActivity.this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            if (mRequestType == IConstant_WebService.WSR_ConversationListReq) {
                requestCommon.id = USerId;
                requestCommon.type = Type;
                RequestManager.ConversationList(ConversationListActivity.this, null, requestCommon, mRequestType);
            }else if(mRequestType == IConstant_WebService.WSR_ConversationDeleteReq) {
                requestCommon.id = USerId;
                requestCommon.type = Type;
                requestCommon.conversation_id = ConversationId;
                RequestManager.ConversationDelete(ConversationListActivity.this, null, requestCommon, mRequestType);
            }
        } else {
            AlertDialogFinish.Show(ConversationListActivity.this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        ChatResponseModel res = (ChatResponseModel) responseObj;
        setData(res, requestType);
    }

    private void setData(ChatResponseModel res, int requestType) {
        try {
            if (res != null) {
                if (requestType == IConstant_WebService.WSR_ConversationListReq) {
                    if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        if (res.conversationListModel != null) {
                            arrayList.clear();
                            rlConv_Empty.setVisibility(View.GONE);
                            if (res.conversationListModel.size() >0) {
                                for (int i = 0; i < res.conversationListModel.size(); i++) {
                                    ChatDetailModel model = new ChatDetailModel();
                                    model.setId(res.conversationListModel.get(i).conversation_id);
                                    model.setMessage(res.conversationListModel.get(i).message);
                                    model.setTime(res.conversationListModel.get(i).updated_at);
                                    if (Type.equalsIgnoreCase("Patient")) {
                                        model.setSender_id(res.conversationListModel.get(i).doctor_id);
                                    } else if (Type.equalsIgnoreCase("Doctor")) {
                                        model.setSender_id(res.conversationListModel.get(i).patient_id);
                                    }
                                    model.setName(res.conversationListModel.get(i).name);
                                    model.setImage(res.conversationListModel.get(i).image);
                                    model.setProfile_image(res.conversationListModel.get(i).profile_image);
                                    model.setReceiver_id(res.conversationListModel.get(i).receiver_id);
                                    model.setType(res.conversationListModel.get(i).type);
          //                          model.setUser_type("patient");
                                    model.setUser_type(res.conversationListModel.get(i).user_type);
                                    model.setCategory_type(res.conversationListModel.get(i).doctor_category);
                                    model.setRead_status(res.conversationListModel.get(i).message_seen);
                                    model.setBlock_type(res.conversationListModel.get(i).block_status);
                                    arrayList.add(model);
                                }
                                detailAdapter = new ChatConversation_Adapter(ConversationListActivity.this, arrayList);
                                mRecyclerView.setAdapter(detailAdapter);
                                Utility.getInstance().runLayoutAnimation(mRecyclerView, this);
                            } else {
                                Utility.getInstance().showToast("Conversation List is Empty", ConversationListActivity.this);
                            }
                        }
                    }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        rlConv_Empty.setVisibility(View.VISIBLE);
                    }
                }else if (requestType == IConstant_WebService.WSR_ConversationDeleteReq) {
                    if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                        arrayList.remove(ConversationDeletePosition);
                        detailAdapter.notifyDataSetChanged();
                        String msg = res.msg;
                        Utility.getInstance().showToast(msg, this);
                    } else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                        AlertDialogFinish.Show(ConversationListActivity.this, res.msg, true);
                    }
                }
            }else {
                AlertDialogFinish.Show(ConversationListActivity.this, Constant.Alart_Status500, true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(ConversationListActivity.this, e.toString(), true);
        }
    }

    private void deleteConversationPopUp(final int position) {
        new AlertDialog.Builder(ConversationListActivity.this)
                .setTitle(Constant.Alart_AppName_Internet)
                .setMessage("Are you sure want to delete conversation?")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ConversationDeletePosition = position;
                        ConversationId = arrayList.get(position).getId();
                        mRequestType = IConstant_WebService.WSR_ConversationDeleteReq;
                        onRequest();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void setLocalData() {
        for (int i =0; i<20; i++) {
            ChatDetailModel model = new ChatDetailModel();
            model.setMessage("This is the sample message for testing the mediczy app " +
                    "chat_bot_one to chat_bot_one chat integration waiting for API");
            model.setTime("9:55 PM");
            model.setDoctor_id(Id);
            model.setName("Prithivi");
            model.setImage("images30.jpg");
            model.setProfile_image("images30.jpg");
            model.setReceiver_id("123");
            model.setRead_status("0");
            model.setBlock_type("un_blocked");
            model.setType("message");
            model.setCategory_type("General");
            arrayList.add(model);
            arrayList.add(model);
        }
        detailAdapter = new ChatConversation_Adapter(ConversationListActivity.this, arrayList);
        mRecyclerView.setAdapter(detailAdapter);
        Utility.getInstance().runLayoutAnimation(mRecyclerView, this);
    }

    @Override
    public void onConversationClicked(final int position, ImageView iV) {
        MLog.e("position", "" + position);
        Bitmap bitmap;
        byte[] byteArray  = null;
        if (iV!=null) {
            bitmap = Utility.getInstance().getBitmap(iV);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            byteArray = byteArrayOutputStream .toByteArray();
        }
        Handler handler = new Handler();
        final byte[] finalByteArray = byteArray;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveChatListActivity(position, finalByteArray);
            }
        }, 200);
    }

    private void moveChatListActivity(int position, byte[] byteArray) {
        Intent i = new Intent(ConversationListActivity.this, ChatListActivity.class);
        i.putExtra("conversation_id", arrayList.get(position).getId());
        i.putExtra("doctor_id", arrayList.get(position).getId());
        i.putExtra("name", arrayList.get(position).getName());
        i.putExtra("image", arrayList.get(position).getProfile_image());
        i.putExtra("user_type", arrayList.get(position).getUser_type());
//        i.putExtra("BitmapImage", bitmap);
        i.putExtra("ImageByteArray", byteArray);
        i.putExtra("isFrom", "ConversationListActivity");
        startActivity(i);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onConversationLongClicked(int position) {
        deleteConversationPopUp(position);
    }

    @Override
    public void onUpdate(boolean status) {
        mRequestType = IConstant_WebService.WSR_ConversationListReq;
        onRequest();
    }
}
