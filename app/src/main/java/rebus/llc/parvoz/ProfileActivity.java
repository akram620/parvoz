package rebus.llc.parvoz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rebus.llc.parvoz.adapters.CameraPickAdapter;
import rebus.llc.parvoz.db.DBSample;
import rebus.llc.parvoz.models.SignInData;
import rebus.llc.parvoz.models.UserData;
import rebus.llc.parvoz.others.Encrypt;
import rebus.llc.parvoz.others.MyAsyncTask;
import rebus.llc.parvoz.others.Settings;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static rebus.llc.parvoz.others.Utilities.convertFromatDMYtoYMD;
import static rebus.llc.parvoz.others.Utilities.convertFromatYMDtoDMY;
import static rebus.llc.parvoz.others.Utilities.convertToBase64;
import static rebus.llc.parvoz.others.Utilities.convertToBitmap;
import static rebus.llc.parvoz.others.Utilities.getServerUrls;
import static rebus.llc.parvoz.others.Utilities.justConvertToBitmap;

public class ProfileActivity extends AppCompatActivity  implements MyAsyncTask.ResponseCame{


    Settings settings;
    Context context;
    AutoCompleteTextView lastName;
    AutoCompleteTextView firstName;
    AutoCompleteTextView dateBirth;
    AutoCompleteTextView passNumber;
    AutoCompleteTextView datePassport;
    AutoCompleteTextView passSerija;
    AutoCompleteTextView number;
    AutoCompleteTextView emailV;
    Spinner spinnerPhone;
    LinearLayout linearLayoutUpdate;
    LinearLayout linearLayoutSave;
    LinearLayout linearLayoutCencel;
    LinearLayout linearLayoutClose;
    String[] list_id_numbers;
    String[] list_numbers;
    ArrayAdapter<String> adapter_numbers;
    MyAsyncTask myAsyncTask;
    ObjectMapper objectMapper;
    private View mProgressView;
    private View mLoginFormView;
    Calendar cal_date;
    Calendar cal_date_pass;

    DatePickerDialog picker;
    TextInputLayout newPhoneNumber;
    Spinner spinnerLanguage;
    String[] list_id_lang;
    String[] list_name_lang;
    String[] list_alias_lang;
    ArrayAdapter<String> adapter_lang;
    LinearLayout addNewDocument;
    public String base64Document;
    public ImageView imageView;

    TextInputEditText password2;
    TextInputEditText password;
    LinearLayout deleteAcount;
    //CheckBox checkBoxChangePassword;
    TextInputLayout passwordView;
    TextInputLayout passwordView2;

    CheckBox checkBoxSavePassword;


    private Encrypt md5Conver;

    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    private static String FILE_NAME = "photo.jpg";
    private  File photoFile;

    private int GALLERY = 1, CAMERA = 2;
    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW","android.permission.CAMERA"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = this;
        md5Conver = new Encrypt();
        settings = new Settings(context);

        addNewDocument  = (LinearLayout) findViewById(R.id.addNewDocument);
        base64Document = "";
        checkBoxSavePassword = (CheckBox) findViewById(R.id.checkBoxSavePassword);
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
        newPhoneNumber = (TextInputLayout) findViewById(R.id.newPhoneNumber);
        lastName = (AutoCompleteTextView) findViewById(R.id.lastName);
        firstName = (AutoCompleteTextView) findViewById(R.id.firstName);
        dateBirth = (AutoCompleteTextView) findViewById(R.id.dateBirth);
        passNumber = (AutoCompleteTextView) findViewById(R.id.passNumber);
        datePassport = (AutoCompleteTextView) findViewById(R.id.datePassport);
        passSerija = (AutoCompleteTextView) findViewById(R.id.passSerija);
        emailV = (AutoCompleteTextView) findViewById(R.id.emailV);
        number = (AutoCompleteTextView) findViewById(R.id.number);
        spinnerPhone = (Spinner) findViewById(R.id.spinnerPhone);
        spinnerLanguage = (Spinner) findViewById(R.id.spinnerLanguage);
        linearLayoutUpdate = (LinearLayout) findViewById(R.id.linearLayoutUpdate);
        linearLayoutClose = (LinearLayout) findViewById(R.id.linearLayoutClose);
        linearLayoutSave = (LinearLayout) findViewById(R.id.linearLayoutSave);
        imageView = (ImageView) findViewById(R.id.IdProf);
        linearLayoutCencel = (LinearLayout) findViewById(R.id.linearLayoutCencel);
        deleteAcount = (LinearLayout) findViewById(R.id.deleteAcount);
        password2 = (TextInputEditText) findViewById(R.id.password2);
        password = (TextInputEditText) findViewById(R.id.password);
        //checkBoxChangePassword = (CheckBox) findViewById(R.id.checkBoxChangePassword);

        passwordView = (TextInputLayout) findViewById(R.id.passwordView);
        passwordView2 = (TextInputLayout) findViewById(R.id.passwordView2);

        dateBirth.setInputType(0);
        dateBirth.setFocusable(false);
        datePassport.setInputType(0);
        datePassport.setFocusable(false);
        getUserProfile();
        setEnableFields(false);
        cal_date = Calendar.getInstance();
        cal_date_pass = Calendar.getInstance();

        dateBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Calendar cal = Calendar.getInstance();
                int year  = cal_date.get(Calendar.YEAR);
                int month = cal_date.get(Calendar.MONTH);
                int day   = cal_date.get(Calendar.DAY_OF_MONTH);

                picker = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cal_date.set(Calendar.YEAR, year);
                                cal_date.set(Calendar.MONTH, monthOfYear);
                                cal_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                SimpleDateFormat dateformat_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat dateformat_ddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");
                                String date_to_string_ddMMyyyy = dateformat_ddMMyyyy.format(cal_date.getTime());
                                dateBirth.setText(date_to_string_ddMMyyyy);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        ArrayList<String[]> arrayFrom = DBSample.getLanguagesForSpinner(context);
        list_id_lang =  arrayFrom.get(0);//getResources().getStringArray(R.array.work_types);
        list_name_lang  =  arrayFrom.get(1);
        list_alias_lang =  arrayFrom.get(2);

        adapter_lang = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list_name_lang );
        adapter_lang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter_lang);

        deleteAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
//                    String pass = password.getText().toString();
//
//                    if(settings.equals(pass)){
//
//                    }else{
//                        Toast.makeText(context, "Для удаления акаунта введите пароль!", Toast.LENGTH_SHORT).show();
//                    }
            }
        });

//        checkBoxChangePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    passwordView.setVisibility(View.VISIBLE);
//                    passwordView2.setVisibility(View.VISIBLE);
//                }else{
//                    passwordView.setVisibility(View.GONE);
//                    passwordView2.setVisibility(View.GONE);
//                }
//            }
//        });

        datePassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Calendar cal = Calendar.getInstance();
                int year  = cal_date_pass.get(Calendar.YEAR);
                int month = cal_date_pass.get(Calendar.MONTH);
                int day   = cal_date_pass.get(Calendar.DAY_OF_MONTH);

                picker = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cal_date_pass.set(Calendar.YEAR, year);
                                cal_date_pass.set(Calendar.MONTH, monthOfYear);
                                cal_date_pass.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                //SimpleDateFormat dateformat_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat dateformat_ddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");
                                String date_to_string_ddMMyyyy = dateformat_ddMMyyyy.format(cal_date_pass.getTime());
                                datePassport.setText(date_to_string_ddMMyyyy);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        linearLayoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptSave();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });

        linearLayoutClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearLayoutUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSave.setVisibility(View.VISIBLE);
                linearLayoutCencel.setVisibility(View.VISIBLE);
                linearLayoutUpdate.setVisibility(View.GONE);
                linearLayoutClose.setVisibility(View.GONE);
                addNewDocument.setVisibility(View.VISIBLE);
                setEnableFields(true);
                deleteAcount.setVisibility(View.VISIBLE);
            }
        });

        linearLayoutCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSave.setVisibility(View.GONE);
                linearLayoutCencel.setVisibility(View.GONE);
                linearLayoutUpdate.setVisibility(View.VISIBLE);
                linearLayoutClose.setVisibility(View.VISIBLE);
                addNewDocument.setVisibility(View.GONE);
                deleteAcount.setVisibility(View.GONE);

                setUserData();
                setEnableFields(false);

            }
        });

        spinnerPhone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == list_id_numbers.length-1){
                    newPhoneNumber.setVisibility(View.VISIBLE);
                }else{
                    newPhoneNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // requestMultiplePermissions(); // check permission
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }


        addNewDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                showPictureDialog();
            }
        });
    }


    public void deleteAcount(){

        if( myAsyncTask != null) return;

        String url = getServerUrls()+"user/delete";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));
        params.add(new BasicNameValuePair("user_id", new Settings(context).getIdUser()));
        params.add(new BasicNameValuePair("token", new Settings(context).getToken()));

        myAsyncTask = new MyAsyncTask(context, params, url, "post");
        myAsyncTask.setResponseListener(this);
        myAsyncTask.execute();
    }


    public void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Подтверждение");
        builder.setMessage("Вы действительно хотите удалить акаунт?");
        builder.setPositiveButton("Да",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAcount();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void getUserProfile(){

        if( myAsyncTask != null) return;

        String url = getServerUrls()+"user";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));
        params.add(new BasicNameValuePair("user_id", new Settings(context).getIdUser()));
        params.add(new BasicNameValuePair("token", new Settings(context).getToken()));

        myAsyncTask = new MyAsyncTask(context, params, url, "get");
        myAsyncTask.setResponseListener(this);
        myAsyncTask.execute();
    }

    public void setUserData() {


        adapter_lang = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list_name_lang );
        adapter_lang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter_lang);


        UserData userModel = DBSample.getUserData(context);
        Log.d("Connection", "SET DATA "+userModel.getFirst_name());
        lastName.setText(userModel.getSurname());
        firstName.setText(userModel.getFirst_name());
        if(userModel.getBirthday() != null)dateBirth.setText(convertFromatYMDtoDMY(userModel.getBirthday()));
        passSerija.setText(userModel.getPassport_series());
        passNumber.setText(userModel.getPassport_number());
        if(userModel.getPassport_expires() != null)datePassport.setText(convertFromatYMDtoDMY(userModel.getPassport_expires()));
        emailV.setText(userModel.getEmail());

        ArrayList<String[]> arrayFrom = DBSample.getUserNumbers(context);

        list_id_numbers =  arrayFrom.get(0);//getResources().getStringArray(R.array.work_types);
        list_numbers =  arrayFrom.get(1);
        adapter_numbers = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list_numbers);
        adapter_numbers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhone.setAdapter(adapter_numbers);

        for(int i = 0; i < list_id_lang.length; i++){
            if(list_id_lang[i].equals(""+userModel.getSpoken_language_id())){
                spinnerLanguage.setSelection(i);
            }
        }

        base64Document = DBSample.UserDocument(context);


        if(base64Document != null  ){
            Log.d("Connection", "base64Document != null");
             Bitmap bitmap = justConvertToBitmap(base64Document.replace("data:image/jpeg;base64,", ""));
            Log.d("Connection", "base64Document != "+base64Document.toString());
                imageView.setImageBitmap(bitmap);

        }
    }

    private void setEnableFields(boolean b){
        lastName.setEnabled(b);
        firstName.setEnabled(b);
        dateBirth.setEnabled(b);
        passSerija.setEnabled(b);
        passNumber.setEnabled(b);
        number.setEnabled(b);
        //spinnerPhone.setEnabled(b);
        spinnerLanguage.setEnabled(b);
        datePassport.setEnabled(b);
        password.setEnabled(b);
        password2.setEnabled(b);
        emailV.setEnabled(b);
        //checkBoxChangePassword.setEnabled(b);
    }

    @Override
    public void responseCame(boolean res, String message, JSONObject jObj) {

        showProgress(false);

        if(res){
            try {

                if(myAsyncTask.comandUrl.equals(getServerUrls()+"user/delete")) {
                    new Settings(context).saveLogin("");
                    new Settings(context).savePassword("");
                    new Settings(context).savePassword_2("");


                    DBSample.cleanMyFlights(context);
                    DBSample.cleanFlights(context);
                    DBSample.cleanTicketDocs(context);
                    DBSample.cleanObjavlenija(context);

                    Intent intent = new Intent();
                    intent.putExtra("activity", "Profile");
                    intent.putExtra("action", "deleted");

                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    objectMapper = new ObjectMapper();

                    SignInData signInData = objectMapper.readValue(jObj.toString(), SignInData.class);

                    if(base64Document != null) {
                        if (base64Document.length() > 0) {
                            DBSample.addUserDocument(context, base64Document);
                        }
                    }



                    DBSample.updateUserData(context, signInData.getKlient());

                    DBSample.cleanPhones(context);
                    for (int i = 0; i < signInData.getKlient().getPhones().size(); i++) {
                        DBSample.addPhone(context, signInData.getKlient().getPhones().get(i));
                    }


                    setUserData();

                    number.setText("");
                    linearLayoutSave.setVisibility(View.GONE);
                    linearLayoutCencel.setVisibility(View.GONE);
                    linearLayoutUpdate.setVisibility(View.VISIBLE);
                    linearLayoutClose.setVisibility(View.VISIBLE);
                    addNewDocument.setVisibility(View.GONE);
                }
                setEnableFields(false);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Connection", e.getMessage());
            }

        }else {
            Log.d("Connection", "GET PROFILE 1 ");

            setUserData();
        }

        myAsyncTask = null;

    }



    private void attemptSave() throws JsonProcessingException {
        if (myAsyncTask != null) {
            return;
        }
        // Reset errors.
        lastName.setError(null);
        firstName.setError(null);
        dateBirth.setError(null);
        passNumber.setError(null);
        datePassport.setError(null);
        passSerija.setError(null);
        number.setError(null);

        String lastName_s       = lastName.getText().toString();
        String firstName_s      = firstName.getText().toString();
        String dateBirth_s      = dateBirth.getText().toString();
        String passNumber_s     = passNumber.getText().toString();
        String datePassport_s   = datePassport.getText().toString();
        String passSerija_s     = passSerija.getText().toString();
        String number_s         = number.getText().toString();
        String email = emailV.getText().toString();
//        String password2_s     = password2.getText().toString();
//        String password_s       = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if (TextUtils.isEmpty(firstName_s)) {
            firstName.setError("Укажите имя");
            focusView = firstName;
            cancel = true;
        }else{
            params.add(new BasicNameValuePair("first_name", firstName_s));
        }

        if (TextUtils.isEmpty(lastName_s)) {
            lastName.setError("Укажите фамилию");
            focusView = lastName;
            cancel = true;
        }else{
            params.add(new BasicNameValuePair("surname", lastName_s));
        }

        if (TextUtils.isEmpty(dateBirth_s)) {
            dateBirth.setError("Укажите дату рождения");
            focusView = dateBirth;
            cancel = true;
        }else{
            params.add(new BasicNameValuePair("birthday", convertFromatDMYtoYMD(dateBirth_s)));
        }


//        if(checkBoxChangePassword.isChecked()) {
//            if (TextUtils.isEmpty(password_s) && !isPasswordValid(password_s)) {
//                this.password.setError("Пароль должен быть больше 3 символов");
//                focusView = this.password;
//                cancel = true;
//            } else {
//                params.add(new BasicNameValuePair("password", md5Conver.md5(password_s)));
//            }

            // Check for a valid password, if the user entered one.
//            if (TextUtils.isEmpty(password2_s) && !isPasswordValid(password2_s) && !password_s.equals(password2_s)) {
//                this.password2.setError(getString(R.string.error_field_password2));
//                focusView = this.password2;
//                cancel = true;
//
//                if (!password2_s.equals(password_s)) {
//                    this.password2.setError(getString(R.string.error_field_password2));
//                    focusView = this.password2;
//                }
//
//            } else {
//                params.add(new BasicNameValuePair("password_confirmation", md5Conver.md5(password_s)));
//            }
       // }

        if (TextUtils.isEmpty(passNumber_s)) {
            passNumber.setError("Укажите номер паспорта");
            focusView = passNumber;
            cancel = true;
        }else{
            params.add(new BasicNameValuePair("passport_number", passNumber_s));
        }

        if (TextUtils.isEmpty(passSerija_s)) {
            passSerija.setError("Укажите номер паспорта");
            focusView = passSerija;
            cancel = true;
        }else{
            params.add(new BasicNameValuePair("passport_series", passSerija_s));
        }

        if (TextUtils.isEmpty(email)) {

        }else{
            params.add(new BasicNameValuePair("email", email));
        }


        if (TextUtils.isEmpty(datePassport_s)) {
            datePassport.setError("Укажите срок действия паспорта");
            focusView = datePassport;
            cancel = true;
        }else{
            params.add(new BasicNameValuePair("passport_expires", convertFromatDMYtoYMD(datePassport_s)));
        }

        for(int i = 0; i < list_numbers.length-1; i++){
            params.add(new BasicNameValuePair("phones["+i+"]", list_numbers[i]));
        }

//        if(checkBoxSavePassword.isChecked())
//            settings.savePassword_2(settings.getPassword());
//        else
//            settings.savePassword_2("");


        if(spinnerPhone.getSelectedItemPosition() == list_numbers.length-1){
            if (TextUtils.isEmpty(number_s)) {
                passSerija.setError("Укажите новый номер телефона");
                focusView = passSerija;
                cancel = true;
            }else{
                params.add(new BasicNameValuePair("phones["+(list_numbers.length-1)+"]", number_s));
            }
        }

        params.add(new BasicNameValuePair("spoken_language_id", list_id_lang[spinnerLanguage.getSelectedItemPosition()]));


        try {
            if(base64Document.equals("")){
            }else{

                params.add(new BasicNameValuePair("base64_encoded_image_data", base64Document));
            }
        }catch (NullPointerException msg){
        }

        Log.d("Connection", params.toString());

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            showProgress(true);

            Log.d("Connection", new Settings(context).getToken());
            params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));
            params.add(new BasicNameValuePair("user_id", new Settings(context).getIdUser()));
            params.add(new BasicNameValuePair("token", new Settings(context).getToken()));

            String url = getServerUrls()+"user/update";
            myAsyncTask = new MyAsyncTask(context, params, url, "post");
            myAsyncTask.setResponseListener(this);
            myAsyncTask.execute();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    public void setPhoto(Intent data) {
        Log.d("Connection", "Rezult came right here 1" );
    }


    private void showPictureDialog() {
        final String [] items = new String[] {"Галерея", "Камера"};
        final Integer[] icons = new Integer[] { R.drawable.ic_photo_library, R.drawable.ic_camera};
        ListAdapter adapter = new CameraPickAdapter(this, items, icons);
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Выберите").setAdapter(adapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); photoFile = getPhotoFile(FILE_NAME);
        Uri fileProvider = FileProvider.getUriForFile(this, "rebus.llc.parvoz.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA);
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show();
        }
    }

    private File getPhotoFile(String filaName) {
        File getImage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(filaName, ".jpg", getImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    base64Document = convertToBase64(bitmap);
                    Picasso.with(context).load(contentURI).into(imageView);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap selectedImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            base64Document = convertToBase64(selectedImage);
            Picasso.with(context).load(photoFile.getAbsoluteFile()).into(imageView);
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == IMAGE_RESULT) {
//                if (data != null) {
//                    Log.d("data === ", data.toString());
//                    String filePath = getImageFilePath(data);
////                    Log.d("filePath == ", filePath.toString());
//                    if (filePath != null) {
//                        Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
//                        //imageView.setImageBitmap(selectedImage);
//                        Log.d("Connection", selectedImage.toString());
//                        base64Document = convertToBase64(selectedImage);
//                        long length = base64Document.length();
//                        Log.d("Connection", "LENGTH = " + length);
//                        //convertvToImage(base64Document);
//                        Bitmap bitmap = convertToBitmap(base64Document);
//                        imageView.setImageBitmap(bitmap);
//                    }
//                }
//            }
//        }
//    }

//    public Intent getPickImageChooserIntent() {
//
//        Uri outputFileUri = getCaptureImageOutputUri();
//
//        List<Intent> allIntents = new ArrayList<>();
//        PackageManager packageManager = getPackageManager();
//
//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//        for (ResolveInfo res : listCam) {
//            Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            }
//            allIntents.add(intent);
//        }
//
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
//        for (ResolveInfo res : listGallery) {
//            Intent intent = new Intent(galleryIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            allIntents.add(intent);
//        }
//
//        Intent mainIntent = allIntents.get(allIntents.size() - 1);
//        for (Intent intent : allIntents) {
//            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
//                mainIntent = intent;
//                break;
//            }
//        }
//        allIntents.remove(mainIntent);
//
//        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
//
//        return chooserIntent;
//    }


    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }


    private Uri getCaptureImageOutputUri() {
        Log.d("data22 === ", "getCaptureImageOutputUri");
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            Log.d("getImage === ", getImage.getPath());
            Log.d("getImagName === ", getImage.getName());
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profil.png"));
        }
        Log.d("outputFileUri === ", outputFileUri.getPath());
        return outputFileUri;
    }


    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        Log.d("data22 === ", "getPathFromURI");
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }


    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }
}
