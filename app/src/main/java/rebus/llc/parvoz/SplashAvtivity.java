package rebus.llc.parvoz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rebus.llc.parvoz.db.DBSample;
import rebus.llc.parvoz.models.SignInData;
import rebus.llc.parvoz.others.MyAsyncTask;
import rebus.llc.parvoz.others.Settings;

import static java.lang.Thread.sleep;
import static rebus.llc.parvoz.others.Utilities.getServerUrls;

public class SplashAvtivity extends AppCompatActivity  implements MyAsyncTask.ResponseCame {

    Settings settings;
    MyAsyncTask myAsyncTask;
    Context context;
    ObjectMapper objectMapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_avtivity);

        context = this;

        DBSample.createDb(this);

        settings = new Settings(this);

        ImageView imgLogo = (ImageView) findViewById(R.id.imageViewLogo);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        imgLogo.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }

                    getSpravochnikiList();
//                    Intent infoDialog = new Intent(SplashAvtivity.this, MainActivity.class);
//                    infoDialog.putExtra("activity", "SplashAvtivity");
//                    startActivity(infoDialog);
//                    SplashAvtivity.this.finish();



                } catch (InterruptedException e) {
//                    do nothing
                } finally {
//                    Splashscreen.this.finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }


    public void sendRequest(){

    }

    public void getSpravochnikiList(){

        if( myAsyncTask != null) return;

        String url = getServerUrls()+"poluchit-spravochniki";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        myAsyncTask = new MyAsyncTask(context, params, url, "get");
        myAsyncTask.setResponseListener(this);
        myAsyncTask.execute();
    }

    @Override
    public void responseCame(boolean res, String message, JSONObject jObj) {
        if(res){

                try {
                    objectMapper = new ObjectMapper();
                    SignInData signInData = objectMapper.readValue(jObj.toString(), SignInData.class);
                    DBSample.addSpravochniki(context, signInData.getSpravochniki());
                    myAsyncTask = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Connection", e.getMessage());
                }
        }else {
                myAsyncTask = null;
        }

        if(settings.getLogin().equals("")){
            Intent infoDialog = new Intent(SplashAvtivity.this, LoginActivity.class);
            startActivity(infoDialog);
            SplashAvtivity.this.finish();
        }else {
            Intent infoDialog = new Intent(SplashAvtivity.this, MainActivity.class);
            infoDialog.putExtra("activity", "SplashAvtivity");
            startActivity(infoDialog);
            SplashAvtivity.this.finish();
        }
    }
}
