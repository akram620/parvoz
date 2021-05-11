package rebus.llc.parvoz.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import rebus.llc.parvoz.models.AirportModel;
import rebus.llc.parvoz.models.CitiesModel;
import rebus.llc.parvoz.models.CountryModel;
import rebus.llc.parvoz.models.HorizontalListModel;
import rebus.llc.parvoz.models.LangModel;
import rebus.llc.parvoz.models.MyTicketModel;
import rebus.llc.parvoz.models.ObjavlenijaModel;
import rebus.llc.parvoz.models.Spravochniki;
import rebus.llc.parvoz.models.TicketModel;
import rebus.llc.parvoz.models.UserData;
import rebus.llc.parvoz.others.Settings;

public class DBSample {

        static Settings settings;
        private static final boolean DEBUG = true;
        private static final String LOG_TAG = "DBSimple";

        public static boolean createDb(Context context){//Этот метод создает БД
            DatabaseHelper databaseHelper;
            databaseHelper = new DatabaseHelper(context);
            // создаем базу данных
            databaseHelper.create_db();
            return true;
        }

    public static ArrayList<String[]> getCitiesForSpinner (Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        ArrayList<String[]> stringLists = new ArrayList<>();

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;
        long index;


        sqlQuery = " select cities.* "
                + " from cities  "
                + " where  cities.id > 0 "
                + " order by name ASC ";

        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 0;

        String [] list_id = new String[curSelect.getCount()];
        String [] list_name = new String[curSelect.getCount()];

        while (curSelect.moveToNext()) {

            String id_source =  curSelect.getString(curSelect.getColumnIndex("id"));
            String name      =  curSelect.getString(curSelect.getColumnIndex("name"));

            if(id_source != null) list_id[i] = id_source;
            if(name != null)list_name[i] = name;

            i++;

        }

        stringLists.add(list_id);
        stringLists.add(list_name);

        dbSel.close();
        return stringLists;
    }

    public static ArrayList<String[]> getCountryForSpinner(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        ArrayList<String[]> stringLists = new ArrayList<>();

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;
        long index;


        sqlQuery = " select countries.* "
                + " from countries  "
                + " where  countries.id > 0 "
                + " order by id ASC ";

        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 0;

        String [] list_id = new String[curSelect.getCount()];
        String [] list_name = new String[curSelect.getCount()];
        String [] list_code = new String[curSelect.getCount()];

        while (curSelect.moveToNext()) {

            String id_source =  curSelect.getString(curSelect.getColumnIndex("id"));
            String name      =  curSelect.getString(curSelect.getColumnIndex("name"));
            String code      =  curSelect.getString(curSelect.getColumnIndex("code"));

            if(id_source != null) list_id[i] = id_source;
            if(name != null)list_name[i] = name;
            if(code != null)list_code[i] = "+"+code;

            i++;
        }

        stringLists.add(list_id);
        stringLists.add(list_name);
        stringLists.add(list_code);

        dbSel.close();
        return stringLists;
    }


    public static ArrayList<String[]> getLanguagesForSpinner (Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        ArrayList<String[]> stringLists = new ArrayList<>();

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;
        long index;


        sqlQuery = " select languages.* "
                + " from languages  "
                + " where  languages.id > 0 "
                + " order by id ASC ";

        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 0;

        String [] list_id = new String[curSelect.getCount()];
        String [] list_name = new String[curSelect.getCount()];
        String [] list_alias = new String[curSelect.getCount()];

        while (curSelect.moveToNext()) {

            String id_source =  curSelect.getString(curSelect.getColumnIndex("id"));
            String name      =  curSelect.getString(curSelect.getColumnIndex("name"));
            String alias      =  curSelect.getString(curSelect.getColumnIndex("alias"));

            if(id_source != null) list_id[i] = id_source;
            if(name != null)list_name[i] = name;
            if(alias != null)list_alias[i] = alias;

            i++;

        }

        stringLists.add(list_id);
        stringLists.add(list_name);
        stringLists.add(list_alias);

        dbSel.close();
        return stringLists;
    }

    public static ArrayList<String[]> getUserNumbers (Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        ArrayList<String[]> stringLists = new ArrayList<>();

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;
        long index;


        sqlQuery = " select phones.* "
                + " from phones  "
                + " where  phones.id > 0 "
                + " order by phones.id DESC ";

        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 0;

        String [] list_id = new String[curSelect.getCount()+1];
        String [] list_name = new String[curSelect.getCount()+1];

        while (curSelect.moveToNext()) {

            String id_source =  curSelect.getString(curSelect.getColumnIndex("id"));
            String name      =  curSelect.getString(curSelect.getColumnIndex("number"));

            if(id_source != null) list_id[i] = id_source;
            if(name != null)list_name[i] = name;

            i++;

        }

        list_id[curSelect.getCount()] = "0";
        list_name[curSelect.getCount()] = "На другой номер";

        stringLists.add(list_id);
        stringLists.add(list_name);

        dbSel.close();
        return stringLists;
    }



    public static boolean  execSQL(Context context, String request, Object[] objects) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);
        String address_content = "";
        SQLiteDatabase dbSel = databaseHelper.open();
        dbSel.beginTransaction();
        dbSel.execSQL(request, objects);
        dbSel.setTransactionSuccessful();//Делаем транзакцию
        dbSel.endTransaction();
        dbSel.close();

        return true;
    }

    public static void addUserData(Context context, UserData model) {
        execSQL(context," insert into users("+
                        "id, "+
                        "surname, "+
                        "first_name, "+
                        "patronymic, "+
                        "gender_id, "+
                        "birthday, "+
                        "passport_series, "+
                        "passport_number, "+
                        "passport_expires, "+
                        "spoken_language_id, "+
                        "country_id, "+
                        "email, "+
                        "login, "+
                        "token "+
                        " )"+
                        "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                new Object[]{
                        model.getId(),
                        model.getSurname(),
                        model.getFirst_name(),
                        model.getPatronymic(),
                        model.getGender_id(),
                        model.getBirthday(),
                        model.getPassport_series(),
                        model.getPassport_number(),
                        model.getPassport_expires(),
                        model.getSpoken_language_id(),
                        model.getCountry_id(),
                        model.getEmail(),
                        model.getLogin(),
                        model.getToken()
                });

    }

    public static void updateUserData(Context context, UserData model) {
        execSQL(context," update users set "+
                        "surname=?, "+
                        "first_name=?, "+
                        "patronymic=?, "+
                        "gender_id=?, "+
                        "birthday=?, "+
                        "passport_series=?, "+
                        "passport_number=?, "+
                        "passport_expires=?, "+
                        "spoken_language_id=?, "+
                        "country_id=?, "+
                        "email=?, "+
                        "login=?, "+
                        "token=?, "+
                        "photo_base64=? "+
                        "where id = 1 ",
                new Object[]{
                        model.getSurname(),
                        model.getFirst_name(),
                        model.getPatronymic(),
                        model.getGender_id(),
                        model.getBirthday(),
                        model.getPassport_series(),
                        model.getPassport_number(),
                        model.getPassport_expires(),
                        model.getSpoken_language_id(),
                        model.getCountry_id(),
                        model.getEmail(),
                        model.getLogin(),
                        model.getToken(),
                        model.getPhoto_doc()
                });

    }



    public static void addFlight(Context context, MyTicketModel model) {
        execSQL(context," insert into my_flights("+
                        "status_id, "+
                        "gorod_vyleta_id, "+
                        "gorod_prileta_id, "+
                        "data_vyleta, "+
                        "user_id, "+
                        "updated_at, "+
                        "created_at, "+
                        "id, "+
                        "surname, "+
                        "first_name, "+
                        "patronymic, "+
                        "birthday, "+
                        "gender_id, "+
                        "passport_series, "+
                        "passport_number, "+
                        "passport_expires, "+
                        "aeroport_vyleta_id, "+
                        "aeroport_prileta_id, "+
                        "country_id, "+
                        "stoimost, "+
                        "nomer_bileta, "+
                        "nomer_rejsa, "+
                        "vremja_vyleta, "+
                        "data_prileta, "+
                        "vremja_prileta, "+
                        "valjuta, "+
                        "bagazh "+

                        " )"+
                        "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                new Object[]{
                        model.getStatus_id(),
                        model.getGorod_vyleta_id(),
                        model.getGorod_prileta_id(),
                        model.getData_vyleta(),
                        model.getUser_id(),
                        model.getUpdated_at(),
                        model.getCreated_at(),
                        model.getId(),
                        model.getSurname(),
                        model.getFirst_name(),
                        model.getPatronymic(),
                        model.getBirthday(),
                        model.getGender_id(),
                        model.getPassport_series(),
                        model.getPassport_number(),
                        model.getPassport_expires(),
                        model.getAeroport_vyleta_id(),
                        model.getAeroport_prileta_id(),
                        model.getCountry_id(),
                        model.getStoimost(),
                        model.getNomer_bileta(),
                        model.getNomer_rejsa(),
                        model.getVremja_vyleta(),
                        model.getData_prileta(),
                        model.getVremja_prileta(),
                        model.getValjuta(),
                        model.getBagazh()

                });

    }


    public static void updateMyFlightData(Context context, MyTicketModel model, int id) {
        execSQL(context," update users set "+
                        "status_id=?, "+
                        "gorod_vyleta_id=?, "+
                        "gorod_prileta_id=?, "+
                        "data_vyleta=?, "+
                        "user_id=?, "+
                        "updated_at=?, "+
                        "created_at=?, "+
                        "surname=?, "+
                        "first_name=?, "+
                        "patronymic=?, "+
                        "birthday=?, "+
                        "gender_id=?, "+
                        "passport_series=?, "+
                        "passport_number=?, "+
                        "passport_expires=?, "+
                        "aeroport_vyleta_id=?, "+
                        "aeroport_prileta_id=?, "+
                        "country_id=?, "+
                        "stoimost=?, "+
                        "nomer_bileta=?, "+
                        "nomer_rejsa=?, "+
                        "vremja_vyleta=?, "+
                        "data_prileta=?, "+
                        "vremja_prileta=? "+
                        "where id = "+id,
                new Object[]{
                        model.getStatus_id(),
                        model.getGorod_vyleta_id(),
                        model.getGorod_prileta_id(),
                        model.getData_vyleta(),
                        model.getUser_id(),
                        model.getUpdated_at(),
                        model.getCreated_at(),
                        model.getSurname(),
                        model.getFirst_name(),
                        model.getPatronymic(),
                        model.getBirthday(),
                        model.getGender_id(),
                        model.getPassport_series(),
                        model.getPassport_number(),
                        model.getPassport_expires(),
                        model.getAeroport_vyleta_id(),
                        model.getAeroport_prileta_id(),
                        model.getCountry_id(),
                        model.getStoimost(),
                        model.getNomer_bileta(),
                        model.getNomer_rejsa(),
                        model.getVremja_vyleta(),
                        model.getData_prileta(),
                        model.getVremja_prileta()
                });

    }

    public static void cencelMyFlightData(Context context,int id) {
        execSQL(context," update my_flights set "+
                " status_id= 'otmenjon' "+
                "where id = ? ",  new Object[]{
                    id
                }

        );

    }


    public static void updateMyFlightAlamStatus_7(Context context, int id) {
        Log.d("Connection", ""+id);
        execSQL(context," update my_flights set "+
                " second_alarm = 1 "+
                " where id = ?",  new Object[]{id}

        );

    }

    public static void updateMyFlightAlamStatus_24(Context context, int id) {
        Log.d("Connection", ""+id);
        execSQL(context," update my_flights set "+
                " first_alarm = 1 "+
                " where id = ?",  new Object[]{id}

        );

    }



    public static void updateMyFlightAlamStatus(Context context, String status, int id) {
            Log.d("Connection", ""+id);
        execSQL(context," update my_flights set "+
                        status+"= 1 "+
                        "where id = ?",  new Object[]{ id}

        );

    }


    public static void addFActualnyeRejsi(Context context, TicketModel model) {
        execSQL(context," insert into flights("+
                        "id, "+
                        "gorod_vyleta_id, "+
                        "gorod_prileta_id, "+
                        "data_vyleta, "+
                        "vremja_vyleta, "+
                        "data_prileta, "+
                        "vremja_prileta, "+
                        "aeroport_vyleta_id, "+
                        "aeroport_prileta_id, "+
                        "stoimost, "+
                        "nomer_bileta, "+
                        "nomer_rejsa, "+
                        "valjuta "+

                        " )"+
                        "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                new Object[]{
                        model.getId(),
                        model.getGorod_vyleta_id(),
                        model.getGorod_prileta_id(),
                        model.getData_vyleta(),
                        model.getVremja_vyleta(),
                        model.getData_prileta(),
                        model.getVremja_prileta(),
                        model.getAeroport_vyleta_id(),
                        model.getAeroport_prileta_id(),
                        model.getStoimost(),
                        model.getNomer_bileta(),
                        model.getNomer_rejsa(),
                        model.getValjuta()

                });

    }

    public static void addSpravochniki(Context context, Spravochniki model) {
            cleanSpravochniki(context);
            int i = 0;
            boolean adding = true;
            while (adding){
                int c = 0;
//                if(i < model.getStrany().size()){
//                    addStrany(context, model.getStrany().get(i));
//                }else{
//                    c++;
//                }
                if(i < model.getGoroda().size()){
                    addGoroda(context, model.getGoroda().get(i));
                }else{
                    c++;
                }
                if(i < model.getLanguages().size()){
                    addLanguages(context, model.getLanguages().get(i));
                }else{
                    c++;
                }

                if(i < model.getAeroporty().size()){
                    addAeroport(context, model.getAeroporty().get(i));
                }else{
                    c++;
                }



                if(c == 3){
                    adding = false;
                    break;
                }
                i++;
            }
    }

    public static boolean  cleanPhones(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);
        String address_content = "";
        SQLiteDatabase dbSel = databaseHelper.open();
        dbSel.delete("phones", null, null);

        dbSel.close();
        return true;
    }

    public static boolean  cleanMyFlights(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);
        String address_content = "";
        SQLiteDatabase dbSel = databaseHelper.open();
        dbSel.delete("my_flights", null, null);

        dbSel.close();
        return true;
    }

    public static boolean  cleanObjavlenija(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);
        String address_content = "";
        SQLiteDatabase dbSel = databaseHelper.open();
        dbSel.delete("objavlenija", null, null);

        dbSel.close();
        return true;
    }

    public static boolean  cleanFlights(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);
        String address_content = "";
        SQLiteDatabase dbSel = databaseHelper.open();
        dbSel.delete("flights", null, null);

        dbSel.close();
        return true;
    }

    public static boolean  cleanTicketDocs(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);
        String address_content = "";
        SQLiteDatabase dbSel = databaseHelper.open();

        dbSel.delete("documents", "id_my_flight != ? ", new String[]{Integer.toString(0)});

        dbSel.close();
        return true;
    }

    public static boolean  cleanSpravochniki(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        String address_content = "";

        SQLiteDatabase dbSel = databaseHelper.open();

//        dbSel.delete("countries", null, null);
        dbSel.delete("cities", null, null);
        dbSel.delete("languages", null, null);
        dbSel.delete("aeroporty", null, null);


        dbSel.close();
        return true;
    }

    public static void addStrany(Context context, CountryModel model) {
        execSQL(context," insert into countries("+
                        "id, "+
                        "name, "+
                        "code "+
                        " )"+
                        "values(?, ?, ?)",
                new Object[]{
                        model.getId(),
                        model.getName(),
                        model.getCode()
                });
    }

    public static void addGoroda(Context context, CitiesModel model) {
        execSQL(context," insert into cities("+
                        "id, "+
                        "id_country, "+
                        "name "+
                        " )"+
                        "values(?, ?, ?)",
                new Object[]{
                        model.getId(),
                        model.getCountry_id(),
                        model.getName()
                });
    }

    public static void addPhone(Context context, String number) {
        execSQL(context," insert into phones("+
                        "number "+
                        " )"+
                        "values(?)",
                new Object[]{
                        number
                });
    }


    public static void addLanguages(Context context, LangModel model) {
        execSQL(context," insert into languages("+
                        "id, "+
                        "name, "+
                        "alias"+
                        " )"+
                        "values(?, ?, ?)",
                new Object[]{
                        model.getId(),
                        model.getName(),
                        model.getAlias()
                });
    }

    public static void addAeroport(Context context, AirportModel model) {
        execSQL(context," insert into aeroporty("+
                        "id, "+
                        "name, "+
                        "city_id"+
                        " )"+
                        "values(?, ?, ?)",
                new Object[]{
                        model.getId(),
                        model.getName(),
                        model.getCity_id()
                });
    }

    public static void addObjavlenie(Context context, ObjavlenijaModel model) {
        execSQL(context," insert into objavlenija("+
                        "id, "+
                        "zagolovok, "+
                        "opisanie, "+
                        "created_at, "+
                        "updated_at, "+
                        "received_date_time, "+
                        "id_type, "+
                        "id_flight "+
                        " )"+
                        "values(?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{
                        model.getId(),
                        model.getZagolovok(),
                        model.getOpisanie(),
                        model.getCreated_at(),
                        model.getUpdated_at(),
                        model.getReceived_date_time(),
                        model.getId_type(),
                        model.getId_flight()


                });
    }

    public static void addUserDocument(Context context,  String base64) {

        execSQL(context," update profile_doc set "+
                " base64 = ? "+
                "where id= 1 ",  new Object[]{base64

        });


    }

    public static void addFDocument(Context context, int id_flight, String base64) {
        execSQL(context," insert into documents("+
                        "base64, "+
                        "id_my_flight "+
                        " )"+
                        "values(?, ?)",
                new Object[]{
                        base64,
                        id_flight
                });

    }


    public static ArrayList<MyTicketModel> getMyTickets(Context context, String date, String where) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        ArrayList<MyTicketModel> myTicketModelArrayList = new ArrayList<>();

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;

        long index;
        String where_s = " my_flights.id > 0 ";

        if(date != null) {
            where_s = " status_id != 'otmenjon' AND ( my_flights.data_vyleta  > '" + date + "' or my_flights.data_vyleta  = '" + date + "' ) ";
        }

        if(where != null) where_s = where;

        Log.d("Services--------", "filter "+where_s);


        sqlQuery = " select my_flights.*, ct1.name AS gorod_vyleta_name,  ct2.name AS gorod_prileta_name,  ar1.name AS aeroport_vyleta_name,  ar2.name AS aeroport_prileta_name "
                + " from my_flights  "
                + " LEFT JOIN cities AS ct1 ON (my_flights.gorod_vyleta_id = ct1.id) "
                + " LEFT JOIN cities AS ct2 ON (my_flights.gorod_prileta_id = ct2.id) "
                + " LEFT JOIN aeroporty AS ar1 ON (my_flights.aeroport_vyleta_id = ar1.id) "
                + " LEFT JOIN aeroporty AS ar2 ON (my_flights.aeroport_prileta_id = ar2.id) "
                + " where " + where_s
                + " group by my_flights.id "
                + " order by  my_flights.id DESC ";


        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 1;
        while (curSelect.moveToNext()) {
            MyTicketModel myTicketModel = new MyTicketModel();

            String status_id = curSelect.getString(curSelect.getColumnIndex("status_id"));
            int gorod_vyleta_id = curSelect.getInt(curSelect.getColumnIndex("gorod_vyleta_id"));
            int gorod_prileta_id = curSelect.getInt(curSelect.getColumnIndex("gorod_prileta_id"));
            String data_vyleta = curSelect.getString(curSelect.getColumnIndex("data_vyleta"));
            int user_id = curSelect.getInt(curSelect.getColumnIndex("user_id"));
            String updated_at = curSelect.getString(curSelect.getColumnIndex("updated_at"));
            String created_at = curSelect.getString(curSelect.getColumnIndex("created_at"));
            int id = curSelect.getInt(curSelect.getColumnIndex("id"));
            String surname = curSelect.getString(curSelect.getColumnIndex("surname"));
            String first_name = curSelect.getString(curSelect.getColumnIndex("first_name"));
            String patronymic = curSelect.getString(curSelect.getColumnIndex("patronymic"));
            String birthday = curSelect.getString(curSelect.getColumnIndex("birthday"));
            int gender_id = curSelect.getInt(curSelect.getColumnIndex("gender_id"));
            String passport_series = curSelect.getString(curSelect.getColumnIndex("passport_series"));
            String passport_number = curSelect.getString(curSelect.getColumnIndex("passport_number"));
            String passport_expires = curSelect.getString(curSelect.getColumnIndex("passport_expires"));
            int country_id = curSelect.getInt(curSelect.getColumnIndex("country_id"));
            String gorod_prileta_name = curSelect.getString(curSelect.getColumnIndex("gorod_prileta_name"));
            String gorod_vyleta_name = curSelect.getString(curSelect.getColumnIndex("gorod_vyleta_name"));
            String aeroport_vyleta_name = curSelect.getString(curSelect.getColumnIndex("aeroport_vyleta_name"));
            String aeroport_prileta_name = curSelect.getString(curSelect.getColumnIndex("aeroport_prileta_name"));
            float stoimost = curSelect.getFloat(curSelect.getColumnIndex("stoimost"));
            String nomer_bileta = curSelect.getString(curSelect.getColumnIndex("nomer_bileta"));
            String nomer_rejsa = curSelect.getString(curSelect.getColumnIndex("nomer_rejsa"));
            String vremja_vyleta = curSelect.getString(curSelect.getColumnIndex("vremja_vyleta"));
            String data_prileta = curSelect.getString(curSelect.getColumnIndex("data_prileta"));
            String vremja_prileta = curSelect.getString(curSelect.getColumnIndex("vremja_prileta"));
            String valjuta = curSelect.getString(curSelect.getColumnIndex("valjuta"));

            myTicketModel.setStatus_id(status_id );
            myTicketModel.setGorod_vyleta_id(gorod_vyleta_id);
            myTicketModel.setGorod_prileta_id(gorod_prileta_id);
            myTicketModel.setData_vyleta(data_vyleta);
            myTicketModel.setUser_id(user_id);
            myTicketModel.setUpdated_at(updated_at);
            myTicketModel.setCreated_at(created_at);
            myTicketModel.setId(id);
            myTicketModel.setSurname(surname);
            myTicketModel.setFirst_name(first_name);
            myTicketModel.setPatronymic(patronymic);
            myTicketModel.setBirthday(birthday);
            myTicketModel.setGender_id(gender_id);
            myTicketModel.setPassport_series(passport_series);
            myTicketModel.setPassport_number(passport_number);
            myTicketModel.setPassport_expires(passport_expires);
            myTicketModel.setCountry_id(country_id);
            myTicketModel.setGorod_vyleta_name(gorod_vyleta_name);
            myTicketModel.setGorod_prileta_name(gorod_prileta_name);
            myTicketModel.setAeroport_vyleta_name(aeroport_vyleta_name);
            myTicketModel.setAeroport_prileta_name(aeroport_prileta_name);
            myTicketModel.setStoimost(stoimost);
            myTicketModel.setNomer_bileta(nomer_bileta);
            myTicketModel.setNomer_rejsa(nomer_rejsa);
            myTicketModel.setVremja_vyleta(vremja_vyleta);
            myTicketModel.setData_prileta(data_prileta);
            myTicketModel.setVremja_prileta(vremja_prileta);
            myTicketModel.setValjuta(valjuta);

            myTicketModelArrayList.add(myTicketModel);
        }

        dbSel.close();
        return myTicketModelArrayList;
    }


    public static MyTicketModel getMyTicket(Context context, int id_) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        MyTicketModel myTicketModel = new MyTicketModel();

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;

        long index;

        sqlQuery = " select my_flights.*,  ct1.name AS gorod_vyleta_name,  ct2.name AS gorod_prileta_name,  ar1.name AS aeroport_vyleta_name,  ar2.name AS aeroport_prileta_name "
                + " from my_flights  "
                + " LEFT JOIN cities AS ct1 ON (my_flights.gorod_vyleta_id = ct1.id) "
                + " LEFT JOIN cities AS ct2 ON (my_flights.gorod_prileta_id = ct2.id) "
                + " LEFT JOIN aeroporty AS ar1 ON (my_flights.aeroport_vyleta_id = ar1.id) "
                + " LEFT JOIN aeroporty AS ar2 ON (my_flights.aeroport_prileta_id = ar2.id) "
                + " where my_flights.id = "+id_
                + " order by my_flights.data_vyleta DESC ";

        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 1;
        while (curSelect.moveToNext()) {

            String status_id = curSelect.getString(curSelect.getColumnIndex("status_id"));
            int gorod_vyleta_id = curSelect.getInt(curSelect.getColumnIndex("gorod_vyleta_id"));
            int gorod_prileta_id = curSelect.getInt(curSelect.getColumnIndex("gorod_prileta_id"));
            String data_vyleta = curSelect.getString(curSelect.getColumnIndex("data_vyleta"));
            int user_id = curSelect.getInt(curSelect.getColumnIndex("user_id"));
            String updated_at = curSelect.getString(curSelect.getColumnIndex("updated_at"));
            String created_at = curSelect.getString(curSelect.getColumnIndex("created_at"));
            int id = curSelect.getInt(curSelect.getColumnIndex("id"));
            String surname = curSelect.getString(curSelect.getColumnIndex("surname"));
            String first_name = curSelect.getString(curSelect.getColumnIndex("first_name"));
            String patronymic = curSelect.getString(curSelect.getColumnIndex("patronymic"));
            String birthday = curSelect.getString(curSelect.getColumnIndex("birthday"));
            int gender_id = curSelect.getInt(curSelect.getColumnIndex("gender_id"));
            String passport_series = curSelect.getString(curSelect.getColumnIndex("passport_series"));
            String passport_number = curSelect.getString(curSelect.getColumnIndex("passport_number"));
            String passport_expires = curSelect.getString(curSelect.getColumnIndex("passport_expires"));
            int country_id = curSelect.getInt(curSelect.getColumnIndex("country_id"));
            String gorod_vyleta_name = curSelect.getString(curSelect.getColumnIndex("gorod_vyleta_name"));
            String gorod_prileta_name = curSelect.getString(curSelect.getColumnIndex("gorod_prileta_name"));
            String aeroport_vyleta_name = curSelect.getString(curSelect.getColumnIndex("aeroport_vyleta_name"));
            String aeroport_prileta_name = curSelect.getString(curSelect.getColumnIndex("aeroport_prileta_name"));
            String vremja_vyleta = curSelect.getString(curSelect.getColumnIndex("vremja_vyleta"));
            String data_prileta = curSelect.getString(curSelect.getColumnIndex("data_prileta"));
            String vremja_prileta = curSelect.getString(curSelect.getColumnIndex("vremja_prileta"));
            float stoimost = curSelect.getFloat(curSelect.getColumnIndex("stoimost"));
            String nomer_bileta = curSelect.getString(curSelect.getColumnIndex("nomer_bileta"));
            String nomer_rejsa = curSelect.getString(curSelect.getColumnIndex("nomer_rejsa"));
            String valjuta = curSelect.getString(curSelect.getColumnIndex("valjuta"));
            String bagazh = curSelect.getString(curSelect.getColumnIndex("bagazh"));

            myTicketModel.setStatus_id(status_id );
            myTicketModel.setGorod_vyleta_id(gorod_vyleta_id);
            myTicketModel.setGorod_prileta_id(gorod_prileta_id);
            myTicketModel.setData_vyleta(data_vyleta);
            myTicketModel.setUser_id(user_id);
            myTicketModel.setUpdated_at(updated_at);
            myTicketModel.setCreated_at(created_at);
            myTicketModel.setId(id);
            myTicketModel.setSurname(surname);
            myTicketModel.setFirst_name(first_name);
            myTicketModel.setPatronymic(patronymic);
            myTicketModel.setBirthday(birthday);
            myTicketModel.setGender_id(gender_id);
            myTicketModel.setPassport_series(passport_series);
            myTicketModel.setPassport_number(passport_number);
            myTicketModel.setPassport_expires(passport_expires);
            myTicketModel.setCountry_id(country_id);
            myTicketModel.setGorod_vyleta_name(gorod_vyleta_name);
            myTicketModel.setGorod_prileta_name(gorod_prileta_name);
            myTicketModel.setAeroport_vyleta_name(aeroport_vyleta_name);
            myTicketModel.setAeroport_prileta_name(aeroport_prileta_name);
            myTicketModel.setVremja_vyleta(vremja_vyleta);
            myTicketModel.setData_prileta(data_prileta);
            myTicketModel.setVremja_prileta(vremja_prileta);
            myTicketModel.setStoimost(stoimost);
            myTicketModel.setNomer_bileta(nomer_bileta);
            myTicketModel.setNomer_rejsa(nomer_rejsa);
            myTicketModel.setValjuta(valjuta);
            myTicketModel.setBagazh(bagazh);

        }

        dbSel.close();
        return myTicketModel;
    }


    public static String UserDocument(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        String document = "";

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;

        long index;

        sqlQuery = " select profile_doc.* "
                + " from profile_doc  "
                + " where profile_doc.id = 1 "
                + " order by profile_doc.id DESC ";

        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 1;
        while (curSelect.moveToNext()) {

            document = curSelect.getString(curSelect.getColumnIndex("base64"));
        }

        dbSel.close();
        return document;
    }


    public static String MyDocument(Context context, int id_) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        String document = "";

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;

        long index;

        sqlQuery = " select documents.* "
                + " from documents  "
                + " where documents.id_my_flight = "+id_
                + " order by documents.id DESC ";

        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 1;
        while (curSelect.moveToNext()) {

            document = curSelect.getString(curSelect.getColumnIndex("base64"));
        }

        dbSel.close();
        return document;
    }


    public static ArrayList<HorizontalListModel> getHorizontalList(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        ArrayList<HorizontalListModel> horizontalListModels = new ArrayList<>();

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;

        long index;

        sqlQuery = " select flights.*,  cities.name AS gorod_vyleta_name "
                + " from flights  "
                + " LEFT JOIN cities  ON (flights.gorod_vyleta_id = cities.id) "
                + " where flights.id > 0 "
                + " group by flights.gorod_vyleta_id"
                + " order by flights.gorod_vyleta_id DESC ";


        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 0;

        while (curSelect.moveToNext()) {
            HorizontalListModel horizontalListModel = new HorizontalListModel();
            if(i == 0) horizontalListModel.setState(true);
            int gorod_vyleta_id = curSelect.getInt(curSelect.getColumnIndex("gorod_vyleta_id"));
            String gorod_vyleta_name = curSelect.getString(curSelect.getColumnIndex("gorod_vyleta_name"));
            horizontalListModel.setId_country(gorod_vyleta_id );
            horizontalListModel.setCountryName(gorod_vyleta_name);
            horizontalListModels.add(horizontalListModel);
            i++;
        }

        dbSel.close();
        return horizontalListModels;
    }


    public static ArrayList<TicketModel> getNewTickets(Context context, int id_city) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        ArrayList<TicketModel> myTicketModelArrayList = new ArrayList<>();

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;

        long index;

        sqlQuery = " select flights.*,  ct1.name AS gorod_vyleta_name,  ct2.name AS gorod_prileta_name,  ar1.name AS aeroport_vyleta_name,  ar2.name AS aeroport_prileta_name "
                + " from flights  "
                + " LEFT JOIN cities AS ct1 ON (flights.gorod_vyleta_id = ct1.id) "
                + " LEFT JOIN cities AS ct2 ON (flights.gorod_prileta_id = ct2.id) "
                + " LEFT JOIN aeroporty AS ar1 ON (flights.aeroport_vyleta_id = ar1.id) "
                + " LEFT JOIN aeroporty AS ar2 ON (flights.aeroport_prileta_id = ar2.id) "
                + " where flights.gorod_vyleta_id = "+id_city
                + " order by flights.data_vyleta DESC ";


        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 1;
        while (curSelect.moveToNext()) {
            TicketModel myTicketModel = new TicketModel();

            int gorod_vyleta_id = curSelect.getInt(curSelect.getColumnIndex("gorod_vyleta_id"));
            int gorod_prileta_id = curSelect.getInt(curSelect.getColumnIndex("gorod_prileta_id"));
            String data_vyleta = curSelect.getString(curSelect.getColumnIndex("data_vyleta"));
            int id = curSelect.getInt(curSelect.getColumnIndex("id"));
            String gorod_prileta_name = curSelect.getString(curSelect.getColumnIndex("gorod_prileta_name"));
            String gorod_vyleta_name = curSelect.getString(curSelect.getColumnIndex("gorod_vyleta_name"));
            String aeroport_vyleta_name = curSelect.getString(curSelect.getColumnIndex("aeroport_vyleta_name"));
            String aeroport_prileta_name = curSelect.getString(curSelect.getColumnIndex("aeroport_prileta_name"));
            float stoimost = curSelect.getFloat(curSelect.getColumnIndex("stoimost"));
            String nomer_bileta = curSelect.getString(curSelect.getColumnIndex("nomer_bileta"));
            String nomer_rejsa = curSelect.getString(curSelect.getColumnIndex("nomer_rejsa"));
            String vremja_vyleta = curSelect.getString(curSelect.getColumnIndex("vremja_vyleta"));
            String data_prileta = curSelect.getString(curSelect.getColumnIndex("data_prileta"));
            String vremja_prileta = curSelect.getString(curSelect.getColumnIndex("vremja_prileta"));
            String valjuta = curSelect.getString(curSelect.getColumnIndex("valjuta"));


            myTicketModel.setGorod_vyleta_id(gorod_vyleta_id);
            myTicketModel.setGorod_prileta_id(gorod_prileta_id);
            myTicketModel.setData_vyleta(data_vyleta);
            myTicketModel.setId(id);
            myTicketModel.setGorod_vyleta_name(gorod_vyleta_name);
            myTicketModel.setGorod_prileta_name(gorod_prileta_name);
            myTicketModel.setData_vyleta(data_vyleta);

            myTicketModel.setAeroport_vyleta_name(aeroport_vyleta_name);
            myTicketModel.setAeroport_prileta_name(aeroport_prileta_name);
            myTicketModel.setStoimost(stoimost);
            myTicketModel.setNomer_bileta(nomer_bileta);
            myTicketModel.setNomer_rejsa(nomer_rejsa);
            myTicketModel.setVremja_vyleta(vremja_vyleta);
            myTicketModel.setData_prileta(data_prileta);
            myTicketModel.setVremja_prileta(vremja_prileta);
            myTicketModel.setValjuta(valjuta);

            myTicketModelArrayList.add(myTicketModel);
        }

        dbSel.close();
        return myTicketModelArrayList;
    }


    public static UserData getUserData(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        UserData userModel = new UserData();
        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;

        long index;

        sqlQuery = " select users.* "
                + " from users  "
                + " where users.id = 1 "
                + " order by users.id DESC ";

        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 1;
        while (curSelect.moveToNext()) {
            String surname = curSelect.getString(curSelect.getColumnIndex("surname"));
            String first_name = curSelect.getString(curSelect.getColumnIndex("first_name"));
            String patronymic = curSelect.getString(curSelect.getColumnIndex("patronymic"));
            int gender_id = curSelect.getInt(curSelect.getColumnIndex("gender_id"));
            String birthday = curSelect.getString(curSelect.getColumnIndex("birthday"));
            String passport_series = curSelect.getString(curSelect.getColumnIndex("passport_series"));
            String passport_number = curSelect.getString(curSelect.getColumnIndex("passport_number"));
            String passport_expires = curSelect.getString(curSelect.getColumnIndex("passport_expires"));
            int spoken_language_id = curSelect.getInt(curSelect.getColumnIndex("spoken_language_id"));
            int country_id = curSelect.getInt(curSelect.getColumnIndex("country_id"));
            String email = curSelect.getString(curSelect.getColumnIndex("email"));
            String token = curSelect.getString(curSelect.getColumnIndex("token"));
            String login = curSelect.getString(curSelect.getColumnIndex("login"));
            String photo_base64 = curSelect.getString(curSelect.getColumnIndex("photo_base64"));

            Log.d("Connection", "NAME FROM DB = "+first_name);


            userModel.setSurname(surname);
            userModel.setFirst_name(first_name);
            userModel.setPatronymic(patronymic);
            userModel.setGender_id(gender_id);
            userModel.setBirthday(birthday);
            userModel.setPassport_series(passport_series);
            userModel.setPassport_number(passport_number);
            userModel.setPassport_expires(passport_expires);
            userModel.setSpoken_language_id(spoken_language_id);
            userModel.setCountry_id(country_id);
            userModel.setEmail(email);
            userModel.setToken(token);
            userModel.setLogin(login);
            userModel.setPhoto_doc(photo_base64);
        }

        dbSel.close();
        return userModel;
    }

    public static ArrayList<ObjavlenijaModel> getNotifList(Context context) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        ArrayList<ObjavlenijaModel> objavlenijaModels = new ArrayList<>();

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;

        long index;

        sqlQuery = " select objavlenija.* "
                + " from objavlenija  "
                + " where objavlenija.id >= 0 "
                + " order by objavlenija.created_at DESC ";


        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 0;

        while (curSelect.moveToNext()) {
            ObjavlenijaModel objavlenijaModel = new ObjavlenijaModel();

            int id = curSelect.getInt(curSelect.getColumnIndex("id"));
            String zagolovok  = curSelect.getString(curSelect.getColumnIndex("zagolovok"));
            String opisanie = curSelect.getString(curSelect.getColumnIndex("opisanie"));
            String created_at = curSelect.getString(curSelect.getColumnIndex("created_at"));
            String updated_at  = curSelect.getString(curSelect.getColumnIndex("updated_at"));
            String received_date_time  = curSelect.getString(curSelect.getColumnIndex("received_date_time"));


            objavlenijaModel.setId(id );
            objavlenijaModel.setZagolovok(zagolovok);
            objavlenijaModel.setOpisanie(opisanie);
            objavlenijaModel.setCreated_at(created_at);
            objavlenijaModel.setUpdated_at(updated_at);
            objavlenijaModel.setReceived_date_time(received_date_time);

            objavlenijaModels.add(objavlenijaModel);
            i++;
        }

        dbSel.close();
        return objavlenijaModels;
    }


    public static boolean getFlightNotifList(Context context, int id_flight, int id_type) {

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);

        ArrayList<ObjavlenijaModel> objavlenijaModels = new ArrayList<>();

        SQLiteDatabase dbSel = databaseHelper.open();

        String sqlQuery = "";
        String[] selectionArgs;
        Cursor curSelect = null;

        long index;

        sqlQuery = " select objavlenija.* "
                + " from objavlenija  "
                + " where objavlenija.id_flight = "+id_flight+" AND id_type = "+id_type
                + " order by objavlenija.id_type DESC ";


        curSelect = dbSel.rawQuery(sqlQuery, null);

        int i = 0;

        while (curSelect.moveToNext()) {
            return true;
        }

        dbSel.close();
        return false;
    }



}
