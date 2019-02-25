package app.mediczy_com.ChatBot;

/**
 * Created by bala on 8/29/2018.
 */

public   class Mentions {
    public String i_c;
    public String choice_id;
    public String type;
    public String name;
    public String common_name;
    public String orth;
    public String id;
    public String Comman_ques;
    public String value_ck;

    public String getValue_ck() {
        return value_ck;
    }


    public void setValue_ck(String value_ck) {
        this.value_ck = value_ck;
    }

    public Mentions(String i_c, String choice_id, String type, String name, String common_name, String orth, String id) {
        this.i_c = i_c;
        this.choice_id = choice_id;
        this.type = type;
        this.name = name;
        this.common_name = common_name;
        this.orth = orth;
        this.id = id;
    }


    public Mentions(String Comman_ques) {
        this.Comman_ques = Comman_ques;
    }


    public String getChoice_id() {
        return choice_id;
    }

    public void setChoice_id(String choice_id) {
        this.choice_id = choice_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getOrth() {
        return orth;
    }

    public void setOrth(String orth) {
        this.orth = orth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
