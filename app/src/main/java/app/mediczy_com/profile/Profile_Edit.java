package app.mediczy_com.profile;

import android.Manifest;
import android.app.Dialog;
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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;
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
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.CircularImageView;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.DoctorProfileReq;
import app.mediczy_com.webservicemodel.response.CommonResponse;

/**
 * Created by Prithivi Raj on 14-02-2016.
 */

public class Profile_Edit extends AppCompatActivity implements View.OnClickListener,
        ResponseListener, ProfileImageObserver {

    public static final String ACTION_CLOSE = "app.mediczy.Profile_Edit";
    public static Profile_Edit profile_edit;
    RelativeLayout CoordinateScrollView, Iv_Save;
    Toolbar toolBar;
    CollapsingToolbarLayout collapsingToolbar;
    private CircularImageView Iv_Doctor_Image;
    private ViewPager mViewPager;
    private RelativeLayout prevButton;
    private RelativeLayout nextButton;
    private LinearLayout Ll_View;
    private ImageView Iv_NoImage, ivDegree, ivExp, ivLoc, ivPrice, ivName, ivLastName;
    private EditText ed_doctor_Name, ed_doctor_Last_Name, ed_Degree, ed_Exp, ed_Location, ed_Price,
            ed_service, ed_languageKnown, ed_rgLicence;
    private Button btnSave;
    private ImageLoader imageLoader;
//    private ArrayList<BannerModel> arrayListImage = new ArrayList<BannerModel>();
    private ArrayList<String> array_image_path = new ArrayList<String>();
    private ArrayList<String> array_doctor_banner_id = new ArrayList<String>();
    private  ProfileImageObserver imageObserver;
    private final String FOLDER_NAME = "MediczyPhoto";
    private File destination, output;
    private Uri picUri;
    private int mRequestType = 0;
    private String device_id, redId, ID, ReqType, imagePath="";
    private String responseStatus, responseMsg, resDoctor_Degree, resDoctorName, resDoctorLastName, resDoctorType,
            resServices, resLanguageKnown, resRegistrationLicense, resDoctorExp, resDoctorImage, resDoctorPrice, resDoctorAddress;

    private int pagerPosition = 0;
    private static int PICK_IMAGE = 1;
    private static int CAMERA_REQUEST_2 = 2;
    private final int REQUEST_CAPTURE_PERMISSIONS = 1;
    private final int REQUEST_PICK_PERMISSIONS = 2;
    private boolean isEdited =false;
    ProgressDialog pDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        profile_edit = this;
        imageLoader=new ImageLoader(this);
        imageObserver = (ProfileImageObserver) HomeNavigation.homeNavigation;
        init();

        Iv_Doctor_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(1);
            }
        });
        ed_doctor_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                isEdited =true;
            }
        });
        ed_doctor_Last_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                isEdited =true;
            }
        });
        ed_Degree.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                isEdited =true;
            }
        });
        ed_Exp.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                isEdited =true;
            }
        });
        ed_Location.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                isEdited =true;
            }
        });
        ed_Price.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                isEdited =true;
            }
        });
    }

    private void init() {
        CoordinateScrollView = (RelativeLayout) findViewById(R.id.rlCollapseScroll_edit);
        toolBar = (Toolbar) findViewById(R.id.toolbar1_edit);
        setSupportActionBar(toolBar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.toolbar_activity_color), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Iv_Save = (RelativeLayout)toolBar.findViewById(R.id.rl_toolbar1_edit);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_edit);
        collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setCollapsedTitleTextAppearance(View.SCROLLBAR_POSITION_DEFAULT);

        Ll_View = (LinearLayout) findViewById(R.id.inc_gallery_edit);
        mViewPager = (ViewPager) Ll_View.findViewById(R.id.vd_image_view_pager_image);
        prevButton = (RelativeLayout) Ll_View.findViewById(R.id.vd_image__Left_image);
        nextButton = (RelativeLayout) Ll_View.findViewById(R.id.vd_image__Right_image);
        Iv_NoImage = (ImageView)Ll_View.findViewById(R.id.vd_image_no_image);
        ed_doctor_Last_Name = (EditText)findViewById(R.id.textview_vd_logo_doctor_name_edit_last_name);
        ed_doctor_Name = (EditText)findViewById(R.id.textview_vd_logo_doctor_name_edit);
        ed_Degree = (EditText)findViewById(R.id.ed_vd_full_data_edu);
        ed_Exp = (EditText)findViewById(R.id.ed_vd_full_data_edu_degree_edit);
        ed_Location = (EditText)findViewById(R.id.ed_vd_full_data_loc_edit);
        ed_Price = (EditText)findViewById(R.id.ed_vd_full_data_price_edit);
        ed_service = (EditText) findViewById(R.id.ed_vd_full_data_price_service);
        ed_languageKnown = (EditText) findViewById(R.id.ed_vd_full_data_lang_kn);
        ed_rgLicence = (EditText) findViewById(R.id.ed_vd_full_data_rg_licence);

        btnSave = (Button) findViewById(R.id.saved_edit_pro);
        btnSave.setVisibility(View.GONE);

        ivLastName = (ImageView)findViewById(R.id.imageView1_vd_full_data_edu_edit_icon_last_name);
        ivName = (ImageView)findViewById(R.id.imageView1_vd_full_data_edu_edit_icon_name);
        ivDegree = (ImageView)findViewById(R.id.imageView1_vd_full_data_edu_edit_icon);
        ivExp = (ImageView)findViewById(R.id.imageView1_vd_full_data_edu_degree_edit_icon);
        ivLoc = (ImageView)findViewById(R.id.imageView1_vd_full_data_loc_edit_icon);
        ivPrice = (ImageView)findViewById(R.id.imageView1_vd_full_data_pri_edit_icon);

        Iv_Doctor_Image = (CircularImageView)findViewById(R.id.imageView_vd_doctor_photo_edit);
        Iv_NoImage.setOnClickListener(this);
        Iv_Save.setOnClickListener(this);

        btnSave.setOnClickListener(this);
        ivLastName.setOnClickListener(this);
        ivName.setOnClickListener(this);
        ivDegree.setOnClickListener(this);
        ivExp.setOnClickListener(this);
        ivLoc.setOnClickListener(this);
        ivPrice.setOnClickListener(this);
        ID = LocalStore.getInstance().getUserID(this);
        device_id = Utility.getInstance().getDeviceID(this);
        redId = LocalStore.getInstance().getGcmId(this);
        MLog.e("ID", "" + ID);

        destination = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME);
        if (!destination.exists()) {
            destination.mkdirs();
        }
        ReqType="doctor_profile";
        mRequestType = IConstant_WebService.WSR_DoctorProfile;
        onRequest();
    }

    private Void selectImage(final int i) {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    ImageCapturePermission();
                } else if (items[item].equals("Choose from Gallery")) {
                    ImagePickPermission();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_2) {

                imagePath = getRealPathFromURI(picUri);
                MLog.e("selectedImagePath------->", "" + imagePath);

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                bmOptions.inDither = true;

                Bitmap bmp = BitmapFactory.decodeFile(imagePath, bmOptions);
                Bitmap bitmap2 = getResizedBitmap(bmp, 600);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                //Convert Base64
                byte[] bytes = baos.toByteArray();
                imagePath = Base64.encodeToString(bytes, Base64.DEFAULT);
                Iv_Doctor_Image.setImageBitmap(bitmap2);
                isEdited =true;
            }
            else if (requestCode == PICK_IMAGE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap bitmap1 = BitmapFactory.decodeFile(picturePath);
                Bitmap bitmap2 = getResizedBitmap(bitmap1, 600);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] byteArray = stream.toByteArray();
                cursor.close();
                imagePath = picturePath;
                //Convert Base64
                imagePath = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Iv_Doctor_Image.setImageBitmap(bitmap2);
                isEdited =true;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vd_image_no_image:
                Bundle bundle1 = new Bundle();
                Intent profile = new Intent(Profile_Edit.this, Profile_Edit_Photo.class);
                bundle1.putSerializable("array_image_path", array_image_path);
                bundle1.putSerializable("array_doctor_banner_id", array_doctor_banner_id);
                profile.putExtras(bundle1);
                startActivity(profile);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.saved_edit_pro:
                validate();
                break;
            case R.id.rl_toolbar1_edit:
                validate();
                break;
            case R.id.imageView1_vd_full_data_edu_edit_icon_last_name:
                custom_dialog(ed_doctor_Last_Name, "Enter your last name here...", "Add First Name");
                break;
            case R.id.imageView1_vd_full_data_edu_edit_icon_name:
                custom_dialog(ed_doctor_Name, "Enter your first name here...", "Add Last Name");
                break;
            case R.id.imageView1_vd_full_data_edu_edit_icon:
                custom_dialog(ed_Degree, "Enter your degree here...", "Add Degree");
                break;
            case R.id.imageView1_vd_full_data_edu_degree_edit_icon:
                custom_dialog(ed_Exp, "Enter your experience here...", "Add Experience");
                break;
            case R.id.imageView1_vd_full_data_loc_edit_icon:
                custom_dialog(ed_Location, "Enter your location here...", "Add Location");
                break;
            case R.id.imageView1_vd_full_data_pri_edit_icon:
                custom_dialog(ed_Price, "Enter your price here...", "Add Price");
                break;
        }
    }

    private void validate() {
        String degree = ed_Degree.getText().toString();
        String exp = ed_Exp.getText().toString();
        String location = ed_Location.getText().toString();
        String price = ed_Price.getText().toString();
        String lastName = ed_doctor_Last_Name.getText().toString();
        String firstName = ed_doctor_Name.getText().toString();
        String service = ed_service.getText().toString();
        String languageKnown = ed_languageKnown.getText().toString();
        String rgLicence = ed_rgLicence.getText().toString();

        if (lastName.equals("")){
            Utility.getInstance().showToast("Enter your last name", this);
        }else if (firstName.equals("")){
            Utility.getInstance().showToast("Enter your first name", this);
        }else if (degree.equals("")){
            Utility.getInstance().showToast("Enter your degree", this);
        }else if(exp.equals("")){
            Utility.getInstance().showToast("Enter your experience", this);
        }else if(location.equals("")){
            Utility.getInstance().showToast("Enter your location", this);
        }else if(price.equals("")){
            Utility.getInstance().showToast("Enter your price", this);
        }else if(service.equals("")){
            Utility.getInstance().showToast("Enter your service and specialization", this);
        }else if(languageKnown.equals("")){
            Utility.getInstance().showToast("Enter your language", this);
        }else if(rgLicence.equals("")){
            Utility.getInstance().showToast("Enter your licence", this);
        }else{
            if (Utility.getInstance().isConnectingToInternet(this)) {
/*                Async_Uploading async_image_loading = new Async_Uploading(ReqType);
                async_image_loading.execute();*/
                ReqType="doctor_profile_update";
                mRequestType = IConstant_WebService.WSR_DoctorProfileEdit;
                onRequest();
            } else {
                AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
            }
        }
    }

    private void ImagePickPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(Profile_Edit.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }else {
                ActivityCompat.requestPermissions(Profile_Edit.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PICK_PERMISSIONS);
            }
        }else {
            pickImage();
        }
    }

    private void ImageCapturePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(Profile_Edit.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                imageCapture();
            }else {
                ActivityCompat.requestPermissions(Profile_Edit.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAPTURE_PERMISSIONS);
            }
        }else {
            imageCapture();
        }
    }

    private void imageCapture() {
        String name = Utility.getInstance().dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        output = new File(destination, name + ".jpg");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            picUri = FileProvider.getUriForFile(Profile_Edit.this,
                    Profile_Edit.this.getApplicationContext().getPackageName() + ".provider", output);
        }else {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PICK_PERMISSIONS:
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if( perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    Utility.getInstance().showToast("Access storage permission denied", this);
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
                }else{
                    Utility.getInstance().showToast("Camera permission denied", this);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void custom_dialog(final TextView tv, String hint, final String title) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_add_title);

        final Button btn_add = (Button) dialog.findViewById(R.id.button_popup_add_tittle);
        final EditText Editext_PopUp = (EditText) dialog.findViewById(R.id.editText_popup__add_tittle);
        final TextView tvTitle = (TextView) dialog.findViewById(R.id.textView_popup_add_tittle);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Editext_PopUp, InputMethodManager.SHOW_IMPLICIT);
        Editext_PopUp.setHint(hint);
        tvTitle.setText(title);
        btn_add.setEnabled(true);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String str_ed = Editext_PopUp.getText().toString();
                if (!str_ed.equals("")) {
                    if (title.equalsIgnoreCase("Add Price")){
                        str_ed = "INR "+str_ed;
                    }if (title.equalsIgnoreCase("Add Experience")){
                        str_ed = str_ed+" Year Experience";
                    }
                    tv.setText(str_ed);
                    dialog.dismiss();
                } else {
                    btn_add.setEnabled(false);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            DoctorProfileReq deliveryFormReq = new DoctorProfileReq();
            deliveryFormReq.doctor_id = ID;
            if (ReqType.equals("doctor_profile_update")) {
                String degree = ed_Degree.getText().toString();
                String exp = ed_Exp.getText().toString();
                String location = ed_Location.getText().toString();
                String price = ed_Price.getText().toString();
                String name = ed_doctor_Name.getText().toString();
                String last_name = ed_doctor_Last_Name.getText().toString();
                String service = ed_service.getText().toString();
                String languageKnown = ed_languageKnown.getText().toString();
                String rgLicence = ed_rgLicence.getText().toString();
                price = price.replace("INR", "");
                exp = exp.replace("Year Experience", "");
                deliveryFormReq.firstname = name;
                deliveryFormReq.lastname = last_name;
                deliveryFormReq.degree = degree;
                deliveryFormReq.experience = exp;
                deliveryFormReq.address = location;
                deliveryFormReq.price = price;
                deliveryFormReq.services = service;
                deliveryFormReq.languages = languageKnown;
                deliveryFormReq.licence = rgLicence;
                deliveryFormReq.imagePath = imagePath;
                String requestURL = IConstant_WebService.doctor_profile_update;
                RequestManager.DoctorProfileRequest(this, null, deliveryFormReq,
                        mRequestType, requestURL);
            } else if (ReqType.equals("doctor_profile")) {
                String requestURL = IConstant_WebService.doctor_profile;
                RequestManager.DoctorProfileRequest(this, null, deliveryFormReq,
                        mRequestType, requestURL);
            }
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, false);
        }
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        CommonResponse req = (CommonResponse) responseObj;
        if (req!=null) {
            setData(req);
        } else {
            AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
        }
    }

    private void setData(CommonResponse req) {
        responseStatus = req.status;
        if (ReqType.equals("doctor_profile")) {
            if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)){
                if (req.doctorProfile!=null) {
                    resDoctor_Degree = req.doctorProfile.degree;
                    resDoctorName = req.doctorProfile.firstname;
                    resDoctorLastName = req.doctorProfile.lastname;
                    resDoctorExp = req.doctorProfile.experience;
                    resDoctorPrice = req.doctorProfile.price;
                    resDoctorImage = req.doctorProfile.image_path;
                    resDoctorAddress = req.doctorProfile.address;
                    resServices = req.doctorProfile.services;
                    resLanguageKnown = req.doctorProfile.languages;
                    resRegistrationLicense = req.doctorProfile.licence;
                    if (req.doctor_banners!=null) {
                        if (req.doctor_banners.size() >0) {
                            array_image_path.clear();
                            array_doctor_banner_id.clear();
                            for (int i = 0; i < req.doctor_banners.size(); i++) {
                                String image_path = req.doctor_banners.get(i).image_path;
                                String doctor_banner_id = req.doctor_banners.get(i).doctor_banner_id;
                                array_image_path.add(image_path);
                                array_doctor_banner_id.add(doctor_banner_id);
                            }
                        }
                    }
                    collapsingToolbar.setTitle("" + resDoctorName);
                    ed_Degree.setText("" + resDoctor_Degree);
                    ed_doctor_Name.setText("" + resDoctorName);
                    ed_doctor_Last_Name.setText("" + resDoctorLastName);
                    ed_Exp.setText("" + resDoctorExp);
                    ed_Price.setText(resDoctorPrice);
                    ed_Location.setText("" + resDoctorAddress);
                    ed_service.setText("" + resServices);
                    ed_languageKnown.setText("" + resLanguageKnown);
                    ed_rgLicence.setText("" + resRegistrationLicense);
/*                        ed_Price.setText("INR " +resDoctorPrice);
                        ed_Exp.setText("" + resDoctorExp + " Year Experience");
                        ed_Spec.setText(""+resDoctorSpec);*/
                    imageLoader.DisplayImage(resDoctorImage, Iv_Doctor_Image, 4);
                    ImageGallery_Large();
                    LocalStore.getInstance().setDoctorImage(Profile_Edit.this, resDoctorImage);
                    imageObserver.onImageUpdated(true);
                }
            }else if(responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)){
                responseMsg = req.msg;
                AlertDialogFinish.Show(Profile_Edit.this, responseMsg, true);
            } else {
                AlertDialogFinish.Show(Profile_Edit.this, Constant.Alart_Status500, false);
            }
        } else if (ReqType.equals("doctor_profile_update")) {
            if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)){
                Utility.getInstance().showToast("Profile Updated", this);
                String image = req.image;
                LocalStore.getInstance().setDoctorImage(Profile_Edit.this, image);
                imageObserver.onImageUpdated(true);
                aFinish();
            }else if(responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)){
                responseMsg = req.msg;
                AlertDialogFinish.Show(Profile_Edit.this, responseMsg, true);
            } else {
                AlertDialogFinish.Show(Profile_Edit.this, Constant.Alart_Status500, false);
            }
        }
    }

    @Override
    public void onImageUpdated(boolean status) {
        ReqType="doctor_profile";
        mRequestType = IConstant_WebService.WSR_DoctorProfile;
        onRequest();
    }

    private void ImageGallery_Large() {
        MLog.e("imageUrls", "" + array_image_path);
        try {
            if (array_image_path.size() > 0) {
                // view pager set adapter
                mViewPager.setAdapter(new ImagePagerAdapter_Large(array_image_path));
                mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                    // private  float MIN_SCALE = 0.75f;
                    private float MIN_SCALE = 0.85f;
                    private float MIN_ALPHA = 0.5f;

                    @Override
                    public void transformPage(View page, float position) {
                        // do transformation here
                        int pageWidth = page.getWidth();
                        int pageHeight = page.getHeight();
                        if (position < -1) { // [-Infinity,-1)
                            // This page is way off-screen to the left.
                            page.setAlpha(0);
                        } else if (position <= 1) { // [-1,1]
                            // Modify the default slide transition to shrink the page as well
                            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                            if (position < 0) {
                                page.setTranslationX(horzMargin - vertMargin / 2);
                            } else {
                                page.setTranslationX(-horzMargin + vertMargin / 2);
                            }
                            // Scale the page down (between MIN_SCALE and 1)
                            page.setScaleX(scaleFactor);
                            page.setScaleY(scaleFactor);
                            // Fade the page relative to its size.
                            page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                        } else { // (1,+Infinity]
                            // This page is way off-screen to the right.
                            page.setAlpha(0);
                        }

                    }
                });
                mViewPager.setCurrentItem(pagerPosition);
            }else{
                Iv_NoImage.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE);
                prevButton.setVisibility(View.GONE);
            }

            PageListener pageListener = new PageListener();
            mViewPager.setOnPageChangeListener(pageListener);
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                }
            });
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
            });
            if (array_image_path.size() <= 1) {
                nextButton.setVisibility(View.GONE);
                prevButton.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            MLog.e("onSetImageGallary", "" + e);
        }
    }

    private class ImagePagerAdapter_Large extends PagerAdapter {
        ArrayList<String> imageUrls;
        public ImagePagerAdapter_Large(ArrayList<String> imageUrls1) {
//            imageUrls.clear();
            this.imageUrls = imageUrls1;
            MLog.e("imageUrls_adapter_large", "" + imageUrls);
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            final ImageView imgDisplay;
            final ViewAnimator animator;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.viewpager_image_swipe_activity, container, false);
            imgDisplay = (ImageView) v.findViewById(R.id.image);
            imgDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                }
            });
            imgDisplay.setTag("" + position);
            animator = (ViewAnimator) v.findViewById(R.id.animator);
            animator.setDisplayedChild(1);
            Picasso.with(Profile_Edit.this).load(imageUrls.get(position)).into(imgDisplay, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                    // Index 0 is the image view.
                    animator.setDisplayedChild(0);
                }

                @Override
                public void onError() {
                    // TODO Auto-generated method stub
                    super.onError();
                    animator.setDisplayedChild(0);
//                    Picasso.with(getActivity()).load(imageURL.get(position)).error(R.drawable.image_not_found);
                    imgDisplay.setImageResource(R.drawable.no_image);
                    MLog.e("ImageError", "" + "ImageError");
                }
            });
            imgDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Bundle bundle1 = new Bundle();
                    Intent profile = new Intent(Profile_Edit.this, Profile_Edit_Photo.class);
                    bundle1.putSerializable("array_image_path", array_image_path);
                    bundle1.putSerializable("array_doctor_banner_id", array_doctor_banner_id);
                    profile.putExtras(bundle1);
                    startActivity(profile);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
            ((ViewPager) container).addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    private class PageListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
//            showImageCount(position);
        }
    }

    private void conformSave() {
        new android.app.AlertDialog.Builder(this)
                .setTitle(Constant.Alart_AppName_Internet)
                .setMessage("Do you want to save ?")
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        validate();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                aFinish();
            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                aFinish();
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            aFinish();
            return true;
        }
        return false;
    }

    private void aFinish() {
        finish();
        overridePendingTransition(R.anim.fade_in_1, R.anim.fade_out_1);
    }
}
