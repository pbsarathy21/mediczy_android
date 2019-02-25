package app.mediczy_com.ChatBot;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.mediczy_com.DoctorsList;
import app.mediczy_com.R;
import app.mediczy_com.Retrofit.ListDetails;
import app.mediczy_com.Retrofit.RetrofitInterface;
import app.mediczy_com.Session.Session;
import app.mediczy_com.request.CommonRequest;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.Consts;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.webservicemodel.response.DoctorListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Report_DetailActivity extends AppCompatActivity implements Callback<ListDetails> {


    LinearLayoutManager layoutManager;
    Session session;

    RetrofitInterface apiInterface;
    Context context;
    private Toolbar toolbar;

    Repot_Adapter r_adapter;
    Condition_Report_Adapter condtion_adapter;

    ArrayList<Bot_Condition> obj_Con = new ArrayList<>();


    ArrayList<User_bot_Sys> obj_intial = new ArrayList<>();

    ArrayList<Present_User_Bot> obj_present = new ArrayList<>();
    ArrayList<Present_User_Bot> obj_absent = new ArrayList<>();
    ArrayList<Present_User_Bot> obj_unknow = new ArrayList<>();
    ArrayList<Present_User_Bot> obj_All = new ArrayList<>();
    ArrayList<Integer> count = new ArrayList<>();

    ArrayList<Limitee> limite = new ArrayList<>();
    public Double perc = 0.0;
    public int percentage;
    public static final String TAG = Report_DetailActivity.class.getSimpleName();

    RecyclerView Present_Recyler, result_Recyler, rep_recyler;
    public Bot_Report bot;

    TextView text_date, text_sex, text_sex_value, text_Age, text_age_value;
    Initial_Report initial_report;

    ProgressBar progr;
    LinearLayout linea;
    TextView text_title, text_t, text_app, text_con, text_app2, text_con2, text_submit, text_an, tex;
    LinearLayout linear_sumbit;
    ImageView img_cross;

    ProgressBar progressbar, progressbar2;

    NestedScrollView nested_scrollview;


    ArcMenu arcMenuAndroid;
    FloatingActionButton fab_menu_1, fab_menu_2, fab_menu_3;
    private String userId;
    private String UserType;
    ImageView fab;
    public List<DoctorListResponse> docList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_bot_activity_report__detail);

        init();
        session = new Session(this);

        UserType = LocalStore.getInstance().getType(context);
        MLog.e("UserType", "" + UserType);

        userId = LocalStore.getInstance().getUserID(this);
        MLog.e("userId==>", "" + userId);

        Report_Deatail();
        //visible the arc menu by default

    }


    public void init() {
        context = Report_DetailActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Symptoms Detail");
        progr = (ProgressBar) findViewById(R.id.progr);
        linea = (LinearLayout) findViewById(R.id.linea);
        nested_scrollview = (NestedScrollView) findViewById(R.id.nested_scrollview);

        Present_Recyler = (RecyclerView) findViewById(R.id.Present_Recyler);
        Present_Recyler.setNestedScrollingEnabled(false);

        result_Recyler = (RecyclerView) findViewById(R.id.result_Recyler);
        result_Recyler.setNestedScrollingEnabled(false);

        rep_recyler = (RecyclerView) findViewById(R.id.rep_recyler);
        rep_recyler.setNestedScrollingEnabled(false);

        text_date = (TextView) findViewById(R.id.text_date);
        text_sex = (TextView) findViewById(R.id.text_sex);
        text_sex_value = (TextView) findViewById(R.id.text_sex_value);
        text_Age = (TextView) findViewById(R.id.text_Age);
        text_age_value = (TextView) findViewById(R.id.text_age_value);


        try {
            fab = (ImageView) findViewById(R.id.fab_menu);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(fab);
            Glide.with(this).load(R.raw.chat_bot_rob).into(imageViewTarget);
        } catch (Exception e) {

            Log.e("", "Debug from onCreate Exception" + e.toString());

        }


//arc menu on bottom layout

        arcMenuAndroid = (ArcMenu) findViewById(R.id.arcMenuAndroid);

        fab_menu_1 = (FloatingActionButton) findViewById(R.id.fab_menu_1);
        fab_menu_2 = (FloatingActionButton) findViewById(R.id.fab_menu_2);
        fab_menu_3 = (FloatingActionButton) findViewById(R.id.fab_menu_3);
        //Retrofit


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(RetrofitInterface.class);


        if (arcMenuAndroid.isMenuOpened()) {
            arcMenuAndroid.toggleMenu();
        } else {

            arcMenuAndroid.toggleMenu();


        }


        // change the menu click
        arcMenuAndroid.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {


                //TODO something when menu is opened
            }

            @Override
            public void onMenuClosed() {
                //TODO something when menu is closed
            }
        });
 /* for (int i = 0, size = arcMenuAndroid.getChildCount(); i < size; i++) {
      arcMenuAndroid.getChildAt(i).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(context,"menu_1_clicked==>",Toast.LENGTH_LONG).show();

          }
      });
  }*/
        fab_menu_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "menu_1_clicked==>", Toast.LENGTH_SHORT).show();


                // Intent intent = new Intent(Report_DetailActivity.this, Main_Activity.class);
                // startActivity(intent);

                Download();


            }
        });

        fab_menu_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "menu_2_clicked==>", Toast.LENGTH_SHORT).show();
                shareIt();

            }
        });

        fab_menu_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "menu_3_clicked==>", Toast.LENGTH_SHORT).show();
                Detail();

            }
        });


    }


    public void Report_Deatail() {

        linea.setVisibility(View.VISIBLE);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_bot_report_id = session.getView_id();
        // commonRequest.user_bot_report_id = "4";

        Call<ListDetails> Re_Detail = apiInterface.Report_Detail(commonRequest);
        Re_Detail.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                MLog.e("", "Report Deatil_Status:==>" + response.body());

                MLog.e("", "Report Deatil_Status:==>" + response.body().user_bot_report);


                Consts.Count = response.body().free_code_count;


                if (response.body().status.equalsIgnoreCase("success")) {


                    text_date.setText(response.body().user_bot_report.created_at);
                    text_sex_value.setText(response.body().user_bot_report.gender);
                    text_age_value.setText(response.body().user_bot_report.age);

                    obj_Con = response.body().user_bot_report.user_bot_conditions;
                    obj_intial = response.body().initial_user_bot_symptoms;

                    limite = response.body().limit_conditions;


                    if (!obj_Con.isEmpty()) {
                        condtion_adapter = new Condition_Report_Adapter(Report_DetailActivity.this, obj_Con);
                        result_Recyler.setAdapter(condtion_adapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(Report_DetailActivity.this);
                        result_Recyler.setLayoutManager(layoutManager);

                    } else {
                        MLog.e("list_empty==>", "list_empty==>");
                    }


                    if (!obj_intial.isEmpty()) {
                        initial_report = new Initial_Report(Report_DetailActivity.this, obj_intial);
                        rep_recyler.setAdapter(initial_report);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(Report_DetailActivity.this);
                        rep_recyler.setLayoutManager(layoutManager);

                    } else {
                        MLog.e("list_empty==>", "list_empty==>");
                    }


                    if (!response.body().present_user_bot_symptoms.isEmpty() ||!response.body().absent_user_bot_symptoms.isEmpty()||!response.body().unknown_user_bot_symptoms.isEmpty()) {

                        obj_present = response.body().present_user_bot_symptoms;
                        obj_absent = response.body().absent_user_bot_symptoms;
                        obj_unknow = response.body().unknown_user_bot_symptoms;

                        count.add(0);
                        count.add(obj_present.size());
                        count.add(obj_absent.size() + obj_present.size());

                        obj_All.addAll(obj_present);
                        obj_All.addAll(obj_absent);
                        obj_All.addAll(obj_unknow);


                        r_adapter = new Repot_Adapter(Report_DetailActivity.this, obj_All, count);
                        Present_Recyler.setAdapter(r_adapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(Report_DetailActivity.this);
                        Present_Recyler.setLayoutManager(layoutManager);
                        nested_scrollview.setVisibility(View.VISIBLE);
                        linea.setVisibility(View.GONE);

                    } else {
                        MLog.e("list_empty==>", "list_empty==>");
                    }
                    linea.setVisibility(View.GONE);
                    //Detail();
                    // Toast.makeText(Report_DetailActivity.this, response.body().msg, Toast.LENGTH_LONG).show();


                } else {

                    // Toast.makeText(Report_DetailActivity.this, "IN PROGRESS ..!!!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(Report_DetailActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

    }

    @Override
    public void onFailure(Call<ListDetails> call, Throwable t) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            if (Consts.back.equalsIgnoreCase("0")) {
                Detail();
                Consts.back = "1";

            } else {
                Consts.back = "0";

                Intent intent = new Intent(Report_DetailActivity.this, Report_ListActivity.class);
                startActivity(intent);
                finish();

            }


        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            if (Consts.back.equalsIgnoreCase("0")) {
                Detail();
                Consts.back = "1";

            } else {
                Consts.back = "0";

                Intent intent = new Intent(Report_DetailActivity.this, Report_ListActivity.class);
                startActivity(intent);
                finish();

            }
            return true;
        }
        return false;
    }


    public void Detail() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        //  builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View content = inflater.inflate(R.layout.chat_bot_customdialog, null);
        builder.setView(content);

        //  AlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        text_title = (TextView) content.findViewById(R.id.text_title);
        text_t = (TextView) content.findViewById(R.id.text_t);

        text_app = (TextView) content.findViewById(R.id.text_app);
        text_con = (TextView) content.findViewById(R.id.text_con);
        text_app2 = (TextView) content.findViewById(R.id.text_app2);
        text_con2 = (TextView) content.findViewById(R.id.text_con2);


        progressbar2 = (ProgressBar) content.findViewById(R.id.progressbar2);
        progressbar = (ProgressBar) content.findViewById(R.id.progressbar);
        text_submit = (TextView) content.findViewById(R.id.text_submit);
        linear_sumbit = (LinearLayout) content.findViewById(R.id.linear_sumbit);
        text_an = (TextView) content.findViewById(R.id.text_an);
        tex = (TextView) content.findViewById(R.id.tex);


        img_cross = (ImageView) content.findViewById(R.id.img_cross);

        try {

            text_app.setText(limite.get(0).name);
            //text_con.setText(limite.get(0).severity);
            text_an.setText(limite.get(0).category + " Specialist.");

            tex.setText("You have " + Consts.Count + " free online doctor consultation.");

            text_app2.setText(limite.get(1).name);
            //text_con2.setText(limite.get(1).severity);


            if (!limite.get(0).severity.isEmpty()) {

                if (limite.get(0).severity.equalsIgnoreCase("mild")) {

                    text_con.setText(limite.get(0).severity);
                    text_con.setTextColor(Color.parseColor("#008000"));

                    perc = Double.parseDouble(limite.get(0).probability);
                    MLog.e(TAG, "per==>" + perc);
                    double value = perc * 100;
                    MLog.e(TAG, "value==>" + value);
                    percentage = (int) Math.round(value);
                    MLog.e(TAG, "PERCENTAGE==>" + percentage);
                    Resources res = getResources();
                    Rect bounds = progressbar.getProgressDrawable().getBounds();

                    progressbar.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress));

                    progressbar.getProgressDrawable().setBounds(bounds);
                    progressbar.setProgress(percentage);

                } else if (limite.get(0).severity.equalsIgnoreCase("moderate")) {

                    text_con.setText(limite.get(0).severity);
                    text_con.setTextColor(Color.parseColor("#ffa500"));

                    perc = Double.parseDouble(limite.get(0).probability);
                    MLog.e(TAG, "per==>" + perc);
                    double value = perc * 100;
                    MLog.e(TAG, "value==>" + value);
                    percentage = (int) Math.round(value);
                    MLog.e(TAG, "PERCENTAGE==>" + percentage);
                    Resources res = getResources();
                    Rect bounds = progressbar.getProgressDrawable().getBounds();

                    progressbar.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress_yellow));

                    progressbar.getProgressDrawable().setBounds(bounds);
                    progressbar.setProgress(percentage);

                } else if (limite.get(0).severity.equalsIgnoreCase("severe")) {

                    text_con.setText(limite.get(0).severity);
                    text_con.setTextColor(Color.parseColor("#00CA93"));

                    perc = Double.parseDouble(limite.get(0).probability);
                    MLog.e(TAG, "per==>" + perc);
                    double value = perc * 100;
                    MLog.e(TAG, "value==>" + value);
                    percentage = (int) Math.round(value);
                    MLog.e(TAG, "PERCENTAGE==>" + percentage);
                    Resources res = getResources();
                    Rect bounds = progressbar.getProgressDrawable().getBounds();

                    progressbar.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress_severe));

                    progressbar.getProgressDrawable().setBounds(bounds);
                    progressbar.setProgress(percentage);

                }

            }

            if (!limite.get(1).severity.isEmpty()) {

                if (limite.get(1).severity.equalsIgnoreCase("mild")) {


                    text_con2.setText(limite.get(1).severity);
                    text_con2.setTextColor(Color.parseColor("#008000"));

                    perc = Double.parseDouble(limite.get(1).probability);
                    MLog.e(TAG, "per==>" + perc);
                    double value = perc * 100;
                    MLog.e(TAG, "value==>" + value);
                    percentage = (int) Math.round(value);
                    MLog.e(TAG, "PERCENTAGE==>" + percentage);
                    Resources res = getResources();
                    Rect bounds = progressbar2.getProgressDrawable().getBounds();

                    progressbar2.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress));

                    progressbar2.getProgressDrawable().setBounds(bounds);
                    progressbar2.setProgress(percentage);

                } else if (limite.get(1).severity.equalsIgnoreCase("moderate")) {

                    text_con2.setText(limite.get(1).severity);
                    text_con2.setTextColor(Color.parseColor("#ffa500"));

                    perc = Double.parseDouble(limite.get(1).probability);
                    MLog.e(TAG, "per==>" + perc);
                    double value = perc * 100;
                    MLog.e(TAG, "value==>" + value);
                    percentage = (int) Math.round(value);
                    MLog.e(TAG, "PERCENTAGE==>" + percentage);
                    Resources res = getResources();
                    Rect bounds = progressbar2.getProgressDrawable().getBounds();

                    progressbar2.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress_yellow));

                    progressbar2.getProgressDrawable().setBounds(bounds);
                    progressbar2.setProgress(percentage);

                } else if (limite.get(1).severity.equalsIgnoreCase("severe")) {

                    text_con2.setText(limite.get(1).severity);
                    text_con2.setTextColor(Color.parseColor("#00CA93"));

                    perc = Double.parseDouble(limite.get(1).probability);
                    MLog.e(TAG, "per==>" + perc);
                    double value = perc * 100;
                    MLog.e(TAG, "value==>" + value);
                    percentage = (int) Math.round(value);
                    MLog.e(TAG, "PERCENTAGE==>" + percentage);
                    Resources res = getResources();
                    Rect bounds = progressbar2.getProgressDrawable().getBounds();

                    progressbar2.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress_severe));

                    progressbar2.getProgressDrawable().setBounds(bounds);
                    progressbar2.setProgress(percentage);

                }

            }


        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        //  alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Transparent_white)));

        // alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view));

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        img_cross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MLog.e("fvfdv", "cross");
                alertDialog.dismiss();


            }
        });


        linear_sumbit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Doctor_List();


            }
        });


    }


    private void shareIt() {

        //sharing implementation here
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "My Health Report");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "My Health Report \nhttps://mediczy.com/mediczy-api/api/mobile/download_report/" + session.getView_id() + ".pdf");
        startActivity(Intent.createChooser(sharingIntent, "Mediczy_Report"));
    }


    public void Download() {


        new DownloadFile().execute("https://mediczy.com/mediczy-api/api/mobile/download_report/" + session.getView_id() + ".pdf", "report.pdf");
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Medi_Report/" + "report.pdf");  // -> filename = maven.pdf
        // Uri path = Uri.fromFile(pdfFile);

        Uri path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", pdfFile);

        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);

        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Report_DetailActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }


    }


    public void download(View v) {
        new DownloadFile().execute("https://mediczy.com/mediczy-api/api/mobile/download_report/" + session.getView_id() + ".pdf", "report.pdf");
    }

    public void view(View v) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Medi_Report/" + "report.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Report_DetailActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            try {
                String fileUrl = strings[0];
                //  MLog.e("splir==>", "split==>" + fileUrl.split("download_report/"));// -> http://maven.apache.org/maven-1.x/maven.pdf
                String fileName = strings[1];  // -> maven.pdf
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "Medi_Report");
                folder.mkdir();

                File pdfFile = new File(folder, fileName);

                try {
                    pdfFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileDownloader.downloadFile(fileUrl, pdfFile);


            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


    public void Doctor_List() {
        linea.setVisibility(View.VISIBLE);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.id = userId;
        commonRequest.type = UserType;
        commonRequest.lat = session.getLat();
        commonRequest.longitude = session.getLong();
        Call<ListDetails> Doc_List = apiInterface.Act_Doctor(commonRequest);
        Doc_List.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                MLog.e("", "Doctor_List_Status:==>" + response.body());
                MLog.e("", "Doctor_List_Status:==>" + response.body().doctors);


                if (response.body().status.equalsIgnoreCase("success")) {


                    docList = response.body().doctors;

                    session.setDoc("1");
                    Intent i = new Intent(context, DoctorsList.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);


                } else {

                    Toast.makeText(Report_DetailActivity.this, "No Doctors ..!!!", Toast.LENGTH_LONG).show();
                }
                linea.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(Report_DetailActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();


            }
        });

    }
}
