package app.mediczy_com.ChatBot;

/**
 * Created by bala on 9/7/2018.
 */

public class Left_Robert {

    public Left_Robert(String robert_String, String common_Robert_Id) {
        Robert_String = robert_String;
        Common_Robert_Id = common_Robert_Id;
    }

    public String Robert_String;

    public String getRobert_String() {
        return Robert_String;
    }

    public void setRobert_String(String robert_String) {
        Robert_String = robert_String;
    }

    public String getCommon_Robert_Id() {
        return Common_Robert_Id;
    }

    public void setCommon_Robert_Id(String common_Robert_Id) {
        Common_Robert_Id = common_Robert_Id;
    }

    public String Common_Robert_Id;
    public String Check;

    public String getCheck() {
        return Check;
    }

    public void setCheck(String check) {
        Check = check;
    }

    public Left_Robert(String robert_String, String common_Robert_Id, String check) {
        Robert_String = robert_String;
        Common_Robert_Id = common_Robert_Id;
        Check = check;

    }
}
