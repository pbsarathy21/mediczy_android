package app.mediczy_com.ChatBot;

/**
 * Created by bala on 8/29/2018.
 */

public class Initial_Sym {

    public String choice_id;
    public String symptom_id;
    public String common_name;

    public Initial_Sym(String choice_id, String symptom_id, String common_name) {
        this.choice_id = choice_id;
        this.symptom_id = symptom_id;
        this.common_name = common_name;
    }

    public String getChoice_id() {
        return choice_id;
    }

    public void setChoice_id(String choice_id) {
        this.choice_id = choice_id;
    }

    public String getSymptom_id() {
        return symptom_id;
    }

    public void setSymptom_id(String symptom_id) {
        this.symptom_id = symptom_id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }
}
