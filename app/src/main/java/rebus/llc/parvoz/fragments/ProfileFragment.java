package rebus.llc.parvoz.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rebus.llc.parvoz.LoginActivity;
import rebus.llc.parvoz.R;
import rebus.llc.parvoz.db.DBSample;
import rebus.llc.parvoz.models.SignInData;
import rebus.llc.parvoz.models.UserData;
import rebus.llc.parvoz.others.Encrypt;
import rebus.llc.parvoz.others.MyAsyncTask;
import rebus.llc.parvoz.others.Settings;

import static rebus.llc.parvoz.others.Utilities.convertFromatDMYtoYMD;
import static rebus.llc.parvoz.others.Utilities.convertFromatYMDtoDMY;
import static rebus.llc.parvoz.others.Utilities.getServerUrls;

public class ProfileFragment extends android.support.v4.app.Fragment  implements MyAsyncTask.ResponseCame{

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
        CheckBox checkBoxChangePassword;
        TextInputLayout passwordView;
        TextInputLayout passwordView2;

        CheckBox checkBoxSavePassword;


        private Encrypt md5Conver;



        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
            setRetainInstance(true);
            context = getContext();
            md5Conver = new Encrypt();

            settings = new Settings(context);
            addNewDocument  = (LinearLayout) rootView.findViewById(R.id.addNewDocument);
            base64Document = "";
            checkBoxSavePassword = (CheckBox) rootView.findViewById(R.id.checkBoxSavePassword);
            mProgressView = rootView.findViewById(R.id.login_progress);
            mLoginFormView = rootView.findViewById(R.id.login_form);
            newPhoneNumber = (TextInputLayout) rootView.findViewById(R.id.newPhoneNumber);
            lastName = (AutoCompleteTextView) rootView.findViewById(R.id.lastName);
            firstName = (AutoCompleteTextView) rootView.findViewById(R.id.firstName);
            dateBirth = (AutoCompleteTextView) rootView.findViewById(R.id.dateBirth);
            passNumber = (AutoCompleteTextView) rootView.findViewById(R.id.passNumber);
            datePassport = (AutoCompleteTextView) rootView.findViewById(R.id.datePassport);
            passSerija = (AutoCompleteTextView) rootView.findViewById(R.id.passSerija);
            emailV = (AutoCompleteTextView) rootView.findViewById(R.id.emailV);
            number = (AutoCompleteTextView) rootView.findViewById(R.id.number);
            spinnerPhone = (Spinner) rootView.findViewById(R.id.spinnerPhone);
            spinnerLanguage = (Spinner) rootView.findViewById(R.id.spinnerLanguage);
            linearLayoutUpdate = (LinearLayout) rootView.findViewById(R.id.linearLayoutUpdate);
            linearLayoutSave = (LinearLayout) rootView.findViewById(R.id.linearLayoutSave);
            imageView = (ImageView) rootView.findViewById(R.id.IdProf);
            linearLayoutCencel = (LinearLayout) rootView.findViewById(R.id.linearLayoutCencel);
            deleteAcount = (LinearLayout) rootView.findViewById(R.id.deleteAcount);
            password2 = (TextInputEditText) rootView.findViewById(R.id.password2);
            password = (TextInputEditText) rootView.findViewById(R.id.password);
            checkBoxChangePassword = (CheckBox) rootView.findViewById(R.id.checkBoxChangePassword);

            passwordView = (TextInputLayout) rootView.findViewById(R.id.passwordView);
            passwordView2 = (TextInputLayout) rootView.findViewById(R.id.passwordView2);

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

                    picker = new DatePickerDialog(getActivity(),
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

            checkBoxChangePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        passwordView.setVisibility(View.VISIBLE);
                        passwordView2.setVisibility(View.VISIBLE);
                    }else{
                        passwordView.setVisibility(View.GONE);
                        passwordView2.setVisibility(View.GONE);
                    }
                }
            });

            datePassport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Calendar cal = Calendar.getInstance();
                    int year  = cal_date_pass.get(Calendar.YEAR);
                    int month = cal_date_pass.get(Calendar.MONTH);
                    int day   = cal_date_pass.get(Calendar.DAY_OF_MONTH);

                    picker = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    cal_date_pass.set(Calendar.YEAR, year);
                                    cal_date_pass.set(Calendar.MONTH, monthOfYear);
                                    cal_date_pass.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                    SimpleDateFormat dateformat_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
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

            linearLayoutUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayoutSave.setVisibility(View.VISIBLE);
                    linearLayoutCencel.setVisibility(View.VISIBLE);
                    linearLayoutUpdate.setVisibility(View.GONE);
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

            addNewDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            return rootView;
        }




    public void deleteAcount(){

        if( myAsyncTask != null) return;

        String url = getServerUrls()+"user/delete";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));
        params.add(new BasicNameValuePair("user_id", new Settings(context).getIdUser()));
        params.add(new BasicNameValuePair("token", new Settings(context).getToken()));

        myAsyncTask = new MyAsyncTask(context, params, url, "post   ");
        myAsyncTask.setResponseListener(this);
        myAsyncTask.execute();
    }


    public void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        checkBoxChangePassword.setEnabled(b);
    }

    @Override
    public void responseCame(boolean res, String message, JSONObject jObj) {

        showProgress(false);

        if(res){
                try {

                    if(myAsyncTask.comandUrl.equals(getServerUrls()+"user/delete")) {
                        new Settings(getContext()).saveLogin("");
                        new Settings(getContext()).savePassword("");
                        new Settings(getContext()).savePassword_2("");

                        Intent iFinanceActivity = new Intent(getActivity(), LoginActivity.class);
                        startActivity(iFinanceActivity);
                        DBSample.cleanMyFlights(getContext());
                        DBSample.cleanFlights(getContext());
                        DBSample.cleanTicketDocs(getContext());
                        DBSample.cleanObjavlenija(getContext());
                        getActivity().finish();
                    }else{
                        objectMapper = new ObjectMapper();

                        SignInData signInData = objectMapper.readValue(jObj.toString(), SignInData.class);
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
        String password2_s     = password2.getText().toString();
        String password_s       = password.getText().toString();

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


        if(checkBoxChangePassword.isChecked()) {
            if (TextUtils.isEmpty(password_s) && !isPasswordValid(password_s)) {
                this.password.setError("Пароль должен быть больше 3 символов");
                focusView = this.password;
                cancel = true;
            } else {
                params.add(new BasicNameValuePair("password", md5Conver.md5(password_s)));
            }

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password2_s) && !isPasswordValid(password2_s) && !password_s.equals(password2_s)) {
                this.password2.setError(getString(R.string.error_field_password2));
                focusView = this.password2;
                cancel = true;

                if (!password2_s.equals(password_s)) {
                    this.password2.setError(getString(R.string.error_field_password2));
                    focusView = this.password2;
                }

            } else {
                params.add(new BasicNameValuePair("password_confirmation", md5Conver.md5(password_s)));
            }
        }

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

        if(checkBoxSavePassword.isChecked())
            settings.savePassword_2(settings.getPassword());
        else
            settings.savePassword_2("");


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
                List<String> doc = new ArrayList<>();
                doc.add(base64Document);
                params.add(new BasicNameValuePair("documents", doc.toString()));
            }
        }catch (NullPointerException msg){
        }

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







}
