package app.mediczy_com.chat.chat_new;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.adapter.ChatDetailList_Adapter;
import app.mediczy_com.dashboard.Register;
import app.mediczy_com.dashboard.SplashScreen;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.fcm.FCMConfig;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.CircularImageView;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.NotificationUtils;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.ChatDetailModel;
import app.mediczy_com.webservicemodel.response.ChatResponseModel;

/**
 * Created by Prithivi on 08-06-2017.
 */

public class ChatListActivity extends AppCompatActivity implements ResponseListener {

    private ListView mRecyclerView;
    private ProgressBar progressBar;
    private ImageView ivSendMsg;
    private RelativeLayout rlConv_Empty;
    private EditText edText;
    private File destination, output;
    private Uri picUri;
    private Bitmap bitmap;
    private ImageLoader imageLoader;
    private Menu menu;
    private Bitmap bundleImage;
    private ConversationUpdateObserver conversationUpdateObserver;
    private ChatDetailList_Adapter detailAdapter;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<ChatDetailModel> arrayList = new ArrayList<>();

    private final int REQUEST_CAPTURE_PERMISSIONS = 1;
    private final int REQUEST_PICK_PERMISSIONS = 2;
    private static final int REQUEST_PICK_FILE_PERMISSIONS = 3;
    private final String FOLDER_NAME = "MediczyPhoto";
    private static int PICK_IMAGE = 1;
    private static int CAMERA_REQUEST_2 = 2;
    private static final int PICK_FILE_RESULT_CODE = 3;
    private String conversation_id, doctor_id, USerId, MobileNumber, imagePath = "", isFrom="",
            profileImage, profileName, Type, messageType="", ImageEncoded="", blockTittle="", user_typeBundle="";
    private boolean isEdited = false, updateConversation = false;
    byte[] byteArrayImage  = null;

    ProgressDialog pDialog = null;
    RequestCommon requestCommon;
    private int mRequestType = 0, block_menu_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_detail);
        conversationUpdateObserver = (ConversationUpdateObserver) ConversationListActivity.conversationListActivity;
//        registerReceiver(this.appendChatScreenMsgReceiver, new IntentFilter(Constant.ChatAppendMessage));
        getBundleData();
        setToolbar();
        init();

        ivSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSendMsg();
            }
        });

        edText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                //             mRecyclerView.setStackFromBottom(true);
                showListViewBottom();
                return false;
            }
        });

        edText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
     //               ivSendMsg.setVisibility(View.GONE);
                    ivSendMsg.setVisibility(View.VISIBLE);
                } else {
                    ivSendMsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    // checking for type intent filter
                    if (intent.getAction().equals(FCMConfig.REGISTRATION_COMPLETE)) {
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                    } else if (intent.getAction().equals(FCMConfig.PUSH_NOTIFICATION)) {
                        // new push notification is received
                        String message = intent.getStringExtra("message").toString();
                        String current_time  = intent.getStringExtra("current_time").toString();
                        String sender_id = intent.getStringExtra("sender_id").toString();
                        String image = intent.getStringExtra("image").toString();
                        String receiver_id = intent.getStringExtra("receiver_id").toString();
                        String type = intent.getStringExtra("type").toString();
                        String user_type = intent.getStringExtra("user_type").toString();
                        String message_id = intent.getStringExtra("message_id").toString();
                        ChatDetailModel model = new ChatDetailModel();
                        model.setMessage(message);
                        model.setTime(current_time);
                        model.setSender_id(sender_id);
                        model.setImage(image);
                        model.setReceiver_id(receiver_id);
                        model.setType(type);
                        model.setUser_type(user_type);
                        model.setMessage_id(message_id);
                        arrayList.add(model);
                        if (arrayList.size()==1) {
                            detailAdapter = new ChatDetailList_Adapter(ChatListActivity.this, arrayList);
                            mRecyclerView.setAdapter(detailAdapter);
                        }
                        rlConv_Empty.setVisibility(View.GONE);
                        detailAdapter.notifyDataSetChanged();
                        Utility.getInstance().hideKeyboard(ChatListActivity.this);
                        mRecyclerView.setSelection(mRecyclerView.getCount() - 1);
                        edText.setText("");
                        updateConversation = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(FCMConfig.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(FCMConfig.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void getBundleData() {
        Utility.getInstance().hideKeyboardNew(ChatListActivity.this);
        Bundle bundle = getIntent().getExtras();
        conversation_id = bundle.getString("conversation_id");
        profileName = bundle.getString("name");
        profileImage = bundle.getString("image");
        doctor_id = bundle.getString("doctor_id");
        user_typeBundle = bundle.getString("user_type");
//        bundleImage = getIntent().getParcelableExtra("BitmapImage");
        byteArrayImage  =  bundle.getByteArray("ImageByteArray");
        isFrom = bundle.getString("isFrom");
        MLog.e("isFrom->","" + isFrom);
        MLog.e("conversation_id","" + conversation_id);
        MLog.e("doctor_id","" + doctor_id);
        MLog.e("user_typeBundle->","" + user_typeBundle);
        loadBundleImage();
    }

    private void loadBundleImage() {
        new Thread(new Runnable() {
            public void run() {
                if (user_typeBundle.equalsIgnoreCase("doctor"))
                    bundleImage = BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.length);
            }
        }).start();
    }

    private void setToolbar() {
        imageLoader = new ImageLoader(ChatListActivity.this);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        RelativeLayout mCustomView = (RelativeLayout) findViewById(R.id.chat_tool_action_chat);
        TextView tvName = (TextView) mCustomView.findViewById(R.id.textView__custom_action_bar_chat);
        final CircularImageView iV = (CircularImageView) mCustomView.findViewById(R.id.iv_left_chat_detail_msg_chat);
        TextView tvNameFirstLetter = (TextView) mCustomView.findViewById(R.id.tv_left_chat_detail_msg_chat_name);
        ImageView iVBack = (ImageView) mCustomView.findViewById(R.id.iv_back_chat_detail_msg_chat);
        tvName.setText(profileName);
        if (user_typeBundle.equalsIgnoreCase("patient")) {
            tvNameFirstLetter.setText(Utility.getInstance().getFirstChar(profileName));
            tvNameFirstLetter.setVisibility(View.VISIBLE);
            iV.setVisibility(View.GONE);
        } else if (user_typeBundle.equalsIgnoreCase("doctor")) {
            iV.setImageBitmap(bundleImage);
            iV.setVisibility(View.VISIBLE);
            tvNameFirstLetter.setVisibility(View.GONE);
        }

        iVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
    }

    private void validateSendMsg() {
        String strText = edText.getText().toString().trim();
        if (strText.equals("")) {
            Utility.getInstance().showToast("Please type message to send", ChatListActivity.this);
        } else {
            mRequestType = IConstant_WebService.WSR_ChatSendMessage;
            messageType = "TEXT";
            onRequest();
        }
    }

    private void init() {
        mRecyclerView = (ListView) findViewById(R.id.recycler_chat_detail_list);
        progressBar = (ProgressBar) findViewById(R.id.progress_loading_chat_detail);
        progressBar.setVisibility(View.GONE);

        rlConv_Empty = (RelativeLayout) findViewById(R.id.rl_chat_list_empty);
        ivSendMsg = (ImageView) findViewById(R.id.iv_chat_detail_send_message);
        edText = (EditText) findViewById(R.id.ed_chat_detail_send_message);
        rlConv_Empty.setVisibility(View.GONE);

        MobileNumber = LocalStore.getInstance().getPhoneNumber(this);
        USerId = LocalStore.getInstance().getUserID(this);
        Type = LocalStore.getInstance().getType(this);
        destination = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME);
        if (!destination.exists()) {
            destination.mkdirs();
        }
//        setLocalData();
        mRequestType = IConstant_WebService.WSR_ChatListReq;
        onRequest();
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(ChatListActivity.this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            requestCommon.userId = USerId;
            requestCommon.type = Type;
            requestCommon.doctor_id = doctor_id;
            requestCommon.conversation_id = conversation_id;
            if (mRequestType == IConstant_WebService.WSR_ChatListReq) {
                if (isFrom.equalsIgnoreCase("ViewDetail")) {
                    String url = IConstant_WebService.initiate_conversation;
                    RequestManager.ChatList(ChatListActivity.this, null, requestCommon, mRequestType, url);
                } else {
                    String url = IConstant_WebService.chat_list;
                    RequestManager.ChatList(ChatListActivity.this, null, requestCommon, mRequestType, url);
                }
            } else if (mRequestType == IConstant_WebService.WSR_ChatSendMessage) {
                requestCommon.message_type = messageType;
                requestCommon.message = edText.getText().toString().trim();
                requestCommon.current_time = Utility.getInstance().getCurrentTime();
                requestCommon.image = ImageEncoded;
                if (messageType.equalsIgnoreCase("IMAGE")) {
                    requestCommon.message = ImageEncoded;
                }
                RequestManager.ChatSendMessage(ChatListActivity.this, null, requestCommon, mRequestType);
            } else if (mRequestType == IConstant_WebService.WSR_ChatBlockReq) {
                requestCommon.status = blockTittle;
                requestCommon.current_time = Utility.getInstance().getCurrentTime();
                RequestManager.ChatBlock(ChatListActivity.this, null, requestCommon, mRequestType);
            } else if (mRequestType == IConstant_WebService.WSR_ChatClearReq) {
                requestCommon.current_time = Utility.getInstance().getCurrentTime();
                RequestManager.ChatClear(ChatListActivity.this, null, requestCommon, mRequestType);
            }
        } else {
            AlertDialogFinish.Show(ChatListActivity.this, Constant.Alart_Internet, true);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        ChatResponseModel res = (ChatResponseModel) responseObj;
        if (res != null) {
            setData(res, requestType);
        } else {
            AlertDialogFinish.Show(ChatListActivity.this, Constant.Alart_Status500, true);
        }
    }

    private void setData(ChatResponseModel res, int requestType) {
        try {
            if (requestType == IConstant_WebService.WSR_ChatListReq) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    if (isFrom.equalsIgnoreCase("ViewDetail")) {
                        conversation_id = res.conversation_id;
                        MLog.e("conversation_id :", "" + conversation_id);
                    }
                    setMenu(res);
                    if (res.chatListModel != null) {
                        arrayList.clear();
                        rlConv_Empty.setVisibility(View.GONE);
                        if (res.chatListModel.size() > 0)
                        {
                            for (int i = 0; i < res.chatListModel.size(); i++) {
                                ChatDetailModel model = new ChatDetailModel();
                                model.setMessage(res.chatListModel.get(i).message);
                                model.setTime(res.chatListModel.get(i).created_at);
                                model.setSender_id(res.chatListModel.get(i).sender_id);
                                model.setName(res.chatListModel.get(i).name);
                                model.setImage(res.chatListModel.get(i).image_path);
                                model.setReceiver_id(res.chatListModel.get(i).receiver_id);
                                model.setType(res.chatListModel.get(i).type);
                                model.setUser_type(res.chatListModel.get(i).user_type);
                                model.setMessage_id(res.chatListModel.get(i).message_id);
                                arrayList.add(model);
                            }
                            detailAdapter = new ChatDetailList_Adapter(ChatListActivity.this, arrayList);
                            mRecyclerView.setAdapter(detailAdapter);
                            showListViewBottom();
                        } else {
                            Utility.getInstance().showToast("Chat List is Empty", ChatListActivity.this);
                        }
                    }
                }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    rlConv_Empty.setVisibility(View.VISIBLE);
                    setMenu(res);
                } else {
                    AlertDialogFinish.Show(ChatListActivity.this, Constant.Alart_Status500, true);
                }
            } else if(requestType == IConstant_WebService.WSR_ChatSendMessage) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    ChatDetailModel model = new ChatDetailModel();
                    model.setMessage(res.message);
                    model.setTime(res.created_at);
                    model.setSender_id(res.sender_id);
                    model.setImage(res.image_path);
                    model.setReceiver_id(res.receiver_id);
                    model.setType(res.type);
                    model.setUser_type(res.user_type);
                    model.setMessage_id(res.message_id);
                    arrayList.add(model);
                    if (arrayList.size()==1) {
                        detailAdapter = new ChatDetailList_Adapter(ChatListActivity.this, arrayList);
                        mRecyclerView.setAdapter(detailAdapter);
                    }
                    rlConv_Empty.setVisibility(View.GONE);
                    detailAdapter.notifyDataSetChanged();
                    Utility.getInstance().hideKeyboard(ChatListActivity.this);
                    mRecyclerView.setSelection(mRecyclerView.getCount() - 1);
                    edText.setText("");
                    updateConversation = true;
                }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    AlertDialogFinish.Show(ChatListActivity.this, res.msg, true);
                }else {
                    AlertDialogFinish.Show(ChatListActivity.this, Constant.Alart_Status500, true);
                }
            } else if (requestType == IConstant_WebService.WSR_ChatBlockReq) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    MenuItem item = menu.findItem(block_menu_id);
                    SpannableString spanString = new SpannableString(item.getTitle().toString());
                    spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0);
                    blockTittle = res.msg;
                    if (res.msg.equalsIgnoreCase("un_blocked")) {
                        blockTittle = "Blocked";
                        item.setTitle("Block");
                    }else if(res.msg.equalsIgnoreCase("Blocked")) {
                        blockTittle = "un_blocked";
                        item.setTitle("Unblock");
                    }
                }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    AlertDialogFinish.Show(ChatListActivity.this, res.msg, true);
                }else {
                    AlertDialogFinish.Show(ChatListActivity.this, Constant.Alart_Status500, true);
                }
            } else if (requestType == IConstant_WebService.WSR_ChatClearReq) {
                if (res.status.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    if (arrayList.size()!=0) {
                        arrayList.clear();
                        detailAdapter.notifyDataSetChanged();
                        rlConv_Empty.setVisibility(View.VISIBLE);
                        updateConversation = true;
                    }
                }else if (res.status.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    AlertDialogFinish.Show(ChatListActivity.this, res.msg, true);
                }else {
                    AlertDialogFinish.Show(ChatListActivity.this, Constant.Alart_Status500, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(ChatListActivity.this, e.toString(), true);
        }
    }

    private void setMenu(ChatResponseModel res) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.chat_block) {
                SpannableString spanString = new SpannableString(item.getTitle().toString());
                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0);
                if (res.block_status != null && !res.block_status.equals("")) {
                    if (res.block_status.equalsIgnoreCase("un_blocked")) {
                        blockTittle = "Blocked";
                        item.setTitle("Block");
                    } else if (res.block_status.equalsIgnoreCase("Blocked")) {
                        blockTittle = "un_blocked";
                        item.setTitle("Unblock");
                    } else {
                        Utility.getInstance().showToast(res.block_status, ChatListActivity.this);
                    }
                } else {
                    Utility.getInstance().showToast(res.block_status, ChatListActivity.this);
                }
            }
        }
    }

    private void setLocalData() {
        for (int i = 0; i < 60; i++) {
            ChatDetailModel model = new ChatDetailModel();
            model.setTime("9:55 PM");
            model.setDoctor_id(conversation_id);
            model.setName("Prithivi");
            model.setProfile_image("images30.jpg");
            model.setFile_extension("pdf");
            model.setFile_name("Sample File.pdf");
            if (i == 2 || i == 4 || i == 6 || i == 9 || i == 11 || i == 13 || i == 17 | i == 33 | i == 45) {
                model.setPatient_id(USerId);
                model.setType("message");
                model.setMessage("This is the sample message for testing the mediczy app " +
                        "chat_bot_one to chat_bot_one chat integration waiting for API");
            } else {
                model.setType("text");
                model.setPatient_id("1000");
                model.setMessage("Hello World this is Mediczy.");
                if (i == 20 || i == 24 || i == 26 || i == 30 || i == 38 || i == 40) {
                    model.setImage("images30.jpg");
                    model.setType("image");
                }
                if (i == 50 || i == 54 || i == 56 || i == 58) {
                    model.setImage("images30.jpg");
                    model.setType("file");
                }
            }
            arrayList.add(model);
        }
        detailAdapter = new ChatDetailList_Adapter(ChatListActivity.this, arrayList);
        mRecyclerView.setAdapter(detailAdapter);
        showListViewBottom();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_2) {
                imagePath = Utility.getInstance().getRealPathFromURI(picUri, ChatListActivity.this);
                MLog.e("selectedImagePath------->", "" + imagePath);

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                bmOptions.inDither = true;

                Bitmap bmP = BitmapFactory.decodeFile(imagePath, bmOptions);
                Bitmap bmp = Utility.getInstance().getResizedBitmap(bmP, 600);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                ImageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                bitmap = bmp;
//                setBitmap(bitmap);
                mRequestType = IConstant_WebService.WSR_ChatSendMessage;
                messageType = "IMAGE";
                onRequest();
            } else if (requestCode == PICK_IMAGE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap bmP = BitmapFactory.decodeFile(picturePath);
                Bitmap bmp = Utility.getInstance().getResizedBitmap(bmP, 600);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                ImageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                cursor.close();
                imagePath = picturePath;
                bitmap = bmp;
//                setBitmap(bitmap);
                mRequestType = IConstant_WebService.WSR_ChatSendMessage;
                messageType = "IMAGE";
                onRequest();
            } else if (requestCode == PICK_FILE_RESULT_CODE) {
                String FilePath = data.getData().getPath();
                MLog.e("FilePath", "FilePath: " + FilePath);
                setFile(FilePath);
            }
        }
    }

    private void setFile(String filePath) {
        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
        MLog.e("fileName", "fileName: " + filename);
        if (filename.endsWith(".txt") || filename.endsWith(".doc") || filename.endsWith(".DOC")
                || filename.endsWith(".docx") || filename.endsWith(".DOCX")) {
            addFile("docx", filename);
        } else if (filename.endsWith(".pdf")) {
            addFile("pdf", filename);
        } else {
            Utility.getInstance().showToast("This file format is not supported", ChatListActivity.this);
        }
    }

    private void addFile(String fileExtension, String filename) {
        ChatDetailModel model = new ChatDetailModel();
        model.setMessage("");
        model.setTime(Utility.getInstance().getCurrentTime());
        model.setDoctor_id(conversation_id);
        model.setName(LocalStore.getInstance().getFirstName(ChatListActivity.this));
        model.setImage("images30.jpg");
        model.setType("file");
        model.setFile_extension(fileExtension);
        model.setFile_name(filename);
        model.setPatient_id(USerId);
        arrayList.add(model);
        detailAdapter.notifyDataSetChanged();
        Utility.getInstance().hideKeyboard(ChatListActivity.this);
        mRecyclerView.setSelection(mRecyclerView.getCount() - 1);
        edText.setText("");
        Utility.getInstance().showToast("File Sent", ChatListActivity.this);
    }

    private void setBitmap(Bitmap bitmap) {
        ChatDetailModel model = new ChatDetailModel();
        model.setMessage("Hi");
        model.setDoctor_id(conversation_id);
        model.setName(LocalStore.getInstance().getFirstName(ChatListActivity.this));
        model.setImage("images30.jpg");
        model.setPatient_id(USerId);
        model.setTime(Utility.getInstance().getCurrentTime());
        model.setType("bitmap");
        model.setBitmap(bitmap);
        arrayList.add(model);
        detailAdapter.notifyDataSetChanged();
        Utility.getInstance().hideKeyboard(ChatListActivity.this);
        mRecyclerView.setSelection(mRecyclerView.getCount() - 1);
        isEdited = true;
    }

    private void ImagePickPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(ChatListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                ActivityCompat.requestPermissions(ChatListActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PICK_PERMISSIONS);
            }
        } else {
            pickImage();
        }
    }

    private void FilePickPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(ChatListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                FilePick();
            } else {
                ActivityCompat.requestPermissions(ChatListActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PICK_PERMISSIONS);
            }
        } else {
            FilePick();
        }
    }

    private void ImageCapturePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(ChatListActivity.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                imageCapture();
            } else {
                ActivityCompat.requestPermissions(ChatListActivity.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAPTURE_PERMISSIONS);
            }
        } else {
            imageCapture();
        }
    }

    private void imageCapture() {
        String name = Utility.getInstance().dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        output = new File(destination, name + ".jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            picUri = FileProvider.getUriForFile(ChatListActivity.this,
                    ChatListActivity.this.getApplicationContext().getPackageName() + ".provider", output);
        } else {
            picUri = Uri.fromFile(output);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, CAMERA_REQUEST_2);
    }

    private void pickImage() {
        Intent ia = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(ia, PICK_IMAGE);
    }

    private void FilePick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_RESULT_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PICK_PERMISSIONS:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    Utility.getInstance().showToast("Access storage permission denied", ChatListActivity.this);
                }
                break;
            case REQUEST_CAPTURE_PERMISSIONS:
                Map<String, Integer> permsCapture = new HashMap<String, Integer>();
                permsCapture.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    permsCapture.put(permissions[i], grantResults[i]);
                if (permsCapture.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    imageCapture();
                } else {
                    Utility.getInstance().showToast("Camera permission denied", ChatListActivity.this);
                }
                break;
            case REQUEST_PICK_FILE_PERMISSIONS:
                Map<String, Integer> permsFile = new HashMap<String, Integer>();
                permsFile.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    permsFile.put(permissions[i], grantResults[i]);
                if (permsFile.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    FilePick();
                } else {
                    Utility.getInstance().showToast("Access storage permission denied", ChatListActivity.this);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat_file_select:
                selectImage();
                return true;
            case R.id.clear_chat:
                if (arrayList.size()!=0) {
                    mRequestType = IConstant_WebService.WSR_ChatClearReq;
                    onRequest();
                }else {
                    Utility.getInstance().showToast("No message to clear", ChatListActivity.this);
                }
                return true;
            case R.id.chat_block:
                block_menu_id = R.id.chat_block;
                mRequestType = IConstant_WebService.WSR_ChatBlockReq;
                onRequest();
                return true;
            case android.R.id.home:
                finishActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0); //fix the color to white
        //    item.setTitle("Unblocked");
/*            if (block_type.equalsIgnoreCase("Blocked")) {
                item.setTitle("Un Blocked");
            }else if (block_type.equalsIgnoreCase("Unblocked")) {
                item.setTitle("Blocked");
            }*/
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void finishActivity() {
        if (updateConversation) {
            if (isFrom.equalsIgnoreCase("ConversationListActivity")) {
                conversationUpdateObserver.onUpdate(true);
            }
        }
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            finishActivity();
            return true;
        }
        return false;
    }

    private Void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    ImageCapturePermission();
                } else if (items[item].equals("Choose from Gallery")) {
                    ImagePickPermission();
                } else if (items[item].equals("Select File")) {
                    FilePickPermission();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
        return null;
    }

    private void showListViewBottom() {
        mRecyclerView.post(new Runnable() {
            public void run() {
                mRecyclerView.setSelection(mRecyclerView.getCount() - 1);
            }
        });
    }

    public void imagePicasso(final String imagePath, final ImageView iV) {
        Picasso.with(this).load(imagePath).
                into(iV, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub
                        super.onError();
//                        Picasso.with(ChatListActivity.this).load(imagePath).error(R.drawable.no_image_1);
                        iV.setImageResource(R.drawable.no_image_1);
                        MLog.e("ImageError", "" + "ImageError");
                    }
                });
    }

/*    BroadcastReceiver appendChatScreenMsgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            if (b != null) {
                String message = b.getString("message").toString();
                String current_time  = b.getString("current_time").toString();
                String sender_id = b.getString("sender_id").toString();
                String image = b.getString("image").toString();
                String receiver_id = b.getString("receiver_id").toString();
                String type = b.getString("type").toString();

                ChatDetailModel model = new ChatDetailModel();
                model.setMessage(message);
                model.setTime(current_time);
                model.setSender_id(sender_id);
                model.setImage(image);
                model.setReceiver_id(receiver_id);
                model.setType(type);
                arrayList.add(model);
                if (arrayList.size()==1) {
                    detailAdapter = new ChatDetailList_Adapter(ChatListActivity.this, arrayList);
                    mRecyclerView.setAdapter(detailAdapter);
                }
                rlConv_Empty.setVisibility(View.GONE);
                detailAdapter.notifyDataSetChanged();
                Utility.getInstance().hideKeyboard(ChatListActivity.this);
                mRecyclerView.setSelection(mRecyclerView.getCount() - 1);
                edText.setText("");
                updateConversation = true;
            }
        }
    };*/
}
