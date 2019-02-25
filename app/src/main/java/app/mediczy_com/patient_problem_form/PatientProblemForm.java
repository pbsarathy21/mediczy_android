package app.mediczy_com.patient_problem_form;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import app.mediczy_com.BaseActivity;
import app.mediczy_com.DoctorsList;
import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.TouchImageView;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.viewdetail.ViewDetail;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.RequestCommon;
import app.mediczy_com.webservicemodel.response.CommonResponse;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Prithivi on 30-10-2016.
 */

public class PatientProblemForm extends BaseActivity implements View.OnClickListener, ResponseListener {

    private Spinner sp_gender;
    private Button Btn_Submit;
    private ImageView addImageView, BtnAddPhoto;
    private EditText ed_age, e_height, ed_weight, ed_city, ed_question;
    private RadioButton Rb_main_yes, Rb_main_no, Rb_diag_yes, Rb_diag_no, Rb_med_no,
            Rb_med_yes, Rb_allerg_no, Rb_allerg_yes;
    private ScrollView sc;
    private TextView TvCategory;

    private String responseStatus = "", responseQuestion, responseCategory, responseGender,
            responseAge, ReqType = "", responseHeight, responseWeight, responseCity, responseDiag, responseMsg,
            responseMed, responseAleg, responseQuestionType, time_date_id, categoryName,
            Ph_Number, redId, ID, device_id, doctor_id, imagePath, imageSelected;

    private ProgressDialog pDialog = null;
    private RequestCommon requestCommon;
    private int mRequestType = 0;
    private final int REQUEST_CAPTURE_PERMISSIONS = 1;
    private final int REQUEST_PICK_PERMISSIONS = 2;
    private File destination, output;
    private Uri picUri;
    private static int PICK_IMAGE = 1;
    private static int CAMERA_REQUEST_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_problem_page);

        setToolbar();
        init();

        sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                responseGender = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        BtnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BitmapDrawable drawable = (BitmapDrawable) addImageView.getDrawable();
                    if (drawable != null) {
                        Bitmap bitmap = drawable.getBitmap();
                        showImage(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Patient Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Rb_main_yes = (RadioButton) findViewById(R.id.you);
        Rb_main_no = (RadioButton) findViewById(R.id.someelse);
        Rb_diag_yes = (RadioButton) findViewById(R.id.radioButton_register_yes);
        Rb_diag_no = (RadioButton) findViewById(R.id.radioButton_register_no);
        Rb_med_no = (RadioButton) findViewById(R.id.radioButton_medication_no);
        Rb_med_yes = (RadioButton) findViewById(R.id.radioButton_medication_yes);
        Rb_allerg_no = (RadioButton) findViewById(R.id.radioButton_allergy_no);
        Rb_allerg_yes = (RadioButton) findViewById(R.id.radioButton_allergy_yes);
        Btn_Submit = (Button) findViewById(R.id.submit_patient_from);
        BtnAddPhoto = (ImageView) findViewById(R.id.button_patient_problem_page_add_image);
        addImageView = (ImageView) findViewById(R.id.imageView_patient_problem_page_add_image);
        sc=(ScrollView)findViewById(R.id.scrool);
        TvCategory = (TextView) findViewById(R.id.caterory_selected);
        ed_age = (EditText) findViewById(R.id.editText_age);
        e_height = (EditText) findViewById(R.id.editText_height);
        ed_weight = (EditText) findViewById(R.id.editText_weight);
        ed_city = (EditText) findViewById(R.id.editText_city);
        ed_question = (EditText) findViewById(R.id.question);
        sp_gender = (Spinner) findViewById(R.id.spinner_gender);
        addImageView.setVisibility(View.GONE);
        Rb_main_yes.setOnClickListener(this);
        Rb_main_no.setOnClickListener(this);
        Rb_diag_yes.setOnClickListener(this);
        Rb_diag_no.setOnClickListener(this);
        Rb_med_no.setOnClickListener(this);
        Rb_med_yes.setOnClickListener(this);
        Rb_allerg_no.setOnClickListener(this);
        Rb_allerg_yes.setOnClickListener(this);
        Btn_Submit.setOnClickListener(this);

        ArrayAdapter<String> dataAdapter_gender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.gender_list));
        dataAdapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gender.setAdapter(dataAdapter_gender);

        Bundle bundle = getIntent().getExtras();
        doctor_id = bundle.getString("id");
        time_date_id = bundle.getString("time_date_id");
        categoryName = bundle.getString("categoryName");

        device_id = Utility.getInstance().getDeviceID(this);
        redId = LocalStore.getInstance().getGcmId(this);
        Ph_Number = LocalStore.getInstance().getPhoneNumber(this);
        ID = LocalStore.getInstance().getUserID(this);
        responseCategory = LocalStore.getInstance().getCategory(this);
        TvCategory.setText(responseCategory);

        defaultSelected();
        mRequestType = IConstant_WebService.WSR_PatientProblemForm;
    }

    private void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            requestCommon = new RequestCommon();
            requestCommon.appointment_for = responseQuestionType;
            requestCommon.doctor_id = doctor_id;
            requestCommon.catergory_id = responseCategory;
            requestCommon.patient_mobile_number = Ph_Number;
            requestCommon.issue = responseQuestion;
            requestCommon.gender = responseGender;
            requestCommon.age = responseAge;
            requestCommon.weight = responseWeight;
            requestCommon.height = responseHeight;
            requestCommon.city = responseCity;
            requestCommon.previous_diagnosed = responseDiag;
            requestCommon.medications = responseMed;
            requestCommon.timeslot_id = time_date_id;
            requestCommon.allergies = responseAleg;
            requestCommon.patient_id = ID;
            requestCommon.categoryName = categoryName;
            requestCommon.image  = imageSelected;
            RequestManager.PatientProblemForm(this, null, requestCommon, mRequestType);
        }else {
            AlertDialogFinish.Show(this, Constant.Alart_Internet, true);
        }
    }

    private void defaultSelected() {
        responseQuestionType = "personnel";
        responseDiag = "no";
        responseMed = "no";
        responseAleg = "no";
        Rb_allerg_no.setChecked(true);
        Rb_med_no.setChecked(true);
        Rb_main_yes.setChecked(true);
        Rb_diag_no.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.you:
                responseQuestionType = "personnel";
                Rb_main_no.setChecked(false);
                Rb_main_yes.setChecked(true);
                break;

            case R.id.someelse:
                responseQuestionType = "common";
                Rb_main_no.setChecked(true);
                Rb_main_yes.setChecked(false);
                break;

            case R.id.radioButton_register_no:
                responseDiag = "no";
                Rb_diag_yes.setChecked(false);
                Rb_diag_no.setChecked(true);
                break;

            case R.id.radioButton_register_yes:
                responseDiag = "yes";
                Rb_diag_yes.setChecked(true);
                Rb_diag_no.setChecked(false);
                break;

            case R.id.radioButton_medication_no:
                responseMed = "no";
                Rb_med_no.setChecked(true);
                Rb_med_yes.setChecked(false);
                break;

            case R.id.radioButton_medication_yes:
                responseMed = "yes";
                Rb_med_no.setChecked(false);
                Rb_med_yes.setChecked(true);
                break;

            case R.id.radioButton_allergy_no:
                responseAleg = "no";
                Rb_allerg_no.setChecked(true);
                Rb_allerg_yes.setChecked(false);
                break;

            case R.id.radioButton_allergy_yes:
                responseAleg = "yes";
                Rb_allerg_no.setChecked(false);
                Rb_allerg_yes.setChecked(true);
                break;

            case R.id.submit_patient_from:
                responseQuestion = ed_question.getText().toString().trim();
                responseAge = ed_age.getText().toString().trim();
                responseHeight = e_height.getText().toString().trim();
                responseWeight = ed_weight.getText().toString().trim();
                responseCity = ed_city.getText().toString().trim();
                if (responseQuestion.equals("")) {
                    ed_question.setError("Enter Your Question");
                } else if (responseAge.equals("")) {
                    ed_age.setError("Enter Your age");
                } else if (responseHeight.equals("")) {
                    e_height.setError("Enter Your Height");
                } else if (responseWeight.equals("")) {
                    ed_weight.setError("Enter Your Weight");
                } else if (responseCity.equals("")) {
                    ed_city.setError("Enter Your City");
                }else{
                    ReqType = "common";
                    onRequest();
                }
                break;
        }
    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    @Override
    public void onResponseReceived(Object responseObj, int requestType) {
        Utility.getInstance().dismissDialog(pDialog, this);
        if (requestType == IConstant_WebService.WSR_PatientProblemForm) {
            CommonResponse req = (CommonResponse) responseObj;
            if (req!=null) {
                setData(req);
            } else {
                AlertDialogFinish.Show(this, Constant.Alart_Status500, true);
            }
        }
    }

    private void setData(CommonResponse req) {
        try {
            if (ReqType.equals("common")) {
                responseStatus = req.status;
                if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)) {
                    responseMsg = req.msg;
                    Utility.getInstance().showToast(responseMsg, this);
                    navigateToMyAppointment();
                }
                else if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)) {
                    responseMsg = req.msg;
                    AlertDialogFinish.Show(this, responseMsg, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogFinish.Show(this, e.toString(), true);
        }
    }

    private void navigateToMyAppointment() {
        finishActivity();
        try {
            ViewDetail.viewDetail.finish();
            DoctorsList.doctorsList.finish();
            HomeNavigation.homeNavigation.navigateToMyAppointment();

        } catch (Exception e) {
            e.printStackTrace();
            MLog.e("Exception->", e.toString());
        }
    }

    private Void selectImage() {
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

    private void ImagePickPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(PatientProblemForm.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }else {
                ActivityCompat.requestPermissions(PatientProblemForm.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PICK_PERMISSIONS);
            }
        }else {
            pickImage();
        }
    }

    private void ImageCapturePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(PatientProblemForm.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                imageCapture();
            }else {
                ActivityCompat.requestPermissions(PatientProblemForm.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAPTURE_PERMISSIONS);
            }
        }else {
            imageCapture();
        }
    }

    private void imageCapture() {
//        String name = Utility.getInstance().dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
//        output = new File(destination, name + ".jpg");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            picUri = FileProvider.getUriForFile(PatientProblemForm.this,
//                    PatientProblemForm.this.getApplicationContext().getPackageName() + ".provider", output);
//            File file = new File("Mediczy", name+ ".jpg");
//            picUri = Uri.fromFile(file);
//        }else {
//            picUri = Uri.fromFile(output);
//        }
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
//        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//        startActivityForResult(intent, CAMERA_REQUEST_2);

/*        if(pictureIntent.resolveActivity(PatientProblemForm.this.getApplicationContext().getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "app.mediczy_com.android.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, CAMERA_REQUEST_2);
            }
        }*/
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(pictureIntent, CAMERA_REQUEST_2);
    }

//    private File createImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String imageFileName = "IMG_" + timeStamp + "_";
//        File storageDir = PatientProblemForm.this.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(imageFileName,".jpg", storageDir);
//        imageSelected = image.getAbsolutePath();
//        return image;
//    }

    private void pickImage() {
        Intent ia = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(ia, PICK_IMAGE);
//      startActivityForResult(ia, Crop.REQUEST_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_2) {
                if (data != null && data.getExtras() != null) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    addImageView.setImageBitmap(imageBitmap);
                    addImageView.setVisibility(View.VISIBLE);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] bytes = baos.toByteArray();
                    imageSelected = Base64.encodeToString(bytes, Base64.DEFAULT);
                } else {
                    Utility.getInstance().showToast("No Camera Supported", PatientProblemForm.this);
                }
//                imagePath = getRealPathFromURI(picUri);
//                MLog.e("selectedImagePath------->", "" + imagePath);
//                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                bmOptions.inJustDecodeBounds = false;
//                bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
//                bmOptions.inDither = true;
//
//                Bitmap bmp = BitmapFactory.decodeFile(imagePath, bmOptions);
//                Bitmap bitmap2 = getResizedBitmap(bmp, 800);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap2.compress(Bitmap.CompressFormat.JPEG, 70, baos);
//                byte[] bytes = baos.toByteArray();
//                imageSelected = Base64.encodeToString(bytes, Base64.DEFAULT);
//                addImageView.setImageBitmap(bitmap2);
//                addImageView.setVisibility(View.VISIBLE);
            } else if (requestCode == PICK_IMAGE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                MLog.e("picturePath", "picturePath :+" + picturePath);
                Bitmap bitmap1 = BitmapFactory.decodeFile(picturePath);
                Bitmap bitmap2 = getResizedBitmap(bitmap1, 600);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] bytes = stream.toByteArray();
                imageSelected = Base64.encodeToString(bytes, Base64.DEFAULT);
                cursor.close();
                addImageView.setImageBitmap(bitmap2);
                addImageView.setVisibility(View.VISIBLE);
            }
        }
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

    public void showImage(Bitmap bitmap) {
        int width = 20;
        int height = 40;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialog.dismiss();
            }
        });
        Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        screenWidth = screenWidth - width;
        screenHeight = screenHeight - height;
        MLog.d("ScreenResoluction->",+ screenWidth+","+screenHeight);
        final TouchImageView imageView = new TouchImageView(this);
        imageView.setImageBitmap(bitmap);
//	    builder.addContentView(imageView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.addContentView(imageView, new RelativeLayout.LayoutParams(screenWidth, screenHeight));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
            }
        });
        dialog.show();
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
}
