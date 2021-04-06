package rebus.llc.parvoz.others;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private static final String APP_SHARED_PREFS    = "CreatFragments";
    private SharedPreferences _sharedPrefs;
    private SharedPreferences.Editor _prefsEditor;
    public static final String LANG                 = "landuage";

    public static final String LOGIN                = "login";
    public static final String PASSWORD             = "password";
    public static final String PASSWORD_2           = "password_2";
    public static final String TOKEN                = "token";


    public static final String SERVER_URL           = "server_url";

    public static final String USER_NAME            = "user_name";
    public static final String PROJECT_NAME         = "project_name";
    public static final String ID_PROJECT           = "id_project";
    public static final String ID_USER              = "id_user";

    public static final String USER_PHONE           = "user_phone";
    public static final String USER_CARNUMBER       = "user_car_number";
    public static final String USER_CAR_MODEL       = "user_car_model";

    public static String ID_REQUEST = "id_request";
    public static final String MY_LAST_LOCATION_LAT    = "my_last_locaton_lat";
    public static final String MY_LAST_LOCATION_LONG   = "my_last_locaton_long";


    public static final String ID_MENTOR     = "id_mentor";
    public static final String MENTOR_NAME   = "mentor_name";
    public static final String MENTOR_PHONE  = "mentor_phone";
    public static final String MENTOR_EMAIL  = "mentor_email";

    public static final String PAYMENTS_REVISION            = "payments_revision";
    public static final String PROJECT_REFERENCES_REVISION  = "project_references_revision";
    public static final String GLOCAL_REFERENCES_REVISION   = "global_references_revision";

    public static final String CONFIRM_NOTIF_COUNT   = "confirm_notif";
    public static final String UNCONFIRM_NOTIF_COUNT   = "unconfirm_notif";

    public static final String GET_PROJECT_DATA   = "get_project_data";

    public Settings(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();

        if(getPassword_2() == null){
            savePassword_2("");
        }
    }

    public String getToken(){
        return _sharedPrefs.getString(TOKEN, "");
    }

    public void saveToken(String text){
        _prefsEditor.putString(TOKEN, text);
        _prefsEditor.commit();
    }


    public String getLang(){
        return _sharedPrefs.getString(LANG, "ru");
    }

    public void saveLang(String text){
        _prefsEditor.putString(LANG, text);
        _prefsEditor.commit();
    }


    public String getLogin() {//получение логина
        return _sharedPrefs.getString(LOGIN, "");
    }

    public void saveLogin(String text) {//сохранение логина
        _prefsEditor.putString(LOGIN, text);
        _prefsEditor.commit();
    }

    public String getPassword() {//получение паролья
        return _sharedPrefs.getString(PASSWORD, "");
    }

    public void savePassword(String text) {//сохранение паролья
        _prefsEditor.putString(PASSWORD, text);
        _prefsEditor.commit();
    }

    public String getPassword_2() {//получение паролья
        return _sharedPrefs.getString(PASSWORD_2, "");
    }

    public void savePassword_2(String text) {//сохранение паролья
        _prefsEditor.putString(PASSWORD_2, text);
        _prefsEditor.commit();
    }

    public String getIdRequest() {//получение логина
        return _sharedPrefs.getString(ID_REQUEST, "");
    }

    public void saveIdRequest(String text) {//сохранение логина
        _prefsEditor.putString(ID_REQUEST, text);
        _prefsEditor.commit();
    }

    public String getMyLastLocationLat() {//получение логина
        return _sharedPrefs.getString(MY_LAST_LOCATION_LAT, "");
    }

    public void saveMyLastLocationLat(String text) {//сохранение логина
        _prefsEditor.putString(MY_LAST_LOCATION_LAT, text);
        _prefsEditor.commit();
    }

    public String getMyLastLocationLong() {//получение логина
        return _sharedPrefs.getString(MY_LAST_LOCATION_LONG, "");
    }

    public void saveMyLastLocationLong(String text) {//сохранение логина
        _prefsEditor.putString(MY_LAST_LOCATION_LONG, text);
        _prefsEditor.commit();
    }



    public String getUserName() {//получение логина
        return _sharedPrefs.getString(USER_NAME, "");
    }

    public void saveUserName(String text) {//сохранение логина
        _prefsEditor.putString(USER_NAME, text);
        _prefsEditor.commit();
    }


    public String getUserPhone() {//получение логина
        return _sharedPrefs.getString(USER_PHONE, "");
    }

    public void saveUserPhone(String text) {//сохранение логина
        _prefsEditor.putString(USER_PHONE, text);
        _prefsEditor.commit();
    }


    public String getUserCarnumber() {//получение логина
        return _sharedPrefs.getString(USER_CARNUMBER, "");
    }

    public void saveUserCarnumber(String text) {//сохранение логина
        _prefsEditor.putString(USER_CARNUMBER, text);
        _prefsEditor.commit();
    }




    public String getProjectName() {
        return _sharedPrefs.getString(PROJECT_NAME, "");
    }

    public void saveProjectName(String text) {
        _prefsEditor.putString(PROJECT_NAME, text);
        _prefsEditor.commit();
    }

    public String getIdProject() {
        return _sharedPrefs.getString(ID_PROJECT, "");
    }

    public void saveIdProject(String text) {
        _prefsEditor.putString(ID_PROJECT, text);
        _prefsEditor.commit();
    }

    public String getIdUser() {
        return _sharedPrefs.getString(ID_USER, "");
    }

    public void saveIdUser(String text) {
        _prefsEditor.putString(ID_USER, text);
        _prefsEditor.commit();
    }



    public void saveId_mentor(String text) {
        _prefsEditor.putString(ID_MENTOR, text);
        _prefsEditor.commit();
    }

    public String getId_mentor() {
        return _sharedPrefs.getString(ID_MENTOR, "");
    }


    public void saveMentor_name(String text) {
        _prefsEditor.putString(MENTOR_NAME, text);
        _prefsEditor.commit();
    }

    public String getMentor_name() {
        return _sharedPrefs.getString(MENTOR_NAME, "");
    }



    public void saveMentor_phone(String text) {
        _prefsEditor.putString(MENTOR_PHONE, text);
        _prefsEditor.commit();
    }

    public String getMentor_phone() {
        return _sharedPrefs.getString(MENTOR_PHONE, "");
    }



    public void saveMentor_email(String text) {
        _prefsEditor.putString(MENTOR_EMAIL, text);
        _prefsEditor.commit();
    }

    public String getMentor_email() {
        return _sharedPrefs.getString(MENTOR_EMAIL, "");
    }

    public void savePaymentsRevision(String text) {
        _prefsEditor.putString(PAYMENTS_REVISION, text);
        _prefsEditor.commit();
    }

    public String getPaymentsRevision() {
        return _sharedPrefs.getString(PAYMENTS_REVISION, "");
    }

    public void saveGlocalReferencesRevision(String text) {
        _prefsEditor.putString(GLOCAL_REFERENCES_REVISION, text);
        _prefsEditor.commit();
    }

    public String getGlocalReferencesRevision() {
        return _sharedPrefs.getString(GLOCAL_REFERENCES_REVISION, "");
    }

    public void saveProjectReferencesRevision(String text) {
        _prefsEditor.putString(PROJECT_REFERENCES_REVISION, text);
        _prefsEditor.commit();
    }

    public String getProjectReferencesRevision() {
        return _sharedPrefs.getString(PROJECT_REFERENCES_REVISION, "");
    }

    public void saveConfrimNotifCount(String text) {
        _prefsEditor.putString(CONFIRM_NOTIF_COUNT, text);
        _prefsEditor.commit();
    }

    public String getConfrimNotifCount() {
        return _sharedPrefs.getString(CONFIRM_NOTIF_COUNT, "");
    }


    public void saveUnconfirmNotifCount(String text) {
        _prefsEditor.putString(UNCONFIRM_NOTIF_COUNT, text);
        _prefsEditor.commit();
    }

    public String getUnconfirmNotifCount() {
        return _sharedPrefs.getString(UNCONFIRM_NOTIF_COUNT, "");
    }

    public void saveGetProjectData(String text) {
        _prefsEditor.putString(GET_PROJECT_DATA, text);
        _prefsEditor.commit();
    }

    public String getGetProjectData() {
        return _sharedPrefs.getString(GET_PROJECT_DATA, "");
    }


    public void saveServerUrl(String text) {
        _prefsEditor.putString(SERVER_URL, text);
        _prefsEditor.commit();
    }

    public String getServerUrl() {
        return _sharedPrefs.getString(SERVER_URL, "");
    }

}


