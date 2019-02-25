package app.mediczy_com.utility;

/**
 * Created by ravi on 20/02/18.
 */

public class Bluetooth {
    public static final String TABLE_NAME = "notes";

    public static final String TABLE_CHAT_INITAL = "chat_initial";

    public static final String KEY_CHAT_COMMON_NAME = "common_name";
    public static final String KEY_CHAT_CHOICE_ID = "choice_id";
    public static final String KEY_SYMPTON_ID = "sympton_id";
    public static final String COLUMN_ID = "c_id";


    public static final String TABLE_POSTION = "chat_postion";

    public static final String KEY_POSSION = "Message_postion";

    public static final String KEY_MESSAGE = "Message_write";


    public static final String CREATE_TABLE_CHAT_INITAIL = "CREATE TABLE " + TABLE_CHAT_INITAL + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_SYMPTON_ID + " TEXT  ,"
            + KEY_CHAT_COMMON_NAME + " TEXT  ,"
            + KEY_CHAT_CHOICE_ID + " TEXT" + ")";


    public static final String CREATE_TABLE_POSITION = "CREATE TABLE " + TABLE_POSTION + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_POSSION + " TEXT  ,"
            + KEY_MESSAGE + " TEXT" + ")";


    private int id;
    String inspect_date;
    String symptom_id;
    String common_name;
    String choice_id;


    public String Mess_pos;
    public String Mess_tex;

    public static String getTableName() {
        return TABLE_NAME;
    }

    public String getMess_pos() {
        return Mess_pos;
    }

    public void setMess_pos(String mess_pos) {
        Mess_pos = mess_pos;
    }

    public String getMess_tex() {
        return Mess_tex;
    }

    public void setMess_tex(String mess_tex) {
        Mess_tex = mess_tex;
    }

    public Bluetooth() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bluetooth(String symptom_id, String common_name, String choice_id) {
        this.symptom_id = symptom_id;
        this.common_name = common_name;
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

    public String getChoice_id() {
        return choice_id;
    }

    public void setChoice_id(String choice_id) {
        this.choice_id = choice_id;
    }
}
