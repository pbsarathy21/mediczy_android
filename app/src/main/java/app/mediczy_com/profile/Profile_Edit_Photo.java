package app.mediczy_com.profile;

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
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import app.mediczy_com.R;
import app.mediczy_com.dialog.AlertDialogFinish;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.TouchImageView;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservice.request.RequestManager;
import app.mediczy_com.webservice.request.ResponseListener;
import app.mediczy_com.webservicemodel.request.DoctorProfileReq;
import app.mediczy_com.webservicemodel.response.CommonResponse;

/**
 * Created by Prithivi Raj on 14-02-2016.
 */
public class Profile_Edit_Photo extends AppCompatActivity implements ResponseListener {

    private final int REQUEST_CAPTURE_PERMISSIONS = 1;
    private final int REQUEST_PICK_PERMISSIONS = 2;
    private Toolbar toolbar;
    private GridView gridView;
    private  ProfileImageObserver imageObserver;
    private final String FOLDER_NAME = "MediczyPhoto";
    private File destination, output;
    private Uri picUri;
    private static int PICK_IMAGE = 1;
    private static int CAMERA_REQUEST_2 = 2;
    private String device_id, redId, ID, ReqType, imagePath="", imageSelected,
            responseStatus, responseMsg, responseImage;
    private String doctor_banner_id;
    private ArrayList<String> array_image_path = new ArrayList<String>();
    private ArrayList<String> array_doctor_banner_id = new ArrayList<String>();
    private ProgressDialog pDialog = null;
    private int mRequestType = 0, selectedImagePosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_photo);
        imageObserver = (ProfileImageObserver) Profile_Edit.profile_edit;
        toolbar = (Toolbar) findViewById(R.id.toolbar_profile_edit_photo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();

      /*  gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long io) {
                String image_path = array_image_path.get(position);
                doctor_banner_id = array_doctor_banner_id.get(position);
                selectedImagePosition = position;
                MLog.e("selectedImagePosition", "" + selectedImagePosition);
                MLog.e("doctor_banner_id", "" + doctor_banner_id);
                MLog.e("image_path", "" + image_path);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(Profile_Edit_Photo.this, Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        selectedImage();
                    }else {
                        ActivityCompat.requestPermissions(Profile_Edit_Photo.this,
                                new String[]{Manifest.permission.CALL_PHONE}, 1);
                    }
                }else {
                    selectedImage();
                }
            }
        });*/

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long io) {

                String image_path = array_image_path.get(position);
                doctor_banner_id = array_doctor_banner_id.get(position);
                selectedImagePosition = position;
                MLog.e("selectedImagePosition", "" + selectedImagePosition);
                MLog.e("doctor_banner_id", "" + doctor_banner_id);
                MLog.e("image_path", "" + image_path);

                selectedImage();
            }
        });
    }

    private void init() {
        gridView = (GridView)findViewById(R.id.gridView_view_categry_edit_photo);
        ID = LocalStore.getInstance().getUserID(this);
        device_id = Utility.getInstance().getDeviceID(this);
        redId = LocalStore.getInstance().getGcmId(this);
        MLog.e("ID", "" + ID);
        destination = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME);
        if (!destination.exists()) {
            destination.mkdirs();
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            array_image_path = bundle.getStringArrayList("array_image_path");
            array_doctor_banner_id = bundle.getStringArrayList("array_doctor_banner_id");
        }
        MLog.e("array_image_path->", "" + array_image_path);
        MLog.e("array_doctor_banner_id->", "" + array_doctor_banner_id);
        SetImage(array_image_path);
    }

    public void onRequest() {
        if (Utility.getInstance().isConnectingToInternet(this)) {
            Utility.getInstance().showLoading(pDialog, this);
            DoctorProfileReq deliveryFormReq = new DoctorProfileReq();
            deliveryFormReq.doctor_banner_id = doctor_banner_id;
            deliveryFormReq.imagePath = imageSelected;
            deliveryFormReq.doctor_id = ID;
            if (ReqType.equals("photo_edit")) {
                RequestManager.DoctorEditPhoto(this, null, deliveryFormReq, mRequestType);
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
        responseMsg = req.msg;
        if (ReqType.equals("photo_edit")) {
            if (responseStatus.equalsIgnoreCase(Constant.Response_Status_Success)) {
                responseImage = req.image;
                if (responseImage!=null) {
                    array_image_path.set(selectedImagePosition, responseImage);
                    gridView.setAdapter(new Image_Type_Adapter(this, array_image_path));
                    imageObserver.onImageUpdated(true);
                    Utility.getInstance().showToast(responseMsg, this);
                } else {
                    Utility.getInstance().showToast(responseImage, this);
                }
            }else if(responseStatus.equalsIgnoreCase(Constant.Response_Status_Error)){
                AlertDialogFinish.Show(Profile_Edit_Photo.this, responseMsg, true);
            } else {
                AlertDialogFinish.Show(Profile_Edit_Photo.this, Constant.Alart_Status500, false);
            }
        }
    }

    private void selectedImage() {
        if (Utility.getInstance().isConnectingToInternet(Profile_Edit_Photo.this)) {
            selectImage();
        } else {
            AlertDialogFinish.Show(Profile_Edit_Photo.this, Constant.Alart_Internet, true);
        }
    }

    private void SetImage(ArrayList<String> arrayListImage) {
        if (Utility.getInstance().isConnectingToInternet(Profile_Edit_Photo.this)) {
            if (arrayListImage.size()>0){
                gridView.setAdapter(new Image_Type_Adapter(this, arrayListImage));
            }else{
                Utility.getInstance().showToast("No Image !!!", this);
            }
        } else {
            AlertDialogFinish.Show(Profile_Edit_Photo.this, Constant.Alart_Internet, true);
        }
    }

    private void ImagePickPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(Profile_Edit_Photo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }else {
                ActivityCompat.requestPermissions(Profile_Edit_Photo.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PICK_PERMISSIONS);
            }
        }else {
            pickImage();
        }
    }
    
    private void ImageCapturePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(Profile_Edit_Photo.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                imageCapture();
            }else {
                ActivityCompat.requestPermissions(Profile_Edit_Photo.this,
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
            picUri = FileProvider.getUriForFile(Profile_Edit_Photo.this,
                    Profile_Edit_Photo.this.getApplicationContext().getPackageName() + ".provider", output);
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
//      startActivityForResult(ia, Crop.REQUEST_PICK);
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
                Bitmap bitmap2 = getResizedBitmap(bmp, 800);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] bytes = baos.toByteArray();
                imageSelected = Base64.encodeToString(bytes, Base64.DEFAULT);
                if (Utility.getInstance().isConnectingToInternet(this)) {
                    ReqType="photo_edit";
                    mRequestType = IConstant_WebService.WSR_DoctorBannerEdit;
                    onRequest();
                }else{
                    AlertDialogFinish.Show(this, Constant.Alart_Internet, false);
                }
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
                if (Utility.getInstance().isConnectingToInternet(this)) {
                    ReqType = "photo_edit";
                    mRequestType = IConstant_WebService.WSR_DoctorBannerEdit;
                    onRequest();
                } else {
                    AlertDialogFinish.Show(this, Constant.Alart_Internet, false);
                }
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

    public class Image_Type_Adapter extends BaseAdapter {
        LayoutInflater mInflater;
        Context ctx;
        private ImageLoader imageLoader;
        ArrayList<String> arrayList;

        public Image_Type_Adapter(Context context, ArrayList<String> array_bitmap_grid_adpater) {
            this.mInflater = LayoutInflater.from(context);
            this.ctx = context;
            imageLoader = new ImageLoader(ctx);
            this.arrayList = array_bitmap_grid_adpater;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder1 holder;
            if (convertView == null) {
                holder = new ViewHolder1();
                convertView = mInflater.inflate(R.layout.adapter_photos, null);
                holder.Iv__image = (ImageView) convertView.findViewById(R.id.imageView_adapter_home_type_edit);
                holder.Iv__image.setTag(position);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder1) convertView.getTag();
            }
            String imagePath = arrayList.get(position).toString();
            MLog.e("imagePath->", "" + imagePath);

            holder.Iv__image.setScaleType(ImageView.ScaleType.FIT_XY);
//        holder.Iv__image.setImageBitmap(arrayList.get(position).getImage());
            imageLoader.DisplayImage(imagePath, holder.Iv__image, 4);

            holder.Iv__image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        BitmapDrawable drawable = (BitmapDrawable) holder.Iv__image.getDrawable();
                        if (drawable != null) {
                            Bitmap bitmap = drawable.getBitmap();
                            showImage(bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });

            holder.Iv__image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String image_path = arrayList.get(position);
                    doctor_banner_id = array_doctor_banner_id.get(position);
                    selectedImagePosition = position;
                    MLog.e("selectedImagePosition", "" + selectedImagePosition);
                    MLog.e("doctor_banner_id", "" + doctor_banner_id);
                    MLog.e("image_path", "" + image_path);
                    if (Utility.getInstance().isConnectingToInternet(Profile_Edit_Photo.this)) {
                        selectImage();
                    } else {
                        AlertDialogFinish.Show(Profile_Edit_Photo.this, Constant.Alart_Internet, true);
                    }
                }
            });
            return convertView;
        }
        class ViewHolder1 {
            ImageView Iv__image;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            finish();
            overridePendingTransition(R.anim.left_right, R.anim.right_left);
            return true;
        }
        return false;
    }
}
