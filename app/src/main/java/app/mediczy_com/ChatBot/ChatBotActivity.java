package app.mediczy_com.ChatBot;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import app.mediczy_com.HomeNavigation;
import app.mediczy_com.R;
import app.mediczy_com.Retrofit.ListDetails;
import app.mediczy_com.Retrofit.RetrofitInterface;
import app.mediczy_com.Session.Session;
import app.mediczy_com.dashboard.HomeFragment;
import app.mediczy_com.request.CommonRequest;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.Bluetooth;
import app.mediczy_com.utility.Consts;
import app.mediczy_com.utility.CustomTextViewLight;
import app.mediczy_com.utility.Dbhelper;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Util;
import app.mediczy_com.utility.Validation;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ChatBotActivity extends AppCompatActivity implements Callback<ListDetails> {


    private Toolbar toolbar;
    LinearLayout linear_bottom, linear_type, linear_ed, linear_send, linear_option, linear_yes, linear_no, linear_dont;
    EditText edit_send;
    TextView text_send, text_yes, text_no, text_dont, date;

    C_BotAdapter c_botAdapter;
    RecyclerView chatview;
    LinearLayout linear_second_chat, linear_second_yes, linear_second_no;
    TextView text_second_yes, text_second_no, t_title;
    String Sym_id = "", Syn_name = "", Sym_che = "";

    ArrayList<Mentions> men = new ArrayList<>();
    ArrayList<Mentions> men_value = new ArrayList<>();
    ArrayList<Initial_Sym> in_sys = new ArrayList<>();
    ArrayList<Value_men> post_values = new ArrayList<>();

    ArrayList<Right_Chat> Ch_RI = new ArrayList<>();
    List<Bluetooth> chat_message = new ArrayList<>();
    ArrayList<Diagonose_Value> diagonose = new ArrayList<>();
    ArrayList<Item_Value> Ite_v = new ArrayList<>();
    Dbhelper dbhelper;
    Activity activity;
    int req_type = 0;

    String Send_chat = "";
    LinearLayoutManager layoutManager;
    Session session;

    RetrofitInterface apiInterface;
    Context context;

    ImageView img_go, img_cross;
    EditText edit_feed, edit_age, edit_gender;
    TextView text_submit, text_title, text_rep;
    LinearLayout linear_sumbit, linear_re;
    Validation validation;

    String Name, Age, Gender;
    boolean name, age, gender;
    ArrayList<Static_Value> ques = new ArrayList<>();


    ArrayList<Left_Robert> Rob = new ArrayList<>();

    LinearLayout linear_gender, linear_male, linear_female, line_rob;
    TextView text_male, text_female;

    private String userId;

    private FeatureCoverFlow coverFlow;
    private CoverFlowAdapter adapter;
    private ArrayList<Game> games;
    private ArrayList<Symptom_Pack> sys_pack = new ArrayList<>();
    RelativeLayout kjc;
    ProgressBar progr;
    LinearLayout linea;

    DotProgressBar dotProgressBar;

    //  public final int SPLASH_DISPLAY_LENGTH = 500;
    ImageView rob_rob;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_bot_activity_chat_bot);
        context = ChatBotActivity.this;
        init();
        session = new Session(this);

        validation = new Validation();

        Font();

        // Detail();

        userId = LocalStore.getInstance().getUserID(this);
        MLog.e("userId==>", "" + userId);

        String time = new SimpleDateFormat("hh:mm a").format(new Date());


        // String dates = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dates = new SimpleDateFormat("dd-MMMM-yyyy").format(new Date());
        // date.setText(dates);
        //  Util.setFont(3, context, date, dates);
        // in_sys.add(new Initial_Sym("", "", ""));
        //  men_value.addAll(men);

        linear_type.setVisibility(View.GONE);


        coverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);

        //settingDummyData();


        try {

            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(rob_rob);
            Glide.with(this).load(R.raw.chat_bot_rob).into(imageViewTarget);
        } catch (Exception e) {

            Log.e("", "Debug from onCreate Exception" + e.toString());

        }


        if (Consts.Commom_Flag.equalsIgnoreCase("0")) {


            Rob.add(new Left_Robert("Hi, I am Mediczy!\n\nI can help you if you're feeling unwell.\n\n" +
                    "Just Tell me your Health Symptoms to Start Assessment.", "0", ""));
            Rob.add(new Left_Robert("After your assessment, Mediczy will suggest what you could do next. This may include a visit to a doctor, pharmacist, or specialist, or to seek emergency care.", "0", ""));
            c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
            chatview.setAdapter(c_botAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
            chatview.setLayoutManager(layoutManager);


        } else if (Consts.Commom_Flag.equalsIgnoreCase("1")) {

        } else if (Consts.Commom_Flag.equalsIgnoreCase("2")) {

        }


        //Retrofit


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(RetrofitInterface.class);


        Count();


        linear_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linear_type.setVisibility(View.VISIBLE);
                linear_option.setVisibility(View.GONE);
                edit_send.setInputType(InputType.TYPE_CLASS_NUMBER);

                Consts.Commom_Flag = "2";
                Rob.add(new Left_Robert("How old are you?", "0", "Yes"));
                c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                chatview.setAdapter(c_botAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                chatview.setLayoutManager(layoutManager);
                int newMsgPosition = Rob.size() - 1;
                c_botAdapter.notifyDataSetChanged();
                // Scroll RecyclerView to the last message.
                chatview.scrollToPosition(newMsgPosition);


            }
        });

        linear_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Consts.Commom_Flag = "1";
                Rob.add(new Left_Robert("Do you accept our terms and Conditions?", "0", "No"));
                c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                chatview.setAdapter(c_botAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                chatview.setLayoutManager(layoutManager);
                int newMsgPosition = Rob.size() - 1;
                c_botAdapter.notifyDataSetChanged();
                // Scroll RecyclerView to the last message.
                chatview.scrollToPosition(newMsgPosition);

            }
        });


        linear_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //  InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                //inputMethodManager.toggleSoftInputFromWindow(edit_send.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                // edit_send.requestFocus();


                if (Consts.Commom_Flag.equalsIgnoreCase("2")) {


                    if (edit_send.getText().toString().equalsIgnoreCase("")) {

                        Toast.makeText(ChatBotActivity.this, "Enter Your Age", Toast.LENGTH_SHORT).show();

                    } else {

                        try {


                            int Age = Integer.parseInt(edit_send.getText().toString());
                            if (Age >= 18 && Age <= 130) {


                                Consts.Commom_Flag = "2";
                                Rob.add(new Left_Robert("Are You female or male?", "0", edit_send.getText().toString()));
                                c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                                Consts.Common_age = edit_send.getText().toString();
                                chatview.setAdapter(c_botAdapter);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                                chatview.setLayoutManager(layoutManager);
                                int newMsgPosition = Rob.size() - 1;
                                c_botAdapter.notifyDataSetChanged();
                                // Scroll RecyclerView to the last message.
                                chatview.scrollToPosition(newMsgPosition);
                                edit_send.setText("");
                                linear_type.setVisibility(View.GONE);
                                linear_gender.setVisibility(View.VISIBLE);
                                edit_send.setInputType(InputType.TYPE_CLASS_TEXT);

                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.toggleSoftInputFromWindow(edit_send.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                                edit_send.requestFocus();

                            } else {

                                Consts.Commom_Flag = "2";
                                Rob.add(new Left_Robert("I need to know your Age", "0", edit_send.getText().toString()));
                                c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                                chatview.setAdapter(c_botAdapter);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                                chatview.setLayoutManager(layoutManager);
                                int newMsgPosition = Rob.size() - 1;
                                c_botAdapter.notifyDataSetChanged();
                                // Scroll RecyclerView to the last message.
                                chatview.scrollToPosition(newMsgPosition);
                                edit_send.setText("");

                            }

                        } catch (NumberFormatException r) {
                            r.printStackTrace();
                        }


                    }


                } else {
                    if (edit_send.getText().toString().equalsIgnoreCase("")) {

                        Toast.makeText(ChatBotActivity.this, "Enter the Queries", Toast.LENGTH_SHORT).show();

                    } else {





                     /*   Ch_RI.add(new Right_Chat(edit_send.getText().toString()));
                        if (Ch_RI.size() > 0) {
                            for (int i = 0; i < Ch_RI.size(); i++) {
                                MLog.e("posiyion==>", "podition==>" + Ch_RI.get(i));
                                dbhelper.insert_Message(String.valueOf(i), Ch_RI.get(i).C_Right);
                            }
                        }

                        chat_message = dbhelper.get_Mess();
                        MLog.e("size==>", "size==>" + chat_message.size());*/

                        Web();


                    }
                }


            }
        });

        linear_second_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Consts.Common_Mess = "present";
                Consts.Common_Status = "Yes";

                for (int i = 0; i < Ite_v.size(); i++) {
                    if (diagonose.size() > 0) {
                        if (diagonose.get(i).getChoice_id().equalsIgnoreCase("")) {
                            diagonose.remove(i);
                            diagonose.add(new Diagonose_Value(Ite_v.get(i).id, Ite_v.get(i).name, Consts.Common_Mess, Consts.question_value));

                        } else {
                            diagonose.remove(diagonose.size() - 1);
                            diagonose.add(new Diagonose_Value(Ite_v.get(i).id, Ite_v.get(i).name, Consts.Common_Mess, Consts.question_value));
                        }
                    }


                    Second_Level_Chat();
                }


            }
        });

        linear_second_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Consts.Common_Mess = "absent";
                Consts.Common_Status = "No";

                for (int i = 0; i < Ite_v.size(); i++) {

                    if (diagonose.size() > 0) {
                        if (diagonose.get(i).getChoice_id().equalsIgnoreCase("")) {
                            diagonose.remove(i);
                            diagonose.add(new Diagonose_Value(Ite_v.get(i).id, Ite_v.get(i).name, Consts.Common_Mess, Consts.question_value));

                        } else {
                            diagonose.remove(diagonose.size() - 1);
                            diagonose.add(new Diagonose_Value(Ite_v.get(i).id, Ite_v.get(i).name, Consts.Common_Mess, Consts.question_value));
                        }
                    }
                    Second_Level_Chat();
                }

            }
        });

        linear_dont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Consts.Common_Mess = "unknown";
                Consts.Common_Status = "Don't Know";
                for (int i = 0; i < Ite_v.size(); i++) {
                    if (diagonose.size() > 0) {
                        if (diagonose.get(i).getChoice_id().equalsIgnoreCase("")) {
                            diagonose.remove(i);
                            diagonose.add(new Diagonose_Value(Ite_v.get(i).id, Ite_v.get(i).name, Consts.Common_Mess, Consts.question_value));

                        } else {
                            diagonose.remove(diagonose.size() - 1);
                            diagonose.add(new Diagonose_Value(Ite_v.get(i).id, Ite_v.get(i).name, Consts.Common_Mess, Consts.question_value));
                        }
                    }
                    Second_Level_Chat();
                }
            }
        });


        linear_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChatBotActivity.this, Report_DetailActivity.class);
                startActivity(intent);
                finish();

            }
        });


        linear_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Consts.Common_gender = "male";

                Consts.Commom_Flag = "3";
                Rob.add(new Left_Robert("Thank you!   ", "0", Consts.Common_gender));
                Rob.add(new Left_Robert("What concerns you most about your health?", "0", ""));
                Rob.add(new Left_Robert("Enter your Health Symptoms to Understand what could be wrong (Example:-\"Headache\" \"Fever\" \"Back Pain\")", "0", ""));
                c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                chatview.setAdapter(c_botAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                chatview.setLayoutManager(layoutManager);
                int newMsgPosition = Rob.size() - 1;
                c_botAdapter.notifyDataSetChanged();
                // Scroll RecyclerView to the last message.
                chatview.scrollToPosition(newMsgPosition);
                linear_gender.setVisibility(View.GONE);
                linear_type.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(edit_send.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                edit_send.requestFocus();


            }
        });

        linear_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Consts.Common_gender = "female";

                Consts.Commom_Flag = "3";

                Rob.add(new Left_Robert("Thank you!", "0", Consts.Common_gender));
                Rob.add(new Left_Robert("What concerns you most about your health?", "0", ""));
                Rob.add(new Left_Robert("Please describe your first Major symptoms.", "0", ""));
                c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                chatview.setAdapter(c_botAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                chatview.setLayoutManager(layoutManager);
                int newMsgPosition = Rob.size() - 1;
                c_botAdapter.notifyDataSetChanged();
                // Scroll RecyclerView to the last message.
                chatview.scrollToPosition(newMsgPosition);
                linear_gender.setVisibility(View.GONE);
                linear_type.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(edit_send.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                edit_send.requestFocus();

            }
        });

    }


    private void init() {
        activity = ChatBotActivity.this;
        //   context = ChatBotActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Symptoms Chat");
        dbhelper = new Dbhelper(this);
        chatview = (RecyclerView) findViewById(R.id.chatview);
        linear_re = (LinearLayout) findViewById(R.id.linear_re);
        dotProgressBar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        t_title = (TextView) findViewById(R.id.t_title);
        progr = (ProgressBar) findViewById(R.id.progr);
        linea = (LinearLayout) findViewById(R.id.linea);

        rob_rob = (ImageView) findViewById(R.id.rob_rob);

        kjc = (RelativeLayout) findViewById(R.id.kjc);
        linear_second_chat = (LinearLayout) findViewById(R.id.linear_second_chat);
        linear_second_yes = (LinearLayout) findViewById(R.id.linear_second_yes);
        linear_second_no = (LinearLayout) findViewById(R.id.linear_second_no);

        line_rob = (LinearLayout) findViewById(R.id.line_rob);

        text_second_yes = (TextView) findViewById(R.id.text_second_yes);
        text_second_no = (TextView) findViewById(R.id.text_second_no);


        linear_bottom = (LinearLayout) findViewById(R.id.linear_bottom);
        linear_type = (LinearLayout) findViewById(R.id.linear_type);
        linear_ed = (LinearLayout) findViewById(R.id.linear_ed);
        linear_send = (LinearLayout) findViewById(R.id.linear_send);
        linear_option = (LinearLayout) findViewById(R.id.linear_option);
        linear_yes = (LinearLayout) findViewById(R.id.linear_yes);
        linear_no = (LinearLayout) findViewById(R.id.linear_no);
        linear_dont = (LinearLayout) findViewById(R.id.linear_dont);

        edit_send = (EditText) findViewById(R.id.edit_send);
        text_send = (TextView) findViewById(R.id.text_send);
        text_yes = (TextView) findViewById(R.id.text_yes);
        text_no = (TextView) findViewById(R.id.text_no);
        text_dont = (TextView) findViewById(R.id.text_dont);

/*
        text_time_auto = (TextView) findViewById(R.id.text_time_auto);
        text_time_service = (TextView) findViewById(R.id.text_time_service);
        text_time_case = (TextView) findViewById(R.id.text_time_case);
        text_time_you = (TextView) findViewById(R.id.text_time_you);*/
        date = (TextView) findViewById(R.id.date);

        linear_gender = (LinearLayout) findViewById(R.id.linear_gender);
        linear_male = (LinearLayout) findViewById(R.id.linear_male);
        linear_female = (LinearLayout) findViewById(R.id.linear_female);

        text_male = (TextView) findViewById(R.id.text_male);
        text_female = (TextView) findViewById(R.id.text_female);





      /*  ques.add(new Static_Value("Hi! I'am an automatic sympton checker.This service is for informational purpose and is not a qualified medical opinion ,In case health emergency, call your local emergency number immediately Do you accept our terms and Conditions?"));

        c_botAdapter = new C_BotAdapter(ChatBotActivity.this, men_value, Ch_RI, ques);
        chatview.setAdapter(c_botAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
        chatview.setLayoutManager(layoutManager);*/


    }


    public void Font() {


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            session.setChat_No("");
            session.setChat_Yes("");
            session.setText_chat("");
            chat_message.clear();
            men_value.clear();
            dbhelper.deleteAllRecord();
            Consts.Common_Status = "";
            Consts.Common_Mess = "";
            Consts.Commom_Flag = "0";
            Consts.question_value = "";
            Intent intent = new Intent(ChatBotActivity.this, HomeNavigation.class);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            session.setChat_No("");
            session.setChat_Yes("");
            session.setText_chat("");
            chat_message.clear();
            men_value.clear();
            dbhelper.deleteAllRecord();
            Consts.Common_Status = "";
            Consts.Common_Mess = "";
            Consts.Commom_Flag = "0";
            Consts.question_value = "";
            Intent intent = new Intent(ChatBotActivity.this, HomeNavigation.class);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }


    public void Web() {

        Send_chat = edit_send.getText().toString();

        req_type = 1;
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_query = edit_send.getText().toString();
        commonRequest.is_android = "1";
        commonRequest.inital_symptoms = post_values;
        MLog.e("", "Chat_Bot" + ",\nuser_query===>" + commonRequest.user_query + ",\ninital_symptoms===>" + commonRequest.inital_symptoms);

        Call<ListDetails> userCall = apiInterface.chat_view(commonRequest);
        userCall.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                MLog.e("", "Check obvious:==>" + response.body());

                MLog.e("", "Check obvious:==>" + response.body().toString());
                final List<Mentions> men = response.body().mentions;


                if (men != null)
                    if (!men.isEmpty()) {
                        //ADAPTER

                        for (int i = 0; i < men.size(); i++) {
                            post_values.add(new Value_men(men.get(i).id, men.get(i).common_name, men.get(i).choice_id, edit_send.getText().toString()));


                            if (post_values.size() == 1) {

                                Consts.Commom_Flag = "4";
                                Rob.add(new Left_Robert("I Have noted:" + men.get(i).common_name, "0", edit_send.getText().toString()));
                                Rob.add(new Left_Robert("We need minimum 2 symptoms to proceed further. Please enter one more symptom", "0", ""));
                                c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                                chatview.setAdapter(c_botAdapter);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                                chatview.setLayoutManager(layoutManager);
                                int newMsgPosition = Rob.size() - 1;
                                c_botAdapter.notifyDataSetChanged();
                                // Scroll RecyclerView to the last message.
                                chatview.scrollToPosition(newMsgPosition);

                                if (men.size() == 2) {

                                } else {
                                    edit_send.setText("");
                                }


                            } else {


                                Consts.Commom_Flag = "4";
                                Rob.add(new Left_Robert("I Have noted:" + men.get(i).common_name, "0", edit_send.getText().toString()));
                                Rob.add(new Left_Robert("Is there anything else would you like to report ? if you don't have anymore symptoms then enter No", "0", ""));
                                c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                                chatview.setAdapter(c_botAdapter);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                                chatview.setLayoutManager(layoutManager);
                                int newMsgPosition = Rob.size() - 1;
                                c_botAdapter.notifyDataSetChanged();
                                // Scroll RecyclerView to the last message.
                                chatview.scrollToPosition(newMsgPosition);
                                edit_send.setText("");


                            }


                        }


                    } else {


                        if (post_values.size() >= 2) {
                            linear_type.setVisibility(View.GONE);

                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInputFromWindow(edit_send.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                            edit_send.requestFocus();

                            //Toast.makeText(ChatBotActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                            Second_Level_Chat();

                        } else {

                            //   line_rob.setVisibility(View.VISIBLE);
                           /* new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {*/
                            //   line_rob.setVisibility(View.GONE);
                            Consts.Commom_Flag = "4";
                            Rob.add(new Left_Robert("I'm Sorry but I didn't understand.", "0", edit_send.getText().toString()));
                            Rob.add(new Left_Robert("Please name your symptoms using simple language.", "0", ""));
                            c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                            chatview.setAdapter(c_botAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                            chatview.setLayoutManager(layoutManager);
                            int newMsgPosition = Rob.size() - 1;
                            c_botAdapter.notifyDataSetChanged();
                            // Scroll RecyclerView to the last message.
                            chatview.scrollToPosition(newMsgPosition);
                            edit_send.setText("");

                        }


                        //   }, SPLASH_DISPLAY_LENGTH);


                        // }


                    }
            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(ChatBotActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();

            }
        });

    }


    public void Second_Level_Chat() {

        req_type = 2;
        final CommonRequest commonRequest = new CommonRequest();

        commonRequest.age = Consts.Common_age;
        commonRequest.gender = Consts.Common_gender;
        commonRequest.is_android = "1";
        commonRequest.inital_symptoms = post_values;
        commonRequest.diagnosis_symptoms = diagonose;

        Call<ListDetails> SecondCall = apiInterface.Send_Chat(commonRequest);
        SecondCall.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, final Response<ListDetails> response) {

                MLog.e("", "Check obvious:==>" + response.body());

                MLog.e("", "Check obvious:==>" + response.body().should_stop);


                if (response.body().should_stop.equalsIgnoreCase("true")) {

                    //  Toast.makeText(ChatBotActivity.this, "Chat Over ..!!!", Toast.LENGTH_LONG).show();
                    linear_second_chat.setVisibility(View.GONE);
                    linear_re.setVisibility(View.VISIBLE);

                    Report_Chat();

                } else {


                    linear_second_chat.setVisibility(View.VISIBLE);
                    Consts.question_value = response.body().question.text;

                    Ite_v = response.body().question.items;

                    if (Ite_v != null)
                        if (!Ite_v.isEmpty()) {

                            //ADAPTER
                            for (int i = 0; i < Ite_v.size(); i++) {

                                //Consts.Common_Mess = "";
                                diagonose.add(new Diagonose_Value(Ite_v.get(i).id, Ite_v.get(i).name, Consts.Common_Mess, response.body().question.text));

                                men_value.add(new Mentions("" + response.body().question.text));


                                if (Consts.Common_Status.equalsIgnoreCase("")) {

                                } else {


                                }


                                if (Consts.Commom_Flag.equalsIgnoreCase("4")) {


                                    // line_rob.setVisibility(View.VISIBLE);
                                    //  new Handler().postDelayed(new Runnable() {
                                    //   @Override
                                    //     public void run() {
                                    //       line_rob.setVisibility(View.GONE);

                                    Consts.Commom_Flag = "4";
                                    Rob.add(new Left_Robert("Okay, let me ask you a couple of questions.", "0", edit_send.getText().toString()));
                                    Rob.add(new Left_Robert(response.body().question.text, "0", ""));
                                    c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                                    chatview.setAdapter(c_botAdapter);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                                    chatview.setLayoutManager(layoutManager);
                                    int newMsgPosition = Rob.size() - 1;
                                    c_botAdapter.notifyDataSetChanged();
                                    // Scroll RecyclerView to the last message.
                                    chatview.scrollToPosition(newMsgPosition);
                                    edit_send.setText("");
                                    Consts.Commom_Flag = "5";

                                    //   }


                                    //   }, SPLASH_DISPLAY_LENGTH);


                                } else if (Consts.Commom_Flag.equalsIgnoreCase("5")) {

                                    //  line_rob.setVisibility(View.VISIBLE);
                                    //    new Handler().postDelayed(new Runnable() {
                                    ///     @Override
                                    //    public void run() {
                                    ///   line_rob.setVisibility(View.GONE);
                                    Rob.add(new Left_Robert(response.body().question.text, "0", Consts.Common_Status));
                                    c_botAdapter = new C_BotAdapter(ChatBotActivity.this, Rob);
                                    chatview.setAdapter(c_botAdapter);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(ChatBotActivity.this);
                                    chatview.setLayoutManager(layoutManager);
                                    int newMsgPosition = Rob.size() - 1;
                                    c_botAdapter.notifyDataSetChanged();
                                    // Scroll RecyclerView to the last message.
                                    chatview.scrollToPosition(newMsgPosition);
                                    edit_send.setText("");
                                    Consts.Commom_Flag = "5";

                                    //    }


                                    //   }, SPLASH_DISPLAY_LENGTH);


                                }


                            }


                        } else {

                        }

                }
            }


            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(ChatBotActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();


            }
        });
    }


    public void Report_Chat() {


        req_type = 2;
        final CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_id = userId;
        commonRequest.age = Consts.Common_age;
        commonRequest.gender = Consts.Common_gender;
        commonRequest.is_android = "1";
        commonRequest.inital_symptoms = post_values;
        commonRequest.diagnosis_symptoms = diagonose;

        Call<ListDetails> RepCall = apiInterface.Report_Chat(commonRequest);
        RepCall.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                MLog.e("", "Report Status:==>" + response.body());

                MLog.e("", "Report Status:==>" + response.body().status);


                if (response.body().status.equalsIgnoreCase("success")) {

                    //  Toast.makeText(ChatBotActivity.this, response.body().msg, Toast.LENGTH_LONG).show();
                    session.setView_id(response.body().user_bot_report_id);
                    //Consts.user_bot_report_id = response.body().user_bot_report_id;

                    Log.e("Report_Id", "Report_id==>" + session.getView_id());

                    // Toast.makeText(ChatBotActivity.this, "IN PROGRESS ..!!!", Toast.LENGTH_LONG).show();

                } else {


                }

            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(ChatBotActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();


            }
        });
    }


    public void Count() {

        linea.setVisibility(View.VISIBLE);
        final CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_id = userId;


        Call<ListDetails> cou = apiInterface.Sys_count(commonRequest);
        cou.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                MLog.e("", "Counts Status:==>" + response.body());

                MLog.e("", "Counts Count:==>" + response.body().status);


                if (response.body().status.equalsIgnoreCase("success")) {
                    linea.setVisibility(View.GONE);


                    MLog.e("", "Counts Count:==>" + response.body().remaining_count);

                    t_title.setText("Credit : " + response.body().remaining_count);

                    // Package();


                    if (response.body().remaining_count.equalsIgnoreCase("0")) {
                        Package();

                    } else {


                    }


                } else {

                    Toast.makeText(ChatBotActivity.this, "Invaild User Contact Admin..!!!", Toast.LENGTH_LONG).show();
                    // Package();
                    Intent intent = new Intent(ChatBotActivity.this, HomeNavigation.class);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    startActivity(intent);
                    finish();
                    linea.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(ChatBotActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
                linea.setVisibility(View.GONE);

            }
        });
    }


    public void Package() {


        //final CommonRequest commonRequest = new CommonRequest();
        // commonRequest.user_id = userId;


        Call<ListDetails> offer_pack = apiInterface.pack();
        offer_pack.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                MLog.e("", "Package Status:==>" + response.body());

                MLog.e("", "Package Count:==>" + response.body().status);


                if (response.body().status.equalsIgnoreCase("success")) {

                    sys_pack = response.body().symptom_packages;


                    adapter = new CoverFlowAdapter(ChatBotActivity.this, sys_pack);
                    coverFlow.setAdapter(adapter);
                    coverFlow.setOnScrollPositionListener(onScrollListener());
                    kjc.setVisibility(View.VISIBLE);
                    linear_option.setVisibility(View.GONE);

                } else {


                }

            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(ChatBotActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

    }

    @Override
    public void onFailure(Call<ListDetails> call, Throwable t) {


    }

    private FeatureCoverFlow.OnScrollPositionListener onScrollListener() {
        return new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                Log.e("MainActiivty", "position: " + position);
            }

            @Override
            public void onScrolling() {
                Log.e("MainActivity", "scrolling");
            }
        };
    }

    private void settingDummyData() {
        games = new ArrayList<>();
        games.add(new Game(R.drawable.chat_bot_image1, "Assassin Creed 3"));
        games.add(new Game(R.drawable.chat_bot_image2, "Avatar 3D"));
        games.add(new Game(R.drawable.chat_bot_image3, "Call Of Duty Black Ops 3"));
        games.add(new Game(R.drawable.chat_bot_image4, "DotA 2"));
        games.add(new Game(R.drawable.chat_bot_image5, "Halo 5"));
        games.add(new Game(R.drawable.chat_bot_image6, "Left 4 Dead 2"));
        games.add(new Game(R.drawable.chat_bot_image7, "StarCraft"));
        games.add(new Game(R.drawable.chat_bot_image8, "The Witcher 3"));
        games.add(new Game(R.drawable.chat_bot_image9, "Tom raider 3"));
        games.add(new Game(R.drawable.chat_bot_image10, "Need for Speed Most Wanted"));
    }

}
