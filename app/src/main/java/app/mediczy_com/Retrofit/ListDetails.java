package app.mediczy_com.Retrofit;


import java.util.ArrayList;
import java.util.List;

import app.mediczy_com.ChatBot.Bot_Report;
import app.mediczy_com.ChatBot.Limitee;
import app.mediczy_com.ChatBot.Mentions;
import app.mediczy_com.ChatBot.Present_User_Bot;
import app.mediczy_com.ChatBot.Question;
import app.mediczy_com.ChatBot.Symptom_Pack;
import app.mediczy_com.ChatBot.User_Re_List;
import app.mediczy_com.ChatBot.User_bot_Sys;
import app.mediczy_com.Slider.Slider_list;
import app.mediczy_com.webservicemodel.response.DoctorListResponse;

public class ListDetails {
    public String status;


    public String obvious;

    public String free_code_count;

    public List<Mentions> mentions;


    public String text;
    public Question question;

    public String should_stop;

    public Bot_Report user_bot_report;


    public ArrayList<User_bot_Sys> initial_user_bot_symptoms;

    public ArrayList<Present_User_Bot> present_user_bot_symptoms;
    public ArrayList<Present_User_Bot> absent_user_bot_symptoms;
    public ArrayList<Present_User_Bot> unknown_user_bot_symptoms;

    public ArrayList<Limitee> limit_conditions;
    public List<DoctorListResponse> doctors;

    public ArrayList<Symptom_Pack> symptom_packages = new ArrayList<>();

    public ArrayList<Slider_list> splash_screens;


    public String msg;
    public String user_bot_report_id;
    public ArrayList<User_Re_List> user_bot_reports;


    public String remaining_count;

}
