package rebus.llc.parvoz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rebus.llc.parvoz.db.DBSample;
import rebus.llc.parvoz.models.SignInData;
import rebus.llc.parvoz.others.Encrypt;
import rebus.llc.parvoz.others.MyAsyncTask;
import rebus.llc.parvoz.others.Settings;

import static rebus.llc.parvoz.others.Utilities.getServerUrls;

public class SignUpActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>, MyAsyncTask.ResponseCame {

    TextInputEditText login;
    //TextInputEditText password;
    TextInputEditText password2;

    CheckBox checkBoxSavePassword;
    Button email_sign_in_button;
    TextView linkSignIn;
    TextView countryCode;
    ObjectMapper objectMapper;

    Settings setting;
    boolean local_check;
    Context context;
    private View mProgressView;
    private View mLoginFormView;
    MyAsyncTask myAsyncTask;
    Spinner spinnerLanguage;
    Spinner spinnerCountry;

    String[] list_id_lang;
    String[] list_name_lang;
    String[] list_alias_lang;
    ArrayAdapter<String> adapter_lang;

    String[] list_id_country;
    String[] list_name_country;
    String[] list_code_country;

    ArrayAdapter<String> adapter_country;
    private Encrypt md5Conver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context = this;
        setting = new Settings(this);
        objectMapper = new ObjectMapper();
        md5Conver = new Encrypt();

        login = (TextInputEditText) findViewById(R.id.login);
        //password = (TextInputEditText) findViewById(R.id.password);
        password2 = (TextInputEditText) findViewById(R.id.password2);
        checkBoxSavePassword = (CheckBox) findViewById(R.id.checkBoxSavePassword);
        email_sign_in_button = (Button) findViewById(R.id.email_sign_in_button);
        linkSignIn = (TextView) findViewById(R.id.linkSignIn);
        countryCode = (TextView) findViewById(R.id.countryCode);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        spinnerLanguage = (Spinner) findViewById(R.id.spinnerLanguage);
        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        ArrayList<String[]> arrayFrom = DBSample.getLanguagesForSpinner(context);
        list_id_lang =  arrayFrom.get(0);//getResources().getStringArray(R.array.work_types);
        list_name_lang  =  arrayFrom.get(1);
        list_alias_lang =  arrayFrom.get(2);

        adapter_lang = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list_name_lang );
        adapter_lang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter_lang);

        ArrayList<String[]> arrayCOuntry = DBSample.getCountryForSpinner(context);
        list_id_country =  arrayCOuntry.get(0);
        list_name_country  =  arrayCOuntry.get(1);
        list_code_country  =  arrayCOuntry.get(2);

        adapter_country = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list_name_country);
        adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter_country);

        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptLogin();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });


        linkSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoDialog = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(infoDialog);
                SignUpActivity.this.finish();
            }
        });

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryCode.setText(list_code_country[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void attemptLogin() throws JsonProcessingException {
        if (myAsyncTask != null) {
            return;
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        // Reset errors.
        login.setError(null);
        //password.setError(null);
        password2.setError(null);

        // Store values at the time of the login attempt.
        String number = login.getText().toString();
        //String password = this.password.getText().toString();
        String password2 = this.password2.getText().toString();


        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(number)) {
            login.setError(getString(R.string.error_field_required));
            focusView = login;
            cancel = true;
        }else {
            params.add(new BasicNameValuePair("login", countryCode.getText()+number));
            params.add(new BasicNameValuePair("phones[0]", countryCode.getText()+number));

        }

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            this.password.setError("Пароль должен быть больше 3 символов");
//            focusView = this.password;
//            cancel = true;
//        }else{
//            params.add(new BasicNameValuePair("password", md5Conver.md5(password)));
//        }

        //params.add(new BasicNameValuePair("spoken_language_id", "1"));
        params.add(new BasicNameValuePair("spoken_language_id", list_id_lang[spinnerLanguage.getSelectedItemPosition()]));

        //params.add(new BasicNameValuePair("country_id", list_id_country[spinnerCountry.getSelectedItemPosition()]));

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password2) && !isPasswordValid(password2) && !password.equals(password2)) {
//            this.password2.setError(getString(R.string.error_field_password2));
//            focusView = this.password2;
//            cancel = true;
//
//            if(!password.equals(password2)){
//                this.password2.setError(getString(R.string.error_field_password2));
//                focusView = this.password2;
//            }
//
//        }else{
//            params.add(new BasicNameValuePair("password_confirmation", md5Conver.md5(password)));
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            showProgress(true);

            String url =getServerUrls()+"sign-up";
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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }



    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void responseCame(boolean res, String message, JSONObject jObj) {

        if(res){
            try {
                SignInData signInData = objectMapper.readValue(jObj.toString(), SignInData.class);
                //setting.savePassword(password.getText().toString());
//                if(checkBoxSavePassword.isChecked())
//                    setting.savePassword_2(password.getText().toString());
                setting.saveToken(signInData.getToken());
                setting.saveLogin(signInData.getKlient().getLogin());
                setting.saveIdUser(""+signInData.getKlient().getId());
                DBSample.updateUserData(context, signInData.getKlient());

                DBSample.addSpravochniki(context, signInData.getSpravochniki()); //HERE WE ADD OUR SPRAVOCHNIKI INTO DB
                DBSample.cleanPhones(context);

                for(int i = 0; i < signInData.getKlient().getPhones().size(); i++){
                    DBSample.addPhone(context, signInData.getKlient().getPhones().get(i));
                }

                Intent infoDialog = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(infoDialog);
                SignUpActivity.this.finish();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Connection", e.getMessage());
            }
        }else {
            showProgress(false);
            myAsyncTask = null;
        }
        showProgress(false);

    }
}
