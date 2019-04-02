package app.mediczy_com.utility;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.iconstant.Constant;

/**
 * Created by Prithivi on 19-11-2016.
 */

public class Utility {

    private static Utility  objUtility;
    ProgressDialog pDialog = null;

    private Utility(){
        //ToDo here
    }

    public static Utility getInstance() {
        if (objUtility == null) {
            objUtility = new Utility();
        }
        return objUtility;
    }

    public void showDialogRepeat(Context context) {
        final Handler mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        if (HomeNavigation.homeNavigation != null) {
                            Thread.sleep(Constant.Alert_Demo_Message_Interval);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    if (HomeNavigation.homeNavigation!=null &&
                                            HomeNavigation.homeNavigation.getTheme()!=null){
                                        new AlertDialog.Builder(HomeNavigation.homeNavigation)
                                                .setTitle(Constant.Alart_AppName_Internet)
                                                .setMessage(Constant.Alart_Demo_Message)
                                                .setCancelable(true)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //                      ctx.finish();
                                                    }
                                                }).show();
                                    }

                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void showLoading(ProgressDialog pDialog, Context context) {
        this.pDialog = new ProgressDialog(context);
        this.pDialog.setCancelable(false);
        this.pDialog.setMessage(Constant.dialog_msg);
        // strings in a remote file.
        this.pDialog.show();
    }

    public void dismissDialog(ProgressDialog pDialog, Context context) {
        this.pDialog.dismiss();
    }

    public void runLayoutAnimation(RecyclerView recyclerView, Context context) {
        int resId = Utility.getInstance().getRecycleViewAnimationId();
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public int getRecycleViewAnimationId() {
        int resId = R.anim.recyclerview_animation_fall_down;
        return resId;
    }
    public void showToast(String msg, Context ctx){
        try {
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showToastLong(String msg, Context ctx){
        try {
            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getFirstChar(String str){
        try {
            str = String.valueOf(str.charAt(0));
            MLog.e("FirstLetter->", "" + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  str;
    }

    public Bitmap getBitmap(ImageView iV) {
        Bitmap bitmap = null;
        try {
            BitmapDrawable drawable = (BitmapDrawable) iV.getDrawable();
            bitmap = drawable.getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        String formattedDate = dateFormat.format(new Date()).toString();
        return formattedDate;
    }

    public static boolean isDateAfter(String startDate, String endDate)
    {
        try {
            String myFormatString = "dd/M/yyyy"; // for example
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date date1 = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (date1.after(startingDate))
                return true;
            else
                return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean isExpire(String date) throws java.text.ParseException {
        if(date.isEmpty() || date.trim().equals("")){
            return false;
        }else{
            SimpleDateFormat sdf =  new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss a"); // Jan-20-2015 1:30:55 PM
            Date d;
            Date d1;
            String today = getToday("MMM-dd-yyyy hh:mm:ss a");
            MLog.e("today", "" + today);
//            server_current_time=today;
            try {
                //System.out.println("expdate>> "+date);
                //System.out.println("today>> "+today+"\n\n");
                d = sdf.parse(date);
                d1 = sdf.parse(today);
                if(d1.compareTo(d) <0){// not expired
                    return false;
                }else if(d.compareTo(d1)==0){// both date are same
                    if(d.getTime() < d1.getTime()){// not expired
                        return false;
                    }else if(d.getTime() == d1.getTime()){//expired
                        return true;
                    }else{//expired
                        return true;
                    }
                }else{//expired
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public String getToday(String format){
        Date date = new Date();
        return new SimpleDateFormat(format).format(date);
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public String getDateAndTime(Date date) {
        SimpleDateFormat sdf =  new SimpleDateFormat("MMM-dd-yyyy hh:mm a"); // Jan-20-2015 1:30:55 PM
        String strDate = sdf.format(date);
        return strDate;
    }

    public String getTimeHoursAfter(int i){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss a");
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, i);
        String str = sdf.format(now.getTime());
        return str;
    }

    public String getTimeMinAfter(int i){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss a");
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, i);
        String str = sdf.format(now.getTime());
        return str;
    }

    public float roundToDecimal(double i) {
        MLog.e("roundToDecimal: ", "" + String.valueOf(i));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Float.valueOf(decimalFormat.format(i));
    }

    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }
        return false;
    }

    public boolean checkPlayServices(final Activity context) {
        int PLAY_SERVICES_RESOLUTION_REQUEST= 0;
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, context,
                        PLAY_SERVICES_RESOLUTION_REQUEST);
                if (dialog != null) {
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            if (ConnectionResult.SERVICE_INVALID == resultCode) context.finish();
                        }
                    });
                    return false;
                }
            }
            new AlertDialog.Builder(context)
                    .setTitle(Constant.Alart_AppName_Internet)
                    .setMessage("Google Play Services Error " +
                            "This device is not supported for required Google Play Services")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            context.finish();
                        }
                    }).show();
            return false;
        }
        return true;
    }

    public boolean eMailValidation(String emailID) {
        String exp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern email_Pattern = Pattern.compile(exp);
        Matcher email_Matcher = email_Pattern.matcher(emailID);
        return email_Matcher.matches();
    }

    public String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public boolean isSdPresent() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public void hideKeyboardNew(Activity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void hideKeyboard(FragmentActivity context) {
        // Check if no view has focus:
        View view = ((FragmentActivity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void hideKeyboard(Activity context) {
        // Check if no view has focus:
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void callBrowser(Context context, String URLInfo) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLInfo));
        context.startActivity(browserIntent);
    }

    public boolean isPlayServiceAvailable(Context context)
    {
        // TODO Auto-generated method stub
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        MLog.i("Tag","checkGooglePlayServicesAvailable, connectionStatusCode=" + connectionStatusCode);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            return false;
        }
//		    Toast.makeText(context,"Google Play service available", Toast.LENGTH_LONG).show();
        return true;
    }

    public String getDeviceID(Context ctx){
        String AndroidUDID;
        AndroidUDID = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        MLog.e("Android UDID: ", AndroidUDID);
        return AndroidUDID;
    }

    public Boolean isConnectingToInternet(Context ctx) {
        Boolean isConnected = false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            isConnected = info != null && info.isAvailable()&& info.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MLog.e("isConnected: ", "" + isConnected);
        return isConnected;
    }

    public void showImage(ImageView image, Context ctx)
    {
        try {
            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
            final Bitmap bitmap = drawable.getBitmap();
            int width = 20;
            int height = 40;
            final Dialog builder = new Dialog(ctx);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            builder.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    builder.dismiss();
                }
            });
            Display display = ((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int screenWidth = display.getWidth();
            int screenHeight = display.getHeight();
            screenWidth = screenWidth - width;
            screenHeight = screenHeight - height;
            MLog.e("Screen_Res---->",+ screenWidth+","+screenHeight);
            final TouchImageView imageView = new TouchImageView(ctx);
            imageView.setImageBitmap(bitmap);
    //	    builder.addContentView(imageView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(screenWidth, screenHeight));
            imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    builder.dismiss();
                }
            });
            imageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
                @Override
                public void onMove() {
    /*				PointF point = imageView.getScrollPosition();
                    RectF rect = imageView.getZoomedRect();
                    float currentZoom = imageView.getCurrentZoom();
                    boolean isZoomed = imageView.isZoomed();*/
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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

    public static String getRealPathFromURI(Uri contentUri, Activity ctx) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = ctx.managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    /*
    //version check
    public class VersionChecker extends AsyncTask<String, String, String> {

        String newVersion;
        ProgressDialog pDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeNavigation.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Checking latest version..."); // typically you will define such
            // strings in a remote file.
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + Constant.Package_Service + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }

        @Override
        protected void onPostExecute(String latestVersion) {
            super.onPostExecute(latestVersion);
            pDialog.dismiss();

            String localVersion = getResources().getString(R.string.versionName);
            MLog.e("localVersion----->", "" + localVersion);
            MLog.e("latestVersion----->", "" + latestVersion);
            if (!latestVersion.equals(localVersion)){
                AlertDialogFinish.Update(HomeNavigation.this, Constant.Alert_Update_ReqMsg, true);
            }
        }
    }

*/
}
