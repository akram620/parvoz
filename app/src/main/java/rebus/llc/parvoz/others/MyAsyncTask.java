package rebus.llc.parvoz.others;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MyAsyncTask  extends AsyncTask<Void, Void, Void> {

    private boolean Debuger = true;
    final String CON = "Connection";
    Settings settings;
    Encrypt  md5Conver;
    Context context;
    String   message = "";
    String content;
    String xml_request;
    public String comandUrl = "";

    List<NameValuePair> params;
    JSONObject jObj;
    private ResponseCame responseListenerCancelMeeting;

    String progress_message = "";

    boolean debuget = true;

    String kolichestvoVizitovVOcheredi;

    boolean response_bool;
    SQLiteDatabase dbSel;

    String id_meeting = "";

    String requestType;

    public MyAsyncTask(Context context, List<NameValuePair> params, String comandUrl, String requestType) {
        this.context   = context;
        this.comandUrl = comandUrl;
        this.params = params;
        this.requestType = requestType;

        response_bool = false;

        kolichestvoVizitovVOcheredi = "0";

        settings = new Settings(context);
        md5Conver = new Encrypt();

    }


    @Override
    protected void onPreExecute() {
        // progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);
        Log.d("Connection", "onPostExecute");

        if(responseListenerCancelMeeting != null){
            Log.d("Connection", "onPostExecute-2");

            responseListenerCancelMeeting.responseCame(response_bool, message, jObj);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        String result = "";
        boolean res = false;

        SendJsonRequest sendXmlReq = new SendJsonRequest();
        String urlServ = comandUrl;

        progress_message = "Запрос на получение данных";
        publishProgress();
        InputStream is = null;
        String json = "";
        HttpResponse httpResponse = null;

        if(requestType.equals("post")) {
            Log.d(CON, "requestType = post");

            try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urlServ);
                httpPost.setEntity(new UrlEncodedFormEntity(this.params));
                httpResponse = httpClient.execute(httpPost);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                if (Debuger)
                    Log.d(CON, "response.getStatusLine().getStatusCode() - " + httpResponse.getStatusLine().getStatusCode());

                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();
                    Log.d(CON, "JSON " + json);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    response_bool = true;
                    res = true;

                    // try parse the string to a JSON object
                    try {
                        jObj = new JSONObject(json);
                    } catch (JSONException e) {
                        Log.e(CON, "Error parsing data " + e.toString());

                    }

                }else{
                    message = ""+httpResponse.getStatusLine().getStatusCode();

                }

            } catch (Exception e) {
                Log.e(CON, "Error converting result " + e.toString());
            }
        }else {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            String paramString = URLEncodedUtils.format(this.params, "utf-8");
            String url = urlServ+"?" + paramString;
            HttpGet httpGet = new HttpGet(url);

            try {

                httpResponse = httpClient.execute(httpGet);

                if (Debuger)
                    Log.d(CON, "response.getStatusLine().getStatusCode() - " + httpResponse.getStatusLine().getStatusCode());

                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
                Log.d(CON, "JSON " + json);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    response_bool = true;
                    res = true;

                    // try parse the string to a JSON object
                    try {
                        jObj = new JSONObject(json);
                    } catch (JSONException e) {
                        Log.e(CON, "Error parsing data " + e    .toString());
                    }

                }else{
                    message = ""+httpResponse.getStatusLine().getStatusCode();
                }

            } catch (Exception e) {
            }

        }





        response_bool = res;

        return null;
    }


    public void insertIntoDB(String table_name, ContentValues contentValues) {
        dbSel.insert(table_name, null, contentValues);
    }

    public void updateIntoDB(String table_name, ContentValues contentValues) {
        dbSel.update(table_name, contentValues, "id_meeting= "+id_meeting, null);
    }

    public void setResponseListener(ResponseCame responseListener){
        this.responseListenerCancelMeeting = responseListener;
    }

    public interface ResponseCame{
        void responseCame(boolean res, String message, JSONObject jObj);
    }

    public static boolean isNetworkAvailable(Application application) {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager)application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
