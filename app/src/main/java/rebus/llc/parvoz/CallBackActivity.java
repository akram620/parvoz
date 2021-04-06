package rebus.llc.parvoz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rebus.llc.parvoz.db.DBSample;
import rebus.llc.parvoz.others.MyAsyncTask;
import rebus.llc.parvoz.others.Settings;

import static rebus.llc.parvoz.others.Utilities.getServerUrls;


public class CallBackActivity extends AppCompatActivity  implements MyAsyncTask.ResponseCame{


    String[] list_id_numbers;
    String[] list_numbers;
    ArrayAdapter<String> adapter_numbers;
    Spinner spinnerNumbers;
    Context context;
    View newPhoneNumber;
    MyAsyncTask myAsyncTask;
    AutoCompleteTextView number;
    View mProgressView;
    View mLoginFormView;
    ObjectMapper objectMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_back);

        context = this;

        LinearLayout linearLayoutCencel = (LinearLayout) findViewById(R.id.linearLayoutCencel);
        LinearLayout linearLayoutSave = (LinearLayout) findViewById(R.id.linearLayoutSave);
        number = (AutoCompleteTextView) findViewById(R.id.number);

        spinnerNumbers = (Spinner) findViewById(R.id.spinnerPhone);
        newPhoneNumber = findViewById(R.id.newPhoneNumber);
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);

        ArrayList<String[]> arrayFrom = DBSample.getUserNumbers(context);

        list_id_numbers =  arrayFrom.get(0);//getResources().getStringArray(R.array.work_types);
        list_numbers =  arrayFrom.get(1);
        adapter_numbers = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list_numbers);
        adapter_numbers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumbers.setAdapter(adapter_numbers);

        spinnerNumbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        linearLayoutCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    }



    private void attemptSave() throws JsonProcessingException {
        if (myAsyncTask != null) {
            return;
        }
        // Reset errors.
        number.setError(null);

        String pnone_number   = number.getText().toString();

        boolean cancel = false;
        View focusView = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if(spinnerNumbers.getSelectedItemPosition() != list_numbers.length-1)
            params.add(new BasicNameValuePair("phone", list_numbers[spinnerNumbers.getSelectedItemPosition()]));
        else {
            if(spinnerNumbers.getSelectedItemPosition() == list_numbers.length-1){
                if (TextUtils.isEmpty(pnone_number)) {
                    number.setError("Укажите номер");
                    focusView = number;
                    cancel = true;
                }else{
                    params.add(new BasicNameValuePair("phone", pnone_number));
                }
            }else{
                cancel = true;
                focusView = spinnerNumbers;
                Toast.makeText(context, "Укажите номер!", Toast.LENGTH_SHORT).show();
            }

        }
        params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));
        params.add(new BasicNameValuePair("user_id", new Settings(context).getIdUser()));
        params.add(new BasicNameValuePair("token", new Settings(context).getToken()));



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            showProgress(true);

            String url = getServerUrls()+"obratnyj-zvonok";
            myAsyncTask = new MyAsyncTask(context, params, url, "post");
            myAsyncTask.setResponseListener(this);
            myAsyncTask.execute();
        }
    }
    @Override
    public void responseCame(boolean res, String message, JSONObject jObj) {

        if(res){
//            try {
                //MyTicketModel signInData = objectMapper.readValue(jObj.toString(), MyTicketModel.class);

                Toast.makeText(context, "Запрос на перезвон отправлен!", Toast.LENGTH_SHORT).show();


                CallBackActivity.this.finish();

//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("Connection", e.getMessage());
//            }
        }else {
            showProgress(false);
        }
        myAsyncTask = null;

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


}
