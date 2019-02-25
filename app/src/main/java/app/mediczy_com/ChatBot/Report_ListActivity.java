package app.mediczy_com.ChatBot;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.Retrofit.ListDetails;
import app.mediczy_com.Retrofit.RetrofitInterface;
import app.mediczy_com.Session.Session;
import app.mediczy_com.request.CommonRequest;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Report_ListActivity extends AppCompatActivity {

    LinearLayoutManager layoutManager;
    Session session;

    RetrofitInterface apiInterface;
    Context context;
    private Toolbar toolbar;

    RecyclerView list_report;

    List_Report_Adapter list_REPORT;
    public ArrayList<User_Re_List> obj_list = new ArrayList<>();

    ProgressBar progressBar;
    LinearLayout linearLayout;

    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_bot_activity_report__list);

        init();

        userId = LocalStore.getInstance().getUserID(this);
        MLog.e("userId==>", "" + userId);
        Report_List();
    }


    public void init() {
        context = Report_ListActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Symptoms List");
        list_report = (RecyclerView) findViewById(R.id.list_report);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        //Retrofit


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(RetrofitInterface.class);


    }

    public void Report_List() {
        linearLayout.setVisibility(View.VISIBLE);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_id = userId;

        Call<ListDetails> Report_List = apiInterface.Report_List(commonRequest);
        Report_List.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                MLog.e("", "Report Deatil_Status:==>" + response.body());


                if (response.body().status.equalsIgnoreCase("success")) {


                    //  Toast.makeText(Report_ListActivity.this, response.body().msg, Toast.LENGTH_LONG).show();
                    obj_list = response.body().user_bot_reports;

                    if (!obj_list.isEmpty()) {
                        list_REPORT = new List_Report_Adapter(Report_ListActivity.this, obj_list);
                        list_report.setAdapter(list_REPORT);
                        LinearLayoutManager layoutManager2 = new LinearLayoutManager(Report_ListActivity.this);
                        list_report.setLayoutManager(layoutManager2);
                        linearLayout.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(Report_ListActivity.this, "No Records ..!!!", Toast.LENGTH_LONG).show();

                    }


                } else {

                    Toast.makeText(Report_ListActivity.this, "No Records ..!!!", Toast.LENGTH_LONG).show();
                }
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(Report_ListActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();


            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishActivity();
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finishActivity();
            return true;
        }
        return false;
    }

    private void finishActivity() {
 //       Intent intent = new Intent(Report_ListActivity.this, HomeNavigation.class);
 //       startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }
}
